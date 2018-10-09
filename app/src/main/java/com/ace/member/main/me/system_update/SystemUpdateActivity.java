package com.ace.member.main.me.system_update;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ace.member.BuildConfig;
import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.login.LoginActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.Session;
import com.ace.member.utils.StringUtil;
import com.og.LibGlobal;
import com.og.LibSession;
import com.og.update.UpdateChecker;
import com.og.utils.CustomDialog;
import com.og.utils.SlidingLayout;
import com.og.utils.Utils;

import javax.inject.Inject;

import butterknife.BindView;


public class SystemUpdateActivity extends BaseActivity implements SystemUpdateContract.View {
	@Inject
	SystemUpdatePresenter mPresenter;
	@BindView(R.id.tv_current_version)
	TextView mTvCurrentVersion;
	@BindView(R.id.tv_update_log)
	TextView mTvUpdateLog;
	@BindView(R.id.sl)
	SlidingLayout mSl;
	@BindView(R.id.fl_download)
	FrameLayout mFlDownload;
	@BindView(R.id.btn_download)
	Button mBtnDownload;
	@BindView(R.id.rl_progress)
	RelativeLayout mRlProgress;
	@BindView(R.id.pb_download)
	ProgressBar mPbDownload;
	@BindView(R.id.tv_progress)
	TextView mTvProgress;


	private UpdateChecker mUpdateChecker = new UpdateChecker();

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerSystemUpdateComponent.builder()
			.systemUpdatePresenterModule(new SystemUpdatePresenterModule(this, this))
			.build()
			.inject(this);
		init();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_system_update;
	}

	@SuppressLint("SetTextI18n")
	private void init() {
		ToolBarConfig.builder(this, null)
			.setTvTitleRes(R.string.system_update)
			.setEnableBack(true)
			.setBackListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (LibSession.sVersionStatus == LibGlobal.APK_VERSION_STATUS_2_UNSUPPORTED) {
						showNotSupported();
					} else {
						onBackPressed();
					}
				}
			})
			.build();
		StringBuilder s = new StringBuilder("V ");
		s.append(LibSession.sVersionName);
		s.append(" (");
		s.append(Session.hasNewVersion ? Utils.getString(R.string.need_update) : Utils.getString(R.string.latest_version));
		s.append(")");
		mTvCurrentVersion.setText(s.toString());
		mPresenter.start();
		mBtnDownload.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mPresenter.download();
			}
		});
	}

	public void showNotSupported() {
		Dialog mDialog = new CustomDialog.Builder(SystemUpdateActivity.this, com.og.R.layout.dialog_normal_layout)
			.setMessage(com.og.R.string.not_supported_version)
			.setIcon(com.og.R.drawable.ic_error)
			.setMessageColor(com.og.R.color.lib_color11)
			.setPositiveButton(com.og.R.string.ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			})
			.create();
		mDialog.setCancelable(false);
		mDialog.show();
	}

	@Override
	public void showCheckForDialog() {
		mUpdateChecker.checkForDialog(SystemUpdateActivity.this, BuildConfig.UPGRADE_URL);
	}

	@Override
	public void setUpdateLog(String log) {
		mTvUpdateLog.setText(StringUtil.dealUpdateLog(log));
	}

	@Override
	public void enableContent(boolean enable) {
		mSl.setVisibility(enable ? View.VISIBLE : View.GONE);
		mFlDownload.setVisibility(enable ? View.VISIBLE : View.GONE);
	}

	@Override
	public void enableDownloadButton(boolean enable) {
		mBtnDownload.setEnabled(enable);
	}

	@Override
	public void enableDownloadProgress(boolean enable) {
		mRlProgress.setVisibility(enable ? View.VISIBLE : View.GONE);
	}

	@Override
	public void setPbProgress(int progress) {
		mPbDownload.setProgress(progress);
	}

	@Override
	public void setTvProgressText(String progress) {
		mTvProgress.setText(progress);
	}

	@Override
	public void onBackPressed() {
		mPresenter.stop();
		if (!TextUtils.isEmpty(getIntent().getStringExtra("type"))) {
			Utils.toActivity(this, LoginActivity.class);
			finish();
			return;
		}
		super.onBackPressed();
	}
}
