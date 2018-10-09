package com.ace.member.main.home.ace_loan.repay_plan_list;

import android.content.Context;

import com.ace.member.base.BasePresenter;
import com.ace.member.bean.ACELoanRepayBean;
import com.google.gson.reflect.TypeToken;
import com.og.LibSession;
import com.og.http.IDataRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class AceRepayPlanListPresenter extends BasePresenter implements AceRepayPlanListContract.Presenter {
	private final AceRepayPlanListContract.View mView;

	@Inject
	public AceRepayPlanListPresenter(AceRepayPlanListContract.View view, Context context) {
		super(context);
		mView = view;
	}

	@Override
	public void getRepayPlanList() {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "loan");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("cmd", "getAceLoanRepayList");
		submit(params, new IDataRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				try {
					List<ACELoanRepayBean> list= JsonUtil.jsonToList(result,new TypeToken<List<ACELoanRepayBean>>(){});
					mView.initListView(list);
				}catch (Exception e){
					FileUtils.addErrorLog(e);
				}
			}

			@Override
			public void loadFailure(int errorCode, String result, String uniqueToken) {

			}
		});
	}
}
