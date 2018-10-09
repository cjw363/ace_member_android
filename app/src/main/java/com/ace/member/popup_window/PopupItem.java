package com.ace.member.popup_window;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class PopupItem {
	private Drawable mIcon;
	private Bitmap mThumb;
	private String mTitle;
	private int mActionId = -1;
	private boolean mSelected;
	private boolean mSticky;

	/**
	 * Constructor
	 *
	 * @param actionId Action id for case statements
	 * @param title    Title
	 * @param icon     Icon to use
	 */
	public PopupItem(int actionId, String title, Drawable icon) {
		mTitle = title;
		mIcon = icon;
		mActionId = actionId;
	}

	/**
	 * Constructor
	 */
	public PopupItem() {
		this(-1, null, null);
	}

	/**
	 * Constructor
	 *
	 * @param actionId Action id of the item
	 * @param title    Text to show for the item
	 */
	public PopupItem(int actionId, String title) {
		this(actionId, title, null);
	}

	/**
	 * Constructor
	 *
	 * @param icon {@link Drawable} action mIcon
	 */
	public PopupItem(Drawable icon) {
		this(-1, null, icon);
	}

	/**
	 * Constructor
	 *
	 * @param actionId Action ID of item
	 * @param icon     {@link Drawable} action mIcon
	 */
	public PopupItem(int actionId, Drawable icon) {
		this(actionId, null, icon);
	}

	/**
	 * Set action mTitle
	 *
	 * @param title action title
	 */
	public void setTitle(String title) {
		mTitle = title;
	}

	/**
	 * Get action mTitle
	 *
	 * @return action mTitle
	 */
	public String getTitle() {
		return mTitle;
	}

	/**
	 * Set action mIcon
	 *
	 * @param icon {@link Drawable} action icon
	 */
	public void setIcon(Drawable icon) {
		mIcon = icon;
	}

	/**
	 * Get action mIcon
	 *
	 * @return {@link Drawable} action icon
	 */
	public Drawable getIcon() {
		return mIcon;
	}

	/**
	 * Set action id
	 *
	 * @param actionID Action id for this action
	 */
	public void setmActionId(int actionID) {
		mActionId = actionID;
	}

	/**
	 * @return Our action id
	 */
	public int getActionId() {
		return mActionId;
	}

	/**
	 * Set mSticky status of button
	 *
	 * @param sticky true for sticky, pop up sends event but does not disappear
	 */
	public void setSticky(boolean sticky) {
		mSticky = sticky;
	}

	/**
	 * @return true if button is sticky, menu stays visible after press
	 */
	public boolean isSticky() {
		return mSticky;
	}

	/**
	 * Set selected flag;
	 *
	 * @param selected Flag to indicate the item is mSelected
	 */
	public void setSelected(boolean selected) {
		mSelected = selected;
	}

	/**
	 * Check if item is selected
	 *
	 * @return true or false
	 */
	public boolean isSelected() {
		return mSelected;
	}

	/**
	 * Set mThumb
	 *
	 * @param thumb Thumb image
	 */
	public void setThumb(Bitmap thumb) {
		mThumb = thumb;
	}

	/**
	 * Get thumb image
	 *
	 * @return Thumb image
	 */
	public Bitmap getThumb() {
		return mThumb;
	}
}

