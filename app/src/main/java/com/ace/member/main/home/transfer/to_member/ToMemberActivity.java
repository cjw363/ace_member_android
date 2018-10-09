package com.ace.member.main.home.transfer.to_member;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseCountryCodeActivity;
import com.ace.member.bean.Balance;
import com.ace.member.bean.Currency;
import com.ace.member.listener.IMyViewOnClickListener;
import com.ace.member.main.bottom_dialog.BottomDialog;
import com.ace.member.main.home.transfer.TransferResultActivity;
import com.ace.member.main.home.transfer.recent.TransferRecentActivity;
import com.ace.member.service.WebSocketService;
import com.ace.member.simple_listener.SimpleTextWatcher;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.BaseApplication;
import com.ace.member.utils.Session;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class ToMemberActivity extends BaseCountryCodeActivity implements ToMemberContract.View {

	@Inject
	ToMemberPresenter mToMemberPresenter;

	@BindView(R.id.tv_source_phone)
	TextView mTvSourcePhone;
	@BindView(R.id.et_phone_no)
	EditText mEtPhoneNo;
	@BindView(R.id.ll_currency)
	LinearLayout mLlCurrency;
	@BindView(R.id.tv_select)
	TextView mTvSelect;
	@BindView(R.id.tv_balance)
	TextView mTvBalance;
	@BindView(R.id.et_amount)
	EditText mEtAmount;
	@BindView(R.id.tv_fee)
	TextView mTvFee;
	@BindView(R.id.et_remark)
	EditText mEtRemark;
	@BindView(R.id.btn_submit)
	Button mBtnSubmit;
	@BindView(R.id.tv_po)
	AppCompatImageView mTvPo;
	@BindView(R.id.tv_bal)
	TextView mTvBalanceTitle;
	@BindView(R.id.ll_target_phone)
	LinearLayout mLlTargetPhone;
	@BindView(R.id.tv_target_phone_hint)
	TextView mTvTargetPhoneHint;
	@BindView(R.id.tv_target_country_code)
	TextView mTvTargetCountryCode;
	@BindView(R.id.ll_target_country_code)
	LinearLayout mLlTargetCountryCode;
	@BindView(R.id.tv_target_phone)
	TextView mTvTargetPhone;
	@BindView(R.id.fl_target_phone)
	FrameLayout mFlTargetPhone;
	@BindView(R.id.tv_fee_title)
	TextView mTvFeeTitle;
	@BindView(R.id.tv_line_4)
	View mVLine;
	@BindView(R.id.ll_receiver_name)
	LinearLayout mLlReceiverName;
	@BindView(R.id.tv_member_name)
	TextView mTvMemberName;
	@BindView(R.id.ll_toolbar_menu)
	LinearLayout mLlToolbarMenu;
	@BindView(R.id.iv_menu)
	AppCompatImageView mIvMenu;


	public Context context;
	private ArrayList<String> mCurrencyList;
	private String mCurrency = "", mTargetPhone = "", mTargetCountryCode = String.valueOf(AppGlobal.COUNTRY_CODE_855_CAMBODIA), sourcePhone = "";//默认
	private Boolean isNameSearch = false;
	public static final int SOURCE_1_MEMBER = 1;
	long startTime;
	private SimpleTextWatcher.MoneyTextWatcher mMoneyTextWatcher;
	private int status = 1;
	private static int SUMBIT_STATUS_1_ABLE = 1;
	private static int SUMBIT_STATUS_2_DISABLE = 2;
	private Intent mWebsocketServiceIntent;
	private int mMemberId;
	private boolean mIsFriend = false;
	public static final int RESULT_CANCEL = -1;
	public static final int RESULT_OK = 1;
	private boolean mIsReceiveMoney;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerToMemberComponent.builder()
			.toMemberPresenterModule(new ToMemberPresenterModule(this, this))
			.build()
			.inject(this);

		initData();
		initActivity();
		initSocket();
		setListener();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_to_member;
	}

	private void initData() {
		try {
			mTargetPhone = getIntent().getStringExtra("phone");
			mMemberId = getIntent().getIntExtra("member_id", 0);
			String currency = getIntent().getStringExtra("currency");
			double amount = getIntent().getDoubleExtra("amount", 0);
			mIsFriend = getIntent().getBooleanExtra("is_friend", false);
			mIsReceiveMoney = getIntent().getBooleanExtra("is_receive_money",false);
			if (!TextUtils.isEmpty(mTargetPhone)) {
				showTargetPhone(mTargetPhone);
			}
			if (!TextUtils.isEmpty(currency)) {
				mCurrency = currency;
				mTvSelect.setText(currency);
				mTvSelect.setTextColor(Utils.getColor(R.color.black));
				showBalance(mCurrency);
				mLlCurrency.setOnClickListener(null);
			}

			if (amount > 0) {
				mEtAmount.setText(Utils.format(amount, mCurrency));
				mEtAmount.setEnabled(false);
				mTvFee.setText(R.string.zero_with_decimal);
			}

			if (mMemberId > 0) {
				mToMemberPresenter.getMemberByID(mMemberId);
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}


	protected void initActivity() {
		try {
			setCurrencyList();
			ToolBarConfig.builder(this, null)
				.setTvTitleRes(R.string.to_member)
				.setIvMenuRes(R.drawable.ic_history)
				.setEnableMenu(true)
				.build();
			String phone = Session.user.getPhone();
			sourcePhone = phone.substring(phone.indexOf("+"), phone.indexOf("-")) + "" + phone.substring(phone.indexOf("-"));
			mTvSourcePhone.setText(sourcePhone);
			mTvFeeTitle.setTextColor(Utils.getColor(R.color.clr_tv_fee_normal));
			mToMemberPresenter.getInfoForTransferToMember();
			try {
				chkTradingPasswordStatus();
			} catch (Exception e) {
				FileUtils.addErrorLog(e);
				e.printStackTrace();
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	private void initSocket() {
		mWebsocketServiceIntent = new Intent(this, WebSocketService.class);
		Bundle b = new Bundle();
		b.putString("socket_host", Session.socketServers.getReceiveMoney());
		mWebsocketServiceIntent.putExtras(b);
		startService(mWebsocketServiceIntent);
	}

	private void setCurrencyList() {
		if (mCurrencyList == null && Session.currencyList != null) {
			try {
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
			} catch (Exception e) {
				FileUtils.addErrorLog(e);
			}
		}
	}

	public void chkTradingPasswordStatus() {
		if (Session.user.getStatusTradingPassword() == AppGlobal.TRADING_PASSWORD_STATUS_4_TO_SET) {
			Utils.showToast(R.string.please_set_trading_password);
			mBtnSubmit.setEnabled(false);
		}
	}

	private void setListener() {
		mMoneyTextWatcher = new SimpleTextWatcher.MoneyTextWatcher(mEtAmount) {
			@Override
			public void afterTextChanged(Editable editable) {
				try {
					if (!"".equals(mEtAmount.getText().toString())) {
						mTvFee.setTextColor(Utils.getColor(R.color.clr_tv_fee_select));
						mTvFeeTitle.setTextColor(Utils.getColor(R.color.clr_tv_fee_select));
						mTvFee.setText(R.string.zero_with_decimal);
					} else {
						mTvFee.setTextColor(Utils.getColor(R.color.clr_tv_fee_normal));
						mTvFeeTitle.setTextColor(Utils.getColor(R.color.clr_tv_fee_normal));
						mTvFee.setText("");
					}
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}
		};
		mEtAmount.addTextChangedListener(mMoneyTextWatcher);

		mEtPhoneNo.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			//延时三秒自动查找member;
			public void afterTextChanged(Editable editable) {
				isNameSearch = false;
				startTime = System.currentTimeMillis();
				BaseApplication.getHandler().postDelayed(new checkPhoneHandler(), 2000);
			}
		});
	}

	private class checkPhoneHandler implements Runnable {
		public void run() {
			long endTime = (System.currentTimeMillis()) - startTime;
			//根据延时前和延时后的时间对比判断在2000ms内是否有再次输入情况
			if (endTime >= 2000) {
				if (!isNameSearch) checkInputPhone();
			} else {
				BaseApplication.getHandler()
					.removeCallbacks(this);
			}
		}
	}

	public void checkInputPhone() {
		String phone = "+" + mTargetCountryCode + "-" + mEtPhoneNo.getText().toString().trim();
		if (TextUtils.isEmpty(mEtPhoneNo.getText().toString())) {
			Utils.showToast(R.string.please_input_phone);
			return;
		} else if (sourcePhone.equals(phone)) {
			Utils.showToast(R.string.msg_1722);
			return;
		}
		mToMemberPresenter.checkIsMember(phone);
	}

	@Override
	public void showName(String name) {
		isNameSearch = true;
		if (status == 1) {
			mBtnSubmit.setEnabled(true);
		}
		mLlReceiverName.setVisibility(View.VISIBLE);
		mTvMemberName.setText(name);
		mVLine.setVisibility(View.VISIBLE);
		//		mLlPhone.setVisibility(View.GONE);
		//		mTvPhoneHint.setVisibility(View.GONE);
		//		mEtPhone.setVisibility(View.GONE);
		//		mLlCountryCode.setVisibility(View.GONE);
		//		mTvPhone.setVisibility(View.VISIBLE);
		//		mTvPhone.setText(phone);
	}

	@Override
	public void showTargetPhone(String phone) {
		mTargetPhone = phone;
		mFlTargetPhone.setVisibility(View.GONE);
		mTvTargetPhone.setVisibility(View.VISIBLE);
		mTvTargetPhone.setText(phone);
		mToMemberPresenter.checkIsMember(mTargetPhone);
	}

	@Override
	public String getTargetMember() {
		return AppGlobal.USER_TYPE_1_MEMBER + "+" + mMemberId;
	}

	@Override
	public void setSendMessage(String msg) {
		try {
			WebSocketService.sendMsg(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void hideName() {
		mLlReceiverName.setVisibility(View.GONE);
		mVLine.setVisibility(View.GONE);
	}

	@Override
	public String getAmount() {
		return AppUtils.getAmountValue(getCurrency(), mEtAmount.getText()
			.toString()
			.trim());
	}

	public String getCurrency() {
		return mTvSelect.getText()
			.toString();
	}

	@Override
	public String getRemark() {
		return mEtRemark.getText()
			.toString()
			.trim();
	}

	@Override
	public String getBalance() {
		return mTvBalance.getText()
			.toString()
			.replaceAll("\\s*", "");
	}

	@Override
	public AppCompatActivity getActivity() {
		return this;
	}

	@Override
	public void clearInfo() {
		try {
			mEtAmount.setText("");
			mTvSelect.setText(Utils.getString(R.string.select));
			mCurrency = "";
			mTvSelect.setTextColor(Utils.getColor(R.color.clr_tv_hint));
			mTvBalanceTitle.setTextColor(Utils.getColor(R.color.clr_common_content_hint));

			mEtPhoneNo.setVisibility(View.VISIBLE);
			mLlTargetCountryCode.setVisibility(View.VISIBLE);
			mEtRemark.setText("");
			mTvBalance.setText("");
			mTvFee.setText("");
			mTvFeeTitle.setTextColor(Utils.getColor(R.color.clr_tv_fee_normal));
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}

	}

	@Override
	public void showSuccess(String time, int transferID) {
		try {
			if (!mIsFriend) {
				Intent it = new Intent(this, TransferResultActivity.class);
				it.putExtra("currency", mCurrency);
				if (!TextUtils.isEmpty(mTargetPhone)) {
					it.putExtra("phone", mTargetPhone);
				} else {
					it.putExtra("phone", mTvTargetCountryCode.getText()
						.toString() + "-" + mEtPhoneNo.getText()
						.toString());
				}
				it.putExtra("amount", getAmount());
				it.putExtra("fee", getResources().getString(R.string.zero_with_decimal));
				it.putExtra("time", time);
				it.putExtra("from", "from_to_member");
				startActivity(it);
				clearInfo();
				if(mIsReceiveMoney){
					finish();
				}
			} else {
				Intent intent = new Intent();
				intent.putExtra("transfer_id", transferID);
				setResult(RESULT_OK, intent);
				finish();
			}
		} catch (Resources.NotFoundException e) {
			FileUtils.addErrorLog(e);
		}
	}

	@OnClick(R.id.ll_currency)
	public void onViewClicked() {
		showCurrency();
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
				.setClickListener(new IMyViewOnClickListener() {
					@Override
					public void onClick(View view, int position) {
						mCurrency = tvContent[position].toString();
						mTvSelect.setText(tvContent[position]);
						mTvSelect.setTextColor(Utils.getColor(R.color.black));
						showBalance(mCurrency);
					}

					@Override
					public void onLongClick(View view, int position) {

					}

					@Override
					public void onItemClick(List list, View view, int position) {

					}

					@Override
					public void onItemClick(JSONArray data, View view, int position) {

					}
				});
			builder.createAndShow();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showBalance(String currency) {
		try {
			final List<Balance> balanceList = Session.balanceList;
			for (int i = 0, n = Session.balanceList.size(); i < n; i++) {
				if (Session.balanceList.get(i)
					.getCurrency()
					.equals(currency)) {
					mTvBalance.setText(Utils.format(balanceList.get(i)
						.getAmount(), 2));
					break;
				} else {
					mTvBalance.setText(getString(R.string.zero_with_decimal));
				}
			}
			if (!"0.00".equals(mTvBalance.getText()
				.toString())) {
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

	public void setCountryCode(String curValue, TextView textView) {
		mTargetCountryCode = curValue.replace("+", "")
			.trim();
		mTvTargetCountryCode.setText(curValue);
	}

	@OnClick({R.id.ll_target_country_code, R.id.tv_target_phone_hint, R.id.btn_submit, R.id.ll_toolbar_menu})
	public void onViewClicked(View view) {
		try {
			switch (view.getId()) {
				case R.id.ll_toolbar_menu:
					Intent it;
					it = new Intent(mContext, TransferRecentActivity.class);
					it.putExtra("source", SOURCE_1_MEMBER);
					startActivity(it);
					break;
				case R.id.ll_target_country_code:
					showCountryCode(mTvTargetCountryCode);
					break;
				case R.id.tv_target_phone_hint:
					mLlTargetPhone.setVisibility(View.VISIBLE);
					mTvTargetPhoneHint.setVisibility(View.GONE);
					mEtPhoneNo.setHint("");
					mEtPhoneNo.requestFocus();
					break;
				case R.id.btn_submit:
					String targetPhone = "";
					if (mTargetPhone.isEmpty()) {
						targetPhone = mEtPhoneNo.getText()
							.toString()
							.trim();
					} else {
						mTargetCountryCode = mTargetPhone.substring(1, mTargetPhone.indexOf("-"))
							.trim();
						targetPhone = mTargetPhone.substring(mTargetPhone.indexOf("-") + 1)
							.trim();
					}
					if ("".equals(targetPhone)) {
						Utils.showToast(R.string.please_input_phone);
						return;
					}
					if (TextUtils.isEmpty(mCurrency)) {
						Utils.showToast(R.string.please_select_currency);
						return;
					}
					double a = TextUtils.isEmpty(getAmount()) ? 0 : Double.parseDouble(getAmount().replace(",", "")
						.trim());
					double b = TextUtils.isEmpty(getBalance()) ? 0 : Double.parseDouble(getBalance().replace(",", "")
						.trim());
					if (a <= 0) {
						Utils.showToast(R.string.invalid_amount);
						return;
					}
					if (a > b) {
						Utils.showToast(R.string.msg_1709);
						return;
					}
					mToMemberPresenter.submit(mTargetCountryCode, targetPhone, mCurrency);
					break;
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		mToMemberPresenter.getInfoForTransferToMember();
	}

	@Override
	public void setSubmitEnables(boolean flag) {
		mBtnSubmit.setEnabled(flag);
	}

	@Override
	public void banTransfer(String msg) {
		Utils.showToast(msg);
	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCEL);
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopService(mWebsocketServiceIntent);
		WebSocketService.closeWebSocket();
	}
}
