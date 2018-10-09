package com.ace.member.main.verify_certificate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.MemberProfile;
import com.ace.member.bean.Verify;
import com.ace.member.date_picker.DatePickerDialog;
import com.ace.member.main.bottom_dialog.BottomDialog;
import com.ace.member.main.image_detail.DeleteImageEvent;
import com.ace.member.main.image_detail.ImageDetailActivity;
import com.ace.member.simple_listener.SimpleViewClickListener;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.M;
import com.ace.member.utils.Session;
import com.bumptech.glide.Glide;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class VerifyCertificateActivity extends BaseActivity implements VerifyCertificateContract.View {
	@Inject
	VerifyCertificatePresenter mPresenter;
	@BindView(R.id.ll_status)
	LinearLayout mLlStatus;
	@BindView(R.id.ll_detail)
	LinearLayout mLlDetail;
	@BindView(R.id.iv_status)
	AppCompatImageView mIvStatus;
	@BindView(R.id.tv_status_des)
	TextView mTvStatusDes;
	@BindView(R.id.tv_id_type2)
	TextView mTvIDType2;
	@BindView(R.id.tv_id_number)
	TextView mTvIdNumber;
	@BindView(R.id.tv_remark)
	TextView mTvRemark;

	@BindView(R.id.tv_name)
	TextView mTvName;
	@BindView(R.id.tv_sex_2)
	TextView mTvSex2;
	@BindView(R.id.tv_nationality_2)
	TextView mTvNationality2;
	@BindView(R.id.btn_recertify)
	Button mBtnRecertify;

	@BindView(R.id.sv_verify)
	ScrollView mSvVerify;
	@BindView(R.id.ll_id_type)
	LinearLayout mLlIdType;
	@BindView(R.id.tv_id_type)
	TextView mTvIdType;

	@BindView(R.id.et_id_number)
	EditText mEtIdNumber;

	@BindView(R.id.ll_sex)
	LinearLayout mLlSex;
	@BindView(R.id.tv_sex)
	TextView mTvSex;
	@BindView(R.id.iv_sex)
	AppCompatImageView mIvSex;

	@BindView(R.id.ll_nationality)
	LinearLayout mLlNationality;
	@BindView(R.id.tv_nationality)
	TextView mTvNationality;
	@BindView(R.id.iv_nationality)
	AppCompatImageView mIvNationality;

	@BindView(R.id.ll_birthday)
	LinearLayout mLlBirthday;
	@BindView(R.id.tv_birthday)
	TextView mTvBirthday;
	@BindView(R.id.iv_birthday)
	AppCompatImageView mIvBirthday;

	@BindView(R.id.iv_photo1)
	AppCompatImageView mIvPhoto1;
	@BindView(R.id.iv_take_photo1)
	AppCompatImageView mIvTakePhoto1;
	@BindView(R.id.iv_photo2)
	AppCompatImageView mIvPhoto2;
	@BindView(R.id.iv_take_photo2)
	AppCompatImageView mIvTakePhoto2;
	@BindView(R.id.btn_submit)
	Button mBtnSubmit;
	private BottomDialog mIdTypeBottomDialog;
	private BottomDialog mSexBottomDialog;
	private BottomDialog mNationalityBottomDialog;

	private String[] mIdTypeArr;
	private String[] mSexArr;
	private String[] mNationalityArr;
	private String mBirthday;

	private String[] mPhotos;
	private String[] mTemps;
	private int mCurrent;
	private DatePickerDialog mDatePickerDialog;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerVerifyCertificateComponent.builder()
			.verifyCertificatePresenterModule(new VerifyCertificatePresenterModule(this, this))
			.build()
			.inject(this);
		mPresenter.start();
		init();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_verify_certificate;
	}

	private void init() {
		ToolBarConfig.builder(this, null).setTvTitleRes(R.string.id_certificate).build();
		mIdTypeArr = Utils.getStringArray(R.array.id_type_array);
		mPhotos = new String[2];
		mTemps = new String[2];
	}

	private void showSelectIDTypeDlg() {
		if (mIdTypeBottomDialog == null) {
			if (mIdTypeArr == null || mIdTypeArr.length == 0) {
				mIdTypeArr = Utils.getStringArray(R.array.id_type_array);
			}
			BottomDialog.Builder builder = new BottomDialog.Builder(this);
			mIdTypeBottomDialog = builder.setTvTitle(Utils.getString(R.string.id_type))
				.setTvContent2(mIdTypeArr)
				.setClickListener(new SimpleViewClickListener() {
					@Override
					public void onClick(View view, int position) {
						mPresenter.onTypeSelected(position);
					}
				})
				.create();
		}
		mIdTypeBottomDialog.show();
	}

	private void showSelectSexDlg() {
		if (mSexBottomDialog == null) {
			if (mSexArr == null || mSexArr.length == 0) {
				mSexArr = Utils.getStringArray(R.array.sex_array);
			}
			BottomDialog.Builder builder = new BottomDialog.Builder(this);
			mSexBottomDialog = builder.setTvTitle(Utils.getString(R.string.sex))
				.setTvContent2(mSexArr)
				.setClickListener(new SimpleViewClickListener() {
					@Override
					public void onClick(View view, int position) {
						mPresenter.onSexSelected(position);
					}
				})
				.create();
		}
		mSexBottomDialog.show();
	}

	private void showSelectNationalityDlg() {
		if (mNationalityBottomDialog == null) {
			if (mNationalityArr == null || mNationalityArr.length == 0) {
				mNationalityArr = Utils.getStringArray(R.array.nationality_array);
			}
			BottomDialog.Builder builder = new BottomDialog.Builder(this);
			mNationalityBottomDialog = builder.setTvTitle(Utils.getString(R.string.nationality))
				.setTvContent2(mNationalityArr)
				.setClickListener(new SimpleViewClickListener() {
					@Override
					public void onClick(View view, int position) {
						mPresenter.onNationalitySelected(position);
					}
				})
				.setBottomHeight(Utils.dip2px(256))
				.create();
		}
		mNationalityBottomDialog.show();
	}

	private void showSelectBirthdayDlg() {
		if (mDatePickerDialog == null) {
			mDatePickerDialog = new DatePickerDialog(this);
			mDatePickerDialog.setDateSelectListener(new DatePickerDialog.IDateSelectListener() {
				@Override
				public void onDateSelect(String date) {
					mBirthday = date;
					mTvBirthday.setText(date);
					mTvBirthday.setTextColor(Utils.getColor(R.color.black));
				}
			});
		}
		mDatePickerDialog.show();
	}


	@OnClick({R.id.btn_recertify, R.id.ll_id_type, R.id.ll_sex, R.id.ll_nationality, R.id.ll_birthday, R.id.iv_take_photo1, R.id.iv_take_photo2, R.id.iv_photo1, R.id.iv_photo2, R.id.btn_submit})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.btn_recertify:
				enableRlStatus(false);
				enableSvVerify(true);
				break;
			case R.id.ll_id_type:
				showSelectIDTypeDlg();
				break;
			case R.id.ll_sex:
				showSelectSexDlg();
				break;
			case R.id.ll_nationality:
				showSelectNationalityDlg();
				break;
			case R.id.ll_birthday:
				showSelectBirthdayDlg();
				break;
			case R.id.iv_take_photo1:
				mTemps[0] = toCamera(0);
				break;
			case R.id.iv_take_photo2:
				mTemps[1] = toCamera(1);
				break;
			case R.id.iv_photo1:
				mCurrent = 0;
				toImageDetail(0, view, "file://" + mPhotos[0]);
				break;
			case R.id.iv_photo2:
				mCurrent = 1;
				toImageDetail(0, view, "file://" + mPhotos[1]);
				break;
			case R.id.btn_submit:
				if (!Utils.isFastClick(this)) {
					mPresenter.btnSubmit(mEtIdNumber.getText().toString().trim(), Arrays.asList(mPhotos));
				}
				break;
		}
	}

	@Subscribe
	public void onDeleteImageEvent(DeleteImageEvent event) {
		FileUtils.delete(mPhotos[mCurrent]);
		mPhotos[mCurrent] = "";
		enablePhoto(mCurrent, false);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == 0 || requestCode == 1) {
				mPhotos[requestCode] = mTemps[requestCode];
				enablePhoto(requestCode, true);
				Glide.with(this).load(mPhotos[requestCode]).into(requestCode == 0 ? mIvPhoto1 : mIvPhoto2);
				AppUtils.scanFile(this, mPhotos[requestCode], null);
			}
		}
	}

	private void enablePhoto(int index, boolean enable) {
		if (index == 0) mIvPhoto1.setVisibility(enable ? View.VISIBLE : View.GONE);
		if (index == 1) mIvPhoto2.setVisibility(enable ? View.VISIBLE : View.GONE);
	}

	public String toCamera(int index) {
		String path = "";
		PERMISSIONS = new String[]{android.Manifest.permission.CAMERA};
		if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
			setPermissions();
		} else {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			path = FileUtils.getCameraImageFilePath() + UUID.randomUUID().toString() + ".jpg";
			if (TextUtils.isEmpty(path)) {
				Utils.showToast(R.string.not_enough_storage_space);
			} else {
				intent.putExtra(MediaStore.EXTRA_OUTPUT, FileUtils.getUriForFile(new File(path)));
				startActivityForResult(intent, index);
			}
		}
		return path;
	}


	public void toImageDetail(int current, View view, String image) {
		Intent intent = new Intent(this, ImageDetailActivity.class);
		intent.putExtra(M.ImagePicker.OPEN_MENU, true);
		intent.putExtra(M.ImagePicker.IMAGES, (Serializable) Collections.singletonList(image));
		intent.putExtra(M.ImagePicker.CURRENT, current);
		ActivityOptionsCompat options = ActivityOptionsCompat.makeScaleUpAnimation(view, 0, 0, view.getWidth(), view
			.getHeight());
		startActivity(intent, options.toBundle());
	}

	@Override
	public void enableSvVerify(boolean enable) {
		mSvVerify.setVisibility(enable ? View.VISIBLE : View.GONE);
	}

	@Override
	public void enableBtnSubmit(boolean enable) {
		mBtnSubmit.setEnabled(enable);
	}

	public void setIdType(int index) {
		mTvIdType.setText(mIdTypeArr[index]);
		mTvIdType.setTextColor(Utils.getColor(R.color.black));
	}

	@Override
	public void setSex(int index) {
		mTvSex.setText(mSexArr[index]);
		mTvSex.setTextColor(Utils.getColor(R.color.black));
	}

	@Override
	public void setNationality(int index) {
		mTvNationality.setText(mNationalityArr[index]);
		mTvNationality.setTextColor(Utils.getColor(R.color.black));
	}

	@Override
	public String getBirthDay() {
		return mBirthday;
	}

	@Override
	public void enableRlStatus(boolean enable) {
		mLlStatus.setVisibility(enable ? View.VISIBLE : View.GONE);
	}

	@Override
	public void enableDetail(boolean enable) {
		mLlDetail.setVisibility(enable ? View.VISIBLE : View.GONE);
	}

	@Override
	public void enableBtnRecertify(boolean enable) {
		mBtnRecertify.setVisibility(enable ? View.VISIBLE : View.GONE);
	}

	@Override
	public void setVerify(Verify verify) {
		mTvIDType2.setText(AppUtils.getIDCertificateType(verify.getCertificateType()));
		mTvIdNumber.setText(verify.getCertificateNumber());
		mTvRemark.setText(verify.getRemark());
		int status = verify.getStatus();
		mIvStatus.setImageResource(AppUtils.getIDCertificateStatusDrawable(status));
		mTvStatusDes.setText(AppUtils.getIDCertificateStatusDes(status));

	}

	@Override
	public void setProfile(MemberProfile profile) {
		if (profile == null || profile.getFlagLock() != AppGlobal.FLAG_LOCK_YES) {
			mLlSex.setClickable(true);
			mLlNationality.setClickable(true);
			mLlBirthday.setClickable(true);

			mIvSex.setVisibility(View.VISIBLE);
			mIvNationality.setVisibility(View.VISIBLE);
			mIvBirthday.setVisibility(View.VISIBLE);
		} else {
			mLlSex.setClickable(false);
			mLlNationality.setClickable(false);
			mLlBirthday.setClickable(false);

			mIvSex.setVisibility(View.GONE);
			mIvNationality.setVisibility(View.GONE);
			mIvBirthday.setVisibility(View.GONE);
		}

		if (profile != null) {
			mTvSex.setText(AppUtils.getSex(profile.getSex()));
			mTvNationality.setText(AppUtils.getNationality(profile.getNationality()));
			mTvBirthday.setText(profile.getDateOfBirth());
			mBirthday = profile.getDateOfBirth();

			mTvSex.setTextColor(Utils.getColor(R.color.black));
			mTvNationality.setTextColor(Utils.getColor(R.color.black));
			mTvBirthday.setTextColor(Utils.getColor(R.color.black));
		}
	}

	@Override
	public void setProfile2(MemberProfile profile) {
		if (profile == null) return;
		mTvName.setText(Session.user.getName());
		mTvSex2.setText(AppUtils.getSex(profile.getSex()));
		mTvNationality2.setText(AppUtils.getNationality(profile.getNationality()));
	}

	@Override
	protected void onDestroy() {
		mPresenter.onFinish();
		super.onDestroy();
	}
}
