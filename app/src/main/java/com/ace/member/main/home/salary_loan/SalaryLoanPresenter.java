package com.ace.member.main.home.salary_loan;


import android.content.Context;
import android.support.design.widget.Snackbar;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.og.LibSession;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

class SalaryLoanPresenter extends BasePresenter implements SalaryLoanContract.SalaryLoanPresenter {

	private final SalaryLoanContract.SalaryLoanView mView;

	@Inject
	SalaryLoanPresenter(Context context, SalaryLoanContract.SalaryLoanView view) {
		super(context);
		mView = view;
	}

	@Override
	public void getSalaryLoanData(boolean isLoading) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "loan");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("cmd", "getSalaryLoan");
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				try {
					SalaryLoanData data = JsonUtil.jsonToBean(result, SalaryLoanData.class);
					assert data != null;
					if (data.getCreditLoan() != null) {
						mView.setData(data);
					} else {
						mView.hideInfo();
						mView.setBtnLoanEnable(false);
						mView.setBtnReturnLoanEnable(false);
					}
					mView.showRefreshStatus(false);
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
					e.printStackTrace();
				}
			}
		}, isLoading);
	}

}
