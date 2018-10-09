package com.ace.member.main.third_party.wsa;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.SparseArray;

import com.ace.member.BuildConfig;
import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.bean.EdcWsaConfig;
import com.ace.member.bean.EdcWsaDataWrapper;
import com.ace.member.bean.FileUploadResult;
import com.ace.member.bean.SingleStringBean;
import com.ace.member.bean.WsaBill;
import com.ace.member.listener.ICompressListener;
import com.ace.member.main.image_detail.Image;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.M;
import com.ace.member.utils.PayHelper;
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


public class WsaPresenter extends BasePresenter implements WsaContract.Presenter {
	private final WsaContract.View mView;
	private String mToken;
	private SparseArray<String> mSparseArray;
	private String[] mWsaTypeArr;
	private List<EdcWsaConfig> mConfigs;
	private WsaBill mLatestBill;

	@Inject
	public WsaPresenter(WsaContract.View view, Context context) {
		super(context);
		this.mView = view;
	}

	@Override
	public void start() {
		if (mSparseArray == null)
			mSparseArray = AppUtils.getEdcWsaTypeArr(AppGlobal.PAYMENT_TYPE_2_WSA);
		if (mWsaTypeArr == null) mWsaTypeArr = AppUtils.getEdcWsaTypeArr2(AppGlobal.PAYMENT_TYPE_2_WSA);
		getConfig();
	}

	@Override
	public void updateFee(String amount) {
		if (!Utils.isEmptyList(mConfigs) && !TextUtils.isEmpty(amount)) {
			Double a = Double.parseDouble(amount);
			calculateFee(a);
		} else {
			mView.setFee("0");
		}
	}

	private void calculateFee(double a) {
		if (a <= 0) {
			mView.setFee("0");
			return;
		}
		int split;

		if (a <= AppGlobal.WSA_SPLIT_1_50000) split = 1;
		else if (a < AppGlobal.WSA_SPLIT_2_400000) split = 2;
		else split = 3;

		for (EdcWsaConfig config : mConfigs) {
			if (config.getSplit() == split) {
				mView.setFee(String.valueOf(config.getFee()));
				return;
			}
		}
		mView.setFee("0");
	}


