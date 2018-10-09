package com.ace.member.main.me.about;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.og.utils.FileUtils;

import javax.inject.Inject;

import butterknife.BindView;


public class AboutActivity extends BaseActivity implements AboutContract.AboutView {

	@Inject
	AboutPresenter mAboutPresenter;
	@BindView(R.id.tv_phone)
	TextView mTvPhone;
	@BindView(R.id.tv_website)
	TextView mTvWebsite;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerAboutComponent.builder()
			.aboutPresenterModule(new AboutPresenterModule(this, this))
			.build()
			.inject(this);
		initActivity();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_about;
	}

	public void initActivity() {
		ToolBarConfig.builder(this, null).setTvTitleRes(R.string.about).build();
		mTvPhone.setText(getClickableSpan());
		mTvPhone.setMovementMethod(LinkMovementMethod.getInstance());
	}

	private SpannableString getClickableSpan() {
		SpannableString spanStr = null;//   098 666 678 / 099 666 678 / 071 6666 078
		try {
			spanStr = new SpannableString(getResources().getString(R.string.hot_line_phone));
			//设置下划线文字
			spanStr.setSpan(new UnderlineSpan(), 0, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			//设置文字的单击事件
			spanStr.setSpan(new ClickableSpan() {

				@Override
				public void onClick(View widget) {
					//这个tel：必须要加上，表示我要打电话。否则不会有打电话功能，由于在打电话清单文件里设置了这个“协议”
					Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "+855 98 666 678"));
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
			}, 0, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			//设置文字的前景色
			spanStr.setSpan(new ForegroundColorSpan(Color.BLUE), 0, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			//设置下划线文字
			spanStr.setSpan(new UnderlineSpan(), 14, 25, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			//设置文字的单击事件
			spanStr.setSpan(new ClickableSpan() {

				@Override
				public void onClick(View widget) {

					Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "+855 99 666 678"));
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
			}, 14, 25, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			//设置文字的前景色
			spanStr.setSpan(new ForegroundColorSpan(Color.BLUE), 14, 25, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

			spanStr.setSpan(new UnderlineSpan(), 28, 40, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			//设置文字的单击事件
			spanStr.setSpan(new ClickableSpan() {

				@Override
				public void onClick(View widget) {

					Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "+855 71 6666 078"));
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
			}, 28, 40, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			//设置文字的前景色
			spanStr.setSpan(new ForegroundColorSpan(Color.BLUE), 28, 40, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		} catch (Resources.NotFoundException e) {
			FileUtils.addErrorLog(e);
		}
		return spanStr;
	}

}
