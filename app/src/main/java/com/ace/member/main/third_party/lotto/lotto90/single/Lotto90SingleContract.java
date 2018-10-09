package com.ace.member.main.third_party.lotto.lotto90.single;

import com.ace.member.bean.Product;

import java.util.Map;

public interface Lotto90SingleContract {
	interface Lotto90SingleContractView {
		void initInfo(double betAmount, Product product);
		Map<String,String> getParams();
		void closeBetting();
		void againBetting();
	}

	interface Lotto90SingleContractPresenter {
		void getProduct();
		void placeOrders();
	}
}
