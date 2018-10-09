package com.ace.member.main.friends;

import android.content.Context;
import android.text.TextUtils;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.bean.ContactInfo;
import com.ace.member.bean.FriendsDataWrapper;
import com.ace.member.main.friends.db.TableDB;
import com.ace.member.main.friends.db.dao.ChatDao;
import com.ace.member.main.friends.db.dao.ContactDao;
import com.ace.member.utils.Session;
import com.ace.member.utils.StringUtil;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class FriendsPresenter extends BasePresenter implements FriendsContract.FriendsPresenter {
	private final FriendsContract.FriendsView mView;

	@Inject
	public FriendsPresenter(Context context, FriendsContract.FriendsView mView) {
		super(context);
		this.mView = mView;
	}

	@Override
	public void getFriendsMsgList() {
		//先检查本地数据库是否需要更新
		//用tbl_chat和tbl_notification的表的数据去比较服务器，remote_id,lmt
		String chatLmt = StringUtil.checkStr(ChatDao.getInstance().getLastLmt(TableDB.TABLE_CHAT));
		String chatMemberLmt = StringUtil.checkStr(ChatDao.getInstance().getLastLmt(TableDB.TABLE_CHAT_MEMBER));
		String friendRequestLmt = StringUtil.checkStr(ChatDao.getInstance().getLastLmt(TableDB.TABLE_FRIEND_REQUEST));
		String friendsLmt = StringUtil.checkStr(ChatDao.getInstance().getLastLmt(TableDB.TABLE_FRIENDS));
		String memberLmt = StringUtil.checkStr(ChatDao.getInstance().getLastLmt(TableDB.TABLE_MEMBER));
		String transferLmt = StringUtil.checkStr(ChatDao.getInstance().getLastLmt(TableDB.TABLE_TRANSFER));
		String groupLmt = StringUtil.checkStr(ChatDao.getInstance().getLastLmt(TableDB.TABLE_GROUP));
		String chatExtendGroupLmt = StringUtil.checkStr(ChatDao.getInstance().getLastLmt(TableDB.TABLE_CHAT_EXTEND_GROUP));

		List<Map<String, String>> chatTimeLastRead = ChatDao.getInstance().getChatTimeLastRead();
		String chatTimeLastReadJson = StringUtil.checkStr(JsonUtil.beanToJson(chatTimeLastRead));
		Map<String, String> m = new HashMap<>();
		m.put("_a", "friends");
		m.put("_b", "aj");
		m.put("_s", Session.sSid);
		m.put("chat_lmt", chatLmt);
		m.put("chat_member_lmt", chatMemberLmt);
		m.put("friend_request_lmt", friendRequestLmt);
		m.put("friends_lmt", friendsLmt);
		m.put("member_lmt", memberLmt);
		m.put("transfer_lmt", transferLmt);
		m.put("group_lmt", groupLmt);
		m.put("chat_extend_group_lmt", chatExtendGroupLmt);
		m.put("chat_time_last_read_json", chatTimeLastReadJson);
		m.put("cmd", "getNewMessages");
		submit(m, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				try {
					FriendsDataWrapper friendsDataWrapper = JsonUtil.jsonToBean(result, FriendsDataWrapper.class);
					ChatDao.getInstance().insertTable(friendsDataWrapper);
					mView.showRecentMsgList(ChatDao.getInstance().queryRecentMsgList());
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
					e.printStackTrace();
				}
			}

			@Override
			public void loadFailure(int errorCode, String result, String uniqueToken) {
				try {
					mView.showRecentMsgList(ChatDao.getInstance().queryRecentMsgList());
					Utils.showToast(R.string.fail);
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
					e.printStackTrace();
				}
			}
		}, false);
	}

	@Override
	public void isFriend(String phone) {
		ContactInfo contactInfo = ContactDao.getInstance().queryIsFriend(phone);
		if (contactInfo != null) {
			mView.toChatActivity(contactInfo.getMemberId(), contactInfo.getName());
		} else {
			getMemberIdByPhone(phone);
		}
	}

	@Override
	public void getMemberIdByPhone(String phone) {
		Map<String, String> m = new HashMap<>();
		m.put("_a", "friends");
		m.put("_b", "aj");
		m.put("_s", Session.sSid);
		m.put("phone", phone);
		m.put("cmd", "getMemberIDByPhone");
		submit(m, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				try {
					if (!TextUtils.isEmpty(result)) {
						JSONObject jsonObject = new JSONObject(result);
						boolean exist = jsonObject.optBoolean("exist");
						if (exist) {
							int member_id = jsonObject.optInt("member_id");
							mView.toFriendProfileActivity(member_id);
						} else {
							Utils.showToast(R.string.no_contacts_found);
						}
					}
				} catch (JSONException e) {
					FileUtils.addErrorLog(e);
					e.printStackTrace();
				}
			}

			@Override
			public void loadFailure(int errorCode, String result, String uniqueToken) {
				Utils.showToast(R.string.fail);
			}
		});
	}

	@Override
	public void getFriendRequestList() {
		String friendRequestLmt = StringUtil.checkStr(ContactDao.getInstance().getLastLmt(TableDB.TABLE_FRIEND_REQUEST));
		String memberLmt = StringUtil.checkStr(ChatDao.getInstance().getLastLmt(TableDB.TABLE_MEMBER));
		Map<String, String> map = new HashMap<>();
		map.put("_a", "friends");
		map.put("_b", "aj");
		map.put("_s", Session.sSid);
		map.put("friend_request_lmt", friendRequestLmt);
		map.put("member_lmt", memberLmt);
		map.put("cmd", "getLastFriendRequestList");
		submit(map, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				try {
					FriendsDataWrapper friendsDataWrapper = JsonUtil.jsonToBean(result, FriendsDataWrapper.class);
					ContactDao.getInstance().insertTable(friendsDataWrapper);
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
					e.printStackTrace();
				}
			}
		});
	}
}
