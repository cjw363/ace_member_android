package com.ace.member.main.friends.chat.contacts_list;

import com.ace.member.bean.ContactInfo;

import java.util.List;

public interface ContactsContract {
	interface ContactsView {
		void showContactsList(List<ContactInfo> contactInfoList);
	}

	interface ContactsPresenter {
		void getContactsList();
	}

}
