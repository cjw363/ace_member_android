package com.ace.member.main.home.money.withdraw_cash.confirm;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.bean.SingleStringBean;
import com.ace.member.main.home.money.withdraw_cash.WithdrawCashResult;
import com.ace.member.main.home.money.withdraw_cash.WithdrawCashResultActivity;
import com.ace.member.main.home.money.withdraw_cash.WithdrawCashResultDetail;
import com.ace.member.utils.AppGlobal;
import com.og.LibSession;
import com.og.http.SimpleRequestListener;
import com.og.utils.CustomDialog;
import com.og.utils.JsonUtil;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class WithdrawCashConfirmPresenter extends BasePresenter implements WithdrawCashConfirmContract.WithdrawCashConfirmPresenter {

	private final WithdrawCashConfirmContract.WithdrawCashConfirmView mView;
	private String mTime = "";

	@Inject
	public WithdrawCashConfirmPresenter(Context context, WithdrawCashConfirmContract.WithdrawCashConfirmView mView) {
		super(context);
		this.mView = mView;
	}

	public void submit() {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "transfer");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("cmd", "confirmWithdrawCash");
		params.put("id", mView.getWithdrawID());
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					SingleStringBean bean = JsonUtil.jsonToBean(result, SingleStringBean.class);
					assert bean != null;
					mDlg = new CustomDialog.Builder(mContext).setMessage(bean.getValue())
						.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						})
						.create();
					mDlg.setCancelable(false);
					mDlg.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, false);
	}

	private Dialog mDlg;

	public void requestResult() {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "transfer");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("cmd", "getWithdrawCashResultByID");
		params.put("id", mView.getWithdrawID());
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					WithdrawCashResult withdrawCashResult = JsonUtil.jsonToBean(result, WithdrawCashResult.class);
					assert withdrawCashResult != null;
					boolean notData = withdrawCashResult.isNotData();
					if (!notData) {
						WithdrawCashResultDetail withdrawCashResultDetail = withdrawCashResult.getData();
						if (AppGlobal.STATUS_3_COMPLETED == withdrawCashResultDetail.getStatus()) {
							Intent intent = new Intent(mContext, WithdrawCashResultActivity.class);
							Bundle b = new Bundle();
							b.putSerializable("result", withdrawCashResult.getData());
							intent.putExtras(b);
							mDlg.dismiss();
							mContext.startActivity(intent);
							mView.finishSelf();
						} else if (AppGlobal.STATUS_4_CANCELLED == withdrawCashResultDetail.getStatus()) {
							final Dialog dlg = new CustomDialog.Builder(mContext).setMessage(R.string.withdraw_cancelled)
								.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
										mView.finishSelf();
									}
								})
								.create();
							dlg.setCancelable(false);
							dlg.show();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, false);
	}

}
