package com.ace.member.main.me;


import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.adapter.LVMenuAdapter;
import com.ace.member.base.BaseFragment;
import com.ace.member.bean.Balance;
import com.ace.member.login.LoginActivity;
import com.ace.member.main.currency.CurrencyActivity;
import com.ace.member.main.me.about.AboutActivity;
import com.ace.member.main.me.exchange.ExchangeActivity;
import com.ace.member.main.me.language.LanguageActivity;
import com.ace.member.main.me.log.LogActivity;
import com.ace.member.main.me.password.PasswordActivity;
import com.ace.member.main.me.payment_history.PaymentHistoryActivity;
import com.ace.member.main.me.portrait.PortraitActivity;
import com.ace.member.main.me.service_point.ServicePointActivity;
import com.ace.member.main.me.statement.StatementActivity;
import com.ace.member.main.me.system_update.SystemUpdateActivity;
import com.ace.member.main.me.transaction.TransactionActivity;
import com.ace.member.main.verify_certificate.VerifyCertificateActivity;
import com.ace.member.popup_window.MenuPopWindow;
import com.ace.member.popup_window.MyQRCodePopWindow;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.GlideUtil;
import com.ace.member.utils.Session;
import com.ace.member.utils.StringUtil;
import com.ace.member.view.MyListView;
import com.ace.member.view.RoundRectImageView;
import com.og.LibGlobal;
import com.og.utils.CustomDialog;
import com.og.utils.DialogFactory;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class MeFragment extends BaseFragment implements MeContract.MeView, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

	@BindView(R.id.ll_balance)
	LinearLayout mLLBalance;

	@BindView(R.id.srl)
	SwipeRefreshLayout mRefreshLayout;

	@Inject
	MePresenter mMePresenter;

	@BindView(R.id.rl_portrait)
	RelativeLayout mRlPortrait;
	@BindView(R.id.tv_my_name)
	TextView mTvMyName;
	@BindView(R.id.tv_my_phone)
	TextView mTvMyPhone;
	@BindView(R.id.ll_me_menu)
	LinearLayout mLlMenu;
	@BindView(R.id.iv_menu)
	ImageView mIvMenu;
	@BindView(R.id.iv_phone)
	AppCompatImageView mIvPhone;
	@BindView(R.id.iv_certificate)
	AppCompatImageView mIvCertificate;
	@BindView(R.id.iv_finger)
	AppCompatImageView mIvFinger;
	@BindView(R.id.scroll_view)
	ScrollView mScrollView;
	@BindView(R.id.iv_level)
	AppCompatImageView mIvLevel;
	@BindView(R.id.profile_image)
	RoundRectImageView mProfileImage;

	private LayoutInflater mInflater;
	private int[] mFunctionIcon = {R.drawable.ic_statement, R.drawable.ic_transaction, R.drawable.ic_payment_history, R.drawable.ic_exchange, R.drawable.ic_service_point};
	private int[] mFunctionIconName = {R.string.statement, R.string.transaction, R.string.payment_history, R.string.exchange, R.string.service_point};

	private ArrayList<Integer> mFunctionMoreIcon = new ArrayList<>(Arrays.asList(R.drawable.ic_id_certificate,R.drawable.ic_password, R.drawable.ic_language, R.drawable.ic_update, R.drawable.ic_log, R.drawable.ic_about, R.drawable.ic_logout));
	private ArrayList<Integer> mFunctionMoreIconName = new ArrayList<>(Arrays.asList(R.string.id_certificate,R.string.password, R.string.language, R.string.system_update, R.string.log, R.string.about, R.string.logout));

	private MenuPopWindow mMenuPopWindow;
	private MyQRCodePopWindow mQRCodePopWindow;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInflater = getActivity().getLayoutInflater();
	}

	@Override
	protected int getContentViewLayout() {
		return R.layout.activity_fragment_me;
	}

	@Override
	protected void initView() {
		DaggerMeComponent.builder()
			.mePresenterModule(new MePresenterModule(this, getContext()))
			.build()
			.inject(this);

		mRefreshLayout.setColorSchemeColors(Utils.getColor(R.color.colorPrimary));
		mRefreshLayout.setOnRefreshListener(this);

		//解决滑动冲突
		mScrollView.getViewTreeObserver()
			.addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
				@Override
				public void onScrollChanged() {
					mRefreshLayout.setEnabled(mScrollView!=null && mScrollView.getScrollY() == 0);
				}
			});

		ToolBarConfig.Builder builder = new ToolBarConfig.Builder(null, getView());
		builder.setTvTitleRes(R.string.me).setEnableBack(false);
		ToolBarConfig toolBarConfig = builder.setIvMenuRes(R.drawable.ic_menu)
			.setEnableMenu(true)
			.build();
		toolBarConfig.enableDot(Session.hasNewVersion);
		mTvMyName.setText(Session.user.getName());
		mTvMyPhone.setText(StringUtil.phoneReplaceWithStar(Session.user.getPhone()));
		initMenuPopWindow();
		showMenu();
		showBalance();
		updatePhoneIcon(Session.isPhoneVerified);
		updateCertificateIcon(Session.isIdVerified);
		updateFingerPrintIcon(Session.isFingerprintVerified);
		setLevelIcon(Session.user.getLevel());
	}

	private void showMenu() {
		mLlMenu.removeAllViews();
		View view = mInflater.inflate(R.layout.view_function, mLlMenu, false);
		MyListView lvMenu = (MyListView) view.findViewById(R.id.lv_function);
		LVMenuAdapter adapter = new LVMenuAdapter(mFunctionIcon, mFunctionIconName);
		lvMenu.setAdapter(adapter);
		MyOnItemClickListener onItemClick = new MyOnItemClickListener();
		lvMenu.setOnItemClickListener(onItemClick);
		mLlMenu.addView(view);
	}

	private void initMenuPopWindow() {
		mMenuPopWindow = new MenuPopWindow.Builder(getContext(), mFunctionMoreIconName, new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int resourceId = mFunctionMoreIcon.get(position);
				Intent it;
				switch (resourceId) {
					case R.drawable.ic_id_certificate:
						//上传照片
						Utils.toActivity(getActivity(), VerifyCertificateActivity.class);
						break;
					case R.drawable.ic_password:
						it = new Intent(getActivity(), PasswordActivity.class);
						startActivity(it);
						onResume();
						break;
					case R.drawable.ic_language:
						it = new Intent(getActivity(), LanguageActivity.class);
						startActivity(it);
						onResume();
						break;
					case R.drawable.ic_update:
						it = new Intent(getActivity(), SystemUpdateActivity.class);
						getActivity().startActivity(it);
						break;
					case R.drawable.ic_log:
						it = new Intent(getActivity(), LogActivity.class);
						startActivity(it);
						break;
					case R.drawable.ic_about:
						it = new Intent(getActivity(), AboutActivity.class);
						startActivity(it);
						break;
					case R.drawable.ic_logout:
						try {
							Dialog mDialog = new CustomDialog.Builder(getContext()).setMessage(R.string.sure_logout)
								.setIcon(R.drawable.ic_warining)
								.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialogInterface, int i) {
										mMePresenter.logout();
										dialogInterface.dismiss();
									}
								})
								.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialogInterface, int i) {
										dialogInterface.dismiss();
									}
								})
								.create();
							mDialog.setCancelable(false);
							//							if (mDialog.getWindow() != null) {
							//								mDialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
							//							}
							mDialog.show();
						} catch (Exception e) {
							FileUtils.addErrorLog(e);
						}
						break;

				}
				mMenuPopWindow.dismiss();
			}
		}).setItemIcons(mFunctionMoreIcon)
			.enableDot(mFunctionMoreIconName.indexOf(R.string.system_update), Session.hasNewVersion)
			.build();
	}

	public void showBalance() {
		mLLBalance.removeAllViews();
		List<Balance> balanceList = Session.balanceList;
		if (balanceList == null) {
			View view = View.inflate(getContext(), R.layout.view_lv_menu, null);
			AppCompatImageView ivCurrency = (AppCompatImageView) view.findViewById(R.id.iv_menu_icon);
			TextView tvCurrency = (TextView) view.findViewById(R.id.tv_menu_title);
			TextView tvBalance = (TextView) view.findViewById(R.id.tv_menu_content);
			ivCurrency.setImageResource(R.drawable.ic_balance);
			tvCurrency.setText(Utils.getString(R.string.balance_usd));
			tvBalance.setText(Utils.format("0", 2));
			view.setTag(AppGlobal.USD);
			view.setOnClickListener(this);
			mLLBalance.addView(view);
			return;
		}
		for (int i = 0; i < balanceList.size(); i++) {
			View view = View.inflate(getContext(), R.layout.view_lv_menu, null);
			AppCompatImageView ivCurrency = (AppCompatImageView) view.findViewById(R.id.iv_menu_icon);
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
			ivCurrency.setImageResource(R.drawable.ic_balance);
			view.setOnClickListener(this);
			mLLBalance.addView(view);
		}
	}

	@Override
	public void updatePhoneIcon(boolean enable) {
		mIvPhone.setEnabled(enable);
	}

	@Override
	public void updateCertificateIcon(boolean enable) {
		mIvCertificate.setEnabled(enable);
	}

	@Override
	public void updateFingerPrintIcon(boolean enable) {
		mIvFinger.setEnabled(enable);
	}

	@Override
	public void updateBalance() {
		showBalance();
	}

	@Override
	public void setLevelIcon(int level) {
		switch (level) {
			case AppGlobal.MEMBER_LEVEL_1_STANDARD:
				mIvLevel.setImageResource(R.drawable.ic_level_standard);
				break;
			case AppGlobal.MEMBER_LEVEL_2_SILVER:
				mIvLevel.setImageResource(R.drawable.ic_level_silver);
				break;
			case AppGlobal.MEMBER_LEVEL_3_GOLD:
				mIvLevel.setImageResource(R.drawable.ic_level_gold);
				break;
			case AppGlobal.MEMBER_LEVEL_4_DIAMOND:
				mIvLevel.setImageResource(R.drawable.ic_level_diamond);
				break;
		}
	}

	@Override
	public void updatePortrait() {
		GlideUtil.loadThumbnailPortrait(getActivity(),Session.user.getPortrait(),mProfileImage);
	}

	@Override
	public void onClick(View v) {
		if (v.getTag() != null) {
			TextView tvBalance = (TextView) v.findViewById(R.id.tv_menu_content);
			switch (v.getTag().toString()) {
				case AppGlobal.USD:
					showCurrencyActivity(AppGlobal.USD, tvBalance.getText().toString());
					break;
				case AppGlobal.KHR:
					showCurrencyActivity(AppGlobal.KHR, tvBalance.getText().toString());
					break;
				case AppGlobal.VND:
					showCurrencyActivity(AppGlobal.VND, tvBalance.getText().toString());
					break;
				case AppGlobal.THB:
					showCurrencyActivity(AppGlobal.THB, tvBalance.getText().toString());
					break;
			}
		}
	}

	@OnClick({R.id.ll_toolbar_menu, R.id.iv_my_qr_code,R.id.rl_portrait})
	public void onViewClicked(View view) {
		switch (view.getId()){
			case R.id.ll_toolbar_menu:
				mMenuPopWindow.showAsDropDown(getActivity(), mIvMenu);
				break;
			case R.id.iv_my_qr_code:
				mQRCodePopWindow = new MyQRCodePopWindow(getActivity());
				mQRCodePopWindow.showPopWindow(getActivity(), Gravity.CENTER);
				break;
			case R.id.rl_portrait:
				Utils.toActivity(getActivity(),PortraitActivity.class);
				break;
		}

	}

	@Override
	public void showRefreshStatus(boolean isRefreshing) {
		mRefreshLayout.setRefreshing(isRefreshing);
	}

	@Override
	public void showRefreshResult(String msg) {
		Utils.showToast(msg);
	}

	@Override
	public void onRefresh() {
		mMePresenter.getBalanceAndStatus(true);
	}

	private class MyOnItemClickListener implements AdapterView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
			int id = mFunctionIcon[i];
			Intent it;
			switch (id) {
				case R.drawable.ic_statement:
					it = new Intent(getActivity(), StatementActivity.class);
					startActivity(it);
					break;
				case R.drawable.ic_transaction:
					it = new Intent(getActivity(), TransactionActivity.class);
					startActivity(it);
					break;
				case R.drawable.ic_payment_history:
					it = new Intent(getActivity(), PaymentHistoryActivity.class);
					startActivity(it);
					break;
				case R.drawable.ic_service_point:
					if (Utils.isPackageInstall(LibGlobal.GOOGLE_PLAY_STORE) || Utils.isPackageInstall(LibGlobal.GOOGLE_PLAY_SERVICE)) {
						DialogFactory.ToastDialog(getContext(), getResources().getString(R.string.install_google_services), 0);
					} else {
						PERMISSIONS = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
						if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
							setPermissions();
						} else {
							it = new Intent(getActivity(), ServicePointActivity.class);
							startActivity(it);
						}
					}
					break;
				case R.drawable.ic_exchange:
					it = new Intent(getActivity(), ExchangeActivity.class);
					getActivity().startActivity(it);
					break;
			}
		}
	}

	@Override
	public void toLogout() {
		Session.clear();
		Intent it = new Intent(getActivity(), LoginActivity.class);
		startActivity(it);
		getActivity().finish();
		//			Process.killProcess(LibApplication.getMainTid());
	}

	private void showCurrencyActivity(String currency, String balance) {
		Intent intent = new Intent(getActivity(), CurrencyActivity.class);
		intent.putExtra("currency", currency);
		intent.putExtra("balance", balance);
		startActivity(intent);
	}

	@Override
	public void onDestroyView() {
		if (mMenuPopWindow != null) mMenuPopWindow.dismiss();
		super.onDestroyView();
	}

	@Override
	public void onResume() {
		super.onResume();
		mMePresenter.getBalanceAndStatus(false);
	}
}
