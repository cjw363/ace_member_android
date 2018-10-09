package com.ace.member.bean;

import com.ace.member.base.BaseBean;
import com.google.gson.annotations.SerializedName;


public class FileUploadResult extends BaseBean {
	@SerializedName("err_code")
	private int errCode;

	private String file0;

	private String file1;

	private String file2;

	public int getErrCode() {
		return errCode;
	}

	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}

	public String getFile0() {
		return file0;
	}

	public void setFile0(String file0) {
		this.file0 = file0;
	}

	public String getFile1() {
		return file1;
	}

	public void setFile1(String file1) {
		this.file1 = file1;
	}

	public String getFile2() {
		return file2;
	}

	public void setFile2(String file2) {
		this.file2 = file2;
	}
}
