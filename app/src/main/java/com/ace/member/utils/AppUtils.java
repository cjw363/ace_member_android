package com.ace.member.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.SparseArray;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.ace.member.R;
import com.ace.member.bean.Balance;
import com.ace.member.bean.Currency;
import com.ace.member.listener.ICompressListener;
import com.ace.member.main.image_detail.Image;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AppUtils {
	private static MediaScannerConnection sConnection;

	public static String getTopUpWay(int topUpWay) {
		String s = "";
		switch (topUpWay) {
			case AppGlobal.TOP_UP_1_SHOW_PINCODE:
				s = Utils.getString(R.string.show_pincode);
				break;
			case AppGlobal.TOP_UP_2_DIRECTLY_TOP_UP:
				s = Utils.getString(R.string.directly_top_up);
				break;
			case AppGlobal.TOP_UP_3_SEND_SMS:
				s = Utils.getString(R.string.send_sms);
				break;
		}
		return s;
	}

	public static void enableSoftInput(Activity activity, boolean enable) {
		if (activity == null || activity.getWindow() == null) return;
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm == null) return;
		if (enable) {
			imm.showSoftInput(activity.getWindow()
					.getDecorView(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
		} else {
			imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
		}
	}

	public static void enableSoftInput(View view, boolean enable) {
		if (view == null) return;
		InputMethodManager imm = (InputMethodManager) view.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm == null) return;
		if (enable) imm.showSoftInput(view, InputMethodManager.RESULT_UNCHANGED_SHOWN);
		else imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	public static String getMsgByTradingPasswordStatus(int status) {
		String s;
		switch (status) {
			case AppGlobal.TRADING_PASSWORD_STATUS_3_INACTIVE:
				s = Utils.getString(R.string.inactive_trading_password);
				break;
			case AppGlobal.TRADING_PASSWORD_STATUS_4_TO_SET:
				s = Utils.getString(R.string.please_set_trading_password);
				break;
			case AppGlobal.TRADING_PASSWORD_STATUS_6_TO_VERIFY_CERTIFICATE:
				s = Utils.getString(R.string.finish_trading_password_setting);
				break;
			default:
				s = Utils.getString(R.string.please_set_trading_password);
				break;
		}
		return s;
	}

	public static String getCountryNameByCode(int code) {
		String countryName = "";
		switch (code) {
			case AppGlobal.COUNTRY_CODE_84_VIETNAM:
				countryName = Utils.getString(R.string.vietnam);
				break;
			case AppGlobal.COUNTRY_CODE_855_CAMBODIA:
				countryName = Utils.getString(R.string.cambodia);
				break;
			case AppGlobal.COUNTRY_CODE_86_CHINA:
				countryName = Utils.getString(R.string.china);
				break;
			case AppGlobal.COUNTRY_CODE_66_THAILAND:
				countryName = Utils.getString(R.string.thailand);
				break;
		}
		return countryName;
	}

	public static SpannableString getCallSpan(final Activity activity, final String text) {
		if (activity == null || TextUtils.isEmpty(text)) return null;
		SpannableString spanStr = null;
		try {
			spanStr = new SpannableString(text);
			spanStr.setSpan(new UnderlineSpan(), 0, text.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
			spanStr.setSpan(new ClickableSpan() {
				@Override
				public void onClick(View widget) {
					Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + text.replaceAll("#", " %23")));
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					activity.startActivity(intent);
				}
			}, 0, text.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
			spanStr.setSpan(new ForegroundColorSpan(Utils.getColor(R.color.colorPrimary)), 0, text.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return spanStr;
	}

	public static void scanFile(final Context context, final String filePath, final String mimeType) {
		try {
			sConnection = new MediaScannerConnection(context, new MediaScannerConnection.MediaScannerConnectionClient() {
				@Override
				public void onMediaScannerConnected() {
					sConnection.scanFile(filePath, mimeType);
				}

				@Override
				public void onScanCompleted(String path, Uri uri) {
					sConnection.disconnect();
					sConnection = null;
				}
			});
			sConnection.connect();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	public static void compressImages(final List<String> list, final ICompressListener listener) {
		if (Utils.isEmptyList(list, true)) return;
		ThreadPoolUtil.getInstance().execute(new Runnable() {
			@Override
			public void run() {
				final String tempPath = FileUtils.getTempPath();
				final Map<String, File> map = new TreeMap<>();
				if (!TextUtils.isEmpty(tempPath)) {
					Utils.runOnUIThread(new Runnable() {
						@Override
						public void run() {
							listener.onStart(tempPath);
						}
					});
					ExecutorService executorService = Executors.newFixedThreadPool(4);
					String fileExtName;
					try {
						for (int i = 0; i < list.size(); i++) {
							final int finalI = i;
							Utils.runOnUIThread(new Runnable() {
								@Override
								public void run() {
									listener.onCompressing(finalI + 1, list.size());
								}
							});
							final String index = String.valueOf(i);
							final String filePath = list.get(i);
							fileExtName = filePath.substring(filePath.lastIndexOf(".") + 1).toLowerCase();
							//如果是gif
							if (fileExtName.equals(M.ImagePicker.TYPE_GIF_LOW)) {
								File gifFile = new File(filePath);
								map.put(index, gifFile);
								continue;
							}

							//如果大小小于100k
							File file = new File(filePath);
							if (file.length() <= 102400) {
								map.put(index, file);
								continue;
							}

							final Bitmap.CompressFormat compressFormat;
							fileExtName = "jpeg";
							compressFormat = Bitmap.CompressFormat.JPEG;
							final String fileName = index + "." + fileExtName;
							executorService.execute(new Runnable() {
								@Override
								public void run() {
									BitmapUtil.scaleBitmapAndSave(filePath, tempPath + fileName, compressFormat, 720, 1280, 50);
									map.put(index, new File(tempPath + fileName));
								}
							});
						}
						executorService.shutdown();
						executorService.awaitTermination(30, TimeUnit.SECONDS);
						Utils.runOnUIThread(new Runnable() {
							@Override
							public void run() {
								listener.onFinish(map);
							}
						});
					} catch (InterruptedException e) {
						e.printStackTrace();
						FileUtils.addErrorLog(e);
						Utils.runOnUIThread(new Runnable() {
							@Override
							public void run() {
								listener.onError();
							}
						});
					} finally {
						executorService = null;
					}
				} else {
					try {
						listener.onStart(tempPath);
						for (int i = 0, n = list.size(); i < n; i++) {
							listener.onCompressing(i + 1, n);
							map.put(String.valueOf(i), new File(list.get(i)));
						}
						listener.onFinish(map);
					} catch (Exception e) {
						FileUtils.addErrorLog(e);
					}
				}
			}
		});
	}

	public static void compressImages2(final List<Image> list, final ICompressListener listener) {
		compressImages(dealImageList2(list), listener);
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	public static void checkFilePath(String path) {
		File file = new File(path);
		if (file.isDirectory() && !file.exists()) {
			file.mkdirs();
		} else {
			if (!file.getParentFile().exists()) {
				file.mkdirs();
			}
		}
	}

	public static double getBalance(String currency) {
		if (TextUtils.isEmpty(currency)) return 0;
		List<Balance> list = Session.balanceList;
		if (list != null) {
			for (Balance balance : list) {
				if (balance != null && balance.getCurrency().equals(currency)) {
					try {
						return Double.parseDouble(String.valueOf(balance.getAmount()).replace(",", ""));
					} catch (Exception e) {
						return 0;
					}
				}
			}
		}
		return 0;
	}

	public static List<String> dealImageList(List<Image> list) {
		List<String> strings = null;
		try {
			strings = new ArrayList<>();
			if (Utils.isEmptyList(list)) return strings;
			for (int i = 0, n = list.size(); i < n; i++) {
				strings.add(list.get(i).getUri());
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return strings;
	}

	public static List<String> dealImageList2(List<Image> list) {
		List<String> strings = null;
		try {
			strings = new ArrayList<>();
			if (Utils.isEmptyList(list)) return strings;
			for (int i = 0, n = list.size(); i < n; i++) {
				strings.add(list.get(i).getDate());
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return strings;
	}

	public static SparseArray<String> getEdcWsaTypeArr(int type) {
		SparseArray<String> sparseArray = new SparseArray<>();
		if (type == AppGlobal.PAYMENT_TYPE_1_EDC) {
			sparseArray.put(1, Utils.getString(R.string.edc_type_1_phnom_penh_kandal));
			sparseArray.put(2, Utils.getString(R.string.edc_type_2_battambang));
			sparseArray.put(3, Utils.getString(R.string.edc_type_3_siem_reap));
			sparseArray.put(4, Utils.getString(R.string.edc_type_4_sihanoukville));
			sparseArray.put(5, Utils.getString(R.string.edc_type_5_ang_ta_som));
			sparseArray.put(6, Utils.getString(R.string.edc_type_6_banteay_meanchey));
			sparseArray.put(7, Utils.getString(R.string.edc_type_7_bavat));
			sparseArray.put(8, Utils.getString(R.string.edc_type_8_chipoul));
			sparseArray.put(9, Utils.getString(R.string.edc_type_9_kampong_cham));
			sparseArray.put(10, Utils.getString(R.string.edc_type_10_kampong_rou));
			sparseArray.put(11, Utils.getString(R.string.edc_type_11_kampong_speu));
			sparseArray.put(12, Utils.getString(R.string.edc_type_12_kampong_sralao));
			sparseArray.put(13, Utils.getString(R.string.edc_type_13_kao_seyma));
			sparseArray.put(14, Utils.getString(R.string.edc_type_14_kratie));
			sparseArray.put(15, Utils.getString(R.string.edc_type_15_memot));
			sparseArray.put(16, Utils.getString(R.string.edc_type_16_mongkul_borey));
			sparseArray.put(17, Utils.getString(R.string.edc_type_17_ponhea_krek));
			sparseArray.put(18, Utils.getString(R.string.edc_type_18_prey_veng));
			sparseArray.put(19, Utils.getString(R.string.edc_type_19_rattanakiri));
			sparseArray.put(20, Utils.getString(R.string.edc_type_20_senmonorom));
			sparseArray.put(21, Utils.getString(R.string.edc_type_21_snuol));
			sparseArray.put(22, Utils.getString(R.string.edc_type_22_steung_treng));
			sparseArray.put(23, Utils.getString(R.string.edc_type_23_svay_rieng));
			sparseArray.put(24, Utils.getString(R.string.edc_type_24_takeo));
		} else {
			sparseArray.put(1, Utils.getString(R.string.wsa_type_1_ppwsa));
			sparseArray.put(2, Utils.getString(R.string.wsa_type_2_srwsa));
		}
		return sparseArray;
	}

	public static String[] getEdcWsaTypeArr2(int type) {
		SparseArray<String> sparseArray = getEdcWsaTypeArr(type);
		int size = sparseArray.size();
		String[] arr = new String[size];
		for (int i = 0; i < size; i++) {
			arr[i] = sparseArray.get(sparseArray.keyAt(i));
		}
		return arr;
	}

	public static boolean checkEnoughMoney(String currency, double money) {
		if (TextUtils.isEmpty(currency) || Utils.isEmptyList(Session.balanceList)) return false;
		try {
			for (Balance balance : Session.balanceList) {
				if (balance.getCurrency().toUpperCase().equals(currency.toUpperCase())) {
					return money <= Double.parseDouble(String.valueOf(balance.getAmount()).replace(",", ""));
				}
			}
		} catch (NumberFormatException e) {
			FileUtils.addErrorLog(e);
		}
		return false;
	}

	public static boolean checkEnoughMoney(String currency, Double... moneys) {
		double total = 0;
		for (Double money : moneys) total += (money == null ? 0 : money);
		return checkEnoughMoney(currency, total);
	}

	public static boolean checkEnoughMoney(String currency, String money) {
		return checkEnoughMoney(currency, TextUtils.isEmpty(money) ? 0 : Double.parseDouble(money.replace(",", "")));
	}

	public static boolean checkEnoughMoney(String currency, String... moneys) {
		double total = 0;
		try {
			for (String money : moneys) {
				total += (TextUtils.isEmpty(money) ? 0 : Double.parseDouble(money.replace(",", "")));
			}
		} catch (NumberFormatException e) {
			FileUtils.addErrorLog(e);
		}
		return checkEnoughMoney(currency, total);
	}

	public static int getStatus(int status) {
		if (status == AppGlobal.STATUS_1_PENDING) return R.string.pending;
		if (status == AppGlobal.STATUS_3_COMPLETED) return R.string.completed;
		if (status == AppGlobal.STATUS_4_CANCELLED) return R.string.cancelled;
		return R.string.error;
	}

	public static int getYesOrNo(int flag) {
		int id;
		switch (flag) {
			case AppGlobal.FLAG_YES:
				id = R.string.yes;
				break;
			case AppGlobal.FLAG_NO:
				id = R.string.no;
				break;
			default:
				id = R.string.no;
				break;
		}
		return id;
	}

	/**
	 * 提取用户自己记录的remark
	 *
	 * @param remark 后台获取的remark
	 * @return 用户自己的remark
	 */
	public static String getUserRemark(String remark) {
		String userRemark = "";
		if (!TextUtils.isEmpty(remark)) {
			int index = remark.indexOf("(Remark:");
			if (index >= 0) {
				userRemark = remark.substring(index + 8, remark.length() - 1);
			}
		}
		return userRemark;
	}

	public static int getIDCertificateStatusDrawable(int status) {
		if (status == AppGlobal.CERTIFICATE_STATUS_1_PENDING) return R.drawable.ic_certificate_pending;
		if (status == AppGlobal.CERTIFICATE_STATUS_2_ACCEPTED)
			return R.drawable.ic_certificate_accepted;
		if (status == AppGlobal.CERTIFICATE_STATUS_4_REJECTED) return R.drawable.ic_certificate_reject;
		return 0;
	}

	public static String getIDCertificateStatusDes(int status) {
		if (status == AppGlobal.CERTIFICATE_STATUS_1_PENDING)
			return Utils.getString(R.string.certification_is_pending);
		if (status == AppGlobal.CERTIFICATE_STATUS_2_ACCEPTED)
			return Utils.getString(R.string.certification_is_accepted);
		if (status == AppGlobal.CERTIFICATE_STATUS_4_REJECTED)
			return Utils.getString(R.string.certification_is_rejected);
		return "";
	}

	/**
	 * KHR,VND 会取整, USD,THB 保留两位小数
	 */
	public static String simplifyAmount(String currency, String amount) {
		try {
			if (null == currency) return Utils.format(amount, 2);
			currency = currency.toUpperCase();
			if (AppGlobal.USD.equals(currency) || AppGlobal.THB.equals(currency)) {
				return Utils.format(amount, 2);
			}
			return Utils.format(amount, 0);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}

		return amount;
	}

	/**
	 * 返回格式: 11 USD
	 */
	public static String getAmountCurrency(String currency, String amount) {
		String formatAmount = simplifyAmount(currency, amount);
		return formatAmount + " " + currency;
	}

	/**
	 * 提取Amount的值
	 * 99,999.00 => 99999.00
	 * KHR,VND 会取整, USD,THB 保留两位小数
	 */
	public static String getAmountValue(String currency, String amount) {
		if (null != currency) {
			currency = currency.toUpperCase();
			if (AppGlobal.KHR.equals(currency) || AppGlobal.VND.equals(currency)) {
				//				amount = removeDecimal(amount);
				amount = Utils.format(amount, 0);
			}
		}
		return amount.replace(",", "");
	}

	/**
	 * 去除小数
	 */
	private static String removeDecimal(String str) {
		try {
			str = Utils.format(str, 10);
			String[] str1 = str.split("\\.");
			return Utils.format(str1[0]);
		} catch (Exception e) {
			e.printStackTrace();
			FileUtils.addErrorLog(e);
		}
		return "0";
	}

	//获取转换货币的汇率   currency1->currency2
	public static double getExchangeRate(String currency1, String currency2) {
		double rate = 0;
		try {
			double exchange1 = getExchangeByCurrency(currency1);
			double exchange2 = getExchangeByCurrency(currency2);
			rate = exchange2 / exchange1;
		} catch (Exception e) {
			e.printStackTrace();
			FileUtils.addErrorLog(e);
		}
		return rate;
	}

	//获取exchange 建议必要时先更新Session.exchangeList值
	public static double getExchangeByCurrency(String currency) {
		double exchange = 0;
		try {
			for (Currency curObj : Session.currencyList) {
				if (curObj.getCurrency().trim().equals(currency.trim())) {
					exchange = curObj.getExchange();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			FileUtils.addErrorLog(e);
		}
		return exchange;
	}

	//获取转换货币的汇率   currency1->currency2
	public static double getExchangeFeeRate(String currency1, String currency2) {
		double rate = 0;
		try {
			double exchange1 = getExchangeFeeByCurrency(currency1);
			double exchange2 = getExchangeFeeByCurrency(currency2);
			rate = exchange2 / exchange1;
		} catch (Exception e) {
			e.printStackTrace();
			FileUtils.addErrorLog(e);
		}
		return rate;
	}

	//获取exchangeFee 建议必要时先更新Session.exchangeList值
	public static double getExchangeFeeByCurrency(String currency) {
		double exchange = 0;
		try {
			for (Currency curObj : Session.currencyList) {
				if (curObj.getCurrency().trim().equals(currency.trim())) {
					exchange = curObj.getExchangeFee();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			FileUtils.addErrorLog(e);
		}
		return exchange;
	}

	public static String getSex(int sex) {
		if (sex == AppGlobal.SEX_DEFAULT) return Utils.getString(R.string.sex);
		if (sex == AppGlobal.SEX_MALE) return Utils.getString(R.string.male);
		if (sex == AppGlobal.SEX_FEMALE) return Utils.getString(R.string.female);
		return null;
	}

	public static String getNationality(int nationality) {
		if (nationality == AppGlobal.COUNTRY_CODE_855_CAMBODIA)
			return Utils.getString(R.string.nation_cambodians);
		if (nationality == AppGlobal.COUNTRY_CODE_84_VIETNAM)
			return Utils.getString(R.string.nation_vietnamese);
		if (nationality == AppGlobal.COUNTRY_CODE_86_CHINA)
			return Utils.getString(R.string.nation_chinese);
		if (nationality == AppGlobal.COUNTRY_CODE_66_THAILAND)
			return Utils.getString(R.string.nation_thais);
		if (nationality == AppGlobal.COUNTRY_CODE_60_MALAYSIA)
			return Utils.getString(R.string.nation_malaysians);
		if (nationality == AppGlobal.COUNTRY_CODE_62_INDONESIA)
			return Utils.getString(R.string.nation_indonesians);
		if (nationality == AppGlobal.COUNTRY_CODE_33_FRENCH)
			return Utils.getString(R.string.nation_french_citizens);
		return null;
	}

	public static String getIDCertificateType(int certificateType) {
		if (certificateType == 1) return Utils.getString(R.string.certificate_type_id);
		if (certificateType == 2) return Utils.getString(R.string.passport);
		if (certificateType == 3) return Utils.getString(R.string.drive_license);
		if (certificateType == 4) return Utils.getString(R.string.family_book);
		return null;
	}

	public static String getUserInputType(int userInputType) {
		if (userInputType == AppGlobal.USER_INPUT_TYPE_1_BY_HAND)
			return Utils.getString(R.string.by_hand);
		if (userInputType == AppGlobal.USER_INPUT_TYPE_2_QR_CODE)
			return Utils.getString(R.string.qr_code);
		return Utils.getString(R.string.error);
	}

	public static String getUserType(int userType) {
		if (userType == AppGlobal.USER_TYPE_1_MEMBER)
			return String.format(Utils.getString(R.string.bracket), Utils.getString(R.string.member));
		if (userType == AppGlobal.USER_TYPE_2_AGENT)
			return String.format(Utils.getString(R.string.bracket), Utils.getString(R.string.agent));
		if (userType == AppGlobal.USER_TYPE_7_BRANCH)
			return String.format(Utils.getString(R.string.bracket), Utils.getString(R.string.branch));
		return Utils.getString(R.string.error);
	}

	public static String getFunctionPauseMsg(int functionCode) {
		String s = null;
		switch (functionCode) {
			case M.FunctionCode.FUNCTION_100_MEMBER_REGISTER:
				s = formatFunctionPauseMsg(R.string.register, functionCode);
				break;
			case M.FunctionCode.FUNCTION_102_MEMBER_LOGIN_ANDROID:
				s = formatFunctionPauseMsg(R.string.login, functionCode);
				break;
			case M.FunctionCode.FUNCTION_111_MEMBER_DEPOSIT:
				s = formatFunctionPauseMsg(R.string.deposit, functionCode);
				break;
			case M.FunctionCode.FUNCTION_112_MEMBER_WITHDRAW:
				s = formatFunctionPauseMsg(R.string.withdraw, functionCode);
				break;
			case M.FunctionCode.FUNCTION_118_MEMBER_RECEIVE_TO_ACCOUNT:
				s = formatFunctionPauseMsg(R.string.receive_to_account, functionCode);
				break;
			case M.FunctionCode.FUNCTION_119_MEMBER_EXCHANGE:
				s = formatFunctionPauseMsg(R.string.exchange, functionCode);
				break;
			case M.FunctionCode.FUNCTION_121_MEMBER_TOP_UP_SHOW_PIN_CODE:
				s = formatFunctionPauseMsg(R.string.top_up_show_pin_code, functionCode);
				break;
			case M.FunctionCode.FUNCTION_122_MEMBER_TOP_UP_SEND_SMS:
				s = formatFunctionPauseMsg(R.string.top_up_send_sms, functionCode);
				break;
			case M.FunctionCode.FUNCTION_131_MEMBER_PAY_ELECTRICITY_BILL:
				s = formatFunctionPauseMsg(R.string.electricity, functionCode);
				break;
			case M.FunctionCode.FUNCTION_132_MEMBER_PAY_WATER_BILL:
				s = formatFunctionPauseMsg(R.string.water_supply, functionCode);
				break;
			case M.FunctionCode.FUNCTION_135_MEMBER_PAY_BILL_TO_PARTNER:
				s = formatFunctionPauseMsg(R.string.pay_partner_bill, functionCode);
				break;
			case M.FunctionCode.FUNCTION_113_MEMBER_TRANSFER_TO_MEMBER:
				s = formatFunctionPauseMsg(R.string.transfer_to_member, functionCode);
				break;
			case M.FunctionCode.FUNCTION_114_MEMBER_TRANSFER_TO_NO_MEMBER:
				s = formatFunctionPauseMsg(R.string.transfer_to_non_member, functionCode);
				break;
			case M.FunctionCode.FUNCTION_115_MEMBER_TRANSFER_TO_PARTNER:
				s = formatFunctionPauseMsg(R.string.transfer_to_partner, functionCode);
				break;
		}
		return s;
	}

	private static String formatFunctionPauseMsg(int res, int code) {
		return String.format(Utils.getString(R.string.function_pause_format), Utils.getString(res), code);
	}

	public static String setCountDown(int time) {
		int hh = (time % (24 * 3600) / 3600), mm = time % 3600 / 60, ss = time % 60;
		return (hh < 10 ? "0" : "") + hh + ":" + (mm < 10 ? "0" : "") + mm + ":" + (ss < 10 ? "0" : "") + ss;
	}

}
