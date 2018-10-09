package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

public class DBChatMemberInfo extends BaseBean{
  @SerializedName("chat_id")
  private int chatId;
  @SerializedName("member_id")
  private int memberId;
  @SerializedName("time_join")
  private String timeJoin;
  @SerializedName("flag_mute_notifications")
  private int flagMuteNotifications;
  @SerializedName("time_last_read")
  private String timeLastRead;
  private String lmt;

  public String getTimeLastRead() {
    return timeLastRead;
  }

  public void setTimeLastRead(String timeLastRead) {
    this.timeLastRead = timeLastRead;
  }

  public String getLmt() {
    return lmt;
  }

  public void setLmt(String lmt) {
    this.lmt = lmt;
  }

  public int getChatId() {
    return chatId;
  }

  public void setChatId(int chatId) {
    this.chatId = chatId;
  }

  public int getMemberId() {
    return memberId;
  }

  public void setMemberId(int memberId) {
    this.memberId = memberId;
  }

  public String getTimeJoin() {
    return timeJoin;
  }

  public void setTimeJoin(String timeJoin) {
    this.timeJoin = timeJoin;
  }

  public int getFlagMuteNotifications() {
    return flagMuteNotifications;
  }

  public void setFlagMuteNotifications(int flagMuteNotifications) {
    this.flagMuteNotifications = flagMuteNotifications;
  }
}
