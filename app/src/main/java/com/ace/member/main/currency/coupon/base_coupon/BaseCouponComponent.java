package com.ace.member.main.currency.coupon.base_coupon;

import dagger.Component;

@Component(modules = BaseCouponPresenterModule.class)
public interface BaseCouponComponent {
	void inject(BaseCouponFragment fragment);
}

