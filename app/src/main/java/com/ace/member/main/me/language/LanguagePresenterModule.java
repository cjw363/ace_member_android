package com.ace.member.main.me.language;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class LanguagePresenterModule {
	private final LanguageContract.LanguageView mLanguageView;
	private final Context mContext;

	public LanguagePresenterModule(LanguageContract.LanguageView mLanguageView, Context mContext) {this.mLanguageView = mLanguageView;
		this.mContext = mContext;
	}

	@Provides
	LanguageContract.LanguageView providesLanguageContractView(){return mLanguageView;}

	@Provides
	Context providesContext(){return mContext;}

}
