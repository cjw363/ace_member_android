package com.ace.member.main.friends.chat_group.chat_group_info;

import com.ace.member.bean.ChatGroupInfoBean;

public interface ChatGroupInfoContract {
	interface ChatInfoView {

		void setChatGroupData(ChatGroupInfoBean chatGroupData);
	}

	interface ChatInfoPresenter {

		void getGroupChatInfo(int chatID);

		void setChatMute(int chatID, int flagMute);

		void clearChatHistory(int chatID);
	}
}
