package com.ace.member.event;


import com.ace.member.bean.PhoneCompany;

public class SelectPhoneCompanyEvent {
	private PhoneCompany mPhoneCompany;

	public SelectPhoneCompanyEvent(PhoneCompany phoneCompany) {
		mPhoneCompany = phoneCompany;
	}

	public PhoneCompany getPhoneCompany() {
		return mPhoneCompany;
	}

	public void setPhoneCompany(PhoneCompany phoneCompany) {
		mPhoneCompany = phoneCompany;
	}
}
