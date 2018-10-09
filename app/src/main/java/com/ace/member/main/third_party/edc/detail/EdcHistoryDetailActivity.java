package com.ace.member.main.third_party.edc.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.EdcBill;
import com.ace.member.main.image_detail.ImageDetailActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.ColorUtil;
import com.ace.member.utils.M;
import com.bumptech.glide.Glide;
import com.og.utils.Utils;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class EdcHistoryDetailActivity extends BaseActivity implements EdcHistoryDetailContract.View {

	@Inject
	EdcHistoryDetailPresenter mPresenter;
	@BindView(R.id.tv_amount)
	TextView mTvAmount;
	@BindView(R.id.tv_bill_amount)
	TextView mTvBillAmount;
	@BindView(R.id.tv_fee)
	TextView mTvFee;
	@BindView(R.id.tv_type)
	TextView mTvType;
	@BindView(R.id.tv_number)
	TextView mTvNumber;
	@BindView(R.id.tv_phone)
	TextView mTvPhone;
	@BindView(R.id.tv_remark)
	TextView mTvRemark;
	@BindView(R.id.tv_time)
	TextView mTvTime;
	@BindView(R.id.tv_status)
	TextView mTvStatus;
	@BindView(R.id.iv_image)
	AppCompatImageView mIvImage;
	@BindView(R.id.btn_done)
	Button mBtnDone;
	private int mId;
	private EdcBill mBill;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerEdcHistoryDetailComponent.builder()
			.edcHistoryDetailPresenterModule(new EdcHistoryDetailPresenterModule(this, this))
			.build()
			.inject(this);
		mId = getIntent().getIntExtra("id", 0);
		init();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_edc_history_detail;
	}

	private void init() {
		ToolBarConfig.builder(this, null).setTvTitleRes(R.string.detail)
			.build();
		mBtnDone.setVisibility(getIntent().getBooleanExtra("showConfirm", false) ? View.VISIBLE : View.GONE);
		mPresenter.start(mId);
	}

	@OnClick({R.id.iv_image, R.id.btn_done})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.iv_image:
				toImageDetail();
				break;
			case R.id.btn_done:
				finish();
				break;
		}
	}

	@Override
	public void setBill(EdcBill bill) {
		mBill = bill;
		mTvType.setText(AppUtils.getEdcWsaTypeArr(AppGlobal.PAYMENT_TYPE_1_EDC)
			.get(bill.getType()));
		mTvAmount.setText(Utils.format(bill.getTotal()) + " " + bill.getCurrency());
		mTvBillAmount.setText(Utils.format(bill.getAmount()) + " " + bill.getCurrency());
		mTvFee.setText(Utils.format(bill.getFee()) + " " + bill.getCurrency());
		mTvTime.setText(bill.getTime());
		mTvNumber.setText(bill.getNumber());
		mTvPhone.setText(bill.getPhone());
		mTvRemark.setText(bill.getRemark());
		mTvStatus.setText(AppUtils.getStatus(bill.getStatus()));
		mTvStatus.setTextColor(ColorUtil.getStatusColor(bill.getStatus()));
		Glide.with(this)
			.load(bill.getRealThumbnailFileName())
			.placeholder(R.drawable.ic_image)
			.into(mIvImage);
	}


	public void toImageDetail() {
		ArrayList<String> list = new ArrayList<>(1);
		list.add(mBill.getRealNormalFileName());
		Intent intent = new Intent(this, ImageDetailActivity.class);
		intent.putStringArrayListExtra(M.ImagePicker.IMAGES, list);
		intent.putExtra(M.ImagePicker.CURRENT, 1);
		ActivityOptionsCompat options = ActivityOptionsCompat.makeScaleUpAnimation(mIvImage, 0, 0, mIvImage.getWidth(), mIvImage.getHeight());
		startActivity(intent, options.toBundle());
	}
}
