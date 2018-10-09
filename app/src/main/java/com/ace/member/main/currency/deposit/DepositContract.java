package com.ace.member.main.currency.deposit;

import android.support.v7.app.AppCompatActivity;

import com.ace.member.bean.BankAccount;

import java.util.List;
import java.util.Map;

interface DepositContract {
	interface DepositView {
		void setCompanyBankList(List<BankAccount> list);

		void setMemberBankList(List<BankAccount> list);

		void enableCompanyBank(boolean b);

		void initBank();

		void banDeposit(boolean temp);

		void showDepositFail();

		void enableDepositBtn(boolean enable);

		void toDepositDetail(int id);

		AppCompatActivity getActivity();
	}

	interface DepositPresenter {
		void start(String currency);

		void deposit(String currency, String amount, String bank, String companyBankAccountNo, String memberBankAccountNo, String remark);

		void deleteBank(Map<String, String> params, String currency);
	}
}
