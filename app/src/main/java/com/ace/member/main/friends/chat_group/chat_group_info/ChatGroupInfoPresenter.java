package com.ace.member.main.friends.chat_group.chat_group_info;

import android.content.Context;

import com.ace.member.base.BasePresenter;
import com.ace.member.bean.ChatGroupInfoBean;
import com.ace.member.main.friends.db.dao.ChatDao;
import com.ace.member.utils.Session;
import com.og.http.SimpleRequestListener;
import com.og.utils.JsonUtil;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class ChatGroupInfoPresenter extends BasePresenter implements ChatGroupInfoContract.ChatInfoPresenter {

	private final ChatGroupInfoContract.ChatInfoView mView;

	@Inject
	ChatGroupInfoPresenter(Context context, ChatGroupInfoContract.ChatInfoView chatInfoView) {
		super(context);
		mView = chatInfoView;
	}

	@Override
	public void getGroupChatInfo(int chatID) {
		HashMap<String, String> map = new HashMap<>();
		map.put("_a", "friends");
		map.put("_b", "aj");
		map.put("_s", Session.sSid);
		map.put("cmd", "getChatGroupInfo");
		map.put("chat_id", chatID + "");
		submit(map, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				ChatGroupInfoBean data = JsonUtil.jsonToBean(result, ChatGroupInfoBean.class);
				mView.setChatGroupData(data);
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
