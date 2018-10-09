package com.ace.member.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ace.member.R;


public class ResultListViewFooter extends LinearLayout {
	public final static int STATE_NORMAL = 0;//
	public final static int STATE_READY = 1;
	public final static int STATE_LOADING = 2;

	private Context mContext;
	private View mContextView;
	private View mProgressBar;
	private TextView mHintView;

	public ResultListViewFooter(Context context) {
		super(context);
		initView(context);
	}

	public ResultListViewFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public void setState(int state) {
		mHintView.setVisibility(INVISIBLE);
		mProgressBar.setVisibility(INVISIBLE);
		if (state == STATE_READY) {
			mProgressBar.setVisibility(VISIBLE);
			mHintView.setVisibility(VISIBLE);
			mHintView.setText(getResources().getString(R.string.release_to_load_more));
		}
				else if (state == STATE_LOADING) {
			mProgressBar.setVisibility(VISIBLE);
			mHintView.setVisibility(VISIBLE);
			mHintView.setText(getResources().getString(R.string.loading));
				}
				else {

			mHintView.setVisibility(GONE);
			//			mHintView.setText(R.string.load);
//					mHintView.setText(R.string.pull_up_load_more);
				}
	}

	public void setBottomMargin(int height) {
		if (height < 0) return;
		LayoutParams lp = (LayoutParams) mContextView.getLayoutParams();
		lp.bottomMargin = height;
		mContextView.setLayoutParams(lp);
	}

	public int getBottomMargin() {
		LayoutParams lp = (LayoutParams) mContextView.getLayoutParams();
		return lp.bottomMargin;
	}

	/**
	 * normal status
	 */
	public void normal() {
		mHintView.setVisibility(VISIBLE);
		mProgressBar.setVisibility(GONE);
	}

	/**
	 * loading status
	 */
	public void loading() {
		mHintView.setVisibility(GONE);
		mProgressBar.setVisibility(VISIBLE);
	}

	/**
	 * hide footer when disable pull load more
	 */
	public void hide() {
		LayoutParams lp = (LayoutParams) mContextView.getLayoutParams();
		lp.height = 0;
		mContextView.setLayoutParams(lp);
	}

	/**
	 * show footer
	 */
	public void show() {
		LayoutParams lp = (LayoutParams) mContextView.getLayoutParams();
		lp.height = LayoutParams.WRAP_CONTENT;
		mContextView.setLayoutParams(lp);
	}

	private void initView(Context context) {
		mContext = context;
		LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext).inflate(com.og.R.layout.rlistview_footer, null);
		addView(moreView);
		moreView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		mContextView = moreView.findViewById(com.og.R.id.lv_footer_content);
		mProgressBar = moreView.findViewById(com.og.R.id.lv_footer_progressbar);
		mHintView = (TextView) moreView.findViewById(com.og.R.id.lv_footer_hint_textview);
	}

}
