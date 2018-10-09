package com.ace.member.main.home.top_up.phone_company;

import android.content.Context;

import com.ace.member.base.BasePresenter;
import com.ace.member.bean.PhoneCompany;
import com.ace.member.utils.Session;
import com.google.gson.reflect.TypeToken;
import com.og.LibSession;
import com.og.http.SimpleRequestListener;
import com.og.utils.JsonUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

final class PhoneCompanyPresenter extends BasePresenter implements PhoneCompanyContract.Presenter {

	private final PhoneCompanyContract.View mView;

	@Inject
	public PhoneCompanyPresenter(Context context, PhoneCompanyContract.View view) {
		super(context);
		mView = view;
	}
}
