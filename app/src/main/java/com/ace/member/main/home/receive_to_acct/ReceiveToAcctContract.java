package com.ace.member.main.home.receive_to_acct;


public class ReceiveToAcctContract {
	interface View {
		void showSuccess(R2ADataBeen data);

		void invalidCode();

		void notRunningFunction();
	}

	interface Presenter {
		void chkSecurityCode(String securityCode);
	}
}
