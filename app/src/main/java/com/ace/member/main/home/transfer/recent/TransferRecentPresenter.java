package com.ace.member.main.home.transfer.recent;

import android.content.Context;

import com.ace.member.base.BasePresenter;
import com.ace.member.bean.TransferRecent;
import com.og.LibSession;
import com.og.http.SimpleRequestListener;
import com.og.utils.DialogFactory;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class TransferRecentPresenter extends BasePresenter implements TransferRecentContract.TransferRecentPresenter {

	private final TransferRecentContract.TransferRecentView mView;
	public static final int SOURCE_1_MEMBER = 1;
	public static final int SOURCE_2_NON_MEMBER = 2;
	public static final int SOURCE_3_PARTNER = 3;
	public static final int SOURCE_4_MERCHANT = 4;

	@Inject
	public TransferRecentPresenter(Context context, TransferRecentContract.TransferRecentView mView) {
		super(context);
		this.mView = mView;
	}

	public void getTransferRecentList(int page,int source) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "transfer");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("page", String.valueOf(page));
		params.put("source", String.valueOf(source));
		if (source == SOURCE_1_MEMBER)  params.put("cmd", "getMemberTransferHistoryList");
		else if (source == SOURCE_2_NON_MEMBER) params.put("cmd", "getNonMemberTransferHistoryList");
		else if (source == SOURCE_3_PARTNER) params.put("cmd", "getPartnerTransferHistoryList");
		else if (source == SOURCE_4_MERCHANT) params.put("cmd", "getMerchantTransferHistoryList");

		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					TransferRecentBean bean=JsonUtil.jsonToBean(result,TransferRecentBean.class);
					if (bean == null) return;
					int total = bean.getTotal();
					int page = bean.getPage();
					int size = bean.getSize();
					int nextPage = Utils.nextPage(total, page, size);
					List<TransferRecent> list = bean.getList();
					if (list == null) list = new ArrayList<>();
					mView.addRecentList(nextPage, list, total > size);
					DialogFactory.unblock();
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}

			@Override
			public void loadFailure(int code,String result,String token) {
				mView.addRecentList(1, new ArrayList<TransferRecent>(), false);
			}
		}, false);
	}

}
