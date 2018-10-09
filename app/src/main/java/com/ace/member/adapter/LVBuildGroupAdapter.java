package com.ace.member.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.bean.ContactInfo;
import com.ace.member.utils.GlideUtil;
import com.ace.member.view.RoundRectImageView;
import com.og.utils.FileUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LVBuildGroupAdapter extends BaseAdapter {
	private final Context mContext;
	private List<ContactInfo> mContactInfoList;
	private LayoutInflater mInflater;
	private List<Integer> mGroupMember;
	private Map<Integer, ContactInfo> selectedMap;

	public LVBuildGroupAdapter(List<ContactInfo> contactInfoList, Context mContext, List<Integer> groupMember) {
		this.mContext=mContext;
		mContactInfoList = contactInfoList;
		mInflater = LayoutInflater.from(mContext);
		selectedMap = new HashMap<>();

		if (groupMember != null && contactInfoList != null) {//初始化之前已选中的联系人
			mGroupMember = groupMember;
			for (int i = 0; i < contactInfoList.size(); i++) {
				ContactInfo contact = contactInfoList.get(i);
				if (mGroupMember.contains(contact.getMemberId())) {
					selectedMap.put(contact.getMemberId(), contact);
				}
			}
		}
	}

	@Override
	public int getCount() {
		return mContactInfoList == null ? 0 : mContactInfoList.size();
	}

	@Override
	public Object getItem(int position) {
		return mContactInfoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public interface OnCheckBoxClickListener {
		void onCheckBoxClick(int position, CheckBox checkBox);
	}

	private OnCheckBoxClickListener mOnCheckBoxClickListener;

	public void setCheckBoxClickListener(OnCheckBoxClickListener onCheckBoxClickListener) {
		mOnCheckBoxClickListener = onCheckBoxClickListener;
	}

	private View.OnClickListener mCheckBoxListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mOnCheckBoxClickListener != null) {
				CheckBox checkBox = (CheckBox) v.getTag();
				int position = (Integer) checkBox.getTag();
				mOnCheckBoxClickListener.onCheckBoxClick(position, checkBox);
			}
		}
	};

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try {
			final ViewHolder viewHolder;
			ContactInfo contact = mContactInfoList.get(position);
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.view_contacts_build_group_item, null);
				viewHolder.ivHead = (RoundRectImageView) convertView.findViewById(R.id.iv_contacts_head);
				viewHolder.tvLetter = (TextView) convertView.findViewById(R.id.tv_contacts_title_letter);
				viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_contacts_name);
				viewHolder.llTitle = (LinearLayout) convertView.findViewById(R.id.ll_contacts_title);
				viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.cb_contacts);
				viewHolder.rlItem = (RelativeLayout) convertView.findViewById(R.id.rl_contacts_item);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.llTitle.setVisibility(contact.isLetterFlag() ? View.VISIBLE : View.GONE);
			GlideUtil.loadThumbnailPortrait(mContext, contact.getPortrait(), viewHolder.ivHead);
			viewHolder.tvLetter.setText(contact.getFirstLetter());
			viewHolder.tvName.setText(contact.getName());
			viewHolder.checkBox.setTag(position);
			viewHolder.rlItem.setClickable(true);
			viewHolder.rlItem.setOnClickListener(mCheckBoxListener);
			viewHolder.rlItem.setTag(viewHolder.checkBox);

			if (mGroupMember != null && mGroupMember.size() > 0) {//从已有的群组进入是，原来的成员不可再点击取消选中
				if (mGroupMember.contains(contact.getMemberId())) {
					viewHolder.rlItem.setClickable(false);
				}
			}
			viewHolder.checkBox.setChecked(isSelected(contact.getMemberId()));
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

	public void setSelectedMap(Map<Integer, ContactInfo> selectedMap) {
		this.selectedMap = selectedMap;
		notifyDataSetChanged();
	}

	public boolean isSelected(int key) {
		return selectedMap.get(key) != null;
	}

	public void putSelectedMember(int position) {
		selectedMap.put(mContactInfoList.get(position).getMemberId(), mContactInfoList.get(position));//key-id,value-对象
	}

	public void removeSelectedMember(int position) {
		selectedMap.remove(mContactInfoList.get(position).getMemberId());
	}

	public Map<Integer, ContactInfo> getSelectedMap() {
		return selectedMap;
	}

	public List<ContactInfo> getSelectedList() {
		return new ArrayList<>(selectedMap.values());
	}

	public class ViewHolder {
		LinearLayout llTitle;
		RelativeLayout rlItem;
		CheckBox checkBox;
		RoundRectImageView ivHead;
		TextView tvLetter, tvName;
	}

}
