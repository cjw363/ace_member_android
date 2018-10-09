package com.ace.member.base;


import android.view.View;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.listener.IMyViewOnClickListener;
import com.ace.member.main.bottom_dialog.BottomDialog;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.Session;
import com.og.utils.ItemObject;
import com.og.utils.Utils;

import org.json.JSONArray;

import java.util.List;

public abstract class BaseCountryCodeFragment extends BaseFragment {

	public void showCountryCode(final TextView textView) {
		try {
			if (Session.countryCodeList == null) return;
			final CharSequence[] tvContent2 = new CharSequence[Session.countryCodeList.size()];
			final int[] tvContent1 = new int[Session.countryCodeList.size()];
			final int[] codeRes = new int[Session.countryCodeList.size()];
			for (int i = 0; i < Session.countryCodeList.size(); i++) {
				ItemObject item = Session.countryCodeList.get(i);
				tvContent2[i] = item.getValue();
				switch (item.getKey()) {
					case AppGlobal.COUNTRY_CODE_855_CAMBODIA:
						tvContent1[i] = R.string.cambodia;
						codeRes[i] = R.drawable.ic_cambodia;
						break;
					case AppGlobal.COUNTRY_CODE_84_VIETNAM:
						tvContent1[i] = R.string.vietnam;
						codeRes[i] = R.drawable.ic_vietnam;
						break;
					case AppGlobal.COUNTRY_CODE_66_THAILAND:
						tvContent1[i] = R.string.thailand;
						codeRes[i] = R.drawable.ic_thailand;
						break;
					case AppGlobal.COUNTRY_CODE_86_CHINA:
						tvContent1[i] = R.string.china;
						codeRes[i] = R.drawable.ic_china;
						break;
					default:
						tvContent1[i] = R.string.cambodia;
						codeRes[i] = R.drawable.ic_cambodia;
						break;
				}

			}

			BottomDialog.Builder builder = new BottomDialog.Builder(getActivity()).setTvTitle(Utils.getString(R.string.area_code_type)).setTvContent1(tvContent1).setTvContent2(tvContent2).setIvRes(codeRes).setClickListener(new IMyViewOnClickListener () {
				@Override
				public void onClick(View view, int position) {
					String curValue = tvContent2[position].toString();
					setCountryCode(curValue, textView);
				}

				@Override
				public void onLongClick(View view, int position) {

				}

				@Override
				public void onItemClick(List list, View view, int position) {

				}

				@Override
				public void onItemClick(JSONArray data, View view, int position) {

				}
			});
			builder.createAndShow();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setCountryCode(String curValue, TextView textView) {}
}
