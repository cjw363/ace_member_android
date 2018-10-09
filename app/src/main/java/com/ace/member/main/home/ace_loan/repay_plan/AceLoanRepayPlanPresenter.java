package com.ace.member.main.home.ace_loan.repay_plan;

import android.content.Context;

import com.ace.member.base.BasePresenter;
import com.ace.member.bean.ACELoanRepayBean;
import com.google.gson.reflect.TypeToken;
import com.og.LibSession;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class AceLoanRepayPlanPresenter extends BasePresenter implements AceLoanRepayPlanContract.Presenter {

	private final AceLoanRepayPlanContract.View mView;
	@Inject
	public AceLoanRepayPlanPresenter(AceLoanRepayPlanContract.View view,Context context) {
		super(context);
		mView=view;
	}

	@Override
	public void getData(int id) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "loan");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("cmd", "getAceLoanRepay");
		params.put("id",String.valueOf(id));
		submit(params,new SimpleRequestListener(){
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				try {
					AceLoanRepayLoanData data=JsonUtil.jsonToBean(result,AceLoanRepayLoanData.class);
					assert data != null;
					mView.initView(data.getCapitalAmount(),data.getPlanInterestAmount(),data.getInterestRate(),data.getList());
				}catch (Exception e){
					FileUtils.addErrorLog(e);
				}
			}
		});
	}
}
