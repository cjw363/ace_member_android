package com.og.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.og.R;


public class CustomDialog2 extends Dialog {

	public CustomDialog2(Context context) {
		super(context);
	}

	public CustomDialog2(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private Context context;
		private String message, btnPositiveText, btnNegativeText;
		private View contentView;
		private OnClickListener btnPositiveClickListener, btnNegativeClickListener;
		private int layoutID = 0, textSize = 16, gravity = Gravity.START | Gravity.CENTER;
		private TextView mMessage;

		public Builder(Context context) {
			this.context = context;
		}

		public Builder(Context context, int layoutID) {
			this.context = context;
			this.layoutID = layoutID;
		}

		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}

		/**
		 * Set the Dialog message from resource
		 */
		public Builder setMessage(int mid) {
			this.message = (String) context.getText(mid);
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
			int lid = layoutID > 0 ? layoutID : R.layout.dialog_simple2_layout;
			try {
				View layout = inflater.inflate(lid, null);
				Button btnPositive = ((Button) layout.findViewById(R.id.btnPositive));
				Button btnNegative = ((Button) layout.findViewById(R.id.btnNegative));
				ScrollView content = ((ScrollView) layout.findViewById(R.id.content));

				dialog.addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

				if (btnPositiveText != null) {
					btnPositive.setText(btnPositiveText);
					btnPositive.setTextSize(textSize);
					if (btnPositiveClickListener != null) {
						btnPositive.setOnClickListener(new View.OnClickListener() {
							public void onClick(View v) {
								btnPositiveClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
							}
						});
					}
				} else {
					btnPositive.setVisibility(View.GONE);
				}

				if (btnNegativeText != null) {
					btnNegative.setText(btnNegativeText);
					btnNegative.setTextSize(textSize);
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
				}
				// set the content message
				if (message != null) {
					mMessage = ((TextView) layout.findViewById(R.id.message));
					//					mMessage.setGravity(gravity);//这里为什么要改变原有的布局，再xml中设置不就行了吗？
					mMessage.setText(message);
				} else if (contentView != null) {
					// if no message set
					// add the contentView to the dialog body
					content.removeAllViews();
					content.addView(contentView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				}
				dialog.setContentView(layout);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return dialog;
		}
	}
}
