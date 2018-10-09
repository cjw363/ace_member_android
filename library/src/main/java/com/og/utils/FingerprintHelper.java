package com.og.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.Log;
import com.og.LibSession;
import com.og.R;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import java.io.IOException;
import java.security.*;

public class FingerprintHelper {
	private Context mContext;
	private FingerprintManager mManager;
	private SharedPreferences mPreferences;


	private String keyStoreKey; //保存在keySore里面的key
	private final static String encryptFingerprint = "encrypt_fingerprint"; //保存在本地加密过后的电话信息
	private final static String encryptIV = "encrypt_iv";//保存在本地的key
	public final static String originalPhone = "member_phone"; //保存在本地的原始电话号码

	private static final String TRANSFORMATION = "AES/GCM/NoPadding";
	private static final String ANDROID_KEY_STORE = "AndroidKeyStore";

	private static final String KEY_PRE_LOGIN = "fingerprint_login";
	private static final String KEY_PRE_PAY = "fingerprint_pay";

	private static final String KEY_STORE_LOGIN_KEY = "keyStore_login";
	private static final String KEY_STORE_PAY_KEY = "keyStore_pay";

	private final static String KEY_USE_FINGERPRINT = "use_fingerprint";

	private KeyStore mKeyStore;

	public FingerprintHelper(Context context, String preKey, String storeKey) {
		this.mContext = context;
		this.keyStoreKey = storeKey;
		mPreferences = context.getSharedPreferences(preKey, Context.MODE_PRIVATE);
		if (Build.VERSION.SDK_INT >= 23) {
			mManager = context.getSystemService(FingerprintManager.class);
			try {
				mKeyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
				mKeyStore.load(null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean checkAvailable(boolean showMsg) {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
			if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions((Activity) mContext, new String[]{android.Manifest.permission.USE_FINGERPRINT}, 1);
				if (showMsg) Utils.showToast(R.string.no_permission);
				return false;
			} else {
				if (!LibSession.sDebug && Utils.getDeviceID(mContext).equals("000000000000000")) {
					if (showMsg) Utils.showToast(R.string.invalid_device_id);
				} else if (!mManager.isHardwareDetected()) {
					if (showMsg) Utils.showToast(R.string.fingerprint_hardware_not_support);
					return false;
				} else if (!mManager.hasEnrolledFingerprints()) {
					if (showMsg) Utils.showToast(R.string.has_no_fingerprint);
					return false;
				}
				return true;
			}
		} else {
			if (showMsg) Utils.showToast(R.string.fingerprint_not_support);
			return false;
		}

	}

	private void encryptText(final String textToEncrypt) throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IOException, InvalidAlgorithmParameterException, SignatureException, BadPaddingException, IllegalBlockSizeException {

		final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		SecretKey secretKey = getSecretKey(keyStoreKey);
		if (secretKey != null) {
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			storeData(encryptIV, new String(Base64.encode(cipher.getIV(), Base64.DEFAULT)));
			storeData(encryptFingerprint, new String(Base64.encode(cipher.doFinal(textToEncrypt.getBytes("UTF-8")), Base64.DEFAULT)));
		}
	}

	private SecretKey getSecretKey(final String alias) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
		final KeyGenerator keyGenerator;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
			keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE);
			keyGenerator.init(new KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
				.setBlockModes(KeyProperties.BLOCK_MODE_GCM)
				.setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
				.build());
			return keyGenerator.generateKey();
		}
		return null;
	}

	private byte[] getEncryption() {
		return Base64.decode(getData(encryptFingerprint), Base64.DEFAULT);
	}

	private byte[] getIv() {
		return Base64.decode(getData(encryptIV), Base64.DEFAULT);
	}

	private String decryptData(final byte[] encryptedData, final byte[] encryptionIv) throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {

		final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		final GCMParameterSpec spec;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
			spec = new GCMParameterSpec(128, encryptionIv);
			cipher.init(Cipher.DECRYPT_MODE, getDecryptSecretKey(keyStoreKey), spec);

			return new String(cipher.doFinal(encryptedData), "UTF-8");
		}
		return null;
	}

	private SecretKey getDecryptSecretKey(final String alias) throws NoSuchAlgorithmException, UnrecoverableEntryException, KeyStoreException {
		mKeyStore.getEntry(alias, null);
		return ((KeyStore.SecretKeyEntry) mKeyStore.getEntry(alias, null)).getSecretKey();
	}

	public String getData(String keyName) {
		return mPreferences.getString(keyName, "");
	}

	private boolean storeData(String key, String data) {
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.putString(key, data);
		return editor.commit();
	}

	public boolean getIsUseFingerprint() {
		return mPreferences.getBoolean(KEY_USE_FINGERPRINT, false);
	}

	public boolean setIsUseFingerprint(Boolean data) {
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.putBoolean(KEY_USE_FINGERPRINT, data);
		return editor.commit();
	}

	public void saveFingerprintData(String phone) {
		try {
			encryptText(phone);
			storeData(originalPhone, phone);
		} catch (UnrecoverableEntryException | NoSuchAlgorithmException | SignatureException | BadPaddingException | KeyStoreException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchPaddingException | IOException | NoSuchProviderException | IllegalBlockSizeException e) {
			e.printStackTrace();
		}
	}

	//得到解密后的信息,也就是还原信息
	public String decryptFingerprintData() {
		try {
			return decryptData(getEncryption(), getIv());
		} catch (UnrecoverableEntryException | NoSuchAlgorithmException | BadPaddingException | KeyStoreException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchPaddingException | IOException | NoSuchProviderException | IllegalBlockSizeException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void clearData() {
		mPreferences.edit().clear().apply();
	}

	public void test() {
		Log.e("是否启用", "" + getIsUseFingerprint());
		Log.e("加密数据:->", "" + new String(getEncryption()));
		Log.e("还原信息-->", "" + decryptFingerprintData());
		Log.e("保存的Share信息", "" + getData(encryptFingerprint));
		//	Log.e("破坏信息","--");
		//	storeData(dataFingerprint, "fKOOy1qhoFi9norroXIl/k8/PqSLL4dTVA+W+cg=");
		//	Log.e("破坏后的Share信息",""+getData(dataFingerprint));
		//	Log.e("破坏后的还原信息-->",""+getFingerprintData());
	}

	public static class Login extends FingerprintHelper {
		public Login(Context context) {
			super(context, FingerprintHelper.KEY_PRE_LOGIN, FingerprintHelper.KEY_STORE_LOGIN_KEY);
		}
	}

	public static class Pay extends FingerprintHelper {
		public Pay(Context context) {
			super(context, FingerprintHelper.KEY_PRE_PAY, FingerprintHelper.KEY_STORE_PAY_KEY);
		}
	}

}

