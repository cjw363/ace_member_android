package com.ace.member.main.currency.coupon.unvailable;

import com.ace.member.main.currency.coupon.base_coupon.BaseCouponFragment;
import com.ace.member.utils.M;


public class UnAvailableCouponFragment extends BaseCouponFragment {

	@Override
	protected int getStatus() {
		return M.CouponStatus.STATUS_4_UN_USED_EXPIRE;
	}
}
