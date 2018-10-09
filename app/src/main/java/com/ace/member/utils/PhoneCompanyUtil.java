package com.ace.member.utils;


import com.ace.member.R;

public class PhoneCompanyUtil {
	public static int getPhoneCompanyResourceByName(String name) {
		String iconName = "ic_top_up_" + name.toLowerCase();
		int iconDrawableID = BaseApplication.getContext().getResources().getIdentifier(BaseApplication.getContext().getPackageName() + ":drawable/" + iconName, null, null);
		return iconDrawableID == 0 ? R.drawable.ic_top_up_ais : iconDrawableID;
	}
}
