package com.ace.member.main.third_party;


import android.content.Context;

import com.ace.member.base.BasePresenter;

import javax.inject.Inject;

public class ThirdPartyPresenter extends BasePresenter implements ThirdPartyContract.Presenter {

	private final ThirdPartyContract.View mView;

	@Inject
	public ThirdPartyPresenter(ThirdPartyContract.View view, Context context) {
		super(context);
		mView = view;
	}
}
