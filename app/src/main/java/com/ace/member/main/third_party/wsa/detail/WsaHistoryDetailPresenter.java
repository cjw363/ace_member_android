package com.ace.member.main.third_party.wsa.detail;

import android.content.Context;

import com.ace.member.base.BasePresenter;
import com.ace.member.bean.WsaBill;
import com.og.LibSession;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;


public class WsaHistoryDetailPresenter extends BasePresenter implements WsaHistoryDetailContract.Presenter {
	private final WsaHistoryDetailContract.View mView;
	private int mId;
	private WsaBill mWsaBill;

	@Inject
	public WsaHistoryDetailPresenter(WsaHistoryDetailContract.View view, Context context) {
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
		params.put("cmd", "getWsaBill");
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					mWsaBill = JsonUtil.jsonToBean(result, WsaBill.class);
					if (mWsaBill != null) mView.setBill(mWsaBill);
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}
		});
	}
}
