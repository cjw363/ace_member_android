package com.ace.member.main.third_party.bill_payment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatImageView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.adapter.BillPaymentRuleAdapter;
import com.ace.member.base.BaseActivity;
import com.ace.member.base.BasePresenter;
import com.ace.member.bean.BillerBean;
import com.ace.member.bean.BillerConfig;
import com.ace.member.main.third_party.bill_payment.history.BillPaymentHistoryActivity;
import com.ace.member.main.third_party.bill_payment.select_biller.SelectBillerActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.M;
import com.ace.member.utils.SPUtil;
import com.ace.member.utils.SnackBarUtil;
import com.og.LibSession;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.FutureTask;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class BillPaymentActivity extends BaseActivity implements BillPaymentContract.view {

	@Inject
	BillPaymentPresenter mPresenter;

	@BindView(R.id.iv_biller_company)
	AppCompatImageView mIvBillerCompany;
	@BindView(R.id.tv_biller_company_name)
	TextView mTvBillerCompanyName;
	@BindView(R.id.tv_currency)
	TextView mTvCurrency;
	@BindView(R.id.et_bill_number)
	EditText mEtBillNumber;
	@BindView(R.id.et_amount)
	EditText mEtAmount;
	@BindView(R.id.tv_fee_title)
	TextView mTvFeeTitle;
	@BindView(R.id.tv_fee)
	TextView mTvFee;
	@BindView(R.id.et_remark)
	EditText mEtRemark;
	@BindView(R.id.iv_touch)
	AppCompatImageView mIvTouch;
	@BindView(R.id.tv_bill_title)
	TextView mTvBillTitle;
	@BindView(R.id.ll_fee_rule_area)
	LinearLayout mLlFeeRuleArea;
	@BindView(R.id.lv_rule)
	ListView mLvRule;
	@BindView(R.id.btn_submit)
	Button mBtnSubmit;

	private String mToken = "";
	private List<BillerConfig> mConfigList;
	private BillerBean mSelBiller;
	private BillerConfig mSelConfig;
	private double mFee = 0;
	private boolean misInput = false, mIsRunning = true;
	private FutureTask<Void> mAmountFutureTask;
	private TextWatcher mEtAmountTextWatcher;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerBillPaymentComponent.builder()
				.billPaymentPresenterModule(new BillPaymentPresenterModule(this, this))
				.build()
				.inject(this);
		initActivity();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_bill_payment;
	}

	protected void initActivity() {
		ToolBarConfig.builder(this, null)
				.setEnableMenu(true)
				.setMenuType(ToolBarConfig.MenuType.MENU_IMAGE)
				.setIvMenuRes(R.drawable.ic_history)
				.setTvTitleRes(R.string.bill_payment)
				.setMenuListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(BillPaymentActivity.this, BillPaymentHistoryActivity.class);
						startActivity(intent);
					}
				})
				.build();
		initView();
		setListenerEvent();
	}

	@Override
	public void enableBtnSubmit(boolean enable) {
		mBtnSubmit.setEnabled(enable);
	}

	private void setListenerEvent() {
//		mEtBillNumber.addTextChangedListener(new SimpleTextWatcher() {
//			@Override
//			public void afterTextChanged(Editable s) {
//				mBtnSubmit.setEnabled(checkSubmitEnable());
//			}
//		});

		mEtAmountTextWatcher = new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				if (mAmountFutureTask != null) {
					mAmountFutureTask.cancel(true);
					misInput = false;
				}
				if (misInput) return;
				misInput = true;
				mAmountFutureTask = new FutureTask<>(new Runnable() {
					@Override
					public void run() {
						initAmount();
						misInput = false;
						mAmountFutureTask.cancel(true);
					}
				}, null);
				Utils.runOnUIThread(mAmountFutureTask, 1000);

			}

			@Override
			public void afterTextChanged(Editable editable) {
				String currency = AppGlobal.USD;
				if (mSelBiller != null) currency = mSelBiller.getCurrency();
				if (!TextUtils.isEmpty(getAmount())) {
					mTvFee.setTextColor(Utils.getColor(R.color.clr_tv_fee_select));
					mTvFeeTitle.setTextColor(Utils.getColor(R.color.clr_tv_fee_select));
					mPresenter.updateFeeInfo(mConfigList);
					mEtAmount.setHint(null);
					mTvCurrency.setText(currency);
				} else {
					mEtAmount.setHint(getResources().getString(R.string.amount) + " (" + currency + ")");
					mTvCurrency.setText(null);
					showCalculateFeeArea(null, 0);
				}
//				mBtnSubmit.setEnabled(checkSubmitEnable());
			}
		};

		mEtAmount.addTextChangedListener(mEtAmountTextWatcher);
	}

	private boolean checkSubmitEnable() {
		String amountStr = getAmount();
		String billStr = mEtBillNumber.getText().toString();
		if (!TextUtils.isEmpty(amountStr) && !TextUtils.isEmpty(billStr) && mIsRunning) {
			return true;
		}
		return false;
	}

	private void initAmount() {
		mEtAmount.removeTextChangedListener(mEtAmountTextWatcher);
		String amount = getAmount(), currency = AppGlobal.USD;
		if (mSelBiller != null) currency = mSelBiller.getCurrency();
		if (!TextUtils.isEmpty(amount)) {
			mEtAmount.setText(Utils.format(amount, 2));
			int len = mEtAmount.getText().toString().length();
			mEtAmount.setSelection(len);
			mTvCurrency.setText(currency);
		} else {
			mEtAmount.setHint(getResources().getString(R.string.amount) + " (" + currency + ")");
		}
		mEtAmount.addTextChangedListener(mEtAmountTextWatcher);
	}

	private void initView() {
		mPresenter.getToken("BILL_PAYMENT", new BasePresenter.IGetToken() {
			@Override
			public void getTokenSuccess(String token) {
				mToken = token;
			}

			@Override
			public void getTokenFail() {

			}
		});

		int billID = SPUtil.getInt("biller_id", 0);
		initBiller(billID);

	}

	@Override
	public void enableEmptyPartnerCompany(boolean enable) {
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
	}

	@OnClick({R.id.rl_select_company, R.id.btn_submit, R.id.tv_currency})
	protected void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.rl_select_company:
				Intent it = new Intent(BillPaymentActivity.this, SelectBillerActivity.class);
				int type = 1;
				if (mSelBiller != null) type = mSelBiller.getType();
				it.putExtra("type", type);
				startActivityForResult(it, Activity.RESULT_CANCELED);
				break;
			case R.id.btn_submit:
				submit();
				break;
			case R.id.tv_currency:
				mEtAmount.requestFocus();
				AppUtils.enableSoftInput(mEtAmount, true);
				break;
		}
	}

	private void submit() {
		try {
			if (!mIsRunning) {
				mBtnSubmit.setEnabled(false);
				Utils.showToast(AppUtils.getFunctionPauseMsg(M.FunctionCode.FUNCTION_135_MEMBER_PAY_BILL_TO_PARTNER), Snackbar.LENGTH_LONG);
				return;
			}

			if (mSelBiller == null || mSelBiller.getPartnerID() == 0 || mSelBiller.getID() == 0) {
				Utils.showToast(getResources().getString(R.string.select_biller_error));
				return;
			}

			String billID = mEtBillNumber.getText().toString();
			if (TextUtils.isEmpty(billID)) {
				Utils.showToast(String.format(getString(R.string.invalid_bill_number), mSelBiller.getBillTitle()));
				return;
			}

			double amount = Utils.strToDouble(getAmount());
			if (amount <= 0) {
				Utils.showToast(getResources().getString(R.string.invalid_amount));
				return;
			}

			if (mSelConfig == null) {
				Utils.showToast(getResources().getString(R.string.config_error));
				return;
			}

			double maxAmount = mSelBiller.getAmount();
			double exchange = AppUtils.getExchangeRate(mSelConfig.getCurrency(), getCurrency());
			double splitAmount = Utils.round2(mSelConfig.getAmount() * exchange, 2);
			double minAmount = Math.min(maxAmount, splitAmount);
			if (amount > minAmount) {
				Utils.showToast(String.format(getResources().getString(R.string.amount_limit), Utils.format(minAmount, mSelBiller
						.getCurrency()) + " " + mSelBiller.getCurrency()));
				return;
			}
			if (!AppUtils.checkEnoughMoney(mSelBiller.getCurrency(), amount + mFee)) {
				Utils.showToast(R.string.msg_1709);
				return;
			}

			String billLengthStr = mSelBiller.getBillLength();
			String[] str = billLengthStr.split(",");
			int len1 = billID.length();
			if (!checkBillLength(str, len1)) {
				Utils.showToast(String.format(getString(R.string.bill_number_length), mSelBiller.getBillTitle(), billLengthStr));
				return;
			}

			String remark = mEtRemark.getText().toString();
			String currency = mSelBiller.getCurrency();
			String fee = AppUtils.simplifyAmount(currency, Utils.round(mFee, 2));
			Map<String, String> p = new HashMap<>();
			p.put("partner_id", String.valueOf(mSelBiller.getPartnerID()));
			p.put("id", String.valueOf(mSelBiller.getID()));
			p.put("currency", String.valueOf(currency));
			p.put("bill_id", billID);
			p.put("amount", String.valueOf(amount));
			p.put("type", String.valueOf(mSelBiller.getType()));
			p.put("unique_token", mToken);
			p.put("fee", fee);
			p.put("remark", remark);
			p.put("_a", "payment");
			p.put("_b", "aj");
			p.put("_s", LibSession.sSid);
			p.put("cmd", "checkBillPayment");
			mPresenter.submit(p);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	private boolean checkBillLength(String[] str, int len) {
		boolean bool = false;
		for (String a : str) {
			int b = Integer.valueOf(a);
			if (len == b) {
				bool = true;
				break;
			}
		}
		return bool;
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			int id = data.getBundleExtra("bundle").getInt("biller_id");
			initBiller(id);
		}
	}

	@Override
	public void initBillerConfig(List<BillerConfig> list, BillerBean bean, boolean isRunning) {
		mConfigList = list;
		mIsRunning = isRunning;
		if (!mIsRunning) {
			Utils.showToast(AppUtils.getFunctionPauseMsg(M.FunctionCode.FUNCTION_135_MEMBER_PAY_BILL_TO_PARTNER), Snackbar.LENGTH_LONG);
		}
		setSelBiller(bean);
		showRule(mConfigList);
		String amountStr = getAmount();
		double amount = TextUtils.isEmpty(amountStr) ? 0 : Double.valueOf(amountStr);
		if (amount > 0) {
			mPresenter.updateFeeInfo(list);
		}

	}

	private void showRule(List<BillerConfig> list) {
		try {
			if (list == null || list.size() == 0) {
				mLlFeeRuleArea.setVisibility(View.GONE);
				SnackBarUtil.show(getWindow().findViewById(android.R.id.content), Utils.getString(R.string.config_error), Snackbar.LENGTH_SHORT,null);
				return;
			}
			mLlFeeRuleArea.setVisibility(View.VISIBLE);
			String currency = mSelBiller.getCurrency();
			double maxAmount = mSelBiller.getAmount();
			List<BillerConfig> list1 = new ArrayList<>();
			int size = list.size();
			for (int i = 0; i < size; i++) {
				BillerConfig config = list.get(i);
				BillerConfig config1 = new BillerConfig();
				double exchange = AppUtils.getExchangeRate(config.getCurrency(), currency);
				double amount = Utils.round2(config.getAmount() * exchange, 2);
				double minAmount = Math.min(amount, maxAmount);
				double exchange1 = AppUtils.getExchangeFeeRate(config.getCurrency(), currency);
				double fixed = Utils.round2(config.getFee() * exchange1, 2);
				config1.setFee(fixed);
				config1.setCurrency(currency);
				config1.setAmount(minAmount);
				config1.setType(config.getType());
				config1.setPercent(config.getPercent());
				list1.add(config1);
				if (maxAmount < amount) {
					break;
				}
			}

			BillPaymentRuleAdapter adapter = new BillPaymentRuleAdapter(list1);
			mLvRule.setAdapter(adapter);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	private void initBiller(int id) {
		String currency = AppGlobal.USD;
		if (id > 0) {
			enableEmptyPartnerCompany(false);
			mPresenter.getBillerConfig(id);
		} else {
			String title = getResources().getString(R.string.bill_id);
			mTvBillTitle.setText(title);
			mEtBillNumber.setHint(title);
			mEtAmount.setHint(getResources().getString(R.string.amount_usd));
			enableEmptyPartnerCompany(true);
			setFee(0.00, currency);
		}

	}

	private void setSelBiller(BillerBean bean) {
		//暂时默认
		mIvBillerCompany.setImageResource(R.drawable.ic_partner_common);
		String code = bean.getCode();
		String name = bean.getName();
		String title = bean.getTitle();
		String str = code + " " + name;
		if (!TextUtils.isEmpty(title)) str += " (" + title + ")";
		mTvBillerCompanyName.setText(str);
		String billTitle = bean.getBillTitle();
		mTvBillTitle.setText(billTitle);
		String billLengthStr = bean.getBillLength();
		mEtBillNumber.setHint(String.format(Utils.getString(R.string.length_limit), billLengthStr));
		SPUtil.putInt("biller_id", bean.getID());
		mSelBiller = bean;
		initAmount();
	}


	@Override
	public String getCurrency() {
		String currency = mTvCurrency.getText().toString();
		if (TextUtils.isEmpty(currency) && mSelBiller != null) {
			currency = mSelBiller.getCurrency();
		} else {
			if (TextUtils.isEmpty(currency)) currency = AppGlobal.USD;
		}
		return currency;
	}

	@Override
	public String getAmount() {
		return mEtAmount.getText().toString().replace(",", "");
	}

	@Override
	public void initToken(String token) {
		mToken = token;
	}

	@Override
	public void setFee(double amount, String currency) {
		mTvFee.setText(Utils.format(Utils.round(amount, 2), currency) + " " + currency);
	}

	@Override
	public void showCalculateFeeArea(BillerConfig config, double amount) {
		if (config == null || amount <= 0) {
			mTvFeeTitle.setTextColor(Utils.getColor(R.color.clr_tv_fee_normal));
			mTvFee.setTextColor(Utils.getColor(R.color.clr_tv_fee_normal));
			mTvFee.setText(Utils.format(0, AppGlobal.USD));
			setFee(0.00, AppGlobal.USD);
		} else {
			mTvFeeTitle.setTextColor(Utils.getColor(R.color.clr_tv_fee_select));
			mTvFee.setTextColor(Utils.getColor(R.color.clr_tv_fee_select));
			String currency = mSelBiller.getCurrency();
			double rate = AppUtils.getExchangeFeeRate(config.getCurrency(), currency);
			mFee = 0;
			double percent = 0;
			mSelConfig = config;
			if (config.getType() == 2) {
				mFee = config.getFee() * rate;
			} else {
				percent = config.getPercent();
				mFee = (percent * amount * rate) / 100;
			}
			setFee(mFee, currency);
		}
	}

	@Override
	public void saveFail(int code, List<BillerConfig> list, BillerBean bean) {
		mSelBiller = bean;
		mConfigList = list;
		switch (code) {
			case M.MessageCode.MSG_1724_OVER_MAX_AMOUNT:
				String currency = mSelBiller.getCurrency();
				double maxAmount = mSelBiller.getAmount();
				double splitAmount = mSelConfig.getAmount();
				double minAmount = Math.min(maxAmount, splitAmount);
				Utils.showToast(String.format(getResources().getString(R.string.amount_limit), Utils.format(minAmount, currency)) + " " + currency);
				break;
			case M.MessageCode.MSG_1725_OVER_BILL_ID_LENGTH:
				String billLengthStr = mSelBiller.getBillLength();
				Utils.showToast(String.format(getString(R.string.bill_number_length), mSelBiller.getBillTitle(), billLengthStr));
				break;
			case M.MessageCode.ERR_1709_NOT_ENOUGH_BALANCE:
				Utils.showToast(R.string.msg_1709);
				break;
			case com.og.M.MessageCode.ERR_505_FUNCTION_NOT_RUNNING:
				mIsRunning = false;
				mBtnSubmit.setEnabled(false);
				Utils.showToast(AppUtils.getFunctionPauseMsg(M.FunctionCode.FUNCTION_135_MEMBER_PAY_BILL_TO_PARTNER), Snackbar.LENGTH_LONG);
				break;
			default:
				Utils.showToast(R.string.fail);
				break;
		}
	}

	@Override
	public void saveSuccess() {
		Utils.showToast(getResources().getString(R.string.success));
		mEtAmount.setText("");
		mEtBillNumber.setText("");
		mTvFeeTitle.setTextColor(Utils.getColor(R.color.clr_tv_fee_normal));
		mTvFee.setTextColor(Utils.getColor(R.color.clr_tv_fee_normal));
		String currency = AppGlobal.USD;
		if (mSelBiller != null) currency = mSelBiller.getCurrency();
		String str = getResources().getString(R.string.zero_with_decimal) + currency;
		mTvFee.setText("");
		mTvFee.setHint(str);
		mEtRemark.setText("");
	}
}
