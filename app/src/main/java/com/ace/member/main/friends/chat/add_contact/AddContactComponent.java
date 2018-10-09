package com.ace.member.main.friends.chat.add_contact;

import dagger.Component;

@Component(modules = AddContactPresenterModule.class)
public interface AddContactComponent {
	void inject(AddContactActivity addContactActivity);

}
