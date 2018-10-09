package com.ace.member.main.me.service_point;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.ServicePoint;
import com.ace.member.utils.AppGlobal;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class ServicePointActivity extends BaseActivity implements ServicePointContract.ServicePointView, OnMapReadyCallback, CompoundButton.OnCheckedChangeListener {

	@Inject
	ServicePointPresenter mServicePointPresenter;

	@BindView(R.id.ll_info)
	LinearLayout mLlInfo;
	@BindView(R.id.cb_branch)
	CheckBox mCbBranch;
	@BindView(R.id.cb_agent)
	CheckBox mCbAgent;

	private GoogleMap mGoogleMap;
	protected SupportMapFragment mSupportMapFragment;

	protected boolean mIsBranchChecked = true;
	protected boolean mIsAgentChecked = true;

	private List<ServicePoint> mBranchList;
	private List<ServicePoint> mAgentList;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerServicePointComponent.builder()
			.servicePointPresenterModule(new ServicePointPresenterModule(this, this))
			.build()
			.inject(this);
		initActivity();
		initData();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_service_point;
	}

	@Override
	protected void initActivity() {
		mSupportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		mSupportMapFragment.getMapAsync(this);
		mCbBranch.setOnCheckedChangeListener(this);
		mCbAgent.setOnCheckedChangeListener(this);
	}

	public void initData() {
		mServicePointPresenter.getServicePoint();
	}


	@Override
	public void onMapReady(GoogleMap map) {
		mGoogleMap = map;
		mGoogleMap.setInfoWindowAdapter(new MyMarkerAdapter());
		LatLng cambodia = new LatLng(11.544184, 104.892504);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(cambodia, 7));
	}

	@Override
	public void updateServicePoint() {
		mGoogleMap.clear();
		if (mIsBranchChecked) {
			initBranch();
		}
		if (mIsAgentChecked) {
			initAgent();
		}
	}

	@Override
	public void setList(ArrayList<ServicePoint> branchList, ArrayList<ServicePoint> agentList) {
		this.mBranchList = branchList;
		this.mAgentList = agentList;
	}

	@Override
	public void setCount(int agentCount, int branchCount) {
		mCbAgent.setText(String.format(getResources().getString(R.string.agent_count), agentCount));
		mCbBranch.setText(String.format(getResources().getString(R.string.branch_count), branchCount));
	}

	@Override
	public void showInfo() {
		mLlInfo.setVisibility(View.VISIBLE);
	}

	private void initBranch() {
		try {
			int len = mBranchList.size();
			for (int i = 0; i < len; i++) {
				BitmapDescriptor bitMap = BitmapDescriptorFactory.fromResource(R.drawable.ic_map_mark_yellow);
				ServicePoint point = mBranchList.get(i);
				LatLng sydney = new LatLng(Double.parseDouble(point.getLatitude()), Double.parseDouble(point
					.getLongitude()));
				String title = String.format(Utils.getString(R.string.bracket),Utils.getString(R.string.branch)) + " " + point.getName();
				String snippet = point.getAddress();
				if (!snippet.isEmpty() && !point.getAddressRemark().isEmpty()) {
					snippet += " (" + point.getAddressRemark() + ")";
				}
				if (!point.getAddressKH().isEmpty()) {
					snippet += "\n" + point.getAddressKH();
					if (!snippet.isEmpty() && !point.getAddressRemarkKH().isEmpty()) {
						snippet += " (" + point.getAddressRemarkKH() + ")";
					}
				}
				Marker marker = mGoogleMap.addMarker(new MarkerOptions().position(sydney)
					.icon(bitMap)
					.title(title));
				if (!snippet.isEmpty()) {
					marker.setSnippet(snippet);
				}
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	private void initAgent() {
		try {
			int len = mAgentList.size();
			for (int i = 0; i < len; i++) {
				BitmapDescriptor bitMap = BitmapDescriptorFactory.fromResource(R.drawable.ic_map_mark_blue);
				ServicePoint point = mAgentList.get(i);
				LatLng sydney = new LatLng(Double.parseDouble(point.getLatitude()), Double.parseDouble(point
					.getLongitude()));
				String title = String.format(Utils.getString(R.string.bracket),Utils.getString(R.string.agent)) + " " + point.getName();
				String snippet = point.getAddress();
				if (!snippet.isEmpty() && !point.getAddressRemark().isEmpty()) {
					snippet += " (" + point.getAddressRemark() + ")";
				}
				if (!point.getAddressKH().isEmpty()) {
					snippet += "\n" + point.getAddressKH();
					if (!snippet.isEmpty() && !point.getAddressRemarkKH().isEmpty()) {
						snippet += " (" + point.getAddressRemarkKH() + ")";
					}
				}
				Marker marker = mGoogleMap.addMarker(new MarkerOptions().position(sydney)
					.icon(bitMap)
					.title(title));
				if (!snippet.isEmpty()) {
					marker.setSnippet(snippet);
				}
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	/**
	 * 设置标记内容的样式
	 */
	private class MyMarkerAdapter implements GoogleMap.InfoWindowAdapter {

		@Override
		public View getInfoWindow(Marker marker) {
			return null;
		}

		@Override
		public View getInfoContents(Marker marker) {
			View view = LayoutInflater.from(ServicePointActivity.this)
				.inflate(R.layout.view_marker_info_window, null);
			setViewContent(marker, view);
			return view;
		}

		private void setViewContent(Marker marker, View view) {
			TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
			tvTitle.setText(marker.getTitle());
			TextView tvAddress = (TextView) view.findViewById(R.id.tv_address);
			tvAddress.setText(marker.getSnippet());
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
			case R.id.cb_branch:
				if (isChecked) {
					mIsBranchChecked = true;
					initBranch();
				} else {
					mIsBranchChecked = false;
					updateServicePoint();
				}
				break;
			case R.id.cb_agent:
				if (isChecked) {
					mIsAgentChecked = true;
					initAgent();
				} else {
					mIsAgentChecked = false;
					updateServicePoint();
				}
				break;
		}
	}
}
