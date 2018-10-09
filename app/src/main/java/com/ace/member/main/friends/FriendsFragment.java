package com.ace.member.main.friends;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.ace.member.R;
import com.ace.member.adapter.LVRecentMsgAdapter;
import com.ace.member.base.BaseFragment;
import com.ace.member.bean.RecentMsg;
import com.ace.member.bean.SocketMessage;
import com.ace.member.main.friends.chat.ChatActivity;
import com.ace.member.main.friends.chat.add_contact.AddContactActivity;
import com.ace.member.main.friends.chat.contacts_list.ContactsActivity;
import com.ace.member.main.friends.chat.friend_profile.FriendProfileActivity;
import com.ace.member.main.friends.chat_group.build_group.BuildGroupActivity;
import com.ace.member.main.friends.db.dao.ChatDao;
import com.ace.member.main.friends.db.dao.ContactDao;
import com.ace.member.main.friends.search.SearchFriendsFragment;
import com.ace.member.popup_window.MenuPopWindow;
import com.ace.member.service.IMWebSocketService;
import com.ace.member.toolbar.ToolBarConfig;
import com.og.utils.JsonUtil;
import com.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class FriendsFragment extends BaseFragment implements FriendsContract.FriendsView, AdapterView.OnItemClickListener {
	public static final int REQUEST_CODE_3_SCAN = 3;

	private static final int RECENT_TYPE_100_CHAT_MSG = 100;
	private static final int RECENT_TYPE_999_OFFICIAL_MSG = 999;//最近消息类型，官方通知消息

	@Inject
	FriendsPresenter mPresenter;

	@BindView(R.id.iv_menu)
	AppCompatImageView mIvMenu;
	@BindView(R.id.lv_friends_chat)
	ListView mListView;
	@BindView(R.id.et_friends_search)
	EditText mEtFriendsSearch;

	private ArrayList<Integer> mFunctionMoreIcon = new ArrayList<>(Arrays.asList(R.drawable.ic_friends_group_chat, R.drawable.ic_friends_add_contact_black, R.drawable.ic_scan_contact));
	private ArrayList<Integer> mFunctionMoreIconName = new ArrayList<>(Arrays.asList(R.string.group_chat, R.string.add_contact, R.string.scan));
	private BroadcastReceiver mBroadcastReceiver;
	private LVRecentMsgAdapter mAdapter;
	private SearchFriendsFragment mSearchDialog;
	private MenuPopWindow mMenuPopWindow;
	private ToolBarConfig mToolBarConfig;

	@Override
	protected int getContentViewLayout() {
		return R.layout.fragment_friends;
	}

	@Override
	protected void initView() {
		DaggerFriendsComponent.builder().friendsPresenterModule(new FriendsPresenterModule(this, getActivity())).build().inject(this);
		mToolBarConfig = ToolBarConfig.builder(null, getView()).setTvTitleRes(R.string.friends).setIvMenuRes(R.drawable.ic_friends_contacts_list, R.drawable.ic_friends_more).setEnableMenu(true).setEnableBack(false).build();
		initMenuPopWindow();
	}

	@Override
	protected void initData() {
		initSearchDialog();
		registerWebSocketReceiver();
	}

	private void initSearchDialog() {
		mSearchDialog = new SearchFriendsFragment();
	}

	private void initMenuPopWindow() {
		mMenuPopWindow = new MenuPopWindow.Builder(getContext(), mFunctionMoreIconName, new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int resourceId = mFunctionMoreIcon.get(position);
				switch (resourceId) {
					case R.drawable.ic_friends_group_chat:
						startActivity(new Intent(getActivity(), BuildGroupActivity.class));
						break;
					case R.drawable.ic_friends_add_contact_black:
						startActivity(new Intent(getActivity(), AddContactActivity.class));
						break;
					case R.drawable.ic_scan_contact:
						startActivityForResult(new Intent(getActivity(), CaptureActivity.class), REQUEST_CODE_3_SCAN);
						break;
				}
				mMenuPopWindow.dismiss();
			}
		}).setItemIcons(mFunctionMoreIcon).build();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			if (requestCode == REQUEST_CODE_3_SCAN) {
				String result = data.getStringExtra("result");
				String phone = result.split("\\|")[1];
				if (!TextUtils.isEmpty(phone)) mPresenter.isFriend(phone);
			}
		}
	}

	@Override
	public void toChatActivity(int id, String name) {
		Intent intent = new Intent(getActivity(), ChatActivity.class);
		intent.putExtra("member_id", id);
		intent.putExtra("name", name);
		startActivity(intent);
	}

	@Override
	public void toFriendProfileActivity(int member_id) {
		Intent intent = new Intent(getActivity(), FriendProfileActivity.class);
		intent.putExtra("member_id", member_id);
		startActivity(intent);
	}

	private void registerWebSocketReceiver() {
		mBroadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				//收到消息后的接收广播,刷新数据
				String data = intent.getStringExtra("data");
				SocketMessage message = JsonUtil.jsonToBean(data, SocketMessage.class);
				assert message != null;

				int type = message.getType();
				if (type == IMWebSocketService.IM_SOCKET_TYPE_11_CHAT_MSG) {
					mPresenter.getFriendsMsgList();
				} else if (type == IMWebSocketService.IM_SOCKET_TYPE_22_REQUEST_MSG) {
					mToolBarConfig.enableLeftDot(true);
					mPresenter.getFriendRequestList();
				}
			}
		};
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(IMWebSocketService.WEB_SOCKET_CHAT_LIST);
		getActivity().registerReceiver(mBroadcastReceiver, intentFilter);
	}

	@OnClick({R.id.iv_menu_left, R.id.iv_menu, R.id.et_friends_search})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.iv_menu_left:
				startActivity(new Intent(getActivity(), ContactsActivity.class));
				break;
			case R.id.iv_menu:
				mMenuPopWindow.showAsDropDown(getActivity(), mIvMenu);
				break;
			case R.id.et_friends_search:
				mSearchDialog.show(getActivity().getSupportFragmentManager(), "SearchFriendsFragment");
				break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		mPresenter.getFriendsMsgList();
	}

	@Override
	public void showRecentMsgList(List<RecentMsg> recentMsgList) {
		mAdapter = new LVRecentMsgAdapter(recentMsgList, getContext());
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);

		mToolBarConfig.enableLeftDot(ContactDao.getInstance().getNtfRequestUnreadCount() > 0);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		RecentMsg recentMsg = mAdapter.getData().get(position);
		int type = recentMsg.getType();
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		if (type == RECENT_TYPE_100_CHAT_MSG) {
			intent.setClass(getActivity(), ChatActivity.class);
			int chatId = recentMsg.getChatId();
			ChatDao.getInstance().updateChatMsg(chatId);//更新状态
			bundle.putInt("chat_id", chatId);
			bundle.putInt("member_id", recentMsg.getMemberId());
			bundle.putString("name", recentMsg.getName());
			intent.putExtras(bundle);
			startActivity(intent);
		} else if (type == RECENT_TYPE_999_OFFICIAL_MSG) {
		}
	}

	@Override
	public void onDestroyView() {
		if (mMenuPopWindow != null) mMenuPopWindow.dismiss();
		if (mSearchDialog != null) mSearchDialog = null;
		if (mBroadcastReceiver != null) {
			getActivity().unregisterReceiver(mBroadcastReceiver);
			mBroadcastReceiver = null;
		}
		super.onDestroyView();
	}
}
