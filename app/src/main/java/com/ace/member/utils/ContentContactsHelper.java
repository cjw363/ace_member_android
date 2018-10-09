package com.ace.member.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.ace.member.bean.ContactInfo;
import com.og.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ContentContactsHelper {
	/**
	 * 读取联系人
	 *
	 * @param context
	 */
	public static List<ContactInfo> readContacts(Context context) {
		Cursor cursor = null;
		Cursor phoneCursor = null;
		try {
			cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
			if (cursor != null && cursor.getCount() > 0) {
				List<ContactInfo> contactInfoList = new ArrayList<>();
				while (cursor.moveToNext()) {
					String name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));//联系人名字
					String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)); //联系人ID
					//根据联系人的ID来读取电话号码
					phoneCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{contactId}, null, null);
					// 取得电话号码(可能存在多个号码)
					if (phoneCursor != null && phoneCursor.getCount() > 0) {
						while (phoneCursor.moveToNext()) {
							String phone = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)); //普通数据类型（电话号码）
							contactInfoList.add(new ContactInfo(name, Utils.formatPhone3(phone)));
						}
					}
				}
				return contactInfoList;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (cursor != null) cursor.close();
			if (phoneCursor != null) phoneCursor.close();
		}
		return null;
	}


}
