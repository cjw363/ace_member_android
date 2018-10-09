package com.ace.member.utils;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.ace.member.main.input_password.InputPasswordCallback;
import com.og.R;
import com.og.utils.CustomDialog;
import com.og.utils.FileUtils;
import com.og.utils.FingerprintDialog;
import com.og.utils.FingerprintHelper;

public class PayHelper {
	private AppCompatActivity mActivity;
	private CallBackAll mPayCallBack;

	private FingerprintHelper helper;

	public PayHelper(AppCompatActivity mActivity){
		this.mActivity = mActivity;
		helper = new FingerprintHelper.Pay(mActivity);
	}

	public void startPay(CallBackAll payCallBack){
		mPayCallBack = payCallBack;
		init();
	}

	public void init(){
		boolean useFingerprint = helper.getIsUseFingerprint();
		if(useFingerprint) payWithFingerprint();
		else payWithInputPassword();
	}

	private void payWithFingerprint(){
		String fingerprintPhone = helper.decryptFingerprintData();
		String dataPhone = helper.getData(FingerprintHelper.originalPhone);

		if(fingerprintPhone == null){
			helper.setIsUseFingerprint(false);
		}else if(!fingerprintPhone.isEmpty() && fingerprintPhone.equals(dataPhone)){
			try {
				final FingerprintDialog dialog = FingerprintDialog.newInstance(mActivity);
				dialog.setCancelable(false);
				dialog.setInputPassword(true);
				dialog.show(mActivity.getSupportFragmentManager(),"dialog_pay");

				dialog.startAuth(new FingerprintDialog.PartAuthResult() {

					@Override
					public void cancelFingerprint() {
						dialog.dismiss();
					}

					@Override
					public void authSuccess() {
						dialog.dismiss();
						mPayCallBack.paySuccess();
					}

					@Override
					public void authFail() {
						dialog.dismiss();
						payWithInputPassword();
					}

					@Override
					public void clickInputPassword() {
						dialog.dismiss();
						payWithInputPassword();
					}
				});
			} catch (Exception e) {
				FileUtils.addErrorLog(e);
			}
		}
	}

	private void payWithInputPassword(){
		InputPasswordUtil.inputPassword(mActivity,new InputPasswordCallback.SimpleInputPasswordCallbackListener(){
			@Override
			public void onSuccess() {
				mPayCallBack.paySuccess();
			}

			@Override
			public void onFail() {
				mPayCallBack.payFail();
			}

			@Override
			public void onLock() {
				//helper.setIsUseFingerprint(false);
				//helper.clearData();
				mPayCallBack.payFail();
			}

			@Override
			public void onCancel() {
				mPayCallBack.cancelPay();
			}
		});
	}

	public interface CallBackAll{
		void cancelPay();
		void paySuccess();
		void payFail();
	}
	
	public static class CallBackPart implements CallBackAll{

		@Override
		public void cancelPay() {

		}

		@Override
		public void paySuccess() {

		}

		@Override
		public void payFail() {

		}
	}
}
