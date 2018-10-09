package com.ace.member.main.me.statement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.adapter.StatementAdapter;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.Currency;
import com.ace.member.popup_window.DatePickerPopWindow;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.Session;
import com.ace.member.view.MoneyTextView;
import com.og.LibGlobal;
import com.og.component.CustomSlidingRefreshListView;
import com.og.utils.DateUtils;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;


public class StatementActivity extends BaseActivity implements StatementContract.StatementView, CustomSlidingRefreshListView.IXListViewListener {

	@Inject
	StatementPresenter mPresenter;

	@BindView(R.id.tv_beginning)
	MoneyTextView mTvBeginning;
	@BindView(R.id.lv_statement)
	CustomSlidingRefreshListView mLvStatement;
	@BindView(R.id.iv_menu)
	AppCompatImageView mIvMenu;
	@BindView(R.id.tv_condition)
	TextView mTvCondition;
	@BindView(R.id.rl_condition)
	RelativeLayout mRlCondition;

	private StatementAdapter mAdapter;
	private String mDateStart;
	private String mDateEnd;
	private String mSelectedCurrency = "";
	private ArrayList<String> mCurrencyList;
	private DatePickerPopWindow mDatePickerPopWindow;
	private int mPage = 1;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerStatementComponent.builder()
				.statementPresenterModule(new StatementPresenterModule(this, this))
				.build()
				.inject(this);
		initActivity();
		initListener();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_statement;
	}

	@Override
	protected void initActivity() {
		ToolBarConfig.builder(this, null).setTvTitleRes(R.string.statement)
				.setIvMenuRes(R.drawable.ic_filter)
				.setMenuType(ToolBarConfig.MenuType.MENU_IMAGE)
				.setEnableMenu(true)
				.build();
		mLvStatement.setPullLoadEnable(false);
		mLvStatement.setXListViewListener(this);
		mLvStatement.setRefreshTime(DateUtils.getFormatDataTime());
		mLvStatement.setPullRefreshEnable(false);
		mLvStatement.hideLoadMore();
		mLvStatement.setPullRefreshEnable(true);
		setCurrencyList();
		mDateStart = DateUtils.getToday();//默认展示当天数据
		mDateEnd = DateUtils.getToday();
		mPage = 1;
		mPresenter.getStatement(mDateStart, mDateEnd, mSelectedCurrency, mPage);
		setTvCondition();

		mDatePickerPopWindow = new DatePickerPopWindow.Builder(StatementActivity.this, mDateStart, mDateEnd, mSelectedCurrency, new DatePickerPopWindow.OnDatePickedListener() {
			@Override
			public void onDatePickCompleted(String dateStart, String dateEnd, String selectedCurrency) {
				mDateStart = dateStart;
				mDateEnd = dateEnd;
				if ("All".equals(selectedCurrency)) {
					selectedCurrency = "";
				}
				mSelectedCurrency = selectedCurrency;
				mPage = 1;
				mPresenter.getStatement(mDateStart, mDateEnd, selectedCurrency, mPage);
				setTvCondition();
			}
		}).minYear(LibGlobal.MIN_YEAR).setSearchList(mCurrencyList).build();
	}

	private void setTvCondition() {
		String currency;
		if (TextUtils.isEmpty(mSelectedCurrency)) {
			currency =getResources().getString(R.string.all_currency);
		} else {
			currency = mSelectedCurrency;
		}
		mTvCondition.setText(currency + " / " + mDateStart + " " + Utils.getString(R.string.to) + " " + mDateEnd);
	}

	private void initListener() {
		mIvMenu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mDatePickerPopWindow.showPopWin(StatementActivity.this);
			}
		});

		mRlCondition.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mDatePickerPopWindow.showPopWin(StatementActivity.this);
			}
		});
	}

	@Override
	public void setBeginning(String currency, String beginning) {
		mTvBeginning.setMoney(currency, beginning);
	}

	private void setCurrencyList() {
		if (mCurrencyList == null && Session.currencyList != null) {
			mCurrencyList = new ArrayList<>();
			mCurrencyList.add("All");
			int len = Session.currencyList.size();
			for (int i = 0; i < len; i++) {
				try {
					Currency obj = Session.currencyList.get(i);
					mCurrencyList.add(obj.getCurrency());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void showStatementData(int nextPage, boolean canLoadMore) {
		try {
			mPage = nextPage;
			mLvStatement.setRefreshTime(DateUtils.getFormatDataTime());
			int size = mPresenter.getStatementData().size();
			if (size > 0 && canLoadMore) {
				mLvStatement.setPullLoadEnable(true);
				mLvStatement.showLoadMore();
			} else {
				mLvStatement.setPullLoadEnable(false);
				mLvStatement.hideLoadMore();
			}

			mAdapter = new StatementAdapter(mPresenter.getStatementData(), this);
			mLvStatement.setAdapter(mAdapter);
			onLoad();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
	}

	@Override
	public void showNextStatementData(int nextPage) {
		try {
			if (nextPage == 0) {
				mLvStatement.hideLoadMore();
				mLvStatement.setPullLoadEnable(false);
			}
			mPage = nextPage;
			mAdapter.notifyDataSetChanged();
			onLoad();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
	}

	@Override
	public void onRefresh() {
		mPage = 1;
		mPresenter.getStatement(mDateStart, mDateEnd, mSelectedCurrency, mPage);
	}

	@Override
	public void onLoadMore() {
		if (mPage > 0) {
			mPresenter.getStatement(mDateStart, mDateEnd, mSelectedCurrency, mPage);
		}
	}

	private void onLoad() {
		mLvStatement.stopRefresh();
		mLvStatement.stopLoadMore();
	}
}
