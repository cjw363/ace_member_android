package com.ace.member.main.home.transfer.to_merchant;


import android.support.v7.app.AppCompatActivity;

public interface ToMerchantContract {

	interface View {
		String getAmount();

		String getRemark();

		String getBalance();

		void clearInfo();

		void showSuccess(String time);

		AppCompatActivity getActivity();

		void setFee(double v);

		String getCurrency();

		String getFee();

		void setReceiver(String phone);

		void setSubmitEnables(boolean flag);
	}

	interface Presenter {
		void submit(String currency);
	}

}
