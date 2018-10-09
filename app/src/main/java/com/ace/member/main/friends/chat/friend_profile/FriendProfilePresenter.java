package com.ace.member.main.friends.chat.friend_profile;

import android.content.Context;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.bean.FriendProfileInfo;
import com.ace.member.service.IMWebSocketService;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.Session;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class FriendProfilePresenter extends BasePresenter implements FriendProfileContract.FriendProfilePresenter {
	private final FriendProfileContract.FriendProfileView mView;

	@Inject
	public FriendProfilePresenter(Context context, FriendProfileContract.FriendProfileView view) {
		super(context);
		mView = view;
	}

	@Override
	public void getFriendProfileInfo(int memberId) {
		Map<String, String> map = new HashMap<>();
		map.put("_a", "friends");
		map.put("_b", "aj");
		map.put("_s", Session.sSid);
		map.put("member_id", String.valueOf(memberId));
		map.put("cmd", "getFriendProfileInfo");
		submit(map, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				try {
					FriendProfileInfo friendProfileInfo = JsonUtil.jsonToBean(result, FriendProfileInfo.class);
					mView.showFriendProfile(friendProfileInfo);
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

	@Override
	public void rejectApplication(int memberId) {
		Map<String, String> m = new HashMap<>();
		m.put("_a", "friends");
		m.put("_b", "aj");
		m.put("_s", Session.sSid);
		m.put("friend_id", String.valueOf(memberId));
		m.put("cmd", "rejectContactRequest");
		submit(m, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				Boolean resultBoolean = Boolean.valueOf(result);
				mView.showBtnResult(resultBoolean);
			}

			@Override
			public void loadFailure(int errorCode, String result, String uniqueToken) {
				Utils.showToast(R.string.fail);
			}
		});
	}

	@Override
	public void addApplication(final int friendId, String content, int addType) {
		Map<String, String> m = new HashMap<>();
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
				mView.showBtnResult(resultBoolean);
			}

			@Override
			public void loadFailure(int errorCode, String result, String uniqueToken) {
				Utils.showToast(R.string.fail);
			}
		});
	}

	@Override
	public void remarkName(int memberId, final String inputStr) {
		Map<String, String> m = new HashMap<>();
		m.put("_a", "friends");
		m.put("_b", "aj");
		m.put("_s", Session.sSid);
		m.put("member_id", memberId + "");
		m.put("content", inputStr);
		m.put("cmd", "remarkFriendName");
		submit(m, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				mView.showRemarkName(Boolean.valueOf(result), inputStr);
			}

			@Override
			public void loadFailure(int errorCode, String result, String uniqueToken) {
				Utils.showToast(R.string.fail);
			}
		});
	}

	public String getCountryName(int code) {
		switch (code) {
			case AppGlobal.COUNTRY_CODE_855_CAMBODIA:
				return Utils.getString(R.string.cambodia);
			case AppGlobal.COUNTRY_CODE_66_THAILAND:
				return Utils.getString(R.string.thailand);
			case AppGlobal.COUNTRY_CODE_86_CHINA:
				return Utils.getString(R.string.china);
			case AppGlobal.COUNTRY_CODE_84_VIETNAM:
				return Utils.getString(R.string.vietnam);
			default:
				return null;
		}
	}
}
