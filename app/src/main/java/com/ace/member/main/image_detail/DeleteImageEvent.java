package com.ace.member.main.image_detail;


public class DeleteImageEvent {
	private int mPosition;

	public DeleteImageEvent(int position) {
		mPosition = position;
	}

	public int getPosition() {
		return mPosition;
	}

	public void setPosition(int position) {
		mPosition = position;
	}
}
