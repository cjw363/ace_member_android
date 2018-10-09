package com.ace.member.main.home.money.code;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.FrameLayout;

import com.ace.member.R;
import com.ace.member.base.BaseFragment;
import com.ace.member.utils.AppGlobal;
import com.og.utils.FileUtils;
import com.og.utils.Utils;
import com.zxing.ZxingUtils;


public class CodeFragment extends BaseFragment implements View.OnClickListener {
	public static final String TAG = "CodeFragment";
	private String mCode;
	private int mType;
	private AppCompatImageView mIvCode;

	public CodeFragment() {
		Bundle bundle = new Bundle();
		bundle.putString("TAG", TAG);
		setArguments(bundle);
	}

	public static CodeFragment newInstance() {
		return new CodeFragment();
	}

	@Override
	protected int getContentViewLayout() {
		return R.layout.fragment_code;
	}

	@Override
	protected void initView() {
		try {
			FrameLayout flContent = (FrameLayout) getView().findViewById(R.id.fl_content);
			mIvCode = (AppCompatImageView) getView().findViewById(R.id.iv_code);
			flContent.setOnClickListener(this);
			mIvCode.setOnClickListener(this);
			initCodeView();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	private void initCodeView() {
		try {
			Bitmap codeBitmap = null;
			if (mType == AppGlobal.CODE_TYPE_1_BAR_CODE) {
				Matrix matrix = new Matrix();
				matrix.setRotate(90);
				codeBitmap = ZxingUtils.createBarcode(getContext(), mCode, Utils.getDimenPx(R.dimen.height440), Utils.getDimenPx(R.dimen.width120), false);
				codeBitmap = Bitmap.createBitmap(codeBitmap, 0, 0, codeBitmap.getWidth(), codeBitmap.getHeight(), matrix, true);
			} else if (mType == AppGlobal.CODE_TYPE_2_QR_CODE) {
				codeBitmap = ZxingUtils.createQRImage(mCode, Utils.getDimenPx(R.dimen.width280), Utils.getDimenPx(R.dimen.width280));
			}
			mIvCode.setImageBitmap(codeBitmap);
		} catch (OutOfMemoryError o) {
			o.printStackTrace();
		}
	}

	@Override
	protected void initData() {
		mCode = getArguments().getString("code");
		mType = getArguments().getInt("type");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.fl_content:
			case R.id.iv_code:
				getFragmentManager().popBackStack();
				break;
		}
	}
}
