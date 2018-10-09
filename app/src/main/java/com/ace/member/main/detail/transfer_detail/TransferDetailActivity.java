package com.ace.member.main.detail.transfer_detail;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.TransferRecent;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.ColorUtil;
import com.ace.member.utils.TransactionType;
import com.ace.member.view.MoneyTextView;
import com.og.utils.Utils;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;

public class TransferDetailActivity extends BaseActivity implements TransferDetailContract.TransferDetailView {

	@Inject
	TransferDetailPresenter mPresenter;

	@BindView(R.id.tv_amount)
	MoneyTextView mTvAmount;
	@BindView(R.id.tv_currency)
	TextView mTvCurrency;
	@BindView(R.id.tv_transfer_amount)
	MoneyTextView mTvTransferAmount;
	@BindView(R.id.tv_transfer_fee)
	MoneyTextView mTvTransferFee;
	@BindView(R.id.tv_sender)
	TextView mTvSender;
	@BindView(R.id.tv_receiver)
	TextView mTvReceiver;
	@BindView(R.id.tv_accept_code)
	TextView mTvAcceptCode;
	@BindView(R.id.rl_remark)
	RelativeLayout mRlRemark;
	@BindView(R.id.tv_remark)
	TextView mTvRemark;
	@BindView(R.id.tv_time)
	TextView mTvTime;
	@BindView(R.id.tv_status)
	TextView mTvStatus;
	@BindView(R.id.tv_type)
	TextView mTvType;
	@BindView(R.id.ll_accept_code)
	LinearLayout mLlAcceptCode;
	@BindView(R.id.ll_target)
	LinearLayout mLlTarget;
	@BindView(R.id.tv_target_phone)
	TextView mTvTargetPhone;
	@BindView(R.id.iv_icon)
	AppCompatImageView mIvIcon;
	@BindView(R.id.tv_sub_type)
	TextView mTvSubType;
	@BindView(R.id.ll_transfer_fee)
	LinearLayout mLlTransferFee;

	private int mSourceFlag;
	private int mTransferRecentID;

