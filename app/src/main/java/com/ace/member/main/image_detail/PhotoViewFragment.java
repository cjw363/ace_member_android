package com.ace.member.main.image_detail;

import android.os.Bundle;
import android.text.TextUtils;

import com.ace.member.R;
import com.ace.member.base.BaseFragment;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.M;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import uk.co.senab.photoview.PhotoView;


public class PhotoViewFragment extends BaseFragment {
	public static final String TAG = "PhotoViewFragment";

	@BindView(R.id.pv)
	PhotoView mPv;
	private String mImage;

	public PhotoViewFragment() {
		Bundle bundle = new Bundle();
		bundle.putString(M.ImagePicker.FRAGMENT_NAME, TAG);
		this.setArguments(bundle);
	}

	public void setImage(String image) {
		this.mImage = image;
	}

	@Override
	public int getContentViewLayout() {
		return R.layout.view_photo_view;
	}

	@Override
	public void initView() {
		if (!TextUtils.isEmpty(mImage)) {
			if (mImage.toLowerCase().endsWith(M.ImagePicker.TYPE_GIF_LOW)) {
				Glide.with(this).load(mImage).asGif().diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(R.drawable.ic_image).into(mPv);
			} else {
				Glide.with(this).load(mImage).asBitmap().placeholder(R.drawable.ic_image).into(mPv);
			}
		}
	}

	@Override
	public void initData() {
		mImage = this.getArguments().getString(M.ImagePicker.IMAGE);
	}
}
