package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

public class DBFriendRequestInfo extends BaseBean{
  private int id;
  private String time;
  @SerializedName("member_id")
  private int memberId;
  @SerializedName("member_name")
  private String memberName;
  private String content;
  @SerializedName("type_add")
  private int typeAdd;
  private int status;
  @SerializedName("time_complete")
  private String timeComplete;
  private String lmt;

  public int getTypeAdd() {
    return typeAdd;
  }

  public void setTypeAdd(int typeAdd) {
    this.typeAdd = typeAdd;
  }

  public String getMemberName() {
    return memberName;
  }

  public void setMemberName(String memberName) {
    this.memberName = memberName;
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

  public int getMemberId() {
    return memberId;
  }

  public void setMemberId(int memberId) {
    this.memberId = memberId;
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

  public String getTimeComplete() {
    return timeComplete;
  }

  public void setTimeComplete(String timeComplete) {
    this.timeComplete = timeComplete;
  }
}
