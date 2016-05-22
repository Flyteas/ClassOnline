package pw.flyshit.ClassOnline.Util;

import java.text.SimpleDateFormat;
import java.util.Date;

/* 日期转换器工具类 */
public class DateTimeConverter 
{
	public DateTimeConverter()
	{
		
	}
	
	public Date dateTimeStrToDate(String dateTime,String dateTimeFormat) throws Exception //string to date，dateTimeFormat为日期格式
	{
		SimpleDateFormat dateFormater= new SimpleDateFormat(dateTimeFormat);
		Date date = dateFormater.parse(dateTime);
		return date;
	}
	
	public long dateTimeStrToLong(String dateTime,String dateTimeFormat) throws Exception //string to long，dateTimeFormat为日期格式
	{
		SimpleDateFormat dateFormater= new SimpleDateFormat(dateTimeFormat);
		Date date = dateFormater.parse(dateTime);
		return date.getTime();    
	}
	
	public Date dateTimeLongToDate(long dateTime) //long to date
	{
		Date date = new Date(dateTime);
		return date;
	}
	
	public String dateTimeLongToStr(long dateTime,String dateTimeFormat) throws Exception //long to string，dateTimeFormat为日期格式
	{
		Date date = new Date(dateTime);
		SimpleDateFormat dateFormater= new SimpleDateFormat(dateTimeFormat);
		return dateFormater.format(date);
	}
}
