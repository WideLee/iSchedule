package com.android.iSchedule;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class findAllEvents extends Activity {

	ListView listView;
	AutoCompleteTextView inputTitle;
	Button selectAllButton;
	Button unSecectAllButton;
	Button deleteEventButton;
	Button backButton;
	iScheduleDB dbHelper = new iScheduleDB(this);
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SparseBooleanArray position = new SparseBooleanArray();
	List<Event> allEvents = new ArrayList<Event>();
	EventAdapter eventsAdapter;
	List<Map<String, String>> eventsList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.find_all_events);

		listView = (ListView) this.findViewById(R.id.eventList);
		selectAllButton = (Button) this.findViewById(R.id.selectAll);
		unSecectAllButton = (Button) this.findViewById(R.id.unSelectAll);
		deleteEventButton = (Button) this.findViewById(R.id.deleteEvent);
		inputTitle = (AutoCompleteTextView) this
				.findViewById(R.id.find_event_title);
		backButton = (Button) this.findViewById(R.id.back);

		try {
			allEvents = dbHelper.getALLEvent();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		List<String> eventTitle = new ArrayList<String>();
		Set<String> eventSet = new HashSet<String>();
		for (int i = 0; i < allEvents.size(); i++) {
			eventSet.add(allEvents.get(i).getTitle());
		}
		Iterator<String> it = eventSet.iterator();
		while (it.hasNext()) {
			String str = it.next();
			eventTitle.add(str);
		}
		eventsList = new ArrayList<Map<String,String>>();
		updateList("");
		
		ArrayAdapter<String> eventTitleAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, eventTitle);
		inputTitle.setAdapter(eventTitleAdapter);
		// 设置最小的自动补全长度是1
		inputTitle.setThreshold(1);
		// 设置文本改变时的响应
		inputTitle.addTextChangedListener(watcher);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				ViewHolder holder = (ViewHolder) arg1.getTag();
				holder.cb.toggle();
				EventAdapter.getIsSelected().put(arg2, holder.cb.isChecked());	
			}
		
		});

		selectAllButton.setOnClickListener(selectAllOnclick);
		unSecectAllButton.setOnClickListener(unSecectAllOnclick);
		//deleteEventButton.setOnClickListener(deleteOnclick);
		backButton.setOnClickListener(backOnclick);
	}

	public TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			String eventTitleString = s.toString();
			updateList(eventTitleString);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// nothing
		}

		@Override
		public void afterTextChanged(Editable s) {
			// nothing
		}
	};

	// 更新列表，参数是事件主题输入框的文本
	// 如果参数为空的字符串，那返回所有的事件
	public void updateList(String eventsTitle) {
		if (allEvents.size() > 0) {
			allEvents.clear();
			eventsList.clear();
		}
		try {
			if (eventsTitle.equals("")) {
				allEvents = dbHelper.getALLEvent();
			} else {
				allEvents = dbHelper.FuzzyQuery(eventsTitle);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < allEvents.size(); i++) {
			Event event = allEvents.get(i);
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("title", event.getTitle());
			map.put("beginTime", "开始时间: " + formatter.format(event.getStartTime()));
			map.put("endTime", "结束时间: " + formatter.format(event.getEndTime()));
			
			eventsList.add(map);
		}
		
		eventsAdapter = new EventAdapter(eventsList, findAllEvents.this);
		
		listView.setAdapter(eventsAdapter);
	}
	
	
	public OnClickListener selectAllOnclick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			for (int i = 0; i < allEvents.size(); i++) {
				EventAdapter.getIsSelected().put(i, true);
			}
			eventsAdapter.notifyDataSetChanged();
		}
	};

	public OnClickListener unSecectAllOnclick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			for (int i = 0; i < allEvents.size(); i++) {
				EventAdapter.getIsSelected().put(i, false);
			}
			eventsAdapter.notifyDataSetChanged();
		}
	};

	public OnClickListener backOnclick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.setClass(findAllEvents.this, Main.class);
			startActivity(intent);
			finish();
		}
	};

	public OnClickListener deleteOnclick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			SparseBooleanArray checkEvent = new SparseBooleanArray();
			checkEvent = listView.getCheckedItemPositions();
			for (int i = 0; i < allEvents.size(); i++) {
				if (checkEvent.get(i)) {
					dbHelper.deleteEventById((int) allEvents.get(i)
							.getEventId());
				}
				Log.d("checkEventsize", Boolean.toString(checkEvent.get(i)));
			}
			updateList(inputTitle.getText().toString());
		}
	};

	// 设置返回键按下的响应
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.setClass(findAllEvents.this, Main.class);
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.watch_event, menu);
		return true;
	}
}