package com.ace.member.main.me.payment_history;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.PaymentHistoryBean;
import com.ace.member.main.third_party.edc.detail.EdcHistoryDetailActivity;
import com.ace.member.main.third_party.wsa.detail.WsaHistoryDetailActivity;
import com.ace.member.popup_window.DatePickerPopWindow;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.og.component.CustomSlidingRefreshListView;
import com.og.utils.DateUtils;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;

public class PaymentHistoryActivity extends BaseActivity implements PaymentHistoryContract.PaymentHistoryView, CustomSlidingRefreshListView.IXListViewListener {
	@Inject
	PaymentHistoryPresenter mPaymentHistoryPresenter;

	@BindView(R.id.lv_payment)
	CustomSlidingRefreshListView mLVPaymentHistory;

	@BindView(R.id.iv_menu)
	AppCompatImageView mIvMenu;

	@BindView(R.id.tv_condition)
	TextView mTvCondition;

	public PaymentHistoryListAdapter mAdapter;
	private int mPage = 1;

	private String mDateStart;
	private String mDateEnd;
	private String mSelectedType;
	private ArrayList<String> mTypeList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerPaymentHistoryComponent.builder()
			.paymentHistoryPresenterModule(new PaymentHistoryPresenterModule(this, this))
			.build()
			.inject(this);
		initActivity();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_payment_history;
	}

	public void initActivity() {
		ToolBarConfig.builder(this, null)
			.setTvTitleRes(R.string.payment_history)
			.setIvMenuRes(R.drawable.ic_filter)
			.setMenuType(ToolBarConfig.MenuType.MENU_IMAGE)
			.setEnableMenu(true)
			.build();
		setTypeList();
		initListener();

		mLVPaymentHistory.setPullLoadEnable(false);
		mLVPaymentHistory.setXListViewListener(this);
		mLVPaymentHistory.setRefreshTime(DateUtils.getFormatDataTime());
		mLVPaymentHistory.hideLoadMore();
		mLVPaymentHistory.setPullRefreshEnable(true);

		mDateStart = DateUtils.getToday();
		mDateEnd = DateUtils.getToday();
		setTvCondition();
		mPaymentHistoryPresenter.getList(mDateStart, mDateEnd, mSelectedType, mPage);
		setItemListener();
	}

	private void setTypeList() {
		if (mTypeList == null) {
			mTypeList = new ArrayList<>();
			mTypeList.add("All");
			mTypeList.add("Electricity");
			mTypeList.add("Water");
		}
	}

	private void initListener() {
		mIvMenu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DatePickerPopWindow datePickerPopWin = new DatePickerPopWindow.Builder(PaymentHistoryActivity.this, mDateStart, mDateEnd, mSelectedType, new DatePickerPopWindow.OnDatePickedListener() {
					@Override
					public void onDatePickCompleted(String dateStart, String dateEnd, String selectedType) {
						mDateStart = dateStart;
						mDateEnd = dateEnd;
						if ("All".equals(selectedType)) {
							selectedType = "";
						}
						mSelectedType = selectedType;
						mPaymentHistoryPresenter.getList(mDateStart, mDateEnd, mSelectedType, mPage);
						setTvCondition();
					}
				}).setSearchList(mTypeList).setCurrencyWidth(Utils.getDimenPx(R.dimen.width100)).build();
				datePickerPopWin.showPopWin(PaymentHistoryActivity.this);

				datePickerPopWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
					@Override
					public void onDismiss() {
						WindowManager.LayoutParams lp = getWindow().getAttributes();
						lp.alpha = 1f;
						getWindow().setAttributes(lp);
					}
				});

				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 0.7f;
				getWindow().setAttributes(lp);
			}
		});
	}

	private void setTvCondition() {
		String type;
		if (TextUtils.isEmpty(mSelectedType)) {
			type = "All Type";
		} else {
			type = mSelectedType;
		}
		mTvCondition.setText(type + " / " + mDateStart + " to " + mDateEnd);
	}

	@Override
	public void onRefresh() {
		mPage = 1;
		mPaymentHistoryPresenter.getList(mDateStart, mDateEnd, mSelectedType, mPage);
	}

	@Override
	public void onLoadMore() {
		if (mPage > 0) {
			mPaymentHistoryPresenter.getList(mDateStart, mDateEnd, mSelectedType, mPage);
		}
	}

	private void onLoad() {
		mLVPaymentHistory.stopRefresh();
		mLVPaymentHistory.stopLoadMore();
	}

	@Override
	public void showList(int nextPage, boolean isHint) {
		try {
			mPage = nextPage;
			mLVPaymentHistory.setRefreshTime(DateUtils.getFormatDataTime());
			int len = mPaymentHistoryPresenter.getData().size();
			if (len > 0 && isHint) {
				mLVPaymentHistory.setPullLoadEnable(true);
				mLVPaymentHistory.showLoadMore();
			} else {
				mLVPaymentHistory.setPullLoadEnable(false);
				mLVPaymentHistory.hideLoadMore();
				if (len == 0) Utils.showToast(R.string.no_data);
			}

			mAdapter = new PaymentHistoryListAdapter(mPaymentHistoryPresenter.getData());
			mLVPaymentHistory.setAdapter(mAdapter);
			onLoad();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
	}

	@Override
	public void showNextList(int nextPage) {
		try {
			if (nextPage == 0) {
				mLVPaymentHistory.hideLoadMore();
				mLVPaymentHistory.setPullLoadEnable(false);
				return;
			}
			mPage = nextPage;
			mAdapter.notifyDataSetChanged();
			onLoad();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setItemListener() {
		mLVPaymentHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//list view 的索引是从1开始的
				PaymentHistoryBean bean = mPaymentHistoryPresenter.getData().get(position - 1);
				Intent intent = new Intent(PaymentHistoryActivity.this, bean.getType() == AppGlobal.PAYMENT_TYPE_1_EDC ? EdcHistoryDetailActivity.class : WsaHistoryDetailActivity.class);
				intent.putExtra("id", bean.getId());
				startActivity(intent);
			}
		});
	}


}
