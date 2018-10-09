package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;
/*
* 这个类的解析是整数的 格式为 result:{id:value}/result:{bill_id:value}/result:{time:value}/...
* */
public class SingleIntBean extends BaseBean {
	//alternate可以添加其他字段来解析
	@SerializedName(value = "id",alternate = {"bill_id","time","order_id","phone_type","status","number"})
	private int value=0;
	public void setValue(int value){
		this.value=value;
	}
	public int getValue(){
		return value;
	}
}
