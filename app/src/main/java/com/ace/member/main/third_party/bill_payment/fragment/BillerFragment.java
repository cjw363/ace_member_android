package com.ace.member.main.third_party.bill_payment.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.adapter.SelectBillerAdapter;
import com.ace.member.bean.BillerBean;
import com.ace.member.event.SelectBillerCompanyEvent;
import com.og.utils.EventBusUtil;
import com.og.utils.Utils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BillerFragment extends Fragment implements BillerContract.View {

	@Inject
	BillerPresenter mPresenter;

	@BindView(R.id.lv_biller)
	ListView mLvBiller;
	@BindView(R.id.tv_no_data)
	TextView mTvNoData;

	private View mView;
	private List<BillerBean> mList;


	public static BillerFragment newInstance(int type) {
		BillerFragment fragment = new BillerFragment();
		Bundle b = new Bundle();
		b.putInt("type", type);
		fragment.setArguments(b);
		return fragment;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (mView == null) {
			mView = inflater.inflate(R.layout.view_biller_fragment, container, false);
		} else {
			ViewGroup viewGroup = (ViewGroup) mView.getParent();
			if (viewGroup != null) viewGroup.removeView(mView);
		}
		return mView;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		ButterKnife.bind(this, view);
		DaggerBillerComponent.builder()
				.billerPresenterModule(new BillerPresenterModule(this, getContext()))
				.build()
				.inject(this);
		initEvent();
		int type = getArguments().getInt("type");
		mPresenter.getBiller(type);
	}

	private void initEvent() {
		mLvBiller.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				BillerBean bean = mList.get(position);
				EventBusUtil.post(new SelectBillerCompanyEvent(bean));
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void initListView(List<BillerBean> list) {
		if (list == null || list.size() == 0) {
			mLvBiller.setVisibility(View.GONE);
			mTvNoData.setVisibility(View.VISIBLE);
		} else {
			mList = list;
			mLvBiller.setVisibility(View.VISIBLE);
			mTvNoData.setVisibility(View.GONE);
			SelectBillerAdapter adapter = new SelectBillerAdapter(getContext(), mList);
			mLvBiller.setAdapter(adapter);
		}
	}
}
