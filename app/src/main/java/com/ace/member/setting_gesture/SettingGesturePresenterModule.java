package com.ace.member.setting_gesture;

import android.content.Context;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;

@Module
public class SettingGesturePresenterModule {

	private final SettingGestureContract.SettingGestureView mView;
	private final Context mContext;

	@Inject
	public SettingGesturePresenterModule(SettingGestureContract.SettingGestureView view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	SettingGestureContract.SettingGestureView provideSettingGestureContractView() {
		return mView;
	}

	@Provides
	Context providesContext() {
		return mContext;
	}
}
