package com.ace.member.main.detail.deposit_via_agent_detail;

import android.os.Bundle;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.CashApplication;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.ColorUtil;
import com.ace.member.view.MoneyTextView;

import javax.inject.Inject;

import butterknife.BindView;

public class DepositViaAgentDetailActivity extends BaseActivity implements DepositViaAgentDetailContract.MemberDepositDetailView {

	@Inject
	DepositViaAgentDetailPresenter mPresenter;

	@BindView(R.id.tv_amount)
	MoneyTextView mTvAmount;
	@BindView(R.id.tv_ticket_id)
	TextView mTvTicketID;
	@BindView(R.id.tv_time)
	TextView mTvTime;
	@BindView(R.id.tv_currency)
	TextView mTvCurrency;
	@BindView(R.id.tv_detail_amount)
	MoneyTextView mTvDetailAmount;
	@BindView(R.id.tv_source)
	TextView mTvSource;
	@BindView(R.id.tv_status)
	TextView mTvStatus;
	@BindView(R.id.tv_remark)
	TextView mTvRemark;

	private int mId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerDepositViaAgentDetailComponent.builder()
			.depositViaAgentDetailModule(new DepositViaAgentDetailModule(this, this))
			.build()
			.inject(this);
		initActivity();
		getData();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_deposit_via_agent_detail;
	}

	public void initActivity() {
		ToolBarConfig.builder(this, null).setTvTitleRes(R.string.deposit_via_agent).build();
		mId = getIntent().getIntExtra("id", 0);
	}

	public void getData() {
		mPresenter.getRecentDetail(mId);
	}

	@Override
	public void setRecentDetail(CashApplication data) {
		if (data != null) {
			String amount = data.getAmount();
			String currency = data.getCurrency();
			String source = AppUtils.getUserType(data.getSourceType()) + " " + data.getSourcePhone();
			int status = data.getStatus();

			mTvAmount.setMoney(currency, amount);
			mTvTicketID.setText(String.valueOf(mId));
			mTvTime.setText(data.getTime());
			mTvCurrency.setText(currency);
			mTvDetailAmount.setMoney(currency, amount);
			mTvSource.setText(source);
			mTvStatus.setText(AppUtils.getStatus(status));
			mTvStatus.setTextColor(ColorUtil.getStatusColor(status));
			mTvRemark.setText(data.getRemark());
		}
	}
}
