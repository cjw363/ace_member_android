package com.ace.member.view;


import com.jungly.gridpasswordview.GridPasswordView;
import com.jungly.gridpasswordview.PasswordType;

public interface MyPasswordView {

	//void setError(String error);

	String getPassWord();

	void clearPassword();

	void setPassword(String password);

	void setPasswordVisibility(boolean visible);

	void togglePasswordVisibility();

	void setOnPasswordChangedListener(GridPasswordView.OnPasswordChangedListener listener);

	void setPasswordType(PasswordType passwordType);
}
