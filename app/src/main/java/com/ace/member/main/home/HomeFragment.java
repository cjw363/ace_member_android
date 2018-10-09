package com.ace.member.main.home;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.adapter.GVMenuAdapter;
import com.ace.member.base.BaseFragment;
import com.ace.member.bean.Balance;
import com.ace.member.gesture_lock_setup.LockSetupActivity;
import com.ace.member.listener.IMyViewOnClickListener;
import com.ace.member.main.bottom_dialog.BottomDialog;
import com.ace.member.main.currency.CurrencyActivity;
import com.ace.member.main.home.ace_loan.AceLoanActivity;
import com.ace.member.main.home.money.MoneyActivity;
import com.ace.member.main.home.receive_to_acct.ReceiveToAcctActivity;
import com.ace.member.main.home.salary_loan.SalaryLoanActivity;
import com.ace.member.main.home.scan.ScanResultActivity;
import com.ace.member.main.home.top_up.TopUpActivity;
import com.ace.member.main.home.transfer.TransferActivity;
import com.ace.member.main.me.password.PasswordActivity;
import com.ace.member.sms_notification.SMSNotificationActivity;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.M;
import com.ace.member.utils.Session;
import com.og.event.MessageEvent;
import com.og.utils.FileUtils;
import com.og.utils.Utils;
import com.zxing.activity.CaptureActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;


public class HomeFragment extends BaseFragment implements HomeContract.HomeView, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, AdapterView.OnItemClickListener {

	@Inject
	HomePresenter mPresenter;

	@BindView(R.id.srl)
	SwipeRefreshLayout mRefreshLayout;
	@BindView(R.id.ll_currency)
	LinearLayout mLLCurrency;
	@BindView(R.id.iv_customer_service)
	ImageView mIvCustomerService;
	@BindView(R.id.ll_customer_service)
	LinearLayout mLlCustomerService;
	@BindView(R.id.ll_scan)
	LinearLayout mLlScan;
	@BindView(R.id.iv_money)
	ImageView mIvMoney;
	@BindView(R.id.ll_money)
	LinearLayout mLLMoney;
	@BindView(R.id.gv_function)
	GridView mGvFunction;
	@BindView(R.id.scroll_view)
	ScrollView mScrollView;

	public static final int MSG_FINISH_SETTING_FINGERPRINT_LOGIN = 10001;
	public static final int MSG_FINISH_SETTING_GESTURE = 10002;

	@Override
	protected int getContentViewLayout() {
		return R.layout.fragment_home;
	}

