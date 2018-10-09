package com.ace.member.main;

import android.content.Context;

import com.ace.member.base.BasePresenter;

import javax.inject.Inject;

final class MainPresenter extends BasePresenter implements MainContract.MainPresenter {

	private final MainContract.MainView mMainView;

	@Inject
	public MainPresenter(Context context, MainContract.MainView mainView) {
		super(context);
		mMainView = mainView;
	}
}
