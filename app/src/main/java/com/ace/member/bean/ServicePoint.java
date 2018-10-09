package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

public class ServicePoint extends BaseBean {

	/**
	 * site_type : 2
	 * site_id : 1
	 * division_code : 010306
	 * address : 111
	 * address_remark : 111
	 * address_kh : 111
	 * address_remark_kh : 111
	 * longitude : 104.8925040000
	 * latitude : 11.5441840000
	 * name : SIMON AG
	 */


	@SerializedName("site_type")
	private int siteType;
	@SerializedName("site_id")
	private int siteID;
	@SerializedName("division_code")
	private String divisionCode;
	private String address;
	@SerializedName("address_remark")
	private String addressRemark;
	@SerializedName("address_kh")
	private String addressKH;
	@SerializedName("address_remark_kh")
	private String addressRemarkKH;
	private String longitude;
	private String latitude;
	private String name;

	public int getSiteType() {
		return siteType;
	}

	public void setSiteType(int siteType) {
		this.siteType = siteType;
	}

	public int getSiteID() {
		return siteID;
	}

	public void setSiteID(int siteID) {
		this.siteID = siteID;
	}

	public String getDivisionCode() {
		return divisionCode;
	}

	public void setDivisionCode(String divisionCode) {
		this.divisionCode = divisionCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddressRemark() {
		return addressRemark;
	}

	public void setAddressRemark(String addressRemark) {
		this.addressRemark = addressRemark;
	}

	public String getAddressKH() {
		return addressKH;
	}

	public void setAddressKH(String addressKH) {
		this.addressKH = addressKH;
	}

	public String getAddressRemarkKH() {
		return addressRemarkKH;
	}

	public void setAddressRemarkKH(String addressRemarkKH) {
		this.addressRemarkKH = addressRemarkKH;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
