package com.ace.member.login_password;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.M;
import com.ace.member.utils.Session;
import com.og.LibSession;
import com.og.http.SimpleRequestListener;
import com.og.utils.CustomDialog;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class LoginPasswordPresenter extends BasePresenter implements LoginPasswordContract.ResetPasswordPresenter {

	private String mTitle;
	private final LoginPasswordContract.ResetPasswordView mResetPasswordView;
	private static CustomDialog mCurrentDlg;

	@Inject
	public LoginPasswordPresenter(LoginPasswordContract.ResetPasswordView resetPasswordView, Context context) {
		super(context);
		mResetPasswordView = resetPasswordView;
		mTitle = Utils.getString(R.string.reset_password);
	}

	@Override
	public void request() {
		try {
			if (checkData()) {
				Map<String, String> params = new HashMap<>();
				params.put("_a", "user");
				params.put("_b", "aj");
				params.put("cmd", "updatePassword");
				params.put("_s", LibSession.sSid);
				params.put("user_id", String.valueOf(Session.user.getId()));
				params.put("new_password", mResetPasswordView.getNewPassword());
				params.put("old_password", mResetPasswordView.getOldPassword());

				submit(params, new SimpleRequestListener() {
					@Override
					public void loadSuccess(String result, String token) {
						try {
							if (mResetPasswordView.getFlagReset()) {
								CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
								builder.setMessage(mContext.getResources().getString(R.string.success));
								builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										saveGestureType("", AppGlobal.ACTION_TYPE_6_FORGOT_GESTURE);
										mResetPasswordView.clearGesturePassword();
										mResetPasswordView.finishActivity();
										dialog.dismiss();
									}
								});

								CustomDialog mCurrentDlg = builder.create();
								mCurrentDlg.setCancelable(false);
								mCurrentDlg.show();
							} else {
								mResetPasswordView.startMainActivity();
							}


						} catch (Exception e) {
							FileUtils.addErrorLog(e);
							e.printStackTrace();
						}
					}

					@Override
					public void loadFailure(int code, String result, String uniqueToken) {
						String msg = M.get(mContext, code);
						if (TextUtils.isEmpty(msg))
							msg = mContext.getResources().getString(R.string.error);
						Utils.showToast(msg);
					}

				});
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
	}

	@Override
	public boolean checkData() {

		try {
			String newPassword = mResetPasswordView.getNewPassword();
			String confirmPassword = mResetPasswordView.getConfirmPassword();
			if (TextUtils.isEmpty(newPassword)) {
				Utils.showToast(mContext.getResources()
						.getString(R.string.new_password_required));
				return false;
			}
			if (newPassword.equals(mResetPasswordView.getOldPassword()) || newPassword.equals(Session.user.getPhone())) {
				Utils.showToast(mContext.getResources()
						.getString(R.string.msg_1006));
				return false;
			}
			if (TextUtils.isEmpty(confirmPassword)) {
				Utils.showToast(mContext.getResources()
						.getString(R.string.confirm_password_required));
				return false;
			}
			if (!newPassword.equals(confirmPassword)) {
				Utils.showToast(mContext.getResources()
						.getString(R.string.invalid_confirm_password));
				return false;
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public void saveGestureType(String gesture, int actionType) {
		Map<String, String> p = new HashMap<>();
		p.put("_a", "setting");
		p.put("_b", "aj");
		p.put("cmd", "saveGesture");
		p.put("_s", LibSession.sSid);
		p.put("gesture", gesture);
		p.put("action_type", String.valueOf(actionType));
		submit(p, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result,String token) {

			}
		});
	}
}
