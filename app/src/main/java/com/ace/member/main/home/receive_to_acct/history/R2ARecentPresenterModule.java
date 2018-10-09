package com.ace.member.main.home.receive_to_acct.history;

import android.content.Context;
import dagger.Module;
import dagger.Provides;

@Module
public class R2ARecentPresenterModule {

	private final R2ARecentContract.View mView;
	private final Context mContext;


	public R2ARecentPresenterModule(R2ARecentContract.View mView, Context mContext) {
		this.mView = mView;
		this.mContext = mContext;
	}

	@Provides
	R2ARecentContract.View provideRecentContractView() {return mView;}

	@Provides
	Context provideContext() {return mContext;}
}
