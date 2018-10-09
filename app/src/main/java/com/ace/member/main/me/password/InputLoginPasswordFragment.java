package com.ace.member.main.me.password;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.ace.member.R;
import com.og.utils.Utils;


public class InputLoginPasswordFragment extends BottomSheetDialogFragment {

	Callback callback;
	private Unbinder mUnbinder;
	private EditText mPassword;

	public InputLoginPasswordFragment() {

	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		//Window window = getDialog().getWindow();
		//		View view = inflater.inflate(R.layout.fragment_input_password2, ((ViewGroup) window.findViewById(android.R.id.content)), false);
		//window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		//window.setLayout((int) (Utils.getScreenWidth() * 0.8), -2);
		//return view;
		return inflater.inflate(R.layout.fragment_input_password2, null);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		mUnbinder = ButterKnife.bind(this, view);
		mPassword = (EditText) view.findViewById(R.id.et_input_password);
		mPassword.requestFocus();
		Utils.showKeyboard(mPassword);

		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}

	@OnClick({R.id.fl_close, R.id.fl_confirm,R.id.tv_forgot_password})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.fl_close:
				callback.close();
				break;
			case R.id.fl_confirm:
				callback.confirm();
				break;
			case R.id.tv_forgot_password:
				callback.forgotPassword();
				break;
		}
	}

	public void show(FragmentManager manager, String tag, Callback callback) {
		this.callback = callback;
		super.show(manager, tag);
	}

	@Override
	public void onDestroy() {
		try {
			if (mUnbinder != null) mUnbinder.unbind();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}

	public String getPassword() {
		return mPassword.getText().toString();
	}

	interface Callback {
		void close();
		void confirm();
		void forgotPassword();
	}

}
