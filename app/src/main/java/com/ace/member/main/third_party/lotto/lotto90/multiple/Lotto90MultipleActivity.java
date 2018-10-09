package com.ace.member.main.third_party.lotto.lotto90.multiple;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.adapter.BallAdapter;
import com.ace.member.base.LottoBaseActivity;
import com.ace.member.bean.LottoBetBillItem;
import com.ace.member.bean.Product;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.ace.member.view.MyBallGridView;
import com.google.gson.Gson;
import com.og.utils.FileUtils;
import com.og.utils.Utils;
import com.og.view.SeekBarIndicated;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class Lotto90MultipleActivity extends LottoBaseActivity implements Lotto90MultipleContract.Lotto90MultipleContractView {

	@Inject
	Lotto90MultiplePresenter mPresenter;

	@BindView(R.id.tv_close_time)
	TextView tvCloseTime;
	@BindView(R.id.tv_total)
	TextView tvTotal;
	@BindView(R.id.btn_confirm)
	Button btnConfirm;
	@BindView(R.id.gv_ball)
	MyBallGridView mBallGridView;
	@BindView(R.id.lotto_seek_bar)
	SeekBarIndicated seekBarIndicated;
	@BindView(R.id.btn_random)
	Button btnRandom;

	private BallAdapter mAdapter;
	private List<Integer> mList;
	private double mBalance, mBetAmount;
	private int mMaxNumber = 20, mCloseTime, mProductID, mRandomNumber = 6;
	private boolean isClose = false;
	private Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerLotto90MultipleComponent.builder()
			.lotto90MultiplePresenterModule(new Lotto90MultiplePresenterModule(this, this))
			.build()
			.inject(this);
		initActivity();
		setTotalText();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_lotto90_multiple;
	}

	public void closeBetting() {
		finish();
	}

	public void againBetting() {
		try {
			clearSelectNumber();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	protected void initActivity() {
		try {
			ToolBarConfig.builder(this, null)
				.setTvTitleRes(R.string.lotto_90_multiple)
				.setBackgroundRes(R.color.clr_lotto_head)
				.build();
			mList = new ArrayList<>();
			mAdapter = new BallAdapter(mContext, 90, mList, R.drawable.style_circle_ball);
			mBallGridView.setAdapter(mAdapter);
			mPresenter.getGameInfo();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	private void setListener() {
		mBallGridView.setOnActionUpListener(new MyBallGridView.OnActionUpListener() {
			@Override
			public void onActionUp(View view, int position) {
				try {
					int len = mList.size();
					if (null != view) {
						TextView ball = (TextView) view.findViewById(R.id.tv_content);
						if (!mList.contains(position + 1)) {
							if (len >= mMaxNumber) {
								Utils.showToast(String.format(getResources().getString(R.string.pleas_select_number), mMaxNumber + ""));
								return;
							}
							ball.setTextColor(ContextCompat.getColor(mContext, R.color.white));
							ball.setBackgroundResource(R.drawable.style_circle_ball);
							mList.add(position + 1);
						} else {
							ball.setTextColor(ContextCompat.getColor(mContext, R.color.clr_lotto_btn_text));
							ball.setBackgroundResource(R.drawable.style_circle_white);
							mList.remove((Object) (position + 1));
						}
					}
					setTotalText();
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}
		});
	}

	private void setTotalText() {
		try {
			int len = mList.size();
			String totalStr;
			if (len > 5) {
				int m = Utils.combination(len, 5);
				double total = mBetAmount * m;
				totalStr = Utils.format(total, 2) + " " + AppGlobal.USD;
			} else {
				totalStr = Utils.format(0, 2) + " " + AppGlobal.USD;
			}
			tvTotal.setText(totalStr);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@Override
	public void initGame(double balance, double betAmount, int maxNumber, Product product) {
		try {
			mBalance = balance;
			mBetAmount = betAmount;
			mMaxNumber = maxNumber > 0 ? maxNumber : mMaxNumber;
			if (product != null) {
				mProductID = product.getId();
				mCloseTime = product.getDiffToClose();
			} else {
				mProductID = 0;
				mCloseTime = 0;
			}
			btnRandom.setEnabled(true);
			btnConfirm.setEnabled(true);
			initSeekBar();

			if (mCloseTime > 0) {
				isClose = false;
				tvCloseTime.setText(AppUtils.setCountDown(mCloseTime));
				mHandler.removeCallbacks(myRunCountDown);
				mHandler.postDelayed(myRunCountDown, 1000);
			} else {
				tvCloseTime.setText(getResources().getString(R.string.closed));
				isClose = true;
			}
			setListener();

		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	private void initSeekBar() {
		seekBarIndicated.setMin(6);
		seekBarIndicated.setMax(mMaxNumber);
		seekBarIndicated.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				mRandomNumber = seekBar.getProgress() + 6;
				if (mRandomNumber < 6) mRandomNumber = 6;
				if (mRandomNumber > mMaxNumber) mRandomNumber = mMaxNumber;
			}
		});
	}

	@OnClick({R.id.btn_clear, R.id.btn_random, R.id.btn_confirm})
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.btn_clear:
				clearSelectNumber();
				break;
			case R.id.btn_random:
				randomSelectNumber();
				break;
			case R.id.btn_confirm:
				submit();
				break;
		}
	}

	private void submit() {
		if (Utils.isFastClick(mContext)) return;
		if (isClose) {
			Utils.showToast(Utils.getString(R.string.game_is_closed));
			return;
		}
		int len = mList.size();
		if (len == 0) {
			Utils.showToast(Utils.getString(R.string.please_select_number));
			return;
		}
		if (len < 6 || len > mMaxNumber) {
			Utils.showToast(String.format(getResources().getString(R.string.pleas_select_number), mMaxNumber + ""));
			return;
		}
		int m = Utils.combination(len, 5);
		double total = m * mBetAmount;
		if (total > mBalance) {
			return;
		}
		mPresenter.placeOrder();
	}

	public Map<String, String> getParams() {
		try {
			Gson mGson = new Gson();
			List<LottoBetBillItem> list = new ArrayList<>();
			int len = mList.size();
			String number = "";
			for (int i = 0; i < len; i++) {
				number += mList.get(i) + ",";
			}
			number = number.substring(0, number.length() - 1);
			LottoBetBillItem item = new LottoBetBillItem();
			item.setNumber(number);
			list.add(item);
			int m = Utils.combination(len, 5);
			double total = m * mBetAmount;
			String betList = mGson.toJson(list);
			Map<String, String> p = new HashMap<>();
			p.put("bet_amount", String.valueOf(total));
			p.put("bet_list", betList);
			p.put("product_id", String.valueOf(mProductID));
			p.put("type", AppGlobal.LOTTO_BUY_TICKET_12_TYPE + "");
			return p;
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return null;
	}

	public void updateInfo(double balance) {
		mBalance = balance;
	}

	private void randomSelectNumber() {
		try {
			int len = mList.size();
			if (mRandomNumber > mMaxNumber) mRandomNumber = mMaxNumber;
			if (len == mRandomNumber || len > mRandomNumber) {
				clearSelectNumber();
			}
			do {
				int i = (int) Math.floor(Math.random() * 90);
				mBallGridView.randomSelect(i, new MyBallGridView.IRandomSelect() {
					@Override
					public void randomSelect(View view, int position) {
						if (view != null && mList.indexOf((Object) (position + 1)) == -1) {
							TextView ball = (TextView) view.findViewById(R.id.tv_content);
							ball.setTextColor(ContextCompat.getColor(mContext, R.color.white));
							ball.setBackgroundResource(R.drawable.style_circle_ball);
							mList.add(position + 1);
						}
					}
				});
			} while (mList.size() < mRandomNumber);
			setTotalText();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	private void clearSelectNumber() {
		try {
			int len = mList.size();
			if (len < 1) return;
			for (int i = 0; i < len; i++) {
				int position = mList.get(i);
				mBallGridView.resetGridView(position - 1, new MyBallGridView.IResetSelect() {
					@Override
					public void resetSelect(View view) {
						if (view != null) {
							TextView ball = (TextView) view.findViewById(R.id.tv_content);
							ball.setTextColor(ContextCompat.getColor(mContext, R.color.clr_lotto_btn_text));
							ball.setBackgroundResource(R.drawable.style_circle_white);
						}
					}
				});
			}
			mList.clear();
			mAdapter.notifyDataSetChanged();
			setTotalText();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	private Runnable myRunCountDown = new Runnable() {
		@Override
		public void run() {
			mCloseTime -= 1;
			if (mCloseTime > 0) {
				isClose = false;
				tvCloseTime.setText(AppUtils.setCountDown(mCloseTime));
				mHandler.postDelayed(myRunCountDown, 1000);
			} else {
				isClose = true;
				mHandler.removeCallbacks(myRunCountDown);
				tvCloseTime.setText(getResources().getString(R.string.closed));
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mHandler.removeCallbacks(myRunCountDown);
	}
}
