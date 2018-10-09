package com.ace.member.main.detail.withdraw_detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.BalanceRecord;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.BankUtil;
import com.ace.member.utils.ColorUtil;
import com.og.utils.Utils;

import javax.inject.Inject;

import butterknife.BindView;

public class WithdrawDetailActivity extends BaseActivity implements WithdrawDetailContract.View {
	@Inject
	WithdrawDetailPresenter mPresenter;
	@BindView(R.id.iv_bank)
	AppCompatImageView mIvBank;
	@BindView(R.id.tv_bank)
	TextView mTvBank;
	@BindView(R.id.tv_amount)
	TextView mTvAmount;
	@BindView(R.id.tv_name)
	TextView mTvName;
	@BindView(R.id.tv_account)
	TextView mTvAccount;
	@BindView(R.id.tv_withdraw_fee)
	TextView mTvWithdrawFee;
	@BindView(R.id.tv_bank_fee)
	TextView mTvBankFee;
	@BindView(R.id.tv_remark)
	TextView mTvRemark;
	@BindView(R.id.tv_time)
	TextView mTvTime;
	@BindView(R.id.tv_status)
	TextView mTvStatus;
	@BindView(R.id.btn_confirm)
	Button mBtnConfirm;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerWithdrawDetailComponent.builder()
			.withdrawDetailPresenterModule(new WithdrawDetailPresenterModule(this, this))
			.build()
			.inject(this);
		init();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_withdraw_detail;
	}

	private void init() {
		int id = getIntent().getIntExtra("id", 0);
		boolean showConfirm = getIntent().getBooleanExtra("showConfirm", false);
		mPresenter.getBalanceRecord(id);
		new ToolBarConfig.Builder(this, null).setTvTitleRes(R.string.withdraw_detail)
			.build();
		if (showConfirm) {
			mBtnConfirm.setVisibility(View.VISIBLE);
			mBtnConfirm.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}
	}

	@Override
	public void setBalanceRecord(BalanceRecord record) {
		if (record != null) {
			String bankCode = record.getBankCode();
			String currency = record.getCurrency();
			int digits = currency.equals(AppGlobal.KHR) || currency.equals(AppGlobal.VND) ? 0 : 2;
			int status = record.getStatus();
			mTvBank.setText(bankCode);
			mIvBank.setImageResource(BankUtil.getBankImageResourceByBankCode(bankCode));
			mTvAmount.setText(Utils.format(record.getAmount()
				.replace("-", ""), digits) + " " + currency);
			mTvName.setText(record.getBankAccountName());
			mTvAccount.setText(record.getBankAccountNo());
			mTvBankFee.setText(Utils.format(record.getBankFee()
				.replace("-", ""), digits) + " " + currency);
			mTvWithdrawFee.setText(Utils.format(record.getTransactionFee()
				.replace("-", ""), digits) + " " + currency);
			mTvRemark.setText(record.getRemark());
			mTvTime.setText(record.getTime());
			mTvStatus.setText(AppUtils.getStatus(status));
			mTvStatus.setTextColor(ColorUtil.getStatusColor(status));
		}
	}
}
