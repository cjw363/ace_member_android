package com.ace.member.event;


import com.ace.member.bean.EdcBill;
import com.ace.member.bean.WsaBill;

public class SelectWsaRecentEvent {
	private WsaBill mWsaBill;

	public SelectWsaRecentEvent(WsaBill bill) {
		mWsaBill=bill;
	}

	public WsaBill getWsaBill() {
		return mWsaBill;
	}

	public void setWsaBill(WsaBill wsaBill) {
		mWsaBill = wsaBill;
	}
}
