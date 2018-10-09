package com.ace.member.main.friends.chat.friend_profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.FriendProfileInfo;
import com.ace.member.main.friends.chat.ChatActivity;
import com.ace.member.main.home.transfer.to_member.ToMemberActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.FastBlurUtil;
import com.ace.member.utils.GlideUtil;
import com.ace.member.utils.Session;
import com.ace.member.view.RoundRectImageView;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.og.utils.Utils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class FriendProfileActivity extends BaseActivity implements FriendProfileContract.FriendProfileView, TextView.OnEditorActionListener {
	private static final int STATUS_0_REQUEST_ADD = 0;
	private static final int STATUS_1_REQUEST_PENDING = 1;
	private static final int STATUS_2_REQUEST_ACCEPTED = 2;
	private static final int STATUS_4_REQUEST_REJECTED = 4;

	private static final int TYPE_ADD_1_SCAN_QR_CODE = 1;//扫描二维码
	private static final int TYPE_ADD_2_SCAN_MY_QR_CODE = 2;//被扫描二维码
	private static final int TYPE_ADD_3_SEARCH_VIA_PHONE = 3;//查找手机号码方式
	private static final int TYPE_ADD_4_SEARCH_MY_PHONE = 4;//被查找手机号码方式
	private static final int TAG_1_EDIT = 1;
	private static final int TAG_2_CANCEL = 2;
	@Inject
	FriendProfilePresenter mPresenter;

	@BindView(R.id.aiv_friend_profile_bg)
	AppCompatImageView mAivFriendProfileBg;
	@BindView(R.id.aiv_friend_profile_head)
	RoundRectImageView mAivFriendProfileHead;
	@BindView(R.id.btn_friend_profile_transfer)
	Button mBtnFiendProfileTransfer;
	@BindView(R.id.btn_friend_profile_other)
	Button mBtnFiendProfileOther;
	@BindView(R.id.tv_friend_profile_real_name)
	TextView mTvFiendProfileRealName;
	@BindView(R.id.tv_friend_profile_area)
	TextView mTvFiendProfileArea;
	@BindView(R.id.tv_friend_profile_phone)
	TextView mTvFiendProfilePhone;
	@BindView(R.id.tv_friend_profile_add_type)
	TextView mTvFriendProfileAddType;
	@BindView(R.id.ll_friend_profile_add_type)
	LinearLayout mLlFriendProfileAddType;
	@BindView(R.id.ll_friend_profile_button)
	LinearLayout mLlFriendProfileButton;
	@BindView(R.id.et_friend_profile_name_remark)
	EditText mEtNameRemark;
	@BindView(R.id.iv_et_friend_profile_name_remark)
	AppCompatImageView mIvNameRemark;
	private FriendProfileInfo mFriendProfileInfo;
	private String mNameRemark;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActivity();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_friend_profile;
	}

	@Override
	protected void initActivity() {
		DaggerFriendProfileComponent.builder().friendProfileModule(new FriendProfileModule(this, this)).build().inject(this);
		ToolBarConfig.builder(this, null).setIvMenuRes(R.drawable.ic_friends_contact_setting).setEnableMenu(true).setTvTitleRes(R.string.profile).setBackgroundDrawableRes(R.drawable.bg_gradient_black).setBackListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		}).build();

		//		StatusBarCompat.compatFullScreen(this);//设置透明状态栏
	}

	@Override
	protected void onResume() {
		super.onResume();
		Intent intent = getIntent();
		int memberId = intent.getIntExtra("member_id", 0);
		mPresenter.getFriendProfileInfo(memberId);
	}

	@Override
	public void showFriendProfile(FriendProfileInfo friendProfileInfo) {
		mFriendProfileInfo = friendProfileInfo;
		String portrait = friendProfileInfo.getPortrait();
		GlideUtil.loadNormalPortrait(mContext, portrait, mAivFriendProfileHead);
		GlideUtil.loadBitmap(mContext, portrait, R.drawable.head_portrait_1, new SimpleTarget<Bitmap>() {
			@Override
			public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
				mAivFriendProfileBg.setImageBitmap(FastBlurUtil.blurBitmap(mContext, resource));//高斯模糊背景
			}
		});

		String name = friendProfileInfo.getName();
		String phone = friendProfileInfo.getPhone();
		int countryCode = friendProfileInfo.getCountryCode();
		int status = friendProfileInfo.getStatus();
		int requestId = friendProfileInfo.getMemberId();//申请发起人
		mNameRemark = friendProfileInfo.getNameRemark();
		mNameRemark = mNameRemark == null ? name : mNameRemark;

		mEtNameRemark.setText(mNameRemark);
		mTvFiendProfileRealName.setText(name);
		mTvFiendProfilePhone.setText(phone);
		mTvFiendProfileArea.setText(mPresenter.getCountryName(countryCode));

		if (friendProfileInfo.isApplication()) {//有申请记录
			if (requestId == Session.user.getId()) {//申请人是自己
				switch (status) {
					case STATUS_1_REQUEST_PENDING:
						mBtnFiendProfileOther.setEnabled(false);
						mBtnFiendProfileOther.setText(R.string.pending);
						break;
					case STATUS_2_REQUEST_ACCEPTED:
						mBtnFiendProfileOther.setEnabled(true);
						mBtnFiendProfileOther.setText(R.string.send_message);
						break;
					case STATUS_4_REQUEST_REJECTED:
						mBtnFiendProfileOther.setEnabled(true);
						mBtnFiendProfileOther.setText(R.string.add);//被拒绝了，还可以再申请，新插入一条
						break;
				}
			} else {//申请人是对方
				switch (status) {
					case STATUS_1_REQUEST_PENDING:
						mBtnFiendProfileOther.setEnabled(true);
						mBtnFiendProfileOther.setText(R.string.ignore);
						break;
					case STATUS_2_REQUEST_ACCEPTED:
						mBtnFiendProfileOther.setEnabled(true);
						mBtnFiendProfileOther.setText(R.string.send_message);
						break;
					case STATUS_4_REQUEST_REJECTED:
						mBtnFiendProfileOther.setEnabled(false);
						mBtnFiendProfileOther.setText(R.string.rejected);
						break;
				}
			}

			if (status == STATUS_2_REQUEST_ACCEPTED) {//已经是好友关系，可备注名字
				int typeAdd = friendProfileInfo.getTypeAdd();
				String typeAddStr = "";
				switch (typeAdd) {
					case TYPE_ADD_1_SCAN_QR_CODE:
						typeAddStr = Utils.getString(R.string.scan_my_qr_code);
						break;
					case TYPE_ADD_2_SCAN_MY_QR_CODE:
						typeAddStr = Utils.getString(R.string.scan_qr_code);
						break;
					case TYPE_ADD_3_SEARCH_VIA_PHONE:
						typeAddStr = Utils.getString(R.string.search_my_phone);
						break;
					case TYPE_ADD_4_SEARCH_MY_PHONE:
						typeAddStr = Utils.getString(R.string.search_via_phone);
						break;
				}
				mLlFriendProfileAddType.setVisibility(View.VISIBLE);
				mTvFriendProfileAddType.setText(typeAddStr);
				mIvNameRemark.setVisibility(View.VISIBLE);

				mIvNameRemark.setTag(TAG_1_EDIT);
				mEtNameRemark.setOnEditorActionListener(this);

			} else {
				mLlFriendProfileAddType.setVisibility(View.GONE);
			}

		} else {
			if (friendProfileInfo.getId() == Session.user.getId()) {//自己
				mLlFriendProfileButton.setVisibility(View.GONE);
			} else {
				mBtnFiendProfileOther.setEnabled(true);
				mBtnFiendProfileOther.setText(R.string.add);
			}
		}
	}

	@Override
	public void showBtnResult(Boolean isSuccess) {
		int status = (Integer) mBtnFiendProfileOther.getTag();
		if (isSuccess) {
			if (status == STATUS_0_REQUEST_ADD || status == STATUS_2_REQUEST_ACCEPTED) {
				mBtnFiendProfileOther.setText(R.string.request_sent);
			} else if (status == STATUS_1_REQUEST_PENDING) {
				mBtnFiendProfileOther.setText(R.string.rejected);
			}
			Utils.showToast(R.string.success);
		} else {
			mBtnFiendProfileOther.setEnabled(true);
			mBtnFiendProfileOther.setTextColor(Utils.getColor(R.color.colorPrimary));
			Utils.showToast(R.string.fail);
		}
	}

	@Override
	public void showRemarkName(Boolean isSuccess, String nameRemark) {
		if (isSuccess) {
			mNameRemark = nameRemark;
			toggleEditName(false);
			Utils.showToast(R.string.success);
		} else {
			Utils.showToast(R.string.fail);
		}
	}

	@OnClick({R.id.btn_friend_profile_transfer, R.id.btn_friend_profile_other, R.id.iv_et_friend_profile_name_remark})
	public void onClickView(View view) {
		if (mFriendProfileInfo != null) {
			switch (view.getId()) {
				case R.id.btn_friend_profile_transfer:
					Intent intentTransfer = new Intent(FriendProfileActivity.this, ToMemberActivity.class);
					intentTransfer.putExtra("phone", mFriendProfileInfo.getPhone());
					startActivity(intentTransfer);
					break;
				case R.id.btn_friend_profile_other:
					int status = mFriendProfileInfo.getStatus();
					int id = mFriendProfileInfo.getId();
					int requestId = mFriendProfileInfo.getMemberId();//申请发起人
					String name = mFriendProfileInfo.getName();
					String nameRemark = mFriendProfileInfo.getNameRemark();
					nameRemark = (nameRemark == null ? name : nameRemark);

					if (mFriendProfileInfo.isApplication()) {//有申请记录
						if (requestId == Session.user.getId()) {//申请人是自己
							switch (status) {
								case STATUS_4_REQUEST_REJECTED:
									mPresenter.addApplication(id, "", TYPE_ADD_3_SEARCH_VIA_PHONE);
									mBtnFiendProfileOther.setEnabled(false);
									mBtnFiendProfileOther.setTextColor(Utils.getColor(R.color.clr_chat_tv_hint));
									break;
								case STATUS_2_REQUEST_ACCEPTED:
									Intent intentOther = new Intent(FriendProfileActivity.this, ChatActivity.class);
									intentOther.putExtra("member_id", mFriendProfileInfo.getId());
									intentOther.putExtra("name", nameRemark);
									startActivity(intentOther);
									break;
							}
						} else {
							switch (status) {
								case STATUS_1_REQUEST_PENDING:
									mPresenter.rejectApplication(id);
									mBtnFiendProfileOther.setEnabled(false);
									mBtnFiendProfileOther.setTextColor(Utils.getColor(R.color.clr_chat_tv_hint));
									break;
								case STATUS_2_REQUEST_ACCEPTED:
									Intent intentOther = new Intent(FriendProfileActivity.this, ChatActivity.class);
									intentOther.putExtra("member_id", mFriendProfileInfo.getId());
									intentOther.putExtra("name", nameRemark);
									startActivity(intentOther);
									break;
							}
						}
					} else {//没有申请记录
						mPresenter.addApplication(id, "", TYPE_ADD_3_SEARCH_VIA_PHONE);
						mBtnFiendProfileOther.setEnabled(false);
						mBtnFiendProfileOther.setTextColor(Utils.getColor(R.color.clr_chat_tv_hint));
					}
					mBtnFiendProfileOther.setTag(status);
					break;
				case R.id.iv_et_friend_profile_name_remark:
					if ((Integer) mIvNameRemark.getTag() == TAG_1_EDIT) {//可进入编辑模式
						toggleEditName(true);
					} else {
						toggleEditName(false);
					}
					break;
			}
		}
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_DONE) {
			String inputStr = mEtNameRemark.getText().toString();
			if (mFriendProfileInfo != null) {
				if (!inputStr.equals(mNameRemark)) {
					mPresenter.remarkName(mFriendProfileInfo.getId(), inputStr);
				} else {
					toggleEditName(false);
				}
				return true;
			}
		}
		return false;
	}

	private void toggleEditName(boolean isEdit) {
		if (mFriendProfileInfo != null) {
			if (isEdit) {
				mEtNameRemark.setFocusable(true);
				mEtNameRemark.setFocusableInTouchMode(true);
				mEtNameRemark.requestFocus();

				AppUtils.enableSoftInput(mEtNameRemark, true);

				mIvNameRemark.setImageResource(R.drawable.ic_close);
				mIvNameRemark.setTag(TAG_2_CANCEL);
			} else {
				mEtNameRemark.clearFocus();
				mEtNameRemark.setFocusable(false);
				mEtNameRemark.setFocusableInTouchMode(false);
				mEtNameRemark.setText(mNameRemark);//恢复原来的名字

				AppUtils.enableSoftInput(mEtNameRemark, false);

				mIvNameRemark.setImageResource(R.drawable.ic_friends_edit);
				mIvNameRemark.setTag(TAG_1_EDIT);
			}
		}
	}
}