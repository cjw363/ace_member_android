package com.ace.member.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.bean.PartnerBean;
import com.ace.member.listener.IMyViewOnClickListener;

import java.util.List;

public class PartnerCompanyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private LayoutInflater mInflater;
	private List<PartnerBean> mList;
	private IMyViewOnClickListener mClickListener;

	public PartnerCompanyAdapter(Context context, List<PartnerBean> list) {
		mInflater = LayoutInflater.from(context);
		mList = list;
	}

	public void setPartnerCompany(List<PartnerBean> list) {
		mList = list;
	}

	public void setClickListener(IMyViewOnClickListener clickListener) {
		mClickListener = clickListener;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		int res = R.layout.view_gv_phone_company;
		View view = mInflater.inflate(res, null);
//		if (viewType != 0) return new RVPhoneCompanyAdapter.TitleViewHolder(view);
		return new PartnerCompanyAdapter.ContentViewHolder(view);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		PartnerCompanyAdapter.ContentViewHolder viewHolder = (PartnerCompanyAdapter.ContentViewHolder) holder;
		PartnerBean item = mList.get(position);
		viewHolder.ivPartnerCompany.setImageResource(R.drawable.ic_top_up_ais);
		String name = item.getName();
		viewHolder.tvPartnerCompanyName.setText(name);

	}

	@Override
	public int getItemCount() {
		return mList.size();
	}

	private class ContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		AppCompatImageView ivPartnerCompany;
		TextView tvPartnerCompanyName;

		ContentViewHolder(View itemView) {
			super(itemView);
			ivPartnerCompany = (AppCompatImageView) itemView.findViewById(R.id.iv_phone_company);
			tvPartnerCompanyName = (TextView) itemView.findViewById(R.id.tv_phone_company_name);
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			if (mClickListener != null) mClickListener.onClick(v, getLayoutPosition());
		}
	}

}
