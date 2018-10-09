package com.ace.member.main.home.receive_to_acct;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.keyboard.KeyboardUtil;
import com.ace.member.main.home.receive_to_acct.history.R2ARecentActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.Session;
import com.ace.member.view.VerificationCodeView;
import com.og.utils.FileUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class ReceiveToAcctActivity extends BaseActivity implements ReceiveToAcctContract.View {

	@Inject
	ReceiveToAcctPresenter mPresenter;

	@BindView(R.id.ll_root)
	LinearLayout mLlRoot;
	@BindView(R.id.sv_main)
	ScrollView mSvMain;
	@BindView(R.id.tv_r2a_phone)
	TextView mPhoneNumber;
	@BindView(R.id.et_r2a_security_code)
	VerificationCodeView mCodeGrid;
	@BindView(R.id.tv_r2a_content2)
	TextView mContentTitle;
	@BindView(R.id.v_line2)
	View mLine2;

	public String mSecurityCode = "";
	private KeyboardUtil mKeyboardUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerReceiveToAcctComponent.builder()
			.receiveToAcctPresenterModule(new ReceiveToAcctPresenterModule(this, this))
			.build()
			.inject(this);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		initView();
		initKeyboard();
		initListener();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_receive_to_acct;
	}

	private void initView() {
		ToolBarConfig.builder(this, null).setIvMenuRes(R.drawable.ic_history).setEnableMenu(true).setTvTitleRes(R.string.receive_to_acct).build();
		mPhoneNumber.setText(Session.user.getPhone());
		mPresenter.start();
	}

	private void initKeyboard() {
		//初始化数字键盘
		mKeyboardUtil = new KeyboardUtil(this, mLlRoot, mSvMain);
		//VerificationCodeView设置数字键盘
		mCodeGrid.setKeyboardUtil(mKeyboardUtil);
	}

	private void initListener() {
		mCodeGrid.setOnCompleteListener(new VerificationCodeView.Listener() {
			@Override
			public void onComplete(String content) {
				mSecurityCode = content;
				mPresenter.chkSecurityCode(mSecurityCode);
			}
		});
	}

	@OnClick({R.id.ll_toolbar_menu})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.ll_toolbar_menu:
				Intent intent = new Intent(this, R2ARecentActivity.class);
				startActivity(intent);
				break;
		}
	}

	@Override
	public void invalidCode() {
		mSecurityCode = "";
		Animation shakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake_x);
		mCodeGrid.clear(true);
		mCodeGrid.startAnimation(shakeAnim);
	}

	@Override
	public void notRunningFunction() {
		mCodeGrid.setVisibility(View.GONE);
		mContentTitle.setVisibility(View.GONE);
		mLine2.setVisibility(View.GONE);
		mKeyboardUtil.hideKeyboardLayout();
	}

	@Override
	public void showSuccess(R2ADataBeen data) {
		try {
			mCodeGrid.clear(false);
			mSecurityCode = "";
			Intent it = new Intent(ReceiveToAcctActivity.this, R2AResultActivity.class);
			it.putExtra("currency", data.getCurrency());
			it.putExtra("sender", data.getSender());
			it.putExtra("amount", data.getAmount());
			it.putExtra("time", data.getTime());
			startActivity(it);
			finish();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

}
