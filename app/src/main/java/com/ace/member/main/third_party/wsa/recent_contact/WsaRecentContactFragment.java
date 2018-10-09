package com.ace.member.main.third_party.wsa.recent_contact;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.adapter.LVWsaRecentContactAdapter;
import com.ace.member.bean.WsaBill;
import com.ace.member.event.SelectWsaRecentEvent;
import com.og.utils.EventBusUtil;
import com.og.utils.Utils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WsaRecentContactFragment extends DialogFragment implements WsaRecentContactContract.View {

	@Inject
	WsaRecentContactPresenter mPresenter;

	private Unbinder mUnbinder;
	@BindView(R.id.lv_recent_contact)
	ListView mLvRecentContact;
	@BindView(R.id.tv_empty)
	TextView mTvEmpty;
	LVWsaRecentContactAdapter mAdapter;

	public WsaRecentContactFragment() {
		Bundle bundle = new Bundle();
		bundle.putString("TAG", "EdcWsaRecentContactFragment");
		setArguments(bundle);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_TITLE, 0);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		Window window = getDialog().getWindow();
		assert window != null;
		View view = inflater.inflate(R.layout.fragment_edcwsa_recent_contact, ((ViewGroup) window.findViewById(android.R.id.content)), false);
		window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		int width = (int) (Utils.getScreenWidth() * 0.7);
		int height = (int) (Utils.getScreenHeight() * 0.6);
		window.setLayout(width, height);
		mUnbinder = ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		DaggerWsaRecentContactComponent.builder()
			.wsaRecentContactPresenterModule(new WsaRecentContactPresenterModule(this, getContext()))
			.build()
			.inject(this);

		mLvRecentContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				EventBusUtil.post(new SelectWsaRecentEvent(mAdapter.getItem(position)));
				WsaRecentContactFragment.this.dismiss();
			}
		});
		mPresenter.start();
	}


	@Override
	public void setList(List<WsaBill> list) {
		if (mAdapter == null) {
			mAdapter = new LVWsaRecentContactAdapter(getContext(), list);
			mLvRecentContact.setAdapter(mAdapter);
		} else mAdapter.setList(list);
	}

	@Override
	public void enableEmpty(boolean enable) {
		if (enable) {
			mLvRecentContact.setVisibility(View.GONE);
			mTvEmpty.setVisibility(View.VISIBLE);
		} else {
			mLvRecentContact.setVisibility(View.VISIBLE);
			mTvEmpty.setVisibility(View.VISIBLE);
		}
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
}
