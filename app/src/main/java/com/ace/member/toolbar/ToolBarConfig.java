package com.ace.member.toolbar;


import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ace.member.R;
import com.og.utils.FileUtils;

public class ToolBarConfig {
	private Activity mActivity;
	private View mView;
	private int mIvBackRes;
	private int mTvTitleRes;
	private String mTitle;
	private int mIvMenuRes;
	private int mIvMenuLeftRes;
	private int mBackgroundRes;
	private int mBackgroundDrawableRes;
	private int mTvMenuRes;
	private int mIvBackWidth;
	private int mIvBackHeight;
	private MenuType mMenuType;

	private boolean mEnableBack;
	private boolean mEnableMenu;

	private View.OnClickListener mBackListener;
	private View.OnClickListener mMenuListener;

	private SparseArray<View> mViewSparseArray;

	public static Builder builder(Activity activity, View view){
		return new Builder(activity, view);
	}

	public ToolBarConfig(Builder builder) {
		mActivity = builder.mActivity;
		mView = builder.mView;
		mIvBackRes = builder.mIvBackRes;
		mTvTitleRes = builder.mTvTitleRes;
		mTitle=builder.mTitle;
		mIvMenuRes = builder.mIvMenuRes;
		mIvMenuLeftRes = builder.mIvMenuLeftRes;
		mTvMenuRes = builder.mTvMenuRes;
		mMenuType = builder.mMenuType;
		mIvBackWidth=builder.mIvBackWidth;
		mIvBackHeight=builder.mIvBackHeight;
		mBackgroundRes = builder.mBackgroundRes;
		mBackgroundDrawableRes = builder.mBackgroundDrawableRes;

		mEnableBack = builder.mEnableBack;
		mEnableMenu = builder.mEnableMenu;

		mBackListener = builder.mBackListener;
		mMenuListener = builder.mMenuListener;

		mViewSparseArray = new SparseArray<>();

		setIvBack();
		setTitle();
		setLlMenu();
		setBackground();
	}

