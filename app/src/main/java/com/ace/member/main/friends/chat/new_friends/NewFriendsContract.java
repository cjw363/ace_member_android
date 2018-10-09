package com.ace.member.main.friends.chat.new_friends;

import com.ace.member.bean.ContactInfo;
import com.ace.member.bean.NewFriendsInfo;

import java.util.List;

public interface NewFriendsContract {
	interface NewFriendsView {
		void showNewFriendsList(List<NewFriendsInfo> newFriendsInfoList);

		void showAcceptResult(Boolean isSuccess, int memberId, String nameRemark);

		void showMayKnowFriends(List<NewFriendsInfo> mayKnowFriendsList);

		void showAddResult(Boolean isSuccess);
	}

	interface NewFriendsPresenter {
		void getNewFriendsList();

		void acceptApplication(int id, String nameRemark, int addType);

		void getMayKnowFriends(List<ContactInfo> contactInfoList);

		void addApplication(int friendId, String content, int addType);
	}
}
