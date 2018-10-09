package com.ace.member.main.image_detail;

import android.net.Uri;

import com.ace.member.base.BaseBean;

public class Image extends BaseBean {
	private int mId;
	private String mBucketId;
	private String mBucketDisplayName;
	private String mDate;
	private String mUri;

	public Image() {
	}

	public Image(int id, String bucketId, String bucketDisplayName, String date) {
		this(id, bucketId, bucketDisplayName, date, "file://" + Uri.parse(date));
	}

	public Image(int id, String bucketId, String bucketDisplayName, String date, String uri) {
		this.mId = id;
		this.mBucketId = bucketId;
		this.mBucketDisplayName = bucketDisplayName;
		this.mDate = date;
		this.mUri = uri;
	}

	public int getId() {
		return mId;
	}

	public void setId(int id) {
		this.mId = id;
	}

	public String getBucketId() {
		return mBucketId;
	}

	public void setBucketId(String bucketId) {
		this.mBucketId = bucketId;
	}

	public String getBucketDisplayName() {
		return mBucketDisplayName;
	}

	public void setBucketDisplayName(String bucketDisplayName) {
		this.mBucketDisplayName = bucketDisplayName;
	}

	public String getDate() {
		return mDate;
	}

	public void setDate(String date) {
		this.mDate = date;
	}

	public String getUri() {
		return mUri;
	}

	public void setUri(String uri) {
		this.mUri = uri;
	}
}
