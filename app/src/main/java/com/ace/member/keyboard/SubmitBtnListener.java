package com.ace.member.keyboard;


import android.widget.Button;

public class SubmitBtnListener {
	private Button mButton;
	public SubmitBtnListener(Button button) {
		this.mButton = button;
	}

	public void onClick(){
		mButton.callOnClick();
	}

	public boolean isBtnEnabled(){
		return mButton.isEnabled();
	}
}