	private void setIvBack() {
		FrameLayout flBack = (FrameLayout) mView.findViewById(R.id.fl_back);
		ImageView ivBack = (ImageView) mView.findViewById(R.id.iv_back);
		if (mEnableBack) {
			flBack.setVisibility(View.VISIBLE);
			if(mIvBackWidth>0 && mIvBackHeight>0){
				ivBack.getLayoutParams().width=mIvBackWidth;
				ivBack.getLayoutParams().height=mIvBackHeight;
			}
			if (mIvBackRes != 0) ivBack.setImageResource(mIvBackRes);
			if (mBackListener == null) mBackListener = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mActivity != null) mActivity.onBackPressed();
				}
			};
			flBack.setOnClickListener(mBackListener);
		} else {
			flBack.setVisibility(View.GONE);
		}
	}

	private void setTitle() {
		if (mTvTitleRes != 0) ((TextView) mView.findViewById(R.id.tv_title)).setText(mTvTitleRes);
		if (!TextUtils.isEmpty(mTitle)) ((TextView) mView.findViewById(R.id.tv_title)).setText(mTitle);
	}

	private void setBackground() {
		if (mBackgroundRes != 0)mView.findViewById(R.id.rl_toolbar).setBackgroundColor(ContextCompat.getColor(mActivity.getBaseContext(), mBackgroundRes));
		if (mBackgroundDrawableRes != 0)mView.findViewById(R.id.rl_toolbar).setBackgroundResource(mBackgroundDrawableRes);
	}

	public void setTitle(String title) {
		((TextView) getView(R.id.tv_title)).setText(title);
	}

	public void setMenuText(String text) {
		((TextView) getView(R.id.tv_menu)).setText(text);
	}

	public TextView getMenuTv() {
		return ((TextView) getView(R.id.tv_menu));
	}

	public View getView(int viewId) {
		View view = null;
		try {
			view = mViewSparseArray.get(viewId);
			if (view == null) {
				view = mView.findViewById(viewId);
				mViewSparseArray.put(viewId, view);
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return view;
	}

	private void setLlMenu() {
		LinearLayout llMenu = (LinearLayout) mView.findViewById(R.id.ll_toolbar_menu);
		if (mEnableMenu) {
			ImageView ivMenu = (ImageView) mView.findViewById(R.id.iv_menu);
			ImageView ivMenuLeft = (ImageView) mView.findViewById(R.id.iv_menu_left);
			TextView tvMenu = (TextView) mView.findViewById(R.id.tv_menu);

			if (mIvMenuRes != 0) ivMenu.setImageResource(mIvMenuRes);
			if (mIvMenuLeftRes != 0) {
				ivMenuLeft.setImageResource(mIvMenuLeftRes);
				ivMenuLeft.setVisibility(View.VISIBLE);
			}
			if (mTvMenuRes != 0) tvMenu.setText(mTvMenuRes);

			llMenu.setVisibility(View.VISIBLE);
			if (mMenuListener != null) llMenu.setOnClickListener(mMenuListener);
			if (mMenuType == MenuType.MENU_IMAGE) {
				ivMenu.setVisibility(View.VISIBLE);
				tvMenu.setVisibility(View.GONE);
			} else if (mMenuType == MenuType.MENU_TEXT) {
				ivMenu.setVisibility(View.GONE);
				ivMenuLeft.setVisibility(View.GONE);
				tvMenu.setVisibility(View.VISIBLE);
			} else if (mMenuType == MenuType.MENU_BOTH) {
				ivMenu.setVisibility(View.VISIBLE);
				tvMenu.setVisibility(View.VISIBLE);
			}
		} else {
			llMenu.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置是否启用红点
	 *
	 * @param enable
	 */
	public void enableDot(boolean enable) {
		if (mEnableMenu && mMenuType != MenuType.MENU_TEXT)
			mView.findViewById(R.id.v_dot).setVisibility(enable ? View.VISIBLE : View.INVISIBLE);
	}

	/**
	 * 设置是否启用红点
	 */
	public void enableLeftDot(boolean enable) {
		if (mEnableMenu && mMenuType != MenuType.MENU_TEXT)
			mView.findViewById(R.id.v_dot_left).setVisibility(enable ? View.VISIBLE : View.INVISIBLE);
	}

	public static class Builder {
		Activity mActivity;
		View mView;
		int mIvBackRes;
		int mTvTitleRes;
		String mTitle;
		int mBackgroundRes;
		int mBackgroundDrawableRes;
		int mIvMenuRes;
		int mIvMenuLeftRes;
		int mTvMenuRes;
		int mIvBackWidth;
		int mIvBackHeight;
		MenuType mMenuType;

		boolean mEnableBack;
		boolean mEnableMenu;

		View.OnClickListener mBackListener;
		View.OnClickListener mMenuListener;

		/**
		 * 配置Toolbar
		 *
		 * @param activity toolbar所在的Activity，当需要启用返回按钮功能，又不想重写返回按钮点击事件时，必须传入，除非手动设置点击事件。
		 * @param view     toolbar所在的view层，当传入的activity不为null时，可传入null。
		 */
		public Builder(Activity activity, View view) {
			mActivity = activity;
			mView = view == null ? mActivity.getWindow().getDecorView() : view;
			mMenuType = MenuType.MENU_IMAGE;
			mEnableBack = true;
		}

		/**
		 * 设置返回按钮图标资源
		 *
		 * @param ivBackRes 返回按钮图标资源
		 * @return Builder
		 */
		public Builder setIvBackRes(int ivBackRes) {
			mIvBackRes = ivBackRes;
			return this;
		}

		public Builder setIvBackSize(int width,int height){
			mIvBackWidth=width;
			mIvBackHeight=height;
			return this;
		}

		/**
		 * 设置标题资源
		 *
		 * @param tvTitleRes 标题资源
		 * @return Builder
		 */
		public Builder setTvTitleRes(int tvTitleRes) {
			mTvTitleRes = tvTitleRes;
			return this;
		}

		public Builder setTvTitle(String title) {
			mTitle = title;
			return this;
		}

		/**
		 * 设置标题资源
		 *
		 * @param backgroundRes 背景资源
		 * @return Builder
		 */
		public Builder setBackgroundRes(int backgroundRes) {
			mBackgroundRes = backgroundRes;
			return this;
		}

		/**
		 * 设置标题Drawable资源
		 */
		public Builder setBackgroundDrawableRes(int backgroundRes) {
			mBackgroundDrawableRes = backgroundRes;
			return this;
		}

		/**
		 * 设置菜单图标资源
		 *
		 * @param ivMenuRes 菜单图标资源
		 * @return Builder
		 */
		public Builder setIvMenuRes(int ivMenuRes) {
			mIvMenuRes = ivMenuRes;
			return this;
		}
		/**
		 * 设置菜单图标资源
		 *
		 * @param ivMenuRes 菜单图标资源
		 * @return Builder
		 */
		public Builder setIvMenuRes(int ivMenuLeftRes,int ivMenuRes) {
			mIvMenuRes = ivMenuRes;
			mIvMenuLeftRes = ivMenuLeftRes;
			return this;
		}

		/**
		 * 设置菜单标题资源
		 *
		 * @param tvMenuRes 菜单标题资源
		 * @return Builder
		 */
		public Builder setTvMenuRes(int tvMenuRes) {
			mTvMenuRes = tvMenuRes;
			return this;
		}

		/**
		 * 设置菜单样式类型
		 *
		 * @param menuType 样式类型{@link MenuType}
		 * @return Builder
		 */
		public Builder setMenuType(MenuType menuType) {
			mMenuType = menuType;
			return this;
		}

		/**
		 * 设置启用返回按钮
		 *
		 * @param enableBack 是否启用返回按钮，默认true
		 * @return Builder
		 */
		public Builder setEnableBack(boolean enableBack) {
			mEnableBack = enableBack;
			return this;
		}

		/**
		 * 设置启用菜单
		 *
		 * @param enableMenu 是否启用菜单，默认false
		 * @return Builder
		 */
		public Builder setEnableMenu(boolean enableMenu) {
			mEnableMenu = enableMenu;
			return this;
		}

		/**
		 * 设置返回按钮点击监听器
		 *
		 * @param backListener 返回按钮点击监听器
		 * @return Builder
		 */
		public Builder setBackListener(View.OnClickListener backListener) {
			mBackListener = backListener;
			return this;
		}

		/**
		 * 设置菜单点击监听器
		 *
		 * @param menuListener 菜单点击监听器
		 * @return Builder
		 */
		public Builder setMenuListener(View.OnClickListener menuListener) {
			mMenuListener = menuListener;
			return this;
		}

		/**
		 * 构建
		 *
		 * @return ToolBarConfig
		 */
		public ToolBarConfig build() {
			return new ToolBarConfig(this);
		}
	}

	/**
	 * 菜单展示样式类型
	 */
	public enum MenuType {
		/**
		 * 图标
		 */
		MENU_IMAGE, /**
		 * 文字
		 */
		MENU_TEXT, /**
		 * 图标和文字
		 */
		MENU_BOTH
	}
}
