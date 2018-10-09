package com.ace.member.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseViewHolder;
import com.ace.member.bean.ChatMsg;
import com.ace.member.utils.EmotionUtils;
import com.ace.member.utils.GlideUtil;
import com.ace.member.utils.Session;
import com.ace.member.view.BubbleView.BubbleLinearLayout;
import com.ace.member.view.BubbleView.BubbleTextView;
import com.ace.member.view.MoneyTextView;
import com.ace.member.view.RoundRectImageView;
import com.og.utils.DateUtils;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class LVChatAdapter extends BaseAdapter {
	private static final int VIEW_TYPE_0_DEFAULT = 0;
	private static final int VIEW_TYPE_1_SYSTEM_MSG = 1;
	private static final int VIEW_TYPE_2_TRANSFER = 2;
	private static final int VIEW_TYPE_3_VOICE = 3;

	private static final int COUNT_4_VIEW_TYPE = 4;

	private static final int MESSAGE_TYPE_1_TEXT = 1;
	private static final int MESSAGE_TYPE_3_TRANSFER = 3;
	private static final int MESSAGE_TYPE_4_VOICE = 4;
	private static final int MESSAGE_TYPE_99_SYSTEM_MSG = 99;

	private Context mContext;
	private List<ChatMsg> mList;

	public interface OnImageClickListener {
		void onImageClick(int position, ImageView imageView);
	}

	public interface OnVoiceClickListener {
		void onVoiceClick(int position, TextView textView);
	}

	public interface OnTransferClickListener {
		void onTransferClick(int position, LinearLayout linearLayout);
	}

	private OnImageClickListener mOnImageClickListener;
	private OnVoiceClickListener mOnVoiceClickListener;
	private OnTransferClickListener mOnTransferClickListener;

	public void setOnImageClickListener(OnImageClickListener mListener) {
		this.mOnImageClickListener = mListener;
	}

	public void setOnVoiceClickListener(OnVoiceClickListener mListener) {
		this.mOnVoiceClickListener = mListener;
	}

	public void setOnTransferClickListener(OnTransferClickListener mListener) {
		this.mOnTransferClickListener = mListener;
	}

	private View.OnClickListener mImageClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mOnImageClickListener != null) {
				int position = (int) v.getTag(R.id.position);
				mOnImageClickListener.onImageClick(position, (ImageView) v);
			}
		}
	};

	private View.OnClickListener mVoiceClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			if (mOnVoiceClickListener != null) {
				int position = (int) v.getTag();
				mOnVoiceClickListener.onVoiceClick(position, (TextView) v);
			}
		}
	};

	private View.OnClickListener mTransferClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			if (mOnTransferClickListener != null) {
				int position = (int) v.getTag();
				mOnTransferClickListener.onTransferClick(position, (LinearLayout) v);
			}
		}
	};

	public LVChatAdapter(Context context, List<ChatMsg> list) {
		mContext = context;
		mList = list;
	}

	@Override
	public int getItemViewType(int position) {
		ChatMsg chatMsg = getItem(position);
		int contentType = chatMsg.getContentType();
		if (contentType == MESSAGE_TYPE_1_TEXT) {
			return VIEW_TYPE_0_DEFAULT;
		} else if (contentType == MESSAGE_TYPE_99_SYSTEM_MSG) {
			return VIEW_TYPE_1_SYSTEM_MSG;
		} else if (contentType == MESSAGE_TYPE_3_TRANSFER) {
			return VIEW_TYPE_2_TRANSFER;
		} else if (contentType == MESSAGE_TYPE_4_VOICE) {
			return VIEW_TYPE_3_VOICE;
		}
		return super.getItemViewType(position);
	}

	@Override
	public int getViewTypeCount() {
		return COUNT_4_VIEW_TYPE;
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public ChatMsg getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try {
			switch (getItemViewType(position)) {
				case VIEW_TYPE_0_DEFAULT:
					return handleChatView(position, convertView, parent);
				case VIEW_TYPE_1_SYSTEM_MSG:
					return handleSysMsgView(position, convertView, parent);
				case VIEW_TYPE_2_TRANSFER:
					return handleTransferView(position, convertView, parent);
				case VIEW_TYPE_3_VOICE:
					return handleVoiceView(position, convertView, parent);
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
		return null;
	}

	private View handleChatView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.view_chat_item, parent, false);
			convertView.setTag(new ChatViewHolder(convertView));
		}
		if (convertView.getTag() instanceof ChatViewHolder) {
			final ChatViewHolder viewHolder = (ChatViewHolder) convertView.getTag();
			ChatMsg chatMsg = mList.get(position);
			String content = chatMsg.getContent();
			//			String contentStr = setString(content);
			int memberId = chatMsg.getMemberId();
			String portrait = chatMsg.getPortrait();

			if (memberId == Session.user.getId()) {
				viewHolder.mTvMyMsg.setText(EmotionUtils.getEmotionContent(mContext, viewHolder.mTvMyMsg, content));
				viewHolder.mTvMyMsg.setVisibility(View.VISIBLE);
				viewHolder.mIvMyIcon.setVisibility(View.VISIBLE);
				viewHolder.mTvFriendMsg.setVisibility(View.GONE);
				viewHolder.mIvFriendIcon.setVisibility(View.GONE);

				GlideUtil.loadThumbnailPortrait(mContext, Session.user.getPortrait(), viewHolder.mIvMyIcon);
				viewHolder.mIvMyIcon.setTag(R.id.position, position);
				viewHolder.mIvMyIcon.setOnClickListener(mImageClickListener);
			} else {
				viewHolder.mTvFriendMsg.setText(EmotionUtils.getEmotionContent(mContext, viewHolder.mTvFriendMsg, content));
				viewHolder.mTvFriendMsg.setVisibility(View.VISIBLE);
				viewHolder.mIvFriendIcon.setVisibility(View.VISIBLE);
				viewHolder.mTvMyMsg.setVisibility(View.GONE);
				viewHolder.mIvMyIcon.setVisibility(View.GONE);

				GlideUtil.loadThumbnailPortrait(mContext, portrait, viewHolder.mIvFriendIcon);
				viewHolder.mIvFriendIcon.setTag(R.id.position, position);
				viewHolder.mIvFriendIcon.setOnClickListener(mImageClickListener);
			}
			setChatTime(viewHolder, position);
		}
		return convertView;
	}

	private View handleSysMsgView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.view_chat_item_sys_msg, parent, false);
			convertView.setTag(new SysMsgViewHolder(convertView));
		}
		if (convertView.getTag() instanceof SysMsgViewHolder) {
			final SysMsgViewHolder viewHolder = (SysMsgViewHolder) convertView.getTag();
			ChatMsg chatMsg = mList.get(position);
			convertView.setVisibility((chatMsg.getMemberId() == Session.user.getId()) ? View.VISIBLE : View.GONE);
			viewHolder.mTvSysMsg.setText(chatMsg.getContent());
		}
		return convertView;
	}

	private View handleTransferView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.view_chat_item_transfer, parent, false);
			convertView.setTag(new TransferViewHolder(convertView));
		}
		if (convertView.getTag() instanceof TransferViewHolder) {
			final TransferViewHolder viewHolder = (TransferViewHolder) convertView.getTag();
			ChatMsg chatMsg = mList.get(position);
			String content = chatMsg.getContent();
			String contentStr = setString(content);
			int memberId = chatMsg.getMemberId();
			String currency = chatMsg.getCurrency();
			double amount = chatMsg.getAmount();
			String portrait = chatMsg.getPortrait();
			if (memberId == Session.user.getId()) {
				viewHolder.mTvTransferRemark.setText(contentStr);
				viewHolder.mTvTransferAmount.setMoney(currency, amount + "");
				viewHolder.mLlMyMsg.setTag(position);
				viewHolder.mLlMyMsg.setOnClickListener(mTransferClickListener);
				viewHolder.mLlMyMsg.setVisibility(View.VISIBLE);
				viewHolder.mIvMyIcon.setVisibility(View.VISIBLE);
				viewHolder.mLlFriendMsg.setVisibility(View.GONE);
				viewHolder.mIvFriendIcon.setVisibility(View.GONE);

				GlideUtil.loadThumbnailPortrait(mContext, Session.user.getPortrait(), viewHolder.mIvMyIcon);
				viewHolder.mIvMyIcon.setTag(R.id.position, position);
				viewHolder.mIvMyIcon.setOnClickListener(mImageClickListener);
			} else {
				viewHolder.mTvFriendTransferRemark.setText(contentStr);
				viewHolder.mTvFriendTransferAmount.setMoney(currency, amount + "");
				viewHolder.mLlFriendMsg.setTag(position);
				viewHolder.mLlFriendMsg.setOnClickListener(mTransferClickListener);
				viewHolder.mLlFriendMsg.setVisibility(View.VISIBLE);
				viewHolder.mIvFriendIcon.setVisibility(View.VISIBLE);
				viewHolder.mIvMyIcon.setVisibility(View.GONE);
				viewHolder.mLlMyMsg.setVisibility(View.GONE);

				GlideUtil.loadThumbnailPortrait(mContext, portrait, viewHolder.mIvFriendIcon);
				viewHolder.mIvFriendIcon.setTag(R.id.position, position);
				viewHolder.mIvFriendIcon.setOnClickListener(mImageClickListener);
			}
			setChatTime(viewHolder, position);
		}
		return convertView;
	}

	private View handleVoiceView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.view_chat_item_voice, parent, false);
			convertView.setTag(new VoiceViewHolder(convertView));
		}
		if (convertView.getTag() instanceof VoiceViewHolder) {
			final VoiceViewHolder viewHolder = (VoiceViewHolder) convertView.getTag();
			ChatMsg chatMsg = mList.get(position);
			String content = chatMsg.getContent();
			int memberId = chatMsg.getMemberId();
			String portrait = chatMsg.getPortrait();

			if (memberId == Session.user.getId()) {
				long time = Long.parseLong((String) getParamsMap(content).get("time"));
				viewHolder.mTvMyMsg.setText(calculateLength(time));
				viewHolder.mTvMyMsg.setTag(position);
				viewHolder.mTvMyMsg.setOnClickListener(mVoiceClickListener);
				viewHolder.mTvMyVoiceTime.setVisibility(View.VISIBLE);
				viewHolder.mTvMyVoiceTime.setText(calculateTime(time));
				viewHolder.mTvMyMsg.setVisibility(View.VISIBLE);
				viewHolder.mIvMyIcon.setVisibility(View.VISIBLE);
				viewHolder.mTvFriendMsg.setVisibility(View.GONE);
				viewHolder.mIvFriendIcon.setVisibility(View.GONE);

				GlideUtil.loadThumbnailPortrait(mContext, Session.user.getPortrait(), viewHolder.mIvMyIcon);
				viewHolder.mIvMyIcon.setTag(R.id.position, position);
				viewHolder.mIvMyIcon.setOnClickListener(mImageClickListener);
			} else {
				long time = Long.parseLong((String) getParamsMap(content).get("time"));
				viewHolder.mTvFriendMsg.setText(calculateLength(time));
				viewHolder.mTvFriendMsg.setTag(position);
				viewHolder.mTvFriendMsg.setOnClickListener(mVoiceClickListener);
				viewHolder.mTvFriendVoiceTime.setVisibility(View.VISIBLE);
				viewHolder.mTvFriendVoiceTime.setText(calculateTime(time));
				viewHolder.mTvFriendMsg.setVisibility(View.VISIBLE);
				viewHolder.mIvFriendIcon.setVisibility(View.VISIBLE);
				viewHolder.mTvMyMsg.setVisibility(View.GONE);
				viewHolder.mIvMyIcon.setVisibility(View.GONE);

				GlideUtil.loadThumbnailPortrait(mContext, portrait, viewHolder.mIvFriendIcon);
				viewHolder.mIvFriendIcon.setTag(R.id.position, position);
				viewHolder.mIvFriendIcon.setOnClickListener(mImageClickListener);
			}
			setChatTime(viewHolder, position);
		}
		return convertView;
	}

	private void setChatTime(BaseViewHolder holder, int position) {
		if (position >= 1 && mList.get(position).getContentType() != MESSAGE_TYPE_99_SYSTEM_MSG) {
			String time = mList.get(position).getTime();
			String previousTime = mList.get(position - 1).getTime();
			if (DateUtils.secsBetween(previousTime, time) >= 300) {
				holder.mRlTime.setVisibility(View.VISIBLE);
				holder.mTvTime.setText(formatTime(time));
			} else {
				holder.mRlTime.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * 若过于臃肿，可以将holder移出新建类
	 */
	private class ChatViewHolder extends BaseViewHolder {
		BubbleTextView mTvMyMsg, mTvFriendMsg;

		ChatViewHolder(View itemView) {
			super(itemView);
			mTvFriendMsg = (BubbleTextView) itemView.findViewById(R.id.tv_chat_friend);
			mTvMyMsg = (BubbleTextView) itemView.findViewById(R.id.tv_chat_me);
			mIvMyIcon = (RoundRectImageView) itemView.findViewById(R.id.iv_chat_me_item);
			mIvFriendIcon = (RoundRectImageView) itemView.findViewById(R.id.iv_chat_friend_item);
			mRlTime = (RelativeLayout) itemView.findViewById(R.id.rl_chat_time);
			mTvTime = (TextView) itemView.findViewById(R.id.tv_chat_time);
		}
	}

	private class SysMsgViewHolder extends BaseViewHolder {
		TextView mTvSysMsg;

		SysMsgViewHolder(View itemView) {
			super(itemView);
			mTvSysMsg = (TextView) itemView.findViewById(R.id.tv_chat_sys_msg);
		}
	}

	private class TransferViewHolder extends BaseViewHolder {
		BubbleLinearLayout mLlMyMsg, mLlFriendMsg;
		AppCompatImageView mIvTransfer, mIvFriendTransfer;
		TextView mTvTransferRemark, mTvFriendTransferRemark;
		MoneyTextView mTvTransferAmount, mTvFriendTransferAmount;

		TransferViewHolder(View itemView) {
			super(itemView);
			mIvMyIcon = (RoundRectImageView) itemView.findViewById(R.id.iv_chat_me_item);
			mIvFriendIcon = (RoundRectImageView) itemView.findViewById(R.id.iv_chat_friend_item);
			mLlFriendMsg = (BubbleLinearLayout) itemView.findViewById(R.id.ll_chat_friend);
			mLlMyMsg = (BubbleLinearLayout) itemView.findViewById(R.id.ll_chat_me);
			mIvTransfer = (AppCompatImageView) itemView.findViewById(R.id.iv_transfer);
			mIvFriendTransfer = (AppCompatImageView) itemView.findViewById(R.id.iv_friend_transfer);
			mTvTransferRemark = (TextView) itemView.findViewById(R.id.tv_transfer_remark);
			mTvTransferAmount = (MoneyTextView) itemView.findViewById(R.id.tv_transfer_amount);
			mTvFriendTransferRemark = (TextView) itemView.findViewById(R.id.tv_friend_transfer_remark);
			mTvFriendTransferAmount = (MoneyTextView) itemView.findViewById(R.id.tv_friend_transfer_amount);
			mRlTime = (RelativeLayout) itemView.findViewById(R.id.rl_chat_time);
			mTvTime = (TextView) itemView.findViewById(R.id.tv_chat_time);
		}
	}

	private class VoiceViewHolder extends BaseViewHolder {
		BubbleTextView mTvMyMsg, mTvFriendMsg;
		TextView mTvMyVoiceTime, mTvFriendVoiceTime;

		VoiceViewHolder(View itemView) {
			super(itemView);
			mTvFriendMsg = (BubbleTextView) itemView.findViewById(R.id.tv_chat_friend);
			mTvMyMsg = (BubbleTextView) itemView.findViewById(R.id.tv_chat_me);
			mIvMyIcon = (RoundRectImageView) itemView.findViewById(R.id.iv_chat_me_item);
			mIvFriendIcon = (RoundRectImageView) itemView.findViewById(R.id.iv_chat_friend_item);
			mTvMyVoiceTime = (TextView) itemView.findViewById(R.id.tv_chat_me_voice_time);
			mTvFriendVoiceTime = (TextView) itemView.findViewById(R.id.tv_chat_friend_voice_time);
			mRlTime = (RelativeLayout) itemView.findViewById(R.id.rl_chat_time);
			mTvTime = (TextView) itemView.findViewById(R.id.tv_chat_time);
		}
	}

	private String setString(String msg) {
		if (msg.length() <= 23) {
			return msg;
		} else {
			StringBuilder sb = new StringBuilder(msg);
			for (int i = 1; i <= sb.length() / 23; i++) {
				sb.insert(i * 22 + i - 1, "\n");
			}
			return sb.toString();
		}
	}

	public void addFootData(ChatMsg chatMsg) {
		if (!contains(chatMsg)) {
			mList.add(chatMsg);
			notifyDataSetChanged();
		}
	}

	//是否已经包含这条信息
	private boolean contains(ChatMsg chatMsg) {
		if (mList != null) {
			for (int i = mList.size() - 1; i >= 0; i--) {
				ChatMsg listMsg = mList.get(i);
				if (listMsg.getTime().equals(chatMsg.getTime()) && listMsg.getContent().equals(chatMsg.getContent()) && listMsg.getContentType() == chatMsg.getContentType() && listMsg.getMemberId() == chatMsg.getMemberId())//如果消息的时间,内容,类型,发出人都相同，就认为是重复消息
					return true;
			}
		}
		return false;
	}

	public List<ChatMsg> getData() {
		return mList;
	}

	private Map getParamsMap(String msg) {
		String[] params = msg.split("\\?")[1].split("&");
		HashMap<String, String> map = new HashMap<>();
		for (String s : params) {
			String[] split = s.split("=");
			map.put(split[0], split[1]);
		}
		return map;
	}

	private String calculateLength(long time) {
		String blankString = "";
		long l = time / 1000;
		if (l < 3) l = 3;//最小宽度
		else if (l > 20) l = 20;//最大宽度
		for (int i = 1; i <= l; i++) {
			blankString += "\t";//一个空格的宽度
		}
		return blankString;
	}

	private String calculateTime(long time) {
		DecimalFormat df = new DecimalFormat("0.0");
		String format = df.format((double) time / 1000);
		return format + "\"";
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
