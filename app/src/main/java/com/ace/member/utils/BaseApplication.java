package com.ace.member.utils;

import com.ace.member.BuildConfig;
import com.og.LibApplication;
import com.og.LibSession;

public final class BaseApplication extends LibApplication{

	@Override
	public void onCreate() {
		super.onCreate();

		initSession();
	}

	private void initSession() {
		Session.clear();
		LibSession.sDebug = BuildConfig.DEBUG;
		LibSession.sServiceUrl = BuildConfig.SERVICE_URL;
		LibSession.sUpgradeUrl = BuildConfig.UPGRADE_URL;
	}

}
