package com.ace.member.main.home.ace_loan.history;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.adapter.AceLoanHistoryAdapter;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.ACELoanDetailBean;
import com.ace.member.main.home.ace_loan.history_detail.AceLoanHistoryDetailActivity;
import com.ace.member.popup_window.DatePickerPopWindow;
import com.ace.member.toolbar.ToolBarConfig;
import com.og.LibGlobal;
import com.og.component.CustomSlidingRefreshListView;
import com.og.utils.DateUtils;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class AceLoanHistoryActivity extends BaseActivity implements AceLoanHistoryContract.View, CustomSlidingRefreshListView.IXListViewListener {

	@Inject
	AceLoanHistoryPresenter mPresenter;

	@BindView(R.id.ll_condition)
	LinearLayout mLlCondition;
	@BindView(R.id.lv_loan)
	CustomSlidingRefreshListView mLvLoan;
	@BindView(R.id.tv_condition)
	TextView mTvCondition;

	private AceLoanHistoryAdapter mAdapter;
	private int mPage = 1;
	private List<ACELoanDetailBean> mList = new ArrayList<>();
	private String mDateStart = "", mDateEnd = "";
	private DatePickerPopWindow mDatePickerPopWindow;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerAceLoanHistoryComponent.builder()
				.aceLoanHistoryPresenterModule(new AceLoanHistoryPresenterModule(this, this))
				.build()
				.inject(this);
		initActivity();
		initEventListener();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_ace_loan_history;
	}

	@Override
	protected void onResume() {
		super.onResume();
		mDateStart = "";
		mDateEnd = "";
		mPage = 1;
		mPresenter.getLoadDetailList(mDateStart, mDateEnd, mPage);
	}

	private void initEventListener() {
		mLvLoan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent it=new Intent(AceLoanHistoryActivity.this, AceLoanHistoryDetailActivity.class);
				Bundle b=new Bundle();
				ACELoanDetailBean bean=mList.get(position-1);
				b.putSerializable("detail",bean);
				it.putExtra("bundle",b);
				startActivity(it);
			}
		});
	}

	protected void initActivity() {
		try {
			new ToolBarConfig.Builder(this, null).setTvTitleRes(R.string.loan_history)
					.setIvMenuRes(R.drawable.ic_filter)
					.setMenuType(ToolBarConfig.MenuType.MENU_IMAGE)
					.setEnableMenu(true)
					.build();
			mLvLoan.setPullLoadEnable(false);
			mLvLoan.setXListViewListener(this);
			mLvLoan.setRefreshTime(DateUtils.getFormatDataTime());
			mLvLoan.setPullRefreshEnable(false);
			mLvLoan.hideLoadMore();
			mLvLoan.setPullRefreshEnable(true);
			String state = DateUtils.getToday();//默认展示当天数据
			String end = DateUtils.getToday();
			mDatePickerPopWindow = new DatePickerPopWindow.Builder(AceLoanHistoryActivity.this, state, end, "", new DatePickerPopWindow.OnDatePickedListener() {
				@Override
				public void onDatePickCompleted(String dateStart, String dateEnd, String selectedCurrency) {
					mDateStart = dateStart;
					mDateEnd = dateEnd;
					mPage = 1;
					mPresenter.getLoadDetailList(mDateStart, mDateEnd, mPage);
					setTvCondition();
				}
			}).minYear(LibGlobal.MIN_YEAR).setSearch(false).build();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	private void setTvCondition() {
		mLlCondition.setVisibility(View.VISIBLE);
		mTvCondition.setText(mDateStart + " " + Utils.getString(R.string.to) + " " + mDateEnd);
	}

	@OnClick({R.id.iv_menu, R.id.tv_condition, R.id.ll_close})
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
			case R.id.iv_menu:
				mDatePickerPopWindow.showPopWin(AceLoanHistoryActivity.this);
				break;
			case R.id.tv_condition:
				mDatePickerPopWindow.showPopWin(AceLoanHistoryActivity.this);
				break;
			case R.id.ll_close:
				mLlCondition.setVisibility(View.GONE);
				mDateStart = "";
				mDateEnd = "";
				mPage = 1;
				mPresenter.getLoadDetailList(mDateStart, mDateEnd, mPage);
				break;
		}
	}

	@Override
	public void initView(List<ACELoanDetailBean> list, int nexPage, boolean canLoadMore) {
		try {
			if (list == null || list.size() == 0) {
				Utils.showToast(R.string.no_data);
				return;
			}
			mList.clear();
			mList = list;
			mPage = nexPage;
			mLvLoan.setRefreshTime(DateUtils.getFormatDataTime());
			int size = list.size();
			if (size > 0 && canLoadMore) {
				mLvLoan.setPullLoadEnable(true);
				mLvLoan.showLoadMore();
			} else {
				mLvLoan.setPullLoadEnable(false);
				mLvLoan.hideLoadMore();
			}

			mAdapter = new AceLoanHistoryAdapter(mContext, list);
			mLvLoan.setAdapter(mAdapter);
			onLoad();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@Override
	public void nextPage(List<ACELoanDetailBean> list, int nextPage) {
		try {
			if (nextPage == 0) {
				mLvLoan.hideLoadMore();
				mLvLoan.setPullLoadEnable(false);
			}
			mPage = nextPage;
			mList.addAll(list);
			mAdapter.notifyDataSetChanged();
			onLoad();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@Override
	public void onRefresh() {
		mPage = 1;
		mPresenter.getLoadDetailList(mDateStart, mDateEnd, mPage);
	}

	@Override
	public void onLoadMore() {
		if (mPage > 0) {
			mPresenter.getLoadDetailList(mDateStart, mDateEnd, mPage);
		}
	}

	private void onLoad() {
		mLvLoan.stopRefresh();
		mLvLoan.stopLoadMore();
	}
}
