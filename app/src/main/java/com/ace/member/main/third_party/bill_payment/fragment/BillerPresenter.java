package com.ace.member.main.third_party.bill_payment.fragment;

import android.content.Context;
import android.util.Log;

import com.ace.member.base.BasePresenter;
import com.ace.member.bean.BillerBean;
import com.google.gson.reflect.TypeToken;
import com.og.LibSession;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class BillerPresenter extends BasePresenter implements BillerContract.Presenter {
	private final BillerContract.View mView;

	@Inject
	public BillerPresenter(BillerContract.View view,Context context) {
		super(context);
		mView=view;
	}

	@Override
	public void getBiller(int type) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "payment");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("cmd", "getBiller");
		params.put("type",String.valueOf(type));
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				try {
					List<BillerBean> list = JsonUtil.jsonToList(result, new TypeToken<List<BillerBean>>() {
					});
					mView.initListView(list);
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}
		},false);
	}
}
