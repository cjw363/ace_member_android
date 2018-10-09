package com.ace.member.main.home.top_up.phone_company;

import com.ace.member.bean.PhoneCompany;

import java.util.List;

public interface PhoneCompanyContract {

	interface View {
		void setPhoneCompanyList(List<PhoneCompany> list);
	}

	interface Presenter {}
}
