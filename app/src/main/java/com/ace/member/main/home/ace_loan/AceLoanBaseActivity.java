package com.ace.member.main.home.ace_loan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.ace.member.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public abstract class AceLoanBaseActivity extends BaseActivity {

	protected final static int ACTION_1_TYPE = 1;
	protected final static int ACTION_2_TYPE = 2;

	protected static List<BaseActivity> mListLoanActivity = new ArrayList<>();
	protected int mActionType = 0;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	protected void setHeight(BaseAdapter adapter, ListView listView) {
		int listViewHeight = 0;
		int adaptCount = adapter.getCount();
		for (int i = 0; i < adaptCount; i++) {
			View temp = adapter.getView(i, null, listView);
			temp.measure(0, 0);
			listViewHeight += temp.getMeasuredHeight();
		}
		ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
		layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
		layoutParams.height = listViewHeight;
		listView.setLayoutParams(layoutParams);
	}
}
