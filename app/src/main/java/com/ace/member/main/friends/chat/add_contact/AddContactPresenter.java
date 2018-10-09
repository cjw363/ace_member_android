package com.ace.member.main.friends.chat.add_contact;

import android.content.Context;
import android.text.TextUtils;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.bean.ContactInfo;
import com.ace.member.bean.FriendsDataWrapper;
import com.ace.member.bean.NewFriendsInfo;
import com.ace.member.bean.SearchFriendDataWrapper;
import com.ace.member.main.friends.db.TableDB;
import com.ace.member.main.friends.db.dao.ChatDao;
import com.ace.member.main.friends.db.dao.ContactDao;
import com.ace.member.service.IMWebSocketService;
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

public class AddContactPresenter extends BasePresenter implements AddContactContract.AddContactPresenter {
	private static final int TYPE_NEW_FRIENDS_1_TITLE = 1;
	private AddContactContract.AddContactView mView;

	@Inject
	public AddContactPresenter(Context context, AddContactContract.AddContactView mView) {
		super(context);
		this.mView = mView;
	}

	@Override
	public void getNewFriendsList() {
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
					mView.showNewFriendsList(addTitle(ContactDao.getInstance().queryNewFriendsList(), Utils.getString(R.string.friend_requests)));
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
					e.printStackTrace();
				}
			}

			@Override
			public void loadFailure(int errorCode, String result, String uniqueToken) {
				try {
					mView.showNewFriendsList(addTitle(ContactDao.getInstance().queryNewFriendsList(), Utils.getString(R.string.friend_requests)));
					Utils.showToast(R.string.fail);
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
					e.printStackTrace();
				}
			}
		}, true);
	}

	@Override
	public void acceptApplication(final int memberId, final String nameRemark, int addType) {
		Map<String, String> m = new HashMap<>();
		m.put("_a", "friends");
		m.put("_b", "aj");
		m.put("_s", Session.sSid);
		m.put("friend_id", String.valueOf(memberId));
		m.put("name_remark", nameRemark);
		m.put("type_add", String.valueOf(addType));
		m.put("cmd", "addContact");
		submit(m, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				try {
					Boolean resultBoolean = Boolean.valueOf(result);
					if (resultBoolean) {
						IMWebSocketService.sendFriendMsg(memberId, IMWebSocketService.IM_SOCKET_TYPE_11_CHAT_MSG);//通知对方
						IMWebSocketService.sendFriendMsg(Session.user.getId(), IMWebSocketService.IM_SOCKET_TYPE_11_CHAT_MSG);//通知自己
					}
					mView.showAcceptResult(resultBoolean, memberId, nameRemark);
				} catch (Exception e) {
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
	public void addApplication(final int friendId, String content, int addType) {
		final Map<String, String> m = new HashMap<>();
		m.put("_a", "friends");
		m.put("_b", "aj");
		m.put("_s", Session.sSid);
		m.put("friend_id", String.valueOf(friendId));
		m.put("content", content);
		m.put("type_add", String.valueOf(addType));
		m.put("cmd", "addContactRequest");
		submit(m, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				try {
					Boolean resultBoolean = Boolean.valueOf(result);
					if (resultBoolean) {
						IMWebSocketService.sendFriendMsg(friendId, IMWebSocketService.IM_SOCKET_TYPE_22_REQUEST_MSG);//通知对方
					}
					mView.showAddResult(resultBoolean);
				} catch (Exception e) {
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
	public void getMayKnowFriends(List<ContactInfo> contactInfoList) {
		if (contactInfoList == null || contactInfoList.size() == 0) return;
		StringBuilder builder = new StringBuilder();
		for (ContactInfo contact : contactInfoList) {
			builder.append(contact.getPhone());
			builder.append(",");
		}
		String phones = builder.deleteCharAt(builder.length() - 1).toString();
		Map<String, String> m = new HashMap<>();
		m.put("_a", "friends");
		m.put("_b", "aj");
		m.put("_s", Session.sSid);
		m.put("str_phone", phones);
		m.put("cmd", "getMayKnowFriends");
		submit(m, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				try {
					SearchFriendDataWrapper friendDataWrapper = JsonUtil.jsonToBean(result, SearchFriendDataWrapper.class);
					assert friendDataWrapper != null;
					mView.showMayKnowFriends(addTitle(friendDataWrapper.getMayKnowFriendsList(), Utils.getString(R.string.people_you_may_know)));
				} catch (Exception e) {
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

	/**
	 * @return 添加标题
	 */
	private List<NewFriendsInfo> addTitle(List<NewFriendsInfo> newFriendsInfoList, String title) {
		if (newFriendsInfoList != null && newFriendsInfoList.size() != 0) {//手动添加标题
			NewFriendsInfo newFriendsInfo = new NewFriendsInfo();
			newFriendsInfo.setType(TYPE_NEW_FRIENDS_1_TITLE);
			newFriendsInfo.setContent(title);
			newFriendsInfoList.add(0, newFriendsInfo);
			return newFriendsInfoList;
		} else {
			return null;
		}
	}
}
