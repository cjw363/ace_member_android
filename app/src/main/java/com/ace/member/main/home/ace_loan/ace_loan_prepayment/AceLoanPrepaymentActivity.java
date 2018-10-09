package com.ace.member.main.home.ace_loan.ace_loan_prepayment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.adapter.AceLoanPrepaymentAdapter;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.ACELoanPrepaymentBean;
import com.ace.member.main.home.ace_loan.AceLoanBaseActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.PayHelper;
import com.og.utils.FileUtils;
import com.og.utils.ListViewForScrollView;
import com.og.utils.Utils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class AceLoanPrepaymentActivity extends AceLoanBaseActivity implements AceLoanPrepaymentContract.View {

	@Inject
	AceLoanPrepaymentPresenter mPresenter;

	@BindView(R.id.tv_amount)
	TextView mTvAmount;
	@BindView(R.id.lv_detail)
	ListViewForScrollView mLvDetail;

	private String mListStr;
	private double mTotalAmount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerAceLoanPrepaymentComponent.builder()
			.aceLoanPrepaymentPresenterModule(new AceLoanPrepaymentPresenterModule(this, this))
			.build()
			.inject(this);
		mListLoanActivity.add(this);
		initActivity();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_ace_loan_prepayment;
	}

	@Override
	public void closeActivity(boolean bool) {
		//		Log.e("AceLoanPrepay", "action Type: " + mActionType + "  bool: " + bool);
		if (!bool && mActionType == ACTION_2_TYPE) {
			mListLoanActivity.remove(0);
		}
		for (BaseActivity activity : mListLoanActivity) {
			activity.finish();
		}
		mListLoanActivity.clear();
	}

	protected void initActivity() {
		Bundle b = getIntent().getBundleExtra("bundle");
		mListStr = b.getString("list");
		mActionType = b.getInt("action_type");
		//		Log.e("AceLoan", "action type: " + mActionType);
		new ToolBarConfig.Builder(this, null).setTvTitleRes(R.string.repay_now).build();
		mPresenter.getPrepayment(mListStr);
	}

	@Override
	public void initListView(List<ACELoanPrepaymentBean> list) {
		try {
			if (list == null || list.size() == 0) {
				Utils.showToast(Utils.getString(R.string.error));
				return;
			}
			AceLoanPrepaymentAdapter adapter = new AceLoanPrepaymentAdapter(mContext, list);
			mLvDetail.setAdapter(adapter);
			setHeight(adapter, mLvDetail);
			initTotal(list);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}


	private void initTotal(List<ACELoanPrepaymentBean> list) {

		try {
			double totalAmount = 0, totalInterest = 0;
			for (ACELoanPrepaymentBean bean : list) {
				totalAmount += bean.getAmount();
				totalInterest += bean.getInterest();
			}
			mTotalAmount = totalAmount + totalInterest;
			String amt = Utils.format(mTotalAmount, 2) + " " + AppGlobal.USD;
			mTvAmount.setText(amt);
			//		mTvInterestTotal.setText("(" + String.format(Utils.getString(R.string.interest_amount), Utils.format(totalInterest, 2) + " " + AppGlobal.USD) + ")");
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@OnClick({R.id.btn_submit})
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.btn_submit:
				submit();
				break;
		}
	}

	private void submit() {
		if (!AppUtils.checkEnoughMoney(AppGlobal.USD, mTotalAmount)) {
			Utils.showToast(R.string.msg_1709);
			return;
		}
		if (TextUtils.isEmpty(mListStr)) {
			Utils.showToast(R.string.please_choose_prepayment_data);
			return;
		}
		PayHelper payHelper = new PayHelper((AppCompatActivity) mContext);
		payHelper.startPay(new PayHelper.CallBackAll() {
			@Override
			public void cancelPay() {

			}

			@Override
			public void paySuccess() {
				mPresenter.prepayment(mListStr);
			}

			@Override
			public void payFail() {
				Utils.showToast(R.string.fail);
			}
		});


	}

}
