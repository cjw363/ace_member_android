package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

public class DBNotificationInfo extends BaseBean{
  private int id;
  private String time;
  private int chatId;
  private int type;
  private String content;
  private int status;
  private String lmt;
  @SerializedName("time_receive")
  private String timeReceive;

  public String getTimeReceive() {
    return timeReceive;
  }

  public void setTimeReceive(String timeReceive) {
    this.timeReceive = timeReceive;
  }

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

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }
}
