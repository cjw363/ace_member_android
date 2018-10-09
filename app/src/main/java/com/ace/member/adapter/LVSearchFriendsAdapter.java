package com.ace.member.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.bean.SearchFriendsInfo;
import com.ace.member.utils.GlideUtil;
import com.ace.member.utils.Session;
import com.ace.member.view.RoundRectImageView;
import com.og.utils.FileUtils;

import java.util.List;

public class LVSearchFriendsAdapter extends BaseAdapter {
	private static final int VIEW_TYPE_0_TITLE = 0;
	private static final int VIEW_TYPE_1_DEFAULT = 1;
	private static final int COUNT_2_VIEW_TYPE = 2;

	private static final int STATUS_1_PENDING = 1;
	private static final int STATUS_2_ACCEPTED = 2;
	private static final int STATUS_4_REJECTED = 4;

	private static final int TYPE_SEARCH_1_TITLE = 1;
	private final Context mContext;

	private List<SearchFriendsInfo> mList;
	private final LayoutInflater mInflater;

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

	public LVSearchFriendsAdapter(Context context, List<SearchFriendsInfo> list) {
		this.mContext=context;
		mInflater = LayoutInflater.from(context);
		this.mList = list;
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public SearchFriendsInfo getItem(int position) {
		return mList.get(position);
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
		if (getItem(position).getType() == TYPE_SEARCH_1_TITLE) {
			return VIEW_TYPE_0_TITLE;
		} else {
			return VIEW_TYPE_1_DEFAULT;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SearchFriendsInfo searchInfo = mList.get(position);
		if (getItemViewType(position) == VIEW_TYPE_0_TITLE) {
			View titleView = null;
			try {
				titleView = mInflater.inflate(R.layout.view_search_friends_item_title, null);
				View dividerView = titleView.findViewById(R.id.v_search_friends_divider);
				TextView tvTitle = (TextView) titleView.findViewById(R.id.tv_search_friends_title);
				tvTitle.setText(searchInfo.getContent());
				if (position == 0) dividerView.setVisibility(View.GONE);
			} catch (Exception e) {
				FileUtils.addErrorLog(e);
			}
			return titleView;
		} else {

			try {
				ViewHolder viewHolder;
				if (convertView == null) {
					viewHolder = new ViewHolder();
					convertView = mInflater.inflate(R.layout.view_search_friends_item, null);
					viewHolder.mIvIcon = (RoundRectImageView) convertView.findViewById(R.id.iv_search_friends_head);
					viewHolder.mTvName = (TextView) convertView.findViewById(R.id.tv_search_friends_name);
					viewHolder.mTvContent = (TextView) convertView.findViewById(R.id.tv_search_friends_content);
					viewHolder.mBtnStatus = (Button) convertView.findViewById(R.id.btn_search_friends_status);
					convertView.setTag(viewHolder);
				} else {
					viewHolder = (ViewHolder) convertView.getTag();
				}
				viewHolder.mTvName.setText(searchInfo.getName());
				viewHolder.mTvContent.setText(searchInfo.getContent());
				GlideUtil.loadThumbnailPortrait(mContext, searchInfo.getPortrait(), viewHolder.mIvIcon);
				//			viewHolder.mIvIcon.setTag(position);
				//			viewHolder.mIvIcon.setOnClickListener(mImageListener);
				viewHolder.mBtnStatus.setTag(position);
				viewHolder.mBtnStatus.setTag(R.string.status, searchInfo.getStatus());
				viewHolder.mBtnStatus.setOnClickListener(mButtonListener);

				setSearchMembers(viewHolder, searchInfo);//如果查找的是会员消息
			} catch (Exception e) {
				e.printStackTrace();
				FileUtils.addErrorLog(e);
			}
			return convertView;
		}
	}

	private void setSearchMembers(ViewHolder viewHolder, SearchFriendsInfo searchInfo) {
		int myId = Session.user.getId();
		int searchId = searchInfo.getId();
		if (searchInfo.isApplication()) {//有申请记录
			int memberId = searchInfo.getMemberId();//发起人id
			int status = searchInfo.getStatus();
			viewHolder.mBtnStatus.setVisibility(View.VISIBLE);
			if (myId == memberId) {//发起人是自己
				switch (status) {
					case STATUS_1_PENDING://对方还没有接受
						viewHolder.mBtnStatus.setEnabled(false);
						viewHolder.mBtnStatus.setText(R.string.pending);
						break;
					case STATUS_2_ACCEPTED:
						viewHolder.mBtnStatus.setEnabled(false);
						viewHolder.mBtnStatus.setText(R.string.added);
						break;
					case STATUS_4_REJECTED:
						viewHolder.mBtnStatus.setEnabled(false);
						viewHolder.mBtnStatus.setText(R.string.be_rejected);
						break;
				}
			} else {//发起人是对方
				switch (status) {
					case STATUS_1_PENDING:
						viewHolder.mBtnStatus.setEnabled(true);
						viewHolder.mBtnStatus.setText(R.string.accept);
						break;
					case STATUS_2_ACCEPTED:
						viewHolder.mBtnStatus.setEnabled(false);
						viewHolder.mBtnStatus.setText(R.string.added);
						break;
					case STATUS_4_REJECTED:
						viewHolder.mBtnStatus.setEnabled(false);
						viewHolder.mBtnStatus.setText(R.string.rejected);
						break;
				}
			}

		} else {//没有申请记录
			viewHolder.mBtnStatus.setVisibility(View.VISIBLE);
			viewHolder.mBtnStatus.setEnabled(true);
			viewHolder.mBtnStatus.setText(R.string.add);
			if (searchId == myId) {//查找的人是自己
				viewHolder.mBtnStatus.setVisibility(View.GONE);
			}
		}
	}

	public List<SearchFriendsInfo> getData() {
		return mList;
	}

	class ViewHolder {
		TextView mTvName, mTvContent;
		RoundRectImageView mIvIcon;
		Button mBtnStatus;
	}
}
