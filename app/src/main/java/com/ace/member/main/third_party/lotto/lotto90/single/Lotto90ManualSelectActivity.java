package com.ace.member.main.third_party.lotto.lotto90.single;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.adapter.BallAdapter;
import com.ace.member.base.LottoBaseActivity;
import com.ace.member.bean.LottoBetBillItem;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.ace.member.view.MyBallGridView;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class Lotto90ManualSelectActivity extends LottoBaseActivity {


	@BindView(R.id.tv_close_time)
	TextView tvCloseTime;
	@BindView(R.id.gv_ball)
	MyBallGridView mBallGridView;
	@BindView(R.id.ll_numbers)
	LinearLayout llNumbers;
	@BindView(R.id.tv_total)
	TextView tvTotal;

	private BallAdapter mAdapter;
	private int mType = 1;
	private List<Integer> mList;
	private int mCloseTime = 0;
	private LayoutInflater inflater;
	private int mNumber = 5;
	private int position;
	private Handler mHandler = new Handler();
	private double mBetAmount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActivity();
		setListener();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_lotto90_single_manual;
	}

	protected void initActivity() {
		try {
			ToolBarConfig.builder(this, null)
				.setTvTitleRes(R.string.lotto_90_single)
				.setBackgroundRes(R.color.clr_lotto_head)
				.build();
			inflater = LayoutInflater.from(mContext);
			mList = new ArrayList<>();
			Intent it = getIntent();
			if (null != it) {
				mType = it.getIntExtra("type", 1);
				mCloseTime = it.getIntExtra("close_time", 0);
				mBetAmount = it.getDoubleExtra("bet_amount", 0);
				if (mType == AppGlobal.ENTER_SELECT_BALL_2_TYPE) {
					String number = it.getStringExtra("number");
					mList = Utils.formatIntegerArray(number);
					position = it.getIntExtra("position", -1);
				}
			}
			mAdapter = new BallAdapter(mContext, 90, mList, R.drawable.style_circle_ball);
			mBallGridView.setAdapter(mAdapter);
			if (mCloseTime > 0) {
				tvCloseTime.setText(AppUtils.setCountDown(mCloseTime));
				mHandler.removeCallbacks(myRunCountDown);
				mHandler.postDelayed(myRunCountDown, 1000);
			} else {
				tvCloseTime.setText(getResources().getString(R.string.closed));
			}
			setNumberText();
			setTotalText();
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
							if (len >= 5) {
								Utils.showToast(Utils.getString(R.string.most_select_number));
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
					setNumberText();
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}
		});
	}

	private void setNumberText() {
		try {
			int len1 = mList.size();
			llNumbers.removeAllViews();
			if (len1 < 1) {
				return;
			}
			DecimalFormat decimalFormat = new DecimalFormat("00");
			for (int i = 0; i < len1; i++) {
				int number = mList.get(i);
				View view = inflater.inflate(R.layout.view_white_ball, null);
				TextView tvContent = (TextView) view.findViewById(R.id.tv_content);
				tvContent.setText(decimalFormat.format(number));
				llNumbers.addView(view);
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	private void clearSelectNumber() {
		try {
			int len = mList.size();
			if (len < 1) return;
			for (int i = 0; i < len; i++) {
				final int position = mList.get(i);
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
			llNumbers.removeAllViews();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	private boolean getAll() {
		boolean isEmpty = true;
		int n = mList.size();
		if (n == mNumber) isEmpty = false;
		return isEmpty;
	}

	private void randomSelectNumber() {
		try {
			if (!getAll()) {
				clearSelectNumber();
			}
			do {
				int i = (int) Math.floor(Math.random() * 90);
				mBallGridView.randomSelect(i, new MyBallGridView.IRandomSelect() {
					@Override
					public void randomSelect(View view, int position) {
						if (view != null && mList.indexOf((Object) (position + 1)) == -1) {
							TextView ball = (TextView) view.findViewById(R.id.tv_content);
							ball.setTextColor(ContextCompat.getColor(mContext, com.og.R.color.white));
							ball.setBackgroundResource(R.drawable.style_circle_ball);
							mList.add(position + 1);
						}
					}
				});
			} while (mList.size() < mNumber);

			setNumberText();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	private void submitSelectNumber() {
		try {
			if (Utils.isFastClick(mContext)) return;
			int len = mList.size();
			if (len == 0) {
				Utils.showToast(Utils.getString(R.string.please_select_number));
				return;
			}
			if (len != 5) {
				Utils.showToast(Utils.getString(R.string.please_choose_five_number));
				return;
			}
			Intent it = new Intent();
			String number = "";
			for (int i = 0; i < len; i++) {
				number += mList.get(i) + ",";
			}
			number = number.substring(0, number.length() - 1);
			LottoBetBillItem item = new LottoBetBillItem();
			item.setNumber(number);
			it.putExtra("item", item);
			it.putExtra("position", position);
			setResult(0, it);
			finish();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@OnClick({R.id.btn_random, R.id.btn_confirm, R.id.btn_clear})
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.btn_random:
				randomSelectNumber();
				setTotalText();
				break;
			case R.id.btn_confirm:
				submitSelectNumber();
				break;
			case R.id.btn_clear:
				clearSelectNumber();
				break;
		}
	}

	private void setTotalText() {
		try {
			int len = mList.size();
			String totalStr;
			if (len >= 5) {
				totalStr = Utils.format(mBetAmount, 2) + " " + AppGlobal.USD;
			} else {
				totalStr = Utils.format(0, 2) + " " + AppGlobal.USD;
			}
			tvTotal.setText(totalStr);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}


	private Runnable myRunCountDown = new Runnable() {
		@Override
		public void run() {
			mCloseTime -= 1;
			if (mCloseTime > 0) {
				tvCloseTime.setText(AppUtils.setCountDown(mCloseTime));
				mHandler.postDelayed(myRunCountDown, 1000);
			} else {
				mHandler.removeCallbacks(myRunCountDown);
				tvCloseTime.setText(getResources().getString(R.string.closed));
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mHandler.removeCallbacks(myRunCountDown);
		finish();
	}

}
