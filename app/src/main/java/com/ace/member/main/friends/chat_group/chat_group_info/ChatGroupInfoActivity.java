package com.ace.member.main.friends.chat_group.chat_group_info;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.adapter.GVChatGroupInfoAdapter;
import com.ace.member.adapter.GVChatInfoAdapter;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.ChatGroupInfoBean;
import com.ace.member.bean.MemberChatInfoBean;
import com.ace.member.event.FinishEvent;
import com.ace.member.main.friends.chat.chat_info.transfer_history.FriendTransferHistoryActivity;
import com.ace.member.main.friends.chat_group.build_group.BuildGroupActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.og.utils.CustomDialog;
import com.og.utils.GridViewForScrollView;
import com.og.utils.Utils;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;

public class ChatGroupInfoActivity extends BaseActivity implements ChatGroupInfoContract.ChatInfoView, AdapterView.OnItemClickListener {

	@Inject
	ChatGroupInfoPresenter mPresenter;
	@BindView(R.id.gv_user)
	GridViewForScrollView mGvUser;
	@BindView(R.id.sw_mute)
	SwitchCompat mSwMute;
	@BindView(R.id.tv_clear)
	TextView mTvClear;

	private GVChatGroupInfoAdapter mGvAdapter;
	private int mChatID;
	private ArrayList<Integer> mGroupMember;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerChatGroupInfoComponent.builder()
			.chatGroupInfoPresenterModule(new ChatGroupInfoPresenterModule(this, this))
			.build()
			.inject(this);
		initData();
		initActivity();
		iniListener();
		getData();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_chat_group_info;
	}

	private void initData() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			mChatID = bundle.getInt("chat_id");
		}
	}

	@Override
	protected void initActivity() {
		ToolBarConfig.builder(this, null).setTvTitleRes(R.string.chat_info).build();
	}

	private void iniListener() {
		mSwMute.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					mPresenter.setChatMute(mChatID, AppGlobal.FLAG_MUTE_NOTIFICATIONS_1_YES);
				} else {
					mPresenter.setChatMute(mChatID, AppGlobal.FLAG_MUTE_NOTIFICATIONS_2_NO);
				}
			}
		});

		mTvClear.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CustomDialog dialog = new CustomDialog.Builder(ChatGroupInfoActivity.this).setMessage(R.string.clear_chat_history)
					.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							mPresenter.clearChatHistory(mChatID);
							dialog.dismiss();
						}
					})
					.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					})
					.create();
				dialog.setCancelable(false);
				dialog.show();
			}
		});
	}

	public void getData() {
		mPresenter.getGroupChatInfo(mChatID);
	}

	@Override
	public void setChatGroupData(ChatGroupInfoBean chatGroupData) {
		if (chatGroupData == null) return;
		ArrayList<MemberChatInfoBean> memberChatList = chatGroupData.getMemberChatList();
		ArrayList<String> portraitList = new ArrayList<>();
		ArrayList<String> nameList = new ArrayList<>();
		mGroupMember = new ArrayList<>();
		for (MemberChatInfoBean memberChatInfo : memberChatList){
			mGroupMember.add(memberChatInfo.getMemberID());
			portraitList.add(memberChatInfo.getPortrait());
			nameList.add(memberChatInfo.getName());
		}
		mSwMute.setChecked(chatGroupData.getFlagMuteNotifications() == AppGlobal.FLAG_MUTE_NOTIFICATIONS_1_YES);
		mSwMute.setVisibility(View.VISIBLE);

		if (mGvAdapter == null) {
			mGvAdapter = new GVChatGroupInfoAdapter(portraitList, nameList, chatGroupData.isOwner());
		} else {
			mGvAdapter.setData(portraitList, nameList, chatGroupData.isOwner());
		}
		mGvUser.setAdapter(mGvAdapter);
		mGvUser.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (position == mGvAdapter.getCount() - 2) {
			//add member to chat group
			Intent intent = new Intent(ChatGroupInfoActivity.this, BuildGroupActivity.class);
			intent.putExtra("group_member", mGroupMember);
			startActivity(intent);
		} else if (position == mGvAdapter.getCount() - 1){
			//delete member from chat group
			Utils.showToast("to delete member");
		}
	}

	@Subscribe
	public void onRefresh(FinishEvent finishEvent) {
		if (finishEvent.getCode() == AppGlobal.FINISH_CODE_BUILD_CHAT_GROUP_SUCCESS) {
			finish();
		}
	}
}
