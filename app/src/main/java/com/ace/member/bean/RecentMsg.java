package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

public class RecentMsg extends BaseBean {
  private String time;
  @SerializedName("content_type")
  private int contentType;
  private String content;
  private int status;
  @SerializedName("member_id")
  private int memberId;
  @SerializedName("chat_id")
  private int chatId;
  @SerializedName("transfer_id")
  private int transferId;
  @SerializedName("flag_mute_notifications")
  private int flagMuteNotifications;
  private String name;
  @SerializedName("chat_type")
  private int chatType;//单人或群组类型
  private int type;//最近消息的类型
  private int count;//未读消息数量
  private String portrait;//头像

  public String getPortrait() {
    return portrait;
  }

  public void setPortrait(String portrait) {
    this.portrait = portrait;
  }

  public int getChatType() {
    return chatType;
  }

  public void setChatType(int chatType) {
    this.chatType = chatType;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public int getContentType() {
    return contentType;
  }

  public void setContentType(int contentType) {
    this.contentType = contentType;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public int getMemberId() {
    return memberId;
  }

  public void setMemberId(int memberId) {
    this.memberId = memberId;
  }

  public int getChatId() {
    return chatId;
  }

  public void setChatId(int chatId) {
    this.chatId = chatId;
  }

  public int getTransferId() {
    return transferId;
  }

  public void setTransferId(int transferId) {
    this.transferId = transferId;
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

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }
}
