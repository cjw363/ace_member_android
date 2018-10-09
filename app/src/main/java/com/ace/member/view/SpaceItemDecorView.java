package com.ace.member.view;


import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpaceItemDecorView extends RecyclerView.ItemDecoration {
	private int topSpace;
	private int leftSpace;
	private boolean isTopToBottom;

	public SpaceItemDecorView(int topSpace) {
		this.topSpace = topSpace;
		this.leftSpace = 0;
		isTopToBottom = true;
	}

	public SpaceItemDecorView(int topSpace, int leftSpace, boolean isTopToBottom) {
		this.topSpace = topSpace;
		this.leftSpace = leftSpace;
		this.isTopToBottom = isTopToBottom;
	}

	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
		int pos = parent.getChildLayoutPosition(view);
		outRect.set(leftSpace / 2 , 0, leftSpace / 2, 0);
		if (isTopToBottom) {
			if (pos > 0) {
				outRect.top = topSpace;
			}
		} else {
			outRect.top = topSpace;
		}

	}
}
