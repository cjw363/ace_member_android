package com.ace.member.main.home.top_up.recent_order;

import android.content.Context;

import com.ace.member.base.BasePresenter;
import com.ace.member.bean.TopUpOrder;
import com.google.gson.reflect.TypeToken;
import com.og.LibSession;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

final class RecentOrderPresenter extends BasePresenter implements RecentOrderContract.Presenter {

	private final RecentOrderContract.View mView;

	@Inject
	public RecentOrderPresenter(Context context, RecentOrderContract.View view) {
		super(context);
		mView = view;
	}

	@Override
	public void start() {
		getTopUpOrderList(1);
	}

	@Override
	public void getTopUpOrderList(int page) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "user");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("page", String.valueOf(page));
		params.put("cmd", "getTopUpOrderList");
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					JSONObject orderData = new JSONObject(result);
					int total = orderData.getInt("total");
					int page = orderData.getInt("page");
					int size = orderData.getInt("size");
					int nextPage = Utils.nextPage(total, page, size);
					List<TopUpOrder> list = JsonUtil.jsonToList(orderData.optString("list"), new TypeToken<List<TopUpOrder>>() {
					});
					if (list == null) list = new ArrayList<>();
					mView.addOrderList(nextPage, list, total > size);
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}

			@Override
			public void loadFailure(int code, String result, String token) {
				mView.addOrderList(1, new ArrayList<TopUpOrder>(), false);
			}
		}, false);
	}
}
