package com.ace.member.event;

import com.ace.member.bean.PartnerBean;

public class SelectPartnerCompanyEvent {
	private PartnerBean partner;

	public SelectPartnerCompanyEvent(PartnerBean partner) {
		this.partner = partner;
	}

	public PartnerBean getBillerCompany() {
		return partner;
	}

	public void setBillerCompany(PartnerBean partner) {
		this.partner = partner;
	}
}
