package com.ace.member.main.third_party.edc.history;

import android.content.Context;

import com.ace.member.base.BasePresenter;
import com.ace.member.bean.EdcBill;
import com.ace.member.bean.EdcHistoryBean;
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


public class EdcHistoryPresenter extends BasePresenter implements EdcHistoryContract.Presenter {
	private final EdcHistoryContract.View mView;

	@Inject
	public EdcHistoryPresenter(EdcHistoryContract.View view, Context context) {
		super(context);
		this.mView = view;
	}

	@Override
	public void getEdcWsaBillList(int page) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "payment");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("cmd", "getEdcBillList");
		params.put("page", String.valueOf(page));
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					EdcHistoryBean edcHistoryBean = JsonUtil.jsonToBean(result, EdcHistoryBean.class);
					if (edcHistoryBean == null) {
						return;
					}
					int total = edcHistoryBean.getTotal();
					int page = edcHistoryBean.getPage();
					int size = edcHistoryBean.getSize();
					int nextPage = Utils.nextPage(total, page, size);
					List<EdcBill> list = edcHistoryBean.getList();
					if (list == null) list = new ArrayList<>();
					mView.addList(nextPage, list, total > size);
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}

			@Override
			public void loadFailure(int code, String result, String token) {
				mView.addList(1, new ArrayList<EdcBill>(), false);
			}
		}, false);
	}
}
