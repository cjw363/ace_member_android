package com.ace.member.main.third_party.samrithisak_loan;

import android.content.Context;
import android.support.design.widget.Snackbar;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.bean.MemberLoanDateWrapper;
import com.ace.member.bean.MemberLoanPartner;
import com.ace.member.bean.MemberLoanPartnerBill;
import com.ace.member.utils.Session;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;


public class SamrithisakLoanPresenter extends BasePresenter implements SamrithisakLoanContract.Presenter {
	private final SamrithisakLoanContract.View mView;

	@Inject
	public SamrithisakLoanPresenter(SamrithisakLoanContract.View view, Context context) {
		super(context);
		this.mView = view;
	}

	@Override
	public void start(final boolean isRefresh) {
		Map<String, String> map = new HashMap<>();
		map.put("_s", Session.sSid);
		map.put("_a", "loan");
		map.put("_b", "aj");
		map.put("cmd", "getLoanDate");
		submit(map, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				try {
					mView.setEnableRefresh(false);
					if(isRefresh) Utils.showToast(R.string.success);
					MemberLoanDateWrapper dateWrapper = JsonUtil.jsonToBean(result, MemberLoanDateWrapper.class);
					assert dateWrapper != null;
					MemberLoanPartner loanPartner = dateWrapper.getLoanPartner();
					MemberLoanPartnerBill bill = dateWrapper.getLoanPartnerBill();
					mView.setCreditAndLoan(loanPartner);
					mView.setLatestBill(bill);
					DecimalFormat df = new DecimalFormat("0.00%");
					mView.setServiceChargeRate(String.format(Utils.getString(R.string.service_charge_info2),df.format(dateWrapper.getServiceChargeRate()*0.01),Utils.format(dateWrapper.getServiceChargeMinAmount(),2)));
					if (loanPartner == null) {
						mView.setEnableFunction(false);
						mView.setEnableLoan(false);
						mView.setEnableReturnLoan(false);
						Utils.showToast(R.string.no_loan_credit, Snackbar.LENGTH_LONG);
					} else {
						mView.setEnableFunction(true);
						mView.setEnableLoan(loanPartner.getCredit()>0 && loanPartner.getCredit()>loanPartner.getLoan());
						mView.setEnableReturnLoan(loanPartner.getLoan()>0);
					}
				} catch (Exception e) {
					e.printStackTrace();
					mView.setEnableFunction(false);
					mView.setEnableLoan(false);
					mView.setEnableReturnLoan(false);
					mView.setServiceChargeRate(Utils.getString(R.string.service_charge_rate)+": 0");
					FileUtils.addErrorLog(e);
				}
			}

			@Override
			public void loadFailure(int errorCode, String result, String uniqueToken) {
				mView.setEnableRefresh(false);
				if(isRefresh) Utils.showToast(R.string.fail);
				mView.setEnableFunction(false);
			}
		});
	}
}
