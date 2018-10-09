package com.ace.member.main.friends.chat.chat_info.transfer_history;

import android.content.Context;

import com.ace.member.base.BasePresenter;
import com.ace.member.bean.TransferRecent;
import com.ace.member.main.home.transfer.recent.TransferRecentBean;
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

class FriendTransferHistoryPresenter extends BasePresenter implements FriendTransferHistoryContract.TransferRecentPresenter {

	private final FriendTransferHistoryContract.TransferRecentView mView;

	private String mCurrentDate = "";
	private List<TransferRecent> mData;
	private List<TransferRecent> mRawData;

	@Inject
	FriendTransferHistoryPresenter(Context context, FriendTransferHistoryContract.TransferRecentView mView) {
		super(context);
		this.mView = mView;
	}

	void getTransferRecentList(int page, int memberID) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "friends");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("page", String.valueOf(page));
		params.put("cmd", "getFriendTransferHistory");
		params.put("member_id", String.valueOf(memberID));

		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					TransferRecentBean bean = JsonUtil.jsonToBean(result, TransferRecentBean.class);
					if (bean == null) return;
					int total = bean.getTotal();
					int page = bean.getPage();
					int size = bean.getSize();
					int nextPage = Utils.nextPage(total, page, size);
					List<TransferRecent> transferRecentList = bean.getList();
					mCurrentDate = "";//清除数据

					if (page > 1) {
						mRawData.addAll(transferRecentList);
						mData.clear();
						mData = simplifyData(mRawData);
					} else {
						mRawData = new ArrayList<>();
						mRawData = Utils.cloneFrom(transferRecentList);
						mData = simplifyData(transferRecentList);
					}
					if (mData == null) mData = new ArrayList<>();
					mView.setRecentList(nextPage, mData, total > size);

					DialogFactory.unblock();
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}

			@Override
			public void loadFailure(int code, String result, String token) {
				mView.setRecentList(1, new ArrayList<TransferRecent>(), false);
			}
		}, false);
	}

	private List<TransferRecent> simplifyData(List<TransferRecent> list) {
		List<TransferRecent> transferHistory = Utils.cloneFrom(list);
		int size = transferHistory.size();
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				TransferRecent data = transferHistory.get(i);
				String currentDate = Utils.formatDateToYearMonth(data.getTime());
				data.setFlagSameDate(currentDate.equals(mCurrentDate));//检查是否相同月份
				if (!mCurrentDate.equals(currentDate)) {
					if (i >= 1) {
						transferHistory.get(i - 1).setDateEnd(true);
					}
				}
				mCurrentDate = currentDate;
				transferHistory.get(size - 1).setDateEnd(true);
			}
		}
		return transferHistory;
	}

}
