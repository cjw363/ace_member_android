package com.ace.member.main.home.transfer.to_member;


import android.support.v7.app.AppCompatActivity;

public interface ToMemberContract {

	interface View {
		String getAmount();

		String getRemark();

		String getBalance();

		void clearInfo();

		void showSuccess(String time, int transferID);

		AppCompatActivity getActivity();

		void showName(String name);

		void hideName();

		void setSubmitEnables(boolean flag);

		void banTransfer(String msg);

		void showTargetPhone(String phone);

		String getTargetMember();

		void setSendMessage(String msg);
	}

	interface Presenter {
		void submit(String countryCode, String targetPhone, String currency);

		void checkIsMember(final String phone);
	}

}
