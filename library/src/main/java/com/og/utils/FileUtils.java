package com.og.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.og.LibApplication;
import com.og.LibSession;
import com.og.exception.LibCrash;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class FileUtils {

	private static String TAG = "FileUtils";

	public static boolean addJpgToGallery(Bitmap signature, Context context) {
		boolean result = false;
		try {
			File photo = new File(getAlbumStorageDir("KHE_Signature"), String.format("KHE_Signature_%d.jpg", System.currentTimeMillis()));
			saveBitmapToJPG(signature, photo);
			scanMediaFile(photo, context);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static File getAlbumStorageDir(String albumName) {
		// Get the directory for the user's public pictures directory.
		File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName);
		if (!file.mkdirs()) {
			Log.e("Signature", "Directory not created");
		}
		return file;
	}

	public static void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
		try {
			Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(newBitmap);
			canvas.drawColor(Color.WHITE);
			canvas.drawBitmap(bitmap, 0, 0, null);
			OutputStream stream = new FileOutputStream(photo);
			newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
			stream.close();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}

	}

	private static void scanMediaFile(File photo, Context context) {
		try {
			Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			Uri contentUri = Uri.fromFile(photo);
			mediaScanIntent.setData(contentUri);
			context.sendBroadcast(mediaScanIntent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Bitmap loadBitmapFromView(View v) {
		if (v == null) {
			return null;
		}
		try {
			Bitmap img;
			img = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.RGB_565);
			Canvas c = new Canvas(img);
			v.draw(c);
			return img;
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return null;
	}

	public static boolean saveImageFile(Bitmap bm, String fileName, String path, Context context) throws IOException {
		boolean result = false;
		try {
			File folder = new File(path);
			if (!folder.exists()) {
				folder.mkdirs();
			}

			File myCaptureFile = new File(path, fileName);
			if (!myCaptureFile.exists()) {
				myCaptureFile.createNewFile();
			}
			scanMediaFile(myCaptureFile, context);

			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
			bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
			bos.flush();
			bos.close();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public static String bitmap2Str(Bitmap bit) {
		if (bit == null) return null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			bit.compress(Bitmap.CompressFormat.JPEG, 80, bos);
			byte[] bytes = bos.toByteArray();
			return Base64.encodeToString(bytes, Base64.DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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

	public static void addErrorLog(Throwable e) {
		LibCrash log = new LibCrash();
		String errorInfo = Utils.getErrorInfo(e);
		log.execute(LibSession.sVersionName, errorInfo);
	}

	public static void addTestLog(String str) {
		LibCrash log = new LibCrash();
		log.execute(LibSession.sVersionName, str);
	}

	public static Uri getUriForFile(File file) {
		if (file == null) {
			return null;
		}
		Uri uri;
		if (Build.VERSION.SDK_INT >= 24) {
			uri = FileProvider.getUriForFile(LibApplication.getContext(), "com.ace.member.fileProvider", file);
		} else {
			uri = Uri.fromFile(file);
		}
		return uri;
	}

	public static String getCameraImageFilePath() {
		if (Environment.getExternalStorageState()
			.equals(Environment.MEDIA_MOUNTED)) {
			String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/ace-member/picture/";
			File file = new File(path);
			if (!file.exists()) file.mkdirs();
			return path;
		}
		return "";
	}

	public static String getTempPath() {
		if (Environment.getExternalStorageState()
			.equals(Environment.MEDIA_MOUNTED)) {
			String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/ace-member/temp/picture/" + UUID.randomUUID()
				.toString() + "/";
			File file = new File(path);
			if (!file.exists()) file.mkdirs();
			return path;
		}
		return "";
	}

	public static void delete(String path) {
		if (TextUtils.isEmpty(path)) return;
		File file = new File(path);
		if (file.exists()) {
			if (file.isFile()) file.delete();
			else {
				File[] files = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					files[i].delete();
				}
				file.delete();
			}
		}
	}

	public static String getCachePath() {
		return LibApplication.getContext()
			.getCacheDir()
			.getAbsolutePath();
	}
}
