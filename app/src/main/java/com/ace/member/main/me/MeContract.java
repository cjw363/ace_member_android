package com.ace.member.main.me;

interface MeContract {

	interface MeView {
		void updateBalance();

		void toLogout();

		void showRefreshStatus(boolean isRefreshing);

		void showRefreshResult(String msg);

		void showBalance();

		void updatePhoneIcon(boolean enable);

		void updateCertificateIcon(boolean enable);

		void updateFingerPrintIcon(boolean enable);

		void setLevelIcon(int level);

		void updatePortrait();
	}

	interface MePresenter {
		void getBalanceAndStatus(boolean isRefresh);

		void logout();
	}
}
