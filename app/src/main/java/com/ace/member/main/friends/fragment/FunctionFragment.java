package com.ace.member.main.friends.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.ace.member.R;
import com.ace.member.base.BaseFragment;
import com.ace.member.main.friends.chat.ChatActivity;
import com.ace.member.main.home.transfer.to_member.ToMemberActivity;
import com.og.utils.Utils;

import butterknife.BindView;


public class FunctionFragment extends BaseFragment implements AdapterView.OnItemClickListener {
	@BindView(R.id.gridview_chat_function)
	GridView mGridView;

	public static final int REQUEST_CODE_1_TRANSFER = 1;

	@Override
	protected int getContentViewLayout() {
		return R.layout.fragment_chat_function;
	}

	@Override
	protected void initView() {
		int[] functionIcons = {R.drawable.ic_friends_redpacket, R.drawable.ic_friends_transfer};
		int[] functionNames = {R.string.red_packet, R.string.transfer};
		FunctionGridViewAdapter adapter = new FunctionGridViewAdapter(getContext(), functionIcons, functionNames);
		mGridView.setAdapter(adapter);
		mGridView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch (position) {
			case 0:
				Utils.showToast(R.string.red_packet);
				break;
			case 1:
				Intent it = new Intent(getActivity(), ToMemberActivity.class);
				it.putExtra("member_id", ((ChatActivity) getActivity()).getFriendID());
				it.putExtra("is_friend", true);
				getActivity().startActivityForResult(it, REQUEST_CODE_1_TRANSFER);
				break;
		}
	}

}
