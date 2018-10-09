package com.ace.member.main.friends.chat.contacts_list;

import dagger.Component;

@Component(modules = ContactsPresenterModule.class)
public interface ContactsComponent {
	void inject(ContactsActivity contactsActivity);
}
