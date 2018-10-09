package com.ace.member.keyboard;

class KeyModel {

	private Integer code;
	private String label;
	
	KeyModel(Integer code, String label){
		this.code = code;
		this.label = label;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	
}
