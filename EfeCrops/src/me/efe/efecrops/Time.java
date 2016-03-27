package me.efe.efecrops;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Time {
	public int getMonth(){
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MONTH);
	}
	
	public int getDate(){
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DATE);
	}
	
	public int getHour(){
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.HOUR_OF_DAY);
	}
	
	public int getMinute(){
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MINUTE);
	}
	
	public String getTime(){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		String date = sdf.format(cal.getTime());
		return date;
	}
}
