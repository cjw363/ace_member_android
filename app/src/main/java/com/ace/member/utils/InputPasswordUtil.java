package com.ace.member.utils;


import android.support.v7.app.AppCompatActivity;

import com.ace.member.main.input_password.InputPasswordCallback;
import com.ace.member.main.input_password.InputPasswordFragment;

public class InputPasswordUtil {

	public static void inputPassword(AppCompatActivity activity, InputPasswordCallback callback) {
		InputPasswordFragment inputPasswordFragment = new InputPasswordFragment();
		inputPasswordFragment.setContext(activity);
		inputPasswordFragment.setPasswordCallback(callback);
		inputPasswordFragment.setCancelable(false);
		inputPasswordFragment.show(activity.getSupportFragmentManager(), "InputPasswordFragment");

	}
}
