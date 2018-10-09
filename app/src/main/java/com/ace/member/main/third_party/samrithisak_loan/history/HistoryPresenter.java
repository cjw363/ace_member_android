package com.ace.member.main.third_party.samrithisak_loan.history;

import android.content.Context;

import com.ace.member.base.BasePresenter;
import com.ace.member.bean.PartnerLoanFlow;
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


public class HistoryPresenter extends BasePresenter implements HistoryContract.Presenter {
	private final HistoryContract.View mView;

	@Inject
	public HistoryPresenter(HistoryContract.View view, Context context) {
		super(context);
		this.mView = view;
	}

	@Override
	public void start() {
		getList(1);
	}

	@Override
	public void getList(int page) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "loan");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("page", String.valueOf(page));
		params.put("cmd", "getPartnerLoanHistory");
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					JSONObject orderData = new JSONObject(result);
					int total = orderData.getInt("total");
					int page = orderData.getInt("page");
					int size = orderData.getInt("size");
					int nextPage = Utils.nextPage(total, page, size);
					List<PartnerLoanFlow> list = JsonUtil.jsonToList(orderData.optString("list"), new TypeToken<List<PartnerLoanFlow>>() {});
					if (list == null) list = new ArrayList<>();
					mView.addList(nextPage, list, total > size);
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}

			@Override
			public void loadFailure(int code, String result, String token) {
				mView.addList(1, new ArrayList<PartnerLoanFlow>(), false);
			}
		}, false);
	}
}
