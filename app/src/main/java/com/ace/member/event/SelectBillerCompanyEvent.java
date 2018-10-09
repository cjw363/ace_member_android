package com.ace.member.event;

import com.ace.member.bean.BillerBean;
import com.ace.member.bean.PartnerBiller;

public class SelectBillerCompanyEvent {
	private BillerBean biller;

	public SelectBillerCompanyEvent(BillerBean bean) {
		biller = bean;
	}

	public BillerBean getBillerCompany() {
		return biller;
	}

	public void setBillerCompany(BillerBean bean) {
		biller = bean;
	}
}
