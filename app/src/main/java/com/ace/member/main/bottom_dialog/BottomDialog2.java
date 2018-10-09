package com.ace.member.main.bottom_dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.adapter.RVBottomDialogAdapter2;
import com.ace.member.listener.IMyViewOnClickListener;
import com.ace.member.simple_listener.SimpleViewClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BottomDialog2 extends BottomSheetDialogFragment implements View.OnClickListener {
	private BottomController2 mController;
	private Unbinder mUnBinder;
	@BindView(R.id.fl_close)
	FrameLayout mFlClose;
	@BindView(R.id.rl_title)
	RelativeLayout mRlTitle;
	@BindView(R.id.tv_title)
	TextView mTvTitle;
	@BindView(R.id.rv_content)
	RecyclerView mRvContent;

	public BottomDialog2() {
		mController = new BottomController2();
	}

	private void setContext(Context context) {
		mController.setContext(context);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.dlg_bottom_common, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		mUnBinder = ButterKnife.bind(this, view);
		CharSequence title = mController.getTvTitle();
		if (TextUtils.isEmpty(title)) mRlTitle.setVisibility(View.GONE);
		else mTvTitle.setText(title);
		mFlClose.setOnClickListener(this);
		RVBottomDialogAdapter2 adapter = new RVBottomDialogAdapter2(mController.getContext(), mController
				.getList(), mController.getType(), mController.getTvContentColorRes(), mController.getTvContent2Weight());
		adapter.setClickListener(new SimpleViewClickListener() {
			@Override
			public void onClick(View view, int position) {
				dismiss();
				if (mController.getClickListener() != null) {
					mController.getClickListener().onClick(view, position);
				}
			}
		});
		mRvContent.setAdapter(adapter);
		if (mController.getBottomHeight() > 0)
			mRvContent.getLayoutParams().height = mController.getBottomHeight();
		mRvContent.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.fl_close:
				dismiss();
				break;
		}
	}

	public static class Builder {
		private final BottomController2.BottomParams P;

		public Builder(FragmentActivity activity) {
			P = new BottomController2.BottomParams(activity);
		}

		public BottomDialog2.Builder setList(List<ControllerBean> list) {
			P.mList = list;
			return this;
		}

		public BottomDialog2.Builder setTvContent2Weight(int tvContent2Weight) {
			P.mTvContent2Weight = tvContent2Weight;
			return this;
		}

		public BottomDialog2.Builder setTvContentColor(int colorRes) {
			P.mTvContentColorRes = colorRes;
			return this;
		}

		public BottomDialog2.Builder setTvTitle(int res) {
			P.mTvTitle = P.mContext.getString(res);
			return this;
		}

		public BottomDialog2.Builder setTvTitle(CharSequence tvTitle) {
			P.mTvTitle = tvTitle;
			return this;
		}

		public BottomDialog2.Builder setType(int type) {
			P.mType = type;
			return this;
		}

		public BottomDialog2.Builder setBottomHeight(int height) {
			P.mBottomHeight = height;
			return this;
		}

		public BottomDialog2.Builder setClickListener(IMyViewOnClickListener clickListener) {
			P.mClickListener = clickListener;
			return this;
		}

		public BottomDialog2 createAndShow() {
			BottomDialog2 bottomDialog = new BottomDialog2();
			bottomDialog.setContext(P.mContext);
			P.apply(bottomDialog.mController);
			bottomDialog.show(P.mFragmentManager, "BottomDialog2");
			return bottomDialog;
		}

		public BottomDialog2 create() {
			BottomDialog2 bottomDialog = new BottomDialog2();
			bottomDialog.setContext(P.mContext);
			P.apply(bottomDialog.mController);
			return bottomDialog;
		}
	}

	public void show() {
		show(mController.getFragmentManager(), "BottomDialog2");
	}

	@Override
	public void onDestroy() {
		try {
			if (mUnBinder != null) mUnBinder.unbind();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}
}
