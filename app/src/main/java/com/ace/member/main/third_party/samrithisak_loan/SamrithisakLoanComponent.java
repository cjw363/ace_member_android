package com.ace.member.main.third_party.samrithisak_loan;

import dagger.Component;

@Component(modules = SamrithisakLoanPresenterModule.class)
public interface SamrithisakLoanComponent {
	void inject(SamrithisakLoanActivity activity);
}
