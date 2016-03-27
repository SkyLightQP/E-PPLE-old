package me.efe.unlimitedrpg.unlimitedtag;

import java.util.Calendar;
import java.util.Date;

public class CalendarData {
	private final int year;
	private final int month;
	private final int day;
	private final int hour;
	private final int minute;
	
	public CalendarData(int year, int month, int day, int hour, int minute) {
		this.year = year;
		this.month = month;
		this.day = day;
		this.hour = hour;
		this.minute = minute;
	}
	
	public int getYear() {
		return this.year;
	}
	
	public int getMonth() {
		return this.month;
	}
	
	public int getDay() {
		return this.day;
	}
	
	public int getHour() {
		return this.hour;
	}
	
	public int getMinute() {
		return this.minute;
	}
	
	public Date after(Date date) {
		Calendar cal = Calendar.getInstance();
		
		cal.add(Calendar.YEAR, year);
		cal.add(Calendar.MONTH, month);
		cal.add(Calendar.DAY_OF_MONTH, day);
		cal.add(Calendar.HOUR_OF_DAY, hour);
		cal.add(Calendar.MINUTE, minute);
		
		return cal.getTime();
	}
	
	public static CalendarData parse(String date) {
		String[] data = date.split(" ");
		
		int year = Integer.parseInt(data[0]);
		int month = Integer.parseInt(data[1]);
		int day = Integer.parseInt(data[2]);
		int hour = Integer.parseInt(data[3]);
		int minute = Integer.parseInt(data[4]);
		
		return new CalendarData(year, month, day, hour, minute);
	}
}