package com.ace.member.main.detail.bill_payment_detail;

import android.content.Context;

import com.ace.member.base.BasePresenter;
import com.ace.member.main.third_party.bill_payment.history_detail.BillPaymentDetailBean;
import com.ace.member.utils.Session;
import com.og.http.IDataRequestListener;
import com.og.utils.JsonUtil;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

class BillPaymentDetailPresenter extends BasePresenter implements BillPaymentDetailContract.presenter{
	private final BillPaymentDetailContract.view mView;
	@Inject
	BillPaymentDetailPresenter(BillPaymentDetailContract.view view, Context context) {
		super(context);
		mView=view;
	}

	@Override
	public void getDetailData(int id) {
		Map<String, String> p = new HashMap<>();
		p.put("_b", "aj");
		p.put("_a", "payment");
		p.put("_s", Session.sSid);
		p.put("cmd", "getBillPaymentDetail");
		p.put("id",String.valueOf(id));
		submit(p, new IDataRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				BillPaymentDetailBean bean= JsonUtil.jsonToBean(result,BillPaymentDetailBean.class);
				mView.initDetail(bean);
			}

			@Override
			public void loadFailure(int errorCode, String result, String uniqueToken) {

			}
		});
	}
}
