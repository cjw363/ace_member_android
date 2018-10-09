package com.ace.member.main.home.salary_loan.salary_loan_history;

import android.content.Context;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.bean.SalaryLoanFlow;
import com.og.LibSession;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

final class SalaryLoanHistoryPresenter extends BasePresenter implements SalaryLoanHistoryContract.Presenter {

	private final SalaryLoanHistoryContract.View mView;
	private List<SalaryLoanFlow> mList = new ArrayList<>();

	@Inject
	SalaryLoanHistoryPresenter(Context context, SalaryLoanHistoryContract.View view) {
		super(context);
		mView = view;
	}

	@Override
	public void getSalaryLoanHistory(int page) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "loan");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("cmd", "getSalaryLoanHistory");
		params.put("page", String.valueOf(page));
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					SalaryLoanHistoryPageBean bean = JsonUtil.jsonToBean(result, SalaryLoanHistoryPageBean.class);
					if (bean == null) return;
					int total = bean.getTotal();
					int page = bean.getPage();
					int size = bean.getSize();
					int nextPage = Utils.nextPage(total, page, size);
					List<SalaryLoanFlow> list = bean.getList();
					if (page > 1) {
						setData(list);
						mView.showNextPageData(nextPage);
					} else {
						mList.clear();
						setData(list);
						mView.showData(nextPage, total > size);
					}
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}

			@Override
			public void loadFailure(int code, String result, String token) {
				Utils.showToast(R.string.fail);
			}
		}, false);
	}

	private void setData(List<SalaryLoanFlow> list){
		try {
			if (list != null) {
				int len = list.size();
				for (int i = 0; i < len; i++) {
					SalaryLoanFlow bean = list.get(i);
					mList.add(bean);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<SalaryLoanFlow> getHistoryData() {
		return mList;
	}


}
