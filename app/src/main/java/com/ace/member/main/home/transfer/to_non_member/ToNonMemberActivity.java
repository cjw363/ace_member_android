package com.ace.member.main.home.transfer.to_non_member;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseCountryCodeActivity;
import com.ace.member.bean.Balance;
import com.ace.member.bean.Currency;
import com.ace.member.main.bottom_dialog.BottomDialog;
import com.ace.member.main.home.transfer.TransferResultActivity;
import com.ace.member.main.home.transfer.recent.TransferRecentActivity;
import com.ace.member.simple_listener.SimpleTextWatcher;
import com.ace.member.simple_listener.SimpleViewClickListener;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.Session;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class ToNonMemberActivity extends BaseCountryCodeActivity implements ToNonMemberContract.View, View.OnClickListener {

	@Inject
	ToNonMemberPresenter mToNonMemberPresenter;

	@BindView(R.id.tv_sender)
	TextView mTvSender;
	@BindView(R.id.et_receiver)
	EditText mEtReceiver;
	@BindView(R.id.tv_currency)
	TextView mTvCurrency;
	@BindView(R.id.ll_currency)
	LinearLayout mLLCurrency;
	@BindView(R.id.ll_amount)
	LinearLayout mLlAmount;
	@BindView(R.id.et_amount)
	EditText mEtAmount;
	@BindView(R.id.tv_balance)
	TextView mTvBalance;
	@BindView(R.id.tv_fee)
	TextView mTvFee;
	@BindView(R.id.et_remark)
	EditText mEtRemark;
	@BindView(R.id.tv_receiver_country_code)
	TextView mTvReceiverCountryCode;
	@BindView(R.id.ll_receiver)
	LinearLayout mLlReceiver;
	@BindView(R.id.ll_receiver_country_code)
	LinearLayout mLlReceiverCountryCode;
	@BindView(R.id.tv_receiver_hint)
	TextView mTvReceiverHint;
	@BindView(R.id.img_receiver_delete)
	ImageView mImgReceiverDelete;
	@BindView(R.id.tv_receiver)
	TextView mTvReceiver;
	@BindView(R.id.fl_receiver)
	FrameLayout mFlReceiver;
	@BindView(R.id.tv_fee_title)
	TextView mTvFeeTitle;
	@BindView(R.id.tv_balance_title)
	TextView mTvBalanceTitle;
	@BindView(R.id.btn_submit)
	Button mBtnSubmit;
	@BindView(R.id.ll_toolbar_menu)
	LinearLayout mLlToolbarMenu;
	@BindView(R.id.iv_menu)
	AppCompatImageView mIvMenu;

	private ArrayList<String> mCurrencyList;

	private String mCurrency = "", mReceiver = "", mReceiverCountryCode = String.valueOf(AppGlobal.COUNTRY_CODE_855_CAMBODIA);//默认

	public static final int SOURCE_2_NON_MEMBER = 2;
	private SimpleTextWatcher.MoneyTextWatcher mMoneyTextWatcher;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerToNonMemberComponent.builder()
			.toNonMemberPresenterModule(new ToNonMemberPresenterModule(this, this))
			.build()
			.inject(this);

		initData();
		initActivity();
		setListener();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_to_non_member;
	}

	private void initData() {
		mReceiver = getIntent().getStringExtra("phone");
		if (!mReceiver.equals("")) {
			mFlReceiver.setVisibility(View.GONE);
			mTvReceiver.setVisibility(View.VISIBLE);
			mTvReceiver.setText(mReceiver);
		}
	}

	protected void initActivity() {
		setCurrencyList();
		ToolBarConfig.builder(this, null)
			.setTvTitleRes(R.string.to_non_member)
			.setIvMenuRes(R.drawable.ic_history)
			.setEnableMenu(true)
			.build();
		try {
			String phone = Session.user.getPhone();
			String tvSender = phone.substring(phone.indexOf("+"), phone.indexOf("-")) + "" + phone.substring(phone.indexOf("-"));
			mTvSender.setText(tvSender);
			mToNonMemberPresenter.start(true);
			mToNonMemberPresenter.getBalance();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	private void setListener() {
		try {
			mLlReceiverCountryCode.setOnClickListener(this);
			mLLCurrency.setOnClickListener(this);
			mTvReceiverHint.setOnClickListener(this);
			mImgReceiverDelete.setOnClickListener(this);
			mBtnSubmit.setOnClickListener(this);
			mLlToolbarMenu.setOnClickListener(this);

			mMoneyTextWatcher = new SimpleTextWatcher.MoneyTextWatcher(mEtAmount) {
				@Override
				public void afterTextChanged(Editable editable) {
					mToNonMemberPresenter.updateFeeInfo();
				}
			};
			mEtAmount.addTextChangedListener(mMoneyTextWatcher);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ll_toolbar_menu:
				Intent it;
				it = new Intent(mContext, TransferRecentActivity.class);
				it.putExtra("source", SOURCE_2_NON_MEMBER);
				startActivity(it);
				break;
			case R.id.ll_receiver_country_code:
				showCountryCode(mTvReceiverCountryCode);
				break;
			case R.id.ll_currency:
				showCurrency();
				break;
			case R.id.img_receiver_delete:
				mEtReceiver.setText("");
				break;
			case R.id.btn_submit:
				String receiver = "";
				if (mReceiver.isEmpty()) {
					receiver = mEtReceiver.getText().toString().trim();
				} else {
					mReceiverCountryCode = mReceiver.substring(1, mReceiver.indexOf("-"));
					receiver = mReceiver.substring(mReceiver.indexOf("-") + 1);
				}
				mToNonMemberPresenter.submit(mReceiverCountryCode, receiver, mEtRemark.getText().toString().trim());
				break;
			case R.id.tv_receiver_hint:
				mImgReceiverDelete.setVisibility(View.VISIBLE);
				mLlReceiver.setVisibility(View.VISIBLE);
				mTvReceiverHint.setVisibility(View.GONE);
				mEtReceiver.setHint("");
				mEtReceiver.requestFocus();
				break;
		}
	}

	private void showCurrency() {
		try {
			if (mCurrencyList == null) return;
			final CharSequence[] tvContent = new CharSequence[mCurrencyList.size()];
			for (int i = 0; i < mCurrencyList.size(); i++) {
				tvContent[i] = mCurrencyList.get(i);
			}
			BottomDialog.Builder builder = new BottomDialog.Builder(this).setTvTitle(Utils.getString(R.string.currency_type))
				.setTvContent2(tvContent)
				.setClickListener(new SimpleViewClickListener() {
					@Override
					public void onClick(View view, int position) {
						mCurrency = tvContent[position].toString();
						mTvCurrency.setText(tvContent[position]);
						mTvCurrency.setTextColor(Utils.getColor(R.color.black));
						mToNonMemberPresenter.updateFeeInfo();
						showBalance(mCurrency);
						mMoneyTextWatcher.setCurrency(mEtAmount, mCurrency);
						mEtAmount.setText(mEtAmount.getText());
						mEtAmount.setSelection(mEtAmount.getText().toString().length());
					}
				});
			builder.createAndShow();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
	}

	private void setCurrencyList() {
		try {
			if (mCurrencyList == null && Session.currencyList != null) {
				mCurrencyList = new ArrayList<>();
				int len = Session.currencyList.size();
				for (int i = 0; i < len; i++) {
					try {
						Currency obj = Session.currencyList.get(i);
						mCurrencyList.add(obj.getCurrency());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	private void showBalance(String currency) {
		try {
			final List<Balance> balanceList = Session.balanceList;
			for (int i = 0, n = Session.balanceList.size(); i < n; i++) {
				if (Session.balanceList.get(i).getCurrency().equals(currency)) {
					mTvBalance.setText(Utils.format(balanceList.get(i).getAmount(), 2));
					break;
				} else {
					mTvBalance.setText(getString(R.string.zero_with_decimal));
				}
			}
			if (!"0.00".equals(mTvBalance.getText().toString())) {
				mTvBalanceTitle.setTextColor(Utils.getColor(R.color.clr_common_title));
				mTvBalance.setTextColor(Utils.getColor(R.color.clr_common_content));
			} else {
				mTvBalanceTitle.setTextColor(Utils.getColor(R.color.clr_common_content_hint));
				mTvBalance.setTextColor(Utils.getColor(R.color.clr_common_content_hint));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getAmount() {
		return mEtAmount.getText().toString().trim().replace(",", "");
	}

	@Override
	public String getBalance() {
		return mTvBalance.getText().toString().trim().replace(",", "");
	}

	@Override
	public String getFee() {
		return mTvFee.getText().toString().trim().replace(",", "");
	}

	public void setCountryCode(String curValue, TextView textView) {
		try {
			mReceiverCountryCode = curValue.replace("+", "").trim();
			mTvReceiverCountryCode.setText(curValue);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@Override
	public void setFee(double fee) {
		try {
			mTvFee.setText(AppUtils.simplifyAmount(mCurrency, String.valueOf(fee)));
			mTvFeeTitle.setEnabled(fee >= 0 && !Utils.checkEmptyAmount(mEtAmount.getText().toString()));
			mTvFee.setEnabled(fee >= 0 && !Utils.checkEmptyAmount(mEtAmount.getText().toString()));
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@Override
	public String getCurrency() {
		return mCurrency;
	}

	@Override
	public void clearInfo() {
		mLlAmount.setFocusable(true);
		mLlAmount.setFocusableInTouchMode(true);
		mLlAmount.requestFocus();
		mEtAmount.setText("");
		mEtReceiver.setText("");
		mReceiverCountryCode = String.valueOf(AppGlobal.COUNTRY_CODE_855_CAMBODIA);
		mTvReceiverCountryCode.setText("+ " + mReceiverCountryCode);

		mTvCurrency.setText(Utils.getString(R.string.select));
		mCurrency = "";
		mTvCurrency.setTextColor(Utils.getColor(R.color.clr_common_content_hint));
		mTvBalanceTitle.setTextColor(Utils.getColor(R.color.clr_common_content_hint));

		mImgReceiverDelete.setVisibility(View.GONE);
		mLlReceiver.setVisibility(View.GONE);
		mEtReceiver.setVisibility(View.VISIBLE);
		mLlReceiverCountryCode.setVisibility(View.VISIBLE);
		mTvReceiverHint.setVisibility(View.VISIBLE);
		mTvReceiverHint.setText(R.string.phone_number);
		mTvReceiverHint.setTextColor(Utils.getColor(R.color.clr_common_content_hint));
		mEtRemark.setText("");
		mTvBalance.setText("");
		mTvFee.setText("");
	}

	@Override
	public void showSuccess(JSONObject object) {
		Intent it = new Intent(this, TransferResultActivity.class);
		it.putExtra("accept_code", object.optString("accept_code"));
		it.putExtra("currency", object.optString("currency"));
		it.putExtra("phone", object.optString("target_phone"));
		it.putExtra("amount", object.optString("amount"));
		it.putExtra("fee", object.optString("fee"));
		it.putExtra("time", object.optString("time"));
		startActivity(it);
	}

	@Override
	public AppCompatActivity getActivity() {
		return this;
	}

	@Override
	protected void onResume() {
		super.onResume();
		mToNonMemberPresenter.getBalance();
	}

	@Override
	public void setSubmitEnables(boolean flag) {
		mBtnSubmit.setEnabled(flag);
	}
}
