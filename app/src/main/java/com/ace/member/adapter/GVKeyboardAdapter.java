package com.ace.member.adapter;

import android.content.Context;
import android.support.v4.util.SparseArrayCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ace.member.R;
import com.og.utils.FileUtils;

import java.util.Random;


public class GVKeyboardAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private SparseArrayCompat<Integer> mNumberArray;

	public GVKeyboardAdapter(Context context) {
		this.mInflater = LayoutInflater.from(context);
		mNumberArray = createNumberKeyBoard(false);
	}

	@Override
	public int getCount() {
		return 12;
	}

	@Override
	public Integer getItem(int position) {
		return mNumberArray.get(position == 10 ? 9 : position);
	}

	@Override
	public long getItemId(int position) {
		return position == 9 ? -2 : position == 11 ? -1 : getItem(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try {
			ViewHolder holder = null;
			if (convertView == null) {
				if (position == 9) {
					convertView = mInflater.inflate(R.layout.view_keyboard_space_item, null);
				} else if (position == 11) {
					convertView = mInflater.inflate(R.layout.view_keyboard_delete, null);
				} else {
					convertView = mInflater.inflate(R.layout.view_keyboard_number_item, null);
					holder = new ViewHolder(convertView);
					convertView.setTag(holder);
				}
			} else {
				if (position != 9 && position != 11) {
					holder = (ViewHolder) convertView.getTag();
				}
			}

			if (position != 9 && position != 11 && holder != null) {
				holder.mTvNumber.setText(String.valueOf(mNumberArray.get(position == 10 ? 9 : position)));
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return convertView;
	}

	class ViewHolder {
		TextView mTvNumber;

		ViewHolder(View view) {
			mTvNumber = (TextView) view.findViewById(R.id.tv_number);
		}
	}

	private SparseArrayCompat<Integer> createNumberKeyBoard(boolean isRandom) {
		SparseArrayCompat<Integer> arrayCompat = new SparseArrayCompat<>(10);
		for (int i = 0; i < 10; i++) {
			arrayCompat.put(i, i + 1 > 9 ? 0 : i + 1);
		}
		if (isRandom) {
			Random random = new Random(System.currentTimeMillis());
			for (int i = 0; i < 10; i++) {
				int pos = random.nextInt(10);
				int num = arrayCompat.get(pos);
				arrayCompat.put(pos, arrayCompat.get(i));
				arrayCompat.put(i, num);
			}
		}
		return arrayCompat;
	}
}
