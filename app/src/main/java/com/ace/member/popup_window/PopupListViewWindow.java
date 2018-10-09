package com.ace.member.popup_window;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ace.member.R;
import com.og.widget.PopupWindows;

import java.util.ArrayList;
import java.util.List;

public class PopupListViewWindow extends PopupWindows implements OnDismissListener {
	private View mRootView;
	private ImageView mArrowUp;
	private ImageView mArrowDown;
	private LayoutInflater mInflater;
	private ViewGroup mTrack;
	private ScrollView mScroller;
	private OnPopupItemClickListener mItemClickListener;
	private OnDismissListener mDismissListener;

	private List<PopupItem> PopupItems = new ArrayList<>();

	private boolean mDidAction;

	private int mChildPos;
	private int mInsertPos;
	private int mAnimStyle;
	private int mOrientation;
	private int rootWidth = 0;
	private int mPosition=0;

	public static final int HORIZONTAL = 0;
	public static final int VERTICAL = 1;

	public static final int ANIM_GROW_FROM_LEFT = 1;
	public static final int ANIM_GROW_FROM_RIGHT = 2;
	public static final int ANIM_GROW_FROM_CENTER = 3;
	public static final int ANIM_REFLECT = 4;
	public static final int ANIM_AUTO = 5;

	/**
	 * Constructor for default vertical layout
	 *
	 * @param context Context
	 */
	public PopupListViewWindow(Context context) {
		this(context, VERTICAL);
	}

	/**
	 * Constructor allowing orientation override
	 *
	 * @param context     Context
	 * @param orientation Layout orientation, can be vartical or horizontal
	 */
	public PopupListViewWindow(Context context, int orientation) {
		super(context);

		mOrientation = orientation;

		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (mOrientation == HORIZONTAL) {
			setRootViewId(R.layout.view_popup_horizontal);
		} else {
			setRootViewId(R.layout.view_popup_vertical);
		}

		mAnimStyle = ANIM_AUTO;
		mChildPos = 0;
	}

	/**
	 * Get action item at an index
	 *
	 * @param index Index of item (position from callback)
	 * @return Action Item at the position
	 */
	public PopupItem getPopupItem(int index) {
		return PopupItems.get(index);
	}

	/**
	 * Set root view.
	 *
	 * @param id Layout resource id
	 */
	public void setRootViewId(int id) {
		mRootView = (ViewGroup) mInflater.inflate(id, null);
		mTrack = (ViewGroup) mRootView.findViewById(R.id.tracks);

		mArrowDown = (ImageView) mRootView.findViewById(R.id.arrow_down);
		mArrowUp = (ImageView) mRootView.findViewById(R.id.arrow_up);

		mScroller = (ScrollView) mRootView.findViewById(R.id.scroller);

		//This was previously defined on show() method, moved here to prevent force close that occured
		//when tapping fastly on a view to show quickaction dialog.
		//Thanx to zammbi (github.com/zammbi)
		mRootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

		setContentView(mRootView);
	}

	/**
	 * Set animation style
	 *
	 * @param mAnimStyle animation style, default is set to ANIM_AUTO
	 */
	public void setAnimStyle(int mAnimStyle) {
		this.mAnimStyle = mAnimStyle;
	}

	/**
	 * Set listener for action item clicked.
	 *
	 * @param listener Listener
	 */
	public void setOnPopupItemClickListener(OnPopupItemClickListener listener) {
		mItemClickListener = listener;
	}

	/**
	 * Add action item
	 *
	 * @param action {@link PopupItem}
	 */
	public void addPopupItem(PopupItem action) {
		PopupItems.add(action);

		String title = action.getTitle();
		Drawable icon = action.getIcon();

		View container;

		if (mOrientation == HORIZONTAL) {
			container = mInflater.inflate(R.layout.view_action_item_horizontal, null);
		} else {
			container = mInflater.inflate(R.layout.view_action_item_vertical, null);
		}

		ImageView img = (ImageView) container.findViewById(R.id.iv_icon);
		TextView text = (TextView) container.findViewById(R.id.tv_title);

		if (icon != null) {
			img.setImageDrawable(icon);
		} else {
			img.setVisibility(View.GONE);
		}

		if (title != null) {
			text.setText(title);
		} else {
			text.setVisibility(View.GONE);
		}

		final int pos = mChildPos;
		final int actionId = action.getActionId();

		container.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mItemClickListener != null) {
					mItemClickListener.onItemClick(PopupListViewWindow.this, pos, actionId,mPosition);
				}

