package com.og.utils;

import java.io.Serializable;

/**
 * 可以扩展
 */
public class ItemObject implements Serializable {
	private String mValue;
	private Integer mKey;

	public String getValue() {
		return mValue;
	}

	public Integer getKey() {
		return mKey;
	}

	public void setValue(String value) {
		mValue = value;
	}

	public void setKey(Integer key) {
		mKey = key;
	}

	public void setKey(String key){
		try {
			mKey=Integer.parseInt(key);
		}catch (Exception e){
			e.printStackTrace();
			FileUtils.addErrorLog(e);
		}
	}
}
