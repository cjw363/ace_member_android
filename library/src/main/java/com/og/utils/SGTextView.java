package com.og.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

public class SGTextView extends TextView {
	private TextPaint mStrokePaint = new TextPaint();
	private TextPaint mGradientPaint = new TextPaint();

	public SGTextView(Context context) {
		this(context, null);
	}

	public SGTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setStyle(String strokeColor, String startColor, String endColor, float strokeWidthDp, int gradientHeightDp) {
		setStyle(Color.parseColor(strokeColor), DimensUtils.dip2px(getContext(), strokeWidthDp), new LinearGradient(0, 0, 0, DimensUtils.dip2px(getContext(), gradientHeightDp), new int[]{Color.parseColor(startColor), Color.parseColor(endColor)}, null, TileMode.CLAMP));
	}

	public void setStyle(int strokeColor, float strokeWidth, Shader shader) {

		mStrokePaint.setAntiAlias(true);
		// 设定是否使用图像抖动处理，会使绘制出来的图片颜色更加平滑和饱满，图像更加清晰
		mStrokePaint.setDither(true);
		// 如果该项设置为true，则图像在动画进行中会滤掉对Bitmap图像的优化操作，加快显示
		// 速度，本设置项依赖于dither和xfermode的设置
		mStrokePaint.setFilterBitmap(true);

		mStrokePaint.setStrokeWidth(strokeWidth);
		mStrokePaint.setColor(strokeColor);
		// 设置绘制时各图形的结合方式，如平滑效果等
		mStrokePaint.setStrokeJoin(TextPaint.Join.ROUND);
		// 当画笔样式为STROKE或FILL_OR_STROKE时，设置笔刷的图形样式，如圆形样式
		// Cap.ROUND,或方形样式Cap.SQUARE
		mStrokePaint.setStrokeCap(TextPaint.Cap.ROUND);
		mStrokePaint.setStyle(TextPaint.Style.STROKE);

		mGradientPaint.setAntiAlias(true);
		mGradientPaint.setDither(true);
		mGradientPaint.setFilterBitmap(true);
		mGradientPaint.setShader(shader);
		mGradientPaint.setStrokeJoin(TextPaint.Join.ROUND);
		mGradientPaint.setStrokeCap(TextPaint.Cap.ROUND);
		mGradientPaint.setStyle(TextPaint.Style.FILL_AND_STROKE);

		float txtSize = getTextSize();
		mStrokePaint.setTextSize(txtSize);
		mGradientPaint.setTextSize(txtSize);

	}

	public void setShadowLayer(float radius, float dx, float dy, String color) {
		mStrokePaint.setShadowLayer(radius, dx, dy, Color.parseColor(color));
	}

	@Override
	@SuppressLint("DrawAllocation")
	protected void onDraw(@NonNull Canvas canvas) {

//		String text = getText().toString();
//		int width = getMeasuredWidth();
//		if (width == 0) {
//			measure(0, 0);
//			width = (int) (getMeasuredWidth() + mStrokePaint.getStrokeWidth() * 2);
//			setWidth(width);
//		}
//
//		float y = getBaseline();
//		float x = (width - mStrokePaint.measureText(text)) / 2;
//
//		canvas.drawText(text, x, y, mStrokePaint);
//		canvas.drawText(text, x, y, mGradientPaint);

		StaticLayout strokeLayout = new StaticLayout(getText(), mStrokePaint, canvas.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1, 0, true);
		StaticLayout textLayout = new StaticLayout(getText(), mGradientPaint, canvas.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1, 0, true);

		strokeLayout.draw(canvas);
		textLayout.draw(canvas);
	}
}
