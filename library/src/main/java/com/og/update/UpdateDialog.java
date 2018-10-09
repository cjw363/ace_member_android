package com.og.update;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.og.LibApplication;
import com.og.LibGlobal;
import com.og.M;
import com.og.event.MessageEvent;
import com.og.utils.CustomDialog;
import com.og.utils.Utils;
import com.og.R;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UpdateDialog extends DialogFragment {

	private boolean cancelUpdate = false;
	private Dialog mDownloadDialog;
	private String mSavePath;
	private Context mContext;
	private Bundle mParams;
	private View mProgressView;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		mContext = getActivity();
		mParams = getArguments();
		CustomDialog.Builder builder = new CustomDialog.Builder(mContext, R.layout.dialog_normal_layout);
		builder.setMessage(mParams.getString(LibGlobal.APK_UPDATE_CONTENT)).setPositiveButton(R.string.download_now, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				if (Utils.isFastClick(mContext)) return;
				showDownloadDialog();
				EventBus.getDefault().post(new MessageEvent(M.MessageCode.ERR_412_START_DOWNLOAD));
				dismiss();
			}
		}).setNegativeButton(R.string.download_later, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				EventBus.getDefault().post(new MessageEvent(M.MessageCode.ERR_414_CANCEL_DOWNLOAD));
				dismiss();
			}
		}).setGravity(Gravity.START);
		setCancelable(false);
		return builder.create();
	}

	/**
	 * 显示软件下载对话框
	 */
	private void showDownloadDialog() {
		try{
			// 构造软件下载对话框
			CustomDialog.Builder builder = new CustomDialog.Builder(mContext, R.layout.dialog_normal_layout);
//			builder.setTitle(R.string.app_update_being_updated);
			// 给下载对话框增加进度条
			final LayoutInflater inflater = LayoutInflater.from(mContext);
			mProgressView = inflater.inflate(R.layout.app_update_progress, null);
			builder.setContentView(mProgressView);
			// 取消更新
			builder.setPositiveButton(R.string.app_update_cancel, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					// 设置取消状态
					cancelUpdate = true;
					EventBus.getDefault().post(new MessageEvent(M.MessageCode.ERR_414_CANCEL_DOWNLOAD));
				}
			});
			mDownloadDialog = builder.create();
			mDownloadDialog.setCancelable(false);
			mDownloadDialog.show();

			downloadApk();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	private void downloadApk() {
		new downloadApkThread().start();
	}

	private class downloadApkThread extends Thread {
		@Override
		public void run() {
			try {
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					// 获得存储卡的路径
					String sdpath = Environment.getExternalStorageDirectory() + "/";
					mSavePath = sdpath + "download";

					URL url = new URL(mParams.getString(LibGlobal.APK_DOWNLOAD_URL));
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
					File apkFile = new File(mSavePath, mParams.getString(LibGlobal.APK_NAME));
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中
					do {
						int numread = is.read(buf);
						count += numread;
						// 计算进度条位置
						int progress = (int) (((float) count / length) * 100);
						// 更新进度

//						Message msg = Message.obtain();
//						msg.what = Constants.ERR_412_START_DOWNLOAD;
//						msg.arg1 = progress;
//						Constants.mHandler.sendMessage(msg);

						ProgressBar progressbar = (ProgressBar) mProgressView.findViewById(R.id.update_progress);
						progressbar.setProgress(progress);

						if (numread <= 0) {
							// 下载完成
							EventBus.getDefault().post(new MessageEvent(M.MessageCode.ERR_413_DOWNLOAD_FINISH));
							installApk();
							break;
						}
						// 写入文件
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);// 点击取消就停止下载.
					fos.close();
					is.close();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 取消下载对话框显示
			mDownloadDialog.dismiss();
		}
	}

	/**
	 * 安装APK文件
	 */
	private void installApk() {
		File file = new File(mSavePath, mParams.getString(LibGlobal.APK_NAME));
		if (!file.exists()) {
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			Uri uri= FileProvider.getUriForFile(LibApplication.getContext(),"com.ace.member.fileProvider",file);
			i.setDataAndType(uri, "application/vnd.android.package-archive");
		}else {
			i.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		}
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(i);
	}
}
