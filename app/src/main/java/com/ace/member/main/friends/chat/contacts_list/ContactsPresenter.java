package com.ace.member.main.friends.chat.contacts_list;

import android.content.Context;

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

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class ContactsPresenter extends BasePresenter implements ContactsContract.ContactsPresenter {
	private final ContactsContract.ContactsView mView;
	private String firstLetter = "";

	@Inject
	public ContactsPresenter(Context context, ContactsContract.ContactsView contactsView) {
		super(context);
		mView = contactsView;
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
