package com.ace.member.gesture_lock_setup;

import android.content.Context;

import com.ace.member.base.BasePresenter;

import javax.inject.Inject;

public class LockSetupPresenter extends BasePresenter implements LockSetupContract.LockSetupPresenter{

	private final LockSetupContract.LockSetupView mLockSetupView;

	@Inject
	public LockSetupPresenter(Context context, LockSetupContract.LockSetupView mLockSetupView) {
		super(context);
		this.mLockSetupView = mLockSetupView;
	}

	protected void saveGestureSuccess(String gesture){
		mLockSetupView.toMainActivity(gesture);
	}
}
