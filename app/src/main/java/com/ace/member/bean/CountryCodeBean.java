package com.ace.member.bean;


import com.ace.member.base.BaseBean;
import com.og.utils.ItemObject;

import java.util.ArrayList;
import java.util.List;

public class CountryCodeBean extends BaseBean {
	private List<ItemObject> countryCodeList;

	public CountryCodeBean(List<SingleStringBean> list) {
		int len = list.size();
		List<ItemObject> nList = new ArrayList<>();
		for (int i = 0; i < len; i++) {
			SingleStringBean it = list.get(i);
			ItemObject iObj = new ItemObject();
			String code = it.getValue();
			iObj.setKey(code);
			iObj.setValue("+ " + code);
			nList.add(i, iObj);
		}
		this.countryCodeList = nList;
	}

	public List<ItemObject> getCountryCodeList() {
		return countryCodeList;
	}

	public void setCountryCodeList(List<ItemObject> countryCodeList) {
		this.countryCodeList = countryCodeList;
	}
}
