package com.ace.member.sms_notification.first_step;


import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseCountryCodeFragment;
import com.ace.member.keyboard.KeyboardTouchListener;
import com.ace.member.keyboard.KeyboardUtil;
import com.ace.member.keyboard.SubmitBtnListener;
import com.ace.member.listener.NoDoubleClickListener;
import com.ace.member.simple_listener.SimpleTextWatcher;
import com.ace.member.sms_notification.SMSNotificationActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.Session;
import com.ace.member.utils.SnackBarUtil;
import com.og.utils.ItemObject;
import com.og.utils.Utils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FirstStepFragment extends BaseCountryCodeFragment implements FirstStepContract.FirstStepView {

	@Inject
	FirstStepPresenter mPresenter;

	@BindView(R.id.ll_root)
	LinearLayout mLlRoot;
	@BindView(R.id.sv_main)
	ScrollView mSvMain;
	@BindView(R.id.ll_country_code)
	LinearLayout mLlCountryCode;
	@BindView(R.id.tv_country_code)
	TextView mTvCountryCode;
	@BindView(R.id.et_phone)
	EditText mEtPhone;
	@BindView(R.id.iv_agree)
	ImageView mIvAgree;
	@BindView(R.id.btn_next)
	Button mBtnNext;
	@BindView(R.id.ll_agreement)
	LinearLayout mLlAgreement;
	@BindView(R.id.rl_delete)
	RelativeLayout mRlDelete;
	@BindView(R.id.tv_agreement)
	TextView mTvAgreement;

	private String mCountryCode = String.valueOf(AppGlobal.COUNTRY_CODE_855_CAMBODIA);
	private String mPhone;

	private SMSNotificationActivity mActivity;

	private boolean isAgreed = true;
	private boolean isPhone = false;
	private KeyboardUtil mKeyboardUtil;

	@Override
	protected int getContentViewLayout() {
		return R.layout.fragment_notification_first_step;
	}

	@Override
	protected void initView() {
		mActivity = (SMSNotificationActivity) getActivity();
		DaggerFirstStepComponent.builder()
			.firstStepPresenterModule(new FirstStepPresenterModule(this, getContext()))
			.build()
			.inject(this);
		//noinspection ConstantConditions
		ButterKnife.bind(this, getView());

		ToolBarConfig.Builder builder = new ToolBarConfig.Builder(null, getView());
		builder.setTvTitleRes(R.string.verify_phone_number)
			.setEnableBack(true)
			.setBackListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mActivity.onBackPressed();
				}
			})
			.build();

		//一开始进入页面如果没有填写phone不能点击下一步
		if (TextUtils.isEmpty(mEtPhone.getText())) {
			mBtnNext.setEnabled(false);
		}
		if (mActivity.mActionType == SMSNotificationActivity.ACTION_TYPE_2_FORGOT_PASSWORD) {
			mLlAgreement.setVisibility(View.GONE);
			if (!TextUtils.isEmpty(Session.verificationPhone)) {
				setPhone(Session.verificationCountryCode, Session.verificationPhone);
			}
		} else {
			String agreement = getResources().getString(R.string.register_agreement);
			Spanned spanned;
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
				spanned = Html.fromHtml(agreement, Html.FROM_HTML_MODE_LEGACY);
			} else {
				spanned = Html.fromHtml(agreement);
			}
			mTvAgreement.setText(spanned);

		}
		mIvAgree.setSelected(isAgreed);
		initKeyboard();
		initListener();
		getData();
	}

	private void setPhone(String code, String phone) {
		setCountryCode(code, null);
		mEtPhone.setText(phone);
		mEtPhone.setSelection(phone.length());
		mBtnNext.setEnabled(true);
	}

	private void initKeyboard() {
		//初始化数字键盘
		mKeyboardUtil = new KeyboardUtil(getContext(), mLlRoot, mSvMain);
		//绑定数字键盘
		mEtPhone.setOnTouchListener(new KeyboardTouchListener(mKeyboardUtil, KeyboardUtil.INPUT_TYPE_1_NUM, -1));
		//绑定提交按钮
		mKeyboardUtil.bindSubmitBtnListener(new SubmitBtnListener(mBtnNext));
	}

	private void initListener() {
		mEtPhone.addTextChangedListener(new SimpleTextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//允许开头录入0
				formatPhone(mEtPhone, s.toString());

				if (s.length() > 5) {
					isPhone = true;
					mBtnNext.setEnabled(isAgreed);
					mKeyboardUtil.setIvSubmitEnabled(isAgreed);
				} else {
					mBtnNext.setEnabled(false);
					mKeyboardUtil.setIvSubmitEnabled(false);
					isPhone = false;
				}

				if (s.length() > 0) {
					mRlDelete.setVisibility(View.VISIBLE);
				} else {
					mRlDelete.setVisibility(View.GONE);
				}
			}
		});

		mBtnNext.setOnClickListener(new NoDoubleClickListener() {
			@Override
			protected void onNoDoubleClick(View v) {
				mPresenter.checkPhone("+" + mCountryCode, mEtPhone.getText()
					.toString()
					.replaceFirst("^0?", ""), mActivity.mSMSType);
				Utils.hideKeyboard(mBtnNext);
			}
		});
	}

	public void getData() {
		mPresenter.getConfig(mActivity.mActionType);
	}

	@Override
	public void saveVerification() {
		Session.verificationCountryCode = "+" + mCountryCode;
		Session.verificationPhone = mEtPhone.getText().toString().replaceFirst("^0?", "");
	}

	@Override
	public void showToast(String msg) {
		SnackBarUtil.show(mLlRoot, msg);
	}

	@Override
	public void toLogin(String msg) {
		mActivity.toLogin(msg);
	}

	@Override
	public void initCountryCodeList(List<ItemObject> list) {
		Session.countryCodeList = list;
		String curValue = "+ " + mCountryCode;
		mTvCountryCode.setText(curValue);
	}

	public void setCountryCode(String curValue, TextView textView) {
		mCountryCode = curValue.replace("+", "").trim();
		mTvCountryCode.setText(curValue);
	}

	@Override
	public void toNextStep() {
		mActivity.toNextStep();
	}

	@Override
	public void setResendTime(int time) {
		mActivity.setRendTime(time);
	}

	@OnClick({R.id.ll_country_code, R.id.iv_agree, R.id.rl_delete})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.ll_country_code:
				showCountryCode(mTvCountryCode);
				break;
			case R.id.iv_agree:
				isAgreed = !isAgreed;
				view.setSelected(isAgreed);
				mBtnNext.setEnabled(isAgreed && isPhone);
				break;
			case R.id.rl_delete:
				mEtPhone.setText("");
				break;
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (mKeyboardUtil.mIsShow) {
				mKeyboardUtil.hideAllKeyBoard();
				return true;
			}
		}
		return false;
	}
}
