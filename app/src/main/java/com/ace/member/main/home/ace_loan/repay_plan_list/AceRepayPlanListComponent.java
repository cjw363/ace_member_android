package com.ace.member.main.home.ace_loan.repay_plan_list;

import dagger.Component;

@Component(modules = AceRepayPlanListPresenterModule.class)
public interface AceRepayPlanListComponent {
	void inject(AceRepayPlanListActivity aceRepayPlanListActivity);
}
