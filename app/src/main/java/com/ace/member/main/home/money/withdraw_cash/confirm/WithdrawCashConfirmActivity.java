package com.ace.member.main.home.money.withdraw_cash.confirm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.main.home.money.withdraw_cash.WithdrawCashResultDetail;
import com.ace.member.service.WebSocketService;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.PayHelper;
import com.og.http.SocketResponse;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class WithdrawCashConfirmActivity extends BaseActivity implements WithdrawCashConfirmContract.WithdrawCashConfirmView {

	@Inject
	WithdrawCashConfirmPresenter mPresenter;

	@BindView(R.id.tv_currency)
	TextView mTvCurrency;
	@BindView(R.id.tv_amount)
	TextView mTvAmount;
	@BindView(R.id.tv_fee)
	TextView mTvFee;
	@BindView(R.id.time)
	TextView mTvTime;

	private String mWithdrawID;
	private String mCurrency;
	private double mAmount;
	private double mFee;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerWithdrawCashConfirmComponent.builder()
				.withdrawCashConfirmPresenterModule(new WithdrawCashConfirmPresenterModule(this, this))
				.build()
				.inject(this);
//		mSocketHost = Session.socketServers.getCashWithdraw();
		initResultView();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_wihdraw_cash_confirm;
	}

	private void initResultView() {
		initActivity();
		ToolBarConfig.builder(this, null).setTvTitleRes(R.string.withdraw_cash).build();

		try {
			WithdrawCashResultDetail b = (WithdrawCashResultDetail) getIntent().getSerializableExtra("result");
			if (b != null) {
				mCurrency = b.getCurrency();
				mFee = b.getFee();
				mAmount = b.getAmount();
				String time = b.getTime();
				mWithdrawID = b.getId();

				mTvCurrency.setText(mCurrency);
				mTvAmount.setText(Utils.format(mAmount, mCurrency));
				mTvFee.setText(Utils.format(mFee, mCurrency));
				mTvTime.setText(time);
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}

	}

	@Override
	public String getWithdrawID() {
		return mWithdrawID;
	}

	@OnClick({R.id.btn_confirm})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.btn_confirm:
				if (Utils.isFastClick(this)) return;
				PayHelper payHelper = new PayHelper(WithdrawCashConfirmActivity.this);
				payHelper.startPay(new PayHelper.CallBackPart() {
					@Override
					public void paySuccess() {
						if (!AppUtils.checkEnoughMoney(mCurrency, mAmount + mFee)) {
							Utils.showToast(R.string.msg_1709);
							return;
						}
						mPresenter.submit();
					}
				});
				break;
		}
	}


	public void backBroadcastReceiver(int code, String result) {
		mPresenter.requestResult();
	}

	@Override
	public void unregisterSocketReceiver() {
		try {
			unregisterReceiver(mReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void finishSelf() {
		finish();
	}

	public BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				String action = intent.getAction();
				if (WebSocketService.WEB_SOCKET_ACTION.equals(action)) {
					String data = intent.getStringExtra("data");
					SocketResponse response = JsonUtil.jsonToBean(data, SocketResponse.class);
					assert response != null;
					backBroadcastReceiver(response.getCode(), JsonUtil.beanToJson(response.getResult()));
				}
			} catch (Exception e) {
				FileUtils.addErrorLog(e);
			}
		}
	};

	public void initActivity() {
	}

	@Override
	protected void onStart() {
		super.onStart();
		try {
			IntentFilter filter = new IntentFilter(WebSocketService.WEB_SOCKET_ACTION);
			registerReceiver(mReceiver, filter);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			unregisterReceiver(mReceiver);
			mReceiver = null;
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}
}
