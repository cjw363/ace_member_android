package com.zxing.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.google.zxing.ResultPoint;
import com.og.R;
import com.og.utils.Utils;
import com.zxing.camera.CameraManager;

import java.util.Collection;
import java.util.HashSet;

public final class ViewfinderView extends View {
	/**
	 * 刷新界面的时间
	 */
	private static final long ANIMATION_DELAY = 10L;
	private static final int OPAQUE = 0xFF;

	/**
	 * 画笔的颜色
	 */
	private int PAINT_COLOR = Utils.getColor(R.color.colorPrimary);

	/**
	 * 四个绿色边角对应的长度
	 */
	private static final int ANGLE_LENGHT = Utils.dip2px(16);
	/**
	 * 四个绿色边角对应的宽度
	 */
	private static final int CORNER_WIDTH = Utils.dip2px(4);
	/**
	 * 扫描框中的中间线的宽度
	 */
	private static final int MIDDLE_LINE_WIDTH = Utils.dip2px(2);

	/**
	 * 扫描框中的中间线的与扫描框左右的间隙
	 */
	private static final int MIDDLE_LINE_PADDING = 0;
	/**
	 * 中间那条线每次刷新移动的距离
	 */
	private static final int SPEEN_DISTANCE = Utils.dip2px(1.5);

	/**
	 * 字体大小
	 */
	private static final int TEXT_SIZE = Utils.dip2px(14);
	/**
	 * 字体距离扫描框下面的距离
	 */
	private static final int TEXT_PADDING_TOP = Utils.dip2px(30);

	/**
	 * 画笔对象的引用
	 */
	private Paint paint;

	/**
	 * 中间滑动线的最顶端位置
	 */
	private int slideTop;

	/**
	 * 中间滑动线的最底端位置
	 */
	private int slideBottom;

	private Bitmap resultBitmap;
	private final int maskColor;
	private final int resultColor;

	private final int resultPointColor;
	private Collection<ResultPoint> possibleResultPoints;
	private Collection<ResultPoint> lastPossibleResultPoints;

	boolean isFirst;

	public ViewfinderView(Context context, AttributeSet attrs) {
		super(context, attrs);

		paint = new Paint();
		Resources resources = getResources();
		maskColor = ContextCompat.getColor(getContext(), R.color.viewfinder_mask);
		resultColor = ContextCompat.getColor(getContext(), R.color.result_view);

		resultPointColor = ContextCompat.getColor(getContext(), R.color.possible_result_points);
		possibleResultPoints = new HashSet<ResultPoint>(5);
	}

