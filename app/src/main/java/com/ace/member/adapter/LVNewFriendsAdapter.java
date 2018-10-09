package com.ace.member.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.bean.NewFriendsInfo;
import com.ace.member.utils.GlideUtil;
import com.ace.member.view.RoundRectImageView;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class LVNewFriendsAdapter extends BaseAdapter {
	private static final int VIEW_TYPE_0_TITLE = 0;
	private static final int VIEW_TYPE_1_DEFAULT = 1;
	private static final int COUNT_2_VIEW_TYPE = 2;

	private static final int STATUS_1_REQUEST_PENDING = 1;
	private static final int STATUS_2_REQUEST_ACCEPTED = 2;
	private static final int STATUS_4_REQUEST_REJECTED = 4;

	private static final int TYPE_NEW_FRIENDS_1_TITLE = 1;
	private final Context mContext;
	private List<NewFriendsInfo> mData;
	private LayoutInflater mInflater;
	private boolean isFirstWhite;

	public interface OnImageClickListener {
		void onImageClick(int position, AppCompatImageView image);
	}

	private OnImageClickListener mOnImageClickListener;

	public void setImageClickListener(OnImageClickListener onImageClickListener) {
		mOnImageClickListener = onImageClickListener;
	}

	private View.OnClickListener mImageListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mOnImageClickListener != null) {
				int position = (Integer) v.getTag();
				AppCompatImageView image = (AppCompatImageView) v;
				mOnImageClickListener.onImageClick(position, image);
			}
		}
	};

	public interface OnButtonClickListener {
		void onButtonClick(int position, Button button);
	}

	private OnButtonClickListener mOnButtonClickListener;

	public void setButtonClickListener(OnButtonClickListener buttonClickListener) {
		mOnButtonClickListener = buttonClickListener;
	}

	private View.OnClickListener mButtonListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mOnButtonClickListener != null) {
				int position = (Integer) v.getTag();
				Button button = (Button) v;
				mOnButtonClickListener.onButtonClick(position, button);
			}
		}
	};

	public LVNewFriendsAdapter(List<NewFriendsInfo> data, Context context) {
		this.mContext = context;
		mData = data;
		mInflater = LayoutInflater.from(context);
	}

	public LVNewFriendsAdapter(List<NewFriendsInfo> data, Context context, boolean isFirstWhite) {
		this.mContext = context;
		mData = data;
		mInflater = LayoutInflater.from(context);
		this.isFirstWhite = isFirstWhite;
	}

	@Override
	public int getCount() {
		return mData == null ? 0 : mData.size();
	}

	@Override
	public NewFriendsInfo getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount() {
		return COUNT_2_VIEW_TYPE;
	}

	@Override
	public int getItemViewType(int position) {
		if (getItem(position).getType() == TYPE_NEW_FRIENDS_1_TITLE) {
			return VIEW_TYPE_0_TITLE;
		} else {
			return VIEW_TYPE_1_DEFAULT;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		NewFriendsInfo newFriendsInfo = mData.get(position);
		if (getItemViewType(position) == VIEW_TYPE_0_TITLE) {
			View titleView = null;
			try {
				titleView = mInflater.inflate(R.layout.view_new_friends_item_title, null);
				RelativeLayout rlTitle = (RelativeLayout) titleView.findViewById(R.id.rl_new_friends_title);
				TextView tvTitle = (TextView) titleView.findViewById(R.id.tv_new_friends_title);
				tvTitle.setText(newFriendsInfo.getContent());
				if (position == 0 && isFirstWhite)
					rlTitle.setBackgroundColor(Utils.getColor(R.color.white));
			} catch (Exception e) {
				FileUtils.addErrorLog(e);
			}
			return titleView;
		} else {
			try {
				ViewHolder viewHolder;
				if (convertView == null) {
					convertView = mInflater.inflate(R.layout.view_friend_request_item, null);
					viewHolder = new ViewHolder();
					viewHolder.mIvIcon = (RoundRectImageView) convertView.findViewById(R.id.iv_friend_request_head);
					viewHolder.mTvName = (TextView) convertView.findViewById(R.id.tv_friend_request_name);
					viewHolder.mTvContent = (TextView) convertView.findViewById(R.id.tv_friend_request_content);
					viewHolder.mBtnStatus = (Button) convertView.findViewById(R.id.btn_friend_request_status);
					convertView.setTag(viewHolder);
				} else {
					viewHolder = (ViewHolder) convertView.getTag();
				}
				int status = newFriendsInfo.getStatus();
				viewHolder.mTvName.setText(newFriendsInfo.getName());
				viewHolder.mTvContent.setText(newFriendsInfo.getPhone());
				GlideUtil.loadThumbnailPortrait(mContext, newFriendsInfo.getPortrait(), viewHolder.mIvIcon);
				switch (status) {
					case STATUS_1_REQUEST_PENDING:
						viewHolder.mBtnStatus.setEnabled(true);
						viewHolder.mBtnStatus.setText(R.string.accept);
						viewHolder.mBtnStatus.setTextColor(Utils.getColor(R.color.white));
						break;
					case STATUS_2_REQUEST_ACCEPTED:
						viewHolder.mBtnStatus.setEnabled(false);
						viewHolder.mBtnStatus.setText(R.string.added);
						viewHolder.mBtnStatus.setTextColor(Utils.getColor(R.color.clr_chat_tv_hint));
						break;
					case STATUS_4_REQUEST_REJECTED:
						viewHolder.mBtnStatus.setEnabled(false);
						viewHolder.mBtnStatus.setText(R.string.rejected);
						viewHolder.mBtnStatus.setTextColor(Utils.getColor(R.color.clr_chat_tv_hint));
						break;
					default:
						viewHolder.mBtnStatus.setEnabled(true);
						viewHolder.mBtnStatus.setText(R.string.add);
						viewHolder.mBtnStatus.setTextColor(Utils.getColor(R.color.white));
				}
				//			viewHolder.mIvIcon.setTag(position);
				//			viewHolder.mIvIcon.setOnClickListener(mImageListener);
				viewHolder.mBtnStatus.setTag(position);
				viewHolder.mBtnStatus.setTag(R.string.status, status);
				viewHolder.mBtnStatus.setOnClickListener(mButtonListener);
			} catch (Exception e) {
				FileUtils.addErrorLog(e);
			}
			return convertView;
		}
	}

	public List<NewFriendsInfo> getData() {
		return mData;
	}

	public void addData(List<NewFriendsInfo> list) {
		if (list != null) {
			if (mData == null) {
				mData = new ArrayList<NewFriendsInfo>();
			}
			mData.addAll(list);
			this.notifyDataSetChanged();
		}
	}

	class ViewHolder {
		TextView mTvName, mTvContent;
		RoundRectImageView mIvIcon;
		Button mBtnStatus;
	}
}
