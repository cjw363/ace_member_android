package com.ace.member.main.verify_certificate;


import com.ace.member.bean.MemberProfile;
import com.ace.member.bean.Verify;

import java.util.List;

public interface VerifyCertificateContract {
	interface View {
		void enableSvVerify(boolean enable);

		void enableBtnSubmit(boolean enable);

		void setIdType(int index);

		void setSex(int index);

		void setNationality(int index);

		String getBirthDay();

		void enableRlStatus(boolean enable);

		void enableDetail(boolean enable);

		void enableBtnRecertify(boolean enable);

		void setVerify(Verify verify);

		void setProfile(MemberProfile profile);

		void setProfile2(MemberProfile profile);
	}

	interface Presenter {
		void start();

		void onTypeSelected(int position);

		void onSexSelected(int position);

		void onNationalitySelected(int position);

		void btnSubmit(String idNumber, List<String> photos);

		void onFinish();
	}
}