	@Override
	public void onDraw(Canvas canvas) {
		//中间的扫描框，你要修改扫描框的大小，去CameraManager里面修改
		Rect frame = CameraManager.get().getFramingRect();
		if (frame == null) {
			return;
		}

		//初始化中间线滑动的最上边和最下边
		if (!isFirst) {
			isFirst = true;
			slideTop = frame.top;
			slideBottom = frame.bottom;
		}

		//获取屏幕的宽和高
		int screenWidth = canvas.getWidth();
		int sceenHeight = canvas.getHeight();

		paint.setColor(resultBitmap != null ? resultColor : maskColor);

		//画出扫描框外面的阴影部分，共四个部分，扫描框的上面到屏幕上面，扫描框的下面到屏幕下面
		//扫描框的左边面到屏幕左边，扫描框的右边到屏幕右边
		canvas.drawRect(0, 0, screenWidth, frame.top, paint);
		canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
		canvas.drawRect(frame.right + 1, frame.top, screenWidth, frame.bottom + 1, paint);
		canvas.drawRect(0, frame.bottom + 1, screenWidth, sceenHeight, paint);

		if (resultBitmap != null) {
			// Draw the opaque result bitmap over the scanning rectangle
			paint.setAlpha(OPAQUE);
			canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
		} else {

			//画扫描框边上的角，总共8个部分
			paint.setColor(PAINT_COLOR);
			canvas.drawRect(frame.left, frame.top, frame.left + ANGLE_LENGHT, frame.top + CORNER_WIDTH, paint);
			canvas.drawRect(frame.left, frame.top, frame.left + CORNER_WIDTH, frame.top + ANGLE_LENGHT, paint);
			canvas.drawRect(frame.right - ANGLE_LENGHT, frame.top, frame.right, frame.top + CORNER_WIDTH, paint);
			canvas.drawRect(frame.right - CORNER_WIDTH, frame.top, frame.right, frame.top + ANGLE_LENGHT, paint);
			canvas.drawRect(frame.left, frame.bottom - CORNER_WIDTH, frame.left + ANGLE_LENGHT, frame.bottom, paint);
			canvas.drawRect(frame.left, frame.bottom - ANGLE_LENGHT, frame.left + CORNER_WIDTH, frame.bottom, paint);
			canvas.drawRect(frame.right - ANGLE_LENGHT, frame.bottom - CORNER_WIDTH, frame.right, frame.bottom, paint);
			canvas.drawRect(frame.right - CORNER_WIDTH, frame.bottom - ANGLE_LENGHT, frame.right, frame.bottom, paint);

			canvas.drawLine(frame.left,frame.top,frame.right,frame.top,paint);
			canvas.drawLine(frame.left,frame.top,frame.left,frame.bottom,paint);
			canvas.drawLine(frame.right,frame.bottom,frame.right,frame.top,paint);
			canvas.drawLine(frame.right,frame.bottom,frame.left,frame.bottom,paint);

			//绘制中间的线,每次刷新界面，中间的线往下移动SPEEN_DISTANCE
			slideTop += SPEEN_DISTANCE;
			if (slideTop >= frame.bottom) {
				slideTop = frame.top;
			}
			paint.setColor(resultPointColor);
			canvas.drawRect(frame.left + MIDDLE_LINE_PADDING, slideTop - MIDDLE_LINE_WIDTH / 2, frame.right - MIDDLE_LINE_PADDING, slideTop + MIDDLE_LINE_WIDTH / 2, paint);

			//画扫描框下面的字
			paint.setColor(Color.WHITE);
			paint.setTextSize(TEXT_SIZE);
			paint.setAlpha(0x40);
			paint.setTypeface(Typeface.create("System", Typeface.BOLD));
			String text = getResources().getString(R.string.capture_tips);
			float textWidth = paint.measureText(text);//文字宽度
			float xDistance = (screenWidth - textWidth) / 2;
			if (xDistance < 0) xDistance = 0;
			canvas.drawText(text, xDistance, (float) (frame.bottom + (float) TEXT_PADDING_TOP), paint);

			Collection<ResultPoint> currentPossible = possibleResultPoints;
			Collection<ResultPoint> currentLast = lastPossibleResultPoints;
			if (currentPossible.isEmpty()) {
				lastPossibleResultPoints = null;
			} else {
				possibleResultPoints = new HashSet<ResultPoint>(5);
				lastPossibleResultPoints = currentPossible;
				paint.setAlpha(OPAQUE);
				paint.setColor(resultPointColor);
				for (ResultPoint point : currentPossible) {
					canvas.drawCircle(frame.left + point.getX(), frame.top + point.getY(), 6.0f, paint);
				}
			}
			if (currentLast != null) {
				paint.setAlpha(OPAQUE / 2);
				paint.setColor(resultPointColor);
				for (ResultPoint point : currentLast) {
					canvas.drawCircle(frame.left + point.getX(), frame.top + point.getY(), 3.0f, paint);
				}
			}

			//只刷新扫描框的内容，其他地方不刷新
			postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top, frame.right, frame.bottom);
		}
	}

	public void drawViewfinder() {
		resultBitmap = null;
		invalidate();
	}

	/**
	 * Draw a bitmap with the result points highlighted instead of the live
	 * scanning display.
	 *
	 * @param barcode An image of the decoded barcode.
	 */
	public void drawResultBitmap(Bitmap barcode) {
		resultBitmap = barcode;
		invalidate();
	}

	public void addPossibleResultPoint(ResultPoint point) {
		possibleResultPoints.add(point);
	}

}