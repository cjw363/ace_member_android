package com.og.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.og.R;

public class SeekBarIndicated extends FrameLayout implements SeekBar.OnSeekBarChangeListener {

	private TextThumbSeekBar mSeekBar;          //滑动
	private int mSeekBarMarginLeft = 0;
	private int mSeekBarMarginTop = 0;
	private int mSeekBarMarginBottom = 0;
	private int mSeekBarMarginRight = 0;
	private int mSeekBarMin = 0;
	private SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener;

	public SeekBarIndicated(Context context) {
		super(context);
		if (!isInEditMode()) init(context);
	}

	public SeekBarIndicated(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (!isInEditMode()) init(context, attrs, 0);
	}

	public SeekBarIndicated(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		if (!isInEditMode()) init(context, attrs, defStyleAttr);
	}

	private void init(Context context) {
		init(context, null, 0);
	}

	private void init(Context context, AttributeSet attrs, int defStyle) {
		View view = LayoutInflater.from(context).inflate(R.layout.view_seekbar_indicated, this);
		mSeekBar = (TextThumbSeekBar) view.findViewById(R.id.seekbar);
		if (attrs != null) setAttributes(context, attrs, defStyle);
		mSeekBar.setOnSeekBarChangeListener(this);

		getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
			@Override
			public void onGlobalLayout() {
				getProgress();
				getViewTreeObserver().removeOnGlobalLayoutListener(this);
			}
		});
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		getProgress();
		if (mOnSeekBarChangeListener != null)
			mOnSeekBarChangeListener.onProgressChanged(seekBar, progress, fromUser);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		if (mOnSeekBarChangeListener != null) {
			mOnSeekBarChangeListener.onStartTrackingTouch(seekBar);
		}
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		if (mOnSeekBarChangeListener != null) {
			mOnSeekBarChangeListener.onStopTrackingTouch(seekBar);
		}
	}
	private void setAttributes(Context context, AttributeSet attrs, int defStyle) {
		// then obtain typed array
		TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.SeekBarIndicated, defStyle, 0);

		// above
		mSeekBarMarginLeft = arr.getDimensionPixelSize(R.styleable.SeekBarIndicated_seekbar_marginLeft, 0);
		mSeekBarMarginTop = arr.getDimensionPixelSize(R.styleable.SeekBarIndicated_seekbar_marginTop, 0);
		mSeekBarMarginRight = arr.getDimensionPixelSize(R.styleable.SeekBarIndicated_seekbar_marginRight, 0);
		mSeekBarMarginBottom = arr.getDimensionPixelSize(R.styleable.SeekBarIndicated_seekbar_marginBottom, 0);
		mSeekBarMin = arr.getInt(R.styleable.SeekBarIndicated_seekbar_min, 0);
		int seekBarMax = arr.getInt(R.styleable.SeekBarIndicated_seekbar_max, 100);
		int seekBarThumbId = arr.getResourceId(R.styleable.SeekBarIndicated_seekbar_thumb, 0);
		int seekBarProgressDrawableId = arr.getResourceId(R.styleable.SeekBarIndicated_seekbar_progressDrawable, 0);

		setMin(mSeekBarMin);
		setMax(seekBarMax);
		if (seekBarThumbId > 0) {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
				mSeekBar.setThumb(getResources().getDrawable(seekBarThumbId));
			else mSeekBar.setThumb(getResources().getDrawable(seekBarThumbId, null));
		}

		if (seekBarProgressDrawableId > 0) {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
				mSeekBar.setProgressDrawable(getResources().getDrawable(seekBarProgressDrawableId));
			else
				mSeekBar.setProgressDrawable(getResources().getDrawable(seekBarProgressDrawableId, null));
		}
		mSeekBar.setPadding(mSeekBar.getPaddingLeft() + mSeekBarMarginLeft, mSeekBar.getPaddingTop() + mSeekBarMarginTop, mSeekBar.getPaddingRight() + mSeekBarMarginRight, mSeekBar.getPaddingBottom() + mSeekBarMarginBottom);
		arr.recycle();

	}

	public void setMax(int max) {
		mSeekBar.setMax(max - mSeekBarMin);
	}

	public void setMin(int min) {
		mSeekBarMin = min;
		mSeekBar.setMix(min);
	}

	public void setValue(int value) {
		mSeekBar.setProgress(value);
		getProgress();
	}

	public void setOnSeekBarChangeListener(SeekBar.OnSeekBarChangeListener onSeekBarChangeListener) {
		mOnSeekBarChangeListener = onSeekBarChangeListener;
	}

	public int getProgress() {
		int unsignedMin = mSeekBarMin < 0 ? mSeekBarMin * -1 : mSeekBarMin;
		return mSeekBar.getProgress() + unsignedMin;
	}
}
