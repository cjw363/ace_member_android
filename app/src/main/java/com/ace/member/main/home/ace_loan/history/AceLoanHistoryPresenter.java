package com.ace.member.main.home.ace_loan.history;

import android.content.Context;

import com.ace.member.base.BasePresenter;
import com.ace.member.bean.ACELoanDetailBean;
import com.ace.member.bean.FaceValue;
import com.google.gson.reflect.TypeToken;
import com.og.LibSession;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class AceLoanHistoryPresenter extends BasePresenter implements AceLoanHistoryContract.Presenter {

	private final AceLoanHistoryContract.View mView;

	@Inject
	public AceLoanHistoryPresenter(AceLoanHistoryContract.View view, Context context) {
		super(context);
		mView = view;
	}

	@Override
	public void getLoadDetailList(String from, String to, int page) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "loan");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("cmd", "getLoanAceDetailList");
		params.put("from_time", from);
		params.put("to_time", to);
		params.put("page", String.valueOf(page));
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				try {
					AceLoanHistoryData data = JsonUtil.jsonToBean(result, AceLoanHistoryData.class);
					assert data != null;
					int total = data.getTotal();
					int page = data.getPage();
					int size = data.getSize();
					int nextPage = Utils.nextPage(total, page, size);
					List<ACELoanDetailBean> list = data.getList();
					if (page > 1) {
						mView.nextPage(list, nextPage);
					} else {
						mView.initView(list, nextPage, total > size);
					}

				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}
		});
	}
}
