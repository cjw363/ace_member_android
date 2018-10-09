package com.ace.member.main.home.receive_to_acct.detail;

import android.content.Context;
import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.main.home.receive_to_acct.R2ADataBeen;
import com.og.LibSession;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public class R2ADetailPresenter extends BasePresenter implements R2ADetailContract.R2ADetailPresenter {

	private final R2ADetailContract.R2ADetailView mView;

	@Inject
	public R2ADetailPresenter(Context context, R2ADetailContract.R2ADetailView mView) {
		super(context);
		this.mView = mView;
	}

	public void getRecentDetail(int id) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "transfer");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("cmd", "getTransferCashDetailByID");
		params.put("id", String.valueOf(id));
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					R2ADataBeen been = JsonUtil.jsonToBean(result, R2ADataBeen.class);
					mView.setRecentDetail(been);
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}

			@Override
			public void loadFailure(int code, String result, String token) {
				Utils.showToast(R.string.fail);
			}
		}, false);
	}
}
