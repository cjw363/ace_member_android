package com.ace.member.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.bean.BankAccount;
import com.ace.member.listener.IMyViewOnClickListener;
import com.ace.member.utils.ColorUtil;
import com.og.utils.CustomDialog;
import com.og.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BankViewPagerAdapter extends PagerAdapter implements View.OnClickListener {
	private Context mContext;
	private List<BankAccount> mBankAccountList;
	private IMyViewOnClickListener mClickListener;
	public static final int TYPE_1_BANK = 1;
	public static final int TYPE_2_COMPANY_BANK = 2;
	public static final int TYPE_3_MEMBER_BANK = 3;
	private int mType;
	private Map<String, Integer> mMap;
	private SparseArray<View> mViewSparseArray;
	private LayoutInflater mInflater;

	public BankViewPagerAdapter(Context context, List<BankAccount> list, IMyViewOnClickListener listener, int type) {
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		this.mBankAccountList = list;
		this.mClickListener = listener;
		this.mType = type;

		mMap = new HashMap<>();
		mMap.put("ABA", R.drawable.ic_bank_aba);
		mMap.put("ACL", R.drawable.ic_bank_acl);
		mMap.put("CBP", R.drawable.ic_bank_cbp);
		mMap.put("CIMB", R.drawable.ic_bank_cimb);
		mMap.put("FTB", R.drawable.ic_bank_ftb);
		mMap.put("MAY", R.drawable.ic_bank_may);
		mMap.put("PB", R.drawable.ic_bank_pb);
		mMap.put("SCB", R.drawable.ic_bank_scb);
		mMap.put("SPB", R.drawable.ic_bank_spb);
		mMap.put("WING", R.drawable.ic_bank_wing);
		mViewSparseArray = new SparseArray<>();
	}

	public void setBankAccountList(List<BankAccount> bankAccountList) {
		mBankAccountList = bankAccountList;
		notifyDataSetChanged();
	}

	public List<BankAccount> getBankAccountList() {
		return mBankAccountList;
	}

	public void setType(int type) {
		mType = type;
	}

	public int getType() {
		return mType;
	}

	@Override
	public int getCount() {
		return mType == TYPE_3_MEMBER_BANK ? mBankAccountList.size() + 1 : mBankAccountList.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = container.getChildAt(position);
		if (view == null) {
			view = initBankInfoView(position);
			container.addView(view);
		}
		if (mViewSparseArray.get(position) == null) mViewSparseArray.put(position, view);
		return view;
	}

	private View initBankInfoView(final int position) {
		View view = null;
		try {
			if (mType == TYPE_3_MEMBER_BANK) {
				if (position == getCount() - 1) {
					view = mInflater.inflate(R.layout.view_add_bank_item, null);
					view.setOnClickListener(this);
					return view;
				} else {
					//用户的银行有长按删除功能
					return setBankCardInfo(position, true);
				}
			}
			//公司的银行
			view = setBankCardInfo(position, false);

		} catch (Exception e) {e.printStackTrace();}

		return view;
	}

	private View setBankCardInfo(final int position, boolean canDelete) {
		View view = mInflater.inflate(R.layout.view_bank_info_item, null);
		CardView cvBackGround = (CardView) view.findViewById(R.id.cv_background);
		TextView tvBank = (TextView) view.findViewById(R.id.tv_bank);
		TextView tvAccountName = (TextView) view.findViewById(R.id.tv_account_name);
		TextView tvBankAccountNo = (TextView) view.findViewById(R.id.tv_bank_account_no);
		AppCompatImageView civBank = (AppCompatImageView) view.findViewById(R.id.iv_bank);
		String code = mBankAccountList.get(position).getCode();
		civBank.setImageResource(mMap.get(code) == null ? R.drawable.ic_bank : mMap.get(code));
		tvBank.setText(mBankAccountList.get(position).getName());
		tvAccountName.setText(mBankAccountList.get(position).getAccount_name());
		tvBankAccountNo.setText(Utils.formatAccountNo(mBankAccountList.get(position).getAccount_no()));
		cvBackGround.setCardBackgroundColor(ColorUtil.getBankBgColor(code));
		int textColor = ColorUtil.getBankTextColor(code);
		tvBank.setTextColor(textColor);
		tvAccountName.setTextColor(textColor);
		tvBankAccountNo.setTextColor(textColor);

		//卡片是否可长按删除
		if (canDelete) {
			cvBackGround.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(final View v) {
					String msg = mBankAccountList.get(position).getName() + "\n" + mBankAccountList.get(position).getAccount_name() + "\n" + Utils.formatAccountNo(mBankAccountList.get(position).getAccount_no());
					Dialog mDialog = new CustomDialog.Builder(mContext).setTitle(R.string.delete_member_bank_account).setMessage(msg).setIcon(R.drawable.ic_warining).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {
							v.setTag("delete_bank");
							mClickListener.onItemClick(mBankAccountList, v, position);
							dialogInterface.dismiss();
						}
					}).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {
							dialogInterface.dismiss();
						}
					}).create();
					mDialog.show();
					return true;
				}
			});
		}
		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
	}

	@Override
	public void onClick(View v) {
		if (mClickListener != null) {
			mClickListener.onClick(v, getItemPosition(v));
		}
	}

	public int getCurrentIndicatorColor(int position) {
		if (mType == TYPE_3_MEMBER_BANK && position == getCount() - 1)
			return ColorUtil.getBankIndicatorColor(null);
		return ColorUtil.getBankIndicatorColor(mBankAccountList.get(position).getCode());
	}
}
