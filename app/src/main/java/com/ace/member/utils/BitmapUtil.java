package com.ace.member.utils;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Base64;

import com.og.utils.FileUtils;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class BitmapUtil {

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}

			//			long totalPixels = width * height / inSampleSize;
			//			final long totalReqPixelsCap = reqWidth * reqHeight * 2;
			//
			//			while (totalPixels > totalReqPixelsCap) {
			//				inSampleSize *= 2;
			//				totalPixels /= 2;
			//			}
		}
		return inSampleSize;
	}

	/**
	 * 从 fileDescriptor 解码图片
	 *
	 * @param fileDescriptor fileDescriptor
	 * @param reqWidth       reqWidth
	 * @param reqHeight      reqHeight
	 * @return Bitmap
	 */
	public static Bitmap decodeSampledBitmapFromDescriptor(FileDescriptor fileDescriptor, int reqWidth, int reqHeight) {

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
	}

	/**
	 * 从 文件路径解码图片
	 *
	 * @param path      图片路径
	 * @param reqWidth  reqWidth
	 * @param reqHeight reqHeight
	 * @return Bitmap
	 */
	public static Bitmap decodeSampledBitmapFromFilePath(String path, int reqWidth, int reqHeight) {

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, options);
	}


	/**
	 * 从资源文件解码图片
	 *
	 * @param res       Resources
	 * @param resId     资源id
	 * @param reqWidth  reqWidth
	 * @param reqHeight reqHeight
	 * @return Bitmap
	 */
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	/**
	 * 创建缩略图
	 *
	 * @param sourceBitmap 原图
	 * @param reqWidth     要求的宽度
	 * @param reqHeight    要求的高度
	 * @param isFilter     是否适配
	 * @return Bitmap
	 */
	public static Bitmap scaleBitmapFromBitmap(@NonNull Bitmap sourceBitmap, int reqWidth, int reqHeight, boolean isFilter) {
		return Bitmap.createScaledBitmap(sourceBitmap, reqWidth, reqHeight, isFilter);
	}

	public static Bitmap scaleBitmapFromBitmap(@NonNull Bitmap sourceBitmap, float scale, boolean isFilter) {
		int width = (int) (sourceBitmap.getWidth() * scale + 0.5);
		int height = (int) (sourceBitmap.getHeight() * scale + 0.5);
		return scaleBitmapFromBitmap(sourceBitmap, width, height, isFilter);
	}

	public static void scaleBitmapAndSave(@NonNull String sourcePath, String destPath, Bitmap.CompressFormat compressFormat, int scale, int quality) {
		AppUtils.checkFilePath(destPath);
		Bitmap bitmap = BitmapFactory.decodeFile(sourcePath);
		bitmap = scaleBitmapFromBitmap(bitmap, scale, true);
		try {
			bitmap.compress(compressFormat, quality, new FileOutputStream(destPath));
		} catch (FileNotFoundException e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
	}

	public static void scaleBitmapAndSave(@NonNull String sourcePath, String destPath, Bitmap.CompressFormat compressFormat, int width, int height, int quality) {
		AppUtils.checkFilePath(destPath);
		Bitmap bitmap = decodeSampledBitmapFromFilePath(sourcePath, width, height);
		try {
			bitmap.compress(compressFormat, quality, new FileOutputStream(destPath));
		} catch (FileNotFoundException e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
	}

	public static Bitmap str2Bitmap(String str) {
		Bitmap bitmap = null;
		if (null == str || str.equals("")) return null;
		try {
			byte[] bitmapArr;
			bitmapArr = Base64.decode(str, Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(bitmapArr, 0, bitmapArr.length);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	public static void saveBitmap(Bitmap bitmap, String path) {
		try {
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 在5.0以上的系统会出现空指针，原因在于此本来方法不能将vector转化为bitmap
	 */
	public static Bitmap getBitmap(Context context, int vectorDrawableId) {
		Bitmap bitmap = null;
		try {
			if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
				Drawable vectorDrawable = context.getDrawable(vectorDrawableId);
				assert vectorDrawable != null;
				bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
				Canvas canvas = new Canvas(bitmap);
				vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
				vectorDrawable.draw(canvas);
			} else {
				bitmap = BitmapFactory.decodeResource(context.getResources(), vectorDrawableId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}
}
