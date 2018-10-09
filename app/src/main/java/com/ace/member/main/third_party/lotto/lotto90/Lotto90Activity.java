package com.ace.member.main.third_party.lotto.lotto90;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.LottoBaseActivity;
import com.ace.member.main.third_party.lotto.lotto90.multiple.Lotto90MultipleActivity;
import com.ace.member.main.third_party.lotto.lotto90.single.Lotto90SingleActivity;
import com.ace.member.main.third_party.lotto.lotto90.special.Lotto90SpecialActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.M;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import javax.inject.Inject;

import butterknife.BindView;


public class Lotto90Activity extends LottoBaseActivity implements Lotto90Contract.View, View.OnClickListener {

	@Inject
	Lotto90Presenter mPresenter;

	@BindView(R.id.tv_outstanding_amount)
	TextView mTvOutstandingAmount;

	@BindView(R.id.tv_balance)
	TextView mTvBalance;
	@BindView(R.id.menu_single)
	LinearLayout mLlSingle;
	@BindView(R.id.menu_multiple)
	LinearLayout mLlMultiple;
	@BindView(R.id.menu_special)
	LinearLayout mLlSpecial;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DaggerLotto90Component.builder()
			.lotto90PresenterModule(new Lotto90PresenterModule(this, this))
			.build()
			.inject(this);

		initActivity();
	}

	@Override
	protected void onStart() {
		super.onStart();
		mPresenter.getAmount(M.LottoMarket.LOTTO_90_700_MARKET_ID);
	}

	@Override
	protected void initActivity() {
		ToolBarConfig.builder(this, null)
			.setTvTitleRes(R.string.lotto_90)
			.setBackgroundRes(R.color.clr_lotto_head)
			.build();

		mLlSingle.setOnClickListener(this);
		mLlMultiple.setOnClickListener(this);
		mLlSpecial.setOnClickListener(this);
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_lotto90;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.menu_single:
				startActivity(new Intent(this, Lotto90SingleActivity.class));
				break;
			case R.id.menu_multiple:
				startActivity(new Intent(this, Lotto90MultipleActivity.class));
				break;
			case R.id.menu_special:
				startActivity(new Intent(this, Lotto90SpecialActivity.class));
				break;
		}
	}

	@Override
	public void setOutstanding(double outstanding) {
		try {
			mTvOutstandingAmount.setText(Utils.format(outstanding, 2) + " " + AppGlobal.USD);
			mTvBalance.setText(Utils.format(AppUtils.getBalance(AppGlobal.USD), 2) + " " + AppGlobal.USD);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}
}
