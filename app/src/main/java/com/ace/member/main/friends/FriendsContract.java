package com.ace.member.main.friends;

import com.ace.member.bean.RecentMsg;

import java.util.List;

public interface FriendsContract {
	interface FriendsView {
		void showRecentMsgList(List<RecentMsg> recentMsgList);

		void toChatActivity(int id, String name);

		void toFriendProfileActivity(int member_id);
	}

	interface FriendsPresenter {
		void getFriendsMsgList();

		void getMemberIdByPhone(String phone);

		void isFriend(String phone);

		void getFriendRequestList();
	}
}
