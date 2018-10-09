package com.ace.member.main.me.transaction;

import android.content.Context;

import com.ace.member.base.BasePresenter;
import com.ace.member.bean.Transaction;
import com.ace.member.utils.Session;
import com.google.gson.JsonObject;
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


class TransactionPresenter extends BasePresenter implements TransactionContract.TransactionPresenter {

	private final TransactionContract.TransactionView mTransactionView;

	private List<Transaction> mData;
	private String mCurrentDate = "";

	@Inject
	TransactionPresenter(Context context, TransactionContract.TransactionView mTransactionView) {
		super(context);
		this.mTransactionView = mTransactionView;
	}


	@Override
	public void getTransactionList(int page) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "balance");
		params.put("_b", "aj");
		params.put("_s", Session.sSid);
		params.put("cmd", "getTransaction");
		params.put("page", String.valueOf(page));

		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					TransactionPageData transactionPageData = JsonUtil.jsonToBean(result, TransactionPageData.class);
					assert transactionPageData != null;
					int total = transactionPageData.getTotal();
					int page = transactionPageData.getPage();
					int size = transactionPageData.getSize();
					int nextPage = Utils.nextPage(total, page, size);
					List<Transaction> data = transactionPageData.getList();
					Map<String,String> totalAmountObj = transactionPageData.getTotalAmountObj();
					if (page > 1) {
						setData(data, totalAmountObj);
						mTransactionView.showNextTransactionData(nextPage);
					} else {
						mCurrentDate = "";
						mData = new ArrayList<>();
						setData(data, totalAmountObj);
						mTransactionView.showTransactionData(nextPage, total > size);
					}
					DialogFactory.unblock();
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
					e.printStackTrace();
				}
			}
		}, false);
	}

	List<Transaction> getTransactionData() {
		return mData;
	}

	private void setData(List<Transaction> transactionList, Map<String,String> totalAmount) {
		try {
			for (Transaction transactionData : transactionList) {
				transactionData.setFlagSameDate(transactionData.getDate()
					.substring(0, 7)
					.equals(mCurrentDate));
				mCurrentDate = transactionData.getDate().substring(0, 7);
				String total = totalAmount.get(transactionData.getDate().substring(0, 7));
				transactionData.setTotal(total);
			}
			mData.addAll(transactionList);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
	}
}
