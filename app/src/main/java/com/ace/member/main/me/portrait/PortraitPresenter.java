package com.ace.member.main.me.portrait;

import android.content.Context;
import android.support.design.widget.Snackbar;

import com.ace.member.BuildConfig;
import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.bean.FileUploadResult;
import com.ace.member.bean.PortraitBean;
import com.ace.member.listener.ICompressListener;
import com.ace.member.main.image_detail.Image;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.Session;
import com.ace.member.utils.ThreadPoolUtil;
import com.og.http.IHttpCallback;
import com.og.http.OkHttpClientManager;
import com.og.http.SimpleRequestListener;
import com.og.http.UploadRequest;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

final class PortraitPresenter extends BasePresenter implements PortraitContract.Presenter {

	private final PortraitContract.View mView;
	private String mToken;

	@Inject
	public PortraitPresenter(Context context, PortraitContract.View view) {
		super(context);
		mView = view;
	}

	@Override
	public void start() {
		getPortrait();
	}

	@Override
	public void upload(List<Image> list) {
		if (Utils.isEmptyList(list)) {
			Utils.showToast(R.string.please_select_image, Snackbar.LENGTH_LONG);
			return;
		}
		mView.enableSubmit(false);
		try {
			final UploadRequest uploadRequest = new UploadRequest();
			final String url = BuildConfig.FILE_BASE_URL + "web/?_b=aj&_a=images&cmd=uploadImage&type=portrait";
			final String[] tempPath = new String[1];
			AppUtils.compressImages2(list, new ICompressListener.SimpleCompressListener() {
				@Override
				public void onStart(String path) {
					tempPath[0] = path;
				}

				@Override
				public void onError() {
					Utils.showToast(R.string.fail, Snackbar.LENGTH_SHORT);
					mView.enableSubmit(true);
				}

				@Override
				public void onFinish(final Map<String, File> map) {
					uploadRequest.setUrl(url);
					uploadRequest.setFileMap(map);
					ThreadPoolUtil.getInstance()
						.execute(new Runnable() {
							@Override
							public void run() {
								OkHttpClientManager.upload(uploadRequest, new IHttpCallback.SimpleHttpCallback() {
									@Override
									public void onResponse(String s) {
										try {
											FileUploadResult uploadResult = JsonUtil.jsonToBean(s, FileUploadResult.class);
											assert uploadResult != null;
											if (uploadResult.getErrCode() == 0) {
												Map<String, String> params = new HashMap<>();
												params.put("_s", Session.sSid);
												params.put("_a", "user");
												params.put("_b", "aj");
												params.put("cmd", "updatePortrait");
												params.put("unique_token", mToken);
												params.put("image", uploadResult.getFile0());
												submit(params, new SimpleRequestListener() {
													@Override
													public void loadSuccess(String result, String uniqueToken) {
														mToken = uniqueToken;
														mView.enableSubmit(false);
														mView.showSuccess();
													}

													@Override
													public void loadFailure(int errorCode, String result, String uniqueToken) {
														mToken = uniqueToken;
														mView.enableSubmit(true);
														Utils.showToast(R.string.fail, Snackbar.LENGTH_SHORT);
													}
												});
											}
										} catch (Exception e) {
											onError(e);
										} finally {
											FileUtils.delete(tempPath[0]);
										}
									}

									@Override
									public void onError(Throwable throwable) {
										mView.enableSubmit(true);
										Utils.showToast(R.string.fail, Snackbar.LENGTH_SHORT);
										FileUtils.delete(tempPath[0]);
										throwable.printStackTrace();
									}
								});
							}
						});
				}
			});
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			mView.enableSubmit(true);
		}
	}


	private void getPortrait() {
		Map<String, String> params = new HashMap<>();
		params.put("_s", Session.sSid);
		params.put("_a", "user");
		params.put("_b", "aj");
		params.put("cmd", "getPortrait");
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				mToken = uniqueToken;
				PortraitBean portraitBean = JsonUtil.jsonToBean(result, PortraitBean.class);
				mView.setPortrait(portraitBean);
			}
		});
	}


}
