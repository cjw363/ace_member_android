package com.ace.member.main.currency.coupon.base_coupon;

import com.ace.member.bean.Coupon;

import java.util.List;

public interface BaseCouponContract {

	interface View {
		void addList(int nextPage, List<Coupon> list, boolean isHint);
	}

	interface Presenter {
		void start();

		void getList(int page,int status);
	}
}
