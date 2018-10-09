package com.ace.member.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.bean.FaceValue;
import com.ace.member.listener.IMyViewOnClickListener;
import com.og.utils.Utils;


public class RVBottomDialogAdapter extends RecyclerView.Adapter<RVBottomDialogAdapter.ViewHolder> {
	private LayoutInflater mInflater;
	private int[] mIvRes;
	private int[] mTvContent1;
	private CharSequence[] mTvContent2;
	private int mTvContentColorRes;
	private int mTvContent2Weight;
	private IMyViewOnClickListener mClickListener;

	public RVBottomDialogAdapter(Context context, int[] ivRes, int[] tvContent1, CharSequence[] tvContent2, int tvContentColorRes, int tvContent2Weight) {
		mInflater = LayoutInflater.from(context);
		mIvRes = ivRes;
		mTvContent1 = tvContent1;
		mTvContent2 = tvContent2;
		mTvContent2Weight = tvContent2Weight;
		mTvContentColorRes = tvContentColorRes;
	}

	public void setClickListener(IMyViewOnClickListener clickListener) {
		mClickListener = clickListener;
	}

	@Override
	public RVBottomDialogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = mInflater.inflate(R.layout.view_item_dlg_bottom_common,parent,false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(RVBottomDialogAdapter.ViewHolder holder, int position) {
		if (position == getItemCount() - 1) {
			holder.enableView(false);
			return;
		}
		holder.enableView(true);

		if (mIvRes != null && mIvRes.length > position) {
			holder.fl.setVisibility(View.VISIBLE);
			holder.iv.setImageResource(mIvRes[position]);
		} else {
			holder.fl.setVisibility(View.GONE);
		}

		if (mTvContentColorRes != 0) holder.tv1.setTextColor(Utils.getColor(mTvContentColorRes));
		if (mTvContentColorRes != 0) holder.tv2.setTextColor(Utils.getColor(mTvContentColorRes));

		if (mTvContent1 != null && mTvContent1.length > position) {
			holder.tv1.setText(mTvContent1[position]);
		} else {
			holder.tv1.setVisibility(View.GONE);
		}

		if (mTvContent2Weight > 0) {
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, mTvContent2Weight);
			holder.tv2.setLayoutParams(lp);
		}
		holder.tv2.setText(mTvContent2[position]);
	}

	@Override
	public int getItemCount() {
		return (mTvContent2 == null ? 0 : Math.max(mTvContent2.length, mIvRes == null ? 0 : mIvRes.length)) + 1;
	}

	class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		FrameLayout fl;
		AppCompatImageView iv;
		TextView tv1;
		TextView tv2;
		View vDivider;

		public ViewHolder(View view) {
			super(view);
			fl = (FrameLayout) view.findViewById(R.id.fl);
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
				vDivider.setVisibility(View.VISIBLE);
			} else {
				fl.setVisibility(View.GONE);
				iv.setVisibility(View.GONE);
				tv1.setVisibility(View.GONE);
				tv2.setVisibility(View.GONE);
				vDivider.setVisibility(View.GONE);
			}
		}
	}
}
