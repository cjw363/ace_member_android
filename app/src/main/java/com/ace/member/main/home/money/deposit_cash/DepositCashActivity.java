package com.ace.member.main.home.money.deposit_cash;

import android.os.Bundle;

import com.ace.member.R;
import com.ace.member.main.home.money.BaseCashActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.M;
import com.ace.member.utils.Session;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import javax.inject.Inject;

public class DepositCashActivity extends BaseCashActivity implements DepositCashContract.DepositCashView {

	@Inject
	DepositCashPresenter mPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerDepositCashComponent.builder()
				.depositCashPresenterModule(new DepositCashPresenterModule(this, this))
				.build()
				.inject(this);
		mSocketHost = Session.socketServers.getCashDeposit();
		initActivity();
	}

	public void backBroadcastReceiver(int code, String result) {
		if (code == M.SocketCode.SOCKET_CODE_2_DEPOSIT) mPresenter.requestResult();
	}

	public void initActivity() {
		super.initActivity();
		try {
			ToolBarConfig.Builder builder = new ToolBarConfig.Builder(this, null);
			builder.setTvTitleRes(R.string.deposit_cash)
					.setBackgroundRes(R.color.clr_money_deposit_cash)
					.build();
			mImgFunction.setImageResource(R.drawable.ic_deposit_cash);
			mTvFunction.setText(Utils.getString(R.string.deposit_cash));
			mTvTap.setText(Utils.getString(R.string.tap_to_view_deposit_code));
			mPresenter.getTime();
			setCode(TRANSFER_12_MEMBER_DEPOSIT_CASH + "|" + Session.user.getPhone());
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

}
