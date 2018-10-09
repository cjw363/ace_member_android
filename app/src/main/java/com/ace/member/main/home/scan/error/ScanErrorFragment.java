package com.ace.member.main.home.scan.error;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ace.member.R;
import com.ace.member.base.BaseFragment;
import com.ace.member.toolbar.ToolBarConfig;
import com.og.utils.FileUtils;


public class ScanErrorFragment extends BaseFragment {

	public static final String TAG="ScanErrorFragment";

	public ScanErrorFragment(){
		Bundle bundle=new Bundle();
		bundle.putString("TAG",TAG);
		setArguments(bundle);
	}

	public static ScanErrorFragment newInstance(){
		return new ScanErrorFragment();
	}

	@Override
	protected int getContentViewLayout() {
		return R.layout.fragment_scan_error;
	}

	@Override
	protected void initView() {
		try {
			View view = getView();
			if(view == null) return;
			ToolBarConfig.builder(getActivity(), view).setTvTitleRes(R.string.error).build();
			Button btnBack = (Button) view.findViewById(R.id.btn_back);
			btnBack.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					getActivity().finish();
				}
			});
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}
}
