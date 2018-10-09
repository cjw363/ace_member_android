package com.ace.member.main.home.ace_loan.repay_plan_list;

import android.os.Bundle;

import com.ace.member.R;
import com.ace.member.adapter.AceLoanRepayPlanAdapter;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.ACELoanRepayBean;
import com.ace.member.toolbar.ToolBarConfig;
import com.og.utils.FileUtils;
import com.og.utils.ListViewForScrollView;
import com.og.utils.Utils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class AceRepayPlanListActivity extends BaseActivity implements AceRepayPlanListContract.View {

	@Inject
	AceRepayPlanListPresenter mPresenter;

	@BindView(R.id.lv_repay)
	ListViewForScrollView mLvRepay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerAceRepayPlanListComponent.builder()
				.aceRepayPlanListPresenterModule(new AceRepayPlanListPresenterModule(this, this))
				.build()
				.inject(this);
		initActivity();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_ace_loan_repay_plan_list;
	}

	protected void initActivity() {
		new ToolBarConfig.Builder(this, null).setTvTitle(Utils.getString(R.string.repay_plan)).build();
		mPresenter.getRepayPlanList();
	}

	@Override
	public void initListView(List<ACELoanRepayBean> list) {
		try {
			if (list == null || list.size() == 0) {
				Utils.showToast(Utils.getString(R.string.no_data));
			} else {
				AceLoanRepayPlanAdapter adapter = new AceLoanRepayPlanAdapter(mContext, list);
				mLvRepay.setAdapter(adapter);
			}

		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}
}
