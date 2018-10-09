package com.ace.member.main.home.transfer;


import android.content.Context;

import com.ace.member.base.BasePresenter;
import com.ace.member.bean.CheckTransfer;
import com.ace.member.utils.Session;
import com.google.gson.reflect.TypeToken;
import com.og.LibSession;
import com.og.http.SimpleRequestListener;
import com.og.utils.DialogFactory;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class TransferPresenter extends BasePresenter implements TransferContract.TransferPresenter {

	private final TransferContract.TransferView mView;

	@Inject
	public TransferPresenter(Context context, TransferContract.TransferView mView) {
		super(context);
		this.mView = mView;
	}


	public void getRecentList() {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "transfer");
		params.put("_b", "aj");
		params.put("_s", Session.sSid);
		params.put("cmd", "getTransferRecent");

		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					if(result != null){
						List<TransferBean> list= JsonUtil.jsonToList(result,new TypeToken<List<TransferBean>>(){});
						mView.showRecentData(list);
					}

					DialogFactory.unblock();
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
					e.printStackTrace();
				}
			}
		}, false);
	}

	public void checkTransfer() {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "user");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("cmd", "checkTransfer");
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					CheckTransfer bean= JsonUtil.jsonToBean(result,CheckTransfer.class);
					assert bean != null;
					int isRelateMerchant = bean.getIsRelateMerchant();
					int isRelatePartner = bean.getIsRelatePartner();
					mView.showFunction(isRelateMerchant,isRelatePartner);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, false);
	}

}
