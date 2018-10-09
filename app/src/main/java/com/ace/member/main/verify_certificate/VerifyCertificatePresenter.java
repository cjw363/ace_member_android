package com.ace.member.main.verify_certificate;

import android.content.Context;
import android.text.TextUtils;

import com.ace.member.BuildConfig;
import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.bean.FileUploadResult;
import com.ace.member.bean.MemberProfile;
import com.ace.member.bean.Verify;
import com.ace.member.bean.VerifyDataWrapper;
import com.ace.member.listener.ICompressListener;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.Session;
import com.ace.member.utils.ThreadPoolUtil;
import com.og.http.HttpCall;
import com.og.http.IHttpCallback;
import com.og.http.OkHttpClientManager;
import com.og.http.SimpleRequestListener;
import com.og.http.UploadRequest;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;


public class VerifyCertificatePresenter extends BasePresenter implements VerifyCertificateContract.Presenter {
	private final VerifyCertificateContract.View mView;
	private String mToken;
	private List<Integer> mIdTypeList;
	private List<Integer> mSexList;
	private List<Integer> mNationalityList;
	private int mCurrentIdType;
	private int mCurrentSex;
	private int mCurrentNationality;
	private String mTempPath;
	private MemberProfile mProfile;

	@Inject
	public VerifyCertificatePresenter(VerifyCertificateContract.View view, Context context) {
		super(context);
		this.mView = view;
		mIdTypeList = new ArrayList<>(4);
		mIdTypeList.add(AppGlobal.CERTIFICATE_TYPE_1_ID);
		mIdTypeList.add(AppGlobal.CERTIFICATE_TYPE_2_PASSPORT);
		mIdTypeList.add(AppGlobal.CERTIFICATE_TYPE_3_DRIVE_LICENSE);
		mIdTypeList.add(AppGlobal.CERTIFICATE_TYPE_4_FAMILY_BOOK);

		mSexList = new ArrayList<>(2);
		mSexList.add(AppGlobal.SEX_MALE);
		mSexList.add(AppGlobal.SEX_FEMALE);

		mNationalityList = new ArrayList<>(7);
		mNationalityList.add(AppGlobal.COUNTRY_CODE_855_CAMBODIA);
		mNationalityList.add(AppGlobal.COUNTRY_CODE_84_VIETNAM);
		mNationalityList.add(AppGlobal.COUNTRY_CODE_86_CHINA);
		mNationalityList.add(AppGlobal.COUNTRY_CODE_66_THAILAND);
		mNationalityList.add(AppGlobal.COUNTRY_CODE_60_MALAYSIA);
		mNationalityList.add(AppGlobal.COUNTRY_CODE_62_INDONESIA);
		mNationalityList.add(AppGlobal.COUNTRY_CODE_33_FRENCH);
	}

	@Override
	public void start() {
		checkStatus();
	}

	@Override
	public void onTypeSelected(int position) {
		mCurrentIdType = mIdTypeList.get(position);
		mView.setIdType(position);
	}

	@Override
	public void onSexSelected(int position) {
		mCurrentSex = mSexList.get(position);
		mView.setSex(position);
	}

	@Override
	public void onNationalitySelected(int position) {
		mCurrentNationality = mNationalityList.get(position);
		mView.setNationality(position);
	}

