package com.ace.member.base;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.utils.AppGlobal;
import com.og.utils.CustomDialog2;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

public class LottoPresenter extends BasePresenter {

	public LottoPresenter(Context context) {
		super(context);
	}

	protected void showSaveSuccess(double amount) {
		try {
			Dialog dialog = new CustomDialog2.Builder(mContext).setContentView(LayoutInflater.from(mContext)
				.inflate(R.layout.view_betting_success, null))
				.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						closeBettingView();
						dialogInterface.dismiss();
					}
				})
				.setNegativeButton(R.string.buy_again, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						againBetting();
						dialogInterface.dismiss();
					}
				})
				.create();
			dialog.setCancelable(false);
			dialog.getWindow()
				.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
			dialog.show();
			TextView tvAmount = (TextView) dialog.findViewById(R.id.tv_bet_amount);
			String str = Utils.format(amount, 2) + " " + AppGlobal.USD;
			tvAmount.setText(str);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	public void closeBettingView() {
	}

	public void againBetting() {
	}

}
