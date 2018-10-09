package com.ace.member.main.currency.coupon.available;

import com.ace.member.main.currency.coupon.base_coupon.BaseCouponFragment;
import com.ace.member.utils.M;


public class AvailableCouponFragment extends BaseCouponFragment {

	@Override
	protected int getStatus() {
		return M.CouponStatus.STATUS_1_UN_USED;
	}
}
