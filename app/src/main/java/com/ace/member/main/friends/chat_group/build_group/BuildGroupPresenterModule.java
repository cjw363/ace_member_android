package com.ace.member.main.friends.chat_group.build_group;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class BuildGroupPresenterModule {
	private final BuildGroupContract.BuildGroupView mView;
	private final Context mContext;

	public BuildGroupPresenterModule(BuildGroupContract.BuildGroupView view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	BuildGroupContract.BuildGroupView provideBuildGroupView() {
		return mView;
	}

	@Provides
	Context provideContext() {
		return mContext;
	}
}
