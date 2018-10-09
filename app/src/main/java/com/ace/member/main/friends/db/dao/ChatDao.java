package com.ace.member.main.friends.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.ace.member.bean.ChatMsg;
import com.ace.member.bean.RecentMsg;
import com.ace.member.main.friends.db.ChatDB;
import com.ace.member.main.friends.db.TableDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 聊天数据库操作
 */
public class ChatDao extends ChatDB {
	private static final int CHAT_TYPE_1_FRIEND = 1;//1 Friend 2 Group
	private static final int CHAT_TYPE_2_GROUP = 2;

	private static ChatDao mChatDao;

	public static ChatDao getInstance() {
		if (mChatDao == null) {
			synchronized (ChatDB.class) {
				if (mChatDao == null) {
					mChatDao = new ChatDao();
				}
			}
		}
		return mChatDao;
	}

	/**
	 * 获取最近消息列表
	 */
	public List<RecentMsg> queryRecentMsgList() {
		SQLiteDatabase db = open();
		Cursor cursor = null;
		try {
			cursor = db.rawQuery("select DISTINCT * from(select e.remote_id,e.time,e.chat_type,e.member_id,e.content_type,e.content,e.status,f.name_remark name,m.portrait,100 type,e.flag_mute_notifications from (select a.*,b.member_id,b.flag_mute_notifications from (select c2.remote_id,c2.time_last_message time,c2.chat_type, c1.type content_type, c1.content, c1.transfer_id, c1.status from tbl_chat c2 left join tbl_chat_content c1 on c1.time=c2.time_last_message where c2.chat_type=" + CHAT_TYPE_1_FRIEND + " group by c2.remote_id) a  left join tbl_chat_member b on a.remote_id = b.remote_chat_id) e left join tbl_friends f on e.member_id=f.friend_id left join tbl_member m on f.friend_id = m.remote_id union select 0 remote_id,n.time,0 chat_type,0 member_id,n.type content_type,n.content, n.status, 0 name,'' portrait,999 type,0 flag_mute_notifications from tbl_notification n where n.lmt=(select max(lmt) from tbl_notification where type=n.type) and n.type<>" + NOTIFICATION_TYPE_6_FRIEND_REQUES + " union select c.*,g.group_name name,'' portrait,100 type,m.flag_mute_notifications from (SELECT c2.remote_id,c2.time_last_message time,c2.chat_type,0 member_id,c1.type content_type,c1.content,c1.status FROM tbl_chat c2 LEFT JOIN tbl_chat_content c1 ON c1.time = c2.time_last_message WHERE c2.chat_type = " + CHAT_TYPE_2_GROUP + " GROUP BY c2.remote_id ) c left join tbl_chat_extend_group g on c.remote_id=g.chat_id left join tbl_chat_member m on g.chat_id=m.remote_chat_id) order by time desc", null);
			if (cursor.getCount() > 0) {
				ArrayList<RecentMsg> recentMsgList = new ArrayList<>();
				while (cursor.moveToNext()) {
					RecentMsg recentMsg = new RecentMsg();
					int chatId = cursor.getInt(cursor.getColumnIndex("remote_id"));
					int memberId = cursor.getInt(cursor.getColumnIndex("member_id"));
					int type = cursor.getInt(cursor.getColumnIndex("type"));
					int contentType = cursor.getInt(cursor.getColumnIndex("content_type"));
					recentMsg.setTime(cursor.getString(cursor.getColumnIndex("time")));
					recentMsg.setChatId(chatId);
					recentMsg.setMemberId(memberId);
					recentMsg.setContent(cursor.getString(cursor.getColumnIndex("content")));
					recentMsg.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
					recentMsg.setChatType(cursor.getInt(cursor.getColumnIndex("chat_type")));
					recentMsg.setName(cursor.getString(cursor.getColumnIndex("name")));
					recentMsg.setPortrait(cursor.getString(cursor.getColumnIndex("portrait")));
					recentMsg.setContentType(contentType);
					recentMsg.setType(type);
					recentMsg.setFlagMuteNotifications(cursor.getInt(cursor.getColumnIndex("flag_mute_notifications")));
					if (type == RECENT_TYPE_100_CHAT_MSG) {
						recentMsg.setCount(Integer.parseInt(queryOne("select count(1) count from tbl_chat_content where status =" + MESSAGE_STATUS_1_NEW + " and member_id=" + memberId + " and remote_chat_id=" + chatId)));
					} else if (type == RECENT_TYPE_999_OFFICIAL_MSG) {
						recentMsg.setCount(Integer.parseInt(queryOne("select count(1) count from tbl_notification where type =" + contentType + " and status=" + MESSAGE_STATUS_1_NEW)));
					}
					recentMsgList.add(recentMsg);
				}
				return recentMsgList;
			}
		} finally {
			if (cursor != null) cursor.close();
			db.close();
		}
		return null;
	}

