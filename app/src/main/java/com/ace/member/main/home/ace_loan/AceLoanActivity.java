package com.ace.member.main.home.ace_loan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.adapter.AceLoanDetailAdapter;
import com.ace.member.bean.ACELoanBean;
import com.ace.member.bean.ACELoanDetailBean;
import com.ace.member.main.home.ace_loan.history.AceLoanHistoryActivity;
import com.ace.member.main.home.ace_loan.loan.MemberLoanAceActivity;
import com.ace.member.main.home.ace_loan.loan_detail.AceLoanDetailActivity;
import com.ace.member.main.home.ace_loan.repay_plan_list.AceRepayPlanListActivity;
import com.ace.member.main.home.ace_loan.return_loan.ReturnLoanAceActivity;
import com.ace.member.popup_window.MenuPopWindow;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.og.utils.FileUtils;
import com.og.utils.ListViewForScrollView;
import com.og.utils.Utils;
import com.roundProgress.RoundProgressBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class AceLoanActivity extends AceLoanBaseActivity implements AceLoanContract.View, SwipeRefreshLayout.OnRefreshListener {

	@Inject
	AceLoanPresenter mPresenter;

	@BindView(R.id.srl_ace_loan)
	SwipeRefreshLayout mRefreshLayout;
	@BindView(R.id.sv_loan)
	ScrollView mScrollView;
	@BindView(R.id.v_round)
	RoundProgressBar mVRound;
	@BindView(R.id.tv_currency)
	TextView mTvCurrency;
	@BindView(R.id.tv_credit)
	TextView mTvCredit;
	@BindView(R.id.tv_credit_currency)
	TextView mTvCreditCurrency;
	@BindView(R.id.tv_rate)
	TextView mTvRate;
	@BindView(R.id.btn_loan)
	Button mBtnLoan;
	@BindView(R.id.btn_return_loan)
	Button mBtnReturnLoan;
	@BindView(R.id.lv_loan)
	ListViewForScrollView mLvLoan;
	@BindView(R.id.ll_current_loan)
	LinearLayout mLlCurrentLoan;
	@BindView(R.id.iv_menu)
	ImageView mIvMenu;

	private List<ACELoanDetailBean> mList;
	private MenuPopWindow mMenuPopWindow;

	private ArrayList<Integer> mFunctionMoreIcon = new ArrayList<>(Arrays.asList(R.drawable.ic_repay_plan, R.drawable.ic_history_black));
	private ArrayList<Integer> mFunctionMoreIconName = new ArrayList<>(Arrays.asList(R.string.repay_plan, R.string.history));

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			DaggerAceLoanComponent.builder()
					.aceLoanPresenterModule(new AceLoanPresenterModule(this, this))
					.build()
					.inject(this);
			initActivity();
			initEventListener();

		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_ace_loan;
	}

	private void initEventListener() {
		mRefreshLayout.setOnRefreshListener(this);

		//解决滑动冲突
		mScrollView.getViewTreeObserver()
				.addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
					@Override
					public void onScrollChanged() {
						mRefreshLayout.setEnabled(mScrollView!=null && mScrollView.getScrollY() == 0);
					}
				});
		mLvLoan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long index) {
				ACELoanDetailBean bean = mList.get(position);
				int id = bean.getId();
				Intent it = new Intent(AceLoanActivity.this, AceLoanDetailActivity.class);
				Bundle b = new Bundle();
				b.putInt("id", id);
				b.putInt("action_type", ACTION_1_TYPE);
				it.putExtra("bundle", b);
				startActivity(it);
			}
		});
	}

	protected void initActivity() {
		new ToolBarConfig.Builder(this, null).setEnableMenu(true)
				.setMenuType(ToolBarConfig.MenuType.MENU_IMAGE)
				.setIvMenuRes(R.drawable.ic_menu)
				.setTvTitleRes(R.string.ace_loan)
				.build();
		initPopWindown();
		mRefreshLayout.setColorSchemeColors(Utils.getColor(R.color.colorPrimary));
	}

	private void initPopWindown() {
		mMenuPopWindow = new MenuPopWindow.Builder(mContext, mFunctionMoreIconName, new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int resourceId = mFunctionMoreIcon.get(position);
				switch (resourceId) {
					case R.drawable.ic_history_black:
						Utils.toActivity(AceLoanActivity.this, AceLoanHistoryActivity.class);
						break;
					case R.drawable.ic_repay_plan:
						Utils.toActivity(AceLoanActivity.this, AceRepayPlanListActivity.class);
						break;
				}
				mMenuPopWindow.dismiss();
			}
		}).setItemIcons(mFunctionMoreIcon).build();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mPresenter.getAceLoan(true);
	}

	@Override
	public void initView(ACELoanBean bean) {
		try {
			String rateStr = getResources().getString(R.string.day_service_charge_rate);
			if (bean == null) {
				mVRound.setProgress(0);
				String str = getResources().getString(R.string.zero_with_decimal);
				String currency = getResources().getString(R.string.usd);
				mTvCurrency.setText(currency);
				mTvCredit.setText(str);
				mTvCreditCurrency.setText(currency);
				mTvRate.setText(String.format(rateStr, str + "%"));
				mBtnLoan.setEnabled(false);
				mBtnReturnLoan.setEnabled(false);
			} else {
				double loan = bean.getLoan();
				String currency = bean.getCurrency();
				if (TextUtils.isEmpty(currency)) currency = AppGlobal.USD;
				mTvCurrency.setText(currency);
				double credit = bean.getCredit();
				mTvCredit.setText(Utils.format(credit, currency));
				mTvCreditCurrency.setText(currency);
				double rate = bean.getRate();
				mTvRate.setText(String.format(rateStr, Utils.format(rate, 2) + "%"));
				double amount = credit - loan;
				mVRound.setRestart();
				mVRound.setIsPercent(false);
				mVRound.setTotalAmount(credit, amount);
				int progress = (int) Math.ceil((amount) / credit * 100);
				mVRound.setProgress(progress);
				if (amount > 0) {
					mBtnLoan.setEnabled(true);
				} else {
					mBtnLoan.setEnabled(false);
				}
				if (loan > 0) {
					mBtnReturnLoan.setEnabled(true);
				} else {
					mBtnReturnLoan.setEnabled(false);
				}
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@Override
	public void initLoanDetail(List<ACELoanDetailBean> list) {
		try {
			if (list == null || list.size() == 0) {
				mLlCurrentLoan.setVisibility(View.GONE);
				mBtnReturnLoan.setEnabled(false);
			} else {
				mList = list;
				mLlCurrentLoan.setVisibility(View.VISIBLE);
				AceLoanDetailAdapter adapter = new AceLoanDetailAdapter(mContext, list);
				mLvLoan.setAdapter(adapter);
				setHeight(adapter, mLvLoan);
				mBtnReturnLoan.setEnabled(true);
			}
			//			setScrollView();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}


	@OnClick({R.id.btn_return_loan, R.id.btn_loan, R.id.ll_toolbar_menu})
	public void onClicked(View view) {
		try {
			int id = view.getId();
			switch (id) {
				case R.id.btn_loan:
					Utils.toActivity(AceLoanActivity.this, MemberLoanAceActivity.class);
					break;
				case R.id.btn_return_loan:
					Bundle b = new Bundle();
					Intent it = new Intent(AceLoanActivity.this, ReturnLoanAceActivity.class);
					b.putInt("action_type", ACTION_2_TYPE);
					it.putExtra("bundle", b);
					startActivity(it);
					break;
				case R.id.ll_toolbar_menu:
					mMenuPopWindow.showAsDropDown(this, mIvMenu);
					break;
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@Override
	public void showRefreshStatus(boolean isRefreshing) {
		mRefreshLayout.setRefreshing(isRefreshing);
	}

	@Override
	public void onRefresh() {
		mPresenter.getAceLoan(false);
	}
}
