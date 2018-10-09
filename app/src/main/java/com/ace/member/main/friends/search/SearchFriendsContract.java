package com.ace.member.main.friends.search;

import com.ace.member.bean.SearchFriendsInfo;

import java.util.List;

public interface SearchFriendsContract {
	interface SearchFriendsView {
		void showSearchResult(List<SearchFriendsInfo> searchFriendsInfoList);

		void showAddResult(boolean isSuccess);

		void showAcceptResult(Boolean isSuccess, int memberId, String nameRemark);
	}

	interface SearchFriendsPresenter {
		void searchKeyWord(String keyWord);

		void addApplication(int friendId, String content, int addType);

		void acceptApplication(int id, String nameRemark, int addType);
	}
}
