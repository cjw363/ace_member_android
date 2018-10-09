package com.ace.member.main.verify_certificate;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class VerifyCertificatePresenterModule {

	private final VerifyCertificateContract.View mView;
	private final Context mContext;

	public VerifyCertificatePresenterModule(VerifyCertificateContract.View view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	VerifyCertificateContract.View provideView() {
		return mView;
	}

	@Provides
	Context providesContext() {
		return mContext;
	}
}
