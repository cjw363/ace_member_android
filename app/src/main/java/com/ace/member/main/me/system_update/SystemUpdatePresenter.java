package com.ace.member.main.me.system_update;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import com.ace.member.BuildConfig;
import com.ace.member.base.BasePresenter;
import com.ace.member.utils.Session;
import com.og.LibGlobal;
import com.og.M;
import com.og.event.MessageEvent;
import com.og.http.OkHttpClientManager;
import com.og.utils.DialogFactory;
import com.og.utils.FileUtils;
import com.og.utils.NetUtils;
import com.og.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.inject.Inject;

import okhttp3.Request;


public class SystemUpdatePresenter extends BasePresenter implements SystemUpdateContract.Presenter {
	private final SystemUpdateContract.View mView;
	private String mApkUrl;
	private String mSavePath;
	private String mApkName;
	private DownloadApkThread mDownloadApkThread;

	@Inject
	public SystemUpdatePresenter(SystemUpdateContract.View view, Context context) {
		super(context);
		this.mView = view;
	}


	@Override
	public void start() {
		requestUpdateLog();
	}

	@Override
	public void stop() {
		if(mDownloadApkThread!=null && mDownloadApkThread.isAlive()){
			mDownloadApkThread.interrupt();
		}
	}

	@Override
	public void download() {
		mView.enableContent(false);
		mView.enableDownloadProgress(true);
		mDownloadApkThread= new DownloadApkThread(new DownloadCallback() {
			@Override
			public void progress(final int progress) {
				Utils.runOnUIThread(new Runnable() {
					@Override
					public void run() {
						mView.setPbProgress(progress);
						mView.setTvProgressText(progress + "%");
					}
				});

			}
		});
		mDownloadApkThread.start();
	}

	private void requestUpdateLog() {
		if (mContext != null && !NetUtils.isNetworkAvailable(mContext)) {
			EventBus.getDefault().post(new MessageEvent(M.MessageCode.ERR_401_NETWORK_INVALID));
			DialogFactory.unblock();
			return;
		}

		OkHttpClientManager.getAsyn(BuildConfig.UPGRADE_URL, new OkHttpClientManager.ResultCallback<String>() {
			@Override
			public void onError(Request request, Exception e) {
				EventBus.getDefault().post(new MessageEvent(M.MessageCode.ERR_411_UPDATE_ERROR));
			}

			@Override
			public void onResponse(String response) {
				parseJson(response);
			}
		});
	}

	private void parseJson(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			String updateMessage = obj.optString(LibGlobal.APK_UPDATE_CONTENT);
			mApkUrl = obj.optString(LibGlobal.APK_DOWNLOAD_URL);
			int apkCode = obj.optInt(LibGlobal.APK_VERSION_CODE);
			mApkName = obj.getString(LibGlobal.APK_NAME);
			mView.setUpdateLog(updateMessage);
			int versionCode = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
			if (apkCode > versionCode) {
				mView.enableDownloadButton(true);
				Session.hasNewVersion=true;
			} else {
				mView.enableDownloadButton(false);
				EventBus.getDefault().post(new MessageEvent(M.MessageCode.ERR_415_NO_UPDATE));
			}
		} catch (Exception e) {
			mView.enableDownloadButton(false);
			FileUtils.addErrorLog(e);
		}
	}

	private class DownloadApkThread extends Thread {
		DownloadCallback mDownloadCallback;
		DownloadApkThread(DownloadCallback callback){
			mDownloadCallback=callback;
		}
		@Override
		public void run() {
			try {
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					// 获得存储卡的路径
					String sdpath = Environment.getExternalStorageDirectory() + "/";
					mSavePath = sdpath + "download";

					URL url = new URL(mApkUrl);
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.connect();
					// 获取文件大小
					int length = conn.getContentLength();
					// 创建输入流
					InputStream is = conn.getInputStream();

					File file = new File(mSavePath);
					// 判断文件目录是否存在
					if (!file.exists()) {
						file.mkdir();
					}
					File apkFile = new File(mSavePath, mApkName);
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中
					while (true){
						int numread = is.read(buf);
						count += numread;
						// 计算进度条位置
						int progress = (int) (((float) count / length) * 100);
						// 更新进度

						if(mDownloadCallback!=null){
							mDownloadCallback.progress(progress);
						}

						if (numread <= 0) {
							// 下载完成
							if(mDownloadCallback!=null){
								mDownloadCallback.progress(100);
							}
							installApk();
							mView.finish();
							break;
						}
						// 写入文件
						fos.write(buf, 0, numread);
					}
					fos.close();
					is.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	interface DownloadCallback{
		void progress(int progress);
	}

	/**
	 * 安装APK文件
	 */
	private void installApk() {
		File file = new File(mSavePath, mApkName);
		if (!file.exists()) {
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			Uri uri = FileProvider.getUriForFile(mContext, "com.ace.member.fileProvider", file);
			i.setDataAndType(uri, "application/vnd.android.package-archive");
		} else {
			i.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		}
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(i);
	}
}
