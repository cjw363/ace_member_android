package com.og.adapter;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.og.R;
import com.og.utils.FileUtils;
import com.og.utils.ItemObject;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSpinnerAdapter<T> extends BaseAdapter {
	public interface IOnItemSelectListener {
		void onItemClick(int pos);
	}

	;

	private List<T> mObjects = new ArrayList<T>();
	//	private int mSelectItem = 0;
	private float mTextSize = 0;
	private boolean mBool = false, isPhone = false;

	private LayoutInflater mInflater;

	AbstractSpinnerAdapter(Context context) {
		init(context);
	}

	public void refreshData(List<T> objects, int selIndex, boolean bool, float textSize, boolean phone) {
		mObjects = objects;
		mTextSize = textSize;
		mBool = bool;
		isPhone = phone;
		if (selIndex < 0) {
			selIndex = 0;
		}
		if (selIndex >= mObjects.size()) {
			selIndex = mObjects.size() - 1;
		}

		//		mSelectItem = selIndex;
	}

	private void init(Context context) {
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}


	@Override
	public int getCount() {
		return mObjects.size();
	}

	@Override
	public Object getItem(int pos) {
		return mObjects.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
		ViewHolder viewHolder;

		try {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.spinner_item_layout, null);
				viewHolder = new ViewHolder();
				viewHolder.mTextView = (TextView) convertView.findViewById(R.id.textView);
				if (mTextSize > 0) viewHolder.mTextView.setTextSize(mTextSize);
				viewHolder.mTextViewKey = (TextView) convertView.findViewById(R.id.tv_key);
				viewHolder.mSpinnerItemLine = (LinearLayout) convertView.findViewById(R.id.spinner_item_line);
				if (mBool) viewHolder.mSpinnerItemLine.setGravity(Gravity.CENTER);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			ItemObject item = (ItemObject) getItem(pos);
			if (isPhone) {
				viewHolder.mTextView.setText(Html.fromHtml("&#160 <a href=\"tel:" + item.getValue() + "\">" + item.getValue() + "</a>"));
				viewHolder.mTextView.setMovementMethod(LinkMovementMethod.getInstance());
			} else {
				viewHolder.mTextView.setText(item.getValue());
			}
			viewHolder.mTextViewKey.setText(item.getKey());
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}

		return convertView;
	}

	private static class ViewHolder {
		TextView mTextView;
		TextView mTextViewKey;
		LinearLayout mSpinnerItemLine;
	}
}
