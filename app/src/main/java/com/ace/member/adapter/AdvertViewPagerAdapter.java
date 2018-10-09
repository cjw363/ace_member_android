package com.ace.member.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ace.member.R;
import com.ace.member.listener.IMyViewOnClickListener;
import com.og.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class AdvertViewPagerAdapter extends PagerAdapter {
	private Context mContext;
	private List<Integer> mAdImageList;
	private IMyViewOnClickListener mClickListener;
	private int mType;
	private SparseArray<View> mViewSparseArray;
	private LayoutInflater mInflater;

	public AdvertViewPagerAdapter(Context context, IMyViewOnClickListener listener, int type) {
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		this.mAdImageList = new ArrayList<>();
		mAdImageList.add(R.drawable.ad_lotto90);
		mAdImageList.add(R.drawable.ad_lotto639);
		mAdImageList.add(R.drawable.ad_lotto649);
		mAdImageList.add(R.drawable.ad_lotto18);
		mAdImageList.add(R.drawable.ad_lotto27);
		mAdImageList.add(R.drawable.ad_lotto4d);
		this.mClickListener = listener;
		this.mType = type;
		mViewSparseArray = new SparseArray<>();
	}

	public void setType(int type) {
		mType = type;
	}

	public int getType() {
		return mType;
	}
//
//	@Override
//	public Object instantiateItem(ViewGroup container, final int position) {
//		try {
//			View view = container.getChildAt(position);
//			if (view == null) {
//				view = setBankCardInfo(position);
//				container.addView(view);
//			}
//			view.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					mClickListener.onClick(v, position);
//				}
//			});
//			if (mViewSparseArray.get(position) == null) mViewSparseArray.put(position, view);
//			return view;
//		} catch (Exception e) {
//			FileUtils.addErrorLog(e);
//		}
//		return null;
//	}

	private View setBankCardInfo(int position) {
		View view = mInflater.inflate(R.layout.view_advert_info_item, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.img);
		imageView.setImageDrawable(Utils.getDrawable(mAdImageList.get(position)));
		return view;
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		int newPosition = position%mAdImageList.size();
		container.removeView(setBankCardInfo(newPosition));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		int newPosition = position % mAdImageList.size();
		return setBankCardInfo(newPosition);
	}
}
