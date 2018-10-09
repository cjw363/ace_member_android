package com.ace.member.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.utils.BaseApplication;
import com.og.utils.FileUtils;

import java.util.ArrayList;


public class LvCountryCodeAdapter extends BaseAdapter {
  private ArrayList<Integer> mCountryCodeDrawables;
  private ArrayList<String> mCountryCodeNames;
  private ArrayList<String> mCountryCodePhones;

  public LvCountryCodeAdapter(ArrayList<Integer> countryCodeDrawables, ArrayList<String> countryCodeNames, ArrayList<String> countryCodePhones) {
    mCountryCodeDrawables = countryCodeDrawables;
    mCountryCodeNames = countryCodeNames;
    mCountryCodePhones = countryCodePhones;
  }

  @Override
  public int getCount() {
    return mCountryCodeDrawables.size();
  }

  @Override
  public Object getItem(int position) {
    return null;
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    try {
      ViewHolder viewHolder;
      if (convertView == null) {
        convertView = LayoutInflater.from(BaseApplication.getContext()).inflate(R.layout.view_country_code_item, null);
        viewHolder = new ViewHolder(convertView);
        convertView.setTag(viewHolder);
      } else {
        viewHolder = (ViewHolder) convertView.getTag();
      }
      viewHolder.mImageView.setImageResource(mCountryCodeDrawables.get(position));
      viewHolder.mTvCountryPhone.setText(mCountryCodePhones.get(position));
      viewHolder.mTvCountryName.setText(mCountryCodeNames.get(position));
    } catch (Exception e) {
      FileUtils.addErrorLog(e);
    }
    return convertView;
  }

  class ViewHolder {
    TextView mTvCountryName, mTvCountryPhone;
    ImageView mImageView;

    ViewHolder(View view) {
      mTvCountryName = (TextView) view.findViewById(R.id.tv_item_country_name);
      mTvCountryPhone = (TextView) view.findViewById(R.id.tv_item_country_phone);
      mImageView = (ImageView) view.findViewById(R.id.iv_lv_item_country_image);
    }
  }
}
