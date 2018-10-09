package com.ace.member.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTabHost;
import android.text.TextUtils;
import android.widget.PopupWindow;
import android.widget.TabHost;

import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.main.friends.FriendsFragment;
import com.ace.member.main.home.HomeFragment;
import com.ace.member.main.me.MeFragment;
import com.ace.member.main.third_party.ThirdPartyFragment;
import com.ace.member.service.IMWebSocketService;
import com.ace.member.service.WebSocketService;
import com.ace.member.utils.Session;
import com.ace.member.view.TabItem;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MainActivity extends BaseActivity implements MainContract.MainView {
	@Inject
	MainPresenter mMainPresenter;
	private List<TabItem> mTabItems;

	public PopupWindow mPopupWindow;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		DaggerMainComponent.builder().mainPresenterModule(new MainPresenterModule(this, this)).build().inject(this);
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_main;
	}

	private void init() {
		startIMWebSocketService();
		final FragmentTabHost fragmentTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		fragmentTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
		initTabData();
		TabItem tabItem;
		TabHost.TabSpec tabSpec;
		for (int i = 0, n = mTabItems.size(); i < n; i++) {
			tabItem = mTabItems.get(i);
			tabSpec = fragmentTabHost.newTabSpec(tabItem.getTitle()).setIndicator(tabItem.getView());
			fragmentTabHost.addTab(tabSpec, tabItem.getFragmentClass(), tabItem.getBundle());
		}
		mTabItems.get(3).setEnableDot(Session.hasNewVersion);
	}

	private void startIMWebSocketService() {//登陆成功后启动websocketservice
		try {
			stopService(new Intent(this, IMWebSocketService.class));
			if (Session.socketServers != null) {
				String imHost = Session.socketServers.getIm();
				if (!TextUtils.isEmpty(imHost)) {
					Intent intent = new Intent(this, IMWebSocketService.class);
					Bundle bundle = new Bundle();
					bundle.putString("socket_host", imHost);
					intent.putExtras(bundle);
					startService(intent);
				}
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
	}

	private void initTabData() {
		mTabItems = new ArrayList<>(4);
		mTabItems.add(new TabItem.Builder(HomeFragment.class).setSelectorRes(R.drawable.sel_home).setTitleRes(R.string.home).build());
		mTabItems.add(new TabItem.Builder(FriendsFragment.class).setSelectorRes(R.drawable.sel_friends).setTitleRes(R.string.friends).build());
		mTabItems.add(new TabItem.Builder(ThirdPartyFragment.class).setSelectorRes(R.drawable.sel_service).setTitleRes(R.string.service).build());
		mTabItems.add(new TabItem.Builder(MeFragment.class).setSelectorRes(R.drawable.sel_me).setTitleRes(R.string.me).build());
	}

	private long lastTime = 0;

	@Override
	public void onBackPressed() {
		long currentTime = System.currentTimeMillis();
		if (lastTime != 0 && currentTime - lastTime <= 2000) {
			stopService(new Intent(this, WebSocketService.class));
			exitApp();
			//			finish();
			//			Process.killProcess(LibApplication.getMyPid());
		} else {
			lastTime = currentTime;
			Utils.showToast(R.string.double_click_to_exit_the_program);
		}
	}
}
