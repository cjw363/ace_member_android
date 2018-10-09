package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FriendsDataWrapper extends BaseBean {
	@SerializedName("chat_id_list")
	private List<DBChatIdInfo> chatIdList;
	@SerializedName("chat_member_list")
	private List<DBChatMemberInfo> chatMemberList;
	@SerializedName("chat_content_list")
	private List<DBChatContentInfo> chatContentList;
	@SerializedName("notification_list")
	private List<DBNotificationInfo> notificationList;
	@SerializedName("friend_request_list")
	private List<DBFriendRequestInfo> friendRequestInfoList;
	@SerializedName("friends_list")
	private List<DBFriendInfo> friendsList;
	@SerializedName("member_list")
	private List<DBMemberInfo> memberList;
	@SerializedName("transfer_list")
	private List<DBTransferInfo> transferList;
	@SerializedName("group_list")
	private List<DBGroupInfo> groupList;
	@SerializedName("chat_extend_group_list")
	private List<DBChatExtendGroupInfo> chatExtendGroupList;
	@SerializedName("flag_mute_notifications")
	private int flagMuteNotifications;
	@SerializedName("chat_transaction_list")
	private List<DBTransactionInfo> transactionList;

	public List<DBGroupInfo> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<DBGroupInfo> groupList) {
		this.groupList = groupList;
	}

	public List<DBChatExtendGroupInfo> getChatExtendGroupList() {
		return chatExtendGroupList;
	}

	public void setChatExtendGroupList(List<DBChatExtendGroupInfo> chatExtendGroupList) {
		this.chatExtendGroupList = chatExtendGroupList;
	}

	public List<DBMemberInfo> getMemberList() {
		return memberList;
	}

	public void setMemberList(List<DBMemberInfo> memberList) {
		this.memberList = memberList;
	}

	public List<DBChatIdInfo> getChatIdList() {
		return chatIdList;
	}

	public void setChatIdList(List<DBChatIdInfo> chatIdList) {
		this.chatIdList = chatIdList;
	}

	public List<DBChatMemberInfo> getChatMemberList() {
		return chatMemberList;
	}

	public void setChatMemberList(List<DBChatMemberInfo> chatMemberList) {
		this.chatMemberList = chatMemberList;
	}

	public List<DBChatContentInfo> getChatContentList() {
		return chatContentList;
	}

	public void setChatContentList(List<DBChatContentInfo> chatContentList) {
		this.chatContentList = chatContentList;
	}

	public List<DBNotificationInfo> getNotificationList() {
		return notificationList;
	}

	public void setNotificationList(List<DBNotificationInfo> notificationList) {
		this.notificationList = notificationList;
	}

	public List<DBFriendRequestInfo> getFriendRequestList() {
		return friendRequestInfoList;
	}

	public void setFriendRequestList(List<DBFriendRequestInfo> friendRequestInfoList) {
		this.friendRequestInfoList = friendRequestInfoList;
	}

	public List<DBFriendInfo> getFriendsList() {
		return friendsList;
	}

	public void setFriendsList(List<DBFriendInfo> friendsList) {
		this.friendsList = friendsList;
	}

	public List<DBTransferInfo> getTransferList() {
		return transferList;
	}

	public void setTransferList(List<DBTransferInfo> transferList) {
		this.transferList = transferList;
	}

	public int getFlagMuteNotifications() {
		return flagMuteNotifications;
	}

	public void setFlagMuteNotifications(int flagMuteNotifications) {
		this.flagMuteNotifications = flagMuteNotifications;
	}

	public List<DBTransactionInfo> getTransactionList() {
		return transactionList;
	}

	public void setTransactionList(List<DBTransactionInfo> transactionList) {
		this.transactionList = transactionList;
	}
}
