package com.ace.member.main.detail.transfer_detail;


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

import javax.inject.Inject;

class TransferDetailPresenter extends BasePresenter implements TransferDetailContract.TransferDetailPresenter {

	private final TransferDetailContract.TransferDetailView mView;

	@Inject
	TransferDetailPresenter(Context context, TransferDetailContract.TransferDetailView view) {
		super(context);
		mView = view;
	}

	@Override
	public void getTransferAmountData(HashMap<String, String> params) {
		params.put("_a", "transfer");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("cmd", "getTransferRecentDetail");
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					TransferRecent data = JsonUtil.jsonToBean(result, TransferRecent.class);
					assert data != null;
					mView.setAmountDetail(data);
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}

			@Override
			public void loadFailure(int code, String result, String token) {
				Utils.showToast(R.string.fail);
			}
		});
	}

	@Override
	public void getTransferFeeData(HashMap<String, String> params) {
		params.put("_a", "transfer");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("cmd", "getTransferRecentDetail");
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					TransferRecent data = JsonUtil.jsonToBean(result, TransferRecent.class);
					assert data != null;
					mView.setFeeDetail(data);
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}

			@Override
			public void loadFailure(int code, String result, String token) {
				Utils.showToast(R.string.fail);
			}
		});
	}

	@Override
	public void getTransferInData(HashMap<String, String> params) {
		params.put("_a", "transfer");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("cmd", "getTransferRecentDetail");
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					TransferRecent data = JsonUtil.jsonToBean(result, TransferRecent.class);
					assert data != null;
					mView.setInDetail(data);
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}

			@Override
			public void loadFailure(int code, String result, String token) {
				Utils.showToast(R.string.fail);
			}
		});
	}
}
