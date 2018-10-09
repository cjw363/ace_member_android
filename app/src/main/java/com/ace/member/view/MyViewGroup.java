package com.ace.member.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class MyViewGroup extends ViewGroup {

	private final static String TAG = "MyViewGroup";

	private static final int PADDING_HOR = 0;//水平方向padding
	private static final int PADDING_VERTICAL = 5;//垂直方向padding
	private static final int SIDE_MARGIN = 5;//左右间距
	private static final int TEXT_MARGIN = 5;


	public MyViewGroup(Context context) {
		super(context);
	}

	public MyViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyViewGroup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int x = 0;//横坐标
		int y = 0;//纵坐标
		int rows = 1;//总行数
		int specWidth = MeasureSpec.getSize(widthMeasureSpec);
		int actualWidth = specWidth - SIDE_MARGIN * 2;//实际宽度
		int childCount = getChildCount();
		for(int index = 0;index<childCount;index++){
			View child = getChildAt(index);
			child.setPadding(PADDING_HOR, PADDING_VERTICAL, PADDING_HOR, PADDING_VERTICAL);
			child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
			int width = child.getMeasuredWidth();
			int height = child.getMeasuredHeight();
			x += width+TEXT_MARGIN;
			if(x>actualWidth){//换行
				x = width+TEXT_MARGIN;
				rows++;
			}
			y = rows*(height+TEXT_MARGIN);
		}
		setMeasuredDimension(actualWidth, y);
	}


	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {

		int childCount = getChildCount();
		int autualWidth = arg3 - arg1;
		int x = 0;// 横坐标开始
		int y = 0;//纵坐标开始
		int rows = 1;
		for(int i=0;i<childCount;i++){
			View view = getChildAt(i);
			int width = view.getMeasuredWidth();
			int height = view.getMeasuredHeight();
			x += width+TEXT_MARGIN;
			if(x>autualWidth){//换行
				x = width+TEXT_MARGIN;
				rows++;
			}
			y = rows*(height+TEXT_MARGIN);
//			if(i==0){
//				view.layout(x-width-TEXT_MARGIN, y-height, x, y);
//			}else{
				view.layout(x-width, y-height, x, y);
//			}
		}
	}

}
