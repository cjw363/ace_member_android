package com.ace.member.main.home.top_up.order_detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.TopUpOrder;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.ColorUtil;
import com.ace.member.utils.PhoneCompanyUtil;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import javax.inject.Inject;

import butterknife.BindView;

public class TopUpOrderDetailActivity extends BaseActivity implements TopUpOrderDetailContract.View {
	@Inject
	TopUpOrderDetailPresenter mPresenter;
	@BindView(R.id.iv_phone_company)
	AppCompatImageView mIvPhoneCompany;
	@BindView(R.id.tv_company_name)
	TextView mTvCompanyName;
	@BindView(R.id.tv_face_value)
	TextView mTvFaceValue;
	@BindView(R.id.tv_price)
	TextView mTvPrice;
	@BindView(R.id.ll_phone_number)
	LinearLayout mLlPhoneNumber;
	@BindView(R.id.tv_phone_number)
	TextView mTvPhoneNumber;
	@BindView(R.id.tv_top_up_way)
	TextView mTvTopUpWay;
	@BindView(R.id.tv_pincode2)
	TextView mTvPincode2;
	@BindView(R.id.tv_time)
	TextView mTvTime;
	@BindView(R.id.tv_sn)
	TextView mTvSn;
	@BindView(R.id.tv_status)
	TextView mTvStatus;
	@BindView(R.id.btn_confirm)
	Button mBtnConfirm;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerTopUpOrderDetailComponent.builder()
			.topUpOrderDetailPresenterModule(new TopUpOrderDetailPresenterModule(this, this))
			.build()
			.inject(this);
		init();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_top_up_order_detail;
	}

	private void init() {
		int topUpOrderId = getIntent().getIntExtra("id", 0);
		boolean showConfirm = getIntent().getBooleanExtra("showConfirm", false);
		mPresenter.getTopUpOrder(topUpOrderId);
		ToolBarConfig.builder(this, null).setTvTitleRes(R.string.order_detail).build();
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
	public void setTopUpOrder(TopUpOrder order) {
		try {
			if (order != null) {
				String currency=order.getCurrency();
				if (!TextUtils.isEmpty(order.getPhoneCompanyName()))
					mIvPhoneCompany.setImageResource(PhoneCompanyUtil.getPhoneCompanyResourceByName(order.getPhoneCompanyName()));
				mTvCompanyName.setText(order.getPhoneCompanyName());
				String tvFaceValue = currency + " " + order.getFaceValue();
				mTvFaceValue.setText(tvFaceValue);
				if (order.getTopUpType() == AppGlobal.TOP_UP_1_SHOW_PINCODE) {
					mLlPhoneNumber.setVisibility(View.GONE);
				} else {
					mLlPhoneNumber.setVisibility(View.VISIBLE);
					mTvPhoneNumber.setText(order.getPhone());
				}
				if (order.getTopUpType()!=AppGlobal.TOP_UP_1_SHOW_PINCODE) {
					mTvPincode2.setTextIsSelectable(false);
					mTvPincode2.setText(order.getPincode2());
				} else {
					mTvPincode2.setTextIsSelectable(true);
					mTvPincode2.setText(AppUtils.getCallSpan(this, order.getPincode2()));
					mTvPincode2.setMovementMethod(LinkMovementMethod.getInstance());
				}
				double price = order.getPrice();
				String tvPrice = Utils.format(price == 0 ? 0 : Math.abs(price), currency) + " " + currency;
				mTvPrice.setText(tvPrice);
				mTvTime.setText(order.getTime());
				mTvTopUpWay.setText(AppUtils.getTopUpWay(order.getTopUpType()));
				mTvSn.setText(order.getSn());
				mTvStatus.setTextColor(ColorUtil.getStatusColor(AppGlobal.STATUS_3_COMPLETED));
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}
}
