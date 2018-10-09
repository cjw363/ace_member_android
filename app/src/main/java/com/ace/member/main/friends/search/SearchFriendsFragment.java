package com.ace.member.main.friends.search;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatImageView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.adapter.LVSearchFriendsAdapter;
import com.ace.member.bean.SearchFriendsInfo;
import com.ace.member.main.friends.chat.ChatActivity;
import com.ace.member.main.friends.chat.friend_profile.FriendProfileActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.og.utils.Utils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchFriendsFragment extends DialogFragment implements SearchFriendsContract.SearchFriendsView, TextWatcher, AdapterView.OnItemClickListener {
	private static final int TYPE_ADD_3_SEARCH_VIA_PHONE = 3;//查找手机号码

	private static final int STATUS_0_REQUEST_ADD = 0;
	private static final int STATUS_1_REQUEST_PENDING = 1;

	private static final int TYPE_VIEW_1_TITLE = 1;
	@Inject
	SearchFriendsPresenter mPresenter;

	@BindView(R.id.et_friends_search)
	EditText mEtFriendsSearch;
	@BindView(R.id.iv_friends_search_clear)
	AppCompatImageView mIvFriendsSearchClear;
	@BindView(R.id.lv_friends_search)
	ListView mLvFriendsSearch;
	@BindView(R.id.tv_search_friends_no_more)
	TextView mTvSearchNoMore;

	private Window mWindow;
	private long startTime;
	private long delayTime = 2000;

	private static Handler mHandler = new Handler();
	private LVSearchFriendsAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mWindow = getDialog().getWindow();
		assert mWindow != null;
		mWindow.requestFeature(Window.FEATURE_NO_TITLE);// 必须放在setContextView之前调用
		return inflater.inflate(R.layout.fragment_dialog_search, (ViewGroup) mWindow.findViewById(android.R.id.content), false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initView();
	}

	private void initView() {
		DaggerSearchFriendsComponent.builder().searchFriendsPresenterModule(new SearchFriendsPresenterModule(this, getActivity())).build().inject(this);
		ToolBarConfig.Builder builder = new ToolBarConfig.Builder(null, getView());
		builder.setTvTitleRes(R.string.search).setEnableBack(true).setBackListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mEtFriendsSearch.setText("");
				dismiss();
			}
		}).build();
		//noinspection ConstantConditions
		ButterKnife.bind(this, getView());
		setWindow();// 设置窗体属性
		mEtFriendsSearch.addTextChangedListener(this);
	}

	/**
	 * 设置窗体属性
	 */
	private void setWindow() {
		// 透明状态栏
		//    mWindow.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		//
		int apiLevel = Build.VERSION.SDK_INT;
		if (apiLevel >= Build.VERSION_CODES.LOLLIPOP) {//5.0.1
			//取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
			//			mWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			//需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
			//      mWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			//设置状态栏颜色
			//			mWindow.setStatusBarColor(Utils.getColor(R.color.colorPrimary));
		}
		//    StatusBarCompat.compat(mWindow,getActivity());

		// 退出,进入动画
		mWindow.setWindowAnimations(R.style.AnimationRight);
		// 清理背景变暗
		mWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		// 点击window外的区域 是否消失
		getDialog().setCanceledOnTouchOutside(true);
		// 是否可以取消,会影响上面那条属性
		setCancelable(true);
		// window外可以点击,不拦截窗口外的事件
		mWindow.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
		// 设置背景颜色,只有设置了这个属性,宽度才能全屏MATCH_PARENT
		mWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		WindowManager.LayoutParams mWindowAttributes = mWindow.getAttributes();
		mWindowAttributes.width = WindowManager.LayoutParams.MATCH_PARENT;// 这个属性需要配合透明背景颜色,才会真正的
		// MATCH_PARENT
		mWindowAttributes.height = WindowManager.LayoutParams.MATCH_PARENT;
		mWindow.setAttributes(mWindowAttributes);
	}


	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		startTime = System.currentTimeMillis();
		mHandler.postDelayed(new CheckInputRunnable(), delayTime);
	}

	@Override
	public void showSearchResult(List<SearchFriendsInfo> searchFriendsInfoList) {
		if (searchFriendsInfoList != null && searchFriendsInfoList.size() != 0) {
			mAdapter = new LVSearchFriendsAdapter(getActivity(), searchFriendsInfoList);
			mLvFriendsSearch.setAdapter(mAdapter);
			mLvFriendsSearch.setOnItemClickListener(this);
			mLvFriendsSearch.setVisibility(View.VISIBLE);
			mTvSearchNoMore.setVisibility(View.GONE);
			mLvFriendsSearch.setOnItemClickListener(this);
			mAdapter.setButtonClickListener(new LVSearchFriendsAdapter.OnButtonClickListener() {
				@Override
				public void onButtonClick(int position, Button button) {
					SearchFriendsInfo searchFriendsInfo = mAdapter.getData().get(position);
					int memberId = searchFriendsInfo.getId();
					String name = searchFriendsInfo.getName();
					int status = (Integer) button.getTag(R.string.status);
					if (status == STATUS_1_REQUEST_PENDING) {
						mPresenter.acceptApplication(memberId, name, searchFriendsInfo.getTypeAdd());
						button.setEnabled(false);
						button.setText(R.string.added);
					} else if (status == STATUS_0_REQUEST_ADD) {
						mPresenter.addApplication(memberId, "", TYPE_ADD_3_SEARCH_VIA_PHONE);
						button.setEnabled(false);
						button.setText(R.string.pending);
					}
				}
			});
		} else {
			mLvFriendsSearch.setVisibility(View.GONE);
			mTvSearchNoMore.setVisibility(View.VISIBLE);
		}
	}

	@OnClick({R.id.iv_friends_search_clear, R.id.tv_friends_search})
	public void onClickView(View view) {
		switch (view.getId()) {
			case R.id.iv_friends_search_clear:
				mEtFriendsSearch.setText("");
				break;
			case R.id.tv_friends_search:
				startTime = System.currentTimeMillis();//修改时间使上一次的无效
				mHandler.post(new Runnable() {//立即执行
					@Override
					public void run() {
						String keyWord = mEtFriendsSearch.getText().toString().trim();
						if (!TextUtils.isEmpty(keyWord)) {
							mPresenter.searchKeyWord(keyWord);
						}
					}
				});
				break;
		}
	}

	@Override
	public void showAddResult(boolean isSuccess) {
		if (isSuccess) {
			Utils.showToast(R.string.success);
		} else {
			Utils.showToast(R.string.fail);
		}
	}

	@Override
	public void showAcceptResult(Boolean isSuccess, int memberId, String nameRemark) {
		if (isSuccess) {
			Intent intent = new Intent(getActivity(), ChatActivity.class);
			intent.putExtra("member_id", memberId);
			intent.putExtra("name", nameRemark);
			startActivity(intent);
		} else {
			Utils.showToast(R.string.fail);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		SearchFriendsInfo searchFriendsInfo = mAdapter.getData().get(position);
		if (searchFriendsInfo.getType() == TYPE_VIEW_1_TITLE) return;//标题
		int memberId = searchFriendsInfo.getId();
		Intent intent = new Intent(getActivity(), FriendProfileActivity.class);
		intent.putExtra("member_id", memberId);
		startActivity(intent);
	}

	private class CheckInputRunnable implements Runnable {
		@Override
		public void run() {
			long endTime = (System.currentTimeMillis()) - startTime;
			//根据延时前和延时后的时间对比判断在2000ms内是否有再次输入情况
			if (endTime >= delayTime) {
				String keyWord = mEtFriendsSearch.getText().toString().trim();
				if (!TextUtils.isEmpty(keyWord)) {
					mPresenter.searchKeyWord(keyWord);
				}
			} else {
				mHandler.removeCallbacks(this);
			}
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	@Override
	public void afterTextChanged(Editable s) {}
}
