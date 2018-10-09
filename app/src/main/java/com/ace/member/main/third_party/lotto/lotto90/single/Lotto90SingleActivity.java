package com.ace.member.main.third_party.lotto.lotto90.single;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.adapter.Lotto90GroupNumberAdapter;
import com.ace.member.base.LottoBaseActivity;
import com.ace.member.bean.LottoBetBillItem;
import com.ace.member.bean.Product;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.google.gson.Gson;
import com.og.component.LeftSlideDeleteListView;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class Lotto90SingleActivity extends LottoBaseActivity implements Lotto90SingleContract.Lotto90SingleContractView {

	@Inject
	Lotto90SinglePresenter mPresenter;
	@BindView(R.id.tv_close_time)
	TextView tvLeftTime;
	@BindView(R.id.lv_bet_list)
	LeftSlideDeleteListView lvBetList;
	@BindView(R.id.tv_total)
	TextView tvTotal;
	@BindView(R.id.btn_submit)
	Button btnSubmit;

	private List<LottoBetBillItem> selectNumbers;
	private int mNumber = 5, mRandomNumber = 1;//一次随机号码个数为5
	private int mGroundNumber = 10;//最多选10组
	private Lotto90GroupNumberAdapter mAdapter;
	private double mBetAmount;
	private int productID;
	private int mCloseTime;
	private boolean isClose = false, isUpdateData = true;

	private Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerLotto90SingleComponent.builder()
			.lotto90SinglePresenterModule(new Lotto90SinglePresenterModule(this, this))
			.build()
			.inject(this);
		initActivity();
		initEvent();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_lotto90_single;
	}

	public void closeBetting() {
		finish();
	}

	public void againBetting() {
		try {
			clearGroup();
			calculateTotal();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	protected void initActivity() {
		try {
			ToolBarConfig.builder(this, null)
				.setTvTitleRes(R.string.lotto_90_single)
				.setBackgroundRes(R.color.clr_lotto_head)
				.build();
			selectNumbers = new ArrayList<>();
			mAdapter = new Lotto90GroupNumberAdapter(mContext, selectNumbers);
			lvBetList.setAdapter(mAdapter);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (isUpdateData) {
			mPresenter.getProduct();
		}
		isUpdateData = true;
	}

	private void initEvent() {

		//左滑动删除
		lvBetList.setOnListViewItemDeleteClickListener(new LeftSlideDeleteListView.OnListViewItemDeleteClickListener() {
			@Override
			public void onListViewItemDeleteClick(int position) {
				selectNumbers.remove(position);
				mAdapter.notifyDataSetChanged();
				calculateTotal();
			}
		});

		lvBetList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				try {
					LottoBetBillItem item = selectNumbers.get(i);
					Intent it = new Intent(Lotto90SingleActivity.this, Lotto90ManualSelectActivity.class);
					it.putExtra("number", item.getNumber());
					it.putExtra("type", AppGlobal.ENTER_SELECT_BALL_2_TYPE);
					it.putExtra("close_time", mCloseTime);
					it.putExtra("bet_amount", mBetAmount);
					it.putExtra("position", i);
					isUpdateData = false;
					startActivityForResult(it, AppGlobal.ENTER_SELECT_BALL_2_TYPE);
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}
		});
	}


	@OnClick({R.id.btn_random_1, R.id.btn_random_5, R.id.btn_submit, R.id.btn_manual_betting, R.id.btn_clear})
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
			case R.id.btn_random_1:
				mRandomNumber = 1;
				showRandom();
				calculateTotal();
				break;
			case R.id.btn_random_5:
				mRandomNumber = 5;
				showRandom();
				calculateTotal();
				break;
			case R.id.btn_manual_betting:
				isUpdateData = false;
				showManualSelect();
				break;
			case R.id.btn_clear:
				clearGroup();
				calculateTotal();
				break;
			case R.id.btn_submit:
				placeOrders();
				break;
		}
	}

	private void placeOrders() {
		if (Utils.isFastClick(mContext)) return;
		if (isClose) {
			Utils.showToast(Utils.getString(R.string.game_is_closed));
			return;
		}
		int len = selectNumbers.size();
		if (len == 0) {
			Utils.showToast(Utils.getString(R.string.please_betting));
			return;
		}
		if (len > mGroundNumber) {
			Utils.showToast(Utils.getString(R.string.bet_more_10_group));
			return;
		}

		double total = len * mBetAmount;
		if (total > AppUtils.getBalance(AppGlobal.USD)) {
			return;
		}
		mPresenter.placeOrders();
	}

	public Map<String, String> getParams() {
		Gson mGson = new Gson();
		String betList = mGson.toJson(selectNumbers);
		double total = selectNumbers.size() * mBetAmount;
		Map<String, String> p = new HashMap<>();
		p.put("bet_amount", String.valueOf(total));
		p.put("bet_list", betList);
		p.put("product_id", String.valueOf(productID));
		p.put("type", AppGlobal.LOTTO_BUY_TICKET_11_TYPE + "");
		return p;
	}

	private void showManualSelect() {
		try {
			int len = selectNumbers.size();
			if (len >= mGroundNumber) {
				Utils.showToast(Utils.getString(R.string.bet_more_10_group));
				return;
			}

			Intent it = new Intent(Lotto90SingleActivity.this, Lotto90ManualSelectActivity.class);
			it.putExtra("type", AppGlobal.ENTER_SELECT_BALL_1_TYPE);
			it.putExtra("close_time", mCloseTime);
			it.putExtra("bet_amount", mBetAmount);
			startActivityForResult(it, AppGlobal.ENTER_SELECT_BALL_1_TYPE);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	private void calculateTotal() {
		try {
			int len = selectNumbers.size();
			String totalStr;
			if (len > 0) {
				double total = mBetAmount * len;
				totalStr = Utils.format(total, 2) + " " + AppGlobal.USD;
			} else {
				totalStr = Utils.format(0, 2) + " " + AppGlobal.USD;
			}
			tvTotal.setText(totalStr);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	private void clearGroup() {
		selectNumbers.clear();
		mAdapter.notifyDataSetChanged();
	}

	private void showRandom() {
		try {
			int len = selectNumbers.size();
			if (len >= mGroundNumber) {
				Utils.showToast(Utils.getString(R.string.bet_more_10_group));
				return;
			}
			int a = mGroundNumber - len;
			if (a > mRandomNumber) {
				for (int i = 0; i < mRandomNumber; i++) {
					randomSelectNumber();
				}
				mAdapter.notifyDataSetChanged();
			} else {
				for (int i = 0; i < a; i++) {
					randomSelectNumber();
				}
				mAdapter.notifyDataSetChanged();
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}


	//随机组
	private void randomSelectNumber() {
		try {
			String number = "";
			List<Integer> selectNumber = new ArrayList<>();
			do {
				int i = (int) Math.floor(Math.random() * 90) + 1;
				if (selectNumber.indexOf(i) == -1) {
					selectNumber.add(i);
					number += i + ",";
				}
			} while (selectNumber.size() < mNumber);
			number = number.substring(0, number.length() - 1);
			LottoBetBillItem item = new LottoBetBillItem();
			item.setNumber(number);
			selectNumbers.add(item);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@Override
	public void initInfo(double betAmount, Product product) {
		try {
			mBetAmount = betAmount;
			if (product != null) {
				productID = product.getId();
				mCloseTime = product.getDiffToClose();
			} else {
				productID = 0;
				mCloseTime = 0;
			}
			btnSubmit.setEnabled(true);
			if (mCloseTime > 0) {
				isClose = false;
				tvLeftTime.setText(AppUtils.setCountDown(mCloseTime));
				mHandler.removeCallbacks(myRunCountDown);
				mHandler.postDelayed(myRunCountDown, 1000);
			} else {
				tvLeftTime.setText(getResources().getString(R.string.closed));
				isClose = true;
			}
			calculateTotal();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case AppGlobal.ENTER_SELECT_BALL_2_TYPE:
				if (null != data) {
					//					List<Integer> list = data.getIntegerArrayListExtra("ball_list");
					LottoBetBillItem item = data.getParcelableExtra("item");
					int i = data.getIntExtra("position", -1);
					if (i > -1) {
						selectNumbers.remove(i);
						selectNumbers.add(i, item);
						mAdapter.notifyDataSetChanged();
					}
				}
				break;
			case AppGlobal.ENTER_SELECT_BALL_1_TYPE:
				if (null != data) {
					//					List<Integer> list = data.getIntegerArrayListExtra("ball_list");
					LottoBetBillItem item = data.getParcelableExtra("item");
					selectNumbers.add(item);
					mAdapter.notifyDataSetChanged();
				}
				break;
		}
		calculateTotal();
	}

	private Runnable myRunCountDown = new Runnable() {
		@Override
		public void run() {
			mCloseTime -= 1;
			if (mCloseTime > 0) {
				isClose = false;
				tvLeftTime.setText(AppUtils.setCountDown(mCloseTime));
				mHandler.postDelayed(myRunCountDown, 1000);
			} else {
				isClose = true;
				mHandler.removeCallbacks(myRunCountDown);
				tvLeftTime.setText(Utils.getString(R.string.closed));
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mHandler.removeCallbacks(myRunCountDown);
	}
}
