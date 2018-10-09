package com.ace.member.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class LottoSpecial2dItem implements Parcelable {

	//这个变量定义有下划线是因为要解析到后端
	private int bet_type = 1;
	private String number;
	private int type_sub = 1;
	private int mix = 2;
	private int times = 1;
	private int special_type = 0;
	private int page = 1;

	public LottoSpecial2dItem() {}

	public LottoSpecial2dItem(Parcel in) {
		bet_type = in.readInt();
		number = in.readString();
		type_sub = in.readInt();
		mix = in.readInt();
		times = in.readInt();
		special_type = in.readInt();
		page = in.readInt();
	}

	public static final Creator<LottoSpecial2dItem> CREATOR = new Creator<LottoSpecial2dItem>() {
		@Override
		public LottoSpecial2dItem createFromParcel(Parcel in) {
			return new LottoSpecial2dItem(in);
		}

		@Override
		public LottoSpecial2dItem[] newArray(int size) {
			return new LottoSpecial2dItem[size];
		}
	};

	public void setBetType(int type) {
		bet_type = type;
	}

	public int getBetType() {
		return bet_type;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getNumber() {
		return number;
	}

	public void setTypeSub(int type) {
		type_sub = type;
	}

	public int getTypeSub() {
		return type_sub;
	}

	public void setMix(int mix) {
		this.mix = mix;
	}

	public int getMix() {
		return mix;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public int getTimes() {
		return times;
	}

	public void setSpecialType(int type) {
		special_type = type;
	}

	public int getSpecialType() {
		return special_type;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPage() {
		return page;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeInt(bet_type);
		parcel.writeString(number);
		parcel.writeInt(type_sub);
		parcel.writeInt(mix);
		parcel.writeInt(times);
		parcel.writeInt(special_type);
		parcel.writeInt(page);
	}
}
