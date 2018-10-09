package com.ace.member.main.me.service_point;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
class ServicePointPresenterModule {

	private final ServicePointContract.ServicePointView mView;
	private final Context mContext;

	ServicePointPresenterModule(ServicePointContract.ServicePointView view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	ServicePointContract.ServicePointView provideServicePointContractView() {
		return mView;
	}

	@Provides
	Context providesContext() {
		return mContext;
	}
}
