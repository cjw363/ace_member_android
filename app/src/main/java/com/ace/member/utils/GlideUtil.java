package com.ace.member.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.ace.member.BuildConfig;
import com.ace.member.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;

public class GlideUtil {

	/**
	 * 加载原图
	 *
	 * @param context
	 * @param portrait
	 * @param view
	 * @param defaultResource 默认图片
	 */
	public static void loadNormalImage(Context context, String portrait, ImageView view, int defaultResource) {
		//		view.setTag(null);//需要清空tag，否则报错
		view.setTag(R.id.image_url, portrait);
		if (TextUtils.isEmpty(portrait)) {
			Glide.with(context).load(defaultResource).asBitmap().diskCacheStrategy(DiskCacheStrategy.SOURCE).centerCrop().into(view);
		} else {
			if ((view.getTag(R.id.image_url)).equals(portrait))
				Glide.with(context).load(getRealNormalPortrait(portrait)).asBitmap().diskCacheStrategy(DiskCacheStrategy.SOURCE).centerCrop().into(view);
		}
	}

	public static void loadThumbnailImage(Context context, String portrait, ImageView view, int defaultResource) {
		//		view.setTag(null);//需要清空tag，否则报错
		view.setTag(R.id.image_url, portrait);
		if (TextUtils.isEmpty(portrait)) {
			Glide.with(context).load(defaultResource).asBitmap().centerCrop().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(view);
		} else {
			if ((view.getTag(R.id.image_url)).equals(portrait))
				Glide.with(context).load(getRealThumbnailPortrait(portrait)).asBitmap().diskCacheStrategy(DiskCacheStrategy.SOURCE).centerCrop().into(view);
		}
	}

	/**
	 * 加载bitmap对象
	 *
	 * @param context
	 * @param portrait
	 * @param target
	 * @param defaultResource
	 */
	public static void loadBitmap(Context context, String portrait, int defaultResource, SimpleTarget<Bitmap> target) {
		if (TextUtils.isEmpty(portrait)) {
			Glide.with(context).load(defaultResource).asBitmap().diskCacheStrategy(DiskCacheStrategy.SOURCE).centerCrop().into(target);
		} else {
			Glide.with(context).load(getRealNormalPortrait(portrait)).asBitmap().diskCacheStrategy(DiskCacheStrategy.SOURCE).centerCrop().into(target);
		}
	}

	/**
	 * 加载原头像
	 */
	public static void loadNormalPortrait(Context context, String portrait, ImageView view) {
		loadNormalImage(context, portrait, view, R.drawable.head_portrait_1);
	}

	/**
	 * 加载缩略头像
	 */
	public static void loadThumbnailPortrait(Context context, String portrait, ImageView view) {
		loadThumbnailImage(context, portrait, view, R.drawable.head_portrait_1);
	}

	//获取原图地址
	private static String getRealNormalPortrait(String portrait) {
		return BuildConfig.FILE_BASE_URL + "images/normal/" + portrait;
	}

	//获取缩略图地址
	private static String getRealThumbnailPortrait(String portrait) {
		return BuildConfig.FILE_BASE_URL + "images/thumbnails/" + portrait;
	}
}
