package com.ace.member.main.currency.flow;

import android.content.Context;

import com.ace.member.base.BasePresenter;
import com.ace.member.bean.BalanceFlow;
import com.ace.member.bean.BalanceFlowPageData;
import com.ace.member.bean.PageBaseBean;
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


public class FlowPresenter extends BasePresenter implements FlowContract.Presenter {
	private final FlowContract.View mView;

	@Inject
	public FlowPresenter(FlowContract.View view, Context context) {
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
		params.put("_a", "balance");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("currency",mView.getCurrency());
		params.put("page", String.valueOf(page));
		params.put("cmd", "getBalanceFlowList");
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					BalanceFlowPageData data= JsonUtil.jsonToBean(result,BalanceFlowPageData.class);
					assert data != null;
					int total = data.getTotal();
					int page = data.getPage();
					int size = data.getSize();
					int nextPage = Utils.nextPage(total, page, size);
					List<BalanceFlow> list=data.getList();
					if (list == null) list = new ArrayList<>();
					mView.addList(nextPage, list, total > size);
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}

			@Override
			public void loadFailure(int code, String result, String token) {
				mView.addList(1, new ArrayList<BalanceFlow>(), false);
			}
		}, false);
	}
}
