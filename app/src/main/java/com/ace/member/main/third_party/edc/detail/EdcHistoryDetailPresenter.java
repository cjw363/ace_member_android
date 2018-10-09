package com.ace.member.main.third_party.edc.detail;

import android.content.Context;

import com.ace.member.base.BasePresenter;
import com.ace.member.bean.EdcBill;
import com.og.LibSession;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;


public class EdcHistoryDetailPresenter extends BasePresenter implements EdcHistoryDetailContract.Presenter {
	private final EdcHistoryDetailContract.View mView;
	private EdcBill mEdcWsaBill;
	private int mId;

	@Inject
	public EdcHistoryDetailPresenter(EdcHistoryDetailContract.View view, Context context) {
		super(context);
		this.mView = view;
	}

	@Override
	public void start(int id) {
		mId = id;
		getEdcWsaBill();
	}

	private void getEdcWsaBill() {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "payment");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("id", String.valueOf(mId));
		params.put("cmd", "getEdcBill");
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					mEdcWsaBill = JsonUtil.jsonToBean(result, EdcBill.class);
					if (mEdcWsaBill != null) mView.setBill(mEdcWsaBill);
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}
		});
	}
}
