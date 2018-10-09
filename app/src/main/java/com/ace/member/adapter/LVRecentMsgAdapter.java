package com.ace.member.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.bean.RecentMsg;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.EmotionUtils;
import com.ace.member.utils.GlideUtil;
import com.ace.member.view.Badge.Badge;
import com.ace.member.view.Badge.QBadgeView;
import com.ace.member.view.RoundRectImageView;
import com.og.utils.DateUtils;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.text.ParseException;
import java.util.List;

public class LVRecentMsgAdapter extends BaseAdapter {
	private static final int RECENT_TYPE_100_CHAT_MSG = 100;
	private static final int RECENT_TYPE_999_OFFICIAL_MSG = 999;//最近消息类型，官方通知消息

	private static final int NOTIFICATION_TYPE_1_TRADE = 1;
	private static final int NOTIFICATION_TYPE_2_REPAYMENT = 2;
	private static final int NOTIFICATION_TYPE_9_OTHERS = 9;

	private List<RecentMsg> mList;
	private Context mContext;

	public LVRecentMsgAdapter(List<RecentMsg> list, Context context) {
		mList = list;
		mContext = context;
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try {
			RecentMsg recentMsg = mList.get(position);
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.view_friends_recent_msg_item, null);
				viewHolder = new ViewHolder();
				viewHolder.mTvName = (TextView) convertView.findViewById(R.id.tv_lv_item_name);
				viewHolder.mIvIcon = (RoundRectImageView) convertView.findViewById(R.id.iv_lv_item_head);
				viewHolder.mTvMsg = (TextView) convertView.findViewById(R.id.tv_lv_item_msg);
				viewHolder.mTvTime = (TextView) convertView.findViewById(R.id.tv_lv_item_time);
				viewHolder.mRlHead = (RelativeLayout) convertView.findViewById(R.id.rl_lv_item_head);
				viewHolder.mIvMute = (AppCompatImageView) convertView.findViewById(R.id.iv_lv_item_mute);
				viewHolder.mBadge = new QBadgeView(mContext).bindTarget(viewHolder.mRlHead);
				viewHolder.mBadge.setBadgeTextSize(10, true);
				viewHolder.mBadge.setBadgeGravity(Gravity.END | Gravity.TOP);
				viewHolder.mBadge.setGravityOffset(1, 1, true);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			int type = recentMsg.getType();
			viewHolder.mTvTime.setText(formatTime(recentMsg.getTime()));
			viewHolder.mTvName.setText(recentMsg.getName());
			viewHolder.mTvMsg.setText(EmotionUtils.getEmotionContent(mContext, viewHolder.mTvMsg, recentMsg.getContent()));
			viewHolder.mBadge.setBadgeNumber(recentMsg.getCount());
			if (type == RECENT_TYPE_100_CHAT_MSG) {
				GlideUtil.loadThumbnailPortrait(mContext, recentMsg.getPortrait(), viewHolder.mIvIcon);
			} else if (type == RECENT_TYPE_999_OFFICIAL_MSG) {
				int notificationType = recentMsg.getContentType();
				if (notificationType == NOTIFICATION_TYPE_9_OTHERS) {
					viewHolder.mTvName.setText(R.string.system_message);
					viewHolder.mIvIcon.setImageResource(R.drawable.ic_system_message);
				} else if (notificationType == NOTIFICATION_TYPE_1_TRADE) {
					viewHolder.mTvName.setText(R.string.trading_message);
					viewHolder.mIvIcon.setImageResource(R.drawable.ic_friends_official_account);
				} else if (notificationType == NOTIFICATION_TYPE_2_REPAYMENT) {
					viewHolder.mTvName.setText(R.string.payment_message);
					viewHolder.mIvIcon.setImageResource(R.drawable.ic_payment_message);
				}
			}
			if (recentMsg.getFlagMuteNotifications() == AppGlobal.FLAG_MUTE_NOTIFICATIONS_1_YES) {
				viewHolder.mIvMute.setVisibility(View.VISIBLE);
			} else {
				viewHolder.mIvMute.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return convertView;
	}

	class ViewHolder {
		Badge mBadge;
		TextView mTvName;
		TextView mTvMsg;
		TextView mTvTime;
		RoundRectImageView mIvIcon;
		RelativeLayout mRlHead;
		AppCompatImageView mIvMute;
	}

	public List<RecentMsg> getData() {
		return mList;
	}

	private String formatTime(String date) {
		if (TextUtils.isEmpty(date)) return "";
		try {
			String today = DateUtils.getToday();
			int daysCount = Math.abs(DateUtils.daysBetween(today, date));
			if (daysCount < 7) {
				switch (daysCount) {
					case 0://今天
						date = date.substring(11, 16);
						break;
					case 1://昨天
						date = Utils.getString(R.string.Yesterday);
						break;
					default:
						date = DateUtils.getWeekOfDate(date);
						break;
				}
			} else {
				date = date.substring(0, 10);
			}
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
}
