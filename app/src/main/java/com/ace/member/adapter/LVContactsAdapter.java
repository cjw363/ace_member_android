package com.ace.member.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.bean.ContactInfo;
import com.ace.member.utils.GlideUtil;
import com.ace.member.view.RoundRectImageView;
import com.og.utils.FileUtils;

import java.util.List;

public class LVContactsAdapter extends BaseAdapter {
	private final Context mContext;
	private List<ContactInfo> mContactInfoList;
	private LayoutInflater mInflater;

	public LVContactsAdapter(List<ContactInfo> contactInfoList, Context mContext) {
		this.mContext=mContext;
		mContactInfoList = contactInfoList;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mContactInfoList.size();
	}

	@Override
	public Object getItem(int position) {
		return mContactInfoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public interface OnItemClickListener {
		void onItemClick(int position, View view);
	}

	private OnItemClickListener mOnItemClickListener;

	public void setItemClickListener(OnItemClickListener onItemClickListener) {
		mOnItemClickListener = onItemClickListener;
	}

	private View.OnClickListener mItemListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mOnItemClickListener != null) {
				int position = (Integer) v.getTag();
				mOnItemClickListener.onItemClick(position, v);
			}
		}
	};

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try {
			ViewHolder viewHolder;
			ContactInfo contact = mContactInfoList.get(position);
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.view_contacts_item, null);
				viewHolder.ivHead = (RoundRectImageView) convertView.findViewById(R.id.iv_contacts_head);
				viewHolder.tvLetter = (TextView) convertView.findViewById(R.id.tv_contacts_title_letter);
				viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_contacts_name);
				viewHolder.llTitle = (LinearLayout) convertView.findViewById(R.id.ll_contacts_title);
				viewHolder.rlItem = (RelativeLayout) convertView.findViewById(R.id.rl_contacts_item);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.llTitle.setVisibility(contact.isLetterFlag() ? View.VISIBLE : View.GONE);
			GlideUtil.loadThumbnailPortrait(mContext, contact.getPortrait(), viewHolder.ivHead);
			viewHolder.tvLetter.setText(contact.getFirstLetter());
			viewHolder.tvName.setText(contact.getName());
			viewHolder.rlItem.setTag(position);
			viewHolder.rlItem.setOnClickListener(mItemListener);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return convertView;
	}

	public int getPositionForSelection(int selection) {
		for (int i = 0; i < mContactInfoList.size(); i++) {
			String firstLetter = mContactInfoList.get(i).getFirstLetter();
			char first = firstLetter.toUpperCase().charAt(0);
			if (first == selection) {
				return i;
			}
		}
		return -1;
	}

	public List<ContactInfo> getData() {
		return mContactInfoList;
	}

	public class ViewHolder {
		LinearLayout llTitle;
		RelativeLayout rlItem;
		RoundRectImageView ivHead;
		TextView tvLetter, tvName;
	}
}
