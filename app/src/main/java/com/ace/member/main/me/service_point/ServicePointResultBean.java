package com.ace.member.main.me.service_point;

import com.ace.member.base.BaseBean;
import com.ace.member.bean.ServicePoint;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServicePointResultBean extends BaseBean {
	@SerializedName("site_data")
	private List<ServicePoint> list;
	@SerializedName("agent_count")
	private int agentCount;
	@SerializedName("branch_count")
	private int branchCount;

	public void setList(List<ServicePoint> list) {
		this.list = list;
	}

	public List<ServicePoint> getList() {
		return list;
	}

	public void setAgentCount(int agentCount) {
		this.agentCount = agentCount;
	}

	public int getAgentCount() {
		return agentCount;
	}

	public void setBranchCount(int branchCount) {
		this.branchCount = branchCount;
	}

	public int getBranchCount() {
		return this.branchCount;
	}

}
