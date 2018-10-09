package com.ace.member.main.third_party.lotto.lotto90.multiple;

import com.ace.member.bean.Product;

import java.util.Map;


public interface Lotto90MultipleContract {
	interface Lotto90MultipleContractView{
		void initGame(double balance, double betAmount, int maxNumber, Product product);
		Map<String,String> getParams();
		void updateInfo(double balance);
		void closeBetting();
		void againBetting();
	}
	interface LottoMultipleContractPresenter{
		void getGameInfo();
		void placeOrder();
	}
}
