package com.ace.member.main.home.top_up.order_detail;

import android.content.Context;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.bean.TopUpOrder;
import com.og.LibSession;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

final class TopUpOrderDetailPresenter extends BasePresenter implements TopUpOrderDetailContract.Presenter {

	private final TopUpOrderDetailContract.View mView;

	@Inject
	public TopUpOrderDetailPresenter(Context context, TopUpOrderDetailContract.View view) {
		super(context);
		mView = view;
	}

	@Override
	public void getTopUpOrder(int id) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "user");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("cmd", "getTopUpOrder");
		params.put("id", String.valueOf(id));
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					TopUpOrder order = JsonUtil.jsonToBean(result, TopUpOrder.class);
					mView.setTopUpOrder(order);
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}

			@Override
			public void loadFailure(int code, String result, String token) {
				Utils.showToast(R.string.fail);
			}
		}, false);
	}
}
