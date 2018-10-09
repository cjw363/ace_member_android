package com.ace.member.main.home.transfer.to_member;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.bean.InfoForTransferToMember;
import com.ace.member.bean.SingleStringBean;
import com.ace.member.bean.TransferLimitConfig;
import com.ace.member.bean.User;
import com.ace.member.bean.UserCommunication;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.M;
import com.ace.member.utils.PayHelper;
import com.ace.member.utils.Session;
import com.og.LibSession;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;


public class ToMemberPresenter extends BasePresenter implements ToMemberContract.Presenter {

	private final ToMemberContract.View mView;

	private String mToken = "";
	private static final String mTokenAction = "TRANSFER_TO_MEMBER";

	@Inject
	public ToMemberPresenter(Context context, ToMemberContract.View mView) {
		super(context);
		this.mView = mView;
	}

	public void checkIsMember(String phone) {
		Map<String, String> params = new HashMap<>();
		params.put("_b", "aj");
		params.put("_s", Session.sSid);
		params.put("_a", "user");
		params.put("cmd", "checkIsMember");
		params.put("member_phone", phone);
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					SingleStringBean bean = JsonUtil.jsonToBean(result, SingleStringBean.class);
					assert bean != null;
					mView.showName(bean.getValue());
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
					e.printStackTrace();
				}
			}

			@Override
			public void loadFailure(int errorCode, String result, String token) {
				Utils.showToast(M.get(mContext, errorCode));
				mView.hideName();
			}
		});
	}

	public void getMemberByID(int memberID) {
		Map<String, String> params = new HashMap<>();
		params.put("_b", "aj");
		params.put("_s", Session.sSid);
		params.put("_a", "user");
		params.put("cmd", "getMemberByID");
		params.put("member_id", memberID + "");
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					User member = JsonUtil.jsonToBean(result, User.class);
					assert member != null;
					mView.showName(member.getName());
					mView.showTargetPhone(member.getPhone());
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
					e.printStackTrace();
				}
			}

			@Override
			public void loadFailure(int errorCode, String result, String token) {
				Utils.showToast(M.get(mContext, errorCode));
				mView.hideName();
			}
		});
	}

	@Override
	public void submit(final String countryCode, final String targetPhone, final String currency) {

		final double a = TextUtils.isEmpty(mView.getAmount()) ? 0 : Double.parseDouble(mView.getAmount().replace(",", ""));
		if (a <= 0) {
			Utils.showToast(R.string.invalid_amount);
			return;
		}

		Map<String, String> params = new HashMap<>();
		params.put("_a", "user");
		params.put("_b", "aj");
		params.put("_s", Session.sSid);
		params.put("cmd", "GetMemberTransferLimit");
		params.put("currency", currency);
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				try {
					TransferLimitConfig limitConfig = JsonUtil.jsonToBean(result, TransferLimitConfig.class);
					assert limitConfig != null;
					if (a > limitConfig.getmaxTransferPerTime()) {
						Utils.showToast(Utils.getString(R.string.over_one_time_limit) + " (" + Utils.format(limitConfig.getmaxTransferPerTime(), currency) + " " + currency + ")", Snackbar.LENGTH_LONG, null);
						return;
					}
					if (a + limitConfig.getDayAmount() > limitConfig.getmaxTransferPerDay()) {
						Utils.showToast(Utils.getString(R.string.over_daily_limit) + " (" + Utils.format(limitConfig.getmaxTransferPerDay(), currency) + " " + currency + ")", Snackbar.LENGTH_LONG, null);
						return;
					}

					PayHelper payHelper = new PayHelper(mView.getActivity());
					payHelper.startPay(new PayHelper.CallBackPart() {
						@Override
						public void paySuccess() {
							final Map<String, String> params = new HashMap<>();
							params.put("_b", "aj");
							params.put("_s", Session.sSid);
							params.put("_a", "transfer");
							params.put("cmd", "saveToMember");
							params.put("amount", mView.getAmount());
							params.put("target_country_code", countryCode);
							params.put("target_phone", targetPhone);
							params.put("currency", currency);
							params.put("remark", mView.getRemark());
							params.put("unique_token", mToken);
							submit(params, new SimpleRequestListener() {
								@Override
								public void loadSuccess(String result, String token) {
									try {
										mToken = token;
										sendSocket();
										ToMemberData bean = JsonUtil.jsonToBean(result, ToMemberData.class);
										assert bean != null;
										String time = bean.getTime();
										int transferID = bean.getTransferID();
										mView.showSuccess(time, transferID);

									} catch (Exception e) {
										FileUtils.addErrorLog(e);
										e.printStackTrace();
									}
								}

								@Override
								public void loadFailure(int code, String result, String token) {
									try {
										mToken = token;
										if (code == com.og.M.MessageCode.ERR_505_FUNCTION_NOT_RUNNING) {
											Utils.showToast(AppUtils.getFunctionPauseMsg(M.FunctionCode.FUNCTION_113_MEMBER_TRANSFER_TO_MEMBER));
											mView.setSubmitEnables(false);
										} else {
											String msg = M.get(mContext, code);
											if (TextUtils.isEmpty(msg)) msg = mContext.getResources()
												.getString(R.string.fail);
											Utils.showToast(msg);
										}
									} catch (Exception e) {
										FileUtils.addErrorLog(e);
									}

								}
							});
						}

					});
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}

			}

			@Override
			public void loadFailure(int errorCode, String result, String uniqueToken) {
				Utils.showToast(M.get(mContext, errorCode));
			}
		});
	}

	private void sendSocket() {
		try {
			UserCommunication comm = new UserCommunication();
			comm.setSource(AppGlobal.USER_TYPE_1_MEMBER + "+" + Session.user.getId());
			comm.setTarget(mView.getTargetMember());
			String commList = JsonUtil.beanToJson(comm);
			mView.setSendMessage(commList);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	public void getInfoForTransferToMember() {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "user");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("cmd", "getInfoForTransferToMember");
		params.put("token_action", mTokenAction);
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					mToken = token;
					InfoForTransferToMember info = JsonUtil.jsonToBean(result, InfoForTransferToMember.class);
					assert info != null;
					boolean functionTransferRunning = info.isFunctionToMemberRunning();
					if (!functionTransferRunning)
						mView.banTransfer(AppUtils.getFunctionPauseMsg(M.FunctionCode.FUNCTION_113_MEMBER_TRANSFER_TO_MEMBER));
					Session.balanceList = info.getBalance();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void loadFailure(int code, String result, String token) {
				Utils.showToast(M.get(mContext, code));
			}
		}, false);
	}


}