	@Override
	public void btnSubmit(final String idNumber, final List<String> photos) {
		if (TextUtils.isEmpty(mToken)) {
			Utils.showToast(R.string.invalid_token);
			return;
		}
		if (mCurrentIdType <= 0) {
			Utils.showToast(R.string.please_select_id_type);
			return;
		}
		if (TextUtils.isEmpty(idNumber)) {
			Utils.showToast(R.string.invalid_id_number);
			return;
		}

		if (mProfile == null || mProfile.getFlagLock() != AppGlobal.FLAG_LOCK_YES) {
			if (mCurrentSex <= 0) {
				Utils.showToast(R.string.please_select_sex);
				return;
			}

			if (mCurrentNationality <= 0) {
				Utils.showToast(R.string.please_select_nationality);
				return;
			}

			if (TextUtils.isEmpty(mView.getBirthDay())) {
				Utils.showToast(R.string.please_select_birthday);
				return;
			}
		}


		if (Utils.isEmptyList(photos, true) || photos.size() != 2) {
			Utils.showToast(R.string.please_take_picture);
			return;
		}
		AppUtils.compressImages(photos, new ICompressListener.SimpleCompressListener() {
			@Override
			public void onStart(String path) {
				mTempPath = path;
			}

			@Override
			public void onError() {
				Utils.showToast(R.string.fail);
			}

			@Override
			public void onFinish(Map<String, File> map) {
				final String url = BuildConfig.FILE_BASE_URL + "web/?_b=aj&_a=images&cmd=uploadImage&type=idc";
				final UploadRequest uploadRequest = new UploadRequest();
				uploadRequest.setUrl(url);
				uploadRequest.setFileMap(map);
				ThreadPoolUtil.getInstance()
					.execute(new Runnable() {
						@Override
						public void run() {
							OkHttpClientManager.upload(uploadRequest, new IHttpCallback.SimpleHttpCallback() {
								@Override
								public void onStarted(HttpCall call) {
									mView.enableBtnSubmit(false);
								}

								@Override
								public void onError(Throwable throwable) {
									Utils.showToast(R.string.fail);
									mView.enableBtnSubmit(true);
								}

								@Override
								public void onResponse(String s) {
									try {
										FileUploadResult result = JsonUtil.jsonToBean(s, FileUploadResult.class);
										assert result != null;
										if (result.getErrCode() == 0) {
											Map<String, String> paramMap = new HashMap<>();
											paramMap.put("_s", Session.sSid);
											paramMap.put("_b", "aj");
											paramMap.put("_a", "verify");
											paramMap.put("cmd", "saveIDVerify");
											paramMap.put("certificate_type", String.valueOf(mCurrentIdType));
											paramMap.put("certificate_number", idNumber);
											paramMap.put("unique_token", mToken);
											paramMap.put("filename0", result.getFile0());
											paramMap.put("filename1", result.getFile1());
											if (mProfile == null || mProfile.getFlagLock() != AppGlobal.FLAG_LOCK_YES) {
												paramMap.put("sex", String.valueOf(mCurrentSex));
												paramMap.put("nationality", String.valueOf(mCurrentNationality));
												paramMap.put("birthday", mView.getBirthDay());
											}
											submit(paramMap, new SimpleRequestListener() {
												@Override
												public void loadSuccess(String result, String uniqueToken) {
													mView.setVerify(new Verify(mCurrentIdType, idNumber, AppGlobal.CERTIFICATE_STATUS_1_PENDING, ""));
													mView.setProfile2(new MemberProfile(mCurrentSex,mCurrentNationality));
													mView.enableRlStatus(true);
													mView.enableDetail(true);
													mView.enableBtnRecertify(false);
													mView.enableBtnSubmit(false);
													mView.enableSvVerify(false);
													VerifyCertificatePresenter.this.onFinish();
												}
											});
										}
									} catch (Exception e) {
										FileUtils.addErrorLog(e);
										Utils.showToast(R.string.fail);
										mView.enableBtnSubmit(true);
									}
								}
							});
						}
					});
			}
		});
	}

	@Override
	public void onFinish() {
		FileUtils.delete(mTempPath);
	}

	private void checkStatus() {
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("_s", Session.sSid);
		paramMap.put("_b", "aj");
		paramMap.put("_a", "verify");
		paramMap.put("cmd", "checkIDVerify");
		submit(paramMap, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					VerifyDataWrapper dataWrapper = JsonUtil.jsonToBean(result, VerifyDataWrapper.class);
					mToken = token;
					assert dataWrapper != null;
					mProfile = dataWrapper.getProfile();
					mView.setProfile(mProfile);
					if (mProfile != null) {
						mCurrentSex = mProfile.getSex();
						mCurrentNationality = mProfile.getNationality();
					}
					Verify verify = dataWrapper.getVerify();
					if (verify != null) {
						mView.setVerify(verify);
						mView.setProfile2(mProfile);
						mView.enableSvVerify(false);
						int status = verify.getStatus();
						if (status == AppGlobal.CERTIFICATE_STATUS_1_PENDING) {
							mView.enableRlStatus(true);
							mView.enableDetail(true);
							mView.enableBtnRecertify(false);
						} else {
							if (status == AppGlobal.CERTIFICATE_STATUS_2_ACCEPTED) {
								mView.enableRlStatus(true);
								mView.enableDetail(true);
								mView.enableBtnRecertify(false);
							} else if (status == AppGlobal.CERTIFICATE_STATUS_4_REJECTED) {
								mView.enableRlStatus(true);
								mView.enableDetail(true);
								mView.enableBtnRecertify(true);
							} else {
								fail();
							}
						}
					} else {
						mView.enableRlStatus(false);
						mView.enableDetail(false);
						mView.enableBtnRecertify(false);
						mView.enableSvVerify(true);
					}
				} catch (Exception e) {
					Utils.showToast(R.string.error);
					FileUtils.addErrorLog(e);
				}
			}

			@Override
			public void loadFailure(int code, String result, String token) {
				mToken = token;
				fail();
			}
		});
	}

	private void fail() {
		mView.enableRlStatus(false);
		mView.enableDetail(false);
		mView.enableBtnRecertify(false);
		mView.enableSvVerify(false);
	}
}
