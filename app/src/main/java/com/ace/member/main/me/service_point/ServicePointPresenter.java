package com.ace.member.main.me.service_point;

import android.content.Context;

import com.ace.member.base.BasePresenter;
import com.ace.member.bean.ServicePoint;
import com.ace.member.utils.AppGlobal;
import com.og.LibSession;
import com.og.http.SimpleRequestListener;
import com.og.utils.JsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

final class ServicePointPresenter extends BasePresenter implements ServicePointContract.ServicePointPresenter {

	private final ServicePointContract.ServicePointView mServicePointView;

	@Inject
	ServicePointPresenter(ServicePointContract.ServicePointView ServicePointView, Context context) {
		super(context);
		mServicePointView = ServicePointView;
	}

	@Override
	public void getServicePoint() {
		Map<String, String> p = new HashMap<>();
		p.put("_a", "user");
		p.put("_b", "aj");
		p.put("cmd", "getServicePoint");
		p.put("_s", LibSession.sSid);

		submit(p, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					ServicePointResultBean bean = JsonUtil.jsonToBean(result, ServicePointResultBean.class);
					if (bean != null) {
						List<ServicePoint> servicePointList = bean.getList();
						if (servicePointList != null) {
							ArrayList<ServicePoint> branchList = new ArrayList<>();
							ArrayList<ServicePoint> agentList = new ArrayList<>();
							for (int i = 0; i < servicePointList.size(); i++) {
								ServicePoint point = servicePointList.get(i);
								int type = point.getSiteType();
								if (AppGlobal.SITE_TYPE_BRANCH == type) {
									branchList.add(point);
								} else if (AppGlobal.SITE_TYPE_AGENT == type) {
									agentList.add(point);
								}
							}
							mServicePointView.setList(branchList, agentList);
							mServicePointView.updateServicePoint();
						}
						int agentCount = bean.getAgentCount();
						int branchCount = bean.getBranchCount();
						mServicePointView.setCount(agentCount, branchCount);
						mServicePointView.showInfo();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