	private void getConfig() {
		Map<String, String> params = new HashMap<>();
		params.put("_s", Session.sSid);
		params.put("_a", "payment");
		params.put("_b", "aj");
		params.put("cmd", "getConfigWsa");
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					EdcWsaDataWrapper edcWsaDataWrapper = JsonUtil.jsonToBean(result, EdcWsaDataWrapper.class);
					assert edcWsaDataWrapper != null;
					mConfigs = edcWsaDataWrapper.getEdcWsaConfigs();
					mToken = token;
					boolean isFunction = edcWsaDataWrapper.isWsaFunctionRunning();
					if (!isFunction)
						Utils.showToast(AppUtils.getFunctionPauseMsg(M.FunctionCode.FUNCTION_132_MEMBER_PAY_WATER_BILL), Snackbar.LENGTH_LONG);
					mLatestBill = edcWsaDataWrapper.getWsaBill();
					if (mLatestBill != null) {
						onSelectType(mSparseArray.indexOfKey(mLatestBill.getType()));
						mView.enablePhone(true);
						mView.setPhone(Utils.getRealPhone(mLatestBill.getPhone()));
					} else {
						onSelectType(mSparseArray.indexOfKey(1));
					}
					if (Utils.isEmptyList(mConfigs)) loadFailure(0, "", "");
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
					e.printStackTrace();
				}
			}

			@Override
			public void loadFailure(int code, String result, String token) {
				Utils.showToast(R.string.fail);
			}
		}, false);
	}

	@Override
	public void submitWsa(final int type, final String number, String phone, String amount, String fee, final String remark, final List<Image> list) {
		if (type <= 0) {
			Utils.showToast(R.string.please_select_type);
			return;
		}
		if (TextUtils.isEmpty(number)) {
			Utils.showToast(type == 1 ? R.string.invalid_bill_no : R.string.invalid_client_id);
			return;
		}

		phone = Utils.getRealPhone(phone);
		if (TextUtils.isEmpty(phone)) {
			Utils.showToast(R.string.invalid_phone);
			return;
		}

		if (amount != null) amount = Utils.dealCurrency(amount);
		if (Utils.checkEmptyAmount(amount)) {
			Utils.showToast(R.string.invalid_amount);
			return;
		}

		if (fee != null) fee = Utils.dealCurrency(fee.replace(",", ""));
		if (Utils.checkEmptyAmount(fee)) {
			Utils.showToast(R.string.invalid_fee);
			return;
		}

		if (!AppUtils.checkEnoughMoney(AppGlobal.KHR, amount, fee)) {
			Utils.showToast(R.string.msg_1709);
			return;
		}

		if (Utils.isEmptyList(list)) {
			Utils.showToast(R.string.need_attachment);
			return;
		}
		PayHelper payHelper = new PayHelper((AppCompatActivity) mContext);
		final String finalPhone = phone;
		final String finalAmount = amount;
		final String finalFee = fee;
		payHelper.startPay(new PayHelper.CallBackPart() {
			@Override
			public void paySuccess() {
				try {
					final UploadRequest uploadRequest = new UploadRequest();
					final String url = BuildConfig.FILE_BASE_URL + "web/?_b=aj&_a=images&cmd=uploadImage&type=payment/wsa";
					final String[] tempPath = new String[1];
					AppUtils.compressImages2(list, new ICompressListener.SimpleCompressListener() {
						@Override
						public void onStart(String path) {
							tempPath[0] = path;
							mView.enablePbCompress(true);
						}

						@Override
						public void onError() {
							mView.enablePbCompress(false);
							Utils.showToast(R.string.fail);
						}

						@Override
						public void onFinish(Map<String, File> map) {
							mView.enablePbCompress(false);
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
														Map<String, String> param = new HashMap<>();
														param.put("_s", Session.sSid);
														param.put("_a", "payment");
														param.put("_b", "aj");
														param.put("cmd", "saveWaterSupply");
														param.put("type", String.valueOf(type));
														param.put("bill_number", number);
														param.put("customer_phone", finalPhone);
														param.put("image", uploadResult.getFile0());
														param.put("amount", finalAmount);
														param.put("fee", String.valueOf(finalFee));
														param.put("applicant_remark", remark);
														param.put("unique_token", mToken);
														submit(param, new SimpleRequestListener() {
															@Override
															public void loadSuccess(String result, String token) {
																try {
																	mToken = token;
																	SingleStringBean bean = JsonUtil.jsonToBean(result, SingleStringBean.class);
																	assert bean != null;
																	String billId = bean.getValue();
																	mView.toPaymentHistoryDetailActivity(Integer.parseInt(billId));
																} catch (Exception e) {
																	Utils.showToast(R.string.fail);
																	FileUtils.addErrorLog(e);
																}
															}

															@Override
															public void loadFailure(int code, String result, String token) {
																mToken = token;
																if (code == com.og.M.MessageCode.ERR_505_FUNCTION_NOT_RUNNING) {
																	mView.enableBtnSubmit(false);
																	Utils.showToast(AppUtils.getFunctionPauseMsg(M.FunctionCode.FUNCTION_132_MEMBER_PAY_WATER_BILL), Snackbar.LENGTH_LONG);
																} else {
																	String msg = M.get(mContext, code);
																	if (TextUtils.isEmpty(msg)) msg = mContext.getResources()
																		.getString(R.string.fail);
																	Utils.showToast(msg);
																}
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
												Utils.showToast(R.string.fail);
												FileUtils.delete(tempPath[0]);
												throwable.printStackTrace();
											}
										});
									}
								});
							mView.resetInterface();
						}
					});
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}
		});
	}

	@Override
	public String[] getWsaTypeArr() {
		return mWsaTypeArr;
	}

	@Override
	public void onSelectType(int position) {
		int type = mSparseArray.keyAt(position);
		mView.setNumberTitle(Utils.getString(type == 1 ? R.string.bill_no : R.string.client_id));
		mView.setSelectType(type);
		mView.setSelectTypeText(mWsaTypeArr[position]);
		mView.setSelectTypeTextColor(Utils.getColor(R.color.black));
	}
}
