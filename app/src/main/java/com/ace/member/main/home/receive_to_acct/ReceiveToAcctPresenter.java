package com.ace.member.main.home.receive_to_acct;

import android.content.Context;
import android.support.design.widget.Snackbar;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.bean.SingleBooleanBean;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.M;
import com.ace.member.utils.Session;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;


public class ReceiveToAcctPresenter extends BasePresenter implements ReceiveToAcctContract.Presenter {
	private ReceiveToAcctContract.View mView;
	private String mUniqueToken;
	private static final String mTokenAction = "RECEIVE_TO_DEPOSIT";


	@Inject
	public ReceiveToAcctPresenter(ReceiveToAcctContract.View view, Context context) {
		super(context);
		this.mView = view;
	}

	private void getToken() {
		super.getToken(mTokenAction, new IGetToken.SimpleGetToken() {
			@Override
			public void getTokenSuccess(String token) {
				mUniqueToken = token;
			}
		});
	}

	@Override
	public void chkSecurityCode(String securityCode) {
		if (securityCode == null || securityCode.length() != 6) {
			mView.invalidCode();
			Utils.showToast(R.string.input_accept_code);
			return;
		}

		Map<String, String> params = new HashMap<>();
		params.put("_b", "aj");
		params.put("_a", "transfer");
		params.put("cmd", "receiveToAcct");
		params.put("_s", Session.sSid);
		params.put("unique_token", mUniqueToken);
		params.put("security_code", securityCode);

		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					mUniqueToken = token;
					R2ADataBeen been = JsonUtil.jsonToBean(result, R2ADataBeen.class);
					mView.showSuccess(been);
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}

			@Override
			public void loadFailure(int errorCode, String result, String token) {
				mUniqueToken = token;
				mView.invalidCode();
				try {
					if(errorCode == com.og.M.MessageCode.ERR_505_FUNCTION_NOT_RUNNING){
						Utils.showToast(AppUtils.getFunctionPauseMsg(M.FunctionCode.FUNCTION_118_MEMBER_RECEIVE_TO_ACCOUNT), Snackbar.LENGTH_LONG);
						mView.notRunningFunction();
					}else{
						Utils.showToast(M.get(mContext, errorCode));
					}
				} catch (Exception e) {
					Utils.showToast(R.string.fail);
				}
			}
		});
	}

	public void start() {
		//getToken();

		Map<String, String> params = new HashMap<>();
		params.put("_s", Session.sSid);
		params.put("_a", "transfer");
		params.put("_b", "aj");
		params.put("cmd", "getR2AConfig");
		params.put("token_action", mTokenAction);
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					mUniqueToken = token;
					SingleBooleanBean bean = JsonUtil.jsonToBean(result, SingleBooleanBean.class);
					assert bean != null;
					if (!bean.getValue()) {
						Utils.showToast(AppUtils.getFunctionPauseMsg(M.FunctionCode.FUNCTION_118_MEMBER_RECEIVE_TO_ACCOUNT), Snackbar.LENGTH_LONG);
						mView.notRunningFunction();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, false);
	}

}
