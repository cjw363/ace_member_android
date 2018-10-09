package com.ace.member.main.home.ace_loan.loan_detail;

import android.content.Context;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.bean.ACELoanDetailBean;
import com.og.LibSession;
import com.og.http.IDataRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class AceLoanDetailPresenter extends BasePresenter implements AceLoanDetailContract.Presenter {
	private AceLoanDetailContract.View mView;

	@Inject
	public AceLoanDetailPresenter(AceLoanDetailContract.View view, Context context) {
		super(context);
		mView = view;
	}

	@Override
	public void getLoanPlanDetail(int id) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "loan");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("cmd", "getAceLoanPlayDetail");
		params.put("id", String.valueOf(id));
		submit(params, new IDataRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				try {
					ACELoanDetailBean bean = JsonUtil.jsonToBean(result, ACELoanDetailBean.class);
					mView.initView(bean);
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}

			@Override
			public void loadFailure(int errorCode, String result, String uniqueToken) {
				Utils.showToast(Utils.getString(R.string.no_data));
			}
		});
	}
}
