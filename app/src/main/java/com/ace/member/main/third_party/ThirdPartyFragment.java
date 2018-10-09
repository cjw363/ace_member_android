package com.ace.member.main.third_party;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.ace.member.R;
import com.ace.member.adapter.GVMenuAdapter;
import com.ace.member.base.BaseFragment;
import com.ace.member.main.third_party.bill_payment.BillPaymentActivity;
import com.ace.member.main.third_party.edc.EdcActivity;
import com.ace.member.main.third_party.lotto.LottoActivity;
import com.ace.member.main.third_party.samrithisak_loan.SamrithisakLoanActivity;
import com.ace.member.main.third_party.wsa.WsaActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.og.LibSession;
import com.og.utils.Utils;

import javax.inject.Inject;

import butterknife.BindView;

public class ThirdPartyFragment extends BaseFragment implements ThirdPartyContract.View, AdapterView.OnItemClickListener {

	@BindView(R.id.gv_function)
	GridView mGvFunction;

	@Inject
	ThirdPartyPresenter mPresenter;

	@Override
	protected int getContentViewLayout() {
		return R.layout.activity_fragment_party;
	}

	@Override
	protected void initView() {
		DaggerThirdPartyComponent.builder()
			.thirdPartyPresenterModule(new ThirdPartyPresenterModule(this, getContext()))
			.build()
			.inject(this);

		ToolBarConfig.builder(null, getView())
			.setTvTitleRes(R.string.service)
			.setEnableBack(false)
			.build();

		int[] menuDrawable;
		int[] menuTitle;
		if (LibSession.sFlagDev) {
			menuDrawable = new int[]{R.drawable.ic_electricity, R.drawable.ic_water, R.drawable.ic_bill_payment, R.drawable.ic_lotto, R.drawable.ic_scratch, R.drawable.ic_khe, R.drawable.ic_samrithisak};
			menuTitle = new int[]{R.string.electricity, R.string.water_supply, R.string.bill_payment, R.string.lotto, R.string.scratch, R.string.kh_express, R.string.samrithisak_loan};
		} else {
			menuDrawable = new int[]{R.drawable.ic_electricity, R.drawable.ic_water, R.drawable.ic_bill_payment};
			menuTitle = new int[]{R.string.electricity, R.string.water_supply, R.string.bill_payment};
		}
		mGvFunction = (GridView) getView().findViewById(R.id.gv_function);
		GVMenuAdapter gvMenuAdapter = new GVMenuAdapter(menuDrawable, menuTitle);
		mGvFunction.setAdapter(gvMenuAdapter);
		mGvFunction.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (parent == mGvFunction) {
			switch (position) {
				case 0:
					Utils.toActivity(getActivity(), EdcActivity.class);
					break;
				case 1:
					Utils.toActivity(getActivity(), WsaActivity.class);
					break;
				case 2:
					Utils.toActivity(getActivity(), BillPaymentActivity.class);
					break;
				case 3:
					Utils.toActivity(getActivity(), LottoActivity.class);
					break;
				case 6:
					Utils.toActivity(getActivity(), SamrithisakLoanActivity.class);
					break;
			}
		}
	}
}
