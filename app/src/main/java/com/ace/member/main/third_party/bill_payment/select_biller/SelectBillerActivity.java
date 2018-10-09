package com.ace.member.main.third_party.bill_payment.select_biller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.ace.member.R;
import com.ace.member.adapter.BillerViewPagerAdapter;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.BillerBean;
import com.ace.member.event.SelectBillerCompanyEvent;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.view.PagerSlidingTabStrip;
import com.og.utils.FileUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class SelectBillerActivity extends BaseActivity {

	@BindView(R.id.tab_title)
	PagerSlidingTabStrip mTabTitle;
	@BindView(R.id.bill_pager)
	ViewPager mVBillPager;

	private String[] mTitles;

	private Map<String, List<BillerBean>> mList;

	@Override
	protected void onCreate(Bundle saveInstanceState) {
		super.onCreate(saveInstanceState);
		initActivity();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_select_biller;
	}

	protected void initActivity() {
		ToolBarConfig.builder(this, null).setTvTitleRes(R.string.biller).build();
		initFragments();
	}

	private void initFragments() {
		try {
			mTitles = new String[]{getResources().getString(R.string.finance), getResources().getString(R.string.insurance), getResources().getString(R.string.internet_and_tv), getResources().getString(R.string.others)};
			BillerViewPagerAdapter adapter = new BillerViewPagerAdapter(getSupportFragmentManager(), mTitles);
			mVBillPager.setAdapter(adapter);
			mTabTitle.setViewPager(mVBillPager);
			Intent it = getIntent();
			int type = it.getIntExtra("type", 1) - 1;
			mVBillPager.setCurrentItem(type);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@Subscribe
	public void onSelectBillerCompanyEvent(SelectBillerCompanyEvent event) {
		try {
			BillerBean bean = event.getBillerCompany();
			Intent it = new Intent();
			Bundle b = new Bundle();
			b.putInt("biller_id",bean.getID());
			it.putExtra("bundle", b);
			setResult(Activity.RESULT_OK, it);
			finish();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

}
