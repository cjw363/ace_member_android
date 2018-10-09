package com.ace.member.main.home.scan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.main.home.money.BaseCashActivity;
import com.ace.member.main.home.scan.error.ScanErrorFragment;
import com.ace.member.main.home.transfer.to_member.ToMemberActivity;
import com.og.utils.FileUtils;

public class ScanResultActivity extends BaseActivity {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.fl_content;
	}

	private void init() {
		try {
			String code = getIntent().getStringExtra("code");
			String[] args = code.split("\\|");
			String business = args[0];
			if (business.equals(BaseCashActivity.TRANSFER_11_MEMBER_RECEIVE_MONEY)) {
				Intent intent = new Intent(this, ToMemberActivity.class);
				intent.putExtra("phone", args[1]);
				intent.putExtra("member_id",Integer.parseInt(args[2]));
				intent.putExtra("currency", args[3]);
				intent.putExtra("is_receive_money",true);
				if (args.length > 4) intent.putExtra("amount", Double.parseDouble(args[4]));
				startActivity(intent);
				finish();
			}
			//其他业务继续if判断，否则跳转到失败页面
			else {
				toError();
			}
		} catch (Exception e) {
			e.printStackTrace();
			toError();
			FileUtils.addErrorLog(e);
		}
	}

	private void toError(){
		try {
			Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fl_content);
			if (fragment == null) {
				fragment = new ScanErrorFragment();
			}
			getSupportFragmentManager().beginTransaction()
				.replace(R.id.fl_content, fragment)
				.commit();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}
}
