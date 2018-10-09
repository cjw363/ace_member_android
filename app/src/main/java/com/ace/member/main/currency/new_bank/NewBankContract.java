package com.ace.member.main.currency.new_bank;


import com.ace.member.bean.BankAccount;

public interface NewBankContract {
	interface View{
		void finish();

		void enableBank(boolean enable);

		void enableBankList(boolean enable);

		void setWindowHeight(int height);

		void clearBankAccountNo();

		void setBankBackGroundColor(int color);

		void setBankImageResource(int resource);

		void setBankName(String name);
		void setBankNameColor(int color);

		void setBankAccountName(String name);
		void setBankAccountNameColor(int color);

		void showToast(int res);
	}
	interface Presenter{
		void addBank(String currency,String bankCode,String bankAccountNO);

		void onCancel();

		void onBankItemClick(BankAccount bankAccount);
	}
}
