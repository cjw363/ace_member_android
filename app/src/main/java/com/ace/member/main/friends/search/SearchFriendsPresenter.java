package com.ace.member.main.friends.search;

import android.content.Context;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.bean.SearchFriendDataWrapper;
import com.ace.member.bean.SearchFriendsInfo;
import com.ace.member.service.IMWebSocketService;
import com.ace.member.utils.Session;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class SearchFriendsPresenter extends BasePresenter implements SearchFriendsContract.SearchFriendsPresenter {
	private static final int TYPE_SEARCH_1_TITLE = 1;


	private final SearchFriendsContract.SearchFriendsView mView;

	@Inject
	public SearchFriendsPresenter(Context context, SearchFriendsContract.SearchFriendsView mView) {
		super(context);
		this.mView = mView;
	}

	@Override
	public void searchKeyWord(String keyWord) {
		Map<String, String> map = new HashMap<>();
		map.put("_a", "friends");
		map.put("_b", "aj");
		map.put("_s", Session.sSid);
		map.put("key_word", keyWord);
		map.put("cmd", "searchFriendKeyWord");
		submit(map, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				try {
					SearchFriendDataWrapper searchDataWrapper = JsonUtil.jsonToBean(result, SearchFriendDataWrapper.class);
					assert searchDataWrapper != null;
					List<SearchFriendsInfo> searchFriendsInfoList = searchDataWrapper.getSearchFriendsInfoList();
					if (searchFriendsInfoList != null && searchFriendsInfoList.size() != 0) {
						SearchFriendsInfo searchFriendsInfo = new SearchFriendsInfo();
						searchFriendsInfo.setType(TYPE_SEARCH_1_TITLE);//手动添加标题
						searchFriendsInfo.setContent(Utils.getString(R.string.contacts));
						searchFriendsInfoList.add(0, searchFriendsInfo);
					}
					mView.showSearchResult(searchFriendsInfoList);
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
				Boolean resultBoolean = Boolean.valueOf(result);
				if (resultBoolean) {
					IMWebSocketService.sendFriendMsg(friendId, IMWebSocketService.IM_SOCKET_TYPE_22_REQUEST_MSG);//通知对方
				}
				mView.showAddResult(resultBoolean);
			}

			@Override
			public void loadFailure(int errorCode, String result, String uniqueToken) {
				Utils.showToast(R.string.fail);
			}
		});
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
				Boolean resultBoolean = Boolean.valueOf(result);
				if (resultBoolean) {
					IMWebSocketService.sendFriendMsg(memberId, IMWebSocketService.IM_SOCKET_TYPE_11_CHAT_MSG);//通知对方
					IMWebSocketService.sendFriendMsg(Session.user.getId(), IMWebSocketService.IM_SOCKET_TYPE_11_CHAT_MSG);//通知自己
				}
				mView.showAcceptResult(resultBoolean, memberId, nameRemark);
			}

			@Override
			public void loadFailure(int errorCode, String result, String uniqueToken) {
				Utils.showToast(R.string.fail);
			}
		});
	}
}
