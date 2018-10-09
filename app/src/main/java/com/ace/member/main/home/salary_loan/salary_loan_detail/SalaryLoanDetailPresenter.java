package com.ace.member.main.home.salary_loan.salary_loan_detail;

import android.content.Context;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.bean.SalaryLoanFlow;
import com.og.LibSession;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

final class SalaryLoanDetailPresenter extends BasePresenter implements SalaryLoanDetailContract.Presenter {

	private final SalaryLoanDetailContract.View mView;

	@Inject
	SalaryLoanDetailPresenter(Context context, SalaryLoanDetailContract.View view) {
		super(context);
		mView = view;
	}

	@Override
	public void getSalaryLoanDetail(int id) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "loan");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("cmd", "getSalaryLoanDetail");
		params.put("flow_id", String.valueOf(id));
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					SalaryLoanFlow salaryLoanFlow = JsonUtil.jsonToBean(result, SalaryLoanFlow.class);
					mView.setDetail(salaryLoanFlow);
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
}
