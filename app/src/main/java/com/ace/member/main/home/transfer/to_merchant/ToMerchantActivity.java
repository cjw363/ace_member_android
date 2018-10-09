package com.ace.member.main.home.transfer.to_merchant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.ace.member.simple_listener.SimpleTextWatcher;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.Session;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class ToMerchantActivity extends BaseCountryCodeActivity implements ToMerchantContract.View {

	@Inject
	ToMerchantPresenter mToMerchantPresenter;

	@BindView(R.id.tv_source_phone)
	TextView mTvSourcePhone;
	@BindView(R.id.tv_target_phone)
	TextView mTvTargetPhone;
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
	@BindView(R.id.tv_fee_title)
	TextView mTvFeeTitle;
	@BindView(R.id.ll_toolbar_menu)
	LinearLayout mLlToolbarMenu;

	public Context context;
	private ArrayList<String> mCurrencyList;
	private String mCurrency = "";//默认
	public static final int SOURCE_4_MERCHANT = 4;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerToMerchantComponent.builder().toMerchantPresenterModule(new ToMerchantPresenterModule(this, this)).build().inject(this);

		initActivity();
		setListener();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_to_merchant;
	}

	protected void initActivity(){
		try {
			setCurrencyList();
			ToolBarConfig.builder(this,null).setTvTitleRes(R.string.to_merchant).setIvMenuRes(R.drawable.ic_history).setEnableMenu(true).build();
			String phone= Session.user.getPhone();
			String sourcePhone = phone.substring(phone.indexOf("+"), phone.indexOf("-")) + "" + phone.substring(phone.indexOf("-"));
			mTvSourcePhone.setText(sourcePhone);
			mTvFeeTitle.setTextColor(Utils.getColor(R.color.clr_tv_fee_normal));
			mToMerchantPresenter.start(true);
			mToMerchantPresenter.getBalance();
			chkTradingPasswordStatus();
		}catch(Exception e){
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
	}

	private void setListener() {
		SimpleTextWatcher.MoneyTextWatcher mMoneyTextWatcher = new SimpleTextWatcher.MoneyTextWatcher(mEtAmount) {
			@Override
			public void afterTextChanged(Editable editable) {
				mToMerchantPresenter.updateFeeInfo();
			}
		};
		mEtAmount.addTextChangedListener(mMoneyTextWatcher);
	}

	private void setCurrencyList() {
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
	}

	@OnClick({R.id.ll_currency, R.id.btn_submit, R.id.ll_toolbar_menu})
	public void onViewClicked(View view) {
		try {
			switch (view.getId()) {
				case R.id.ll_toolbar_menu:
					Intent it;
					it = new Intent(mContext, TransferRecentActivity.class);
					it.putExtra("source", SOURCE_4_MERCHANT);
					startActivity(it);
					break;
				case R.id.ll_currency:
					showCurrency();
					break;
				case R.id.btn_submit:
					mToMerchantPresenter.submit(mCurrency);
					break;
			}
		}catch (Exception e){
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
	}

	private void showCurrency() {
		try {
			if (mCurrencyList == null) return;
			final CharSequence[] tvContent = new CharSequence[mCurrencyList.size()];
			for (int i = 0; i < mCurrencyList.size(); i++) {
				tvContent[i] = mCurrencyList.get(i);
			}
			BottomDialog.Builder builder = new BottomDialog.Builder(this).setTvTitle(Utils.getString(R.string.currency_type)).setTvContent2(tvContent).setClickListener(new IMyViewOnClickListener() {
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
		return AppUtils.getAmountValue(getCurrency(),mEtAmount.getText().toString().trim());
	}

	@Override
	public String getRemark() {
		return mEtRemark.getText().toString().trim();
	}

	@Override
	public String getBalance() {
		return mTvBalance.getText().toString().replaceAll("\\s*", "");
	}

	@Override
	public AppCompatActivity getActivity() {
		return this;
	}

	@Override
	public void setFee(double fee) {
		mTvFee.setText(AppUtils.simplifyAmount(mCurrency, String.valueOf(fee)));
		mTvFeeTitle.setEnabled(fee >= 0 && !Utils.checkEmptyAmount(mEtAmount.getText().toString()));
		mTvFee.setEnabled(fee >= 0 && !Utils.checkEmptyAmount(mEtAmount.getText().toString()));
	}

	@Override
	public String getCurrency() {
		return mTvSelect.getText().toString();
	}

	@Override
	public String getFee() {
		return mTvFee.getText().toString().trim().replace(",", "");
	}

	@Override
	public void setReceiver(String phone) {
		mTvTargetPhone.setText(phone);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mToMerchantPresenter.getBalance();
	}

	@Override
	public void clearInfo() {
		try {
			mEtAmount.setText("");
			mTvSelect.setText(Utils.getString(R.string.select));
			mCurrency = "";
			mTvSelect.setTextColor(Utils.getColor(R.color.clr_tv_hint));
			mTvBalanceTitle.setTextColor(Utils.getColor(R.color.clr_common_content_hint));

			mEtRemark.setText("");
			mTvBalance.setText("");
			mTvFee.setText("");
			mTvFeeTitle.setTextColor(Utils.getColor(R.color.clr_tv_fee_normal));
		}catch (Exception e){
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
	}

	@Override
	public void showSuccess(String time) {
		Intent it = new Intent(this, TransferResultActivity.class);
		it.putExtra("currency", mCurrency);
		it.putExtra("phone", mTvTargetPhone.getText().toString());
		it.putExtra("amount", getAmount());
		it.putExtra("fee", getFee());
		it.putExtra("time", time);
		it.putExtra("from","from_to_merchant");
		startActivity(it);
		clearInfo();
	}

	@Override
	public void setSubmitEnables(boolean flag) {
		mBtnSubmit.setEnabled(flag);
	}
}
