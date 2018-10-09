package com.ace.member.main.home.top_up.order_detail;

import com.ace.member.bean.FaceValue;
import com.ace.member.bean.PhoneCompany;
import com.ace.member.bean.TopUpOrder;
import com.ace.member.event.SelectPhoneCompanyEvent;
import com.og.utils.ItemObject;

import java.util.List;

public interface TopUpOrderDetailContract {

	interface View {
		void setTopUpOrder(TopUpOrder order);
	}

	interface Presenter {
		void getTopUpOrder(int id);
	}
}
