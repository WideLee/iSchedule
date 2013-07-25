package com.android.iSchedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class EventAdapter extends BaseAdapter {

	private List<Map<String, String>> list;
	private static HashMap<Integer, Boolean> isSelected;
	private Context context;
	private LayoutInflater inflater = null;

	public EventAdapter(List<Map<String, String>> list, Context context) {
		
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
		isSelected = new HashMap<Integer, Boolean>();
		initData();
	}

	private void initData(){
		for(int i = 0; i < list.size(); i++){
			getIsSelected().put(i, false);
		}
	}

	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}

	public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
	EventAdapter.isSelected = isSelected;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.find_event_list_item, null);
			holder.eName = (TextView) convertView.findViewById(R.id.event_title);
			holder.eBegin = (TextView) convertView.findViewById(R.id.event_begin_time);
			holder.eEnd = (TextView) convertView.findViewById(R.id.event_end_time);
			holder.cb = (CheckBox) convertView.findViewById(R.id.isChecked);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.eName.setText(list.get(position).get("title"));
		holder.eBegin.setText(list.get(position).get("beginTime"));
		holder.eEnd.setText(list.get(position).get("endTime"));
		
		holder.cb.setChecked(getIsSelected().get(position));
		
		return convertView;
	}

}