	public static final int SOURCE_1_MEMBER = 1;
	public static final int SOURCE_2_NON_MEMBER = 2;
	private int mSubType;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerTransferDetailComponent.builder()
			.transferDetailModule(new TransferDetailModule(this, this))
			.build()
			.inject(this);
		initData();
		initActivity();
		getData();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_transfer_detail;
	}

	private void initData() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			mTransferRecentID = bundle.getInt("id");
			mSubType = bundle.getInt("sub_type");
			int mainSubType = bundle.getInt("main_sub_type");
			mSourceFlag = bundle.getInt("source",0);
			if (mSourceFlag == 0){
				if (mainSubType == TransactionType.TRANSACTION_SUB_TYPE_221_TRANSFER_OUT || mSubType == TransactionType.TRANSACTION_SUB_TYPE_222_TRANSFER_IN || mSubType == TransactionType.TRANSACTION_SUB_TYPE_221_TRANSFER_OUT) {
					mSourceFlag = SOURCE_1_MEMBER;
				} else if (mainSubType == TransactionType.TRANSACTION_SUB_TYPE_223_TRANSFER_OUT_A2C || mSubType == TransactionType.TRANSACTION_SUB_TYPE_223_TRANSFER_OUT_A2C) {
					mSourceFlag = SOURCE_2_NON_MEMBER;
				}
			}
		}
	}

	@Override
	protected void initActivity() {
		ToolBarConfig.builder(this, null).setTvTitleRes(R.string.transfer_detail).build();
		if (mSourceFlag == SOURCE_1_MEMBER) {
			mLlAcceptCode.setVisibility(View.GONE);
			mLlTarget.setVisibility(View.GONE);
			mTvType.setText(R.string.to_member);
		} else {
			mLlAcceptCode.setVisibility(View.VISIBLE);
			mLlTarget.setVisibility(View.VISIBLE);
			mTvType.setText(R.string.to_non_member);
		}

		if (mSubType > 0){
			mIvIcon.setImageResource(TransactionType.getSubTypeIcon(mSubType));
			mTvSubType.setText(TransactionType.getSubTypeName(this, mSubType));
		}
	}

	public void getData() {
		if (mTransferRecentID == 0 || mSourceFlag == 0) {
			Utils.showToast(R.string.error);
			return;
		}
		HashMap<String, String> params = new HashMap<>();
		params.put("id", mTransferRecentID + "");
		params.put("source", mSourceFlag + "");
		if (mSubType == TransactionType.TRANSACTION_SUB_TYPE_222_TRANSFER_IN) {
			mPresenter.getTransferInData(params);
		} else if (mSubType == TransactionType.TRANSACTION_SUB_TYPE_225_TRANSFER_FEE) {
			mPresenter.getTransferFeeData(params);
		} else {
			mPresenter.getTransferAmountData(params);
		}
	}

	@Override
	public void setAmountDetail(TransferRecent data) {
		String currency = data.getCurrency();
		double amount = data.getAmount();
		double fee = data.getFee();
		String senderName = data.getSourceName();
		if (!TextUtils.isEmpty(senderName)){
			senderName = "[" + senderName + "] ";
		}else {
			senderName = "";
		}
		String receiverName = data.getTargetName();
		if (!TextUtils.isEmpty(receiverName)){
			receiverName = "[" + receiverName + "] ";
		}else {
			receiverName = "";
		}
		String sender = senderName + data.getSourcePhone();
		String receiver = receiverName + data.getTargetPhone();
		mTvAmount.setMoney(currency, String.valueOf(amount));
		mTvCurrency.setText(currency);
		mTvTransferAmount.setMoney(currency, String.valueOf(amount));
		mTvTransferFee.setMoney(currency, String.valueOf(fee));
		mTvSender.setText(sender);
		mTvReceiver.setText(receiver);
		mTvAcceptCode.setText(data.getAcceptCode());
		if (!TextUtils.isEmpty(data.getTargetPhone())) {
			String targetPhone = String.format(Utils.getString(R.string.bracket),Utils.getString(R.string.agent)) + " " + data.getTargetPhone();
			mTvTargetPhone.setText(targetPhone);
		} else {
			mLlTarget.setVisibility(View.GONE);
		}
		mTvTime.setText(data.getTime());
		int status = data.getStatus();
		mTvStatus.setText(AppUtils.getStatus(status));
		mTvStatus.setTextColor(ColorUtil.getStatusColor(status));
		if (!TextUtils.isEmpty(data.getRemark())) {
			mTvRemark.setText(data.getRemark());
		} else {
			mRlRemark.setVisibility(View.GONE);
		}
	}

	@Override
	public void setFeeDetail(TransferRecent data) {
		String currency = data.getCurrency();
		double fee = data.getFee();
		double amount = data.getAmount();
		String senderName = data.getSourceName();
		if (!TextUtils.isEmpty(senderName)){
			senderName = "[" + senderName + "] ";
		}else {
			senderName = "";
		}
		String receiverName = data.getTargetName();
		if (!TextUtils.isEmpty(receiverName)){
			receiverName = "[" + receiverName + "] ";
		}else {
			receiverName = "";
		}
		String sender = senderName + data.getSourcePhone();
		String receiver = receiverName + data.getTargetPhone();
		mTvAmount.setMoney(currency, String.valueOf(fee));
		mTvCurrency.setText(currency);
		mTvTransferAmount.setMoney(currency, String.valueOf(amount));
		mTvTransferFee.setMoney(currency, String.valueOf(fee));
		mTvSender.setText(sender);
		mTvReceiver.setText(receiver);
		mTvAcceptCode.setText(data.getAcceptCode());
		if (!TextUtils.isEmpty(data.getTargetPhone())) {
			String targetPhone = String.format(Utils.getString(R.string.bracket),Utils.getString(R.string.agent)) + " " + data.getTargetPhone();
			mTvTargetPhone.setText(targetPhone);
		} else {
			mLlTarget.setVisibility(View.GONE);
		}
		mTvTime.setText(data.getTime());
		int status = data.getStatus();
		mTvStatus.setText(AppUtils.getStatus(status));
		mTvStatus.setTextColor(ColorUtil.getStatusColor(status));
		if (!TextUtils.isEmpty(data.getRemark())) {
			mTvRemark.setText(data.getRemark());
		} else {
			mRlRemark.setVisibility(View.GONE);
		}
	}

	@Override
	public void setInDetail(TransferRecent data) {
		String currency = data.getCurrency();
		double amount = data.getAmount();
		String senderName = data.getSourceName();
		if (!TextUtils.isEmpty(senderName)){
			senderName = "[" + senderName + "] ";
		}else {
			senderName = "";
		}
		String receiverName = data.getTargetName();
		if (!TextUtils.isEmpty(receiverName)){
			receiverName = "[" + receiverName + "] ";
		}else {
			receiverName = "";
		}
		String sender = senderName + data.getSourcePhone();
		String receiver = receiverName + data.getTargetPhone();
		mTvAmount.setMoney(currency, String.valueOf(amount));
		mTvCurrency.setText(currency);
		mTvTransferAmount.setMoney(currency, String.valueOf(amount));
		mLlTransferFee.setVisibility(View.GONE);
		mTvSender.setText(sender);
		mTvReceiver.setText(receiver);
		mTvAcceptCode.setText(data.getAcceptCode());
		if (!TextUtils.isEmpty(data.getTargetPhone())) {
			String targetPhone = String.format(Utils.getString(R.string.bracket),Utils.getString(R.string.agent)) + " " + data.getTargetPhone();
			mTvTargetPhone.setText(targetPhone);
		} else {
			mLlTarget.setVisibility(View.GONE);
		}
		mTvTime.setText(data.getTime());
		int status = data.getStatus();
		mTvStatus.setText(AppUtils.getStatus(status));
		mTvStatus.setTextColor(ColorUtil.getStatusColor(status));
		if (!TextUtils.isEmpty(data.getRemark())) {
			mTvRemark.setText(data.getRemark());
		} else {
			mRlRemark.setVisibility(View.GONE);
		}
	}
}
