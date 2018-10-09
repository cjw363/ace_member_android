package com.ace.member.main.me.about;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class AboutPresenterModule {

	private final AboutContract.AboutView mAboutView;
	private final Context mContext;

	public AboutPresenterModule(AboutContract.AboutView mAboutView, Context mContext) {this.mAboutView = mAboutView;
		this.mContext = mContext;
	}

	@Provides
	AboutContract.AboutView providesAboutContractView(){return mAboutView;}

	@Provides
	Context providesContext(){return mContext;}
}
