package com.ace.member.event;


public class AddBankEvent {
	private String code;
	private String bankAccountNO;

	public AddBankEvent(String code, String bankAccountNO) {
		this.code = code;
		this.bankAccountNO = bankAccountNO;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getBankAccountNO() {
		return bankAccountNO;
	}

	public void setBankAccountNO(String bankAccountNO) {
		this.bankAccountNO = bankAccountNO;
	}
}
