package com.ace.member.main.home.transfer.to_member;


import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class ToMemberPresenterModule {

	private final ToMemberContract.View mView;
	private final Context mContext;

	public ToMemberPresenterModule(ToMemberContract.View mView, Context mContext) {this.mView = mView;
		this.mContext = mContext;
	}

	@Provides
	ToMemberContract.View providesToMemberContractView(){return mView;}

	@Provides
	Context providesContext(){return mContext;}

}
