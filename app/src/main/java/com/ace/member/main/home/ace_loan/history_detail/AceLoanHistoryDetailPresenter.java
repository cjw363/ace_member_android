package com.ace.member.main.home.ace_loan.history_detail;

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

public class AceLoanHistoryDetailPresenter extends BasePresenter implements AceLoanHistoryDetailContract.Presenter {

	private final AceLoanHistoryDetailContract.View mView;

	@Inject
	public AceLoanHistoryDetailPresenter(AceLoanHistoryDetailContract.View view, Context context) {
		super(context);
		mView = view;
	}

	@Override
	public void getLoanDetail(int id) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "loan");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("cmd", "getLoanAceTermDetail");
		params.put("id", String.valueOf(id));
		submit(params,new SimpleRequestListener(){
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				try {
					List<ACELoanRepayBean> list= JsonUtil.jsonToBean(result,new TypeToken<List<ACELoanRepayBean>>(){});
					mView.initLoanDetail(list);
				}catch (Exception e){
					FileUtils.addErrorLog(e);
				}
			}
		});
	}
}
