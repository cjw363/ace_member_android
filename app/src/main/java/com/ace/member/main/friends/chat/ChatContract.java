package com.ace.member.main.friends.chat;

import com.ace.member.bean.ChatMsg;

import java.util.List;

public interface ChatContract {
	interface ChatView {
		void showChatMsg(List<ChatMsg> list, int currentPage, int position);

		void addLastChatMsg(ChatMsg chatMsg);

		void noMoreData(boolean isFirstLoad);

		void refreshFail(int currentPage);

		void playVoice(String filePath);

		void getNewChatMsgResult();

		void setMute(int flagMuteNotifications);
	}

	interface ChatPresenter {
		void getChatMsg(int chatId, int currentPage);

		void sendMsg(String content, int contentType, int memberId);

		void receiveMsg(int chatId);

		//    void sendMediaMsg(ChatMsgEvent msgEvent, String contactID, String groupID);

		void playVoice(String path);

		void checkUpdateChatMsg(int chatId, int currentPage);

		void updateChatMsg(int chatId);

		void getNewChatMsg(int memberId);

		void addTransferMsg(int transferID, int memberId);
	}
}
