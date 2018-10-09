package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

public class DBFriendInfo extends BaseBean{
  private String name;
  @SerializedName("friend_id")
  private int friendID;
  @SerializedName("time_add")
  private String timeAdd;
  @SerializedName("type_add")
  private int typeAdd;
  private String lmt;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getFriendID() {
    return friendID;
  }

  public void setFriendID(int friendID) {
    this.friendID = friendID;
  }

  public String getTimeAdd() {
    return timeAdd;
  }

  public void setTimeAdd(String timeAdd) {
    this.timeAdd = timeAdd;
  }

  public int getTypeAdd() {
    return typeAdd;
  }

  public void setTypeAdd(int typeAdd) {
    this.typeAdd = typeAdd;
  }

  public String getLmt() {
    return lmt;
  }

  public void setLmt(String lmt) {
    this.lmt = lmt;
  }
}
