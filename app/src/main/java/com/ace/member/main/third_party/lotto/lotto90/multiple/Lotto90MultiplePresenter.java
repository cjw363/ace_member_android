package com.ace.member.main.third_party.lotto.lotto90.multiple;

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

import static com.ace.member.R.string.balance;


public class Lotto90MultiplePresenter extends LottoPresenter implements Lotto90MultipleContract.LottoMultipleContractPresenter {
	private final Lotto90MultipleContract.Lotto90MultipleContractView mView;
	private String mUniqueToken;

	@Inject
	public Lotto90MultiplePresenter(Context context, Lotto90MultipleContract.Lotto90MultipleContractView view) {
		super(context);
		mView = view;
	}

	public void getGameInfo() {
		Map<String, String> p = new HashMap<>();
		p.put("_b", "aj");
		p.put("_a", "lotto90");
		p.put("cmd", "getProductInfo");
		p.put("_s", Session.sSid);
		p.put("type", AppGlobal.LOTTO_BUY_TICKET_12_TYPE + "");
		submit(p, new IDataRequestListener() {

			@Override
			public void loadSuccess(String result, String uniqueToken) {
				try {
					mUniqueToken = uniqueToken;
					LottoProductInfo lottoProductInfo = JsonUtil.jsonToBean(result, LottoProductInfo.class);
					assert lottoProductInfo!=null;
					mView.initGame(balance, lottoProductInfo.getBetAmount(), lottoProductInfo.getMaxNumber(), lottoProductInfo.getProduct());
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
				try {
					mUniqueToken = uniqueToken;
					mUniqueToken = uniqueToken;
					LottoResult lottoResult = JsonUtil.jsonToBean(result, LottoResult.class);
					assert lottoResult != null;
					Session.balanceList = lottoResult.getBalance();
					showSaveSuccess(lottoResult.getAccepted());
					mView.updateInfo(balance);
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
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
