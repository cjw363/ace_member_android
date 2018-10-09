package com.ace.member.utils;


import android.text.TextUtils;

import com.ace.member.R;
import com.ace.member.bean.Balance;
import com.ace.member.bean.BankAccount;
import com.og.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class BankUtil {

	public static List<BankAccount> getBankAccountList(List<BankAccount> list, String code) {
		List<BankAccount> bankAccounts = new ArrayList<>();
		if (list == null || list.size() == 0) return bankAccounts;
		for (BankAccount bankAccount : list) {
			if (bankAccount.getCode().equals(code)) {
				bankAccounts.add(bankAccount);
			}
		}
		return bankAccounts;
	}

	public static List<BankAccount> getBankAccountList(List<BankAccount> list) {
		List<BankAccount> bankAccounts = new ArrayList<>();
		if (list == null || list.size() == 0) return bankAccounts;
		String code;
		boolean flag;
		for (BankAccount bankAccount : list) {
			flag = false;
			code = bankAccount.getCode();
			for (BankAccount temp : bankAccounts) {
				if (temp.getCode().equals(code)) {
					flag = true;
					break;
				}
			}
			if (!flag) bankAccounts.add(bankAccount);
		}
		return bankAccounts;
	}

	public static int getBankImageResourceByBankCode(String bankCode) {
		String bankIconName = "ic_bank_" + bankCode.toLowerCase();
		int bankIconDrawableID = BaseApplication.getContext().getResources().getIdentifier(BaseApplication.getContext().getPackageName() + ":drawable/" + bankIconName, null, null);
		return bankIconDrawableID == 0 ? R.drawable.ic_bank : bankIconDrawableID;
	}

	public static double getBalance(String currency) {
		List<Balance> list = Session.balanceList;
		if (Utils.isEmptyList(list)) return 0;
		for (int i = 0, n = list.size(); i < n; i++) {
			Balance balance=list.get(i);
			if (balance.getCurrency().equals(currency)) {
				String a = String.valueOf(balance.getAmount());
				return TextUtils.isEmpty(a) ? 0 : Double.parseDouble(a.replace(",", ""));
			}
		}
		return 0;
	}
}
