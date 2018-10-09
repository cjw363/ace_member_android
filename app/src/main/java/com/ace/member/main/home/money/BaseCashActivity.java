package com.ace.member.main.home.money;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseWebSocketActivity;
import com.og.utils.Utils;
import com.zxing.ZxingUtils;

import butterknife.BindView;

public class BaseCashActivity extends BaseWebSocketActivity implements View.OnClickListener {

	@BindView(R.id.fl_content)
	FrameLayout mFlContent;

	@BindView(R.id.ll_content)
	public LinearLayout mLlContent;

	@BindView(R.id.img_bar_code)
	ImageView mImgBarCode;
	@BindView(R.id.img_qr_code)
	ImageView mImgQRCode;
	@BindView(R.id.img_function)
	public ImageView mImgFunction;
	@BindView(R.id.tv_function)
	public TextView mTvFunction;

	@BindView(R.id.img_bar_code_full_screen)
	ImageView mImgBarCodeFullScreen;
	@BindView(R.id.img_qr_code_full_screen)
	ImageView mImgQRCodeFullScreen;
	@BindView(R.id.ll_bar_code_full_screen)
	LinearLayout mLlBarCodeFullScreen;
	@BindView(R.id.ll_qr_code_full_screen)
	LinearLayout mLlQRCodeFullScreen;
	@BindView(R.id.img_code)
	ImageView mImgCode;
	@BindView(R.id.tv_tap)
	public TextView mTvTap;

	public static String TRANSFER_11_MEMBER_RECEIVE_MONEY = "11";
	public static String TRANSFER_12_MEMBER_DEPOSIT_CASH = "12";
	public static String TRANSFER_13_MEMBER_WITHDRAW_CASH = "13";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		changeAppBrightness(255);
		mImgBarCode.setOnClickListener(this);
		mImgQRCode.setOnClickListener(this);
		mLlBarCodeFullScreen.setOnClickListener(this);
		mLlQRCodeFullScreen.setOnClickListener(this);
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_deposit_cash;
	}

	/**
	 * 改变App当前Window亮度
	 *
	 * @param brightness
	 */
	public void changeAppBrightness(int brightness) {
		Window window = this.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		if (brightness == -1) {
			lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
		} else {
			lp.screenBrightness = (brightness <= 0 ? 1 : brightness) / 255f;
		}
		window.setAttributes(lp);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.img_bar_code:
				mLlBarCodeFullScreen.setVisibility(View.VISIBLE);
				break;
			case R.id.img_qr_code:
				mLlQRCodeFullScreen.setVisibility(View.VISIBLE);
				break;
			case R.id.ll_bar_code_full_screen:
				mLlBarCodeFullScreen.setVisibility(View.GONE);
				break;
			case R.id.ll_qr_code_full_screen:
				mLlQRCodeFullScreen.setVisibility(View.GONE);
				break;
		}
	}

	public void initActivity() {
		super.initActivity();
	}

	public void backBroadcastReceiver(int code,String result) {}

	public void setCode(String code) {
		try {
			mBitmapQRCode = ZxingUtils.createQRImage(code, Utils.getDimenPx(R.dimen.width160), Utils.getDimenPx(R.dimen.height160));
			mImgQRCode.setImageBitmap(mBitmapQRCode);

			mBitmapBarCode = ZxingUtils.createBarcode(this, code, Utils.getDimenPx(R.dimen.width320), Utils.getDimenPx(R.dimen.width80), false);
			mImgBarCode.setImageBitmap(mBitmapBarCode);

			mBitmapQRCodeFullScreen = ZxingUtils.createQRImage(code + "", Utils.getDimenPx(R.dimen.width280), Utils.getDimenPx(R.dimen.width280));
			mImgQRCodeFullScreen.setImageBitmap(mBitmapQRCodeFullScreen);

			Matrix matrix = new Matrix();
			Bitmap bitmap1 = ZxingUtils.createBarcode(this, code, Utils.getDimenPx(R.dimen.height440), Utils.getDimenPx(R.dimen.width120), false);
			// 设置旋转角度
			matrix.setRotate(90);
			// 重新绘制Bitmap
			mBitmapBarCodeFullScreen = Bitmap.createBitmap(bitmap1, 0, 0, bitmap1.getWidth(), bitmap1.getHeight(), matrix, true);
			if (bitmap1 != null && !bitmap1.isRecycled()) {
				bitmap1.recycle();
				bitmap1 = null;
			}
			mImgBarCodeFullScreen.setImageBitmap(mBitmapBarCodeFullScreen);
			//			mBitmap = getBitmap(code, mBitmapBarCodeFullScreen.getHeight());
			//			mImgCode.setImageBitmap(mBitmap);

		} catch (OutOfMemoryError o) {
			o.printStackTrace();
		}
	}

	private Bitmap mBitmap;
	private Bitmap mBitmap2;
	private Bitmap mBitmapQRCode;
	private Bitmap mBitmapBarCode;
	private Bitmap mBitmapQRCodeFullScreen;
	private Bitmap mBitmapBarCodeFullScreen;

	public Bitmap getBitmap(String str, int width) {
		try {
			TextPaint tp1 = new TextPaint();
			tp1.setAntiAlias(true);
			tp1.setTextSize(Utils.getDimenPx(R.dimen.txtSize30));
			StaticLayout s1 = new StaticLayout(Html.fromHtml(str), tp1, width, Layout.Alignment.ALIGN_CENTER, 1, 0, false);
			int h1 = s1.getHeight();
			mBitmap2 = Bitmap.createBitmap(width, h1, Bitmap.Config.ARGB_8888);
			Canvas c1 = new Canvas(mBitmap2);
			c1.drawColor(Color.WHITE);
			c1.translate(0, 0);
			s1.draw(c1);
			Matrix matrix = new Matrix();
			matrix.setRotate(90);
			return Bitmap.createBitmap(mBitmap2, 0, 0, mBitmap2.getWidth(), mBitmap2.getHeight(), matrix, true);
		} catch (OutOfMemoryError o) {
			o.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (mLlQRCodeFullScreen.getVisibility() == View.VISIBLE) {
			mLlQRCodeFullScreen.setVisibility(View.GONE);
			return false;
		}
		if (mLlBarCodeFullScreen.getVisibility() == View.VISIBLE) {
			mLlBarCodeFullScreen.setVisibility(View.GONE);
			return false;
		}
		return super.onKeyDown(keyCode, event);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ZxingUtils.freeBitmap();
		if (mBitmap != null && !mBitmap.isRecycled()) {
			mBitmap.recycle();
			mBitmap = null;
		}
		if (mBitmap2 != null && !mBitmap2.isRecycled()) {
			mBitmap2.recycle();
			mBitmap2 = null;
		}
		if (mBitmapQRCode != null && !mBitmapQRCode.isRecycled()) {
			mBitmapQRCode.recycle();
			mBitmapQRCode = null;
		}
		if (mBitmapBarCode != null && !mBitmapBarCode.isRecycled()) {
			mBitmapBarCode.recycle();
			mBitmapBarCode = null;
		}
		if (mBitmapQRCodeFullScreen != null && !mBitmapQRCodeFullScreen.isRecycled()) {
			mBitmapQRCodeFullScreen.recycle();
			mBitmapQRCodeFullScreen = null;
		}
		if (mBitmapBarCodeFullScreen != null && !mBitmapBarCodeFullScreen.isRecycled()) {
			mBitmapBarCodeFullScreen.recycle();
			mBitmapBarCodeFullScreen = null;
		}
		System.gc();
	}
}
