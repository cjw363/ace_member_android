package com.ace.member.main.currency.new_bank;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.bean.BankAccount;
import com.ace.member.event.AddBankEvent;
import com.ace.member.utils.BankUtil;
import com.ace.member.utils.ColorUtil;
import com.ace.member.utils.Session;
import com.og.http.SimpleRequestListener;
import com.og.utils.EventBusUtil;
import com.og.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;


public class NewBankPresenter extends BasePresenter implements NewBankContract.Presenter {
	private final NewBankContract.View mView;

	@Inject
	public NewBankPresenter(Context context, NewBankContract.View view) {
		super(context);
		this.mView = view;
	}

	@Override
	public void addBank(String currency, final String bankCode, final String bankAccountNO) {
		if (TextUtils.isEmpty(currency)) {
			mView.showToast(R.string.invalid_currency);
			return;
		}
		if (TextUtils.isEmpty(bankCode)) {
			mView.showToast(R.string.select_bank);
			return;
		}
		if (TextUtils.isEmpty(bankAccountNO)) {
			mView.showToast(R.string.invalid_bank_account_no);
			return;
		}
		Map<String, String> map = new HashMap<>();
		map.put("_b", "aj");
		map.put("_a", "user");
		map.put("_s", Session.sSid);
		map.put("cmd", "addMemberBankAccount");
		map.put("currency", currency);
		map.put("bank_code", bankCode);
		map.put("bank_account_no", bankAccountNO);
		submit(map, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				mView.finish();
				EventBusUtil.post(new AddBankEvent(bankCode, bankAccountNO));
			}

			@Override
			public void loadFailure(int code, String result, String token) {
				mView.showToast(R.string.fail);
			}
		});
	}

	@Override
	public void onCancel() {
		mView.enableBank(false);
		mView.enableBankList(true);
		mView.setWindowHeight((int) (Utils.getScreenHeight() * 0.7));
	}

	@Override
	public void onBankItemClick(BankAccount bankAccount) {
		mView.setWindowHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		mView.clearBankAccountNo();
		mView.setBankImageResource(BankUtil.getBankImageResourceByBankCode(bankAccount.getCode()));
		mView.setBankBackGroundColor(ColorUtil.getBankBgColor(bankAccount.getCode()));
		mView.setBankAccountName(Session.user.getName());
		int bankTextColor = ColorUtil.getBankTextColor(bankAccount.getCode());
		mView.setBankName(bankAccount.getName());
		mView.setBankNameColor(bankTextColor);
		mView.setBankAccountNameColor(bankTextColor);
		mView.setBankBackGroundColor(ColorUtil.getBankBgColor(bankAccount.getCode()));
		mView.enableBankList(false);
		mView.enableBank(true);
	}

}
