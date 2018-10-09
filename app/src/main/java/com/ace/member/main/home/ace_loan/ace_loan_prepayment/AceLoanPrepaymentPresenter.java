package com.ace.member.main.home.ace_loan.ace_loan_prepayment;

import android.content.Context;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.bean.ACELoanBean;
import com.ace.member.bean.ACELoanPrepaymentBean;
import com.ace.member.utils.Session;
import com.og.LibSession;
import com.og.http.IDataRequestListener;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class AceLoanPrepaymentPresenter extends BasePresenter implements AceLoanPrepaymentContract.Presenter {
	private final AceLoanPrepaymentContract.View mView;
	private String mToken = "";

	@Inject
	public AceLoanPrepaymentPresenter(AceLoanPrepaymentContract.View view, Context context) {
		super(context);
		mView = view;
	}

	@Override
	public void getPrepayment(String str) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "loan");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("list", str);
		params.put("cmd", "getAceLoanPrepayment");
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				try {
					mToken = uniqueToken;
					ACELoanPrepaymentData data = JsonUtil.jsonToBean(result, ACELoanPrepaymentData.class);
					assert data != null;
					List<ACELoanPrepaymentBean> list = data.getList();
					Session.balanceList = data.getListBalance();
					mView.initListView(list);
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}
		});
	}

	@Override
	public void prepayment(String listStr) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "loan");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("list", listStr);
		params.put("unique_token", mToken);
		params.put("cmd", "saveAceLoanPrepayment");
		submit(params, new IDataRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				try {
					ACELoanBean bean = JsonUtil.jsonToBean(result, ACELoanBean.class);
					Utils.showToast(R.string.success);
					assert bean != null;
					mView.closeActivity(bean.getLoan()==0);
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}

			@Override
			public void loadFailure(int errorCode, String result, String uniqueToken) {
				Utils.showToast(R.string.msg_1709);
			}
		});
	}
}
