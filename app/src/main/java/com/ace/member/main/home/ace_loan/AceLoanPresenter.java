package com.ace.member.main.home.ace_loan;

import android.content.Context;
import android.util.Log;

import com.ace.member.base.BasePresenter;
import com.og.LibSession;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class AceLoanPresenter extends BasePresenter implements AceLoanContract.Presenter {
	private AceLoanContract.View mView;

	@Inject
	public AceLoanPresenter(AceLoanContract.View view, Context context) {
		super(context);
		mView = view;
	}

	@Override
	public void getAceLoan(boolean isLoading) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "loan");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("cmd", "getAceLoan");
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				try {
					LoanData data = JsonUtil.jsonToBean(result, LoanData.class);
					assert data != null;
					mView.initView(data.getLoanBean());
					mView.initLoanDetail(data.getLoanList());
					mView.showRefreshStatus(false);
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}
		}, isLoading);
	}
}
