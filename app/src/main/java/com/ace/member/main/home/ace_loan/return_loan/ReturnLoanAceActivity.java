package com.ace.member.main.home.ace_loan.return_loan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.adapter.AceLoanDetailAdapter;
import com.ace.member.bean.ACELoanDetailBean;
import com.ace.member.bean.SingleIntBean;
import com.ace.member.main.home.ace_loan.AceLoanBaseActivity;
import com.ace.member.main.home.ace_loan.ace_loan_prepayment.AceLoanPrepaymentActivity;
import com.ace.member.main.home.ace_loan.repay_plan.AceLoanRepayPlanActivity;
import com.ace.member.popup_window.DatePickerPopWindow;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.og.LibGlobal;
import com.og.utils.DateUtils;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.ListViewForScrollView;
import com.og.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class ReturnLoanAceActivity extends AceLoanBaseActivity implements ReturnLoanAceContract.View, SwipeRefreshLayout.OnRefreshListener {

	@Inject
	ReturnLoanAcePresenter mPresenter;

	@BindView(R.id.lv_loan)
	ListViewForScrollView mLvLoan;
	@BindView(R.id.tv_amount)
	TextView mTvAmount;
	@BindView(R.id.tv_interest)
	TextView mTvInterest;
	@BindView(R.id.tv_condition)
	TextView mTvCondition;
	@BindView(R.id.iv_menu)
	AppCompatImageView mIvMenu;
	@BindView(R.id.rl_condition)
	RelativeLayout mRlCondition;
	@BindView(R.id.ll_content)
	LinearLayout mLlContent;
	@BindView(R.id.srl_ace_loan)
	SwipeRefreshLayout mRefreshLayout;
	@BindView(R.id.sv_loan)
	ScrollView mSVLoan;
	@BindView(R.id.ll_close)
	LinearLayout mLlClose;
	@BindView(R.id.btn_return_loan)
	Button mBtnReturnLoan;

	private List<ACELoanDetailBean> mList;
	private String mDateStart = "";
	private String mDateEnd = "";
	private DatePickerPopWindow mDatePickerPopWindow;
	private boolean mIsBatch = false;
	private AceLoanDetailAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerReturnLoanAceComponent.builder()
				.returnLoanAcePresenterModule(new ReturnLoanAcePresenterModule(this, this))
				.build()
				.inject(this);
		mListLoanActivity.add(this);
		initActivity();
		initEventListener();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_return_loan_ace;
	}

	@Override
	protected void onResume() {
		super.onResume();
		mIsBatch = false;
		initReturnLoan(false);
		mIvMenu.setImageResource(R.drawable.ic_filter);
		mLlContent.setVisibility(View.GONE);
		mDateStart = "";
		mDateEnd = "";
		mPresenter.getLoanDetailList(mDateStart, mDateEnd,true);
	}

	private void initEventListener() {
		mRefreshLayout.setOnRefreshListener(this);

		//解决滑动冲突
		mSVLoan.getViewTreeObserver()
				.addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
					@Override
					public void onScrollChanged() {
						mRefreshLayout.setEnabled(mSVLoan!=null && mSVLoan.getScrollY() == 0);
					}
				});

		mLvLoan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long index) {
				try {
					if (mIsBatch) {
						ACELoanDetailBean bean = mList.get(position);
						if (bean.getCheck()) {
							bean.setCheck(false);
						} else {
							bean.setCheck(true);
						}
						mAdapter.notifyDataSetChanged();
						mBtnReturnLoan.setEnabled(checkButtonEnabled());
					} else {
						ACELoanDetailBean bean = mList.get(position);
						int id = bean.getId();
						Intent it = new Intent(ReturnLoanAceActivity.this, AceLoanRepayPlanActivity.class);
						Bundle b = new Bundle();
						b.putInt("id", id);
						b.putInt("action_type",mActionType);
						it.putExtra("bundle", b);
						startActivity(it);
					}
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}

			}
		});
	}

	private boolean checkButtonEnabled() {
		boolean isEnabled = false;
		for (ACELoanDetailBean bean : mList) {
			if (bean.getCheck()) {
				isEnabled = true;
				break;
			}
		}
		return isEnabled;
	}

	private void initReturnLoan(boolean isBatch) {
		String str = "";
		if (isBatch) {
			str = Utils.getString(R.string.confirm);
		} else {
			str = Utils.getString(R.string.batch_repay);
		}
		mBtnReturnLoan.setText(str);
	}

	@OnClick({R.id.btn_return_loan, R.id.iv_menu, R.id.tv_condition, R.id.ll_close})
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
			case R.id.btn_return_loan:
				if (mIsBatch) {
					List<SingleIntBean> list = getPrepaymentList();
					Intent it = new Intent(ReturnLoanAceActivity.this, AceLoanPrepaymentActivity.class);
					Bundle b = new Bundle();
					b.putString("list", JsonUtil.beanToJson(list));
					b.putInt("action_type",mActionType);
					it.putExtra("bundle", b);
					startActivity(it);
				} else {
					mIsBatch = true;
					mLlContent.setGravity(View.GONE);
					initReturnLoan(mIsBatch);
					mBtnReturnLoan.setEnabled(false);
					mIvMenu.setImageResource(R.drawable.ic_close_white);
					setBatchChoose(true);
				}

				break;
			case R.id.iv_menu:
				if (mIsBatch) {
					mIsBatch = false;
					mIvMenu.setImageResource(R.drawable.ic_filter);
					initReturnLoan(mIsBatch);
					mBtnReturnLoan.setEnabled(true);
					setBatchChoose(mIsBatch);
				} else {
					mDatePickerPopWindow.showPopWin(ReturnLoanAceActivity.this);
				}
				break;
			case R.id.tv_condition:
				mDatePickerPopWindow.showPopWin(ReturnLoanAceActivity.this);
				break;
			case R.id.ll_close:
				mRlCondition.setVisibility(View.GONE);
				mDateStart = "";
				mDateEnd = "";
				mPresenter.getLoanDetailList(mDateStart, mDateEnd,true);
				break;
		}
	}

	private void setBatchChoose(boolean isBatch) {
		try {
			if (mList == null || mList.size() == 0) return;
			for (ACELoanDetailBean bean : mList) {
				bean.setBatch(isBatch);
				bean.setCheck(false);
			}
			mAdapter.notifyDataSetChanged();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	private List<SingleIntBean> getPrepaymentList() {
		List<SingleIntBean> list = new ArrayList<>();
		for (ACELoanDetailBean bean : mList) {
			if (bean.getCheck()) {
				SingleIntBean bean1 = new SingleIntBean();
				bean1.setValue(bean.getId());
				list.add(bean1);
			}
		}
		return list;
	}


	protected void initActivity() {
		try {
			new ToolBarConfig.Builder(this, null).setTvTitleRes(R.string.return_loan)
					.setIvMenuRes(R.drawable.ic_filter)
					.setMenuType(ToolBarConfig.MenuType.MENU_IMAGE)
					.setEnableMenu(true)
					.build();
			Bundle b = getIntent().getBundleExtra("bundle");
			mActionType = b.getInt("action_type");
			String state = DateUtils.getToday();//默认展示当天数据
			String end = DateUtils.getToday();
			mDatePickerPopWindow = new DatePickerPopWindow.Builder(ReturnLoanAceActivity.this, state, end, "", new DatePickerPopWindow.OnDatePickedListener() {
				@Override
				public void onDatePickCompleted(String dateStart, String dateEnd, String selectedCurrency) {
					mDateStart = dateStart;
					mDateEnd = dateEnd;
					mPresenter.getLoanDetailList(mDateStart, mDateEnd,true);
					setTvCondition();
				}
			}).minYear(LibGlobal.MIN_YEAR).setSearch(false).build();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	private void setTvCondition() {
		try {
			mRlCondition.setVisibility(View.VISIBLE);
			String condition = mDateStart + " " + Utils.getString(R.string.to) + " " + mDateEnd;
			mTvCondition.setText(condition);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@Override
	public void initLoanDetail(List<ACELoanDetailBean> list) {
		try {
			if (list == null || list.size() == 0) {
				mLlContent.setVisibility(View.GONE);
				Utils.showToast(Utils.getString(R.string.no_data));
				return;
			}
			mLlContent.setVisibility(View.VISIBLE);
			mList = list;
			mAdapter = new AceLoanDetailAdapter(mContext, list);
			mLvLoan.setAdapter(mAdapter);
			setHeight(mAdapter, mLvLoan);
//			setScrollView();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

//	private void setScrollView() {
//		mLvLoan.setFocusable(false);
//		mSVLoan.smoothScrollTo(0, 0);
//	}

	@Override
	public void initView(double amount, double interest) {
		try {
			double total = amount + interest;
			mTvAmount.setText(Utils.format(total, 2) + " " + AppGlobal.USD);
			String str = "(" + String.format(Utils.getString(R.string.interest_amount), Utils.format(interest, 2) + " " + AppGlobal.USD) + ")";
			mTvInterest.setText(str);
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
		try {
			mDateStart = "";
			mDateEnd = "";
			mRlCondition.setVisibility(View.GONE);
			mPresenter.getLoanDetailList(mDateStart, mDateEnd,false);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}
}
