package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;
/*
* 这个类的解析是布尔型 格式为 result:{id:value}/result:{bill_id:value}/result:{time:value}/...
* */
public class SingleBooleanBean extends BaseBean {
	@SerializedName(value = "is_function",alternate = {"is_running","have_pending_data","status"})
	private boolean value;
	public void setValue(boolean value){
		this.value=value;
	}
	public boolean getValue(){
		return value;
	}
}
