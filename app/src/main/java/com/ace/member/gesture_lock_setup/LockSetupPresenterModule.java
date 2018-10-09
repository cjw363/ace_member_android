package com.ace.member.gesture_lock_setup;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class LockSetupPresenterModule {

	private final LockSetupContract.LockSetupView mLockSetupView;
	private final Context mContext;

	public LockSetupPresenterModule(LockSetupContract.LockSetupView mLockSetupView, Context mContext) {
		this.mLockSetupView = mLockSetupView;
		this.mContext = mContext;
	}

	@Provides
	LockSetupContract.LockSetupView provideLockSetupContractView() {
		return mLockSetupView;
	}

	@Provides
	Context providesContext() {
		return mContext;
	}
}
