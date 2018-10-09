package com.ace.member.event;


import com.ace.member.bean.EdcBill;

public class SelectEdcRecentEvent {
	private EdcBill mEdcWsaBill;

	public SelectEdcRecentEvent() {
	}

	public SelectEdcRecentEvent(EdcBill edcWsaBill) {
		mEdcWsaBill = edcWsaBill;
	}

	public EdcBill getEdcWsaBill() {
		return mEdcWsaBill;
	}

	public void setEdcWsaBill(EdcBill edcWsaBill) {
		mEdcWsaBill = edcWsaBill;
	}
}
