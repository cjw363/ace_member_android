package com.ace.member.main.friends.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.ace.member.bean.ContactInfo;
import com.ace.member.bean.DBFriendRequestInfo;
import com.ace.member.bean.NewFriendsInfo;
import com.ace.member.main.friends.db.ChatDB;

import java.util.ArrayList;
import java.util.List;

/**
 * 联系人数据库操作
 */
public class ContactDao extends ChatDB {

	private static ContactDao sContactDao;

	public static ContactDao getInstance() {
		if (sContactDao == null) {
			synchronized (ChatDB.class) {
				if (sContactDao == null) {
					sContactDao = new ContactDao();
				}
			}
		}
		return sContactDao;
	}

	/**
	 * 查询联系人列表
	 */
	public List<ContactInfo> queryContactsList() {
		SQLiteDatabase db = open();
		Cursor cursor = null;
		List<ContactInfo> contactInfoList = new ArrayList<>();
		try {
			cursor = db.rawQuery("select f.name_remark name,f.friend_id,m.phone,m.portrait from tbl_friends f LEFT JOIN tbl_member m on f.friend_id=m.remote_id", null);
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					ContactInfo contactInfo = new ContactInfo();
					String name = cursor.getString(cursor.getColumnIndex("name"));
					contactInfo.setName(name);
					contactInfo.setMemberId(cursor.getInt(cursor.getColumnIndex("friend_id")));
					contactInfo.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
					contactInfo.setPortrait(cursor.getString(cursor.getColumnIndex("portrait")));
					contactInfo.setFirstLetter(name.substring(0, 1).toUpperCase());//首字母转大写
					contactInfoList.add(contactInfo);
				}
				return contactInfoList;
			}
		} finally {
			if (cursor != null) cursor.close();
			db.close();
		}
		return contactInfoList;
	}

	/**
	 * 好友申请列表
	 */
	public List<DBFriendRequestInfo> queryFriendRequestList() {
		SQLiteDatabase db = open();
		Cursor cursor = null;
		try {
			cursor = db.rawQuery("select time,member_id,member_name,content,status,time_complete from tbl_friend_request order by lmt desc", null);
			if (cursor.getCount() > 0) {
				List<DBFriendRequestInfo> requestInfoList = new ArrayList<>();
				while (cursor.moveToNext()) {
					DBFriendRequestInfo requestInfo = new DBFriendRequestInfo();
					requestInfo.setTime(cursor.getString(cursor.getColumnIndex("time")));
					requestInfo.setMemberId(cursor.getInt(cursor.getColumnIndex("member_id")));
					requestInfo.setMemberName(cursor.getString(cursor.getColumnIndex("member_name")));
					requestInfo.setContent(cursor.getString(cursor.getColumnIndex("content")));
					requestInfo.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
					requestInfo.setTimeComplete(cursor.getString(cursor.getColumnIndex("time_complete")));
					requestInfoList.add(requestInfo);
				}
				return requestInfoList;
			}
		} finally {
			if (cursor != null) cursor.close();
			db.close();
		}
		return null;
	}

	/**
	 * new friends列表
	 */
	public List<NewFriendsInfo> queryNewFriendsList() {
		SQLiteDatabase db = open();
		Cursor cursor = null;
		try {
			cursor = db.rawQuery("select r.time,r.member_id,r.member_name,r.content,r.type_add,r.status,r.time_complete,m.phone,m.portrait from tbl_friend_request r LEFT JOIN tbl_member m on r.member_id=m.remote_id order by r.lmt desc", null);
			if (cursor.getCount() > 0) {
				List<NewFriendsInfo> newFriendsInfoList = new ArrayList<>();
				while (cursor.moveToNext()) {
					NewFriendsInfo newFriendsInfo = new NewFriendsInfo();
					newFriendsInfo.setTime(cursor.getString(cursor.getColumnIndex("time")));
					newFriendsInfo.setId(cursor.getInt(cursor.getColumnIndex("member_id")));
					newFriendsInfo.setName(cursor.getString(cursor.getColumnIndex("member_name")));
					newFriendsInfo.setContent(cursor.getString(cursor.getColumnIndex("content")));
					newFriendsInfo.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
					newFriendsInfo.setPortrait(cursor.getString(cursor.getColumnIndex("portrait")));
					newFriendsInfo.setTypeAdd(cursor.getInt(cursor.getColumnIndex("type_add")));
					newFriendsInfo.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
					newFriendsInfoList.add(newFriendsInfo);
				}
				return newFriendsInfoList;
			}
		} finally {
			if (cursor != null) cursor.close();
			db.close();
		}
		return null;
	}

	/**
	 * 通过关键词搜索联系人
	 */
	public List<ContactInfo> querySearchContacts(String keyWord) {
		SQLiteDatabase db = open();
		Cursor cursor = null;
		try {
			cursor = db.rawQuery("select f.friend_id,f.name_remark,m.phone,m.portrait from tbl_friends f left join tbl_member m on f.friend_id=m.remote_id where f.name_remark like '%" + keyWord + "%' or m.phone like '%-" + keyWord + "%'", null);
			if (cursor.getCount() > 0) {
				List<ContactInfo> contactInfoList = new ArrayList<>();
				while (cursor.moveToNext()) {
					ContactInfo contactInfo = new ContactInfo();
					contactInfo.setMemberId(cursor.getInt(cursor.getColumnIndex("friend_id")));
					contactInfo.setName(cursor.getString(cursor.getColumnIndex("name_remark")));
					contactInfo.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
					contactInfo.setPortrait(cursor.getString(cursor.getColumnIndex("portrait")));
					contactInfoList.add(contactInfo);
				}
				return contactInfoList;
			}
		} finally {
			if (cursor != null) cursor.close();
			db.close();
		}
		return null;
	}

	/**
	 * 根据手机号码查询有无此联系人
	 */
	public ContactInfo queryIsFriend(String phone) {
		SQLiteDatabase db = open();
		Cursor cursor = null;
		try {
			cursor = db.rawQuery("select friend_id,name_remark from tbl_friends where friend_id=(select remote_id from tbl_member where phone=" + qstr(phone) + ")", null);
			if (cursor.getCount() > 0) {
				if (cursor.moveToNext()) {
					ContactInfo contactInfo = new ContactInfo();
					contactInfo.setMemberId(cursor.getInt(cursor.getColumnIndex("friend_id")));
					contactInfo.setName(cursor.getString(cursor.getColumnIndex("name_remark")));
					return contactInfo;
				}
			}
		} finally {
			if (cursor != null) cursor.close();
			db.close();
		}
		return null;
	}

	/**
	 * 获取好友申请通知未读数量
	 */
	public int getNtfRequestUnreadCount() {
		String count = queryOne("select count(1) from tbl_notification where type=" + NOTIFICATION_TYPE_6_FRIEND_REQUES + " and status=" + MESSAGE_STATUS_1_NEW);
		if (TextUtils.isEmpty(count)) return 0;
		return Integer.parseInt(count);
	}

	/**
	 * 更新申请通知已读
	 */
	public void updateNtfRequestStatus() {
		execSQL("update tbl_notification set status=" + MESSAGE_STATUS_2_READ + " where type=" + NOTIFICATION_TYPE_6_FRIEND_REQUES + " and status=" + MESSAGE_STATUS_1_NEW);
	}
}
