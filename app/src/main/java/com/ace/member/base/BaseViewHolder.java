package com.ace.member.base;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ace.member.view.RoundRectImageView;

import java.util.List;

public class BaseViewHolder<T> {
	private final View mItemView;
	public RoundRectImageView mIvMyIcon, mIvFriendIcon;
	public RelativeLayout mRlTime;
	public TextView mTvTime;

	public BaseViewHolder(View itemView) {
		this.mItemView = itemView;
	}

	public View getView() {
		return mItemView;
	}

	public void setData(int position, List<T> list) {}
}
