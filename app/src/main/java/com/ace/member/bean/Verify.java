package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;


public class Verify extends BaseBean {
	@SerializedName("certificate_type")
	private int certificateType;
	@SerializedName("certificate_number")
	private String certificateNumber;
	private int status;
	private String remark;

	public Verify() {
	}

	public Verify(int certificateType, String certificateNumber, int status, String remark) {
		this.certificateType = certificateType;
		this.certificateNumber = certificateNumber;
		this.status = status;
		this.remark = remark;
	}

	public int getCertificateType() {
		return certificateType;
	}

	public void setCertificateType(int certificateType) {
		this.certificateType = certificateType;
	}

	public String getCertificateNumber() {
		return certificateNumber;
	}

	public void setCertificateNumber(String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
