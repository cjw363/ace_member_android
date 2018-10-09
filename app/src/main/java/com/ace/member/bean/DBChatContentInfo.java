package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

public class DBChatContentInfo extends BaseBean{
  private int id;
  private String time;
  @SerializedName("chat_id")
  private int chatId;
  @SerializedName("member_id")
  private int memberId;
  @SerializedName("type")
  private int type;
  @SerializedName("content")
  private String content;
  @SerializedName("transfer_id")
  private int transferId;
  @SerializedName("status")
  private int status;
  private String lmt;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getLmt() {
    return lmt;
  }

  public void setLmt(String lmt) {
    this.lmt = lmt;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
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

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public int getTransferId() {
    return transferId;
  }

  public void setTransferId(int transferId) {
    this.transferId = transferId;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }
}
