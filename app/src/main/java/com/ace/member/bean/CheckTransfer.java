package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;


public class CheckTransfer extends BaseBean {

	@SerializedName("is_relate_merchant")
	private int isRelateMerchant;

	@SerializedName("is_relate_partner")
	private int isRelatePartner ;

	public int getIsRelatePartner() {
		return isRelatePartner;
	}

	public void setIsRelatePartner(int isRelatePartner) {
		this.isRelatePartner = isRelatePartner;
	}

	public int getIsRelateMerchant() {

		return isRelateMerchant;
	}

	public void setIsRelateMerchant(int isRelateMerchant) {
		this.isRelateMerchant = isRelateMerchant;
	}
}
