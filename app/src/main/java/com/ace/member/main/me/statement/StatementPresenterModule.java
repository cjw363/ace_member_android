package com.ace.member.main.me.statement;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class StatementPresenterModule {

	private final StatementContract.StatementView mStatementView;
	private final Context mContext;

	public StatementPresenterModule(StatementContract.StatementView mStatementView, Context mContext) {this.mStatementView = mStatementView;
		this.mContext = mContext;
	}

	@Provides
	StatementContract.StatementView providesStatementContractView(){return mStatementView;}

	@Provides
	Context providesContext(){return mContext;}
}
