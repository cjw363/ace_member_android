package com.ace.member.main.third_party.bill_payment.history;

import android.content.Context;
import android.widget.TextView;

import com.ace.member.base.BasePresenter;
import com.ace.member.bean.BillPaymentBean;
import com.ace.member.bean.BillPaymentListBean;
import com.ace.member.utils.Session;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class BillPaymentHistoryPresenter extends BasePresenter implements BillPaymentHistoryContract.presenter {

	private BillPaymentHistoryContract.view mView;

	@Inject
	public BillPaymentHistoryPresenter(BillPaymentHistoryContract.view view, Context context) {
		super(context);
		mView = view;
	}

	@Override
	public void getBillPaymentList(int page) {
		Map<String, String> p = new HashMap<>();
		p.put("_b", "aj");
		p.put("_a", "payment");
		p.put("_s", Session.sSid);
		p.put("cmd", "getBillPaymentHistoryList");
		p.put("page", String.valueOf(page));
		submit(p, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				try {
					BillPaymentListBean bean = JsonUtil.jsonToBean(result, BillPaymentListBean.class);
					if (bean == null) {
						return;
					}
					int total = bean.getTotal();
					int page = bean.getPage();
					int size = bean.getSize();
					int nextPage = Utils.nextPage(total, page, size);
					List<BillPaymentBean> list = bean.getList();
					if (list == null) list = new ArrayList<>();
					mView.addList(list,nextPage, total > size);
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}

			@Override
			public void loadFailure(int errorCode, String result, String uniqueToken) {
			}
		});
	}
}
