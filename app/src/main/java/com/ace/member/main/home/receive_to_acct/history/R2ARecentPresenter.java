package com.ace.member.main.home.receive_to_acct.history;

import android.content.Context;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.utils.AppGlobal;
import com.og.LibSession;
import com.og.http.SimpleRequestListener;
import com.og.utils.DialogFactory;
import com.og.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class R2ARecentPresenter extends BasePresenter implements R2ARecentContract.Presenter {

	private final R2ARecentContract.View mView;
	private JSONArray mData = new JSONArray();
	private String mFlagSameDate;

	@Inject
	public R2ARecentPresenter(Context context, R2ARecentContract.View mView) {
		super(context);
		this.mView = mView;
	}


	@Override
	public void start() {
	}

	@Override
	public void getHistoryList(int page) {
		Map<String, String> p = new HashMap<>();
		p.put("_a", "transfer");
		p.put("_b", "aj");
		p.put("_s", LibSession.sSid);
		p.put("page", String.valueOf(page));
		p.put("cmd", "getTransferCashRecord");
		submit(p, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					JSONObject data = new JSONObject(result);
					int total = data.getInt("total");
					int page = data.getInt("page");
					int size = data.getInt("size");
					int nextPage = Utils.nextPage(total, page, size);
					if (page > 1) {
						formatData(data);
						mView.showNextList(nextPage);
					} else {
						mFlagSameDate = "";
						mData = new JSONArray();
						formatData(data);
						mView.showList(nextPage, total > size);
					}
					DialogFactory.unblock();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, false);
	}

	private String getNameStr(String name) {
		if (name.equals("null")) {
			return String.format(Utils.getString(R.string.bracket),Utils.getString(R.string.non_member));
		}
		return String.format(Utils.getString(R.string.bracket),Utils.getString(R.string.member)) + " "+name;
	}

	private void formatData(JSONObject data) {
		try {
			JSONArray arr = Utils.parseArrayData(data.getJSONArray("list"), data.getJSONArray("keys"));
			JSONObject jsonObject;

			String date = "", onlyTime = "", strTime;
			if (arr != null) {
				for (int i = 0, len = arr.length(); i < len; i++) {
					jsonObject = arr.getJSONObject(i);
					strTime = jsonObject.getString("time");
					if (strTime != null) {
						date = strTime.substring(0, 7);
						onlyTime = strTime.substring(5, 16);
					}
					jsonObject.put("source", getNameStr(jsonObject.getString("source")));
					jsonObject.put("date", date);
					jsonObject.put("only_time", onlyTime);
					jsonObject.put("id",jsonObject.getInt("id"));
					jsonObject.put("flagSameDate", date.equals(mFlagSameDate));
					mFlagSameDate = date;
					mData.put(jsonObject);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public JSONArray getData() {
		return mData;
	}
}
