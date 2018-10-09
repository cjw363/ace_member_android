package com.ace.member.sms_notification.first_step;

import com.og.utils.ItemObject;

import java.util.List;

interface FirstStepContract {

	interface FirstStepView {
		void initCountryCodeList(List<ItemObject> list);

		void toNextStep();

		void setResendTime(int time);

		void saveVerification();

		void showToast(String msg);

		void toLogin(String msg);
	}

	interface FirstStepPresenter {
		void getConfig(int mActionType);

		void checkPhone(String countryCode, String phone, int type);
	}
}
