package com.ace.member.main.verify_certificate;


import dagger.Component;

@Component(modules = VerifyCertificatePresenterModule.class)
public interface VerifyCertificateComponent {
	void inject(VerifyCertificateActivity activity);
}
