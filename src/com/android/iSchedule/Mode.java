package com.android.iSchedule;

public class Mode {
	
	private long modeId;
	private int volume;
	private int vibrate;
	// test branches
	// To Do 
	// Setter and Getter
	
	public Mode(int vo, int vi)
	{
		modeId = -1;
		volume = vo;
		vibrate = vi;
	}
	public long getModeId()
	{
		return modeId;
	}
	public int getVolume()
	{
		return volume;
	}
	public int getVibrate()
	{
		return vibrate;
	}
    public void setModeId(long id)
    {
    	this.modeId = id;
    }
	public void setVolume(int volume)
	{
		this.volume = volume;
	}
	public void setVibrate(int vibrate)
	{
		this.vibrate = vibrate;
	}
}
> list = new ArrayList<Mode>();
		SQLiteDatabase db = getReadableDatabase();

		Cursor c = db.query(MODE_TABLE_NAME, null, null, null, null,null, null);
		while (c.moveToNext()){
			Mode mode = new Mode(c.getInt(1), c.getInt(2));
			mode.setModeId(c.getLong(0));
			list.add(mode);
		}
		c.close();
		db.close();
		
		return list;
	}
	// update operation
	// each search operation
	
	
}