package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class TopUpDataWrapper extends BaseBean {
	@SerializedName("phone_company")
	private PhoneCompany phoneCompany;

	@SerializedName("face_value_list")
	private List<FaceValue> faceValueList;

	@SerializedName("function_show_pincode")
	private boolean isFunctionShowPinCode;

	@SerializedName("function_send_sms")
	private boolean isFunctionSendSMS;

	public PhoneCompany getPhoneCompany() {
		return phoneCompany;
	}

	public void setPhoneCompany(PhoneCompany phoneCompany) {
		this.phoneCompany = phoneCompany;
	}

	public List<FaceValue> getFaceValueList() {
		return faceValueList == null ? new ArrayList<FaceValue>() : faceValueList;
	}

	public void setFaceValueList(List<FaceValue> faceValueList) {
		this.faceValueList = faceValueList;
	}

	public boolean isFunctionShowPinCode() {
		return isFunctionShowPinCode;
	}

	public void setFunctionShowPinCode(boolean functionShowPinCode) {
		isFunctionShowPinCode = functionShowPinCode;
	}

	public boolean isFunctionSendSMS() {
		return isFunctionSendSMS;
	}

	public void setFunctionSendSMS(boolean functionSendSMS) {
		isFunctionSendSMS = functionSendSMS;
	}
}
