package com.ace.member.main.third_party.edc.recent_contact;

import android.content.Context;

import com.ace.member.base.BasePresenter;
import com.ace.member.bean.EdcBill;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.Session;
import com.google.gson.reflect.TypeToken;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;


public class EdcRecentContactPresenter extends BasePresenter implements EdcRecentContactContract.Presenter {
	private final EdcRecentContactContract.View mView;


	@Inject
	public EdcRecentContactPresenter(EdcRecentContactContract.View view, Context context) {
		super(context);
		this.mView = view;
	}

	@Override
	public void start() {
		getRecentContact();
	}

	private void getRecentContact() {
		Map<String, String> params = new HashMap<>();
		params.put("_s", Session.sSid);
		params.put("_a", "payment");
		params.put("_b", "aj");
		params.put("cmd","getEdcRecent");
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					List<EdcBill> list = JsonUtil.jsonToList(result, new TypeToken<List<EdcBill>>() {});
					if (list == null) list = new ArrayList<>();
					mView.enableEmpty(list.size()==0);
					mView.setList(list);
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
					mView.enableEmpty(true);
					e.printStackTrace();
				}
			}

			@Override
			public void loadFailure(int errorCode, String result, String uniqueToken) {
				mView.enableEmpty(true);
			}
		}, false);
	}
}
