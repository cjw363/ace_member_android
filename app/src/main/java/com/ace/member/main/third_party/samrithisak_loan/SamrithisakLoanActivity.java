package com.ace.member.main.third_party.samrithisak_loan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.MemberLoanPartner;
import com.ace.member.bean.MemberLoanPartnerBill;
import com.ace.member.event.RefreshEvent;
import com.ace.member.main.third_party.samrithisak_loan.history.HistoryActivity;
import com.ace.member.main.third_party.samrithisak_loan.loan.LoanActivity;
import com.ace.member.main.third_party.samrithisak_loan.return_loan.ReturnLoanActivity;
import com.ace.member.popup_window.MenuPopWindow;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.og.utils.Utils;
import com.roundProgress.RoundProgressBar;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class SamrithisakLoanActivity extends BaseActivity implements SamrithisakLoanContract.View, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

	@Inject
	SamrithisakLoanPresenter mPresenter;

	@BindView(R.id.srl)
	SwipeRefreshLayout mRefreshLayout;
	@BindView(R.id.rpb)
	RoundProgressBar mRoundProgressBar;
	@BindView(R.id.tv_credit)
	TextView mTvCredit;
	@BindView(R.id.tv_service_charge)
	TextView mTvServiceCharge;
	@BindView(R.id.ll_latest_bill)
	LinearLayout mLlLatestBill;
	@BindView(R.id.ll_function)
	LinearLayout mLlFunction;


	@BindView(R.id.tv_bill_date)
	TextView mTvBillDate;
	@BindView(R.id.tv_bill_amount)
	TextView mTvBillAmount;
	@BindView(R.id.tv_due_date)
	TextView mTvDueDate;

	@BindView(R.id.btn_loan)
	Button mBtnLoan;
	@BindView(R.id.btn_return_loan)
	Button mBtnReturnLoan;

	@BindView(R.id.iv_menu)
	AppCompatImageView mIvMenu;
	private MenuPopWindow mPopWindow;


	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerSamrithisakLoanComponent.builder()
			.samrithisakLoanPresenterModule(new SamrithisakLoanPresenterModule(this, this))
			.build()
			.inject(this);
		init();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_samrithisak_loan;
	}

	private void init() {
		ToolBarConfig.builder(this, null)
			.setTvTitleRes(R.string.samrithisak_loan)
			.setEnableMenu(true)
			.setMenuType(ToolBarConfig.MenuType.MENU_IMAGE)
			.setMenuListener(this)
			.setIvMenuRes(R.drawable.ic_menu)
			.build();
		mRefreshLayout.setColorSchemeColors(Utils.getColor(R.color.colorPrimary));
		mRefreshLayout.setOnRefreshListener(this);

		mPresenter.start(false);
	}

	@Override
	public void setEnableFunction(boolean enable) {
		mLlFunction.setVisibility(enable ? View.VISIBLE : View.INVISIBLE);
	}

	@Override
	public void setEnableLoan(boolean enable) {
		mBtnLoan.setEnabled(enable);
	}

	@Override
	public void setEnableReturnLoan(boolean enable) {
		mBtnReturnLoan.setEnabled(enable);
	}

	@Override
	public void setCreditAndLoan(MemberLoanPartner memberLoanPartner) {
		if (memberLoanPartner == null) {
			mTvCredit.setText(Utils.getString(R.string.zero_with_decimal));
			mRoundProgressBar.setProgress(0);
		} else {
			int credit = memberLoanPartner.getCredit();
			int loan = memberLoanPartner.getLoan();
			double available = credit - loan;
			mTvCredit.setText(Utils.format(credit, 2));
			mRoundProgressBar.setRestart();
			mRoundProgressBar.setIsPercent(false);
			mRoundProgressBar.setTotalAmount(credit, available);
			mRoundProgressBar.setMax(credit);
			mRoundProgressBar.setProgress((int) Math.ceil((available) / credit * 100));
		}
	}

	@Override
	public void setLatestBill(MemberLoanPartnerBill bill) {
		if (bill == null) mLlLatestBill.setVisibility(View.INVISIBLE);
		else {
			mLlLatestBill.setVisibility(View.VISIBLE);
			mTvBillDate.setText(bill.getDate());
			mTvBillAmount.setText(String.format(Utils.getString(R.string.format_twice), Utils.format(bill.getAmount(), 2), AppGlobal.USD));
			mTvDueDate.setText(bill.getDueDate());
			mTvDueDate.setTextColor(Utils.getColor(bill.isOverDue() ? R.color.clr_orange : R.color.black));
		}
	}

	@Override
	public void setEnableRefresh(boolean enable) {
		mRefreshLayout.setRefreshing(enable);
	}

	@Override
	public void setServiceChargeRate(String s) {
		mTvServiceCharge.setText(s);
	}

	@OnClick({R.id.btn_loan, R.id.btn_return_loan, R.id.ll_toolbar_menu})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_loan:
				Utils.toActivity(this, LoanActivity.class);
				break;
			case R.id.btn_return_loan:
				Utils.toActivity(this, ReturnLoanActivity.class);
				break;
			case R.id.ll_toolbar_menu:
				showPopWindow();
				break;
		}
	}

	private void showPopWindow() {
		List<Integer> menuNames = new ArrayList<>(2);
		menuNames.add(R.string.config);
		menuNames.add(R.string.history);
		List<Integer> menuIcons = new ArrayList<>(2);
		menuIcons.add(R.drawable.ic_setting_black);
		menuIcons.add(R.drawable.ic_history_black);
		mPopWindow = new MenuPopWindow.Builder(this, menuNames, new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
					case 0:
						break;
					case 1:
						Utils.toActivity(SamrithisakLoanActivity.this, HistoryActivity.class);
						break;
				}
				mPopWindow.dismiss();
			}
		}).setItemIcons(menuIcons).build();
		mPopWindow.showAsDropDown(this, mIvMenu);
	}

	@Subscribe
	public void onRefresh(RefreshEvent event) {
		mPresenter.start(false);
	}

	@Override
	public void onRefresh() {
		mPresenter.start(true);
	}
}
