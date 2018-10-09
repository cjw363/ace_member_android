package com.ace.member.main.me.exchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.Balance;
import com.ace.member.bean.Currency;
import com.ace.member.main.me.exchange.exchange_rate.ExchangeRateActivity;
import com.ace.member.main.me.exchange.recent.ExchangeHistoryActivity;
import com.ace.member.popup_window.MenuPopWindow;
import com.ace.member.simple_listener.SimpleTextWatcher;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.M;
import com.ace.member.utils.Session;
import com.og.LibSession;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.ace.member.R.string.exchange;

public class ExchangeActivity extends BaseActivity implements View.OnClickListener, ExchangeContract.ExchangeContractView, SwipeRefreshLayout.OnRefreshListener {

	@Inject
	ExchangePresenter mExchangePresenter;

	@BindView(R.id.srl)
	SwipeRefreshLayout mRefreshLayout;

	@BindView(R.id.rg_source_currency)
	RadioGroup mRgSourceCurrency;
	@BindView(R.id.rg_target_currency)
	RadioGroup mRgTargetCurrency;
	@BindView(R.id.ll_balance)
	LinearLayout mLlBalance;
	@BindView(R.id.et_exchange_amount)
	EditText mEtAmount;
	@BindView(R.id.tv_exchange_rate)
	TextView mTvExchangeRate;
	@BindView(R.id.tv_exchange_balance)
	TextView mTvExchangeBalance;
	@BindView(R.id.btn_submit)
	Button mBtnSubmit;
	@BindView(R.id.iv_menu)
	ImageView mIvMenu;

	private List<Map<String, Object>> mCurrencyList;
	private double mBalance = 0.00;
	private String mTargetCurrency;
	private String mSourceCurrency;  //初始默认
	private Map<String, String> mExchangeRateObj;

	private ArrayList<Integer> lvIcon = new ArrayList<>(Arrays.asList(R.drawable.ic_exchange_rate, R.drawable.ic_recent));
	private ArrayList<Integer> lvIconName = new ArrayList<>(Arrays.asList(R.string.exchange_rate, R.string.history));
	private MenuPopWindow mMenuPopWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerExchangeComponent.builder()
			.exchangePresenterModule(new ExchangePresenterModule(this, this))
			.build()
			.inject(this);

