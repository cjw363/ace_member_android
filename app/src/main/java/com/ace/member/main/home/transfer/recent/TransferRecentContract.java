package com.ace.member.main.home.transfer.recent;

import com.ace.member.bean.TransferRecent;

import java.util.List;

public interface TransferRecentContract {
		interface TransferRecentView{

			void addRecentList(int nextPage, List<TransferRecent> list, boolean b);
		}

		interface TransferRecentPresenter {

		}
}
