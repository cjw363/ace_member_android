package com.ace.member.bean;


import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ChatGroupInfoBean extends BaseBean {
	@SerializedName("member_info_list")
	private ArrayList<MemberChatInfoBean> memberChatList;
	@SerializedName("flag_mute_notifications")
	private int flagMuteNotifications;
	@SerializedName("is_owner")
	private boolean isOwner;

	public ArrayList<MemberChatInfoBean> getMemberChatList() {
		return memberChatList;
	}

	public void setMemberChatList(ArrayList<MemberChatInfoBean> memberChatList) {
		this.memberChatList = memberChatList;
	}

	public int getFlagMuteNotifications() {
		return flagMuteNotifications;
	}

	public void setFlagMuteNotifications(int flagMuteNotifications) {
		this.flagMuteNotifications = flagMuteNotifications;
	}

	public boolean isOwner() {
		return isOwner;
	}

	public void setOwner(boolean owner) {
		isOwner = owner;
	}
}