		initActivity();
		setListener();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_exchange;
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		initActivity();
	}

	public void initActivity() {
		ToolBarConfig.builder(this, null).setTvTitleRes(exchange).setIvMenuRes(R.drawable.ic_menu).setEnableMenu(true).build();
		mCurrencyList = new ArrayList<>();
		setCurrencyList(Session.currencyList);

		mRefreshLayout.setColorSchemeColors(Utils.getColor(R.color.colorPrimary));
		mRefreshLayout.setOnRefreshListener(this);
		mExchangePresenter.getBalanceInfo(true);
		initMenuPopWindow();
	}

	private void setListener() {
		try {
			mBtnSubmit.setOnClickListener(this);

			mRgSourceCurrency.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
					initTargetCurrencyList(radioButton.getText().toString().trim());
				}

			});
			mRgTargetCurrency.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					updateExchangeInfo();
				}

			});

			mEtAmount.addTextChangedListener(new SimpleTextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					/*
					 * 限制输入金额最多为 limit 位小数
					 */
					if (s.toString().contains(".")) {
						if (s.length() - 1 - s.toString().indexOf(".") > 2) {
							s = s.toString().subSequence(0, s.toString().indexOf(".") + 2 + 1);
							mEtAmount.setText(s);
							mEtAmount.setSelection(s.length());
						}
					}
					/*
					 * 第一位输入小数点的话自动变换为 0.
					 */
					if (".".equals(s.toString().trim().substring(0))) {
						s = "0" + s;
						mEtAmount.setText(s);
						mEtAmount.setSelection(2);
					}

					/*
					 * 避免重复输入小数点前的0 ,没有意义
					 */
					if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
						if (!s.toString().substring(1, 2).equals(".")) {
							mEtAmount.setText(s.subSequence(0, 1));
							mEtAmount.setSelection(1);
						}
					}
				}

				@Override
				public void afterTextChanged(Editable editable) {
					if (!"".equals(mEtAmount.getText().toString())) {
						updateExchangeInfo();
					} else {
						mTvExchangeBalance.setText(getString(R.string.zero_with_decimal));
					}
				}
			});
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@OnClick(R.id.ll_toolbar_menu)
	public void onViewClicked() {
		mMenuPopWindow.showAsDropDown(this, mIvMenu);
	}

	private void initMenuPopWindow() {
		mMenuPopWindow = new MenuPopWindow.Builder(this, lvIconName, new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int resourceId = lvIcon.get(position);
				Intent it;
				switch (resourceId) {
					case R.drawable.ic_exchange_rate:
						it = new Intent(ExchangeActivity.this, ExchangeRateActivity.class);
						startActivity(it);
						onResume();
						break;
					case R.drawable.ic_recent:
						it = new Intent(ExchangeActivity.this, ExchangeHistoryActivity.class);
						startActivity(it);
						onResume();
						break;
				}
				mMenuPopWindow.dismiss();
			}
		}).setItemIcons(lvIcon).build();
	}

	@Override
	public void setExchangeRate(ExchangeInfoBean exchangeInfoBean) {
		try {
			mExchangeRateObj = exchangeInfoBean.getExchangeRate();
			initialization();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_submit:
				if (Utils.isFastClick(this)) {
					return;
				}
				submit();
				break;
		}
	}

	public void submit() {
		final String amount = mEtAmount.getText().toString();
		final Map<String, String> params = new HashMap<>();
		params.put("_b", "aj");
		params.put("_a", "balance");
		params.put("cmd", "checkExchange");
		params.put("_s", LibSession.sSid);
		params.put("amount", amount);
		params.put("target_currency", mTargetCurrency);
		params.put("source_currency", mSourceCurrency);
		if (TextUtils.isEmpty(mTargetCurrency)) {
			Utils.showToast(R.string.invalid_exchange_currency);
			return;
		} else if ("".equals(amount) || Double.valueOf(amount) <= 0) {
			Utils.showToast(R.string.invalid_amount);
			return;
		}
		mExchangePresenter.submitExchange(params);
	}

	public void updateBalance(boolean isRefresh, boolean function) {
		if (!function){
			Utils.showToast(AppUtils.getFunctionPauseMsg(M.FunctionCode.FUNCTION_119_MEMBER_EXCHANGE), Snackbar.LENGTH_LONG);
		}
		if (isRefresh) {
			Utils.showToast(R.string.success);
		}
		mRefreshLayout.setRefreshing(false);
		try {
			mExchangePresenter.getExchangeRate(false);
			if (Session.balanceList == null || currencyAmountView.size() <= 0 || Session.currencyList == null)
				return;
			int len = Session.currencyList.size();
			for (int i = 1; i * 2 <= len + 1; i++) {
				Currency currencyObj1 = Session.currencyList.get((i - 1) * 2);
				String currencyStr1 = currencyObj1.getCurrency();
				double amount1 = getBalanceByCurrency(currencyStr1);
				TextView textView1 = currencyAmountView.get(currencyStr1);
				if (amount1 < 0) {
					textView1.setTextColor(Utils.getColor(R.color.clr_amount_red));
				} else {
					textView1.setTextColor(Utils.getColor(R.color.colorPrimary));
				}
				textView1.setText(Utils.format(amount1, 2));

				if (!((i - 1) * 2 + 1 == len)) {
					Currency currencyObj2 = Session.currencyList.get(i * 2 - 1);
					String currencyStr2 = currencyObj2.getCurrency();
					double amount2 = getBalanceByCurrency(currencyStr2);
					TextView textView2 = currencyAmountView.get(currencyStr2);
					if (amount2 < 0) {
						textView2.setTextColor(Utils.getColor(R.color.clr_amount_red));
					} else {
						textView2.setTextColor(Utils.getColor(R.color.colorPrimary));
					}
					textView2.setText(Utils.format(amount2, 2));
				}
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
	}

	private double getBalanceByCurrency(String currency) {
		try {
			int len = Session.balanceList.size();
			for (int i = 0; i < len; i++) {
				Balance obj = Session.balanceList.get(i);
				String cur = obj.getCurrency();
				if (currency.equals(cur)) {
					return obj.getAmount();
				}
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
		return 0;
	}

	public void setCurrencyList(List<Currency> list) {
		try {
			if (list == null) return;
			mRgSourceCurrency.removeAllViews();
			mRgTargetCurrency.removeAllViews();
			mCurrencyList.clear();
			currencyView = new HashMap<>();
			int len = list.size();
			for (int i = 0; i < len; i++) {
				Map<String, Object> map = new HashMap<>();
				Currency bean = list.get(i);
				String currency = bean.getCurrency();
				map.put("currency", currency);
				RadioButton radioButton1 = (RadioButton) LayoutInflater.from(mContext)
					.inflate(R.layout.view_radio_button, null);
				radioButton1.setText(currency);
				RadioButton radioButton2 = (RadioButton) LayoutInflater.from(mContext)
					.inflate(R.layout.view_radio_button, null);
				radioButton2.setText(currency);
				RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				params.setMargins(0, 0, Utils.getDimenPx(R.dimen.margin10), 0);
				currencyView.put(currency, radioButton1);
				mRgSourceCurrency.addView(radioButton1, params);
				mRgTargetCurrency.addView(radioButton2, params);
				mCurrencyList.add(i, map);
			}

			currencyAmountView = new HashMap<>();
			mLlBalance.removeAllViews();
			for (int i = 1; i * 2 <= len + 1; i++) {
				Currency obj1 = list.get((i - 1) * 2);
				View balanceView = getLayoutInflater().inflate(R.layout.view_exchange_balance, null);
				TextView currency1 = (TextView) balanceView.findViewById(R.id.tv_currency_1);
				TextView amount1 = (TextView) balanceView.findViewById(R.id.tv_amount_1);
				TextView currency2 = (TextView) balanceView.findViewById(R.id.tv_currency_2);
				TextView amount2 = (TextView) balanceView.findViewById(R.id.tv_amount_2);

				String currencyStr1 = obj1.getCurrency();
				String currencyStr1Title = currencyStr1 + " ";
				currency1.setText(currencyStr1Title);
				currencyAmountView.put(currencyStr1, amount1);
				if (!((i - 1) * 2 + 1 == len)) {
					Currency obj2 = list.get((i * 2 - 1));
					String currencyStr2 = obj2.getCurrency();
					String currencyStr2Title = currencyStr2 + " ";
					currency2.setText(currencyStr2Title);
					currencyAmountView.put(currencyStr2, amount2);
				}
				mLlBalance.addView(balanceView);
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
	}

	private Map<String, TextView> currencyAmountView;
	private Map<String, RadioButton> currencyView;

	public void initialization() {
		try {
			for (Map.Entry<String, RadioButton> entry : currencyView.entrySet()) {
				entry.getKey();
				entry.getValue();
				boolean temp = false;
				int len = Session.balanceList.size();
				for (int j = 0; j < len; j++) {
					Balance obj = Session.balanceList.get(j);
					if (entry.getKey().equals(obj.getCurrency())) {
						temp = true;
						break;
					}
				}
				if (!temp) entry.getValue().setEnabled(false);
			}

			int checkIndex = 0;
			if (!(mRgSourceCurrency.getChildAt(0)).isEnabled()) {
				while (checkIndex < mCurrencyList.size() && !(mRgSourceCurrency.getChildAt(checkIndex)).isEnabled()) {
					checkIndex++;
				}
			}
			String currency = "";
			if (checkIndex < mCurrencyList.size()) {
				((RadioButton) mRgSourceCurrency.getChildAt(checkIndex)).setChecked(true);
				currency = mCurrencyList.get(0).get("currency").toString().trim();
			}
			initTargetCurrencyList(currency);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initTargetCurrencyList(String index) {
		try {
			if (Session.balanceList != null && !TextUtils.isEmpty(index)) {
				boolean temp = true;
				int len = Session.balanceList.size();
				for (int i = 0; i < len; i++) {
					Balance balance = Session.balanceList.get(i);
					if (index.equals(balance.getCurrency())) {
						mBalance = balance.getAmount();
						temp = false;
						break;
					}
				}
				if (temp) mBalance = 0;
			} else {
				mBalance = 0;
			}

			mRgTargetCurrency.removeAllViews();
			for (int i = 0; i < mCurrencyList.size(); i++) {
				String currency = mCurrencyList.get(i).get("currency").toString();
				RadioButton radioButton = (RadioButton) LayoutInflater.from(mContext)
					.inflate(R.layout.view_radio_button, null);
				radioButton.setText(currency);
				String rate = mExchangeRateObj.get(index + "->" + currency);

				if (TextUtils.isEmpty(index) || index.equals(currency) || TextUtils.isEmpty(rate)) {
					radioButton.setEnabled(false);
				}
				RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				params.setMargins(0, 0, Utils.getDimenPx(R.dimen.margin10), 0);
				mRgTargetCurrency.addView(radioButton, params);
			}
			int checkIndex = 0;
			if (!(mRgTargetCurrency.getChildAt(0)).isEnabled()) {
				while (checkIndex < mCurrencyList.size() && !(mRgTargetCurrency.getChildAt(checkIndex)).isEnabled()) {
					checkIndex++;
				}
			}
			if (checkIndex < mCurrencyList.size()) {
				((RadioButton) mRgTargetCurrency.getChildAt(checkIndex)).setChecked(true);
				updateExchangeInfo();
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}

	}

	public void clearExchangeInfo() {
		if (mEtAmount != null) {
			mEtAmount.setText("");
		}
	}

	private void updateExchangeInfo() {
		try {
			RadioButton targetRadioButton = (RadioButton) mRgTargetCurrency.findViewById(mRgTargetCurrency
				.getCheckedRadioButtonId());
			mTargetCurrency = targetRadioButton.getText().toString().trim();

			RadioButton sourceRadioButton = (RadioButton) mRgSourceCurrency.findViewById(mRgSourceCurrency
				.getCheckedRadioButtonId());
			mSourceCurrency = sourceRadioButton.getText().toString().trim();

			if (mExchangeRateObj == null) return;

			String rateShow = mExchangeRateObj.get(mSourceCurrency + "->" + mTargetCurrency + "show");
			String rate = mExchangeRateObj.get(mSourceCurrency + "->" + mTargetCurrency);
			mTvExchangeRate.setText(rateShow);

			String sourceAmount = mEtAmount.getText().toString();
			if (!TextUtils.isEmpty(sourceAmount)) {
				updateExchangeAmount(rate, sourceAmount);
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
	}

	@Override
	public void onRefresh() {
		mExchangePresenter.getBalanceInfo(false);
	}

	private void updateExchangeAmount(String rate, String sourceAmount) {
		if (TextUtils.isEmpty(rate)) return;
		double dRate = Double.valueOf(rate);
		double dSourceAmount = Double.valueOf(sourceAmount);
		double exchangeAmount = dRate * dSourceAmount;
		mTvExchangeBalance.setText(Utils.format(exchangeAmount, 2));
	}

	public void finishActivity() {
		finish();
	}

	@Override
	protected void onDestroy() {
		if (mMenuPopWindow != null) mMenuPopWindow.dismiss();
		super.onDestroy();
	}

	@Override
	public void setSubmitEnables(boolean flag) {
		mBtnSubmit.setEnabled(flag);
	}
}
