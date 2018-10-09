package com.ace.member.main.third_party.lotto;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ace.member.R;
import com.ace.member.base.LottoBaseActivity;
import com.ace.member.main.third_party.lotto.lotto90.Lotto90Activity;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.view.ViewPagerScroller;
import com.og.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class LottoActivity extends LottoBaseActivity implements ViewPager.OnPageChangeListener {

	@BindView(R.id.vp_ad)
	ViewPager mVpAdvert;

	@BindView(R.id.ll_point)
	LinearLayout llPointGroup;

	AdvertViewPagerAdapter mAdvertViewPagerAdapter;

	private List<View> mAdImageList;
	private int previousPointPosition = 0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initActivity();
		initDate();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_lotto;
	}

	@Override
	protected void initActivity() {
		ToolBarConfig.builder(this, null)
			.setTvTitleRes(R.string.shanghai_lotto)
			.setBackgroundRes(R.color.clr_lotto_head)
			.build();

		mAdImageList = new ArrayList<>();
		mAdImageList.add(setBankCardInfo(R.drawable.ad_lotto90));
		mAdImageList.add(setBankCardInfo(R.drawable.ad_lotto639));
		mAdImageList.add(setBankCardInfo(R.drawable.ad_lotto649));
		mAdImageList.add(setBankCardInfo(R.drawable.ad_lotto18));
		mAdImageList.add(setBankCardInfo(R.drawable.ad_lotto27));
		mAdImageList.add(setBankCardInfo(R.drawable.ad_lotto4d));

		for (int i = 0; i < mAdImageList.size(); i++) {
			View view = new View(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Utils.getDimenDp(R.dimen.width5), Utils
				.getDimenDp(R.dimen.width5));
			view.setLayoutParams(params);
			params.leftMargin = Utils.getDimenDp(R.dimen.width10);
			view.setBackgroundResource(R.drawable.point_bg);
			view.setEnabled(i == 0);
			llPointGroup.addView(view);
		}

		ViewPagerScroller scroller = new ViewPagerScroller(this);
		scroller.setScrollDuration(1500);
		scroller.initViewPagerScroll(mVpAdvert);//这个是设置切换过渡时间为1.5秒
	}

	private View setBankCardInfo(int imageID) {
		View view = LayoutInflater.from(mContext).inflate(R.layout.view_advert_info_item, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.img);
		imageView.setImageDrawable(Utils.getDrawable(imageID));
		return view;
	}

	protected void initDate() {
		mAdvertViewPagerAdapter = new AdvertViewPagerAdapter();
		mVpAdvert.setAdapter(mAdvertViewPagerAdapter);
		//得放在设置适配器之后
		int currentItem = Integer.MAX_VALUE / 2;
		currentItem -= currentItem % mAdImageList.size();
		mVpAdvert.setCurrentItem(currentItem);
		sendScrollMessage(5000);// 自动切换启动

		//ViewPager 触摸事件
		mVpAdvert.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN) {
					mIsStop = true;
				} else if (action == MotionEvent.ACTION_UP) {
					mIsStop = false;
				}
				return false;
			}
		});

		mVpAdvert.addOnPageChangeListener(this);
	}

	private final int SCROLL_WHAT = 1;
	private boolean mIsStop = false;// 焦点图触摸暂停监听
	private int currentItem = 0;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case SCROLL_WHAT:
					if (currentItem == mAdvertViewPagerAdapter.getCount()) {
						currentItem = 0;
					}
					//点击图片时，自动切换暂停
					if (!mIsStop) {
						int newCurrentItem = mVpAdvert.getCurrentItem() + 1;
						mVpAdvert.setCurrentItem(newCurrentItem);
					}
					sendScrollMessage(5000);
					break;
			}
		}
	};

	private void sendScrollMessage(long delayTimeInMills) {
		handler.removeMessages(SCROLL_WHAT);
		handler.sendEmptyMessageDelayed(SCROLL_WHAT, delayTimeInMills);
	}

	@OnClick({R.id.menu_shr_90, R.id.menu_shr_6_39, R.id.menu_shr_6_49, R.id.menu_shr_18, R.id.menu_shr_27, R.id.menu_shr_4d})
	public void OnClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.menu_shr_90:
				startActivity(new Intent(this, Lotto90Activity.class));
				break;
			case R.id.menu_shr_6_39:
				Utils.showToast("639");
				break;
			case R.id.menu_shr_6_49:
				Utils.showToast("649");
				break;
			case R.id.menu_shr_18:
				Utils.showToast("18");
				break;
			case R.id.menu_shr_27:
				Utils.showToast("27");
				break;
			case R.id.menu_shr_4d:
				Utils.showToast("4D");
				break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mIsStop = true;
		handler.removeCallbacksAndMessages(null);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int position) {
		int newPosition = position % mAdImageList.size();
		llPointGroup.getChildAt(previousPointPosition).setEnabled(false);
		llPointGroup.getChildAt(newPosition).setEnabled(true);

		previousPointPosition = newPosition;
	}

	private class AdvertViewPagerAdapter extends PagerAdapter {

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
			int newPosition = position % mAdImageList.size();
			container.removeView(mAdImageList.get(newPosition));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			int newPosition = position % mAdImageList.size();
			mVpAdvert.addView(mAdImageList.get(newPosition));
			return mAdImageList.get(newPosition);
		}
	}
}
