package com.ace.member.bean;



import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

public class MemberChatInfoBean extends BaseBean {
	@SerializedName("member_id")
	private int memberID;
	@SerializedName("flag_mute_notifications")
	private int flagMuteNotifications;
	private String name;
	private String portrait;

	public int getMemberID() {
		return memberID;
	}

	public void setMemberID(int memberID) {
		this.memberID = memberID;
	}

	public int getFlagMuteNotifications() {
		return flagMuteNotifications;
	}

	public void setFlagMuteNotifications(int flagMuteNotifications) {
		this.flagMuteNotifications = flagMuteNotifications;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPortrait() {
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}
}
