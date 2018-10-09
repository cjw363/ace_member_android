package com.ace.member.main.friends.chat.add_contact;

import com.ace.member.bean.ContactInfo;
import com.ace.member.bean.NewFriendsInfo;

import java.util.List;

public interface AddContactContract {
	interface AddContactView {
		void showNewFriendsList(List<NewFriendsInfo> newFriendsInfoList);

		void showAcceptResult(Boolean isSuccess, int memberId, String nameRemark);

		void showMayKnowFriends(List<NewFriendsInfo> mayKnowFriendsList);

		void showAddResult(Boolean isSuccess);

		void toChatActivity(int id, String name);

		void toFriendProfileActivity(int member_id);
	}

	interface AddContactPresenter {
		void getNewFriendsList();

		void acceptApplication(int id, String nameRemark, int addType);

		void getMayKnowFriends(List<ContactInfo> contactInfoList);

		void addApplication(int friendId, String content, int addType);

		void getMemberIdByPhone(String phone);

		void isFriend(String phone);
	}
}
