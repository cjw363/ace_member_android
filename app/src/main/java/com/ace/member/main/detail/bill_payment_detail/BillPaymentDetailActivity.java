package com.ace.member.main.detail.bill_payment_detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.main.third_party.bill_payment.history_detail.BillPaymentDetailBean;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.TransactionType;
import com.ace.member.view.MoneyTextView;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import javax.inject.Inject;

import butterknife.BindView;

public class BillPaymentDetailActivity extends BaseActivity implements BillPaymentDetailContract.view {
	@Inject
	BillPaymentDetailPresenter mPresenter;

	@BindView(R.id.iv_icon)
	AppCompatImageView mIvIcon;
	@BindView(R.id.tv_sub_type)
	TextView mTvType;
	@BindView(R.id.tv_biller_company_name)
	TextView mTvBillerCompanyName;
	@BindView(R.id.tv_amount)
	MoneyTextView mTvAmount;
	@BindView(R.id.tv_remark)
	TextView mTvRemark;
	@BindView(R.id.tv_time)
	TextView mTvTime;
	@BindView(R.id.tv_currency)
	TextView mTvCurrency;
	@BindView(R.id.tv_bill_amount)
	MoneyTextView mTvBillAmount;
	@BindView(R.id.tv_fee)
	MoneyTextView mTvFee;
	@BindView(R.id.tv_bill_id)
	TextView mTvBillID;
	@BindView(R.id.tv_bill_id_title)
	TextView mTvBillIDTitle;
	@BindView(R.id.tv_phone)
	TextView mTvPhone;

	private int mId;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerBillPaymentDetailComponent.builder()
			.billPaymentDetailPresenterModule(new BillPaymentDetailPresenterModule(this, this))
			.build()
			.inject(this);
		initActivity();
		getData();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_bill_payment_detail2;
	}

	protected void initActivity() {
		ToolBarConfig.Builder builder = new ToolBarConfig.Builder(this, null);
		builder.setTvTitleRes(R.string.detail).build();
		mId = getIntent().getIntExtra("id", 0);
		int subType = getIntent().getIntExtra("sub_type", 0);
		mIvIcon.setImageResource(TransactionType.getSubTypeIcon(subType));
		mTvType.setText(TransactionType.getSubTypeName(mContext, subType));
	}

	public void getData() {
		mPresenter.getDetailData(mId);
	}

	@Override
	public void initDetail(BillPaymentDetailBean bean) {
		try {
			String code = bean.getCode();
			String str = code + " " + bean.getName();
			if (!TextUtils.isEmpty(bean.getTitle())) str += " (" + bean.getTitle() + ")";
			mTvBillerCompanyName.setText(str);
			double amount = bean.getAmount();
			double fee = bean.getFee();
			String currency = bean.getCurrency();
			mTvAmount.setMoney(currency, Utils.format(amount, 2));
			mTvBillIDTitle.setText(bean.getBillIDTitle());
			mTvPhone.setText(bean.getPhone());
			mTvBillID.setText(bean.getBillID());
			mTvCurrency.setText(currency);
			mTvBillAmount.setMoney(currency, Utils.format(amount, 2));
			mTvFee.setMoney(currency, Utils.format(fee, 2));
			String remark = bean.getRemark();
			mTvRemark.setText(remark);
			mTvTime.setText(bean.getTime());
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}
}
