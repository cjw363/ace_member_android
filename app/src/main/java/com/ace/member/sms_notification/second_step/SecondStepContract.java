package com.ace.member.sms_notification.second_step;

interface SecondStepContract {

	interface SecondStepView {
		void toNextStep();

		void toPreviousStep();

		void finish();

		void toTradingPassword();

		void showFailure();

		void setResendTime(int resendTime);

		void startClock();

		void showResendTimeError();

		void showToast(String msg);

		void toLogin(String msg);
	}

	interface SecondStepPresenter {
		void register(String code, String fullPhone);

		void resetPassword(String code, String fullPhone);

		void resetStatus(String code, String fullPhone);

		void sendVerificationCode(String phone, int smsType, int actionType);

	}
}
