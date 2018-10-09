package com.ace.member.main.currency.coupon;

import dagger.Component;

@Component(modules = CouponPresenterModule.class)
public interface CouponComponent {
	void inject(CouponActivity activity);
}

