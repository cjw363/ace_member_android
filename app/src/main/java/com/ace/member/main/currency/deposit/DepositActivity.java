package com.ace.member.main.currency.deposit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.adapter.BankViewPagerAdapter;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.BankAccount;
import com.ace.member.event.AddBankEvent;
import com.ace.member.main.currency.new_bank.NewBankFragment;
import com.ace.member.main.detail.deposit_detail.DepositDetailActivity;
import com.ace.member.simple_listener.SimpleTextWatcher;
import com.ace.member.simple_listener.SimpleViewClickListener;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.BankUtil;
import com.ace.member.utils.M;
import com.ace.member.utils.SnackBarUtil;
import com.datepicker.utils.MeasureUtil;
import com.og.utils.FileUtils;
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

public class DepositActivity extends BaseActivity implements DepositContract.DepositView {

	@Inject
	DepositPresenter mDepositPresenter;
	@BindView(R.id.tv_currency)
	TextView mTvCurrency;
	@BindView(R.id.et_deposit_amount)
	EditText mEtDepositAmount;
	@BindView(R.id.et_remark)
	EditText mEtRemark;
	@BindView(R.id.btn_submit)
	Button mBtnSubmit;
	@BindView(R.id.vp_company_bank_info)
	ViewPager mVpCompanyBankInfo;
	@BindView(R.id.vp_member_bank_info)
	ViewPager mVpMemberBankInfo;
	@BindView(R.id.piv_member_bank)
	PageIndicatorView mPivMemberBank;
	@BindView(R.id.piv_company_bank)
	PageIndicatorView mPivCompanyBank;

	@BindView(R.id.rl_company_bank_info)
	RelativeLayout mRlCompanyBankInfo;

	private List<BankAccount> mCompanyBankInfoList;
	private List<BankAccount> mMemberBankInfoList;
	private ViewPagerClick mViewPagerClick;

	private String mCurrency;
	private String mBank;
	private String mCompanyAccountNo;
	private String mMemberAccountNo;
	private String mNewBank;
	private String mNewMemberAccountNo;
	private BankViewPagerAdapter mBankCompanyAdapter;
	private BankViewPagerAdapter mBankMemberAdapter;
	private ViewPager.SimpleOnPageChangeListener mMemberBankInfoPageListener;
	private ViewPager.SimpleOnPageChangeListener mCompanyBankInfoPageListener;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerDepositComponent.builder()
			.depositPresenterModule(new DepositPresenterModule(this, this))
			.build()
			.inject(this);

