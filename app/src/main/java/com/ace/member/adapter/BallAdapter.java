package com.ace.member.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ace.member.R;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.text.DecimalFormat;
import java.util.List;

public class BallAdapter extends BaseAdapter {
	private Context context;
	private int endNum;
	private List<Integer> selectedNum;
	private int selectedBgID;//选中的背景图片的资源id
	//这些都是要与xml那边的配置来更改的
	private static int ROW_NUMBER = 10;
	private static int HORIZONTAL_SPACING = 10;
	private static int MARGIN = 5;
	private int mWidth = 0;


	public BallAdapter(Context context, int endNum, List<Integer> selectedNum, int selectedBgID) {
		super();
		this.context = context;
		this.endNum = endNum;
		this.selectedNum = selectedNum;
		this.selectedBgID = selectedBgID;
		mWidth = Utils.getScreenSize().widthPixels;
	}

	@Override
	public int getCount() {
		return endNum;
	}

	@Override
	public Object getItem(int i) {
		return i;
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		try {
			ViewHolder viewHolder;
			if (view == null) {
				view = LayoutInflater.from(context).inflate(R.layout.view_ball, null);
				TextView ball = (TextView) view.findViewById(R.id.tv_content);
				viewHolder = new ViewHolder();
				viewHolder.tvContent = ball;
				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}

			DecimalFormat decimalFormat = new DecimalFormat("00");
			viewHolder.tvContent.setText(decimalFormat.format(i + 1));
			viewHolder.tvContent.setTextColor(ContextCompat.getColor(context, R.color.clr_lotto_btn_text));
			viewHolder.tvContent.setGravity(Gravity.CENTER);
			if (selectedNum.contains(i + 1)) {
				viewHolder.tvContent.setBackgroundResource(selectedBgID);
				viewHolder.tvContent.setTextColor(ContextCompat.getColor(context, R.color.white));
			} else {
				viewHolder.tvContent.setBackgroundResource(R.drawable.style_circle_white);
			}
			AbsListView.LayoutParams params = new AbsListView.LayoutParams((mWidth - 10 - ROW_NUMBER * HORIZONTAL_SPACING) / ROW_NUMBER, (mWidth - 10 - ROW_NUMBER * HORIZONTAL_SPACING) / ROW_NUMBER);
			view.setLayoutParams(params);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return view;

	}

	public class ViewHolder {
		TextView tvContent;
	}

}
