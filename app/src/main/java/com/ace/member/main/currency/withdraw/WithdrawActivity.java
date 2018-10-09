package com.ace.member.main.currency.withdraw;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.adapter.BankViewPagerAdapter;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.BankAccount;
import com.ace.member.event.AddBankEvent;
import com.ace.member.main.currency.coupon.CouponActivity;
import com.ace.member.main.currency.new_bank.NewBankFragment;
import com.ace.member.main.detail.withdraw_detail.WithdrawDetailActivity;
import com.ace.member.simple_listener.SimpleTextWatcher;
import com.ace.member.simple_listener.SimpleViewClickListener;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.BankUtil;
import com.ace.member.utils.M;
import com.ace.member.utils.SnackBarUtil;
import com.datepicker.utils.MeasureUtil;
import com.og.utils.Utils;
import com.rd.PageIndicatorView;

import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class WithdrawActivity extends BaseActivity implements WithdrawContract.WithdrawView {

	@Inject
	WithdrawPresenter mWithdrawPresenter;
	@BindView(R.id.vp_member_bank_info)
	ViewPager mVpMemberBankInfo;
	@BindView(R.id.piv_member_bank)
	PageIndicatorView mPivMemberBank;
	@BindView(R.id.tv_currency)
	TextView mTvCurrency;
	@BindView(R.id.tv_balance)
	TextView mTvBalance;
	@BindView(R.id.tv_amount_info)
	TextView mTvAmountInfo;
	@BindView(R.id.et_withdraw_amount)
	EditText mEtWithdrawAmount;

	@BindView(R.id.tv_title_withdraw_fee)
	TextView mTvTitleWithdrawFee;
	@BindView(R.id.tv_withdraw_fee)
	TextView mTvWithdrawFee;
	@BindView(R.id.tv_title_bank_fee)
	TextView mTvTitleBankFee;
	@BindView(R.id.tv_bank_fee)
	TextView mTvBankFee;
	@BindView(R.id.tv_select)
	TextView mTvSelect;
	@BindView(R.id.et_remark)
	EditText mEtRemark;
	@BindView(R.id.btn_submit)
	Button mBtnSubmit;


	private String mCurrency;
	private String mBalance;
	private String mBank;
	private String mMemberAccountNo;
	private ViewPagerClick mViewPagerClick;
	private List<BankAccount> mMemberBankInfoList;
	private List<BankAccount> mCompanyBankInfoList;
	private String mNewBank;
	private String mNewMemberAccountNo;
	private BankViewPagerAdapter mBankMemberAdapter;
	private ViewPager.SimpleOnPageChangeListener mMemberBankInfoPageListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DaggerWithdrawComponent.builder()
			.withdrawPresenterModule(new WithdrawPresenterModule(this, this))
			.build()
			.inject(this);

		initData();
		initActivity();
		setListener();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_withdraw;
	}

	private void initData() {
		mCurrency = getIntent().getStringExtra("currency");
		mBalance = getIntent().getStringExtra("balance");
		setBalance(mBalance);
		mTvWithdrawFee.setText(Utils.format(0, mCurrency));
		mTvBankFee.setText(Utils.format(0, mCurrency));
		mVpMemberBankInfo.setPageMargin(MeasureUtil.dp2px(this, 8));
		mVpMemberBankInfo.setOffscreenPageLimit(20);
	}

	public void initActivity() {
		ToolBarConfig.Builder builder = new ToolBarConfig.Builder(this, null);
		builder.setTvTitleRes(R.string.withdraw)
			.build();

		mTvCurrency.setText(mCurrency);
		mWithdrawPresenter.start(mCurrency);
	}

	private void setListener() {
		mViewPagerClick = new ViewPagerClick();
		mEtWithdrawAmount.addTextChangedListener(new SimpleTextWatcher.MoneyTextWatcher(mEtWithdrawAmount, mCurrency) {
			@Override
			public void afterTextChanged(Editable s) {
				mWithdrawPresenter.onWithdrawAmountTextChange(mEtWithdrawAmount.getText()
					.toString());
			}
		});
	}

	@Override
	public void showToast(String msg) {
		Utils.showToast(msg);
	}

	@Override
	public void showWithDrawFail() {
		SnackBarUtil.show(getWindow().findViewById(android.R.id.content), R.string.fail, Snackbar.LENGTH_SHORT, new Snackbar.Callback() {
			@Override
			public void onDismissed(Snackbar transientBottomBar, int event) {
				finish();
			}
		});
	}

	@Override
	public void setMemberBankList(List<BankAccount> list) {
		this.mMemberBankInfoList = list;
	}

	@Override
	public void setCompanyBankList(List<BankAccount> list) {
		this.mCompanyBankInfoList = list;
	}

	private class ViewPagerClick extends SimpleViewClickListener {
		@Override
		public void onClick(final View view, int position) {
			NewBankFragment newBankFragment = new NewBankFragment();
			Bundle bundle = new Bundle();
			bundle.putSerializable("bank", (Serializable) BankUtil.getBankAccountList(mCompanyBankInfoList));
			bundle.putString("currency", mCurrency);
			newBankFragment.setArguments(bundle);
			newBankFragment.show(getSupportFragmentManager(), "NewBankFragment");
		}

		@Override
		public void onItemClick(List list, View view, int position) {
			BankAccount bankAccount = (BankAccount) list.get(position);
			deleteBank(bankAccount);
		}
	}

	private void deleteBank(BankAccount bankInfo) {
		Map<String, String> params = new HashMap<>();
		params.put("bank_id", bankInfo.getBankID());
		mWithdrawPresenter.deleteBank(params, bankInfo.getCurrency());
	}

	@Override
	public void initBank() {
		mBankMemberAdapter = new BankViewPagerAdapter(this, mMemberBankInfoList, mViewPagerClick, BankViewPagerAdapter.TYPE_3_MEMBER_BANK);
		mVpMemberBankInfo.setAdapter(mBankMemberAdapter);
		mPivMemberBank.setViewPager(mVpMemberBankInfo);

		mMemberBankInfoPageListener = new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				if (position == mBankMemberAdapter.getCount() - 1) {
					mMemberAccountNo = null;
				} else {
					mMemberAccountNo = mMemberBankInfoList.get(position)
						.getAccount_no();
					mBank = mMemberBankInfoList.get(position)
						.getCode();
				}
				mPivMemberBank.setSelectedColor(mBankMemberAdapter.getCurrentIndicatorColor(position));
				mWithdrawPresenter.calculateBankFee(getCurrentBank(), mEtWithdrawAmount.getText()
					.toString());
			}
		};
		mVpMemberBankInfo.addOnPageChangeListener(mMemberBankInfoPageListener);
		setCurrentVPMemberBank();
	}

	@Override
	public BankAccount getCurrentBank() {
		return mVpMemberBankInfo.getCurrentItem() == mBankMemberAdapter.getCount() - 1 ? null : mMemberBankInfoList.get(mVpMemberBankInfo.getCurrentItem());
	}

	@Override
	public void enableWithdrawBtn(boolean enable) {
		mBtnSubmit.setEnabled(enable);
	}

	@Override
	public void toWithdrawDetail(int id) {
		Intent intent = new Intent(this, WithdrawDetailActivity.class);
		intent.putExtra("id", id);
		intent.putExtra("showConfirm", true);
		startActivity(intent);
		finish();
	}

	@Override
	public AppCompatActivity getActivity() {
		return this;
	}

	public void setCurrentVPMemberBank() {
		boolean flag = false;
		int n = mMemberBankInfoList.size();
		for (int i = 0; i < n; i++) {
			flag = false;
			BankAccount bankAccount = mMemberBankInfoList.get(i);
			if (bankAccount.getCode()
				.equals(mNewBank) && bankAccount.getAccount_no()
				.equals(mNewMemberAccountNo)) {
				mVpMemberBankInfo.setCurrentItem(i, true);
				mMemberBankInfoPageListener.onPageSelected(i);
				mBankMemberAdapter.notifyDataSetChanged();
				mNewBank = "";
				mNewMemberAccountNo = "";
				flag = true;
				break;
			}
		}
		if (!flag) mMemberBankInfoPageListener.onPageSelected(0);
	}

	@Override
	public void setWithdrawFee(String withdrawFee) {
		mTvWithdrawFee.setText(Utils.format(withdrawFee, mCurrency));
		enableWithdrawFee(!Utils.checkEmptyAmount(withdrawFee));
	}

	@Override
	public void setBankFee(String fee) {
		mTvBankFee.setText(Utils.format(fee, mCurrency));
		enableBankFee(!Utils.checkEmptyAmount(fee));
	}

	@Override
	public void enableWithdrawFee(boolean enable) {
		mTvTitleWithdrawFee.setEnabled(enable);
		mTvWithdrawFee.setEnabled(enable);
	}

	@Override
	public void enableBankFee(boolean enable) {
		mTvTitleBankFee.setEnabled(enable);
		mTvBankFee.setEnabled(enable);
	}

	@Override
	public void setBalance(String balance) {
		mTvBalance.setText(Utils.format(balance, mCurrency));
	}

	@Override
	public void banWithdraw(boolean result) {
		if (!result)
			Utils.showToast(AppUtils.getFunctionPauseMsg(M.FunctionCode.FUNCTION_112_MEMBER_WITHDRAW), Snackbar.LENGTH_LONG);
	}

	@OnClick({R.id.tv_select,R.id.btn_submit})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.tv_select:
				Utils.toActivity(this, CouponActivity.class);
				break;
			case R.id.btn_submit:
				mWithdrawPresenter.withdraw(mCurrency, mEtWithdrawAmount.getText()
					.toString(), mBank, mMemberAccountNo, mTvBankFee.getText()
					.toString(), mEtRemark.getText()
					.toString());
				break;
		}
	}

	@Subscribe
	public void onRefresh(AddBankEvent addBankEvent) {
		mNewBank = addBankEvent.getCode();
		mNewMemberAccountNo = addBankEvent.getBankAccountNO();
		mWithdrawPresenter.getBankListAndWithdrawFee(mCurrency);
	}
}
