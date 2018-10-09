package com.ace.member.main.friends.chat.new_friends;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.ace.member.R;
import com.ace.member.adapter.LVNewFriendsAdapter;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.NewFriendsInfo;
import com.ace.member.main.friends.chat.ChatActivity;
import com.ace.member.main.friends.chat.friend_profile.FriendProfileActivity;
import com.ace.member.main.friends.db.dao.ContactDao;
import com.ace.member.main.friends.search.SearchFriendsFragment;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.ContentContactsHelper;
import com.og.utils.Utils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class NewFriendsActivity extends BaseActivity implements NewFriendsContract.NewFriendsView, OnItemClickListener {
	private static final int PERMISSION_REQUEST_CONTACTS_55_CODE = 55;

	private static final int TYPE_ADD_3_SEARCH_VIA_PHONE = 3;

	private static final int STATUS_0_REQUEST_ADD = 0;
	private static final int STATUS_1_REQUEST_PENDING = 1;

	private static final int TYPE_VIEW_1_TITLE = 1;
	@Inject
	NewFriendsPresenter mPresenter;

	@BindView(R.id.lv_new_friends)
	ListView mLvNewFriends;

	private LVNewFriendsAdapter mAdapter;
	private SearchFriendsFragment mSearchFriendsFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActivity();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_new_friends;
	}

	@Override
	protected void initActivity() {
		DaggerNewFriendsComponent.builder().newFriendsModule(new NewFriendsModule(this, this)).build().inject(this);
		ToolBarConfig.builder(this, null).setTvTitleRes(R.string.new_friends).setEnableBack(true).setBackListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		}).build();
	}

	private void readContactsWithPermissions() {
		if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
			mPresenter.getMayKnowFriends(ContentContactsHelper.readContacts(mContext));
		} else {
			ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_CONTACTS_55_CODE);
		}
	}

	/**
	 * 请求权限回调
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == PERMISSION_REQUEST_CONTACTS_55_CODE) {
			if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				mPresenter.getMayKnowFriends(ContentContactsHelper.readContacts(mContext));
			} else {
				Toast.makeText(mContext, Utils.getString(R.string.read_contacts_require_permissions), Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		mPresenter.getNewFriendsList();
	}

	@Override
	public void showNewFriendsList(final List<NewFriendsInfo> newFriendsInfoList) {
		mAdapter = new LVNewFriendsAdapter(newFriendsInfoList, mContext, true);
		mLvNewFriends.setAdapter(mAdapter);
		mLvNewFriends.setOnItemClickListener(this);
		mAdapter.setButtonClickListener(new LVNewFriendsAdapter.OnButtonClickListener() {
			@Override
			public void onButtonClick(int position, Button button) {
				NewFriendsInfo newFriendsInfo = mAdapter.getData().get(position);
				int memberId = newFriendsInfo.getId();
				String name = newFriendsInfo.getName();
				int status = (Integer) button.getTag(R.string.status);
				if (status == STATUS_1_REQUEST_PENDING) {
					mPresenter.acceptApplication(memberId, name, newFriendsInfo.getTypeAdd());
					button.setText(R.string.added);
					button.setTextColor(Utils.getColor(R.color.clr_chat_tv_hint));
					button.setEnabled(false);
				} else if (status == STATUS_0_REQUEST_ADD) {
					mPresenter.addApplication(memberId, "", TYPE_ADD_3_SEARCH_VIA_PHONE);
					button.setText(R.string.pending);
					button.setTextColor(Utils.getColor(R.color.clr_chat_tv_hint));
					button.setEnabled(false);
				}
			}
		});
		readContactsWithPermissions();//读取手机联系人，如果没有权限就会申请
		ContactDao.getInstance().updateNtfRequestStatus();//更新好友申请通知状态，已读
	}

	@Override
	public void showAcceptResult(Boolean isSuccess, int memberId, String nameRemark) {
		if (isSuccess) {
			Intent intent = new Intent(NewFriendsActivity.this, ChatActivity.class);
			intent.putExtra("member_id", memberId);
			intent.putExtra("name", nameRemark);
			startActivity(intent);
		} else {
			Utils.showToast(R.string.fail);
		}
	}

	@Override
	public void showMayKnowFriends(List<NewFriendsInfo> mayKnowFriendsList) {
		mAdapter.addData(mayKnowFriendsList);
	}

	@Override
	public void showAddResult(Boolean isSuccess) {
		if (isSuccess) {
			Utils.showToast(R.string.success);
		} else {
			Utils.showToast(R.string.fail);
		}
	}

	@OnClick(R.id.et_friends_search)
	public void onViewClicked(View view) {
		mSearchFriendsFragment = new SearchFriendsFragment();
		mSearchFriendsFragment.show(getSupportFragmentManager(), "SearchFriendsFragment");
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		NewFriendsInfo newFriendsInfo = mAdapter.getData().get(position);
		if (newFriendsInfo.getType() == TYPE_VIEW_1_TITLE) return;//标题
		int memberId = newFriendsInfo.getId();
		Intent intent = new Intent(NewFriendsActivity.this, FriendProfileActivity.class);
		intent.putExtra("member_id", memberId);
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		if (mSearchFriendsFragment != null) mSearchFriendsFragment = null;
		super.onDestroy();
	}
}
