package com.ace.member.main.friends.chat.contacts_list;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.adapter.LVContactsAdapter;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.ContactInfo;
import com.ace.member.main.friends.chat.add_contact.AddContactActivity;
import com.ace.member.main.friends.chat.friend_profile.FriendProfileActivity;
import com.ace.member.main.friends.chat.new_friends.NewFriendsActivity;
import com.ace.member.main.friends.search.SearchFriendsFragment;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.view.SideBar;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class ContactsActivity extends BaseActivity implements ContactsContract.ContactsView, AdapterView.OnItemClickListener {

	public static final int POSITION_0_NEW_FRIENDS = 0;
	public static final int POSITION_1_GROUP_CHAT = 1;
	public static final int POSITION_2_OFFICIAL_ACCOUNT = 2;
	public static final int HEADER_3_COUNT = 3;

	@Inject
	ContactsPresenter mContactsPresenter;
	@BindView(R.id.iv_menu)
	ImageView mIvMenu;
	@BindView(R.id.tv_friends_first_letter_show)
	TextView mTvLetterShow;
	@BindView(R.id.sidebar_friends)
	SideBar mSideBar;
	@BindView(R.id.lv_friends)
	ListView mListView;
	@BindView(R.id.et_friends_search)
	EditText mEtFriendsSearch;

	private LVContactsAdapter mAdapter;
	private View mNewFriendsHeadView;
	private View mGroupChatHeadView;
	private View mOfficialAccountHeadView;
	private SearchFriendsFragment mSearchFriendsFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_contacts;
	}

	private void initView() {
		DaggerContactsComponent.builder().contactsPresenterModule(new ContactsPresenterModule(this, this)).build().inject(this);
		ToolBarConfig.builder(this, null).setTvTitleRes(R.string.contacts).setIvMenuRes(R.drawable.ic_friends_add_contact).setEnableMenu(true).build();
	}

	@OnClick({R.id.ll_toolbar_menu, R.id.et_friends_search})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.ll_toolbar_menu:
				startActivity(new Intent(ContactsActivity.this, AddContactActivity.class));
				break;
			case R.id.et_friends_search:
				mSearchFriendsFragment = new SearchFriendsFragment();
				mSearchFriendsFragment.show(getSupportFragmentManager(), "SearchFriendsFragment");
				break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (position == POSITION_0_NEW_FRIENDS) {
			startActivity(new Intent(this, NewFriendsActivity.class));
		} else if (position == POSITION_1_GROUP_CHAT) {

		} else if (position == POSITION_2_OFFICIAL_ACCOUNT) {

		}
	}

	@Override
	public void showContactsList(List<ContactInfo> contactInfoList) {
		mSideBar.setTextView(mTvLetterShow);
		mSideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
			@Override
			public void onTouchingLetterChanged(String s) {
				setSelectionByStr(s);
			}
		});
		mAdapter = new LVContactsAdapter(contactInfoList, mContext);
		mListView.setAdapter(mAdapter);
		mAdapter.setItemClickListener(new LVContactsAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(int position, View view) {
				ContactInfo contactInfo = mAdapter.getData().get(position);
				Intent intent = new Intent(ContactsActivity.this, FriendProfileActivity.class);
				intent.putExtra("member_id", contactInfo.getMemberId());
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onResume() {
		mContactsPresenter.getContactsList();
		if (mNewFriendsHeadView == null) {
			mNewFriendsHeadView = View.inflate(mContext, R.layout.view_contacts_head_new_friends, null);
			mListView.addHeaderView(mNewFriendsHeadView);
		}
		if (mGroupChatHeadView == null) {
			mGroupChatHeadView = View.inflate(mContext, R.layout.view_contacts_head_group_chat, null);
			mListView.addHeaderView(mGroupChatHeadView);
		}
		if (mOfficialAccountHeadView == null) {
			mOfficialAccountHeadView = View.inflate(mContext, R.layout.view_contacts_head_official_account, null);
			mListView.addHeaderView(mOfficialAccountHeadView);
		}
		mListView.setOnItemClickListener(this);

		super.onResume();
	}

	//根据字符串的首字母指定位置
	private void setSelectionByStr(CharSequence s) {
		if (s.equals("↑")) {
			mListView.setSelection(0);
		} else {
			int position = mAdapter.getPositionForSelection(s.charAt(0));
			if (position != -1) {
				mListView.setSelection(position);
			}
		}
	}

	@Override
	protected void onDestroy() {
		if (mSearchFriendsFragment != null) mSearchFriendsFragment = null;
		super.onDestroy();
	}

}
