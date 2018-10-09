package com.ace.member.main.third_party.wsa;


import android.support.v7.app.AppCompatActivity;

import com.ace.member.main.image_detail.Image;

import java.util.List;

public interface WsaContract {
	interface View {
		void setSelectType(int type);

		void setSelectTypeText(String s);

		void setSelectTypeTextColor(int color);

		void enablePbCompress(boolean enable);

		void setAmount(String amount);

		void setFee(String fee);

		void enableFee(boolean enable);

		void setNumberTitle(String title);

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

		void submitWsa(int type, String number, String phone, String amount, String fee, String remark, List<Image> list);

		String[] getWsaTypeArr();

		void onSelectType(int position);
	}
}
