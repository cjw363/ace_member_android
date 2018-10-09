package com.ace.member.main.third_party.bill_payment.history_detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import javax.inject.Inject;

import butterknife.BindView;

public class BillPaymentHistoryDetailActivity extends BaseActivity implements BillPaymentHistoryDetailContract.view {
	@Inject
	BillPaymentHistoryDetailPresenter mPresenter;

	@BindView(R.id.iv_biller_company)
	AppCompatImageView mIvBillerCompany;
	@BindView(R.id.tv_biller_company_name)
	TextView mTvBillerCompanyName;
	//	@BindView(R.id.tv_type)
	//	TextView mTvType;
	@BindView(R.id.tv_amount)
	TextView mTvAmount;
	@BindView(R.id.tv_fee)
	TextView mTvFee;
	@BindView(R.id.tv_remark)
	TextView mTvRemark;
	@BindView(R.id.tv_time)
	TextView mTvTime;
	@BindView(R.id.tv_total_amount)
	TextView mTvTotalAmount;
	@BindView(R.id.tv_bill_id)
	TextView mTvBillID;
	@BindView(R.id.tv_bill_id_title)
	TextView mTvBillIDTitle;
	@BindView(R.id.tv_phone)
	TextView mTvPhone;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerBillPaymentHistoryDetailComponent.builder()
			.billPaymentHistoryDetailPresenterModule(new BillPaymentHistoryDetailPresenterModule(this, this))
			.build()
			.inject(this);
		initActivity();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_bill_payment_detail;
	}

	protected void initActivity() {
		ToolBarConfig.builder(this, null).setTvTitleRes(R.string.detail).build();
		int id = getIntent().getIntExtra("id", 0);
		mPresenter.getDetailData(id);
	}

	@Override
	public void initDetail(BillPaymentDetailBean bean) {
		try {
			String code = bean.getCode();
			mIvBillerCompany.setImageResource(R.drawable.ic_partner_common);
			String str = code + " " + bean.getName();
			if (!TextUtils.isEmpty(bean.getTitle())) str += " (" + bean.getTitle() + ")";
			mTvBillerCompanyName.setText(str);
			double amount = bean.getAmount();
			double fee = bean.getFee();
//			double total = amount + fee;
			String currency = bean.getCurrency();
			mTvTotalAmount.setText(Utils.format(amount, currency) + " " + currency);
			mTvBillIDTitle.setText(bean.getBillIDTitle());
			mTvPhone.setText(bean.getPhone());
			mTvBillID.setText(bean.getBillID());
			mTvAmount.setText(Utils.format(amount, currency) + " " + currency);
			mTvFee.setText(Utils.format(fee, currency) + " " + currency);
			String remark = bean.getRemark();
			mTvRemark.setText(remark);
			mTvTime.setText(bean.getTime());
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}
}
