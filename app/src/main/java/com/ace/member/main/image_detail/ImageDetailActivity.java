package com.ace.member.main.image_detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.ace.member.R;
import com.ace.member.utils.M;
import com.og.utils.FileUtils;

public class ImageDetailActivity extends AppCompatActivity {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fl_content);
		initView();
	}

	private void initView() {
		try {
			ImageDetailFragment imageDetailFragment = (ImageDetailFragment) getSupportFragmentManager().findFragmentByTag(ImageDetailFragment.TAG);
			if (imageDetailFragment == null) {
				imageDetailFragment = new ImageDetailFragment();
				imageDetailFragment.getArguments()
					.putBoolean(M.ImagePicker.OPEN_MENU, getIntent().getBooleanExtra(M.ImagePicker.OPEN_MENU, false));
				imageDetailFragment.getArguments()
					.putStringArrayList(M.ImagePicker.IMAGES, getIntent().getStringArrayListExtra(M.ImagePicker.IMAGES));
				imageDetailFragment.getArguments()
					.putInt(M.ImagePicker.CURRENT, getIntent().getIntExtra(M.ImagePicker.CURRENT, 0));
				FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
				transaction.add(R.id.fl_content, imageDetailFragment, imageDetailFragment.getArguments()
					.getString(M.ImagePicker.FRAGMENT_NAME));
				transaction.commit();
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, android.R.anim.fade_out);
		finish();
	}
}
