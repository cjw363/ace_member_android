package com.ace.member.main.friends.fragment;

import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.ace.member.R;
import com.ace.member.adapter.GVEmotionAdapter;
import com.ace.member.adapter.EmotionViewPagerAdapter;
import com.ace.member.base.BaseFragment;
import com.ace.member.utils.EmotionUtils;
import com.ace.member.utils.GlobalOnItemClickManagerUtils;
import com.ace.member.view.IndicatorView;
import com.datepicker.utils.MeasureUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class EmotionFragment extends BaseFragment {
	@BindView(R.id.viewPager_fragment_emotion)
	ViewPager mViewPager;
	@BindView(R.id.group_fragment_emotion)
	IndicatorView mIndicatorView;

	private EmotionViewPagerAdapter mAdapter;

	@Override
	protected int getContentViewLayout() {
		return R.layout.fragment_chat_emotion;
	}

	@Override
	protected void initView() {
		initWidget();
		initEmotion();
	}


	private void initWidget() {
		mViewPager.setCurrentItem(0);
		mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			int oldPagerPos = 0;

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				mIndicatorView.playByStartPointToNext(oldPagerPos, position);
				oldPagerPos = position;
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
	}

	private void initEmotion() {
		// 获取屏幕宽度
		int screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
		// item的间距
		int spacing = MeasureUtil.dp2px(getActivity(), 12);
		// 动态计算item的宽度和高度
		int itemWidth = (screenWidth - spacing * 8) / 7;
		//动态计算gridview的总高度
		int gvHeight = itemWidth * 3 + spacing * 6;

		List<GridView> emotionViews = new ArrayList<>();
		List<String> emotionNames = new ArrayList<>();
		// 遍历所有的表情的key
		for (String emojiName : EmotionUtils.EMOTION_STATIC_MAP.keySet()) {
			emotionNames.add(emojiName);
			// 每20个表情作为一组,同时添加到ViewPager对应的view集合中
			if (emotionNames.size() == 23) {
				GridView gv = createEmotionGridView(emotionNames, screenWidth, spacing, itemWidth, gvHeight);
				emotionViews.add(gv);
				// 添加完一组表情,重新创建一个表情名字集合
				emotionNames = new ArrayList<>();
			}
		}

		// 判断最后是否有不足23个表情的剩余情况
		if (emotionNames.size() > 0) {
			GridView gv = createEmotionGridView(emotionNames, screenWidth, spacing, itemWidth, gvHeight);
			emotionViews.add(gv);
		}

		//初始化指示器
		mIndicatorView.initIndicator(emotionViews.size());
		// 将多个GridView添加显示到ViewPager中
		mAdapter = new EmotionViewPagerAdapter(emotionViews);
		mViewPager.setAdapter(mAdapter);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth, gvHeight);
		mViewPager.setLayoutParams(params);


	}

	/**
	 * 创建显示表情的GridView
	 */
	private GridView createEmotionGridView(List<String> emotionNames, int gvWidth, int padding, int itemWidth, int gvHeight) {
		// 创建GridView
		GridView gv = new GridView(getActivity());
		//设置点击背景透明
		gv.setSelector(android.R.color.transparent);
		//设置7列
		gv.setNumColumns(8);
		gv.setPadding(padding, padding, padding, padding);
		gv.setHorizontalSpacing(padding);
		gv.setVerticalSpacing(padding * 2);
		//设置GridView的宽高
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(gvWidth, gvHeight);
		gv.setLayoutParams(params);
		// 给GridView设置表情图片
		GVEmotionAdapter adapter = new GVEmotionAdapter(getActivity(), emotionNames, itemWidth);
		gv.setAdapter(adapter);
		//设置全局点击事件
		gv.setOnItemClickListener(GlobalOnItemClickManagerUtils.getInstance(getActivity()).getOnItemClickListener());
		return gv;
	}
}
