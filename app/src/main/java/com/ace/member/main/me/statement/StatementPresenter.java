package com.ace.member.main.me.statement;

import android.content.Context;
import android.text.TextUtils;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.bean.Statement;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.Session;
import com.og.http.SimpleRequestListener;
import com.og.utils.DialogFactory;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;


class StatementPresenter extends BasePresenter implements StatementContract.StatementPresenter {

	private final StatementContract.StatementView mView;

	private List<Statement> mData;
	private String mCurrentDate = "";
	private List<Statement> mRawData;

	@Inject
	StatementPresenter(Context context, StatementContract.StatementView mStatementView) {
		super(context);
		this.mView = mStatementView;
	}

	public void getStatement(String dateStart, String dateEnd, final String selectedCurrency, int page) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "balance");
		params.put("_b", "aj");
		params.put("_s", Session.sSid);
		params.put("cmd", "getStatement");
		if (!TextUtils.isEmpty(dateStart) && !TextUtils.isEmpty(dateEnd)) {
			params.put("date_start", dateStart);
			params.put("date_end", dateEnd);
		}
		params.put("currency", selectedCurrency);
		params.put("page", String.valueOf(page));

		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					StatementPageData statementPageData = JsonUtil.jsonToBean(result, StatementPageData.class);
					assert statementPageData != null;
					int total = statementPageData.getTotal();
					int page = statementPageData.getPage();
					int size = statementPageData.getSize();
					int nextPage = Utils.nextPage(total, page, size);
					List<Statement> statementList = statementPageData.getList();
					mCurrentDate = "";//清除数据
					String beginningCurrency = selectedCurrency.isEmpty() ? Utils.getString(R.string.usd) : selectedCurrency;
					String beginning = AppUtils.simplifyAmount(beginningCurrency, statementPageData.getBeginning());
					mView.setBeginning(beginningCurrency, beginning);

					if (page > 1) {
						if ((statementList.get(0).getDate()).equals(mRawData.get(mRawData.size() - 1).getDate())) {
							mRawData.addAll(statementList);
							mData.clear();
							mData.addAll(simplifyData(Utils.strToDouble(beginning), mRawData));
						} else {
							mData.addAll(simplifyData(Utils.strToDouble(mData.get(mData.size()-1).getBalance()), statementList));
						}
						mView.showNextStatementData(nextPage);
					} else {
						mRawData = new ArrayList<>();
						mRawData = Utils.cloneFrom(statementList);
						mData = new ArrayList<>();
						mData.addAll(simplifyData(Utils.strToDouble(beginning),statementList));
						mView.showStatementData(nextPage, total > size);
					}
					DialogFactory.unblock();

				} catch (Exception e) {
					FileUtils.addErrorLog(e);
					e.printStackTrace();
				}
			}
		}, true);
	}

	private List<Statement> simplifyData(double beginning, List<Statement> statementList) {
		int size = statementList.size();
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				Statement statement = statementList.get(i);
				String currentDate = statement.getDate();
				statement.setFlagSameDate(currentDate.equals(mCurrentDate));//检查是否相同日期
				if (!mCurrentDate.equals(currentDate)) {
					statement.setDateTitle(true);
					if (i >= 1) {
						statementList.get(i - 1).setDateEnd(true);
					}
				}
				mCurrentDate = currentDate;
				if (i == 0){
					String balance = beginning + Utils.strToDouble(statement.getAmount()) + "";
					statement.setBalance(balance);
				} else {
					String balance = Utils.strToDouble(statementList.get(i - 1).getBalance()) + Utils.strToDouble(statement.getAmount()) + "";
					statement.setBalance(balance);
				}
			}
			statementList.get(0).setDateTitle(true);
			statementList.get(size - 1).setDateEnd(true);
		}
		return statementList;
	}

	@Override
	public List<Statement> getStatementData() {
		return mData;
	}

}
