package com.ace.member.main.home.ace_loan.return_loan;

import android.content.Context;

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

public class ReturnLoanAcePresenter extends BasePresenter implements ReturnLoanAceContract.Presenter {
	private final ReturnLoanAceContract.View mView;

	@Inject
	public ReturnLoanAcePresenter(ReturnLoanAceContract.View view, Context context) {
		super(context);
		mView = view;
	}

	@Override
	public void getLoanDetailList(String time,String time2,boolean loading) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "loan");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("cmd", "getReturnLoanList");
		params.put("from_time",time);
		params.put("to_time",time2);
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				try {
					AceLoanReturnData data = JsonUtil.jsonToBean(result, AceLoanReturnData.class);
					assert data != null;
					mView.initLoanDetail(data.getListDetail());
					mView.initView(data.getAmount(), data.getInterest());
					mView.showRefreshStatus(false);
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}
		},loading);
	}
}
