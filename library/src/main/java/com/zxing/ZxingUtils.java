package com.zxing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.media.ThumbnailUtils;
import android.view.Gravity;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

import static android.graphics.Color.BLACK;

/**
 * 生成条形码和二维码的工具
 */
public class ZxingUtils {

	static Bitmap mBitmap;

	/**
	 * 生成二维码 要转换的地址或字符串,可以是中文
	 *
	 * @param url    url
	 * @param width  width
	 * @param height height
	 * @return Bitmap
	 */
	public static Bitmap createQRImage(String url, final int width, final int height) {
		try {
			// 判断URL合法性
			if (url == null || "".equals(url) || url.length() < 1) {
				return null;
			}
			Hashtable<EncodeHintType, String> hints = new Hashtable<>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			// 图像数据转换，使用了矩阵转换
			BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, width, height, hints);
			int[] pixels = new int[width * height];
			// 下面这里按照二维码的算法，逐个生成二维码的图片，
			// 两个for循环是图片横列扫描的结果
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (bitMatrix.get(x, y)) {
						pixels[y * width + x] = 0xff000000;
					} else {
						pixels[y * width + x] = 0xffffffff;
					}
				}
			}
			bitMatrix.clear();
			// 生成二维码图片的格式，使用ARGB_8888
			mBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
			mBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
			return mBitmap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 生成条形码
	 *
	 * @param context       context
	 * @param contents      需要生成的内容
	 * @param desiredWidth  生成条形码的宽度
	 * @param desiredHeight 生成条形码的高度
	 * @param displayCode   是否在条形码下方显示内容
	 * @return Bitmap
	 */
	public static Bitmap createBarcode(Context context, String contents, int desiredWidth, int desiredHeight, boolean displayCode) {
		try {
			//图片两端所保留的空白的宽度
			int marginW = 20;
			// 条形码的编码类型
			BarcodeFormat barcodeFormat = BarcodeFormat.CODE_128;

			if (displayCode) {
				Bitmap barcodeBitmap = encodeAsBitmap(contents, barcodeFormat, desiredWidth, desiredHeight);
				Bitmap codeBitmap = creatCodeBitmap(contents, desiredWidth + 2 * marginW, desiredHeight, context);
				mBitmap = mixtureBitmap(barcodeBitmap, codeBitmap, new PointF(0, desiredHeight));
				if (barcodeBitmap != null && !barcodeBitmap.isRecycled()) {
					barcodeBitmap.recycle();
					barcodeBitmap = null;
				}
				if (codeBitmap != null && !codeBitmap.isRecycled()) {
					codeBitmap.recycle();
					codeBitmap = null;
				}
			} else {
				mBitmap = encodeAsBitmap(contents, barcodeFormat, desiredWidth, desiredHeight);
			}

			return mBitmap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 生成条形码的Bitmap
	 *
	 * @param contents      需要生成的内容
	 * @param format        编码格式
	 * @param desiredWidth  宽度
	 * @param desiredHeight 高度
	 * @return Bitmap
	 */
	private static Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int desiredWidth, int desiredHeight) {
		try {

			MultiFormatWriter writer = new MultiFormatWriter();
			BitMatrix result = null;
			try {
				result = writer.encode(contents, format, desiredWidth, desiredHeight, null);
			} catch (WriterException e) {
				e.printStackTrace();
			}
			if (result == null) return null;
			int width = result.getWidth();
			int height = result.getHeight();
			int[] pixels = new int[width * height];
			// All are 0, or black, by default
			for (int y = 0; y < height; y++) {
				int offset = y * width;
				for (int x = 0; x < width; x++) {
					pixels[offset + x] = result.get(x, y) ? 0xFF000000 : 0xFFFFFFFF;
				}
			}
			result.clear();
			mBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
			mBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
			return mBitmap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 生成显示编码的Bitmap
	 *
	 * @param contents contents
	 * @param width    width
	 * @param height   height
	 * @param context  context
	 * @return Bitmap
	 */
	private static Bitmap creatCodeBitmap(String contents, int width, int height, Context context) {
		try {
			TextView tv = new TextView(context);
			LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			tv.setLayoutParams(layoutParams);
			tv.setText(contents);
			tv.setHeight(height);
			tv.setGravity(Gravity.CENTER_HORIZONTAL);
			tv.setWidth(width);
			tv.setDrawingCacheEnabled(true);
			tv.setTextColor(BLACK);
			tv.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			tv.layout(0, 0, tv.getMeasuredWidth(), tv.getMeasuredHeight());

			tv.buildDrawingCache();
			return tv.getDrawingCache();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将两个Bitmap合并成一个
	 *
	 * @param first     first
	 * @param second    second
	 * @param fromPoint 第二个Bitmap开始绘制的起始位置（相对于第一个Bitmap）
	 * @return Bitmap
	 */
	private static Bitmap mixtureBitmap(Bitmap first, Bitmap second, PointF fromPoint) {
		try {
			if (first == null || second == null || fromPoint == null) {
				return null;
			}
			int marginW = 20;
			mBitmap = Bitmap.createBitmap(first.getWidth() + second.getWidth() + marginW, first.getHeight() + second.getHeight(), Config.ARGB_4444);
			Canvas cv = new Canvas(mBitmap);
			cv.drawBitmap(first, marginW, 0, null);
			cv.drawBitmap(second, fromPoint.x, fromPoint.y, null);
			cv.save(Canvas.ALL_SAVE_FLAG);
			cv.restore();

			return mBitmap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 生成带LOGO的二维码
	 */
	public static Bitmap createCode(String content, Bitmap logoBitmap, int codeWidth) {
		int logoWidth = logoBitmap.getWidth();
		int logoHeight = logoBitmap.getHeight();
		int logoWidthMax = codeWidth / 5;//LOGO宽度值,最大不能大于二维码20%宽度值,大于可能会导致二维码信息失效
		int logoWidthMin = codeWidth / 10;//LOGO宽度值,最小不能小于二维码10%宽度值,小于影响Logo与二维码的整体搭配
		int logoHaleWidth = logoWidth >= codeWidth ? logoWidthMin : logoWidthMax;
		int logoHaleHeight = logoHeight >= codeWidth ? logoWidthMin : logoWidthMax;
		// 将logo图片按martix设置的信息缩放
		Matrix m = new Matrix();

		float sx = (float) 2 * logoHaleWidth / logoWidth;
		float sy = (float) 2 * logoHaleHeight / logoHeight;
		m.setScale(sx, sy);// 设置缩放信息
		Bitmap newLogoBitmap = Bitmap.createBitmap(logoBitmap, 0, 0, logoWidth, logoHeight, m, false);
		int newLogoWidth = newLogoBitmap.getWidth();
		int newLogoHeight = newLogoBitmap.getHeight();
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);//设置容错级别,H为最高
		//		hints.put(EncodeHintType.MAX_SIZE, LOGO_WIDTH_MAX);// 设置图片的最大值
		//		hints.put(EncodeHintType.MIN_SIZE, LOGO_WIDTH_MIN);// 设置图片的最小值
		//		hints.put(EncodeHintType.MARGIN, 2);//设置白色边距值 版本低设置不了
		// 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
		BitMatrix matrix = null;
		try {
			matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, codeWidth, codeWidth, hints);
		} catch (WriterException e) {
			e.printStackTrace();
		}
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		int halfW = width / 2;
		int halfH = height / 2;
		// 二维矩阵转为一维像素数组,也就是一直横着排了
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (x > halfW - newLogoWidth / 2 && x < halfW + newLogoWidth / 2 && y > halfH - newLogoHeight / 2 && y < halfH + newLogoHeight / 2) {// 该位置用于存放图片信息
					pixels[y * width + x] = newLogoBitmap.getPixel(x - halfW + newLogoWidth / 2, y - halfH + newLogoHeight / 2);
				} else {
					pixels[y * width + x] = matrix.get(x, y) ? Color.BLACK : Color.WHITE;// 设置信息
				}
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		// 通过像素数组生成bitmap,具体参考api
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	/**
	 * @return 返回带有白色背景框logo
	 */
	public static Bitmap modifyLogo(Bitmap bgBitmap, Bitmap logoBitmap) {

		int bgWidth = bgBitmap.getWidth();
		int bgHeight = bgBitmap.getHeight();
		//通过ThumbnailUtils压缩原图片，并指定宽高为背景图的3/4
		logoBitmap = ThumbnailUtils.extractThumbnail(logoBitmap, bgWidth * 3 / 4, bgHeight * 3 / 4, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		Bitmap cvBitmap = Bitmap.createBitmap(bgWidth, bgHeight, Config.ARGB_8888);
		Canvas canvas = new Canvas(cvBitmap);
		// 开始合成图片
		canvas.drawBitmap(bgBitmap, 0, 0, null);
		canvas.drawBitmap(logoBitmap, (bgWidth - logoBitmap.getWidth()) / 2, (bgHeight - logoBitmap.getHeight()) / 2, null);
		canvas.save(Canvas.ALL_SAVE_FLAG);// 保存
		canvas.restore();
		if (cvBitmap.isRecycled()) {
			cvBitmap.recycle();
		}
		return cvBitmap;
	}

	public static void freeBitmap() {
		if (mBitmap != null && !mBitmap.isRecycled()) {
			mBitmap.recycle();
			mBitmap = null;
		}
	}
}
