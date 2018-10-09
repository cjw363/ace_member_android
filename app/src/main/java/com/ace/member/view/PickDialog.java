package com.ace.member.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.adapter.DialogListViewAdapter;

public class PickDialog extends Dialog{
	private Context mContext;
	private String mTitle;
	private LinearLayout mDialogPreview;
	private ListView mDialogNextView;
	private List<Map<String, String>> mItems = new ArrayList<>();
	private PickDialogListener mPickDialogListener;
	DialogListViewAdapter mAdapter;

	public PickDialog(Context context,String title,PickDialogListener pickDialogListener) {
		super(context, R.style.blend_theme_dialog);
		this.mContext=context;
		this.mTitle=title;
		this.mPickDialogListener=pickDialogListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		LayoutInflater inflater =LayoutInflater.from(mContext);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.view_blend_dialog_preview_layout, null);

		TextView tvTitle = (TextView) layout.findViewById(R.id.blend_dialog_title);
		tvTitle.setText(mTitle);
		TextView tvCancel = (TextView) layout.findViewById(R.id.blend_dialog_cancle_btn);
		tvCancel.setText("Cancel");
		mDialogPreview = (LinearLayout) layout.findViewById(R.id.blend_dialog_preview);
		mDialogNextView = (ListView) layout.findViewById(R.id.blend_dialog_next_view);
		

		this.setCanceledOnTouchOutside(true);
		this.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				dismiss();
				mItems.clear();
			}
		});
		tvCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dismiss();
				mItems.clear();
			}

			
		});
		
		
		this.setContentView(layout);
	}
	
	public void initListViewData( List<Map<String, String>> data){
		mItems=data;
		mDialogPreview.setVisibility(View.GONE);
		mDialogNextView.setVisibility(View.VISIBLE);
		mAdapter = new DialogListViewAdapter(mContext, data);
		mDialogNextView.setAdapter(mAdapter);
		mDialogNextView.setOnItemClickListener(new ListView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				dismiss();
				if(mPickDialogListener!=null){
					mPickDialogListener.onListItemClick(position, mItems.get(position));
				}
			}
		});
	}

	public void clearInfo(){
		mItems.clear();
	}


}
