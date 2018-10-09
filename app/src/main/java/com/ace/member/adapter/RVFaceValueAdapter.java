package com.ace.member.adapter;

import android.content.Context;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.bean.FaceValue;
import com.ace.member.bean.PhoneCompany;
import com.ace.member.listener.IMyViewOnClickListener;
import com.ace.member.utils.AppGlobal;
import com.og.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class RVFaceValueAdapter extends RecyclerView.Adapter<RVFaceValueAdapter.ViewHolder> {

	private LayoutInflater mInflater;
	private List<FaceValue> mFaceValueList;
	private SparseArrayCompat<ViewHolder> mViewHolders;
	private IMyViewOnClickListener mClickListener;
	private int mMemberDiscount;
	private String mCurrency;
	private int mCheckPosition = -1;

	public RVFaceValueAdapter(Context context, PhoneCompany phoneCompany, List<FaceValue> list) {
		mInflater = LayoutInflater.from(context);
		if (phoneCompany == null) phoneCompany = new PhoneCompany();
		if (list == null) list = new ArrayList<>();
		mFaceValueList = list;
		mCurrency = phoneCompany.getCurrency();
		mMemberDiscount = phoneCompany.getMemberDiscount();
		mViewHolders = new SparseArrayCompat<>(2);
	}

	public void setFaceValueList(PhoneCompany phoneCompany, List<FaceValue> list) {
		if (phoneCompany == null) phoneCompany = new PhoneCompany();
		if (list == null) list = new ArrayList<>();
		mFaceValueList = list;
		mMemberDiscount = phoneCompany.getMemberDiscount();
		mCurrency = phoneCompany.getCurrency();
		check(-1);
		mViewHolders.clear();
		notifyDataSetChanged();
	}

	public void setClickListener(IMyViewOnClickListener clickListener) {
		mClickListener = clickListener;
	}

	public FaceValue getCurrentFaceValue() {
		return mCheckPosition >= 0 ? mFaceValueList.get(mCheckPosition) : null;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = mInflater.inflate(R.layout.view_face_value, null);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		if (mViewHolders.get(position) == null) mViewHolders.put(position, holder);
		holder.tvFaceValue.setText(Utils.format(mFaceValueList.get(position)
			.getFaceValue(), 0));
		if (mMemberDiscount <= 0) {
			holder.tvPrice.setVisibility(View.GONE);
		} else {
			holder.tvPrice.setVisibility(View.VISIBLE);
			holder.tvPrice.setText(String.format(Utils.getString(R.string.price_with_amount), Utils.format(mFaceValueList.get(position)
				.getFaceValue() * (1 - mMemberDiscount * 0.01),mCurrency)));

		}
		setEnable(holder, mFaceValueList.get(position)
			.getStatus() == AppGlobal.STATUS_1_PENDING && mFaceValueList.get(position)
			.getCount() > 0);
	}

	@Override
	public int getItemCount() {
		return mFaceValueList.size();
	}

	class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		View mView;
		TextView tvFaceValue;
		TextView tvPrice;

		public ViewHolder(View itemView) {
			super(itemView);
			mView = itemView;
			tvFaceValue = (TextView) itemView.findViewById(R.id.tv_face_value);
			tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
			mView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			check(getLayoutPosition());
			if (mClickListener != null) mClickListener.onClick(v, getLayoutPosition());
		}
	}

	public void check(int position) {
		mCheckPosition = position;
		ViewHolder holder;
		for (int i = 0, n = mViewHolders.size(); i < n; i++) {
			holder = mViewHolders.get(i);
			setSelected(holder, i == position);
		}
	}

	private void setEnable(ViewHolder holder, boolean enable) {
		holder.itemView.setEnabled(enable);
		holder.tvFaceValue.setEnabled(enable);
		holder.tvPrice.setEnabled(enable);
	}

	private void setSelected(ViewHolder holder, boolean selected) {
		holder.itemView.setSelected(selected);
		holder.tvFaceValue.setSelected(selected);
		holder.tvPrice.setSelected(selected);
	}

	public boolean checkIsValid() {
		if (mFaceValueList == null) return false;
		for (FaceValue faceValue : mFaceValueList) {
			if (faceValue != null && faceValue.getStatus() == AppGlobal.STATUS_1_PENDING && faceValue.getCount() > 0)
				return true;
		}
		return false;
	}
}
