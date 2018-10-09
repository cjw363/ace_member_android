package com.ace.member.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.listener.IMyViewOnClickListener;
import com.ace.member.main.bottom_dialog.ControllerBean;
import com.og.utils.Utils;

import java.util.List;

public class RVBottomDialogAdapter2 extends RecyclerView.Adapter<RVBottomDialogAdapter2.ViewHolder> {
	private LayoutInflater mInflater;
	private List<ControllerBean> mList;
	private int mTvContentColorRes;
	private int mTvContent2Weight;
	private int mType;
	private IMyViewOnClickListener mClickListener;

	public RVBottomDialogAdapter2(Context context, List<ControllerBean> list, int type, int tvContentColorRes, int tvContent2Weight) {
		mInflater = LayoutInflater.from(context);
		mList = list;
		mType = type;
		mTvContent2Weight = tvContent2Weight;
		mTvContentColorRes = tvContentColorRes;
	}

	public void setClickListener(IMyViewOnClickListener clickListener) {
		mClickListener = clickListener;
	}

	@Override
	public RVBottomDialogAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = mInflater.inflate(R.layout.view_item_dlg_bottom_common_2, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(RVBottomDialogAdapter2.ViewHolder holder, int position) {
		if (position == getItemCount() - 1) {
			holder.enableView(false);
			return;
		}
		holder.enableView(true);
		ControllerBean bean = mList.get(position);
		int iconID = bean.getIconID();
		if (iconID > 0) {
			holder.fl.setVisibility(View.VISIBLE);
			holder.iv.setImageResource(iconID);
		} else {
			holder.fl.setVisibility(View.GONE);
		}

		if (mTvContentColorRes != 0) holder.tv1.setTextColor(Utils.getColor(mTvContentColorRes));
		if (mTvContentColorRes != 0) holder.tv2.setTextColor(Utils.getColor(mTvContentColorRes));
		String content1 = bean.getContent1();
		if (content1 != null && !TextUtils.isEmpty(content1)) {
			holder.tv1.setText(content1);
		} else {
			holder.tv1.setVisibility(View.GONE);
		}

		if (mTvContent2Weight > 0) {
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, mTvContent2Weight);
			holder.tv2.setLayoutParams(lp);
		}
		String content2 = bean.getContent2();
		holder.tv2.setText(content2);
		if (mType == 2 && bean.getChoose()) {
			holder.fl2.setVisibility(View.VISIBLE);
		} else {
			holder.fl2.setVisibility(View.GONE);
		}
	}

	@Override
	public int getItemCount() {
		return mList.size()+1;
	}


	class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		FrameLayout fl;
		AppCompatImageView iv;
		TextView tv1;
		TextView tv2;
		View vDivider;
		FrameLayout fl2;

		public ViewHolder(View view) {
			super(view);
			fl = (FrameLayout) view.findViewById(R.id.fl);
			fl2 = (FrameLayout) view.findViewById(R.id.fl_2);
			iv = (AppCompatImageView) view.findViewById(R.id.iv);
			tv1 = (TextView) view.findViewById(R.id.tv1);
			tv2 = (TextView) view.findViewById(R.id.tv2);
			vDivider = view.findViewById(R.id.v_divider);
			view.setClickable(true);
			view.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			if (mClickListener != null) mClickListener.onClick(v, getLayoutPosition());
		}

		private void enableView(boolean enable) {
			if (enable) {
				fl.setVisibility(View.VISIBLE);
				iv.setVisibility(View.VISIBLE);
				tv1.setVisibility(View.VISIBLE);
				tv2.setVisibility(View.VISIBLE);
				fl2.setVisibility(View.VISIBLE);
				vDivider.setVisibility(View.VISIBLE);
			} else {
				fl.setVisibility(View.GONE);
				iv.setVisibility(View.GONE);
				tv1.setVisibility(View.GONE);
				tv2.setVisibility(View.GONE);
				fl2.setVisibility(View.GONE);
				vDivider.setVisibility(View.GONE);
			}
		}
	}

}
