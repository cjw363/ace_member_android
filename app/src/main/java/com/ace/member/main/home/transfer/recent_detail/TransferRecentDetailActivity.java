package com.ace.member.main.home.transfer.recent_detail;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.TransferRecent;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.ColorUtil;
import com.ace.member.utils.TransactionType;
import com.ace.member.view.MoneyTextView;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import javax.annotation.Nullable;
import javax.inject.Inject;

import butterknife.BindView;

public class TransferRecentDetailActivity extends BaseActivity implements TransferRecentDetailContract.TransferRecentDetailView {

	@Inject
	TransferRecentDetailPresenter mPresenter;
	@BindView(R.id.tv_fc_title)
	TextView mTvFcTitle;
	@BindView(R.id.tv_amount)
	MoneyTextView mTvAmount;
	@BindView(R.id.tv_sender)
	TextView mTvSender;
	@BindView(R.id.tv_receiver)
	TextView mTvReceiver;
	@BindView(R.id.tv_total_amount)
	MoneyTextView mTvTotalAmount;
	@BindView(R.id.tv_fee)
	MoneyTextView mTvFee;
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

	public static final int SOURCE_1_MEMBER = 1;
	public static final int SOURCE_2_NON_MEMBER = 2;
	public static final int SOURCE_3_PARTNER = 3;
	public static final int SOURCE_4_MERCHANT = 4;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerTransferRecentDetailComponent.builder()
			.transferRecentDetailPresenterModule(new TransferRecentDetailPresenterModule(this, this))
			.build()
			.inject(this);
		init();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_transfer_recent_detail;
	}

	private void init() {
		try {
			int sourceFlag = getIntent().getIntExtra("source", 0);
			int transferRecentId = getIntent().getIntExtra("id", 0);
			int subType = getIntent().getIntExtra("sub_type", 0);
			if (subType == TransactionType.TRANSACTION_SUB_TYPE_221_TRANSFER_OUT) {
				sourceFlag = SOURCE_1_MEMBER;
			} else if (subType == TransactionType.TRANSACTION_SUB_TYPE_223_TRANSFER_OUT_A2C) {
				sourceFlag = SOURCE_2_NON_MEMBER;
			}
			mPresenter.getRecentDetail(transferRecentId, sourceFlag);
			ToolBarConfig.builder(this, null).setTvTitleRes(R.string.transfer_detail).build();
			if (sourceFlag == SOURCE_1_MEMBER) {
				mLlAcceptCode.setVisibility(View.GONE);
				mLlTarget.setVisibility(View.GONE);
				mTvType.setText(R.string.to_member);
			} else if (sourceFlag == SOURCE_2_NON_MEMBER) {
				mLlAcceptCode.setVisibility(View.VISIBLE);
				mLlTarget.setVisibility(View.VISIBLE);
				mTvType.setText(R.string.to_non_member);
			} else if (sourceFlag == SOURCE_3_PARTNER) {
				mLlAcceptCode.setVisibility(View.GONE);
				mLlTarget.setVisibility(View.GONE);
				mTvType.setText(R.string.to_partner);
			} else if (sourceFlag == SOURCE_4_MERCHANT) {
				mLlAcceptCode.setVisibility(View.GONE);
				mLlTarget.setVisibility(View.GONE);
				mTvType.setText(R.string.to_merchant);
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@Override
	public void setRecentDetail(TransferRecent recent) {
		if (recent != null) {
			try {
				mTvFcTitle.setText(getResources().getString(R.string.transfer));
				String currency = recent.getCurrency();
				double amount = recent.getAmount();
				double fee = recent.getFee();
				String senderName = recent.getSourceName();
				if (!TextUtils.isEmpty(senderName)) {
					senderName = "[" + senderName + "] ";
				} else {
					senderName = "";
				}
				String receiverName = recent.getTargetName();
				if (!TextUtils.isEmpty(receiverName)) {
					receiverName = "[" + receiverName + "] ";
				} else {
					receiverName = "";
				}
				String sender = senderName + recent.getSourcePhone();
				String receiver = receiverName + recent.getTargetPhone();
				mTvTotalAmount.setMoney(currency, String.valueOf(amount));
				mTvAmount.setMoney(currency, String.valueOf(amount));
				mTvFee.setMoney(currency, String.valueOf(fee));

				mTvSender.setText(sender);
				mTvReceiver.setText(receiver);
				mTvAcceptCode.setText(recent.getAcceptCode());
				if (!TextUtils.isEmpty(recent.getTargetPhone())) {
					String targetPhone = String.format(Utils.getString(R.string.bracket),Utils.getString(R.string.agent))+ " " + recent.getTargetPhone();
					mTvTargetPhone.setText(targetPhone);
				} else {
					mLlTarget.setVisibility(View.GONE);
				}
				mTvTime.setText(recent.getTime());
				int status = recent.getStatus();
				mTvStatus.setText(AppUtils.getStatus(status));
				mTvStatus.setTextColor(ColorUtil.getStatusColor(status));
				if (!TextUtils.isEmpty(recent.getRemark())) {
					mTvRemark.setText(recent.getRemark());
				} else {
					mRlRemark.setVisibility(View.GONE);
				}
			} catch (Exception e) {
				e.printStackTrace();
				FileUtils.addErrorLog(e);
			}
		}
	}
}