				if (!getPopupItem(pos).isSticky()) {
					mDidAction = true;

					dismiss();
				}
			}
		});

		container.setFocusable(true);
		container.setClickable(true);

		if (mOrientation == HORIZONTAL && mChildPos != 0) {
			View separator = mInflater.inflate(R.layout.view_horiz_separator, null);

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);

			separator.setLayoutParams(params);
			separator.setPadding(5, 0, 5, 0);

			mTrack.addView(separator, mInsertPos);

			mInsertPos++;
		}

		mTrack.addView(container, mInsertPos);

		mChildPos++;
		mInsertPos++;
	}

	/**
	 * Show quickaction popup. Popup is automatically positioned, on top or bottom of anchor view.
	 */
	public void show(View anchor,int position) {
		preShow();

		int xPos, yPos, arrowPos;
		mPosition=position;
		mDidAction = false;

		int[] location = new int[2];

		anchor.getLocationOnScreen(location);

		Rect anchorRect = new Rect(location[0], location[1], location[0] + anchor.getWidth(), location[1] + anchor.getHeight());

		//mRootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		mRootView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

		int rootHeight = mRootView.getMeasuredHeight();

		if (rootWidth == 0) {
			rootWidth = mRootView.getMeasuredWidth();
		}
		DisplayMetrics outMetrics = new DisplayMetrics();
		mWindowManager.getDefaultDisplay().getMetrics(outMetrics);
		int screenWidth = outMetrics.widthPixels;
		int screenHeight = outMetrics.heightPixels;//mWindowManager.getDefaultDisplay().getHeight();

		//automatically get X coord of popup (top left)
//		if ((anchorRect.left + rootWidth) > screenWidth) {
//			Log.e("Window","11111111");
//			xPos = anchorRect.left - (rootWidth - anchor.getWidth());
//			xPos = (xPos < 0) ? 0 : xPos;
//
//			arrowPos = anchorRect.centerX() - xPos;
//
//		} else {
//
//			if (anchor.getWidth() > rootWidth) {
//				Log.e("Window","222222");
//				xPos = anchorRect.centerX() - (rootWidth / 2);
//			} else {
//				Log.e("Window","3333333");
//				xPos = anchorRect.left;
//			}
//
//			arrowPos = anchorRect.centerX() - xPos;
//		}
//		xPos = anchorRect.right;
		xPos = anchorRect.left - anchor.getWidth();
		xPos = (xPos < 0) ? 0 : xPos;
		arrowPos = anchorRect.centerX() - xPos;
		int dyTop = anchorRect.top;
		int dyBottom = screenHeight - anchorRect.bottom;

		boolean onTop = (dyTop > dyBottom) ? true : false;

		if (onTop) {
			if (rootHeight > dyTop) {
				yPos = 15;
				ViewGroup.LayoutParams l = mScroller.getLayoutParams();
				l.height = dyTop - anchor.getHeight();
			} else {
				yPos = anchorRect.top - rootHeight;
			}
		} else {
			yPos = anchorRect.bottom;

			if (rootHeight > dyBottom) {
				ViewGroup.LayoutParams l = mScroller.getLayoutParams();
				l.height = dyBottom;
			}
		}

		showArrow(((onTop) ? R.id.arrow_down : R.id.arrow_up), arrowPos);

		setAnimationStyle(screenWidth, anchorRect.centerX(), onTop);

//		Log.e("Window","xPos: "+xPos+"  yPos: "+yPos);
		mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos);
	}

	/**
	 * Set animation style
	 *
	 * @param screenWidth screen width
	 * @param requestedX  distance from left edge
	 * @param onTop       flag to indicate where the popup should be displayed. Set TRUE if displayed on top of anchor view
	 *                    and vice versa
	 */
	private void setAnimationStyle(int screenWidth, int requestedX, boolean onTop) {
		int arrowPos = requestedX - mArrowUp.getMeasuredWidth() / 2;

		switch (mAnimStyle) {
			case ANIM_GROW_FROM_LEFT:
				mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Left : R.style.Animations_PopDownMenu_Left);
				break;

			case ANIM_GROW_FROM_RIGHT:
				mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Right : R.style.Animations_PopDownMenu_Right);
				break;

			case ANIM_GROW_FROM_CENTER:
				mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Center : R.style.Animations_PopDownMenu_Center);
				break;

			case ANIM_REFLECT:
				mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Reflect : R.style.Animations_PopDownMenu_Reflect);
				break;

			case ANIM_AUTO:
				if (arrowPos <= screenWidth / 4) {
					mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Left : R.style.Animations_PopDownMenu_Left);
				} else if (arrowPos > screenWidth / 4 && arrowPos < 3 * (screenWidth / 4)) {
					mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Center : R.style.Animations_PopDownMenu_Center);
				} else {
					mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Right : R.style.Animations_PopDownMenu_Right);
				}

				break;
		}
	}

	/**
	 * Show arrow
	 *
	 * @param whichArrow arrow type resource id
	 * @param requestedX distance from left screen
	 */
	private void showArrow(int whichArrow, int requestedX) {
		final View showArrow = (whichArrow == R.id.arrow_up) ? mArrowUp : mArrowDown;
		final View hideArrow = (whichArrow == R.id.arrow_up) ? mArrowDown : mArrowUp;

		final int arrowWidth = mArrowUp.getMeasuredWidth();

		showArrow.setVisibility(View.VISIBLE);

		ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams) showArrow.getLayoutParams();

		param.leftMargin = requestedX - arrowWidth / 2;

		hideArrow.setVisibility(View.INVISIBLE);
	}

	/**
	 * Set listener for window dismissed. This listener will only be fired if the quicakction dialog is dismissed
	 * by clicking outside the dialog or clicking on sticky item.
	 */
	public void setOnDismissListener(PopupListViewWindow.OnDismissListener listener) {
		setOnDismissListener(this);
		mDismissListener = listener;
	}

	@Override
	public void onDismiss() {
		if (!mDidAction && mDismissListener != null) {
			mDismissListener.onDismiss();
		}
	}

	/**
	 * Listener for item click
	 */
	public interface OnPopupItemClickListener {
		public abstract void onItemClick(PopupListViewWindow source, int pos, int actionId,int position);
	}

	/**
	 * Listener for window dismiss
	 */
	public interface OnDismissListener {
		public abstract void onDismiss();
	}
}
