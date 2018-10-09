package com.ace.member.main.third_party.wsa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.WsaBill;
import com.ace.member.event.SelectWsaRecentEvent;
import com.ace.member.main.bottom_dialog.BottomDialog;
import com.ace.member.main.image_detail.DeleteImageEvent;
import com.ace.member.main.image_detail.Image;
import com.ace.member.main.image_detail.ImageDetailActivity;
import com.ace.member.main.third_party.wsa.detail.WsaHistoryDetailActivity;
import com.ace.member.main.third_party.wsa.history.WsaHistoryActivity;
import com.ace.member.main.third_party.wsa.recent_contact.WsaRecentContactFragment;
import com.ace.member.simple_listener.SimpleTextWatcher;
import com.ace.member.simple_listener.SimpleViewClickListener;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.M;
import com.ace.member.utils.UriUtil;
import com.bumptech.glide.Glide;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class WsaActivity extends BaseActivity implements WsaContract.View {

	@Inject
	WsaPresenter mPresenter;

	@BindView(R.id.tv_type_info)
	TextView mTvTypeInfo;
	@BindView(R.id.tv_type)
	TextView mTvType;
	@BindView(R.id.tv_number_title)
	TextView mTvNumberTitle;
	@BindView(R.id.et_number)
	EditText mEtNumber;
	@BindView(R.id.tv_phone_hint)
	TextView mTvPhoneHint;
	@BindView(R.id.ll_phone)
	LinearLayout mLlPhone;
	@BindView(R.id.et_phone)
	EditText mEtPhone;
	@BindView(R.id.et_amount)
	EditText mEtAmount;
	@BindView(R.id.tv_currency)
	TextView mTvCurrency;
	@BindView(R.id.tv_fee_title)
	TextView mTvFeeTitle;
	@BindView(R.id.tv_fee)
	TextView mTvFee;
	@BindView(R.id.et_remark)
	EditText mEtRemark;
	@BindView(R.id.iv_image)
	AppCompatImageView mIvImage;
	@BindView(R.id.btn_submit)
	Button mBtnSubmit;
	@BindView(R.id.pb_compress)
	ProgressBar mPbCompress;
	private int mCurrentType;
	private BottomDialog mTypeDialog;
	private List<Image> mList;
	private String mPath;
	private BottomDialog mSelectImageDlg;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerWsaComponent.builder()
			.wsaPresenterModule(new WsaPresenterModule(this, this))
			.build()
			.inject(this);
		init();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_wsa;
	}

	private void init() {
		ToolBarConfig.builder(this, null)
			.setEnableMenu(true)
			.setMenuType(ToolBarConfig.MenuType.MENU_IMAGE)
			.setIvMenuRes(R.drawable.ic_history)
			.setTvTitleRes(R.string.water_supply)
			.setMenuListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Utils.toActivity(WsaActivity.this, WsaHistoryActivity.class);
				}
			})
			.build();

		mEtPhone.addTextChangedListener(new SimpleTextWatcher.PhoneTextWatcher(mEtPhone));

		mEtAmount.setHint(Utils.getString(R.string.amount_khr));
		mEtAmount.addTextChangedListener(new SimpleTextWatcher.MoneyTextWatcher(mEtAmount, 0) {
			@Override
			public void afterTextChanged(Editable s) {
				String result = mEtAmount.getText().toString().replace(",", "");
				if (TextUtils.isEmpty(result)) {
					mEtAmount.setHint(Utils.getString(R.string.amount_khr));
					mTvCurrency.setText(null);
				} else {
					mEtAmount.setHint(null);
					mTvCurrency.setText(R.string.khr);
				}
				mPresenter.updateFee(result);
			}
		});
		mPresenter.start();
	}

	@OnClick({R.id.rl_type, R.id.fl_contact, R.id.tv_phone_hint, R.id.tv_currency, R.id.iv_image, R.id.btn_submit})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.rl_type:
				showSelectTypeDlg(mPresenter.getWsaTypeArr());
				break;
			case R.id.fl_contact:
				showSelectRecentContact();
				break;
			case R.id.tv_phone_hint:
				showPhone();
				break;
			case R.id.tv_currency:
				mEtAmount.requestFocus();
				AppUtils.enableSoftInput(mEtAmount, true);
				break;
			case R.id.iv_image:
				if (Utils.isEmptyList(mList)) showSelectImageDlg();
				else toImageDetail(0, mIvImage);
				break;
			case R.id.btn_submit:
				mPresenter.submitWsa(mCurrentType, mEtNumber.getText().toString().trim(), mEtPhone.getText()
					.toString()
					.trim(), mEtAmount.getText().toString().trim(), mTvFee.getText()
					.toString()
					.trim(), mEtRemark.getText().toString().trim(), mList);
				break;
		}
	}

	private void showPhone() {
		mTvPhoneHint.setVisibility(View.GONE);
		mLlPhone.setVisibility(View.VISIBLE);
		mEtPhone.requestFocus();
	}

	private void showSelectRecentContact() {
		WsaRecentContactFragment fragment = new WsaRecentContactFragment();
		fragment.show(getSupportFragmentManager(), "WsaRecentContactFragment");
	}

	private void showSelectTypeDlg(final String[] typeArr) {
		if (mTypeDialog == null) {
			BottomDialog.Builder builder = new BottomDialog.Builder(this);
			mTypeDialog = builder.setTvContent2(typeArr)
				.setTvTitle(Utils.getString(R.string.select))
				.setClickListener(new SimpleViewClickListener() {
					@Override
					public void onClick(View view, int position) {
						mPresenter.onSelectType(position);
					}
				})
				.create();
		}
		mTypeDialog.show();
	}

	@Override
	public void setSelectType(int type) {
		mCurrentType = type;
	}

	@Override
	public void setSelectTypeText(String s) {
		mTvType.setText(s);
	}

	@Override
	public void setSelectTypeTextColor(int color) {
		mTvType.setTextColor(color);
	}

	private void toImagePicker() {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		startActivityForResult(intent, AppGlobal.IMAGE_CODE_1_PICKER);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (mList == null) mList = new ArrayList<>();
			else mList.clear();
			Image image = new Image();
			String uri;
			if (requestCode == AppGlobal.IMAGE_CODE_1_PICKER) {
				if (data == null) return;
				mPath = UriUtil.getPhotoPathFromContentUri(this, data.getData());
			} else if (requestCode == AppGlobal.IMAGE_CODE_2_CAMERA) {
				AppUtils.scanFile(this, mPath, null);
			}
			uri = "file://" + mPath;
			image.setUri(uri);
			image.setDate(mPath);
			mList.add(image);
			Glide.with(this).load(uri).into(mIvImage);
		}
	}

	private void toImageDetail(int current, View view) {
		Intent intent = new Intent(WsaActivity.this, ImageDetailActivity.class);
		intent.putExtra(M.ImagePicker.OPEN_MENU, true);
		intent.putExtra(M.ImagePicker.IMAGES, (Serializable) AppUtils.dealImageList(mList));
		intent.putExtra(M.ImagePicker.CURRENT, current);
		ActivityOptionsCompat options = ActivityOptionsCompat.makeScaleUpAnimation(view, 0, 0, view.getWidth(), view
			.getHeight());
		startActivity(intent, options.toBundle());
	}

	private void showSelectImageDlg() {
		if (mSelectImageDlg == null) {
			BottomDialog.Builder builder = new BottomDialog.Builder(this);
			mSelectImageDlg = builder.setTvTitle(Utils.getString(R.string.select))
				.setIvRes(new int[]{R.drawable.ic_take_picture, R.drawable.ic_album})
				.setTvContent2(new String[]{Utils.getString(R.string.take_picture), Utils.getString(R.string.select_from_album)})
				.setClickListener(new SimpleViewClickListener() {
					@Override
					public void onClick(View view, int position) {
						if (position == 0) toCamera();
						else toImagePicker();
					}
				})
				.create();
		}
		mSelectImageDlg.show();
	}

	@Override
	public void enablePbCompress(boolean enable) {
		mPbCompress.setVisibility(enable ? View.VISIBLE : View.GONE);
	}

	@Override
	public void setAmount(String amount) {
		mEtAmount.setText(amount);
	}

	@Override
	public void setFee(String fee) {
		mTvFee.setText(String.format(Utils.getString(R.string.amount_khr_format), Utils.format(fee)));
		enableFee(!Utils.checkEmptyAmount(fee));
	}

	@Override
	public void enableFee(boolean enable) {
		mTvFeeTitle.setEnabled(enable);
		mTvFee.setEnabled(enable);
	}

	@Override
	public void setNumberTitle(String title) {
		mTvNumberTitle.setText(title);
		mEtNumber.setHint(title);
	}

	@Override
	public void setPhone(String phone) {
		mEtPhone.setText(phone);
	}

	@Override
	public void enablePhone(boolean enable) {
		if (enable) {
			mTvPhoneHint.setVisibility(View.GONE);
			mLlPhone.setVisibility(View.VISIBLE);
		} else {
			mTvPhoneHint.setVisibility(View.VISIBLE);
			mLlPhone.setVisibility(View.GONE);
		}
	}

	@Override
	public void resetInterface() {
		mTvTypeInfo.setFocusable(true);
		mTvTypeInfo.setFocusableInTouchMode(true);
		mTvTypeInfo.requestFocus();
		mEtNumber.setText("");
		mEtAmount.setText("");
		String t = "0 " + Utils.getString(R.string.khr);
		mTvFee.setText(t);
		mEtRemark.setText("");
		if (!Utils.isEmptyList(mList)) mList.clear();
		mIvImage.setImageResource(R.drawable.ic_photograph);
	}

	@Override
	public void enableBtnSubmit(boolean enable) {
		mBtnSubmit.setEnabled(enable);
	}

	@Override
	public void toPaymentHistoryDetailActivity(int billId) {
		Intent intent = new Intent(this, WsaHistoryDetailActivity.class);
		intent.putExtra("id", billId);
		intent.putExtra("showConfirm", true);
		startActivity(intent);
	}

	private void toCamera() {
		PERMISSIONS = new String[]{android.Manifest.permission.CAMERA};
		if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
			setPermissions();
		} else {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			mPath = FileUtils.getCameraImageFilePath() + UUID.randomUUID().toString() + ".jpg";
			if (TextUtils.isEmpty(mPath)) {
				Utils.showToast(R.string.not_enough_storage_space);
				return;
			}
			intent.putExtra(MediaStore.EXTRA_OUTPUT, FileUtils.getUriForFile(new File(mPath)));
			startActivityForResult(intent, AppGlobal.IMAGE_CODE_2_CAMERA);
		}
	}

	@Subscribe
	public void onDeleteImageEvent(DeleteImageEvent event) {
		mList.remove(event.getPosition());
		mIvImage.setImageResource(R.drawable.ic_photograph);
	}

	@Subscribe
	public void onSelectRecentContact(SelectWsaRecentEvent event) {
		if (event == null) return;
		WsaBill bill = event.getWsaBill();
		if (bill == null) return;
		String phone = Utils.getRealPhone(bill.getPhone());
		showPhone();
		mEtPhone.setText(phone);
		mEtPhone.setSelection(phone.length());
	}

	@Override
	public AppCompatActivity getActivity() {
		return this;
	}
}
