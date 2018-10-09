package com.og.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.og.R;
import com.og.adapter.AbstractSpinnerAdapter;
import com.og.adapter.NormalSpinnerAdapter;
import com.og.utils.Utils;

import java.util.List;

public class SpinnerPopWindow2 extends PopupWindow implements AdapterView.OnItemClickListener {
	private Context mContext;
	private ListView mListView;
	private AbstractSpinnerAdapter mAdapter;
	private AbstractSpinnerAdapter.IOnItemSelectListener mItemSelectListener;
	private String title;


	public SpinnerPopWindow2(Context context, String title) {
		super(context);

		mContext = context;
		this.title = title;
		init();
	}


	public void setItemListener(AbstractSpinnerAdapter.IOnItemSelectListener listener) {
		mItemSelectListener = listener;
	}

	private void init() {
		final View view = LayoutInflater.from(mContext).inflate(R.layout.spinner_window_layout2, null);
		setContentView(view);
		setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

		setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		setBackgroundDrawable(dw);
		setAnimationStyle(R.style.AnimBottom);
		setOutsideTouchable(true);
		setClippingEnabled(false);
		setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);


		mListView = (ListView) view.findViewById(R.id.lvContents);

		if(!title.equals("")){
			TextView tv = (TextView) view.findViewById(R.id.title);
			if(tv != null) tv.setText(title);
		}

		ScrollView svPopupWindow = (ScrollView) view.findViewById(R.id.svPopupWindow);
		if(svPopupWindow != null){
			DisplayMetrics display = Utils.getScreenSize();
			svPopupWindow.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, display.heightPixels*2/5));
		}


		mAdapter = new NormalSpinnerAdapter(mContext);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);

		view.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				int height = view.findViewById(R.id.llPopupWindow).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});
	}

	public <T> void refreshData(List<T> list, int selIndex, boolean bool, float textSize, boolean phone) {
		if (list != null && selIndex != -1) {
			if (mAdapter != null) mAdapter.refreshData(list, selIndex, bool, textSize,phone);
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
