package com.ace.member.main.friends.chat.chat_info;

import com.ace.member.bean.MemberChatInfoBean;

public interface ChatInfoContract {
	interface ChatInfoView {

		void setSingleChatData(MemberChatInfoBean memberChatInfoBean);
	}

	interface ChatInfoPresenter {

		void getMemberChatInfo(int chatID, int relateID);

		void setChatMute(int chatID, int flagMute);

		void clearChatHistory(int chatID);
	}
}
