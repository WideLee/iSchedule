package com.android.iSchedule;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class iScheduleDB extends SQLiteOpenHelper {

	private static final String DB_NAME = "iSchedule.db";
	private static final int DB_VRESION = 1;
	private static final String TABLE_NAME = "event";
	//private static final String SQL_CREATE_TABLE = "TO DO"
	
	
	public iScheduleDB(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO �Զ����ɵĹ��캯�����
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO �Զ����ɵķ������
		
	}
	
	// insert operation
	// delete operation
	// update operation
	// each search operation
	
	
}