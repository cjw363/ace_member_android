package com.ace.member.main.friends.chat.chat_info;

import android.content.Context;

import com.ace.member.base.BasePresenter;
import com.ace.member.bean.MemberChatInfoBean;
import com.ace.member.main.friends.db.dao.ChatDao;
import com.ace.member.utils.Session;
import com.og.http.SimpleRequestListener;
import com.og.utils.JsonUtil;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class ChatInfoPresenter extends BasePresenter implements ChatInfoContract.ChatInfoPresenter {

	private final ChatInfoContract.ChatInfoView mView;

	@Inject
	ChatInfoPresenter(Context context, ChatInfoContract.ChatInfoView chatInfoView) {
		super(context);
		mView = chatInfoView;
	}

	@Override
	public void getMemberChatInfo(int chatID, int relateID) {
		HashMap<String, String> map = new HashMap<>();
		map.put("_a", "friends");
		map.put("_b", "aj");
		map.put("_s", Session.sSid);
		map.put("cmd", "getMemberChatInfo");
		map.put("chat_id", chatID + "");
		map.put("member_id", relateID + "");
		submit(map, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				MemberChatInfoBean memberChatInfoBean = JsonUtil.jsonToBean(result, MemberChatInfoBean.class);
				mView.setSingleChatData(memberChatInfoBean);
			}

			@Override
			public void loadFailure(int errorCode, String result, String uniqueToken) {

			}
		});
	}

	@Override
	public void setChatMute(int chatID, int flagMute) {
		HashMap<String, String> map = new HashMap<>();
		map.put("_a", "friends");
		map.put("_b", "aj");
		map.put("_s", Session.sSid);
		map.put("cmd", "setChatMute");
		map.put("chat_id", chatID + "");
		map.put("flag_mute", flagMute + "");
		submit(map, new SimpleRequestListener() {}, false);
	}

	@Override
	public void clearChatHistory(int chatID) {
		//清理本地聊天信息
		ChatDao.getInstance().clearChatHistory(chatID);
		Map<String, String> m = new HashMap<>();
		m.put("_a", "friends");
		m.put("_b", "aj");
		m.put("_s", Session.sSid);
		m.put("chat_id", chatID + "");
		m.put("cmd", "updateChatLastRead");
		submit(m, new SimpleRequestListener() {}, false);
	}
}
