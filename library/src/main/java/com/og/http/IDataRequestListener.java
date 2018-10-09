package com.og.http;

import org.json.JSONObject;

public interface IDataRequestListener {
	void loadSuccess(String result,String uniqueToken);
	void loadFailure(int errorCode,String result,String uniqueToken);
}