	@Override
	protected void initView() {
		DaggerHomeComponent.builder()
			.homePresenterModule(new HomePresenterModule(this, getActivity()))
			.build()
			.inject(this);

		mRefreshLayout.setColorSchemeColors(Utils.getColor(R.color.colorPrimary));
		mRefreshLayout.setOnRefreshListener(this);
		mLlScan.setOnClickListener(this);
		mLlCustomerService.setOnClickListener(this);
		mLLMoney.setOnClickListener(this);

		//解决滑动冲突
		mScrollView.getViewTreeObserver()
			.addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
				@Override
				public void onScrollChanged() {
					mRefreshLayout.setEnabled(mScrollView!=null && mScrollView.getScrollY() == 0);
				}
			});

		int[] menuDrawable = {R.drawable.ic_transfer, R.drawable.ic_top_up, R.drawable.ic_a2c, R.drawable.ic_ace_loan, R.drawable.ic_salary_loan};
		int[] menuTitle = {R.string.transfer, R.string.top_up, R.string.receive_to_acct, R.string.ace_loan, R.string.salary_loan};
		GVMenuAdapter gvAdapter = new GVMenuAdapter(menuDrawable, menuTitle);
		mGvFunction.setAdapter(gvAdapter);
		mGvFunction.setOnItemClickListener(this);
	}

	@Override
	public void onResume() {
		mPresenter.getBalance(false);
		doOtherDialogSetting();
		super.onResume();
	}

	@Override
	public void onRefresh() {
		mPresenter.getBalance(true);
	}

	@Override
	public void setBalance() {
		try {
			mLLCurrency.removeAllViews();
			List<Balance> balanceList = Session.balanceList;
			if (Utils.isEmptyList(balanceList)) {
				View view = View.inflate(getContext(), R.layout.view_lv_menu, null);
				TextView tvCurrency = (TextView) view.findViewById(R.id.tv_menu_title);
				TextView tvBalance = (TextView) view.findViewById(R.id.tv_menu_content);
				tvCurrency.setText(Utils.getString(R.string.balance_usd));
				tvBalance.setText(Utils.format("0", 2));
				view.setTag(AppGlobal.USD);
				view.setOnClickListener(this);
				mLLCurrency.addView(view);
				return;
			}
			for (int i = 0; i < balanceList.size(); i++) {
				View view = View.inflate(getContext(), R.layout.view_lv_menu, null);
				TextView tvCurrency = (TextView) view.findViewById(R.id.tv_menu_title);
				switch (balanceList.get(i).getCurrency()) {
					case AppGlobal.USD:
						tvCurrency.setText(Utils.getString(R.string.balance_usd));
						view.setTag(AppGlobal.USD);
						break;
					case AppGlobal.KHR:
						tvCurrency.setText(Utils.getString(R.string.balance_khr));
						view.setTag(AppGlobal.KHR);
						break;
					case AppGlobal.VND:
						tvCurrency.setText(Utils.getString(R.string.balance_vnd));
						view.setTag(AppGlobal.VND);
						break;
					case AppGlobal.THB:
						tvCurrency.setText(Utils.getString(R.string.balance_thb));
						view.setTag(AppGlobal.THB);
						break;
				}
				TextView tvBalance = (TextView) view.findViewById(R.id.tv_menu_content);
				tvBalance.setText(Utils.format(balanceList.get(i).getAmount(), 2));
				view.setOnClickListener(this);
				mLLCurrency.addView(view);
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
	public void toLockSetupActivity() {
		Intent it = new Intent(getActivity(), LockSetupActivity.class);
		it.putExtra("action_type", AppGlobal.ACTION_TYPE_4_TO_SET_GESTURE);
		startActivity(it);
	}

	@Override
	public void toSMSNotificationActivity() {
		Intent intent = new Intent(getActivity(), SMSNotificationActivity.class);
		intent.putExtra("action_type", SMSNotificationActivity.ACTION_TYPE_4_TO_SET_TRADING_PASSWORD);
		intent.putExtra("countryCode", AppGlobal.COUNTRY_CODE_855_CAMBODIA);
		startActivity(intent);
	}

	@Override
	public void toPasswordActivity() {
		Intent intent = new Intent(getActivity(), PasswordActivity.class);
		intent.putExtra("action_type", PasswordActivity.ACTION_TYPE_1_FROM_HOME);
		startActivity(intent);
	}

	private void showCurrencyActivity(String currency, String balance) {
		Intent intent = new Intent(getActivity(), CurrencyActivity.class);
		intent.putExtra("currency", currency);
		intent.putExtra("balance", balance);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		if (v.getTag() != null) {
			TextView tvBalance = (TextView) v.findViewById(R.id.tv_menu_content);
			String tag = v.getTag().toString();
			String balance = tvBalance.getText().toString();
			switch (tag) {
				case AppGlobal.USD:
					showCurrencyActivity(AppGlobal.USD, balance);
					break;
				case AppGlobal.KHR:
					showCurrencyActivity(AppGlobal.KHR, balance);
					break;
				case AppGlobal.VND:
					showCurrencyActivity(AppGlobal.VND, balance);
					break;
				case AppGlobal.THB:
					showCurrencyActivity(AppGlobal.THB, balance);
					break;
			}
		} else {
			switch (v.getId()) {
				case R.id.ll_scan:
					Intent intent = new Intent(getActivity(), CaptureActivity.class);
					startActivityForResult(intent, M.RequestCode.REQUEST_CODE_1_SCAN);
					break;
				case R.id.ll_customer_service:
					showCustomerService();
					break;
				case R.id.ll_money:
					startActivity(new Intent(getActivity(), MoneyActivity.class));
					break;
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==Activity.RESULT_OK){
			switch (requestCode){
				case M.RequestCode.REQUEST_CODE_1_SCAN:
					Intent intent=new Intent(getActivity(),ScanResultActivity.class);
					intent.putExtra("code",data.getStringExtra("result"));
					startActivity(intent);
					break;
			}
		}
	}

	private void showCustomerService() {
		try {
			if (Session.customerService == null) return;
			final CharSequence[] tvContent = new CharSequence[Session.customerService.length()];
			int[] hotLineDrawable = new int[Session.customerService.length()];
			for (int i = 0; i < Session.customerService.length(); i++) {
				String phone = Session.customerService.optString(i);
				tvContent[i] = phone;
				hotLineDrawable[i] = R.drawable.ic_hotline_blue;
			}
			BottomDialog.Builder builder = new BottomDialog.Builder(getActivity()).setTvTitle(Utils.getString(R.string.hotline))
				.setTvContent2(tvContent)
				.setTvContent2Weight(2)
				.setTvContentColor(R.color.clr_hot_line_phone)
				.setIvRes(hotLineDrawable)
				.setClickListener(new IMyViewOnClickListener() {
					@Override
					public void onClick(View view, int position) {
						Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tvContent[position]));
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						getActivity().startActivity(intent);
					}

					@Override
					public void onLongClick(View view, int position) {

					}

					@Override
					public void onItemClick(List list, View view, int position) {

					}

					@Override
					public void onItemClick(JSONArray data, View view, int position) {

					}
				});
			builder.createAndShow();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (parent == mGvFunction) {
			switch (position) {
				case 0:
					Utils.toActivity(getActivity(), TransferActivity.class);
					break;
				case 1:
					showTopUp();
					break;
				case 2:
					showReceiveToAcct();
					break;
				case 3:
					Utils.toActivity(getActivity(), AceLoanActivity.class);
					break;
				case 4:
					Utils.toActivity(getActivity(), SalaryLoanActivity.class);
					break;
			}
		}
	}


	private void showReceiveToAcct() {
		Intent intent = new Intent(getActivity(), ReceiveToAcctActivity.class);
		startActivity(intent);
	}

	private void showTopUp() {
		Intent intent = new Intent(getActivity(), TopUpActivity.class);
		startActivity(intent);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onMessageEvent(MessageEvent messageEvent) {
		mPresenter.onMessageEvent(messageEvent);
	}

	private void doOtherDialogSetting() {
		mPresenter.doOtherDialogSetting();
	}

}
