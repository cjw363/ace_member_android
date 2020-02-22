package com.ace.member.main.friends.chat;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ace.member.R;
import com.ace.member.adapter.CommonFragmentPagerAdapter;
import com.ace.member.adapter.LVChatAdapter;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.ChatMsg;
import com.ace.member.bean.SocketMessage;
import com.ace.member.event.FinishEvent;
import com.ace.member.main.friends.chat.chat_info.ChatInfoActivity;
import com.ace.member.main.friends.chat.friend_profile.FriendProfileActivity;
import com.ace.member.main.friends.chat_group.chat_group_info.ChatGroupInfoActivity;
import com.ace.member.main.friends.db.dao.ChatDao;
import com.ace.member.main.friends.fragment.EmotionFragment;
import com.ace.member.main.friends.fragment.FunctionFragment;
import com.ace.member.main.home.transfer.recent_detail.TransferRecentDetailActivity;
import com.ace.member.main.home.transfer.to_member.ToMemberActivity;
import com.ace.member.service.IMWebSocketService;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.GlobalOnItemClickManagerUtils;
import com.ace.member.view.ChatListView;
import com.ace.member.view.NoScrollViewPager;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class ChatActivity extends BaseActivity implements ChatContract.ChatView {
	private static final int REQUEST_CODE_66_VOICE = 66;

	private static final int MSG_TYPE_1_TEXT = 1;
	private static final int MSG_TYPE_3_TRANSFER = 3;
	private static final int MSG_TYPE_4_VOICE = 4;

	private static final int CHAT_TYPE_1_FRIEND = 1;//1 Friend 2 Group
	private static final int CHAT_TYPE_2_GROUP = 2;

	@Inject
	ChatPresenter mChatPresenter;

	@BindView(R.id.lv_friends_chat)
	ChatListView mListView;
	@BindView(R.id.et_chat)
	EditText mEditText;
	@BindView(R.id.btn_chat_send)
	Button mButton;
	@BindView(R.id.iv_chat_add)
	ImageView mIvAdd;
	@BindView(R.id.bar_chat)
	LinearLayout mBarLayout;
	@BindView(R.id.emotion_layout_chat)
	FrameLayout mEmotionLayout;
	@BindView(R.id.iv_chat_emoticon)
	ImageView mEmotionImage;
	@BindView(R.id.vp_chat)
	NoScrollViewPager mViewPager;
	@BindView(R.id.tv_title)
	TextView mTvTitle;
	@BindView(R.id.iv_chat_switching)
	AppCompatImageView mIvChatSwitching;
	@BindView(R.id.iv_chat_keyboard)
	AppCompatImageView mIvChatKeyboard;
	@BindView(R.id.tv_chat_voice)
	TextView mTvChatVoice;
	@BindView(R.id.iv_menu)
	AppCompatImageView mIvMenu;
	@BindView(R.id.iv_mute)
	AppCompatImageView mIvMute;

	private int mChatId;
	private int mChatType;
	private int mMemberId;
	private EmotionInputDetector mEmotionInputDetector;

	private BroadcastReceiver mBroadcastReceiver;
	private LVChatAdapter mLvChatAdapter;
	private int mCurrentPage = 0;//当前第几聊天页数

	//  private PlayerService.PlayerBinder mPlayerBinder;
	//  private Intent mIntentService;
	//  private ServiceConnection connection = new ServiceConnection() {
	//    @Override
	//    public void onServiceConnected(ComponentName name, IBinder binder) {
	//      mPlayerBinder = (PlayerService.PlayerBinder) binder;
	//    }
	//
	//    @Override
	//    public void onServiceDisconnected(ComponentName name) {
	//    }
	//  };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_chat;
	}

	private void init() {
		DaggerChatComponent.builder()
			.chatPresenterModule(new ChatPresenterModule(this, this))
			.build()
			.inject(this);
		ToolBarConfig.builder(this, null)
			.setEnableMenu(true)
			.setIvMenuRes(R.drawable.ic_friends_contact_setting)
			.setMenuListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mChatType == CHAT_TYPE_1_FRIEND) {
						Intent intent = new Intent(ChatActivity.this, ChatInfoActivity.class);
						intent.putExtra("chat_id", mChatId);
						intent.putExtra("relate_id", mMemberId);
						startActivity(intent);
					} else if (mChatType == CHAT_TYPE_2_GROUP) {
						Intent intent = new Intent(ChatActivity.this, ChatGroupInfoActivity.class);
						intent.putExtra("chat_id", mChatId);
						startActivity(intent);
					}
				}
			})
			.setBackListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onBackPressed();
				}
			})
			.build();
		initViewPager();
		initEmotionKeyboard();
		registerWebSocketReceiver();

		//6.0以上需要权限申请
		//    requestPermissions();

		// 绑定播放服务
		//    mIntentService = new Intent(ChatActivity.this, PlayerService.class);
		//    bindService(mIntentService, connection, BIND_AUTO_CREATE);
	}

	private void registerWebSocketReceiver() {
		mBroadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				//收到消息后的接收广播,刷新数据
				String data = intent.getStringExtra("data");
				SocketMessage message = JsonUtil.jsonToBean(data, SocketMessage.class);
				assert message != null;

				if (message.getType() == IMWebSocketService.IM_SOCKET_TYPE_11_CHAT_MSG) {
					if (mChatId != 0) mChatPresenter.receiveMsg(mChatId);
				}
			}
		};
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(IMWebSocketService.WEB_SOCKET_CHAT_LIST);
		registerReceiver(mBroadcastReceiver, intentFilter);
	}

	private void initData() {
		mCurrentPage = 0;
		mChatPresenter.clearData();

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		mMemberId = bundle.getInt("member_id");
		mTvTitle.setText(bundle.getString("name"));
		mChatId = bundle.getInt("chat_id");

		if (mChatId == 0) {//这种情况可能出现在如new friends页面，只传入memberId.所以手动获取chatId
			mChatId = ChatDao.getInstance().getChatId(mMemberId);
			if (mChatId == 0) {//从数据库获取不到chatId的话，说明数据库还未插入好友成员。这种情况可能发生在第一次accept好友或者是网络异常导致以前没有插入
				mChatPresenter.getNewChatMsg(mMemberId);
			}
		}
		if (mChatId != 0) {
			mChatType = ChatDao.getInstance().getChatType(mChatId);
			mChatPresenter.checkUpdateChatMsg(mChatId, mCurrentPage);

			if (mChatType == CHAT_TYPE_1_FRIEND) {//单人聊天
				mIvMenu.setImageResource(R.drawable.ic_friends_contact_setting);
			} else if (mChatType == CHAT_TYPE_2_GROUP) {//群组聊天
				mIvMenu.setImageResource(R.drawable.ic_friends_group_chat_setting);
			}
		}
	}

	@Override
	public void getNewChatMsgResult() {
		mChatId = ChatDao.getInstance().getChatId(mMemberId);
		if (mChatId != 0) {
			mChatType = ChatDao.getInstance().getChatType(mChatId);
			mChatPresenter.getChatMsg(mChatId, mCurrentPage);
		} else {
			Utils.showToast(R.string.fail);
		}
	}

	@Override
	public void setMute(int flagMuteNotifications) {
		if (flagMuteNotifications == AppGlobal.FLAG_MUTE_NOTIFICATIONS_1_YES) {
			mIvMute.setVisibility(View.VISIBLE);
		} else {
			mIvMute.setVisibility(View.GONE);
		}
	}


	@Override
	public void onBackPressed() {
		boolean isShowOn = mEmotionInputDetector.interceptBackPress();
		super.onBackPressed();
//		if (!isShowOn) {////todo cjw
//			mChatPresenter.updateChatMsg(mChatId);
//			finish();
//		}
	}

	private void initEmotionKeyboard() {
		mEmotionInputDetector = EmotionInputDetector.with(this)
			.setEmotionView(mEmotionLayout)
			.setViewPager(mViewPager)
			.bindToEditText(mEditText)
			.bindToContent(mBarLayout)
			.bindToEmotionButton(mEmotionImage)
			.bindToAddButton(mIvAdd)
			.bindToSendButton(mButton)
			.bindToListView(mListView)
			.bindToVoiceText(mTvChatVoice)
			.bindToVoiceButton(mIvChatSwitching, mIvChatKeyboard)
			.build();
		GlobalOnItemClickManagerUtils globalOnItemClickListener = GlobalOnItemClickManagerUtils.getInstance(this);
		globalOnItemClickListener.attachToEditText(mEditText);
	}

	private void initViewPager() {
		ArrayList<Fragment> fragments = new ArrayList<>();
		EmotionFragment emotionFragment = new EmotionFragment();
		fragments.add(emotionFragment);
		FunctionFragment functionFragment = new FunctionFragment();
		fragments.add(functionFragment);
		CommonFragmentPagerAdapter pagerAdapter = new CommonFragmentPagerAdapter(getSupportFragmentManager(), fragments);
		mViewPager.setAdapter(pagerAdapter);
		mViewPager.setCurrentItem(0);
		mLvChatAdapter = new LVChatAdapter(this, new ArrayList<ChatMsg>());//初始化
		mListView.setAdapter(mLvChatAdapter);
	}

	@OnClick({R.id.btn_chat_send})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.btn_chat_send:
				String msg = mEditText.getText().toString().trim();
				mChatPresenter.sendMsg(msg, MSG_TYPE_1_TEXT, mMemberId);
				mEditText.setText("");
				break;
		}
	}

	@Override
	public void showChatMsg(List<ChatMsg> list, int currentPage, int position) {
		mCurrentPage = currentPage;
		mLvChatAdapter = new LVChatAdapter(this, list);
		mListView.setAdapter(mLvChatAdapter);
		if (mCurrentPage == 1) {
			mListView.setSelection(mListView.getBottom());
		} else {
			mListView.setSelection(position);
		}

		mLvChatAdapter.setOnImageClickListener(new LVChatAdapter.OnImageClickListener() {
			@Override
			public void onImageClick(int position, ImageView imageView) {
				int memberId = mLvChatAdapter.getData().get(position).getMemberId();
				Intent intent = new Intent(ChatActivity.this, FriendProfileActivity.class);
				intent.putExtra("member_id", memberId);
				startActivity(intent);
			}
		});
		mLvChatAdapter.setOnVoiceClickListener(new LVChatAdapter.OnVoiceClickListener() {
			@Override
			public void onVoiceClick(int position, TextView textView) {
				String msg = mLvChatAdapter.getData().get(position).getContent();
				mChatPresenter.playVoice(msg);
			}
		});
		mLvChatAdapter.setOnTransferClickListener(new LVChatAdapter.OnTransferClickListener() {
			@Override
			public void onTransferClick(int position, LinearLayout linearLayout) {
				int transferID = mLvChatAdapter.getData().get(position).getTransferId();
				toTransferDetail(transferID);
			}
		});
		mListView.setOnRefreshListener(new ChatListView.onRefreshListener() {
			@Override
			public void onRefresh() {
				mChatPresenter.getChatMsg(mChatId, mCurrentPage);
			}
		});
		mListView.refreshFinish();
	}

	private void toTransferDetail(int transferID) {
		Intent intent = new Intent(ChatActivity.this, TransferRecentDetailActivity.class);
		intent.putExtra("id", transferID);
		intent.putExtra("source", TransferRecentDetailActivity.SOURCE_1_MEMBER);
		startActivity(intent);
	}

	@Override
	public void addLastChatMsg(ChatMsg chatMsg) {
		//把新消息加到现有的
		if (mLvChatAdapter != null) {
			mLvChatAdapter.addFootData(chatMsg);
			mListView.setSelection(mListView.getBottom());
		}
	}

	@Override
	public void noMoreData(boolean isFirstLoad) {
		mListView.banRefresh();
		if (!isFirstLoad) Utils.showToast(R.string.no_more);
	}

	@Override
	public void refreshFail(int currentPage) {
		mCurrentPage = currentPage;
		mListView.refreshFinish();
	}

	@Override
	public void playVoice(String filePath) {
		//    mIntentService.putExtra("filePath", filePath);
		//    startService(mIntentService);// 将信息传递给服务
	}

	/**
	 * 开启语音之前判断权限是否打开
	 */
	private void requestPermissions() {
		//判断是否开启摄像头权限，是否开启语音权限
		if ((ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) && (ContextCompat
			.checkSelfPermission(mContext, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
			mEmotionInputDetector.setVoiceTouchListener();
		} else {
			//请求获取摄像头权限，语音权限
			ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, REQUEST_CODE_66_VOICE);
		}

	}

	/**
	 * 请求权限回调
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		if (requestCode == REQUEST_CODE_66_VOICE) {
			if ((grantResults[0] == PackageManager.PERMISSION_GRANTED) && (grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
				mEmotionInputDetector.setVoiceTouchListener();
			} else {
				Toast.makeText(mContext, Utils.getString(R.string.record_require_permissions), Toast.LENGTH_SHORT)
					.show();
			}
		}
	}

	public int getFriendID() {
		return mMemberId;
	}

	//  @Subscribe
	//  public void onChatMsgEvent(ChatMsgEvent msgEvent){
	//    mChatPresenter.sendMediaMsg(msgEvent,mMemberId,mGroupID);
	//  }

	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}

	@Override
	protected void onDestroy() {
		if (mBroadcastReceiver != null) {
			unregisterReceiver(mBroadcastReceiver);
			mBroadcastReceiver = null;
			//          unbindService(connection);
		}
		super.onDestroy();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case FunctionFragment.REQUEST_CODE_1_TRANSFER:
				if (resultCode == ToMemberActivity.RESULT_OK) {
					int transferID = data.getIntExtra("transfer_id", 0);
					mChatPresenter.addTransferMsg(transferID, getFriendID());
				}
				break;
		}
	}

	@Subscribe
	public void onRefresh(FinishEvent finishEvent) {
		if (finishEvent.getCode() == AppGlobal.FINISH_CODE_BUILD_CHAT_GROUP_SUCCESS) {
			finish();
		}
	}

}
