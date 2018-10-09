package com.ace.member.adapter;


import android.content.Context;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.bean.PhoneCompany;
import com.ace.member.listener.IMyViewOnClickListener;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.PhoneCompanyUtil;

import java.util.ArrayList;
import java.util.List;

public class RVPhoneCompanyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	private LayoutInflater mInflater;
	private SparseArray<String> mTitle;
	private SparseArrayCompat<PhoneCompany> mCompany;
	private IMyViewOnClickListener mClickListener;

	public RVPhoneCompanyAdapter(Context contexts, List<PhoneCompany> list) {
		mInflater = LayoutInflater.from(contexts);
		setTitleAndContent(list);
	}

	public void setPhoneCompany(List<PhoneCompany> list) {
		if (list == null) list = new ArrayList<>();
		setTitleAndContent(list);
	}

	public void setClickListener(IMyViewOnClickListener clickListener) {
		mClickListener = clickListener;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		int res = viewType!=0 ? R.layout.view_country_name : R.layout.view_gv_phone_company;
		View view = mInflater.inflate(res, null);
		if (viewType != 0) return new TitleViewHolder(view);
		return new ContentViewHolder(view);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if (isTitle(position)) {
			((TitleViewHolder) holder).tvCompanyName.setText(mTitle.get(position));
		} else {
			ContentViewHolder viewHolder = (ContentViewHolder) holder;
			viewHolder.ivPhoneCompany.setImageResource(PhoneCompanyUtil.getPhoneCompanyResourceByName(mCompany.get(position).getName()));
			viewHolder.tvPhoneCompanyName.setText(mCompany.get(position).getName());
		}
	}

	@Override
	public int getItemCount() {
		return mTitle.size() + mCompany.size();
	}

	public PhoneCompany getPhoneCompany(int position) {
		return mCompany.get(position);
	}

	private boolean isTitle(int position) {
		return mTitle.get(position) != null;
	}

	private void setTitleAndContent(List<PhoneCompany> list) {
		mTitle = new SparseArray<>();
		mCompany = new SparseArrayCompat<>(list.size());
		int code = 0;
		int pos = 0;
		for (int i = 0, n = list.size(); i < n; i++) {
			if (list.get(i).getCountryCode() != code) {
				code = list.get(i).getCountryCode();
				mTitle.put(pos++, AppUtils.getCountryNameByCode(code));
				mCompany.put(pos++, list.get(i));
			} else {
				mCompany.put(pos++, list.get(i));
			}
		}
	}

	@Override
	public int getItemViewType(int position) {
		if (isTitle(position)) return position + 10000;
		return super.getItemViewType(position);
	}

	private class TitleViewHolder extends RecyclerView.ViewHolder {
		TextView tvCompanyName;

		TitleViewHolder(View itemView) {
			super(itemView);
			tvCompanyName = (TextView) itemView.findViewById(R.id.tv_company_name);
		}
	}

	private class ContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		AppCompatImageView ivPhoneCompany;
		TextView tvPhoneCompanyName;

		ContentViewHolder(View itemView) {
			super(itemView);
			ivPhoneCompany = (AppCompatImageView) itemView.findViewById(R.id.iv_phone_company);
			tvPhoneCompanyName = (TextView) itemView.findViewById(R.id.tv_phone_company_name);
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			if (mClickListener != null) mClickListener.onClick(v, getLayoutPosition());
		}
	}

	@Override
	public void onAttachedToRecyclerView(RecyclerView recyclerView) {
		GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
		manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
			@Override
			public int getSpanSize(int position) {
				return isTitle(position) ? 3 : 1;
			}
		});
	}


}
