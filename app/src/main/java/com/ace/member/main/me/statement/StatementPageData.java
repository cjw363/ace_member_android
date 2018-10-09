package com.ace.member.main.me.statement;


import com.ace.member.bean.PageBaseBean;
import com.ace.member.bean.Statement;

public class StatementPageData extends PageBaseBean<Statement> {
	private String beginning;

	public String getBeginning() {
		return beginning;
	}

	public void setBeginning(String beginning) {
		this.beginning = beginning;
	}

	@Override
	public String toString() {
		return "StatementPageData{" + "beginning='" + beginning + '\'' + '}';
	}
}