	public String queryChatRemoteIdLmt(int chatId) {
		return queryOne("select lmt from tbl_chat where remote_id =" + chatId);
	}

	public List<ChatMsg> queryChatByMsg(int chatId, int currentPage) {
		SQLiteDatabase db = open();
		Cursor cursor = null;
		try {
			cursor = db.rawQuery("SELECT c.id,c.time,c.member_id,c.type,c.content,c.transfer_id,m.portrait,tr.currency,tr.amount FROM tbl_chat_content c LEFT JOIN tbl_member m on c.member_id=m.remote_id LEFT JOIN tbl_transfer tr ON c.transfer_id=tr.remote_id where c.remote_chat_id=" + chatId + " order by c.time desc limit " + SIZE_10_PAGE * (currentPage - 1) + " ," + SIZE_10_PAGE, null);
			if (cursor.getCount() > 0) {
				List<ChatMsg> list = new ArrayList<>();
				if (cursor.moveToLast()) {
					do {
						ChatMsg chatMsg = new ChatMsg();
						chatMsg.setId(cursor.getInt(cursor.getColumnIndex("id")));
						chatMsg.setMemberId(cursor.getInt(cursor.getColumnIndex("member_id")));
						chatMsg.setContent(cursor.getString(cursor.getColumnIndex("content")));
						chatMsg.setContentType(cursor.getInt(cursor.getColumnIndex("type")));
						chatMsg.setTransferId(cursor.getInt(cursor.getColumnIndex("transfer_id")));
						chatMsg.setPortrait(cursor.getString(cursor.getColumnIndex("portrait")));
						chatMsg.setCurrency(cursor.getString(cursor.getColumnIndex("currency")));
						chatMsg.setAmount(cursor.getDouble(cursor.getColumnIndex("amount")));
						chatMsg.setTime(cursor.getString(cursor.getColumnIndex("time")));
						list.add(chatMsg);
					} while (cursor.moveToPrevious());
				}
				return list;
			}
		} finally {
			if (cursor != null) cursor.close();
			db.close();
		}
		return null;
	}

	/**
	 * 获取跟对方的聊天页数
	 */
	public int getChatTotalPages(int chatId) {
		int pageSize = SIZE_10_PAGE;//每页显示多少条数据
		int totalRecords = Integer.parseInt(queryOne("SELECT count(1) count FROM tbl_chat_content where remote_chat_id=" + chatId));// 表中的总记录数
		int totalPages = totalRecords % pageSize == 0 ? totalRecords / pageSize : totalRecords / pageSize + 1;//总页数
		return totalPages;
	}

	/**
	 * 更新消息的状态
	 */
	public void updateChatMsg(int chatId) {
		execSQL("update tbl_chat_content set status =" + MESSAGE_STATUS_2_READ + " where remote_chat_id=" + chatId);
	}

	/**
	 * 清除聊天记录
	 *
	 * @param chatID 聊天ID
	 */
	public void clearChatHistory(int chatID) {
		execSQL("delete from " + TableDB.TABLE_CHAT_CONTENT + " where remote_chat_id=" + chatID);
	}

	/**
	 * @param memberId 会员id
	 */
	public int getChatId(int memberId) {
		String remoteId = queryOne("select c.remote_id from tbl_chat c left join tbl_chat_member m on c.remote_id=m.remote_chat_id where m.member_id=" + memberId + " and c.chat_type=" + CHAT_TYPE_1_FRIEND);
		if (TextUtils.isEmpty(remoteId)) return 0;
		return Integer.parseInt(remoteId);
	}

	/**
	 * 根据chatId 判断聊天类型
	 */
	public int getChatType(int chatId) {
		String chatType = queryOne("select chat_type from tbl_chat where remote_id=" + chatId);
		if (TextUtils.isEmpty(chatType)) return 0;
		return Integer.parseInt(chatType);
	}

	/**
	 * 查询消息的最后阅读
	 */
	public String getTimeLastRead(int chatId) {
		return queryOne("select time_last_read from tbl_chat_member where remote_chat_id=" + chatId);
	}

	/**
	 * 获取所有聊天会话的timeLastRead
	 */
	public List<Map<String, String>> getChatTimeLastRead() {
		SQLiteDatabase db = open();
		Cursor cursor = null;
		try {
			cursor = db.rawQuery("select remote_chat_id,time_last_read from tbl_chat_member group by remote_chat_id", null);
			if (cursor.getCount() > 0) {
				List<Map<String, String>> list = new ArrayList<>();
				while (cursor.moveToNext()) {
					HashMap<String, String> map = new HashMap<>();
					map.put("chat_id", cursor.getString(cursor.getColumnIndex("remote_chat_id")));
					map.put("time_last_read", cursor.getString(cursor.getColumnIndex("time_last_read")));
					list.add(map);
				}
				return list;
			}
		} finally {
			if (cursor != null) cursor.close();
			db.close();
		}
		return null;
	}
}
