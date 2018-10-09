package com.ace.member.main.friends.chat.chat_info.transfer_history;

import com.ace.member.bean.TransferRecent;

import java.util.List;

public interface FriendTransferHistoryContract {
		interface TransferRecentView{

			void setRecentList(int nextPage, List<TransferRecent> list, boolean b);
		}

		interface TransferRecentPresenter {

		}
}
