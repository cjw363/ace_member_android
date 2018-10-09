package com.ace.member.main.friends.chat.chat_info;

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
import com.ace.member.adapter.GVChatInfoAdapter;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.MemberChatInfoBean;
import com.ace.member.event.FinishEvent;
import com.ace.member.main.friends.chat.chat_info.transfer_history.FriendTransferHistoryActivity;
import com.ace.member.main.friends.chat_group.build_group.BuildGroupActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.og.utils.CustomDialog;
import com.og.utils.GridViewForScrollView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * 只用于单人聊天,多人聊天info移到 ChatGroupInfoActivity
 */
public class ChatInfoActivity extends BaseActivity implements ChatInfoContract.ChatInfoView, AdapterView.OnItemClickListener {

	@Inject
	ChatInfoPresenter mPresenter;
	@BindView(R.id.rl_transfer_history)
	RelativeLayout mRlTransferHistory;
	@BindView(R.id.gv_user)
	GridViewForScrollView mGvUser;
	@BindView(R.id.sw_mute)
	SwitchCompat mSwMute;
	@BindView(R.id.tv_clear)
	TextView mTvClear;

	private GVChatInfoAdapter mGvAdapter;
	private int mChatID;
	private int mRelateID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerChatInfoComponent.builder()
			.chatInfoPresenterModule(new ChatInfoPresenterModule(this, this))
			.build()
			.inject(this);
		initData();
		initActivity();
		iniListener();
		getData();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_chat_info;
	}

	private void initData() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			mChatID = bundle.getInt("chat_id");
			mRelateID = bundle.getInt("relate_id");
		}
	}

	@Override
	protected void initActivity() {
		ToolBarConfig.builder(this, null).setTvTitleRes(R.string.chat_info).build();
	}

	private void iniListener() {
		mRlTransferHistory.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ChatInfoActivity.this, FriendTransferHistoryActivity.class);
				intent.putExtra("member_id", mRelateID);
				startActivity(intent);
			}
		});

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
				CustomDialog dialog = new CustomDialog.Builder(ChatInfoActivity.this).setMessage(R.string.clear_chat_history)
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
		mPresenter.getMemberChatInfo(mChatID, mRelateID);
	}

	@Override
	public void setSingleChatData(MemberChatInfoBean memberChatInfoBean) {
		mSwMute.setChecked(memberChatInfoBean.getFlagMuteNotifications() == AppGlobal.FLAG_MUTE_NOTIFICATIONS_1_YES);
		mSwMute.setVisibility(View.VISIBLE);
		ArrayList<String> portraitList = new ArrayList<>();
		ArrayList<String> nameList = new ArrayList<>();
		portraitList.add(memberChatInfoBean.getPortrait());
		nameList.add(memberChatInfoBean.getName());
		if (mGvAdapter == null) {
			mGvAdapter = new GVChatInfoAdapter(portraitList, nameList);
		} else {
			mGvAdapter.setData(portraitList, nameList);
		}
		mGvUser.setAdapter(mGvAdapter);
		mGvUser.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (position == mGvAdapter.getCount() -1){
			Intent intent = new Intent(ChatInfoActivity.this, BuildGroupActivity.class);
			ArrayList<Integer> groupMember =  new ArrayList<>();
			groupMember.add(mRelateID);
			intent.putExtra("group_member", groupMember);
			startActivity(intent);
		}
	}

	@Subscribe
	public void onRefresh(FinishEvent finishEvent) {
		if (finishEvent.getCode() == AppGlobal.FINISH_CODE_BUILD_CHAT_GROUP_SUCCESS){
			finish();
		}
	}
}
