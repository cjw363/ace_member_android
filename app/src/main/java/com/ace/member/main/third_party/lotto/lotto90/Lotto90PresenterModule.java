package com.ace.member.main.third_party.lotto.lotto90;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class Lotto90PresenterModule {

	private final Lotto90Contract.View mView;
	private final Context mContext;

	public Lotto90PresenterModule(Lotto90Contract.View view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	Lotto90Contract.View provideView() {
		return mView;
	}

	@Provides
	Context provideContext() {
		return mContext;
	}
}
