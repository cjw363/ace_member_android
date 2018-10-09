package com.ace.member.main.me.portrait;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.PortraitBean;
import com.ace.member.main.bottom_dialog.BottomDialog;
import com.ace.member.main.image_detail.Image;
import com.ace.member.main.image_detail.ImageDetailActivity;
import com.ace.member.simple_listener.SimpleViewClickListener;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.M;
import com.ace.member.utils.SnackBarUtil;
import com.ace.member.utils.UriUtil;
import com.bumptech.glide.Glide;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class PortraitActivity extends BaseActivity implements PortraitContract.View{

	@Inject
	PortraitPresenter mPresenter;

	@BindView(R.id.profile_image)
	AppCompatImageView mProfileImage;
	@BindView(R.id.iv_take_photo)
	AppCompatImageView mIvTakePhoto;
	@BindView(R.id.btn_submit)
	Button mBtnSubmit;
	private String mPath;
	private List<Image> mList;
	private BottomDialog mSelectImageDlg;
	private PortraitBean mPortraitBean;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActivity();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_portrait;
	}

	@Override
	protected void initActivity() {
		ToolBarConfig.builder(this,null).setTvTitleRes(R.string.update_portrait).build();
		DaggerPortraitComponent.builder().portraitPresenterModule(new PortraitPresenterModule(this,this)).build().inject(this);
		mPresenter.start();
	}


	private void toImagePicker() {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		startActivityForResult(intent, AppGlobal.IMAGE_CODE_1_PICKER);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (mList == null) mList = new ArrayList<>();
			else mList.clear();
			Image image = new Image();
			String uri;
			if (requestCode == AppGlobal.IMAGE_CODE_1_PICKER) {
				if (data == null) return;
				mPath = UriUtil.getPhotoPathFromContentUri(this, data.getData());
			} else if (requestCode == AppGlobal.IMAGE_CODE_2_CAMERA) {
				AppUtils.scanFile(this, mPath, null);
			}
			uri = "file://" + mPath;
			image.setUri(uri);
			image.setDate(mPath);
			mList.add(image);
			Glide.with(this)
				.load(uri)
				.into(mProfileImage);
		}
	}

	private void toImageDetail() {
		Intent intent = new Intent(PortraitActivity.this, ImageDetailActivity.class);
		intent.putExtra(M.ImagePicker.OPEN_MENU, true);
		if(mPortraitBean!=null && Utils.isEmptyList(mList)){
			List<String>list= Collections.singletonList(mPortraitBean.getRealNormalFileName());
			intent.putExtra(M.ImagePicker.IMAGES, (Serializable) list);
		}else {
			intent.putExtra(M.ImagePicker.IMAGES, (Serializable) AppUtils.dealImageList(mList));
		}
		intent.putExtra(M.ImagePicker.CURRENT, 0);
		intent.putExtra(M.ImagePicker.OPEN_MENU,false);
		ActivityOptionsCompat options = ActivityOptionsCompat.makeScaleUpAnimation(mProfileImage, 0, 0, mProfileImage.getWidth(), mProfileImage.getHeight());
		startActivity(intent, options.toBundle());
	}

	private void showSelectImageDlg() {
		if (mSelectImageDlg == null) {
			BottomDialog.Builder builder = new BottomDialog.Builder(this);
			mSelectImageDlg = builder.setTvTitle(Utils.getString(R.string.select))
				.setIvRes(new int[]{R.drawable.ic_take_picture, R.drawable.ic_album})
				.setTvContent2(new String[]{Utils.getString(R.string.take_picture), Utils.getString(R.string.select_from_album)})
				.setClickListener(new SimpleViewClickListener() {
					@Override
					public void onClick(View view, int position) {
						if (position == 0) toCamera();
						else toImagePicker();
					}
				})
				.create();
		}
		mSelectImageDlg.show();
	}

	private void toCamera() {
		PERMISSIONS = new String[]{Manifest.permission.CAMERA};
		if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
			setPermissions();
		} else {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			mPath = FileUtils.getCameraImageFilePath() + UUID.randomUUID()
				.toString() + ".jpg";
			if (TextUtils.isEmpty(mPath)) {
				Utils.showToast(R.string.not_enough_storage_space);
				return;
			}
			intent.putExtra(MediaStore.EXTRA_OUTPUT, FileUtils.getUriForFile(new File(mPath)));
			startActivityForResult(intent, AppGlobal.IMAGE_CODE_2_CAMERA);
		}
	}

	@OnClick({R.id.profile_image, R.id.iv_take_photo, R.id.btn_submit})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.profile_image:
				if(!Utils.isEmptyList(mList) || (mPortraitBean!=null && !TextUtils.isEmpty(mPortraitBean.getPortrait()))){
					toImageDetail();
				}
				break;
			case R.id.iv_take_photo:
				showSelectImageDlg();
				break;
			case R.id.btn_submit:
				mPresenter.upload(mList);
				break;
		}
	}

	@Override
	public void setPortrait(PortraitBean portrait) {
		mPortraitBean=portrait;
		if(mPortraitBean==null || TextUtils.isEmpty(mPortraitBean.getPortrait())){
			mProfileImage.setImageResource(R.drawable.head_portrait_1);
		}else {
			String image=mPortraitBean.getRealThumbnailFileName();
			Glide.with(this).load(image).asBitmap().into(mProfileImage);
		}
	}

	@Override
	public void showSuccess() {
		SnackBarUtil.show(getWindow().findViewById(android.R.id.content), R.string.success, Snackbar.LENGTH_LONG, new Snackbar.Callback() {
			@Override
			public void onDismissed(Snackbar transientBottomBar, int event) {
				finish();
			}
		});
	}

	@Override
	public void enableSubmit(boolean enable) {
		mBtnSubmit.setEnabled(enable);
	}
}
