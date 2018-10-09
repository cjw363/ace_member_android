package com.ace.member.main.me.payment_history;

import android.content.Context;

import com.ace.member.base.BasePresenter;
import com.ace.member.bean.PaymentHistoryBean;
import com.ace.member.utils.AppGlobal;
import com.og.LibSession;
import com.og.http.SimpleRequestListener;
import com.og.utils.DialogFactory;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class PaymentHistoryPresenter extends BasePresenter implements PaymentHistoryContract.PaymentHistoryPresenter {
	private final PaymentHistoryContract.PaymentHistoryView mView;

	private List<PaymentHistoryBean> mList = new ArrayList<>();
	private String mFlagSameDate = "";

	@Inject
	public PaymentHistoryPresenter(Context context, PaymentHistoryContract.PaymentHistoryView mView) {
		super(context);
		this.mView = mView;
		mNetClient.setUnblock(true);
	}

	@Override
	public void getList(String dateStart, String dateEnd, String type, int page) {

		Map<String, String> p = new HashMap<>();
		p.put("_a", "payment");
		p.put("_b", "aj");
		p.put("_s", LibSession.sSid);
		p.put("from", dateStart);
		p.put("to", dateEnd);
		p.put("type", "");
		if (type != null) {
			if (type.equals("Water")) {
				p.put("type", String.valueOf(AppGlobal.PAYMENT_TYPE_2_WSA));
			} else if (type.equals("Electricity")) {
				p.put("type", String.valueOf(AppGlobal.PAYMENT_TYPE_1_EDC));
			}
		}
		p.put("page", String.valueOf(page));
		p.put("cmd", "getPaymentHistory");
		submit(p, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					PaymentHistoryListBean bean= JsonUtil.jsonToBean(result,PaymentHistoryListBean.class);
					assert bean != null;
					int total = bean.getTotal();
					int page = bean.getPage();
					int size = bean.getSize();
					int nextPage = Utils.nextPage(total, page, size);
					if (page > 1) {
						formatData(bean.getList());
						mView.showNextList(nextPage);
					} else {
						mFlagSameDate = "";
						mList.clear();
						formatData(bean.getList());
						mView.showList(nextPage, total > size);
					}
					DialogFactory.unblock();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, false);
	}

	private void formatData(List<PaymentHistoryBean> list) {
		try {
			String date = "", onlyTime = "", strTime;
			if (list != null) {
				for (int i = 0, len = list.size(); i < len; i++) {
					PaymentHistoryBean bean=list.get(i);
					strTime = bean.getTime();;
					if (strTime != null) {
						date = strTime.substring(0, 7);
						onlyTime = strTime.substring(5, 16);
					}
					bean.setDate(date);
					bean.setTime(onlyTime);
					bean.setHeader(date.equals(mFlagSameDate));
					mFlagSameDate = date;
					mList.add(bean);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<PaymentHistoryBean> getData() {
		return mList;
	}

}
