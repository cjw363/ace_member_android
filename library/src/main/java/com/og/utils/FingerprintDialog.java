package com.og.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import com.og.R;


public class FingerprintDialog extends DialogFragment {
	private Context context;
	private String message;
	private TextView mMessage;
	private CancellationSignal cancellationSignal;
	private View mView;

	public FingerprintListener callBack;
	private boolean isShowInputPassword = false;

	private Animation mShakeAnim;

	public static FingerprintDialog newInstance(Context context) {
		FingerprintDialog newFragment = new FingerprintDialog();
		newFragment.context = context;
		return newFragment;
	}

	@NonNull
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
		Window window = getDialog().getWindow();
		mView = inflater.inflate(R.layout.dlg_fingerprint, ((ViewGroup) window.findViewById(android.R.id.content)), false);
		window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		window.setLayout((int) (Utils.getScreenWidth() * 0.75), -2);
		TextView tvNegative = (TextView) mView.findViewById(R.id.tvNegative);

		if (message != null) {
			mMessage = ((TextView) mView.findViewById(R.id.fingerprint_message));
			mMessage.setText(message);
		}

		if (isShowInputPassword) {
			View lineView = mView.findViewById(R.id.dlg_view_line);
			lineView.setVisibility(View.VISIBLE);
			TextView textView = (TextView) mView.findViewById(R.id.tv_input_password);
			textView.setVisibility(View.VISIBLE);
			textView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					stopFingerprintAuth();
					callBack.clickInputPassword();
				}
			});
		}

		tvNegative.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				stopFingerprintAuth();
				callBack.cancelFingerprint();
			}
		});
		return mView;
	}

	public void stopFingerprintAuth() {
		cancellationSignal.cancel();
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setMessage(int mid) {
		this.message = (String) context.getText(mid);
	}

	public void setInputPassword(boolean isShow) {
		this.isShowInputPassword = isShow;
	}

	public View getView() {
		return mView;
	}

	public void startAuth(FingerprintListener listener) {
		this.callBack = listener;
		cancellationSignal = new CancellationSignal();
		mShakeAnim = AnimationUtils.loadAnimation(context, R.anim.shake_x);
		checkFingerprint();
	}

	private void checkFingerprint() {
		FingerprintManagerCompat manager = FingerprintManagerCompat.from(this.context);
		cancellationSignal = new CancellationSignal();
		manager.authenticate(null, 0, cancellationSignal, new FingerprintCallBack(), null);
	}

	private class FingerprintCallBack extends FingerprintManagerCompat.AuthenticationCallback {
		// 当出现错误的时候回调此函数，比如多次尝试都失败了的时候，errString是错误信息
		@Override
		public void onAuthenticationError(int errMsgId, CharSequence errString) {
			//认证多次失败后,不是按取消时的情况下
			if (!cancellationSignal.isCanceled()) {
				callBack.authFail();
			}
		}

		// 当指纹验证失败的时候会回调此函数，失败之后允许多次尝试，失败次数过多会停止响应一段时间然后再停止sensor的工作
		@Override
		public void onAuthenticationFailed() {
			mMessage = ((TextView) mView.findViewById(R.id.fingerprint_message));
			mMessage.setTextColor(context.getResources().getColor(R.color.lib_red));
			mMessage.setText(R.string.try_again);
			mMessage.startAnimation(mShakeAnim);
			callBack.authOneTimeFail();
		}

		// 当验证的指纹成功时会回调此函数，然后不再监听指纹sensor
		@Override
		public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
			callBack.authSuccess();
		}
	}

	public interface FingerprintListener {
		void cancelFingerprint(); //点击取消时

		void authSuccess(); //指纹认证成功

		void authFail(); //指纹多次认证失败

		void authOneTimeFail(); //认证一次失败

		void clickInputPassword();
	}

	public static class PartAuthResult implements FingerprintListener {
		@Override
		public void cancelFingerprint() {

		}

		@Override
		public void authSuccess() {

		}

		@Override
		public void authFail() {

		}

		@Override
		public void authOneTimeFail() {

		}

		@Override
		public void clickInputPassword() {

		}
	}

}
