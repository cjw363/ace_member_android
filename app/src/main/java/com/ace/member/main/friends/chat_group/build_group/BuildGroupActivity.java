package com.ace.member.main.friends.chat_group.build_group;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.adapter.GVBuildGroupAdapter;
import com.ace.member.adapter.LVBuildGroupAdapter;
import com.ace.member.adapter.LVSearchContactsAdapter;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.ContactInfo;
import com.ace.member.event.FinishEvent;
import com.ace.member.main.friends.chat.ChatActivity;
import com.ace.member.main.friends.chat.chat_info.ChatInfoActivity;
import com.ace.member.main.friends.db.dao.ContactDao;
import com.ace.member.main.friends.search.SearchFriendsFragment;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.Session;
import com.ace.member.view.SideBar;
import com.og.utils.EventBusUtil;
import com.og.utils.GridViewForScrollView;
import com.og.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class BuildGroupActivity extends BaseActivity implements BuildGroupContract.BuildGroupView, TextWatcher {

	@Inject
	BuildGroupPresenter mBuildGroupPresenter;

	@BindView(R.id.tv_contacts_letter)
	TextView mTvLetterShow;
	@BindView(R.id.sb_contacts)
	SideBar mSideBar;
	@BindView(R.id.lv_contacts)
	ListView mListView;
	@BindView(R.id.tv_menu)
	TextView mTvMenu;
	@BindView(R.id.gv_build_group)
	GridViewForScrollView mGridView;
	@BindView(R.id.hsv_build_group)
	HorizontalScrollView mHorizontalScrollView;
	@BindView(R.id.et_build_group)
	EditText mEtSearch;
	@BindView(R.id.ll_toolbar_menu)
	LinearLayout mLlToolbarMenu;
	@BindView(R.id.rl_contacts)
	RelativeLayout mRlContacts;
	@BindView(R.id.lv_search_contacts)
	ListView mLvSearchContacts;
	@BindView(R.id.ll_search_contacts)
	LinearLayout mLlSearchContacts;

	private LVBuildGroupAdapter mLvAdapter;
	private GVBuildGroupAdapter mGvAdapter;
	private LVSearchContactsAdapter mLvSearchAdapter;
	private SearchFriendsFragment mSearchFriendsFragment;

	private List<Integer> mGroupMember;
	private int mItemWidth = Utils.getDimenDp(R.dimen.width50);//item宽
	private int mMaxWidth = Utils.getScreenWidth() * 3 / 5;//HorizontalScrollView的最大宽度

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActivity();
		initData();
		initGridView();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_build_group;
	}

	private void initData() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			mGroupMember = bundle.getIntegerArrayList("group_member");
		}
		if (mGroupMember == null){
			setTvMenu(0);
		}else {
			setTvMenu(mGroupMember.size() == 1 ? 0 : mGroupMember.size() -1);
		}
	}

	private void initGridView() {
		mGvAdapter = new GVBuildGroupAdapter(mContext, null);
		mGridView.setAdapter(mGvAdapter);
		mGridView.setHorizontalSpacing(0); // 设置列表项水平间距
		mGridView.setStretchMode(GridView.NO_STRETCH);
	}

	@Override
	protected void initActivity() {
		DaggerBuildGroupComponent.builder().buildGroupPresenterModule(new BuildGroupPresenterModule(this, this)).build().inject(this);
		ToolBarConfig.builder(this, null).setTvTitleRes(R.string.group_chat).setMenuType(ToolBarConfig.MenuType.MENU_TEXT).setEnableMenu(true).build();
	}

	@OnClick({R.id.ll_toolbar_menu})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.ll_toolbar_menu:
				Map<Integer, ContactInfo> selectedMap = getSelectedMap();
				if (selectedMap.size() == 1) {//跳转到单人聊天
					ContactInfo contactInfo = getSelectedList().get(0);
					int memberId = contactInfo.getMemberId();
					String name = contactInfo.getName();
					Intent intent = new Intent(BuildGroupActivity.this, ChatActivity.class);
					intent.putExtra("member_id", memberId);
					intent.putExtra("name", name);
					startActivity(intent);
				} else if (selectedMap.size() > 1) {
					// finish ChatInfoActivity and jump into GroupChatActivity
					selectedMap.put(Session.user.getId(), new ContactInfo(Session.user.getId(), Session.user.getName()));
					mBuildGroupPresenter.buildGroupChat(selectedMap);
				}
				EventBusUtil.post(new FinishEvent(AppGlobal.FINISH_CODE_BUILD_CHAT_GROUP_SUCCESS));
				finish();
				break;
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
		mLvAdapter = new LVBuildGroupAdapter(contactInfoList, mContext, mGroupMember);
		mListView.setAdapter(mLvAdapter);
		mLvAdapter.setCheckBoxClickListener(new LVBuildGroupAdapter.OnCheckBoxClickListener() {
			@Override
			public void onCheckBoxClick(int position, CheckBox checkBox) {
				ContactInfo contactInfo = mLvAdapter.getData().get(position);
				checkBox.setChecked(!mLvAdapter.isSelected(contactInfo.getMemberId()));
				if (checkBox.isChecked()) {
					mLvAdapter.putSelectedMember(position);
				} else {
					mLvAdapter.removeSelectedMember(position);
				}

				setTvMenu(getSelectedList().size());
				setGridViewData();//设置gridview的数据
			}
		});
		setGridViewData();//初始化
		mEtSearch.addTextChangedListener(this);
	}

	@Override
	public void toGroupChat(int chatID, String groupName) {
		Intent intent = new Intent(BuildGroupActivity.this, ChatActivity.class);
		intent.putExtra("chat_id", chatID);
		intent.putExtra("name", groupName);
		startActivity(intent);
	}

	//设置右上角标题
	private void setTvMenu(int count) {
		if (count > 0) {
			mTvMenu.setText(Utils.getString(R.string.ok) + "(" + count + ")");
			mLlToolbarMenu.setVisibility(View.VISIBLE);
		} else {
			mTvMenu.setText("");
			mLlToolbarMenu.setVisibility(View.GONE);
		}
	}

	//设置gridview的数据
	private void setGridViewData() {
		List<ContactInfo> selectedList = getSelectedList();
		int itemCount = selectedList.size();
		int gridViewWith = mItemWidth * itemCount;
		LinearLayout.LayoutParams mParams;
		if (gridViewWith > mMaxWidth)
			mParams = new LinearLayout.LayoutParams(mMaxWidth, LinearLayout.LayoutParams.MATCH_PARENT);
		else
			mParams = new LinearLayout.LayoutParams(gridViewWith, LinearLayout.LayoutParams.MATCH_PARENT);

		mHorizontalScrollView.setLayoutParams(mParams);
		mGridView.setLayoutParams(new LinearLayout.LayoutParams(gridViewWith, LinearLayout.LayoutParams.MATCH_PARENT)); // 横向布局的关键
		mGridView.setColumnWidth(mItemWidth); // 设置列表项宽
		mGridView.setNumColumns(itemCount); // 设置列数量=列表集合数

		mGvAdapter.setData(selectedList);
	}

	//获取选中的联系人列表
	private List<ContactInfo> getSelectedList() {
		Map<Integer, ContactInfo> selectedMap = mLvAdapter.getSelectedMap();
		if (mLvSearchAdapter != null) selectedMap.putAll(mLvSearchAdapter.getSelectedMap());

		return new ArrayList<>(selectedMap.values());
	}

	//获取选中的联系人map
	private Map<Integer, ContactInfo> getSelectedMap() {
		Map<Integer, ContactInfo> selectedMap = mLvAdapter.getSelectedMap();
		if (mLvSearchAdapter != null) selectedMap.putAll(mLvSearchAdapter.getSelectedMap());

		return selectedMap;
	}

	@Override
	protected void onResume() {
		super.onResume();
		mBuildGroupPresenter.getContactsList();
	}

	//根据字符串的首字母指定位置
	private void setSelectionByStr(CharSequence s) {
		if (s.equals("↑")) {
			mListView.setSelection(0);
		} else {
			int position = mLvAdapter.getPositionForSelection(s.charAt(0));
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

	@Override
	public void onTextChanged(CharSequence cs, int start, int before, int count) {
		if (!TextUtils.isEmpty(cs)) {
			//从本地数据搜索匹配名字和手机
			String inputStr = cs.toString();
			List<ContactInfo> contactInfoList = ContactDao.getInstance().querySearchContacts(inputStr);
			mLvSearchAdapter = new LVSearchContactsAdapter(contactInfoList, this, mGroupMember, mLvAdapter.getSelectedMap());
			mLvSearchContacts.setAdapter(mLvSearchAdapter);
			mLvSearchAdapter.setCheckBoxClickListener(new LVSearchContactsAdapter.OnCheckBoxClickListener() {
				@Override
				public void onCheckBoxClick(int position, CheckBox checkBox) {
					ContactInfo contactInfo = mLvSearchAdapter.getData().get(position);
					checkBox.setChecked(!mLvSearchAdapter.isSelected(contactInfo.getMemberId()));
					if (checkBox.isChecked()) {
						mLvSearchAdapter.putSelectedMember(position);
					} else {
						mLvSearchAdapter.removeSelectedMember(position);
					}
					setTvMenu(getSelectedList().size());
					setGridViewData();//设置gridview的数据
				}
			});

			mRlContacts.setVisibility(View.GONE);
			mLlSearchContacts.setVisibility(View.VISIBLE);
		} else {
			mLvAdapter.setSelectedMap(getSelectedMap());//更新列表

			mRlContacts.setVisibility(View.VISIBLE);
			mLlSearchContacts.setVisibility(View.GONE);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	@Override
	public void afterTextChanged(Editable s) {}
}
