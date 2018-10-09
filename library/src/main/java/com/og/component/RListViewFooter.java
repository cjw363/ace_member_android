package com.og.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.og.R;

public class RListViewFooter extends LinearLayout {
	public final static int STATE_NORMAL = 0;//
	public final static int STATE_READY = 1;
	public final static int STATE_LOADING = 2;

	private Context mContext;
	private View mContextView;
	private View mProgressBar;
	private TextView mHintView;
	private TextView mLoadMoreView;

	public RListViewFooter(Context context) {
		super(context);
		initView(context);
	}

	public RListViewFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public void setState(int state) {
		mHintView.setVisibility(INVISIBLE);
		mProgressBar.setVisibility(INVISIBLE);
		if (state == STATE_READY || state == STATE_LOADING) {
			mHintView.setVisibility(VISIBLE);
			//			mHintView.setText(getResources().getString(R.string.loosen_load_more));
			mProgressBar.setVisibility(VISIBLE);
			mLoadMoreView.setVisibility(View.INVISIBLE);
		} else {
			mLoadMoreView.setVisibility(View.VISIBLE);
		}
		//		else if (state == STATE_LOADING) {
		//			mProgressBar.setVisibility(VISIBLE);
		//		}
		//		else {
		//			mHintView.setVisibility(VISIBLE);
		//			mHintView.setText(R.string.pull_up_load_more);
		//		}
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
		mLoadMoreView.setVisibility(View.INVISIBLE);
	}

	/**
	 * hide footer when disable pull load more
	 */
	public void hide() {
		LayoutParams lp = (LayoutParams) mContextView.getLayoutParams();
		lp.height = 0;
		mContextView.setLayoutParams(lp);
		mLoadMoreView.setVisibility(View.INVISIBLE);
	}

	/**
	 * show footer
	 */
	public void show() {
		LayoutParams lp = (LayoutParams) mContextView.getLayoutParams();
		lp.height = LayoutParams.WRAP_CONTENT;
		mContextView.setLayoutParams(lp);
		mLoadMoreView.setVisibility(View.VISIBLE);
	}

	private void initView(Context context) {
		mContext = context;
		LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext)
			.inflate(R.layout.rlistview_footer, null);
		addView(moreView);
		moreView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		mContextView = moreView.findViewById(R.id.lv_footer_content);
		mLoadMoreView = (TextView) moreView.findViewById(R.id.lv_footer_textview);
		mProgressBar = moreView.findViewById(R.id.lv_footer_progressbar);
		mHintView = (TextView) moreView.findViewById(R.id.lv_footer_hint_textview);
	}

}
