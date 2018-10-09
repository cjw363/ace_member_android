package com.ace.member.main.me.service_point;

import com.ace.member.bean.ServicePoint;

import java.util.ArrayList;

interface ServicePointContract {

	interface ServicePointView {
		void updateServicePoint();

		void setList(ArrayList<ServicePoint> branchList, ArrayList<ServicePoint> agentList);

		void setCount(int agentCount, int branchCount);

		void showInfo();
	}

	interface ServicePointPresenter {
		void getServicePoint();
	}
}
