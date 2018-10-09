package com.ace.member.main.currency.new_bank;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.adapter.GVBankAdapter;
import com.ace.member.bean.BankAccount;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.SnackBarUtil;
import com.og.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class NewBankFragment extends DialogFragment implements NewBankContract.View {
	@Inject
	NewBankPresenter mPresenter;

	@BindView(R.id.rl_bank)
	RelativeLayout mRlBank;
	@BindView(R.id.rl_bank_info)
	RelativeLayout mRlBankInfo;
	@BindView(R.id.iv_bank)
	AppCompatImageView mIvBank;
	@BindView(R.id.tv_bank)
	TextView mTvBank;
	@BindView(R.id.tv_bank_account_name)
	TextView mTvBankAccountName;

	@BindView(R.id.et_bank_account_no)
	EditText mEtBankAccountNo;

	@BindView(R.id.btn_confirm)
	Button mBtnConfirm;
	@BindView(R.id.btn_cancel)
	Button mBtnCancel;

	@BindView(R.id.rl_bank_list)
	RelativeLayout mRlBankList;

	@BindView(R.id.tv_no_bank)
	TextView mTvNoBank;

	private List<BankAccount> mBankAccounts;
	private Unbinder mUnbinder;
	private String mCurrency;
	private Window mWindow;
	private int mWidth;
	private String mCurrentBankCode;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_TITLE,0);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mWindow = getDialog().getWindow();
		assert mWindow != null;
		View view = inflater.inflate(R.layout.fragment_new_bank, ((ViewGroup)mWindow.findViewById(android.R.id.content)), false);
		mWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		mWidth = (int) (Utils.getScreenWidth() * 0.95);
		int height = (int) (Utils.getScreenHeight() * 0.7);
		mWindow.setLayout(mWidth, height);
		mUnbinder = ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		DaggerNewBankComponent.builder().newBankPresenterModule(new NewBankPresenterModule(this, getContext())).build().inject(this);
		//noinspection unchecked
		mBankAccounts = (List<BankAccount>) getArguments().getSerializable("bank");
		if (mBankAccounts == null) mBankAccounts = new ArrayList<>();
		mCurrency = getArguments().getString("currency");
		GridView gridView = (GridView) view.findViewById(R.id.gv_bank);
		gridView.setEmptyView(mTvNoBank);
		GVBankAdapter gvBankAdapter = new GVBankAdapter(getContext(), mBankAccounts);
		gridView.setAdapter(gvBankAdapter);

		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mCurrentBankCode = mBankAccounts.get(position).getCode();
				mPresenter.onBankItemClick(mBankAccounts.get(position));
			}
		});

	}

	@Override
	public void onDestroy() {
		try {
			if(mUnbinder != null) mUnbinder.unbind();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}

	@OnClick({R.id.btn_confirm, R.id.btn_cancel})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.btn_confirm:
				AppUtils.enableSoftInput(view,false);
				mPresenter.addBank(mCurrency, mCurrentBankCode, mEtBankAccountNo.getText().toString().trim());
				break;
			case R.id.btn_cancel:
				AppUtils.enableSoftInput(getView(),false);
				mPresenter.onCancel();
				break;
		}
	}

	@Override
	public void finish() {
		dismiss();
	}

	@Override
	public void enableBank(boolean enable) {
		mRlBank.setVisibility(enable ? View.VISIBLE : View.GONE);
	}

	@Override
	public void enableBankList(boolean enable) {
		mRlBankList.setVisibility(enable ? View.VISIBLE : View.GONE);
	}

	@Override
	public void setWindowHeight(int height) {
		mWindow.setLayout(mWidth, height);
	}

	@Override
	public void clearBankAccountNo() {
		mEtBankAccountNo.setText("");
	}

	@Override
	public void setBankImageResource(int resource) {
		mIvBank.setImageResource(resource);
	}

	@Override
	public void setBankBackGroundColor(int color) {
		mRlBankInfo.setBackgroundColor(color);
	}

	@Override
	public void setBankName(String name) {
		mTvBank.setText(name);
	}

	@Override
	public void setBankNameColor(int color) {
		mTvBank.setTextColor(color);
	}

	@Override
	public void setBankAccountName(String name) {
		mTvBankAccountName.setText(name);
	}

	@Override
	public void setBankAccountNameColor(int color) {
		mTvBankAccountName.setTextColor(color);
	}

	@Override
	public void showToast(int res) {
		SnackBarUtil.show(mWindow.getDecorView().getRootView(),res, Snackbar.LENGTH_LONG);
	}
}
