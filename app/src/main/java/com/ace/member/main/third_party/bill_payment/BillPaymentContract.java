package com.ace.member.main.third_party.bill_payment;

import com.ace.member.bean.BillerBean;
import com.ace.member.bean.BillerConfig;

import java.util.List;
import java.util.Map;

public interface BillPaymentContract {
	interface view {

		//		void showBillerWindow(List<BillerBean> list);
		void enableBtnSubmit(boolean enable);

		void enableEmptyPartnerCompany(boolean enable);

		void initBillerConfig(List<BillerConfig> list, BillerBean bean, boolean isRunning);

		String getCurrency();

		String getAmount();

		void initToken(String token);

		void setFee(double amount, String currency);

		void showCalculateFeeArea(BillerConfig config, double amount);

		void saveSuccess();

		void saveFail(int code, List<BillerConfig> list, BillerBean bean);
	}

	interface presenter {
		//		void saveBillerBean(BillerBean bean);
		//		void getBiller();

		void getBillerConfig(int billerID);

		void updateFeeInfo(List<BillerConfig> list);

		void submit(Map<String, String> params);
	}
}
