package com.ace.member.main.third_party.lotto.lotto90.single;

import android.content.Context;

import com.ace.member.base.LottoPresenter;
import com.ace.member.bean.LottoProductInfo;
import com.ace.member.bean.LottoResult;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.M;
import com.ace.member.utils.Session;
import com.og.http.IDataRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class Lotto90SinglePresenter extends LottoPresenter implements Lotto90SingleContract.Lotto90SingleContractPresenter {
	private final Lotto90SingleContract.Lotto90SingleContractView mView;
	private String mUniqueToken;

	@Inject
	public Lotto90SinglePresenter(Context context, Lotto90SingleContract.Lotto90SingleContractView view) {
		super(context);
		mView = view;
	}

	public void closeBettingView() {
		mView.closeBetting();
	}

	public void againBetting() {
		mView.againBetting();
	}

	public void getProduct() {
		Map<String, String> p = new HashMap<>();
		p.put("_b", "aj");
		p.put("_a", "lotto90");
		p.put("cmd", "getProductInfo");
		p.put("_s", Session.sSid);
		p.put("type", AppGlobal.LOTTO_BUY_TICKET_11_TYPE + "");
		submit(p, new IDataRequestListener() {

			@Override
			public void loadSuccess(String result, String uniqueToken) {
				try {
					mUniqueToken = uniqueToken;
					LottoProductInfo lottoProductInfo = JsonUtil.jsonToBean(result, LottoProductInfo.class);
					assert lottoProductInfo != null;
					Session.balanceList = lottoProductInfo.getBalance();
					mView.initInfo(lottoProductInfo.getBetAmount(), lottoProductInfo.getProduct());
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}

			@Override
			public void loadFailure(int errorCode, String result, String uniqueToken) {

			}
		});
	}

	public void placeOrders() {
		Map<String, String> p = mView.getParams();
		p.put("_b", "aj");
		p.put("_a", "lotto90");
		p.put("cmd", "placeOrders");
		p.put("unique_token", mUniqueToken);
		p.put("_s", Session.sSid);
		submit(p, new IDataRequestListener() {

			@Override
			public void loadSuccess(String result, String uniqueToken) {
				try {
					mUniqueToken = uniqueToken;
					LottoResult lottoResult = JsonUtil.jsonToBean(result, LottoResult.class);
					assert lottoResult != null;
					Session.balanceList = lottoResult.getBalance();
					showSaveSuccess(lottoResult.getAccepted());
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}

			@Override
			public void loadFailure(int errorCode, String result, String uniqueToken) {
				try {
					mUniqueToken = uniqueToken;
					Utils.showToast(M.get(mContext, errorCode));
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}
		});
	}
}
