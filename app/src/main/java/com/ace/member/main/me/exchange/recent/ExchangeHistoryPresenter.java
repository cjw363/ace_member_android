package com.ace.member.main.me.exchange.recent;

import android.content.Context;

import com.ace.member.base.BasePresenter;
import com.og.LibSession;
import com.og.http.SimpleRequestListener;
import com.og.utils.DialogFactory;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;


class ExchangeHistoryPresenter extends BasePresenter implements ExchangeHistoryContract.HistoryPresenter {

	private final ExchangeHistoryContract.HistoryView mHistoryView;

	private List<ExchangeHistory> mData;
	private String mCurrentDate = "";

	@Inject
	ExchangeHistoryPresenter(Context context, ExchangeHistoryContract.HistoryView mHistoryView) {
		super(context);
		this.mHistoryView = mHistoryView;
	}

	public void getHistoryList(int page) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "balance");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("cmd", "getExchangeRecent");
		params.put("page", String.valueOf(page));

		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					ExchangeHistoryPageData historyPageData = JsonUtil.jsonToBean(result, ExchangeHistoryPageData.class);
					assert historyPageData != null;
					int total = historyPageData.getTotal();
					int page = historyPageData.getPage();
					int size = historyPageData.getSize();
					int nextPage = Utils.nextPage(total, page, size);
					List<ExchangeHistory> data = historyPageData.getList();
					if (page > 1) {
						setData(data);
						mHistoryView.showNextHistoryData(nextPage);
					} else {
						mCurrentDate = "";
						mData = new ArrayList<>();
						setData(data);
						mHistoryView.showHistoryData(nextPage, total > size);
					}
					DialogFactory.unblock();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, false);
	}

	List<ExchangeHistory> getHistoryData() {
		return mData;
	}

	private void setData(List<ExchangeHistory> historyList) {
		try {
			for (ExchangeHistory historyData : historyList) {
				historyData.setFlagSameDate(historyData.getTime()
					.substring(0, 7)
					.equals(mCurrentDate));
				mCurrentDate = historyData.getTime().substring(0, 7);
			}
			mData.addAll(historyList);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
	}
}
