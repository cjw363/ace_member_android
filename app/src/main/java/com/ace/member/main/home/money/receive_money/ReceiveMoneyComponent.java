package com.ace.member.main.home.money.receive_money;


import dagger.Component;

@Component(modules = ReceiveMoneyPresenterModule.class)
public interface ReceiveMoneyComponent {
	void inject(ReceiveMoneyActivity activity);
}

