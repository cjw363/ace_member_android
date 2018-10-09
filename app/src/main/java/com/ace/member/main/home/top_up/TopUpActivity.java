package com.ace.member.main.home.top_up;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.adapter.RVFaceValueAdapter;
import com.ace.member.base.BaseCountryCodeActivity;
import com.ace.member.bean.FaceValue;
import com.ace.member.bean.PhoneCompany;
import com.ace.member.event.SelectPhoneCompanyEvent;
import com.ace.member.main.home.top_up.order_detail.TopUpOrderDetailActivity;
import com.ace.member.main.home.top_up.phone_company.PhoneCompanyFragment;
import com.ace.member.main.home.top_up.recent_order.RecentOrderActivity;
import com.ace.member.simple_listener.SimpleTextWatcher;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.M;
import com.ace.member.utils.PhoneCompanyUtil;
import com.ace.member.utils.Session;
import com.ace.member.view.SpaceItemDecorView;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class TopUpActivity extends BaseCountryCodeActivity implements TopUpContract.View, RadioGroup.OnCheckedChangeListener {
	@Inject
	TopUpPresenter mPresenter;

	@BindView(R.id.ll_phone_number)
	LinearLayout mLlPhoneNumber;
	@BindView(R.id.ll_country_code)
	LinearLayout mLlCountryCode;
	@BindView(R.id.tv_country_code)
	TextView mTvCountryCode;
	@BindView(R.id.tv_phone_number)
	TextView mTvPhoneNumber;
	@BindView(R.id.et_phone)
	EditText mEtPhone;
	@BindView(R.id.fl_clear_input)
	FrameLayout mFlClearInput;
	@BindView(R.id.rl_top_up_company)
	RelativeLayout mRlTopUpCompany;
	@BindView(R.id.tv_face_value)
	TextView mTvFaceValue;
	@BindView(R.id.iv_phone_company)
	AppCompatImageView mIvCompany;
	@BindView(R.id.tv_company_name)
	TextView mTvCompanyName;
	@BindView(R.id.tv_currency)
	TextView mTvCurrency;
	@BindView(R.id.rv_top_up)
	RecyclerView mRvTopUp;
	@BindView(R.id.tv_empty_face_value)
	TextView mTvEmptyFaceValue;
	@BindView(R.id.iv_touch)
	AppCompatImageView mIvTouch;
	@BindView(R.id.rg_top_up_way)
	RadioGroup mRgTopUpWay;
	@BindView(R.id.btn_submit)
	Button mBtnSubmit;
	private RVFaceValueAdapter mFaceValueAdapter;
	private SpaceItemDecorView mDecorView;
	private PhoneCompany mPhoneCompany;
	private boolean mIsFunctionPincode = true;
	private boolean mIsFUnctionSendSMS = true;
	private String mCountryCode;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerTopUpComponent.builder()
			.topUpPresenterModule(new TopUpPresenterModule(this, this))
			.build()
			.inject(this);
		init();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_top_up;
	}

	private void init() {
		ToolBarConfig.builder(this, null).setTvTitleRes(R.string.top_up)
			.setEnableMenu(true)
			.setMenuType(ToolBarConfig.MenuType.MENU_IMAGE)
			.setIvMenuRes(R.drawable.ic_top_up_recent)
			.build();
		String phone = Session.user.getPhone();
		mEtPhone.setText(phone.substring(phone.indexOf("-") + 1));
		mTvPhoneNumber.setFocusable(true);
		mTvPhoneNumber.setFocusableInTouchMode(true);

		mTvFaceValue.setText(String.format(Utils.getString(R.string.face_values), ""));

		mEtPhone.addTextChangedListener(new SimpleTextWatcher.PhoneTextWatcher(mEtPhone) {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				super.onTextChanged(s, start, before, count);
				mFlClearInput.setVisibility(TextUtils.isEmpty(mEtPhone.getText()
					.toString()
					.trim()) ? View.GONE : View.VISIBLE);
			}
		});
		mRgTopUpWay.setOnCheckedChangeListener(this);
		mRgTopUpWay.check(R.id.rb_show_pincode);
		mPresenter.start();
	}

	@Override
	public void enablePhoneNumber(boolean enable) {
		mLlPhoneNumber.setVisibility(enable ? View.VISIBLE : View.GONE);
		mBtnSubmit.setEnabled(true);
	}

	@Override
	public void enableEmptyTopUpCompany(boolean enable) {
		try {
			if (enable) {
				ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.5f, 1.0f, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
				scaleAnimation.setDuration(1000L);
				scaleAnimation.setRepeatMode(Animation.REVERSE);
				scaleAnimation.setRepeatCount(-1);
				mIvTouch.setVisibility(View.VISIBLE);
				mIvTouch.setAnimation(scaleAnimation);
				mIvTouch.startAnimation(scaleAnimation);
			} else {
				mIvTouch.clearAnimation();
				mIvTouch.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@Override
	public void enableEmptyFaceValue(boolean enable) {
		mRvTopUp.setVisibility(enable ? View.GONE : View.VISIBLE);
		mTvEmptyFaceValue.setVisibility(enable ? View.VISIBLE : View.GONE);
	}

	@Override
	public void setCurrentTopUpCompany(PhoneCompany company) {
		mPhoneCompany = company;
		if (company != null) {
			mIvCompany.setImageResource(PhoneCompanyUtil.getPhoneCompanyResourceByName(company.getName()));
			mTvCompanyName.setText(company.getName());
			mTvCurrency.setText(company.getCurrency());
			mTvFaceValue.setText(String.format(Utils.getString(R.string.face_values), "(" + company.getCurrency() + ")"));
		} else {
			mIvCompany.setImageDrawable(null);
			mTvCompanyName.setText("");
			mTvCurrency.setText("");
			mTvFaceValue.setText(String.format(Utils.getString(R.string.face_values), ""));
		}
	}

	@Override
	public void setFaceValueList(List<FaceValue> list) {
		try {
			if (mFaceValueAdapter == null) {
				mFaceValueAdapter = new RVFaceValueAdapter(this, mPhoneCompany, list);
				mRvTopUp.setLayoutManager(new GridLayoutManager(this, 3));
				mRvTopUp.setAdapter(mFaceValueAdapter);
				if (mDecorView == null) {
					mDecorView = new SpaceItemDecorView(Utils.getDimenDp(R.dimen.width10), Utils.getDimenDp(R.dimen.height10), false);
					mRvTopUp.addItemDecoration(mDecorView);
				}
			} else {
				mFaceValueAdapter.setFaceValueList(mPhoneCompany, list);
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@Subscribe
	public void onSelectPhoneCompanyEvent(SelectPhoneCompanyEvent event) {
		mPresenter.onSelectPhoneCompanyEvent(event);
	}

	@OnClick({R.id.ll_toolbar_menu, R.id.ll_country_code, R.id.fl_clear_input, R.id.rl_top_up_company, R.id.btn_submit})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.ll_toolbar_menu:
				Intent intent = new Intent(this, RecentOrderActivity.class);
				startActivity(intent);
				break;
			case R.id.ll_country_code:
				showCountryCode(mTvCountryCode);
				break;
			case R.id.fl_clear_input:
				mEtPhone.setText(null);
				break;
			case R.id.rl_top_up_company:
				showPhoneCompany();
				break;
			case R.id.btn_submit:
				mPresenter.topUpSubmit();
				break;
		}
	}

	private void showPhoneCompany() {
		mPresenter.getPhoneCompany();
	}

	@Override
	public String getPhone() {
		return mEtPhone.getText()
			.toString()
			.trim();
	}

	@Override
	public PhoneCompany getPhoneCompany() {
		return mPhoneCompany;
	}

	@Override
	public FaceValue getFaceValue() {
		return mFaceValueAdapter == null ? null : mFaceValueAdapter.getCurrentFaceValue();
	}

	@Override
	public String getCurrency() {
		return mTvCurrency.getText()
			.toString();
	}

	@Override
	public AppCompatActivity getActivity() {
		return this;
	}

	@Override
	public int getTopUpWay() {
		int topUpWay = 0;
		switch (mRgTopUpWay.getCheckedRadioButtonId()) {
			case R.id.rb_show_pincode:
				topUpWay = AppGlobal.TOP_UP_1_SHOW_PINCODE;
				break;
			case R.id.rb_directly_top_up:
				topUpWay = AppGlobal.TOP_UP_2_DIRECTLY_TOP_UP;
				break;
			case R.id.rb_send_sms:
				topUpWay = AppGlobal.TOP_UP_3_SEND_SMS;
				break;
		}
		return topUpWay;
	}

	@Override
	public String getCountryCode() {
		return mCountryCode;
	}

	@Override
	public void setCountryCode(String countryCode) {
		mCountryCode = countryCode;
		mTvCountryCode.setText(String.format(Utils.getString(R.string.country_codes), countryCode));
	}

	@Override
	public void setCountryCode(String curValue, TextView textView) {
		super.setCountryCode(curValue, textView);
		mCountryCode = curValue.replace("+ ", "");
	}

	@Override
	public void setPhoneCompanyList(List<PhoneCompany> list) {
		Bundle bundle = new Bundle();
		bundle.putSerializable("phone_company", (Serializable) list);

		PhoneCompanyFragment pf = new PhoneCompanyFragment();
		pf.setArguments(bundle);
		pf.show(getSupportFragmentManager(), "PhoneCompanyFragment");
	}

	@Override
	public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
		if (group == mRgTopUpWay) {
			switch (checkedId) {
				case R.id.rb_show_pincode:
					enablePhoneNumber(false);
					AppUtils.enableSoftInput(this, false);
					if (!mIsFunctionPincode) showFunctionPause(M.FunctionCode.FUNCTION_121_MEMBER_TOP_UP_SHOW_PIN_CODE);
					break;
				case R.id.rb_directly_top_up:
					enablePhoneNumber(true);
					break;
				case R.id.rb_send_sms:
					enablePhoneNumber(true);
					if (!mIsFUnctionSendSMS) showFunctionPause(M.FunctionCode.FUNCTION_122_MEMBER_TOP_UP_SEND_SMS);
					break;
			}
		}
	}

	@Override
	public void toOrderDetail(int orderId, boolean sms) {
		Intent intent = new Intent(TopUpActivity.this, TopUpOrderDetailActivity.class);
		intent.putExtra("id", orderId);
		intent.putExtra("showConfirm", true);
		startActivity(intent);
	}

	@Override
	public void setIsFunctionShowPincode(boolean isFunctionShowPincode) {
		mIsFunctionPincode = isFunctionShowPincode;
	}

	@Override
	public void setSubmitEnables(boolean flag) {
		mBtnSubmit.setEnabled(flag);
	}

	@Override
	public void setIsFunctionSendSMS(boolean isFunctionSendSMS) {
		mIsFUnctionSendSMS = isFunctionSendSMS;
	}

	@Override
	public void showFunctionPause(int functionCode) {
		Utils.showToast(AppUtils.getFunctionPauseMsg(functionCode), Snackbar.LENGTH_LONG);
	}
}
