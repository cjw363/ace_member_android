package com.ace.member.main.home.transfer;


import java.util.List;

public interface TransferContract {
	interface TransferView{

		void showRecentData(List<TransferBean> list);

		void showFunction(int isRelateMerchant,int isRelatePartner);
	}

	interface TransferPresenter {

	}
}
