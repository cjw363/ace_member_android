package com.ace.member.main.currency.withdraw;

import android.support.v7.app.AppCompatActivity;

import com.ace.member.bean.BankAccount;

import java.util.List;
import java.util.Map;

public interface WithdrawContract {
	interface WithdrawView {
		void setWithdrawFee(String fee);

		void setBankFee(String fee);

		void enableWithdrawFee(boolean enable);

		void enableBankFee(boolean enable);

		void setBalance(String balance);

		void banWithdraw(boolean temp);

		void showToast(String msg);

		void showWithDrawFail();

		void setMemberBankList(List<BankAccount> list);

		void setCompanyBankList(List<BankAccount> list);

		void initBank();

		BankAccount getCurrentBank();

		void enableWithdrawBtn(boolean enable);

		void toWithdrawDetail(int id);

		AppCompatActivity getActivity();
	}

	interface WithdrawPresenter {
		void start(String currency);

		void getBankListAndWithdrawFee(String currency);

		void onWithdrawAmountTextChange(String amount);

		void withdraw(String currency, String amount, String bank, String bankAccountNo, String bankFee, String remark);

		void calculateBankFee(BankAccount bankAccount, String amount);

		void deleteBank(Map<String, String> params, String currency);
	}
}
