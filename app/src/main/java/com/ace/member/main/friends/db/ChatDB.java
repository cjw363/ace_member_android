package com.ace.member.main.friends.db;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.ace.member.bean.DBChatContentInfo;
import com.ace.member.bean.DBChatExtendGroupInfo;
import com.ace.member.bean.DBChatIdInfo;
import com.ace.member.bean.DBChatMemberInfo;
import com.ace.member.bean.DBGroupInfo;
import com.ace.member.bean.DBMemberInfo;
import com.ace.member.bean.DBFriendRequestInfo;
import com.ace.member.bean.DBFriendInfo;
import com.ace.member.bean.DBTransactionInfo;
import com.ace.member.bean.DBTransferInfo;
import com.ace.member.bean.FriendsDataWrapper;
import com.ace.member.bean.DBNotificationInfo;
import com.ace.member.utils.BaseApplication;
import com.ace.member.utils.Session;
import com.og.utils.Utils;

import java.util.List;

import static com.ace.member.main.friends.db.TableDB.DB_IM_RUN;

public class ChatDB {
	protected static final int SIZE_10_PAGE = 10;

	protected static final int RECENT_TYPE_100_CHAT_MSG = 100;
	protected static final int RECENT_TYPE_999_OFFICIAL_MSG = 999;//最近消息类型，官方通知消息

	protected static final int MESSAGE_STATUS_1_NEW = 1;//1 new 新信息 2 read 已读
	protected static final int MESSAGE_STATUS_2_READ = 2;

	protected static final int NOTIFICATION_TYPE_6_FRIEND_REQUES = 6;//好友申请通知

	private ChatSQLiteHelper mChatSQLiteHelper;
	private SQLiteDatabase mDatabase = null;

	public ChatDB() {
		if (mChatSQLiteHelper == null)
			mChatSQLiteHelper = new ChatSQLiteHelper(BaseApplication.getContext(), DB_IM_RUN + Session.user.getId() + ".db");
	}

	/**
	 * 打开数据库
	 */
	public SQLiteDatabase open() {
		if (mChatSQLiteHelper != null) {
			try {
				String DBName = TableDB.DB_IM_RUN + Session.user.getId() + ".db";
				if (!mChatSQLiteHelper.getDatabaseName().equals(DBName)) {//当前账户数据库不对应，这种情况发生在切换账号后
					mChatSQLiteHelper = new ChatSQLiteHelper(BaseApplication.getContext(), DBName);//切换数据库
				}
				mDatabase = mChatSQLiteHelper.getWritableDatabase();//获取可写数据库
			} catch (SQLException e) {
				mDatabase = mChatSQLiteHelper.getReadableDatabase();//获取只读数据库
			}
		}
		return mDatabase;
	}

	/**
	 * 关闭数据库
	 */
	public void close() {
		if (mDatabase != null) mDatabase.close();
	}

	/**
	 * 直接执行sql语句
	 */
	protected void execSQL(String sql) {
		SQLiteDatabase db = open();
		db.beginTransaction();
		try {
			db.execSQL(sql);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}
	}

