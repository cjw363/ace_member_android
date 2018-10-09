package com.ace.member.main.home.receive_to_acct.detail;

import android.os.Bundle;
import android.widget.TextView;
import butterknife.BindView;

import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.main.home.receive_to_acct.R2ADataBeen;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.ColorUtil;
import com.og.utils.FileUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;

public class R2ADetailActivity extends BaseActivity implements R2ADetailContract.R2ADetailView {

	@Inject
	R2ADetailPresenter mPresenter;

	@BindView(R.id.tv_fc_title)
	TextView mTvTitle;
	@BindView(R.id.tv_r2a_amount)
	TextView mTvTotalAmount;
	@BindView(R.id.tv_r2a_sender)
	TextView mTvSender;
	@BindView(R.id.tv_r2a_source)
	TextView mTvSource;
	@BindView(R.id.tv_r2a_accept_code)
	TextView mTvAcceptCode;
	@BindView(R.id.tv_r2a_remark)
	TextView mTvRemark;
	@BindView(R.id.tv_r2a_time)
	TextView mTvTime;
	@BindView(R.id.tv_r2a_status)
	TextView mTvStatus;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerR2ADetailComponent.builder()
			.r2ADetailPresenterModule(new R2ADetailPresenterModule(this, this))
			.build()
			.inject(this);
		init();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_r2a_detail;
	}

	private void init() {
//		String id = getIntent().getStringExtra("id");
		int id = getIntent().getIntExtra("id", 0);

		mTvTitle.setText(R.string.receive_to_acct);
		mPresenter.getRecentDetail(id);
		ToolBarConfig.builder(this, null).setTvTitleRes(R.string.detail).build();
	}

	@Override
	public void setRecentDetail(R2ADataBeen been) {
		if (been != null) {
			try {
				String source = been.getSourceName();
				mTvSource.setText(source);
				mTvTime.setText(been.getTime());
				mTvSender.setText(been.getSender());
				mTvTotalAmount.setText(AppUtils.getAmountCurrency(been.getCurrency(),been.getAmount()));
				mTvAcceptCode.setText(been.getAcceptCode());
				int status = been.getStatus();
				mTvStatus.setText(AppUtils.getStatus(status));
				mTvStatus.setTextColor(ColorUtil.getStatusColor(status));
			} catch (Exception e) {
				FileUtils.addErrorLog(e);
			}
		}
	}
}
