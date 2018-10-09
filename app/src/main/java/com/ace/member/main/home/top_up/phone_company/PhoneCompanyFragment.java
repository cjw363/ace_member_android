package com.ace.member.main.home.top_up.phone_company;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.adapter.RVPhoneCompanyAdapter;
import com.ace.member.bean.PhoneCompany;
import com.ace.member.event.SelectPhoneCompanyEvent;
import com.ace.member.simple_listener.SimpleViewClickListener;
import com.og.utils.EventBusUtil;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class PhoneCompanyFragment extends DialogFragment implements PhoneCompanyContract.View {
	@Inject
	PhoneCompanyPresenter mPresenter;
	@BindView(R.id.rfl_phone_company)
	FrameLayout mFlPhoneCompany;
	@BindView(R.id.rv_phone_company)
	RecyclerView mRvPhoneCompany;
	@BindView(R.id.tv_no_phone_company)
	TextView mTvNoPhoneCompany;
	Unbinder mUnbinder;
	private RVPhoneCompanyAdapter mAdapter;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_TITLE,0);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = null;
		try {
			Window mWindow = getDialog().getWindow();
			assert mWindow != null;
			view = inflater.inflate(R.layout.fragment_phone_company, ((ViewGroup) mWindow.findViewById(android.R.id.content)), false);
			mWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			mWindow.setLayout((int) (Utils.getScreenWidth() * 0.95), (int)(Utils.getScreenHeight() * 0.7));
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		mUnbinder = ButterKnife.bind(this, view);
		DaggerPhoneCompanyComponent.builder().phoneCompanyPresenterModule(new PhoneCompanyPresenterModule(this, getContext())).build().inject(this);
		//noinspection unchecked
		setPhoneCompanyList((List<PhoneCompany>) getArguments().getSerializable("phone_company"));
	}

	@Override
	public void onDestroy() {
		try {
			if(mUnbinder != null) mUnbinder.unbind();//解绑
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}

	@Override
	public void setPhoneCompanyList(List<PhoneCompany> list) {
		mTvNoPhoneCompany.setVisibility(Utils.isEmptyList(list) ? View.VISIBLE : View.GONE);
		if (mAdapter == null) {
			try {
				mRvPhoneCompany.setLayoutManager(new GridLayoutManager(getContext(), 3));
				mAdapter = new RVPhoneCompanyAdapter(getContext(), list);
				mRvPhoneCompany.setAdapter(mAdapter);
				mAdapter.setClickListener(new SimpleViewClickListener() {
					@Override
					public void onClick(View view, int position) {
						EventBusUtil.post(new SelectPhoneCompanyEvent(mAdapter.getPhoneCompany(position)));
						dismiss();
					}
				});
			} catch (Exception e) {
				FileUtils.addErrorLog(e);
			}
		} else {
			mAdapter.setPhoneCompany(list);
		}
	}
}
