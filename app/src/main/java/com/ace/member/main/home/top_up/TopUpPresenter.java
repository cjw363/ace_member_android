package com.ace.member.main.home.top_up;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.bean.FaceValue;
import com.ace.member.bean.PhoneCompany;
import com.ace.member.bean.SingleIntBean;
import com.ace.member.bean.TopUpDataWrapper;
import com.ace.member.event.SelectPhoneCompanyEvent;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.M;
import com.ace.member.utils.PayHelper;
import com.ace.member.utils.SPUtil;
import com.ace.member.utils.Session;
import com.google.gson.reflect.TypeToken;
import com.og.LibSession;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

final class TopUpPresenter extends BasePresenter implements TopUpContract.Presenter {

	private final TopUpContract.View mView;
	private String mToken;

	@Inject
	public TopUpPresenter(Context context, TopUpContract.View view) {
		super(context);
		mView = view;
	}

	@Override
	public void start() {
		try {
			if (TextUtils.isEmpty(mView.getCountryCode())) {
				String phone = Session.user.getPhone();
				mView.setCountryCode(phone.substring(1, phone.indexOf("-")));
			}

			PhoneCompany phoneCompany = SPUtil.getObject("current_phone_company", PhoneCompany.class);
			if (phoneCompany == null) {
				mView.enableEmptyTopUpCompany(true);
				mView.enableEmptyFaceValue(true);
				mView.setCurrentTopUpCompany(null);
			} else {
				getPhoneCompanyWithFaceValueByCompanyID(phoneCompany.getId());
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@Override
	public void onSelectPhoneCompanyEvent(SelectPhoneCompanyEvent event) {
		if (event != null && event.getPhoneCompany() != null) {
			mView.enableEmptyTopUpCompany(false);
			SPUtil.putObject("current_phone_company", event.getPhoneCompany());
			getPhoneCompanyWithFaceValueByCompanyID(event.getPhoneCompany().getId());
		}
	}

	@Override
	public void topUpSubmit() {
		try {
			final int topUpWay = mView.getTopUpWay();
			final String countryCode = mView.getCountryCode();
			final String phone = Utils.getRealPhone(mView.getPhone());
			final FaceValue faceValue = mView.getFaceValue();
			final PhoneCompany phoneCompany = mView.getPhoneCompany();
			if (TextUtils.isEmpty(mToken)) return;
			if (topUpWay != AppGlobal.TOP_UP_1_SHOW_PINCODE) {
				if (TextUtils.isEmpty(countryCode)) {
					Utils.showToast(R.string.invalid_country_code);
					return;
				}
				if (TextUtils.isEmpty(phone)) {
					Utils.showToast(R.string.invalid_phone);
					return;
				}
			}
			if (phoneCompany == null) {
				Utils.showToast(R.string.no_phone_company);
				return;
			}

			final String currency = phoneCompany.getCurrency();

			if (faceValue == null) {
				Utils.showToast(R.string.please_select_face_value);
				return;
			}

			final double price = faceValue.getFaceValue() * (1 - phoneCompany.getMemberDiscount() * 0.01);
			if (!AppUtils.checkEnoughMoney(currency, price)) {
				Utils.showToast(String.format(Utils.getString(R.string.msg_1709), currency));
				return;
			}
			PayHelper payHelper = new PayHelper(mView.getActivity());
			payHelper.startPay(new PayHelper.CallBackPart() {
				@Override
				public void paySuccess() {
					final boolean sms = (topUpWay != AppGlobal.TOP_UP_1_SHOW_PINCODE);
					Map<String, String> params = new HashMap<>();
					params.put("_a", "user");
					params.put("_b", "aj");
					params.put("_s", LibSession.sSid);
					params.put("cmd", "topUp");
					params.put("unique_token", mToken);
					params.put("type", String.valueOf(topUpWay));
					if (sms) {
						params.put("phone", "+" + countryCode + "-" + phone);
					}
					params.put("company_id", String.valueOf(phoneCompany.getId()));
					params.put("face_value", String.valueOf(faceValue.getFaceValue()));
					params.put("price", String.valueOf(price));
					params.put("currency", currency);
					submit(params, new SimpleRequestListener() {
						@Override
						public void loadSuccess(String result, String token) {
							try {
								mToken = token;
								SingleIntBean bean = JsonUtil.jsonToBean(result, SingleIntBean.class);
								assert bean != null;
								int orderId = bean.getValue();
								start();
								if (orderId > 0) mView.toOrderDetail(orderId, sms);
								else payFail();

							} catch (Exception e) {
								payFail();
								FileUtils.addErrorLog(e);
							}
						}

						@Override
						public void loadFailure(int errorCode, String result, String uniqueToken) {
							try {
								mToken = uniqueToken;
								if (errorCode == com.og.M.MessageCode.ERR_505_FUNCTION_NOT_RUNNING) {
									if (topUpWay == AppGlobal.TOP_UP_1_SHOW_PINCODE) {
										Utils.showToast(AppUtils.getFunctionPauseMsg(M.FunctionCode.FUNCTION_121_MEMBER_TOP_UP_SHOW_PIN_CODE));
									} else {
										Utils.showToast(AppUtils.getFunctionPauseMsg(M.FunctionCode.FUNCTION_122_MEMBER_TOP_UP_SEND_SMS));
									}
									mView.setSubmitEnables(false);
								} else {
									String msg = M.get(mContext, errorCode);
									if (TextUtils.isEmpty(msg))
										msg = mContext.getResources().getString(R.string.fail);
									Utils.showToast(msg);
								}
							} catch (Resources.NotFoundException e) {
								FileUtils.addErrorLog(e);
							}
						}
					});
				}

				@Override
				public void payFail() {
					Utils.showToast(R.string.fail);
				}
			});
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	public void getPhoneCompanyWithFaceValueByCompanyID(int companyID) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "user");
		params.put("_b", "aj");
		params.put("company_id", String.valueOf(companyID));
		params.put("_s", LibSession.sSid);
		params.put("cmd", "getTopUpDataByCompanyID");
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					mToken = token;
					TopUpDataWrapper topUpDataWrapper = JsonUtil.jsonToBean(result, TopUpDataWrapper.class);
					if (topUpDataWrapper == null) {
						fail();
						return;
					}

					PhoneCompany phoneCompany = topUpDataWrapper.getPhoneCompany();
					if (phoneCompany == null) {
						fail();
						return;
					}

					List<FaceValue> faceValueList = topUpDataWrapper.getFaceValueList();
					boolean isShowPincode = topUpDataWrapper.isFunctionShowPinCode();
					boolean isSendSMS = topUpDataWrapper.isFunctionSendSMS();

					mView.setIsFunctionShowPincode(isShowPincode);
					mView.setIsFunctionSendSMS(isSendSMS);

					mView.enableEmptyTopUpCompany(false);
					mView.enableEmptyFaceValue(false);
					mView.setCurrentTopUpCompany(phoneCompany);
					mView.setFaceValueList(faceValueList);

					int topUpWay = mView.getTopUpWay();
					if (!isShowPincode && topUpWay == AppGlobal.TOP_UP_1_SHOW_PINCODE)
						mView.showFunctionPause(M.FunctionCode.FUNCTION_121_MEMBER_TOP_UP_SHOW_PIN_CODE);
					if (!isSendSMS && topUpWay == AppGlobal.TOP_UP_3_SEND_SMS)
						mView.showFunctionPause(M.FunctionCode.FUNCTION_122_MEMBER_TOP_UP_SEND_SMS);
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}

			@Override
			public void loadFailure(int code, String result, String token) {
				mToken = token;
				fail();
			}
		}, false);
	}

	public void getPhoneCompany() {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "user");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("cmd", "getPhoneCompanyList");
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					List<PhoneCompany> list = JsonUtil.jsonToList(result, new TypeToken<List<PhoneCompany>>() {});
					mView.setPhoneCompanyList(list);
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}

			@Override
			public void loadFailure(int code, String result, String token) {
				mView.setPhoneCompanyList(new ArrayList<PhoneCompany>());
			}
		});
	}

	private void fail() {
		mView.enableEmptyTopUpCompany(true);
		mView.enableEmptyFaceValue(true);
	}

}
