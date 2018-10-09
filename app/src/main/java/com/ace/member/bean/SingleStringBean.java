package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

/*
* 这个实体类解析字符串对象 格式为 result:{'attachment':value}/result:{'code':value}/....
* */
public class SingleStringBean extends BaseBean {
	//alternate可以添加其他字段
	@SerializedName(value = "code", alternate = {"attachment","time","verify_code","bill_id","user_id","name"})
	private String value;

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
