package com.og.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.og.R;


public class CustomDialog extends Dialog {

	public CustomDialog(Context context) {
		super(context);
	}

	public CustomDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private Context context;
		private String message, btnPositiveText, btnNegativeText, title;
		private View contentView;
		private OnClickListener btnPositiveClickListener, btnNegativeClickListener;
		private int layoutID = 0, textSize = Utils.getDimenPx(R.dimen.txtSize16), gravity = Gravity.CENTER, messageTextSize = Utils.getDimenPx(R.dimen.txtSize16);
		private TextView mMessage;
		private int messageColor = 0;
		private int icon = 0;

		public Builder(Context context) {
			this.context = context;
		}

		public Builder(Context context, int layoutID) {
			this.context = context;
			this.layoutID = layoutID;
		}

		/**
		 * Set the Dialog title
		 */
		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		/**
		 * Set the Dialog title from resource
		 */
		public Builder setTitle(int titleResourceID) {
			this.title = (String) context.getText(titleResourceID);
			return this;
		}

		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}

		public Builder setMessageTextSize(int size) {
			this.messageTextSize = size;
			return this;
		}

		/**
		 * Set the Dialog message from resource
		 */
		public Builder setMessage(int mid) {
			this.message = (String) context.getText(mid);
			return this;
		}

		/**
		 * Set the Dialog message color from resource
		 */
		public Builder setMessageColor(int colorResourceID) {
			this.messageColor = colorResourceID;
			return this;
		}

		/**
		 * Set the Dialog icon from resource
		 */
		public Builder setIcon(int iconResourceID) {
			this.icon = iconResourceID;
			return this;
		}

		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}

		public Builder setBtnTextSize(int size) {
			this.textSize = size;
			return this;
		}

		public Builder setGravity(int gravity) {
			this.gravity = gravity;
			return this;
		}

		/**
		 * Set the positive button resource and it's listener
		 */
		public Builder setPositiveButton(int btnText, OnClickListener listener) {
			this.btnPositiveText = (String) context.getText(btnText);
			this.btnPositiveClickListener = listener;
			return this;
		}

		public Builder setPositiveButton(String btnText, OnClickListener listener) {
			this.btnPositiveText = btnText;
			this.btnPositiveClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(int negativeButtonText, OnClickListener listener) {
			this.btnNegativeText = (String) context.getText(negativeButtonText);
			this.btnNegativeClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(String negativeButtonText, OnClickListener listener) {
			this.btnNegativeText = negativeButtonText;
			this.btnNegativeClickListener = listener;
			return this;
		}

		public CustomDialog create() {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final CustomDialog dialog = new CustomDialog(context, R.style.dialog_custom);
			int lid = layoutID > 0 ? layoutID : R.layout.dialog_normal_layout;
			try {
				View layout = inflater.inflate(lid, null);
				Button btnPositive = ((Button) layout.findViewById(R.id.btnPositive));
				Button btnNegative = ((Button) layout.findViewById(R.id.btnNegative));
				TextView tvVerticalLine = (TextView) layout.findViewById(R.id.tv_vertical_line);
				ScrollView content = ((ScrollView) layout.findViewById(R.id.content));
				AppCompatImageView ivIcon = ((AppCompatImageView) layout.findViewById(R.id.iv_icon));
				TextView tvTitle = (TextView) layout.findViewById(R.id.tv_title);

				dialog.addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

				if (!TextUtils.isEmpty(title)){
					tvTitle.setVisibility(View.VISIBLE);
					tvTitle.setText(title);
				}else {
					tvTitle.setVisibility(View.GONE);
				}

				if (btnPositiveText != null) {
					btnPositive.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
					btnPositive.setText(btnPositiveText);
					if (btnPositiveClickListener != null) {
						btnPositive.setOnClickListener(new View.OnClickListener() {
							public void onClick(View v) {
								btnPositiveClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
							}
						});
					}
				} else {
					btnPositive.setVisibility(View.GONE);
					btnNegative.setBackground(Utils.getDrawable(R.drawable.sel_corner_bottom));
					if (tvVerticalLine != null) tvVerticalLine.setVisibility(View.GONE);
				}

				if (btnNegativeText != null) {
					btnNegative.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
					btnNegative.setText(btnNegativeText);
					if (btnNegativeClickListener != null) {
						btnNegative.setOnClickListener(new View.OnClickListener() {
							public void onClick(View v) {
								btnNegativeClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
							}
						});
					}
				} else {
					if (btnNegative != null) {
						btnNegative.setVisibility(View.GONE);
					}
					btnPositive.setBackground(Utils.getDrawable(R.drawable.sel_corner_bottom));
					if (tvVerticalLine != null) tvVerticalLine.setVisibility(View.GONE);
				}
				// set the content message
				if (message != null) {
					mMessage = ((TextView) layout.findViewById(R.id.message));
					//										mMessage.setGravity(gravity);//这里为什么要改变原有的布局，再xml中设置不就行了吗？
					mMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, messageTextSize);
					mMessage.setText(message);
					//					mMessage.setTextSize(messageTextSize);
				} else if (contentView != null) {
					// if no message set
					// add the contentView to the dialog body
					content.removeAllViews();
					content.addView(contentView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				}

				if (messageColor != 0) {
					mMessage.setTextColor(Utils.getColor(messageColor));
				}

				if (icon != 0) {
					ivIcon.setImageResource(icon);
					ivIcon.setVisibility(View.VISIBLE);
					if (message != null) {
						mMessage.setPadding(0, 0, Utils.getDimenPx(R.dimen.padding50), 0);
					}
				}

				dialog.setContentView(layout);
				if (dialog.getWindow() != null) {
					dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return dialog;
		}
	}
}
