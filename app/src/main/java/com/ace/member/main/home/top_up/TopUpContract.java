package com.ace.member.main.home.top_up;

import android.support.v7.app.AppCompatActivity;

import com.ace.member.bean.FaceValue;
import com.ace.member.bean.PhoneCompany;
import com.ace.member.event.SelectPhoneCompanyEvent;

import java.util.List;

public interface TopUpContract {

	interface View {
		void enablePhoneNumber(boolean enable);

		void enableEmptyTopUpCompany(boolean enable);

		void enableEmptyFaceValue(boolean enable);

		void setCurrentTopUpCompany(PhoneCompany company);

		void setFaceValueList(List<FaceValue> list);

		void setCountryCode(String countryCode);

		void setPhoneCompanyList(List<PhoneCompany> list);

		int getTopUpWay();

		String getCountryCode();

		String getPhone();

		PhoneCompany getPhoneCompany();

		FaceValue getFaceValue();

		String getCurrency();

		AppCompatActivity getActivity();

		void toOrderDetail(int orderId,boolean sms);

		void setIsFunctionShowPincode(boolean isFunctionShowPincode);

		void setIsFunctionSendSMS(boolean isFunctionSendSMS);

		void showFunctionPause(int functionCode);

		void setSubmitEnables(boolean flag);
	}

	interface Presenter {
		void start();

		void onSelectPhoneCompanyEvent(SelectPhoneCompanyEvent event);

		void topUpSubmit();

		void getPhoneCompany();
	}
}
