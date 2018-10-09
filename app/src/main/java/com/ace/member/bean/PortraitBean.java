package com.ace.member.bean;

import com.ace.member.BuildConfig;
import com.ace.member.base.BaseBean;


public class PortraitBean extends BaseBean {
	private String portrait;

	public String getPortrait() {
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}

	public String getRealNormalFileName() {
		return BuildConfig.FILE_BASE_URL + "images/normal/" + portrait;
	}

	public String getRealThumbnailFileName() {
		return BuildConfig.FILE_BASE_URL + "images/thumbnails/" + portrait;
	}

}
