package com.ace.member.main.image_detail;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;

import com.ace.member.R;
import com.ace.member.base.BaseFragment;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.M;
import com.ace.member.view.SafeViewPager;
import com.og.utils.EventBusUtil;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class ImageDetailFragment extends BaseFragment implements View.OnClickListener {

	public static final String TAG = "ImageDetailFragment";
	private List<String> mList;
	private int mCurrent;
	@BindView(R.id.rl_toolbar)
	RelativeLayout mRlToolbar;
	@BindView(R.id.vp)
	public SafeViewPager mViewPager;
	public ImageDetailAdapter mImageDetailAdapter;
	private boolean mEnableMenu;
	private ToolBarConfig mToolBarConfig;

	public ImageDetailFragment() {
		Bundle bundle = new Bundle();
		bundle.putString(M.ImagePicker.FRAGMENT_NAME, TAG);
		this.setArguments(bundle);
	}

	@Override
	public int getContentViewLayout() {
		return R.layout.fragment_image_detail;
	}

	@Override
	public void initView() {
		try {
			if (mEnableMenu) {
				mRlToolbar.setVisibility(View.VISIBLE);
				ToolBarConfig.Builder builder = new ToolBarConfig.Builder(getActivity(), null);
				mToolBarConfig = builder.setEnableMenu(true)
					.setMenuType(ToolBarConfig.MenuType.MENU_IMAGE)
					.setIvMenuRes(R.drawable.ic_delete_image)
					.setMenuListener(this)
					.build();
				mToolBarConfig.setTitle(mCurrent + 1 + "/" + mList.size());
			}

			mImageDetailAdapter = new ImageDetailAdapter(getChildFragmentManager(), mList);
			mViewPager.setAdapter(mImageDetailAdapter);
			mViewPager.setCurrentItem(mCurrent);

			if (mEnableMenu)
				mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
						int current = positionOffset >= 0.5f ? position + 1 : position;
						mToolBarConfig.setTitle(current + 1 + "/" + mList.size());
					}
				});
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@Override
	protected void initData() {
		try {
			mEnableMenu = this.getArguments()
				.getBoolean(M.ImagePicker.OPEN_MENU, false);
			mList = this.getArguments()
				.getStringArrayList(M.ImagePicker.IMAGES);
			mCurrent = this.getArguments()
				.getInt(M.ImagePicker.CURRENT);
			if (mList == null) mList = new ArrayList<>();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ll_toolbar_menu:
				try {
					int current = mViewPager.getCurrentItem();
					EventBusUtil.post(new DeleteImageEvent(current));
					mImageDetailAdapter.getList()
						.remove(current);
					if (Utils.isEmptyList(mImageDetailAdapter.getList())) {
						getActivity().onBackPressed();
					} else {
						mImageDetailAdapter.notifyDataSetChanged();
					}
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
				break;
		}
	}
}
