package com.ace.member.main.third_party.samrithisak_loan;


import com.ace.member.bean.MemberLoanPartner;
import com.ace.member.bean.MemberLoanPartnerBill;

import java.util.List;

public interface SamrithisakLoanContract {
	interface View {
		void setEnableFunction(boolean enable);

		void setEnableLoan(boolean enable);

		void setEnableReturnLoan(boolean enable);

		void setCreditAndLoan(MemberLoanPartner memberLoanPartner);

		void setLatestBill(MemberLoanPartnerBill bill);

		void setEnableRefresh(boolean enable);

		void setServiceChargeRate(String s);
	}

	interface Presenter {
		void start(boolean isRefresh);
	}
}
