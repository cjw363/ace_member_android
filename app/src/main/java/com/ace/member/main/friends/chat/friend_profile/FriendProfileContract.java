package com.ace.member.main.friends.chat.friend_profile;

import com.ace.member.bean.FriendProfileInfo;

public interface FriendProfileContract {
	interface FriendProfileView{

		void showFriendProfile(FriendProfileInfo friendProfileInfo);

		void showBtnResult(Boolean resultBoolean);

		void showRemarkName(Boolean resultBoolean, String nameRemark);
	}
	interface FriendProfilePresenter{

		void getFriendProfileInfo(int memberId);

		void rejectApplication(int memberId);

		void addApplication(final int friendId, String content, int addType);

		void remarkName(int memberId, String inputStr);
	}
}
