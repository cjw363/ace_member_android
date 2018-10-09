package com.ace.member.main.me.language;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.og.utils.Utils;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class LanguageActivity extends BaseActivity implements LanguageContract.LanguageView {

	@Inject
	LanguagePresenter mLanguagePresenter;
	@BindView(R.id.tv_english)
	ImageView mTvEnglish;
	@BindView(R.id.btn_language1)
	LinearLayout mBtnEnglish;
	@BindView(R.id.tv_chinese)
	ImageView mTvChinese;
	@BindView(R.id.btn_language2)
	LinearLayout mBtnChinese;
	@BindView(R.id.tv_cambodian)
	ImageView mTvCambodian;
	@BindView(R.id.btn_language3)
	LinearLayout mBtnCambodian;
	@BindView(R.id.tv_vietnamese)
	ImageView mTvVietnamese;
	@BindView(R.id.btn_language4)
	LinearLayout mBtnVietnamese;
	@BindView(R.id.tv_menu)
	TextView mTvSave;
	@BindView(R.id.tv_system_language)
	ImageView mTvSystemLanguage;
	@BindView(R.id.btn_system_language)
	LinearLayout mBtnSystemLanguage;

	private static final int SAVE_STATUS_1 = 1;
	private static final int SAVE_STATUS_2 = 2;

	private int mStatus;
	private int mSaveStatus = SAVE_STATUS_1;

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerLanguageComponent.builder().languagePresenterModule(new LanguagePresenterModule(this, this)).build().inject(this);
		initActivity();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_language;
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public void initActivity() {
		ToolBarConfig.builder(this, null).setTvTitleRes(R.string.language).setEnableMenu(true).setMenuType(ToolBarConfig.MenuType.MENU_TEXT).setTvMenuRes(R.string.save).build();
		mTvSave.setTextColor(Utils.getColor(R.color.clr_toolbar_right));
		viewCheck();
	}

	@OnClick({R.id.btn_system_language, R.id.btn_language1, R.id.btn_language2, R.id.btn_language3, R.id.btn_language4})
	public void onViewClicked(View view) {
		int STATUS_SYSTEM = 0, STATUS_EN = 1, STATUS_ZH = 2, STATUS_KM = 3, STATUS_VI = 4;
		try {
			switch (view.getId()) {
				case R.id.btn_system_language:
					mTvEnglish.setVisibility(View.INVISIBLE);
					mTvChinese.setVisibility(View.INVISIBLE);
					mTvCambodian.setVisibility(View.INVISIBLE);
					mTvVietnamese.setVisibility(View.INVISIBLE);
					mTvSystemLanguage.setVisibility(View.VISIBLE);
					colorCheck(STATUS_SYSTEM);
					mStatus = STATUS_SYSTEM;
					break;
				case R.id.btn_language1:
					mTvEnglish.setVisibility(View.VISIBLE);
					mTvChinese.setVisibility(View.INVISIBLE);
					mTvCambodian.setVisibility(View.INVISIBLE);
					mTvVietnamese.setVisibility(View.INVISIBLE);
					mTvSystemLanguage.setVisibility(View.INVISIBLE);
					colorCheck(STATUS_EN);
					mStatus = STATUS_EN;
					break;
				case R.id.btn_language2:
					mTvEnglish.setVisibility(View.INVISIBLE);
					mTvChinese.setVisibility(View.VISIBLE);
					mTvCambodian.setVisibility(View.INVISIBLE);
					mTvVietnamese.setVisibility(View.INVISIBLE);
					mTvSystemLanguage.setVisibility(View.INVISIBLE);
					colorCheck(STATUS_ZH);
					mStatus = STATUS_ZH;
					break;
				case R.id.btn_language3:
					mTvEnglish.setVisibility(View.INVISIBLE);
					mTvChinese.setVisibility(View.INVISIBLE);
					mTvCambodian.setVisibility(View.VISIBLE);
					mTvVietnamese.setVisibility(View.INVISIBLE);
					mTvSystemLanguage.setVisibility(View.INVISIBLE);
					colorCheck(STATUS_KM);
					mStatus = STATUS_KM;
					break;
				case R.id.btn_language4:
					mTvEnglish.setVisibility(View.INVISIBLE);
					mTvChinese.setVisibility(View.INVISIBLE);
					mTvCambodian.setVisibility(View.INVISIBLE);
					mTvVietnamese.setVisibility(View.VISIBLE);
					mTvSystemLanguage.setVisibility(View.INVISIBLE);
					colorCheck(STATUS_VI);
					mStatus = STATUS_VI;
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	private void viewCheck() {
		try {
			SharedPreferences ls = getSharedPreferences("language_status", Context.MODE_PRIVATE);
			int id = ls.getInt("id", 0);
			Configuration configuration = getResources().getConfiguration();
			if (id == 0) {
				Locale locale = getResources().getConfiguration().locale;
				String language = locale.getLanguage();
				mTvSystemLanguage.setVisibility(View.VISIBLE);
				switch (language) {
					case "en":
						configuration.setLocale(new Locale("en", "US"));
						break;
					case "zh":
						configuration.setLocale(Locale.CHINESE);
						break;
					case "km":
						configuration.setLocale(new Locale("km", "KH"));
						break;
					case "vi":
						configuration.setLocale(new Locale("vi", "VN"));
						break;
					default:
						configuration.setLocale(new Locale("en", "US"));
						break;
				}
			} else {
				switch (id) {
					case 1:
						mTvEnglish.setVisibility(View.VISIBLE);
						configuration.setLocale(new Locale("en", "US"));
						break;
					case 2:
						mTvChinese.setVisibility(View.VISIBLE);
						configuration.setLocale(Locale.CHINESE);
						break;
					case 3:
						mTvCambodian.setVisibility(View.VISIBLE);
						configuration.setLocale(new Locale("km", "KH"));
						break;
					case 4:
						mTvVietnamese.setVisibility(View.VISIBLE);
						configuration.setLocale(new Locale("vi", "VN"));
						break;
					default:
						break;
				}
			}

			getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	private void colorCheck(int id) {
		try {
			SharedPreferences sp = getSharedPreferences("language_status", Context.MODE_PRIVATE);
			int sid = sp.getInt("id", 0);
			if (id == sid) {
				mTvSave.setTextColor(Utils.getColor(R.color.clr_toolbar_right));
				mSaveStatus = SAVE_STATUS_1;
			} else {
				mTvSave.setTextColor(Utils.getColor(R.color.white));
				mSaveStatus = SAVE_STATUS_2;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	@OnClick(R.id.tv_menu)
	public void onViewClicked() {
		try {
			SharedPreferences ls = getSharedPreferences("language_status", Context.MODE_PRIVATE);
			if (mSaveStatus != SAVE_STATUS_1) {
				ls.edit().putInt("id", mStatus).apply();
				viewCheck();
				final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
				if(intent != null){
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
					startActivity(intent);
					Process.killProcess(Process.myPid());
					System.exit(0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
