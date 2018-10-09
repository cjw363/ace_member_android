package com.ace.member.main.third_party.lotto.lotto90.special;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.adapter.BallAdapter;
import com.ace.member.base.LottoBaseActivity;
import com.ace.member.bean.LottoSpecial2dItem;
import com.ace.member.bean.Product;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.ace.member.view.MyBallGridView;
import com.google.gson.Gson;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class Lotto90SpecialActivity extends LottoBaseActivity implements Lotto90SpecialContract.Lotto90SpecialContractView {
	@Inject
	Lotto90SpecialPresenter mPresenter;

	@BindView(R.id.btn_submit)
	Button btnSubmit;
	@BindView(R.id.tv_close_time)
	TextView tvCloseTime;
	@BindView(R.id.gv_ball)
	MyBallGridView mBallGridView;
	@BindView(R.id.tv_total)
	TextView tvTotal;
	@BindView(R.id.tv_max)
	TextView tvMaxTimes;
	@BindView(R.id.et_times)
	EditText etTimes;
	@BindView(R.id.type_a)
	CheckBox cbTypeA;
	@BindView(R.id.type_b)
	CheckBox cbTypeB;


	private double mBalance, mBetAmount;
	private int mProductID, mCloseTime, mMaxTimes, mType = 1;
	private boolean isClose = false, isInput = true;
	private List<Integer> mList;
	private BallAdapter mAdapter;
	private Handler mHandler = new Handler();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DaggerLotto90SpecialComponent.builder()
			.lotto90SpecialPresenterModule(new Lotto90SpecialPresenterModule(this, this))
			.build()
			.inject(this);
		initActivity();
		setListener();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_lotto90_special;
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
				.setTvTitle(String.format(getResources().getString(R.string.buy_ticket_special), getResources()
					.getString(R.string.lotto_90)))
				.setBackgroundRes(R.color.clr_lotto_head)
				.build();

			mList = new ArrayList<>();
			mAdapter = new BallAdapter(mContext, 90, mList, R.drawable.style_circle_ball);
			mBallGridView.setAdapter(mAdapter);
			mPresenter.getProduct();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	private void setTimes(String times) {
		etTimes.setText(times);
		String str = etTimes.getText().toString();
		etTimes.requestFocus();
		int len = str.length();
		if (len > 0) etTimes.setSelection(len);
	}

	public void initInfo(double betAmount, int maxTimes, Product product) {
		try {
			mBalance = AppUtils.getBalance(AppGlobal.USD);
			mBetAmount = betAmount;
			if (product != null) {
				mProductID = product.getId();
				mCloseTime = product.getDiffToClose();
			} else {
				mProductID = 0;
				mCloseTime = 0;
			}

			mMaxTimes = maxTimes;
			String str = "(1 - " + maxTimes + ")";
			tvMaxTimes.setText(str);
			btnSubmit.setEnabled(true);
			if (mCloseTime > 0) {
				isClose = false;
				tvCloseTime.setText(AppUtils.setCountDown(mCloseTime));
				mHandler.removeCallbacks(myRunCountDown);
				mHandler.postDelayed(myRunCountDown, 1000);
			} else {
				tvCloseTime.setText(getResources().getString(R.string.closed));
				isClose = true;
			}
			setTotal();
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
			setTimes("");
			setTotal();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@OnClick({R.id.btn_submit, R.id.type_a, R.id.type_b, R.id.btn_clear})
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
			case R.id.btn_submit:
				submit();
				break;
			case R.id.btn_clear:
				clearSelectNumber();
				break;
			case R.id.type_a:
				if (((CheckBox) view).isChecked()) {
					mType = 1;
					cbTypeB.setChecked(false);
				} else {
					mType = 0;
				}
				break;
			case R.id.type_b:
				if (((CheckBox) view).isChecked()) {
					mType = 2;
					cbTypeA.setChecked(false);
				} else {
					mType = 0;
				}
				break;
		}
	}

	private void submit() {
		if (Utils.isFastClick(mContext)) return;
		if (isClose) {
			Utils.showToast(R.string.game_is_closed);
			return;
		}

		if (mType == 0) {
			Utils.showToast(R.string.please_select_type);
			return;
		}

		int len = mList.size();
		if (len == 0) {
			Utils.showToast(R.string.please_select_number);
			return;
		}
		if (len > 1) {
			Utils.showToast(R.string.most_select_1_number);
			return;
		}

		String times = etTimes.getText().toString();
		int n = 0;
		if (!TextUtils.isEmpty(times)) n = Integer.valueOf(times);
		if (n < 0) {
			Utils.showToast(R.string.please_enter_times);
			return;
		}
		if (n > mMaxTimes) {
			Utils.showToast(String.format(getResources().getString(R.string.max_times), mMaxTimes + ""));
			return;
		}
		double total = mBetAmount * n;
		if (total > mBalance) {
			Utils.showToast(R.string.out_of_bet_credit);
			return;
		}
		mPresenter.placeOrder();
	}

	public Map<String, String> getParams() {
		Gson mGson = new Gson();
		List<LottoSpecial2dItem> list = new ArrayList<>();
		LottoSpecial2dItem item = new LottoSpecial2dItem();
		item.setNumber(String.valueOf(mList.get(0)));
		String times = etTimes.getText().toString();
		item.setTimes(Integer.valueOf(times));
		item.setTypeSub(mType);
		int n = Integer.valueOf(times);
		double amount = mBetAmount * n;//etAmount.getText().toString();
		list.add(item);
		String betList = mGson.toJson(list);
		Map<String, String> p = new HashMap<>();
		p.put("bet_amount", String.valueOf(amount));
		p.put("bet_list", betList);
		p.put("product_id", String.valueOf(mProductID));
		p.put("type", String.valueOf(AppGlobal.LOTTO_BUY_TICKET_21_TYPE));
		return p;
	}


	private void setListener() {
		mBallGridView.setOnActionUpListener(new MyBallGridView.OnActionUpListener() {
			@Override
			public void onActionUp(View view, int position) {
				int len = mList.size();
				if (null != view) {
					TextView ball = (TextView) view.findViewById(R.id.tv_content);
					if (!mList.contains(position + 1)) {
						if (len >= 1) {
							mList.clear();
							mAdapter.notifyDataSetChanged();
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
			}
		});


		etTimes.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				isInput = true;
				mHandler.removeCallbacks(changText);
			}

			@Override
			public void afterTextChanged(Editable editable) {
				mHandler.postDelayed(changText, 100);
			}
		});
	}

	private Runnable changText = new Runnable() {
		@Override
		public void run() {
			if (isInput) {
				isInput = false;
				setTotal();
			}
		}
	};

	private void setTotal() {
		String times = etTimes.getText().toString();
		int n = 0;
		if (!TextUtils.isEmpty(times)) {
			n = Integer.valueOf(times);
		}
		double amount = mBetAmount * n;
		String total = Utils.format(amount, 2) + " " + AppGlobal.USD;
		tvTotal.setText(total);
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
