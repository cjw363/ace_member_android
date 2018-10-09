package com.og.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import com.og.R;
import com.og.adapter.AbstractSpinnerAdapter;
import com.og.adapter.NormalSpinnerAdapter;

import java.util.List;

public class SpinnerPopWindow extends PopupWindow implements AdapterView.OnItemClickListener {
	private Context mContext;
	private ListView mListView;
	private AbstractSpinnerAdapter mAdapter;
	private AbstractSpinnerAdapter.IOnItemSelectListener mItemSelectListener;


	public SpinnerPopWindow(Context context) {
		super(context);

		mContext = context;
		init();
	}


	public void setItemListener(AbstractSpinnerAdapter.IOnItemSelectListener listener) {
		mItemSelectListener = listener;
	}

	private void init() {
		View view = LayoutInflater.from(mContext).inflate(R.layout.spinner_window_layout, null);
		setContentView(view);
		setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
		setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

		setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0x00);
		setBackgroundDrawable(dw);


		mListView = (ListView) view.findViewById(R.id.lv_spinner_window);


		mAdapter = new NormalSpinnerAdapter(mContext);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
	}


	public <T> void refreshData(List<T> list, int selIndex, boolean bool, float textSize) {
		if (list != null && selIndex != -1) {
			if (mAdapter != null) mAdapter.refreshData(list, selIndex, bool, textSize,false);
		}
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int pos, long arg3) {
		dismiss();
		if (mItemSelectListener != null) {
			mItemSelectListener.onItemClick(pos);
		}
	}
}
