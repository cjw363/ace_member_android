package com.ace.member.main.currency;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.BalanceRecord;
import com.ace.member.event.RefreshEvent;
import com.ace.member.main.currency.deposit.DepositActivity;
import com.ace.member.main.currency.flow.FlowActivity;
import com.ace.member.main.detail.deposit_detail.DepositDetailActivity;
import com.ace.member.main.currency.withdraw.WithdrawActivity;
import com.ace.member.main.detail.withdraw_detail.WithdrawDetailActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.og.utils.EventBusUtil;
import com.og.utils.Utils;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class CurrencyActivity extends BaseActivity implements CurrencyContract.View, SwipeRefreshLayout.OnRefreshListener {

	@BindView(R.id.tv_balance)
	TextView mTvBalance;
	@BindView(R.id.lv_recent_list)
	ListView mLvLatestRecord;
	@BindView(R.id.srl)
	SwipeRefreshLayout mSrl;
	@BindView(R.id.scrollView)
	ScrollView mScrollView;
	@BindView(R.id.btn_deposit)
	Button mBtnDeposit;
	@BindView(R.id.btn_withdraw)
	Button mBtnWithdraw;
	@BindView(R.id.ll_record)
	LinearLayout mLlRecord;

	private String mCurrency;
	private String mBalance;
	private LatestRecordAdapter mRecordAdapter;

	@Inject
	CurrencyPresenter mPresenter;

	private boolean mRefresh;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerCurrencyComponent.builder()
			.currencyPresenterModule(new CurrencyPresenterModule(this, this))
			.build()
			.inject(this);
		initData();
		initView();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_currency;
	}

	private void initData() {
		mCurrency = getIntent().getStringExtra("currency");
		mBalance = getIntent().getStringExtra("balance");
	}

	private void initView() {
		 new ToolBarConfig.Builder(this, null).setTvTitleRes(R.string.currency).setEnableMenu(true).setMenuType(ToolBarConfig.MenuType.MENU_IMAGE).setIvMenuRes(R.drawable.ic_history).build();

		mSrl.setColorSchemeColors(Utils.getColor(R.color.colorPrimary));
		mSrl.setOnRefreshListener(this);
		mTvBalance.setText(String.format(Utils.getString(R.string.format_twice),mCurrency,Utils.format(mBalance, mCurrency)));
		mPresenter.start(mCurrency, false);

		mTvBalance.setFocusable(true);
		mTvBalance.setFocusableInTouchMode(true);

		mLvLatestRecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int type = mRecordAdapter.getItemViewType(position);
				if (type == AppGlobal.APPLICATION_TYPE_2_DEPOSIT) {
					Intent intent = new Intent(CurrencyActivity.this, DepositDetailActivity.class);
					intent.putExtra("id", (int) mRecordAdapter.getItemId(position));
					startActivity(intent);
				} else if (type == AppGlobal.APPLICATION_TYPE_3_WITHDRAW) {
					Intent intent = new Intent(CurrencyActivity.this, WithdrawDetailActivity.class);
					intent.putExtra("id", (int) mRecordAdapter.getItemId(position));
					startActivity(intent);
				}
			}
		});
	}

	@OnClick({R.id.btn_deposit, R.id.btn_withdraw,R.id.ll_toolbar_menu})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_deposit:
				mPresenter.deposit();
				break;
			case R.id.btn_withdraw:
				mPresenter.withdraw(mBalance);
				break;
			case R.id.ll_toolbar_menu:
				Intent intent=new Intent(CurrencyActivity.this,FlowActivity.class);
				intent.putExtra("currency",mCurrency);
				startActivity(intent);
				break;
		}
	}

	@Override
	public void showRefreshStatus(boolean isRefreshing) {
		mSrl.setRefreshing(isRefreshing);
	}

	@Override
	public void showRefreshResult(String msg) {
		Utils.showToast(msg);
	}

	@Override
	public void setRecordList(List<BalanceRecord> list) {
		if (list.size() > 0) mLlRecord.setVisibility(View.VISIBLE);
		if (mRecordAdapter == null) {
			mRecordAdapter = new LatestRecordAdapter(list, mCurrency);
			mLvLatestRecord.setAdapter(mRecordAdapter);
		} else {
			mRecordAdapter.setList(list);
		}
	}

	@Override
	public void setBalance(String balance) {
		mBalance = Utils.format(balance, mCurrency);
		mTvBalance.setText(String.format(Utils.getString(R.string.format_twice),mCurrency,mBalance));
		enableWithdraw(true);
	}

	@Override
	public String getBalance() {
		return mBalance;
	}

	@Override
	public void showDeposit() {
		Intent intent = new Intent(CurrencyActivity.this, DepositActivity.class);
		intent.putExtra("currency", mCurrency);
		intent.putExtra("balance", mBalance);
		startActivity(intent);
	}

	@Override
	public void showWithDraw() {
		Intent intent = new Intent(CurrencyActivity.this, WithdrawActivity.class);
		intent.putExtra("currency", mCurrency);
		intent.putExtra("balance", mBalance);
		startActivity(intent);
	}

	@Override
	public void showMsg(String msg) {
		Utils.showToast(msg);
	}

	@Override
	public void enableDeposit(boolean result) {
//		mBtnDeposit.setEnabled(result);
	}

	@Override
	public void enableWithdraw(boolean result) {
//		result = Double.parseDouble(mBalance.replace(",", "")) > 0 && result;
//		mBtnWithdraw.setEnabled(result);
	}

	public void onRefresh() {
		mPresenter.start(mCurrency, true);
	}

	@Override
	protected void onDestroy() {
		EventBusUtil.unRegister(this);
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mRefresh) {
			mPresenter.start(mCurrency, false);
			mRefresh = !mRefresh;
		}
	}

	@Subscribe
	public void onRefreshEvent(RefreshEvent refreshEvent) {
		mRefresh = true;
	}
}
