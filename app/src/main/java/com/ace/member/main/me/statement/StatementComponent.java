package com.ace.member.main.me.statement;

import dagger.Component;

@Component(modules = StatementPresenterModule.class)
public interface StatementComponent {
	void inject(StatementActivity statementActivity);
}
