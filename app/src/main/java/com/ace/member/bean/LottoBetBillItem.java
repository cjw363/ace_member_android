package com.ace.member.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class LottoBetBillItem implements Parcelable {
	private String number;
	private int type_sub = 0;
	private int mix = 2;
	private int times = 1;

	public void setNumber(String number) {
		this.number = number;
	}

	public String getNumber() {
		return number;
	}

	public void setTypeSub(int type) {
		this.type_sub = type;
	}

	public int getTypeSub() {
		return type_sub;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public int getTimes() {
		return times;
	}

	public void setMix(int mix) {
		this.mix = mix;
	}

	public int getMix() {
		return mix;
	}

	public LottoBetBillItem() {
	}

	protected LottoBetBillItem(Parcel in) {
		number = in.readString();
		type_sub = in.readInt();
		mix = in.readInt();
		times = in.readInt();
	}

	public static final Creator<LottoBetBillItem> CREATOR = new Creator<LottoBetBillItem>() {
		@Override
		public LottoBetBillItem createFromParcel(Parcel in) {
			return new LottoBetBillItem(in);
		}

		@Override
		public LottoBetBillItem[] newArray(int size) {
			return new LottoBetBillItem[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeString(number);
		parcel.writeInt(type_sub);
		parcel.writeInt(mix);
		parcel.writeInt(times);
	}
}
