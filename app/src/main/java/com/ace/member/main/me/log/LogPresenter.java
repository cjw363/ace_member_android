package com.ace.member.main.me.log;

import android.content.Context;

import com.ace.member.base.BasePresenter;
import com.ace.member.bean.LogBean;
import com.og.LibSession;
import com.og.http.SimpleRequestListener;
import com.og.utils.DialogFactory;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class LogPresenter extends BasePresenter implements LogContract.LogPresenter {
	private final LogContract.LogView mLogView;
	private List<LogBean> mList = new ArrayList<>();
	private String mFlagSameDate = "";

	@Inject
	public LogPresenter(Context context, LogContract.LogView mView) {
		super(context);
		this.mLogView = mView;
		mNetClient.setUnblock(true);
	}

	@Override
	public void getLogList(int page) {
		Map<String, String> p = new HashMap<>();
		p.put("_a", "system");
		p.put("_b", "aj");
		p.put("_s", LibSession.sSid);
		p.put("page", String.valueOf(page));
		p.put("cmd", "getLogList");
		submit(p, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					LogListBean bean = JsonUtil.jsonToBean(result, LogListBean.class);
					if (bean == null) return;
					int total = bean.getTotal();
					int page = bean.getPage();
					int size = bean.getSize();
					int nextPage = Utils.nextPage(total, page, size);
					List<LogBean> list = bean.getList();
					if (page > 1) {
						setData(list);
						mLogView.showNextLogData(nextPage);
					} else {
						mList.clear();
						setData(list);
						mLogView.showLogData(nextPage, total > size);
					}
					DialogFactory.unblock();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, false);
	}

	private void setData(List<LogBean> list) {
		try {
			String date = "", onlyTime = "", strTime;
			if (list != null) {
				int len = list.size();
				for (int i = 0; i < len; i++) {
					LogBean bean = list.get(i);
					strTime = bean.getTime();
					if (strTime != null) {
						date = strTime.substring(0, 10);
						onlyTime = strTime.substring(11, 16);
					}
					bean.setTime(onlyTime);
					bean.setDate(date);
					bean.setHead(date.equals(mFlagSameDate));
					mFlagSameDate = date;
					mList.add(bean);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public List<LogBean> getLogData() {
		return mList;
	}
}
