package com.ace.member.main.home.transfer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.ace.member.R;
import com.ace.member.adapter.TransferRecentAdapter;
import com.ace.member.base.BaseActivity;
import com.ace.member.main.home.transfer.to_member.ToMemberActivity;
import com.ace.member.main.home.transfer.to_merchant.ToMerchantActivity;
import com.ace.member.main.home.transfer.to_non_member.ToNonMemberActivity;
import com.ace.member.main.home.transfer.to_partner.ToPartnerActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.og.component.CustomSlidingRefreshListView;
import com.og.utils.DateUtils;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;


public class TransferActivity extends BaseActivity implements TransferContract.TransferView, View.OnClickListener, CustomSlidingRefreshListView.IXListViewListener {

	@Inject
	TransferPresenter mTransferPresenter;
	@BindView(R.id.ll_to_member)
	LinearLayout mLlToMember;
	@BindView(R.id.ll_to_non_member)
	LinearLayout mLlToNonMember;
	@BindView(R.id.lv_recent)
	CustomSlidingRefreshListView mLvRecent;
	@BindView(R.id.ll_to_merchant)
	LinearLayout mLlToMerchant;
	@BindView(R.id.v_merchant_line)
	View mVMerchantLine;
	@BindView(R.id.v_partner_line)
	View mVPartnerLine;
	@BindView(R.id.ll_to_partner)
	LinearLayout mLlToPartner;

	public TransferRecentAdapter mAdapter;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerTransferComponent.builder().transferPresenterModule(new TransferPresenterModule(this, this)).build().inject(this);
		init();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_transfer;
	}

	@Override
	protected void onStart() {
		super.onStart();
		mTransferPresenter.getRecentList();
		mTransferPresenter.checkTransfer();
	}

	private void init() {
		ToolBarConfig.builder(this, null).setTvTitleRes(R.string.transfer).build();
		mLlToMember.setOnClickListener(this);
		mLlToNonMember.setOnClickListener(this);
		mLlToMerchant.setOnClickListener(this);
		mLlToPartner.setOnClickListener(this);

		mLvRecent.setPullLoadEnable(false);
		mLvRecent.setXListViewListener(this);
		mLvRecent.setRefreshTime(DateUtils.getFormatDataTime());
		mLvRecent.setPullRefreshEnable(false);
		mLvRecent.hideLoadMore();
		mLvRecent.setPullRefreshEnable(true);
	}

	@Override
	public void onClick(View v) {
		Intent it = null;
		try {
			switch (v.getId()) {
				case R.id.ll_to_member:
					it = new Intent(this, ToMemberActivity.class);
					it.putExtra("phone", "");
					startActivity(it);
					break;
				case R.id.ll_to_non_member:
					it = new Intent(this, ToNonMemberActivity.class);
					it.putExtra("phone", "");
					startActivity(it);
					break;
				case R.id.ll_to_merchant:
					it = new Intent(this, ToMerchantActivity.class);
					it.putExtra("phone", "");
					startActivity(it);
					break;
				case R.id.ll_to_partner:
					it = new Intent(this, ToPartnerActivity.class);
					it.putExtra("phone", "");
					startActivity(it);
					break;
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}

	}

	@Override
	public void onRefresh() {
		mTransferPresenter.getRecentList();
	}

	@Override
	public void onLoadMore() {
		mTransferPresenter.getRecentList();
	}

	private void onLoad() {
		mLvRecent.stopRefresh();
		mLvRecent.stopLoadMore();
	}

	@Override
	public void showRecentData(List<TransferBean> list) {
		try {
			mLvRecent.setRefreshTime(DateUtils.getFormatDataTime());
			if (list == null || list.size() == 0) Utils.showToast(R.string.no_data);
			mAdapter = new TransferRecentAdapter(list, mContext);
			mLvRecent.setAdapter(mAdapter);
			mLvRecent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					try {
						TransferBean bean = mAdapter.getTransferBean(position - 1);
						String phone = bean.getPhone();
						int type = bean.getType();
						Intent intent = null;
						if (type == AppGlobal.USER_TYPE_1_MEMBER) {
							intent = new Intent(TransferActivity.this, ToMemberActivity.class);
						} else if (type == AppGlobal.USER_TYPE_3_PARTNER) {
							intent = new Intent(TransferActivity.this, ToPartnerActivity.class);
						} else if (type == AppGlobal.USER_TYPE_4_MERCHANT) {
							intent = new Intent(TransferActivity.this, ToMerchantActivity.class);
						} else {
							intent = new Intent(TransferActivity.this, ToNonMemberActivity.class);
						}
						intent.putExtra("phone", phone);
						startActivity(intent);
					} catch (Exception e) {
						FileUtils.addErrorLog(e);
					}
				}
			});
			onLoad();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void showFunction(int isRelateMerchant, int isRelatePartner) {
		if (isRelateMerchant == 1) {
			mLlToMerchant.setVisibility(View.VISIBLE);
			mVMerchantLine.setVisibility(View.VISIBLE);
		} else {
			mLlToMerchant.setVisibility(View.GONE);
			mVMerchantLine.setVisibility(View.GONE);
		}

		if (isRelatePartner == 1) {
			mLlToPartner.setVisibility(View.VISIBLE);
			mVPartnerLine.setVisibility(View.VISIBLE);
		} else {
			mLlToPartner.setVisibility(View.GONE);
			mVPartnerLine.setVisibility(View.GONE);
		}
	}
}
