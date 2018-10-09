package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchFriendDataWrapper extends BaseBean{
	@SerializedName("search_friends_list")
	private List<SearchFriendsInfo> mSearchFriendsInfoList;

	@SerializedName("may_know_friends_list")
	private List<NewFriendsInfo> mMayKnowFriendsList;

	public List<NewFriendsInfo> getMayKnowFriendsList() {
		return mMayKnowFriendsList;
	}

	public void setMayKnowFriendsList(List<NewFriendsInfo> mayKnowFriendsList) {
		mMayKnowFriendsList = mayKnowFriendsList;
	}

	public List<SearchFriendsInfo> getSearchFriendsInfoList() {
		return mSearchFriendsInfoList;
	}

	public void setSearchFriendsInfoList(List<SearchFriendsInfo> searchFriendsInfoList) {
		mSearchFriendsInfoList = searchFriendsInfoList;
	}
}
