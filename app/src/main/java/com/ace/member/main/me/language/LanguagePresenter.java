package com.ace.member.main.me.language;

import android.content.Context;

import com.ace.member.base.BasePresenter;

import javax.inject.Inject;


public class LanguagePresenter extends BasePresenter implements LanguageContract.LanguagePresenter{

	private final LanguageContract.LanguageView mLanguageView;

	@Inject
	public LanguagePresenter(Context context, LanguageContract.LanguageView mLanguageView) {
		super(context);
		this.mLanguageView = mLanguageView;
	}
}
