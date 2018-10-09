package com.ace.member.main.third_party.lotto.lotto90;

import android.content.Context;

import com.ace.member.base.BasePresenter;
import com.ace.member.main.third_party.lotto.LottoOutstandingBean;
import com.ace.member.utils.Session;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;


public class Lotto90Presenter extends BasePresenter implements Lotto90Contract.Presenter {
	private final Lotto90Contract.View mView;

	@Inject
	public Lotto90Presenter(Lotto90Contract.View view, Context context) {
		super(context);
		this.mView = view;
	}

	public void getAmount(int marketID) {
		Map<String, String> p = new HashMap<>();
		p.put("_b", "aj");
		p.put("_a", "balance");
		p.put("cmd", "getBalanceAndOutstanding");
		p.put("market_id", String.valueOf(marketID));
		p.put("_s", Session.sSid);
		submit(p, new SimpleRequestListener() {

			@Override
			public void loadSuccess(String result, String uniqueToken) {
				try {
					LottoOutstandingBean list = JsonUtil.jsonToBean(result, LottoOutstandingBean.class);
					assert list != null;
					Session.balanceList = list.getList();
					mView.setOutstanding(list.getOutstanding());
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}
		});
	}

}
