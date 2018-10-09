package com.ace.member.popup_window;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.utils.BitmapUtil;
import com.ace.member.utils.GlideUtil;
import com.ace.member.utils.Session;
import com.ace.member.view.RoundRectImageView;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.og.utils.Utils;
import com.zxing.ZxingUtils;

public class MyQRCodePopWindow extends PopupWindow {
	private static int BUSINESS_CODE_80_MY_QR_CODE = 80;

	private final Context mContext;
	private LinearLayout mLlMyQrCode;
	private FrameLayout mFlMyQrCode;
	private RoundRectImageView mIvMyQrCodeHead;
	private AppCompatImageView mIvMyQrCode;
	private TextView mTvMyQrCodeName;
	private TextView mTvMyQrCodeContent;

	private Bitmap mHeadBitmap;
	private Bitmap mQrBitmap;

	public MyQRCodePopWindow(Context context) {
		this.mContext = context;

		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		View view = LayoutInflater.from(mContext).inflate(R.layout.view_my_qr_code, null);
		setContentView(view);
		mFlMyQrCode = (FrameLayout) view.findViewById(R.id.fl_my_qr_code);
		mLlMyQrCode = (LinearLayout) view.findViewById(R.id.ll_my_qr_code);
		mIvMyQrCodeHead = (RoundRectImageView) view.findViewById(R.id.iv_my_qr_code_head);
		mTvMyQrCodeName = (TextView) view.findViewById(R.id.tv_my_qr_code_name);
		mTvMyQrCodeContent = (TextView) view.findViewById(R.id.tv_my_qr_code_content);
		mIvMyQrCode = (AppCompatImageView) view.findViewById(R.id.iv_my_qr_code);

		setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

		setBackgroundDrawable(new ColorDrawable());// 设置透明背景,背景如果不设置的话，回退按钮不起作用
		setFocusable(true);
		setOutsideTouchable(true);// 外部可点击
		setClippingEnabled(false);
		setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);

		setClickListener();
		setData();
	}

	private void setData() {
		final String qrCodeStr = BUSINESS_CODE_80_MY_QR_CODE + "|" + Session.user.getPhone();
		mTvMyQrCodeName.setText(Session.user.getName());
		mTvMyQrCodeContent.setText(Session.user.getPhone());
		GlideUtil.loadNormalPortrait(mContext, Session.user.getPortrait(), mIvMyQrCodeHead);
		GlideUtil.loadBitmap(mContext, Session.user.getPortrait(), R.drawable.head_portrait_1, new SimpleTarget<Bitmap>() {
			@Override
			public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
				if (!resource.isRecycled()) {
					mHeadBitmap = ZxingUtils.modifyLogo(BitmapUtil.getBitmap(mContext, R.drawable.bg_rectangle_white_5), resource);//带白边的logo
					mQrBitmap = ZxingUtils.createCode(qrCodeStr, mHeadBitmap, Utils.getDimenPx(R.dimen.width320));
					mIvMyQrCode.setImageBitmap(mQrBitmap);
				}
			}
		});
	}

	private void setClickListener() {
		mFlMyQrCode.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		mLlMyQrCode.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				return;
			}
		});
	}

	/**
	 * 展示
	 *
	 * @param activity
	 */
	public void showPopWindow(final Activity activity, int gravity) {
		setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
				lp.alpha = 1f;
				activity.getWindow().setAttributes(lp);

				freeBitmap();
			}
		});

		showAtLocation(activity.getWindow().getDecorView(), gravity, 0, 0);

		//虚化背景
		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
		lp.alpha = 0.7f;
		activity.getWindow().setAttributes(lp);
	}

	/**
	 * 释放bitmap内存
	 */
	private void freeBitmap() {
		if (mHeadBitmap != null && !mHeadBitmap.isRecycled()) {
			mHeadBitmap.recycle();
			mHeadBitmap = null;
		}
		if (mQrBitmap != null && !mQrBitmap.isRecycled()) {
			mQrBitmap.recycle();
			mQrBitmap = null;
		}
	}
}
