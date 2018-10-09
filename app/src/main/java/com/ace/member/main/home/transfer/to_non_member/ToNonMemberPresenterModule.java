package com.ace.member.main.home.transfer.to_non_member;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class ToNonMemberPresenterModule {

	private final ToNonMemberContract.View mView;
	private final Context mContext;

	public ToNonMemberPresenterModule(ToNonMemberContract.View mView, Context mContext) {this.mView = mView;
		this.mContext = mContext;
	}

	@Provides
	ToNonMemberContract.View providesToCashContractView(){return mView;}

	@Provides
	Context providesContext(){return mContext;}
}
