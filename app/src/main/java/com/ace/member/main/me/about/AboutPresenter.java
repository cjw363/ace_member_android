package com.ace.member.main.me.about;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.ace.member.BuildConfig;
import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.og.LibGlobal;
import com.og.event.MessageEvent;
import com.og.http.OkHttpClientManager;
import com.og.utils.DialogFactory;
import com.og.utils.NetUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import javax.inject.Inject;

import okhttp3.Request;


public class AboutPresenter extends BasePresenter implements AboutContract.AboutPresenter{

	private final AboutContract.AboutView mAboutView;

	@Inject
	public AboutPresenter(Context context, AboutContract.AboutView mAboutView) {
		super(context);
		this.mAboutView = mAboutView;
	}

}
