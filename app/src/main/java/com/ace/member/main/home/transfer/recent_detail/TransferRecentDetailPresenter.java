package com.ace.member.main.home.transfer.recent_detail;

import android.content.Context;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.bean.TransferRecent;
import com.og.LibSession;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class TransferRecentDetailPresenter extends BasePresenter implements TransferRecentDetailContract.TransferRecentDetailPresenter {

	private final TransferRecentDetailContract.TransferRecentDetailView mView;

	@Inject
	public TransferRecentDetailPresenter(Context context, TransferRecentDetailContract.TransferRecentDetailView mView) {
		super(context);
		this.mView = mView;
	}

	public void getRecentDetail(int id, int source) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "transfer");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("cmd", "getTransferRecentDetail");
		params.put("id", String.valueOf(id));
		params.put("source", String.valueOf(source));
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					TransferRecent recent = JsonUtil.jsonToBean(result, TransferRecent.class);
					mView.setRecentDetail(recent);
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
