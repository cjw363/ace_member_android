package com.ace.member.main.home.money.withdraw_cash;

import android.os.Bundle;

import com.ace.member.R;
import com.ace.member.main.home.money.BaseCashActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.Session;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import javax.inject.Inject;


public class WithdrawCashActivity extends BaseCashActivity implements WithdrawCashContract.WithdrawCashView {

	@Inject
	WithdrawCashPresenter mPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerWithdrawCashComponent.builder().withdrawCashPresenterModule(new WithdrawCashPresenterModule(this, this)).build().inject(this);
		mSocketHost = Session.socketServers.getCashWithdraw();
		initActivity();
	}

	public void initActivity() {
		super.initActivity();
		try {
			ToolBarConfig.Builder builder = new ToolBarConfig.Builder(this, null);
			builder.setTvTitleRes(R.string.withdraw_cash).setBackgroundRes(R.color.clr_money_withdraw_cash).build();
			mImgFunction.setImageResource(R.drawable.ic_withdraw_cash);
			mTvFunction.setText(Utils.getString(R.string.withdraw_cash));
			mTvFunction.setTextColor(Utils.getColor(R.color.clr_money_withdraw_cash));
			mLlContent.setBackgroundColor(Utils.getColor(R.color.clr_money_withdraw_cash));
			mTvTap.setText(Utils.getString(R.string.tap_to_view_withdraw_code));
			setCode(TRANSFER_13_MEMBER_WITHDRAW_CASH + "|" + Session.user.getPhone());
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	public void backBroadcastReceiver(int code,String result) {
		mPresenter.requestResult();
	}
}
