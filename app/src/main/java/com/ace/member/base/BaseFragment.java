package com.ace.member.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.og.update.PermissionsChecker;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {
	protected String TAG = "BaseFragment";
	private View mView;

	//要加入6.0以后的限制权限
	protected static final int PERMISSION_REQUEST_CODE = 0; // 系统权限管理页面的参数
	protected String[] PERMISSIONS;//= new String[]{android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
	protected PermissionsChecker mPermissionsChecker;
	private Unbinder mUnbinder;


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
		mPermissionsChecker = new PermissionsChecker(getContext());
		if (mView == null) {
			mView = inflater.inflate(getContentViewLayout(), container, false);
		} else {
			ViewGroup viewGroup = (ViewGroup) mView.getParent();
			if (viewGroup != null) viewGroup.removeView(mView);
		}
		mUnbinder = ButterKnife.bind(this,mView);//绑定fragment
		return mView;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		//不要更改以下两者位置
		initData();
		initView();
	}

	protected abstract int getContentViewLayout();

	/**
	 * 非抽象方法，子类可以根据自己需要来重写该方法
	 */
	protected void initData() {}

	protected abstract void initView();

	protected void setPermissions() {
		//		PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
		try {
			ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_REQUEST_CODE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void flashData() {}

	@Override
	public void onDestroy() {
		try {
			if(mUnbinder != null) mUnbinder.unbind();//解绑
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}
}
