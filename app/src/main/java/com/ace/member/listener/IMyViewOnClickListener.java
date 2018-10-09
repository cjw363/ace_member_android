package com.ace.member.listener;


import android.view.View;

import org.json.JSONArray;

import java.util.List;

public interface IMyViewOnClickListener {
	void onClick(View view, int position);

	void onLongClick(View view, int position);

	void onItemClick(List list, View view, int position);

	void onItemClick(JSONArray data, View view, int position);
}
