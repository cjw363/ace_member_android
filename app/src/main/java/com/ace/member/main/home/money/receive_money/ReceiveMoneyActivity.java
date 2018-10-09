package com.ace.member.main.home.money.receive_money;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseWebSocketActivity;
import com.ace.member.main.home.money.code.CodeFragment;
import com.ace.member.main.home.money.receive_money.set_amount.SetAmountActivity;
import com.ace.member.service.WebSocketService;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.M;
import com.ace.member.utils.Session;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.og.utils.FileUtils;
import com.og.utils.Utils;
import com.zxing.ZxingUtils;

import java.io.File;
import java.io.FileOutputStream;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class ReceiveMoneyActivity extends BaseWebSocketActivity implements ReceiveMoneyContract.View {

	@Inject
	ReceiveMoneyPresenter mPresenter;
	@BindView(R.id.iv_bar_code)
	AppCompatImageView mIvBarCode;
	@BindView(R.id.iv_qr_code)
	AppCompatImageView mIvQrCode;
	@BindView(R.id.tv_currency_amount)
	TextView mTvCurrencyAmount;

	@BindView(R.id.tv_set_amount)
	TextView mTvSetAmount;

	@BindView(R.id.tv_save_image)
	TextView mTvSaveImage;

	@BindView(R.id.rg_currency)
	RadioGroup mRgCurrency;
	@BindView(R.id.rb_usd)
	RadioButton mRbUsd;
	@BindView(R.id.rb_khr)
	RadioButton mRbKhr;
	@BindView(R.id.rb_vnd)
	RadioButton mRbVnd;
	@BindView(R.id.rb_thb)
	RadioButton mRbThb;

	private String mCode;
	private String mCurrency;
	private String mAmount;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		changeAppBrightness(255);
		DaggerReceiveMoneyComponent.builder()
			.receiveMoneyPresenterModule(new ReceiveMoneyPresenterModule(this, this))
			.build()
			.inject(this);
		initSocketHost();
		initActivity();
		mPresenter.start();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_money_receive_money;
	}

	public void initActivity() {
		super.initActivity();
		ToolBarConfig.builder(this, null)
			.setTvTitleRes(R.string.receive_money)
			.setBackgroundRes(R.color.colorPrimary)
			.build();

		mRgCurrency.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
					case R.id.rb_usd:
						mCurrency = AppGlobal.USD;
						mPresenter.onSelectCurrency(mCurrency, mAmount);
						break;
					case R.id.rb_khr:
						mCurrency = AppGlobal.KHR;
						mPresenter.onSelectCurrency(mCurrency, mAmount);
						break;
					case R.id.rb_vnd:
						mCurrency = AppGlobal.VND;
						mPresenter.onSelectCurrency(mCurrency, mAmount);
						break;
					case R.id.rb_thb:
						mCurrency = AppGlobal.THB;
						mPresenter.onSelectCurrency(mCurrency, mAmount);
						break;
				}
			}
		});
	}

	private void initSocketHost() {
		try {
			mSocketHost = Session.socketServers.getReceiveMoney();
		} catch (Exception e) {
			e.printStackTrace();
			FileUtils.addErrorLog(e);
		}
	}

	@Override
	public void initCode(String code) {
		mCode = code;
		try {
			Bitmap qrCodeBitmap = ZxingUtils.createQRImage(mCode, Utils.getDimenPx(R.dimen.width160), Utils.getDimenPx(R.dimen.height160));
			mIvQrCode.setImageBitmap(qrCodeBitmap);

			Bitmap barCodeBitmap = ZxingUtils.createBarcode(this, mCode, Utils.getDimenPx(R.dimen.width320), Utils.getDimenPx(R.dimen.width80), false);
			mIvBarCode.setImageBitmap(barCodeBitmap);
		} catch (Exception e) {
			e.printStackTrace();
			FileUtils.addErrorLog(e);
		}

	}

	@Override
	public void setCurrencyAmount(String currencyAmount) {
		mTvCurrencyAmount.setText(currencyAmount);
	}

	@Override
	public void setCheckRbUsd() {
		mRbUsd.setChecked(true);
	}

	@Override
	public void setCheckRbKhr() {
		mRbKhr.setChecked(true);
	}

	@Override
	public void setCheckRbVnd() {
		mRbVnd.setChecked(true);
	}

	@Override
	public void setCheckRbThb() {
		mRbThb.setChecked(true);
	}

	@Override
	public void showReceiveMoney(ReceiveMoneyResult result) {
		Intent intent=new Intent(this,ReceiveMoneyResultActivity.class);
		intent.putExtra("result",result);
		startActivity(intent);
	}

	public void backBroadcastReceiver(int code,String result) {
		if(code== M.SocketCode.SOCKET_CODE_5_RECEIVE_MONEY) mPresenter.requestResult();
	}

	@Override
	public void unregisterSocketReceiver() {
		unregisterReceiver(imReceiver);
	}

	private void changeAppBrightness(int brightness) {
		try {
			Window window = this.getWindow();
			WindowManager.LayoutParams lp = window.getAttributes();
			if (brightness == -1) {
				lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
			} else {
				lp.screenBrightness = (brightness <= 0 ? 1 : brightness) / 255f;
			}
			window.setAttributes(lp);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@OnClick({R.id.iv_bar_code, R.id.iv_qr_code, R.id.tv_set_amount, R.id.tv_save_image})
	public void onViewClicked(View view) {
		try {
			switch (view.getId()) {
				case R.id.iv_bar_code:
					toCodeFragment(AppGlobal.CODE_TYPE_1_BAR_CODE);
					break;
				case R.id.iv_qr_code:
					toCodeFragment(AppGlobal.CODE_TYPE_2_QR_CODE);
					break;
				case R.id.tv_set_amount:
					if(Utils.strToDouble(mAmount)==0){
						Intent intent=new Intent(ReceiveMoneyActivity.this, SetAmountActivity.class);
						intent.putExtra("currency",mCurrency);
						intent.putExtra("amount",mAmount);
						startActivityForResult(intent,M.RequestCode.REQUEST_CODE_2_SET_AMOUNT);
					}else {
						mTvSetAmount.setText(Utils.getString(R.string.set_amount));
						mAmount=null;
						mPresenter.onSelectCurrency(mCurrency,null);
					}

					break;
				case R.id.tv_save_image:
					AppUtils.scanFile(this, saveImage(), null);
					Utils.showToast(R.string.success, Snackbar.LENGTH_LONG);
					break;
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	private void toCodeFragment(int type) {
		try {
			CodeFragment fragment = (CodeFragment) getSupportFragmentManager().findFragmentById(R.id.fl_content);
			if (fragment == null) fragment = new CodeFragment();
			fragment.getArguments().putInt("type", type);
			fragment.getArguments().putString("code", mCode);
			getSupportFragmentManager().beginTransaction()
				.add(R.id.fl_content, fragment, CodeFragment.TAG)
				.addToBackStack(CodeFragment.TAG)
				.commit();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	private String saveImage() {
		View dView = getWindow().getDecorView();
		dView.setDrawingCacheEnabled(true);
		dView.buildDrawingCache();
		Bitmap bitmap = Bitmap.createBitmap(dView.getDrawingCache());
		if (bitmap != null) {
			try {
				String sdCardPath = Environment.getExternalStorageDirectory().getPath();
				String filePath = sdCardPath + File.separator + "pay_code_" + System.currentTimeMillis() + ".png";
				File file = new File(filePath);
				FileOutputStream os = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
				os.flush();
				os.close();
				return filePath;
			} catch (Exception e) {
				e.printStackTrace();
				FileUtils.addErrorLog(e);
			}
		}
		return null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode== Activity.RESULT_OK){
			switch (requestCode){
				case M.RequestCode.REQUEST_CODE_2_SET_AMOUNT:
					mAmount=data.getStringExtra("amount");
					if(Utils.strToDouble(mAmount)>0){
						mTvSetAmount.setText(Utils.getString(R.string.remove_amount));
					}
					mPresenter.onSelectCurrency(mCurrency,mAmount);
					break;
			}
		}
	}
}
