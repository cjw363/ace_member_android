package com.og.update;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.og.LibApplication;
import com.og.LibGlobal;
import com.og.LibSession;
import com.og.M;
import com.og.R;
import com.og.event.MessageEvent;
import com.og.utils.CustomDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UpdateActivity extends Activity {

	private boolean isInstallApkAfter = false;
	private Bundle mParams;
	private String mSavePath;
	private boolean cancelUpdate = false;
	private boolean idProgress = false;
	private ScrollView slContent;
	private LinearLayout llProgress;
	private LinearLayout llDownload;
	private TextView tvUpdateProgress;
	private int progress = 0;
	private final int PROGRESS_UPDATE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update);
		initActivity();
	}

	public void initActivity() {
		mParams = getIntent().getExtras();
		String content = mParams.getString(LibGlobal.APK_UPDATE_CONTENT, "");

		String title = getResources().getString(R.string.new_version);
		TextView tvTitle = (TextView) findViewById(R.id.tv_title);

		FrameLayout btnBack = (FrameLayout) findViewById(R.id.fl_back);
		ImageView ivBack = (ImageView) findViewById(R.id.iv_back);
		ivBack.setVisibility(View.VISIBLE);
		Button btnDownload = (Button) findViewById(R.id.btn_download);
		llDownload = (LinearLayout) findViewById(R.id.ll_download);
		TextView tvContent = (TextView) findViewById(R.id.tv_content);
		slContent = (ScrollView) findViewById(R.id.scr_content);
		llProgress = (LinearLayout) findViewById(R.id.ll_update_progress);
		tvUpdateProgress = (TextView) findViewById(R.id.tv_update_progress);
		llDownload.setVisibility(View.VISIBLE);
		slContent.setVisibility(View.VISIBLE);
		btnBack.setVisibility(View.VISIBLE);
		llProgress.setVisibility(View.GONE);

		progress = 0;
		tvUpdateProgress.setText(progress + "%");
		tvContent.setText(content.trim());
		tvTitle.setText(title);

		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cancelUpdate = idProgress;
				if (LibSession.sVersionStatus == LibGlobal.APK_VERSION_STATUS_2_UNSUPPORTED) {
					if (idProgress) {
						EventBus.getDefault().post(new MessageEvent(M.MessageCode.ERR_414_CANCEL_DOWNLOAD));
						finish();
					} else {
						showNotSupported();
					}
				} else {
					EventBus.getDefault().post(new MessageEvent(M.MessageCode.ERR_414_CANCEL_DOWNLOAD));
					finish();
				}
			}
		});

		btnDownload.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				idProgress = true;
				showDownloadProgress();
				EventBus.getDefault().post(new MessageEvent(M.MessageCode.ERR_412_START_DOWNLOAD));
			}
		});
	}

	/**
	 * 显示软件下载进度条
	 */
	private void showDownloadProgress() {
		slContent.setVisibility(View.GONE);
		llDownload.setVisibility(View.GONE);
		llProgress.setVisibility(View.VISIBLE);
		downloadApk();
	}

	private void downloadApk() {
		new downloadApkThread().start();
	}

	private class downloadApkThread extends Thread {
		@Override
		public void run() {
			isInstallApkAfter = false;
			try {
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					// 获得存储卡的路径
					String sdpath = Environment.getExternalStorageDirectory() + "/";
					mSavePath = sdpath + "download";
					String path = mParams.getString(LibGlobal.APK_DOWNLOAD_URL);
					URL url = new URL(path);
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
					String name = mParams.getString(LibGlobal.APK_NAME);
					File apkFile = new File(mSavePath, name);
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中

					do {
						int numread = is.read(buf);
						count += numread;
						// 计算进度条位置
						progress = (int) (((float) count / length) * 100);
						Message message = new Message();
						message.what = PROGRESS_UPDATE;
						mHandler.sendMessage(message);
						// 更新进度

						//						Message msg = new Message();
						//						msg.what = Constants.ERR_412_START_DOWNLOAD;
						//						msg.arg1 = progress;
						//						Constants.mHandler.sendMessage(msg);
						ProgressBar progressbar = (ProgressBar) findViewById(R.id.update_progress);
						progressbar.setProgress(progress);

						if (numread <= 0) {
							// 下载完成
							finish();
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
		}
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case PROGRESS_UPDATE:
					tvUpdateProgress.setText(progress + "%");
					break;
			}
			super.handleMessage(msg);
		}
	};

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
			i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			Uri uri= FileProvider.getUriForFile(LibApplication.getContext(),"com.ace.member.fileProvider",file);
			i.setDataAndType(uri, "application/vnd.android.package-archive");
		}else {
			i.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}
		UpdateActivity.this.startActivity(i);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		cancelUpdate = true;
		if (LibSession.sVersionStatus == LibGlobal.APK_VERSION_STATUS_2_UNSUPPORTED) {
			if (idProgress) {
				EventBus.getDefault().post(new MessageEvent(M.MessageCode.ERR_414_CANCEL_DOWNLOAD));
				finish();
			} else {
				showNotSupported();
			}
			return false;
		} else {
			EventBus.getDefault().post(new MessageEvent(M.MessageCode.ERR_414_CANCEL_DOWNLOAD));
			finish();
			return super.onKeyDown(keyCode, event);
		}
	}

	public void showNotSupported() {
		Dialog mDialog = new CustomDialog.Builder(UpdateActivity.this, R.layout.dialog_normal_layout).setMessage(R.string.not_supported_version).setIcon(R.drawable.ic_error).setMessageColor(R.color.lib_color11).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).create();
		mDialog.setCancelable(false);
		mDialog.show();
	}
}