	/**
	 * 查询一个字段
	 */
	protected String queryOne(String sql) {
		SQLiteDatabase db = open();
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, null);
			if (cursor.getCount() > 0) {
				if (cursor.moveToFirst()) {
					String result = cursor.getString(0);
					return checkStr(result);
				}
			}
		} finally {
			if (cursor != null) cursor.close();
			db.close();
		}
		return "";
	}

	protected String qstr(String str) {
		if (str == null) str = "";
		str = str.replace("\\", "\\\\").replace("'", "''");
		return "'" + str + "'";
	}

	/**
	 * 检验字符串，为空返回空串
	 */
	private String checkStr(String str) {
		return (str == null ? "" : str);
	}

	public void insertTable(FriendsDataWrapper wrapper) {
		insertTableChat(wrapper.getChatIdList());
		insertTableChatMember(wrapper.getChatMemberList());
		insertTableChatContent(wrapper.getChatContentList());
		insertTableFriendRequest(wrapper.getFriendRequestList());
		insertTableFriends(wrapper.getFriendsList());
		insertTableNotification(wrapper.getNotificationList());
		insertTableMember(wrapper.getMemberList());
		insertTableTransfer(wrapper.getTransferList());
		insertTableGroup(wrapper.getGroupList());
		insertTableChatExtendGroup(wrapper.getChatExtendGroupList());
		insertTableTransaction(wrapper.getTransactionList());
	}

	private void insertTableChat(List<DBChatIdInfo> chatIdList) {
		if (!Utils.isEmptyList(chatIdList)) {
			String sql = "replace into tbl_chat (remote_id, time, chat_type, time_last_member_join, time_last_message, lmt) values";
			for (DBChatIdInfo chatIdInfo : chatIdList) {
				sql += " (" + chatIdInfo.getId() + "," + qstr(chatIdInfo.getTime()) + "," + chatIdInfo.getChatType() + "," + qstr(chatIdInfo.getTimeLastMemberJoin()) + "," + qstr(chatIdInfo.getTimeLastMessage()) + "," + qstr(chatIdInfo.getLmt()) + "),";
			}
			sql = sql.substring(0, sql.length() - 1);
			execSQL(sql);
		}
	}

	private void insertTableChatMember(List<DBChatMemberInfo> chatMemberList) {
		if (!Utils.isEmptyList(chatMemberList)) {
			String sql = "replace into tbl_chat_member (remote_chat_id, member_id, time_join, flag_mute_notifications, time_last_read, lmt) values";
			for (DBChatMemberInfo chatMemberInfo : chatMemberList) {
				sql += " (" + chatMemberInfo.getChatId() + "," + chatMemberInfo.getMemberId() + "," + qstr(chatMemberInfo.getTimeJoin()) + "," + chatMemberInfo.getFlagMuteNotifications() + "," + qstr(chatMemberInfo.getTimeLastRead()) + "," + qstr(chatMemberInfo.getLmt()) + "),";
			}
			sql = sql.substring(0, sql.length() - 1);
			execSQL(sql);
		}
	}

	private void insertTableChatContent(List<DBChatContentInfo> chatContentList) {
		if (!Utils.isEmptyList(chatContentList)) {//如果是自己的消息，status以已读插入
			int dataCount = 0;
			String sql = "replace into tbl_chat_content (time, remote_chat_id, member_id, type, content, transfer_id, status, lmt) values";
			for (DBChatContentInfo contentInfo : chatContentList) {
				String id = queryOne("select id from tbl_chat_content where time=" + qstr(contentInfo.getTime()) + " and remote_chat_id=" + contentInfo.getChatId() + " and member_id=" + contentInfo.getMemberId() + " and type=" + contentInfo.getType() + " and content=" + qstr(contentInfo.getContent()) + " and transfer_id=" + contentInfo.getTransferId() + " and lmt=" + qstr(contentInfo.getLmt()));
				if (TextUtils.isEmpty(id)) {//过滤重复数据
					sql += " (" + qstr(contentInfo.getTime()) + "," + contentInfo.getChatId() + "," + contentInfo.getMemberId() + "," + contentInfo.getType() + "," + qstr(contentInfo.getContent()) + "," + contentInfo.getTransferId() + "," + (contentInfo.getMemberId() == Session.user.getId() ? MESSAGE_STATUS_2_READ : MESSAGE_STATUS_1_NEW) + "," + qstr(contentInfo.getLmt()) + "),";
					dataCount++;
				}
			}
			if (dataCount > 0) {
				sql = sql.substring(0, sql.length() - 1);
				execSQL(sql);
			}
		}
	}

	private void insertTableFriendRequest(List<DBFriendRequestInfo> requestList) {
		if (!Utils.isEmptyList(requestList)) {
			String sql = "replace into tbl_friend_request (id, time, member_id, member_name, content, type_add, status, time_complete, lmt) values";
			for (DBFriendRequestInfo request : requestList) {
				sql += " (" + request.getId() + "," + qstr(request.getTime()) + "," + request.getMemberId() + "," + qstr(request.getMemberName()) + "," + qstr(request.getContent()) + "," + request.getTypeAdd() + "," + request.getStatus() + "," + qstr(request.getTimeComplete()) + "," + qstr(request.getLmt()) + "),";
			}
			sql = sql.substring(0, sql.length() - 1);
			execSQL(sql);
		}
	}

	private void insertTableFriends(List<DBFriendInfo> friendsList) {
		if (!Utils.isEmptyList(friendsList)) {
			String sql = "replace into tbl_friends (friend_id, name_remark, time_add, type_add, lmt) values";
			for (DBFriendInfo friend : friendsList) {
				sql += " (" + friend.getFriendID() + "," + qstr(friend.getName()) + "," + qstr(friend.getTimeAdd()) + "," + friend.getTypeAdd() + "," + qstr(friend.getLmt()) + "),";
			}
			sql = sql.substring(0, sql.length() - 1);
			execSQL(sql);
		}
	}

	private void insertTableNotification(List<DBNotificationInfo> notificationList) {
		if (!Utils.isEmptyList(notificationList)) {
			String sql = "replace into tbl_notification(id, time, type, content, status, lmt, time_receive) values";
			for (DBNotificationInfo notificationInfo : notificationList) {
				sql += " (" + notificationInfo.getId() + "," + qstr(notificationInfo.getTime()) + "," + notificationInfo.getType() + "," + qstr(notificationInfo.getContent()) + "," + MESSAGE_STATUS_1_NEW + "," + qstr(notificationInfo.getLmt()) + "," + qstr(notificationInfo.getTimeReceive()) + "),";
			}
			sql = sql.substring(0, sql.length() - 1);
			execSQL(sql);
		}
	}

	private void insertTableMember(List<DBMemberInfo> memberList) {
		if (!Utils.isEmptyList(memberList)) {
			String sql = "replace into tbl_member(remote_id, phone, email, portrait, lmt) values";
			for (DBMemberInfo memberInfo : memberList) {
				sql += " (" + memberInfo.getId() + "," + qstr(memberInfo.getPhone()) + "," + qstr(memberInfo.getEmail()) + "," + qstr(memberInfo.getPortrait()) + "," + qstr(memberInfo.getLmt()) + "),";
			}
			sql = sql.substring(0, sql.length() - 1);
			execSQL(sql);
		}
	}

	private void insertTableTransfer(List<DBTransferInfo> transferList) {
		if (!Utils.isEmptyList(transferList)) {
			String sql = "replace into tbl_transfer(remote_id, time, user_type, user_id, currency, amount, fee, to_user_id, to_user_id, status, remark, lmt) values";
			for (DBTransferInfo transferInfo : transferList) {
				sql += " (" + transferInfo.getId() + "," + qstr(transferInfo.getTime()) + "," + transferInfo.getUserType() + "," + transferInfo.getUserID() + "," + qstr(transferInfo.getCurrency()) + "," + transferInfo.getAmount() + "," + transferInfo.getFee() + "," + transferInfo.getToUserType() + "," + transferInfo.getToUserID() + "," + transferInfo.getStatus() + "," + qstr(transferInfo.getRemark()) + "," + qstr(transferInfo.getLmt()) + "),";
			}
			sql = sql.substring(0, sql.length() - 1);
			execSQL(sql);
		}
	}

	public String getLastLmt(String tblName) {
		return queryOne("select max(lmt) lmt from " + tblName);
	}


	private void insertTableGroup(List<DBGroupInfo> groupList) {
		if (!Utils.isEmptyList(groupList)) {
			String sql = "replace into tbl_group(id, time, member_id, chat_id, lmt) values";
			for (DBGroupInfo groupInfo : groupList) {
				sql += " (" + groupInfo.getId() + "," + qstr(groupInfo.getTime()) + "," + groupInfo.getMemberId() + "," + groupInfo.getChatId() + "," + qstr(groupInfo.getLmt()) + "),";
			}
			sql = sql.substring(0, sql.length() - 1);
			execSQL(sql);
		}
	}

	private void insertTableChatExtendGroup(List<DBChatExtendGroupInfo> chatExtendGroupList) {
		if (!Utils.isEmptyList(chatExtendGroupList)) {
			String sql = "replace into tbl_chat_extend_group(chat_id, group_name, owner_id, flag_verify_invitation, group_notice, lmt) values";
			for (DBChatExtendGroupInfo extendGroupInfo : chatExtendGroupList) {
				sql += " (" + extendGroupInfo.getChatId() + "," + qstr(extendGroupInfo.getGroupName()) + "," + extendGroupInfo.getOwnerId() + "," + extendGroupInfo.getFlagVerifyInvitation() + "," + qstr(extendGroupInfo.getGroupNotice()) + "," + qstr(extendGroupInfo.getLmt()) + "),";
			}
			sql = sql.substring(0, sql.length() - 1);
			execSQL(sql);
		}
	}

	private void insertTableTransaction(List<DBTransactionInfo> transactionList) {
		if (!Utils.isEmptyList(transactionList)) {
			String sql = "replace into tbl_member_transaction(remote_id, time, type, sub_type, currency, amount, remark, lmt) values";
			for (DBTransactionInfo transactionInfo : transactionList) {
				sql += " (" + transactionInfo.getId() + "," + qstr(transactionInfo.getTime()) + "," + transactionInfo.getType() + "," + transactionInfo.getSubType() + "," + qstr(transactionInfo.getCurrency()) + "," + transactionInfo.getAmount() + "," + qstr(transactionInfo.getRemark()) + "," + qstr(transactionInfo.getLmt()) + "),";
			}
			sql = sql.substring(0, sql.length() - 1);
			execSQL(sql);
		}
	}
}
