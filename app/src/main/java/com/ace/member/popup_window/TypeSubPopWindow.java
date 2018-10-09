package com.ace.member.popup_window;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.ace.member.R;
import com.og.utils.Utils;

public class TypeSubPopWindow extends PopupWindow {
	private View mContentView;

	public TypeSubPopWindow(final FragmentActivity context){
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mContentView = inflater.inflate(R.layout.view_more_popup_dialog, null);
		int h = Utils.getScreenHeight();//context.getWindowManager().getDefaultDisplay().getHeight();
		int w = Utils.getScreenWidth();//context.getWindowManager().getDefaultDisplay().getWidth();
		this.setContentView(mContentView);
		this.setWidth(w/4);//w / 2 - 50
		this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		this.update();
		ColorDrawable dw = new ColorDrawable(0000000000);
		this.setBackgroundDrawable(dw);
		// mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);

		LinearLayout btnHistory=(LinearLayout) mContentView.findViewById(R.id.history_layout);
		btnHistory.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
//				Intent intent=new Intent(context,HistoryActivity.class);
//				context.startActivityForResult(intent, AppGlobal.SELECT_BOOKING_1_RESULT);
			}
		});

		this.setAnimationStyle(R.style.AnimationPreview);
	}

	public void showPopupWindow(View parent,int x,int y) {
		if (!this.isShowing()) {
			this.showAsDropDown(parent,x,y);//parent.getLayoutParams().width / 2, 0
//			this.showAsDropDown(parent,getWindowManager().getDefaultDisplay().getWidth() /5,(parent.getHeight()+1)*(position-listView.getFirstVisiblePosition() +1/4));
//			this.showAtLocation(parent, Gravity.BOTTOM, x, y);
		} else {
			this.dismiss();
		}
	}
}
