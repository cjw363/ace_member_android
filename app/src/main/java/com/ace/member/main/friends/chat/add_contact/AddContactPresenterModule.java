package com.ace.member.main.friends.chat.add_contact;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class AddContactPresenterModule {
	private final AddContactContract.AddContactView mView;
	private final Context mContext;

	public AddContactPresenterModule(AddContactContract.AddContactView view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	AddContactContract.AddContactView provideAddContactView() {
		return mView;
	}

	@Provides
	Context provideContext() {
		return mContext;
	}
}
