package com.ace.member.main.friends.chat.contacts_list;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class ContactsPresenterModule {
	private final ContactsContract.ContactsView mView;
	private final Context mContext;

	public ContactsPresenterModule(ContactsContract.ContactsView view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	ContactsContract.ContactsView provideContactsView() {
		return mView;
	}

	@Provides
	Context provideContext() {
		return mContext;
	}
}
