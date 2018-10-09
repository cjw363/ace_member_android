package com.ace.member.view;

import java.util.Map;

public interface PickDialogListener {
	public void onListItemClick(int position, Map<String, String> string);
	public void onListItemLongClick(int position, String string);
	public void onCancel();
}
