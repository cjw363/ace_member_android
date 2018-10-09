package com.ace.member.main.friends.chat_group.build_group;

import com.ace.member.bean.ContactInfo;

import java.util.List;
import java.util.Map;

public interface BuildGroupContract {
	interface BuildGroupView {
		void showContactsList(List<ContactInfo> contactInfoList);

		void toGroupChat(int chatID, String groupName);
	}

	interface BuildGroupPresenter {
		void getContactsList();

		void buildGroupChat(Map<Integer, ContactInfo> selectedMap);
	}

}
