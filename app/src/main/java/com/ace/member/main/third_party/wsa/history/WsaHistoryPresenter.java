package com.ace.member.main.third_party.wsa.history;

import android.content.Context;

import com.ace.member.base.BasePresenter;
import com.ace.member.bean.EdcBill;
import com.ace.member.bean.EdcHistoryBean;
import com.ace.member.bean.WsaBill;
import com.ace.member.bean.WsaHistoryBean;
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


public class WsaHistoryPresenter extends BasePresenter implements WsaHistoryContract.Presenter {
	private final WsaHistoryContract.View mView;

	@Inject
	public WsaHistoryPresenter(WsaHistoryContract.View view, Context context) {
		super(context);
		this.mView = view;
	}

	@Override
	public void getWsaBillList(int page) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "payment");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("cmd", "getWsaBillList");
		params.put("page", String.valueOf(page));
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					WsaHistoryBean wsaHistoryBean = JsonUtil.jsonToBean(result, WsaHistoryBean.class);
					if (wsaHistoryBean == null) return;
					int total = wsaHistoryBean.getTotal();
					int page = wsaHistoryBean.getPage();
					int size = wsaHistoryBean.getSize();
					int nextPage = Utils.nextPage(total, page, size);
					List<WsaBill> list = wsaHistoryBean.getList();
					if (list == null) list = new ArrayList<>();
					mView.addList(nextPage, list, total > size);
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}

			@Override
			public void loadFailure(int code, String result, String token) {
				mView.addList(1, new ArrayList<WsaBill>(), false);
			}
		}, false);
	}
}
