package com.ace.member.main.currency.coupon.base_coupon;

import android.content.Context;

import com.ace.member.base.BasePresenter;
import com.ace.member.bean.Coupon;
import com.ace.member.bean.CouponPageData;
import com.og.LibSession;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

final class BaseCouponPresenter extends BasePresenter implements BaseCouponContract.Presenter {

	private final BaseCouponContract.View mView;

	@Inject
	public BaseCouponPresenter(Context context, BaseCouponContract.View view) {
		super(context);
		mView = view;
	}

	@Override
	public void start() {
	}

	@Override
	public void getList(int page, int status) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "user");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("page", String.valueOf(page));
		params.put("cmd", "getCoupon");
		params.put("status",String.valueOf(status));
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					CouponPageData data = JsonUtil.jsonToBean(result, CouponPageData.class);
					assert data != null;
					int total = data.getTotal();
					int page = data.getPage();
					int size = data.getSize();
					int nextPage = Utils.nextPage(total, page, size);
					List<Coupon> list = data.getList();
					if (list == null) list = new ArrayList<>();
					mView.addList(nextPage, list, total > size);
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}

			@Override
			public void loadFailure(int code, String result, String token) {
				mView.addList(1, new ArrayList<Coupon>(), false);
			}
		}, false);
	}

}
