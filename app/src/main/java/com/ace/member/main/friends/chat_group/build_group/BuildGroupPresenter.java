package com.ace.member.main.friends.chat_group.build_group;

import android.content.Context;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.bean.ContactInfo;
import com.ace.member.bean.DBChatExtendGroupInfo;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class BuildGroupPresenter extends BasePresenter implements BuildGroupContract.BuildGroupPresenter {
	private final BuildGroupContract.BuildGroupView mView;
	private String firstLetter = "";

	@Inject
	public BuildGroupPresenter(Context context, BuildGroupContract.BuildGroupView buildGroupView) {
		super(context);
		mView = buildGroupView;
	}

	@Override
	public void getContactsList() {
		String friendsLmt = StringUtil.checkStr(ContactDao.getInstance().getLastLmt(TableDB.TABLE_FRIENDS));
		String memberLmt = StringUtil.checkStr(ChatDao.getInstance().getLastLmt(TableDB.TABLE_MEMBER));
		Map<String, String> map = new HashMap<>();
		map.put("_a", "friends");
		map.put("_b", "aj");
		map.put("_s", Session.sSid);
		map.put("friends_lmt", friendsLmt);
		map.put("member_lmt", memberLmt);
		map.put("cmd", "getLastFriendsList");
		submit(map, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				try {
					FriendsDataWrapper friendsDataWrapper = JsonUtil.jsonToBean(result, FriendsDataWrapper.class);
					ContactDao.getInstance().insertTable(friendsDataWrapper);
					List<ContactInfo> contactInfoList = ContactDao.getInstance().queryContactsList();
					mView.showContactsList(sortList(contactInfoList));
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
					e.printStackTrace();
				}
			}

			@Override
			public void loadFailure(int errorCode, String result, String uniqueToken) {
				try {
					List<ContactInfo> contactInfoList = ContactDao.getInstance().queryContactsList();
					mView.showContactsList(sortList(contactInfoList));
					Utils.showToast(R.string.fail);
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
					e.printStackTrace();
				}
			}
		}, true);
	}

	@Override
	public void buildGroupChat(Map<Integer, ContactInfo> selectedMap) {
		String selectedJson = StringUtil.checkStr(JsonUtil.beanToJson(new ArrayList<>(selectedMap.values())));
		Map<String, String> map = new HashMap<>();
		map.put("_a", "friends");
		map.put("_b", "aj");
		map.put("_s", Session.sSid);
		map.put("selected_member_json", selectedJson);
		map.put("cmd", "buildGroupChat");
		submit(map, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				try {
					DBChatExtendGroupInfo data = JsonUtil.jsonToBean(result, DBChatExtendGroupInfo.class);
					int chatID = data.getChatId();
					String groupName = data.getGroupName();
					if (chatID > 0) {//跳转页面
						Utils.showToast(R.string.success);
						mView.toGroupChat(chatID, groupName);
					} else {
						Utils.showToast(R.string.fail);
					}
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
					e.printStackTrace();
				}
			}

			@Override
			public void loadFailure(int errorCode, String result, String uniqueToken) {
				Utils.showToast(R.string.fail);
			}
		}, true);
	}

	private List<ContactInfo> sortList(List<ContactInfo> contactInfoList) {
		firstLetter = "";//需清除上一次的记录
		Collections.sort(contactInfoList, new Comparator<ContactInfo>() {
			@Override
			public int compare(ContactInfo o1, ContactInfo o2) {
				return o1.getFirstLetter().compareToIgnoreCase(o2.getFirstLetter());
			}
		});
		for (int i = 0; i < contactInfoList.size(); i++) {
			ContactInfo contactInfo = contactInfoList.get(i);
			String letter = contactInfo.getFirstLetter();
			contactInfo.setLetterFlag(!letter.equals(firstLetter));
			firstLetter = letter;
		}
		return contactInfoList;
	}
}
