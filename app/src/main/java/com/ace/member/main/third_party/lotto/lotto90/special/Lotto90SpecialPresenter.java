package com.ace.member.main.third_party.lotto.lotto90.special;


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

public class Lotto90SpecialPresenter extends LottoPresenter implements Lotto90SpecialContract.Lotto90SpecialContractPresenter {

	private final Lotto90SpecialContract.Lotto90SpecialContractView mView;
	private String mUniqueToken;

	@Inject
	public Lotto90SpecialPresenter(Context context, Lotto90SpecialContract.Lotto90SpecialContractView view) {
		super(context);
		mView = view;
	}

	public void getProduct() {
		Map<String, String> p = new HashMap<>();
		p.put("_b", "aj");
		p.put("_a", "lotto90");
		p.put("cmd", "getProductInfo");
		p.put("_s", Session.sSid);
		p.put("type", AppGlobal.LOTTO_BUY_TICKET_21_TYPE + "");
		submit(p, new IDataRequestListener() {

			@Override
			public void loadSuccess(String result, String uniqueToken) {
				try {
					mUniqueToken = uniqueToken;
					LottoProductInfo lottoProductInfo = JsonUtil.jsonToBean(result, LottoProductInfo.class);
					assert lottoProductInfo != null;
					Session.balanceList = lottoProductInfo.getBalance();
					mView.initInfo(lottoProductInfo.getBetAmount(), lottoProductInfo.getMaxTimes(), lottoProductInfo
						.getProduct());
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}

			@Override
			public void loadFailure(int errorCode, String result, String uniqueToken) {

			}
		});
	}


	public void placeOrder() {
		Map<String, String> p = mView.getParams();
		p.put("_b", "aj");
		p.put("_a", "lotto90");
		p.put("cmd", "placeOrders");
		p.put("unique_token", mUniqueToken);
		p.put("_s", Session.sSid);
		submit(p, new IDataRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				mUniqueToken = uniqueToken;
				LottoResult lottoResult = JsonUtil.jsonToBean(result, LottoResult.class);
				assert lottoResult != null;
				Session.balanceList = lottoResult.getBalance();
				showSaveSuccess(lottoResult.getAccepted());
			}

			@Override
			public void loadFailure(int errorCode, String result, String uniqueToken) {
				mUniqueToken = uniqueToken;
				Utils.showToast(M.get(mContext, errorCode));
			}
		});
	}

	public void closeBettingView() {
		mView.closeBetting();
	}

	public void againBetting() {
		mView.againBetting();
	}
}
