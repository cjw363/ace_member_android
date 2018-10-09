package com.ace.member.main.third_party.edc;


import android.support.v7.app.AppCompatActivity;

import com.ace.member.main.image_detail.Image;

import java.util.List;

public interface EdcContract {
	interface View {
		void setSelectType(int type);

		void setSelectTypeText(String s);

		void setSelectTypeTextColor(int color);

		void enablePbCompress(boolean enable);

		void setAmount(String amount);

		void setFee(String fee);

		void enableFee(boolean enable);

		void setPhone(String phone);

		void enablePhone(boolean enable);

		void resetInterface();

		void enableBtnSubmit(boolean enable);

		void toPaymentHistoryDetailActivity(int billId);

		AppCompatActivity getActivity();
	}

	interface Presenter {
		void start();

		void updateFee(String amount);

		void submitEdcWsa(int edcType, String consumer, String phone, String amount, String fee, String remark, List<Image> list);

		String[] getEdcWsaTypeArr();

		void onSelectType(int position);
	}
}
