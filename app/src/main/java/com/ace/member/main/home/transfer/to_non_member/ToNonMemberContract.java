package com.ace.member.main.home.transfer.to_non_member;

import android.support.v7.app.AppCompatActivity;

import org.json.JSONObject;

public interface ToNonMemberContract {

	interface View {

		String getAmount();

		String getBalance();

		String getFee();

		void setFee(double v);

		String getCurrency();

		void clearInfo();

		void showSuccess(JSONObject object);

		AppCompatActivity getActivity();

		void setSubmitEnables(boolean flag);

	}

	interface Presenter {

	}
}