		initActivity();
		setListener();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_deposit;
	}

	public void initActivity() {
		mCurrency = getIntent().getStringExtra("currency");
		mTvCurrency.setText(mCurrency);

		ToolBarConfig.Builder builder = new ToolBarConfig.Builder(this, null);
		builder.setTvTitleRes(R.string.deposit)
			.build();

		mVpCompanyBankInfo.setPageMargin(MeasureUtil.dp2px(this, 8));
		mVpMemberBankInfo.setPageMargin(MeasureUtil.dp2px(this, 8));

		mVpMemberBankInfo.setOffscreenPageLimit(20);
		mVpCompanyBankInfo.setOffscreenPageLimit(20);
		mDepositPresenter.start(mCurrency);
	}

	private void setListener() {
		mViewPagerClick = new ViewPagerClick();
		mEtDepositAmount.addTextChangedListener(new SimpleTextWatcher.MoneyTextWatcher(mEtDepositAmount, mCurrency));
	}

	@Override
	public void setCompanyBankList(List<BankAccount> companyBankInfoList) {
		mCompanyBankInfoList = companyBankInfoList;
		if (mCompanyBankInfoList != null && mCompanyBankInfoList.size() > 0)
			mBank = mCompanyBankInfoList.get(0)
				.getCode();
	}

	@OnClick({R.id.btn_submit})
	public void onViewClicked(View view) {
		if (Utils.isFastClick(this)) return;
		switch (view.getId()) {
			case R.id.btn_submit:
				mDepositPresenter.deposit(mCurrency, mEtDepositAmount.getText()
					.toString()
					.trim(), mBank, mCompanyAccountNo, mMemberAccountNo, mEtRemark.getText()
					.toString());
				break;
		}
	}

	@Override
	public void showDepositFail() {
		SnackBarUtil.show(getWindow().findViewById(android.R.id.content), R.string.fail, Snackbar.LENGTH_SHORT, new Snackbar.Callback() {
			@Override
			public void onDismissed(Snackbar transientBottomBar, int event) {
				finish();
			}
		});
	}

	@Override
	public void enableDepositBtn(boolean enable) {
		mBtnSubmit.setEnabled(enable);
	}

	private String getAmount() {
		return mEtDepositAmount.getText()
			.toString()
			.trim()
			.replace(",", "");
	}

	@Override
	public void toDepositDetail(int id) {
		Intent intent = new Intent(this, DepositDetailActivity.class);
		intent.putExtra("id", id);
		intent.putExtra("showConfirm", true);
		startActivity(intent);
		finish();
	}

	@Override
	public AppCompatActivity getActivity() {
		return this;
	}


	@Override
	public void banDeposit(boolean result) {
		if (!result)
			Utils.showToast(AppUtils.getFunctionPauseMsg(M.FunctionCode.FUNCTION_111_MEMBER_DEPOSIT), Snackbar.LENGTH_LONG);
	}

	@Override
	public void setMemberBankList(List<BankAccount> memberBankInfoList) {
		this.mMemberBankInfoList = memberBankInfoList;
	}

	@Override
	public void enableCompanyBank(boolean b) {
		mRlCompanyBankInfo.setVisibility(b ? View.VISIBLE : View.GONE);
	}

	public boolean enableCompanyBank() {
		if (!Utils.isEmptyList(mBankCompanyAdapter.getBankAccountList())) {
			BankAccount bankAccount = mBankCompanyAdapter.getBankAccountList()
				.get(0);
			if (bankAccount.getCode()
				.equals(mBank)) {
				enableCompanyBank(true);
				mCompanyAccountNo = bankAccount.getAccount_no();
				mBankCompanyAdapter.notifyDataSetChanged();
				return true;
			}
		}
		enableCompanyBank(false);
		mCompanyAccountNo = null;
		return false;
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
		mDepositPresenter.deleteBank(params, bankInfo.getCurrency());
	}

	@Override
	public void initBank() {
		try {
			mBankMemberAdapter = new BankViewPagerAdapter(this, mMemberBankInfoList, mViewPagerClick, BankViewPagerAdapter.TYPE_3_MEMBER_BANK);
			mVpMemberBankInfo.setAdapter(mBankMemberAdapter);
			mPivMemberBank.setViewPager(mVpMemberBankInfo);

			String bank = mNewBank;
			if (TextUtils.isEmpty(bank) && Utils.isNotEmptyList(mMemberBankInfoList))
				bank = mMemberBankInfoList.get(0)
					.getCode();
			mBankCompanyAdapter = new BankViewPagerAdapter(this, BankUtil.getBankAccountList(mCompanyBankInfoList, bank), null, BankViewPagerAdapter.TYPE_2_COMPANY_BANK);
			mVpCompanyBankInfo.setAdapter(mBankCompanyAdapter);
			mPivCompanyBank.setViewPager(mVpCompanyBankInfo);

			mMemberBankInfoPageListener = new ViewPager.SimpleOnPageChangeListener() {
				@Override
				public void onPageSelected(int position) {
					if (position == mBankMemberAdapter.getCount() - 1) {
						enableCompanyBank(false);
						mBank = null;
						mMemberAccountNo = null;
					} else {
						BankAccount bankAccount = mMemberBankInfoList.get(position);
						mBank = bankAccount.getCode();
						mMemberAccountNo = bankAccount.getAccount_no();
						if (!enableCompanyBank()) {
							mBankCompanyAdapter = new BankViewPagerAdapter(DepositActivity.this, BankUtil.getBankAccountList(mCompanyBankInfoList, mBank), null, BankViewPagerAdapter.TYPE_2_COMPANY_BANK);
							mVpCompanyBankInfo.setAdapter(mBankCompanyAdapter);
							mPivCompanyBank.setViewPager(mVpCompanyBankInfo);
							enableCompanyBank();
						}
						mPivCompanyBank.setSelectedColor(mBankMemberAdapter.getCurrentIndicatorColor(position));
					}
					mPivMemberBank.setSelectedColor(mBankMemberAdapter.getCurrentIndicatorColor(position));
				}
			};
			mVpMemberBankInfo.addOnPageChangeListener(mMemberBankInfoPageListener);

			mCompanyBankInfoPageListener = new ViewPager.SimpleOnPageChangeListener() {
				@Override
				public void onPageSelected(int position) {
					mCompanyAccountNo = mBankCompanyAdapter.getBankAccountList()
						.get(position)
						.getAccount_no();
				}
			};
			mVpCompanyBankInfo.addOnPageChangeListener(mCompanyBankInfoPageListener);

			if (TextUtils.isEmpty(mNewBank)) {
				mMemberBankInfoPageListener.onPageSelected(0);
			} else {
				int n = mMemberBankInfoList.size();
				for (int i = 0; i < n; i++) {
					BankAccount bankAccount = mMemberBankInfoList.get(i);
					if (bankAccount.getCode()
						.equals(mNewBank) && bankAccount.getAccount_no()
						.equals(mNewMemberAccountNo)) {
						mVpMemberBankInfo.setCurrentItem(i, true);
						mMemberBankInfoPageListener.onPageSelected(i);
					}
				}
				mNewBank = null;
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@Subscribe
	public void onRefresh(AddBankEvent addBankEvent) {
		mNewBank = addBankEvent.getCode();
		mNewMemberAccountNo = addBankEvent.getBankAccountNO();
		mDepositPresenter.getBankList(mCurrency);
	}
}
