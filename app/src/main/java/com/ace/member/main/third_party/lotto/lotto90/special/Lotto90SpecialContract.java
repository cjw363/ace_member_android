package com.ace.member.main.third_party.lotto.lotto90.special;



import com.ace.member.bean.Product;

import java.util.Map;

public interface Lotto90SpecialContract {
	interface Lotto90SpecialContractView{
		void initInfo(double betAmount, int maxTimes, Product product);
		Map<String,String> getParams();
		void closeBetting();
		void againBetting();
	}
	interface Lotto90SpecialContractPresenter{
		void getProduct();
		void placeOrder();
	}
}
