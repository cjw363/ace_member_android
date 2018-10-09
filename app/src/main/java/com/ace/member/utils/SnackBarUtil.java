package com.ace.member.utils;


import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.ace.member.R;
import com.og.utils.Utils;

public class SnackBarUtil {
	public static void show(View view, CharSequence s) {
		show(view, s, Snackbar.LENGTH_SHORT, null);
	}

	public static void show(View view, CharSequence s, int duration) {
		show(view, s, duration, null);
	}

	public static void show(View view, int infoRes) {
		show(view, infoRes, Snackbar.LENGTH_SHORT);
	}

	public static void show(View view, int infoRes, int duration) {
		show(view, Utils.getString(infoRes), duration, null);
	}

	public static void show(View view, int infoRes, int duration, Snackbar.Callback callback) {
		show(view, Utils.getString(infoRes), duration, callback);
	}

	public static void show(View view, int infoRes, Snackbar.Callback callback) {
		show(view, Utils.getString(infoRes), Snackbar.LENGTH_SHORT, callback);
	}

	public static void show(View view, CharSequence s, Snackbar.Callback callback) {
		show(view, s, Snackbar.LENGTH_SHORT, callback);
	}

	public static void show(View view, CharSequence s, int duration, Snackbar.Callback callback) {
		Snackbar snackbar = Snackbar.make(view, s, duration);
		snackbar.getView().getLayoutParams().width = Utils.getScreenWidth();
		TextView tvSnackBar = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
		tvSnackBar.setTextSize(Utils.getDimenDp(R.dimen.txtSize8));
		tvSnackBar.setMaxLines(5);
		if(callback!=null) snackbar.addCallback(callback);
		snackbar.show();
	}
}
