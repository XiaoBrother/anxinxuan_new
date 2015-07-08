package com.ncpzs.util;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
public class DateUtil {



	/**
	* ����ת�����ַ���
	* @param date 
	* @return str
	*/
	public static String DateToStr(Date date) {
  	   return dateToStrWithPattern(date,"yyyy/MM/dd");
	} 
	/**
	* ����ת�����ַ���
	* @param date 
	* @return str
	*/
	public static String DateToStr(Date date,String pattern) {
  	   return dateToStrWithPattern(date,pattern);
	} 

	/**
	* �ַ���ת��������
	* @param str
	* @return date
	*/
	public static Date StrToDate(String str) {
	   if(str==null||"".equals(str.trim())) return null;
 	   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   Date date = null;
	   try {
	    date = format.parse(str);
	   } catch (ParseException e) {
	    e.printStackTrace();
	   }
	   return date;
	}
	/**
	* �ַ���ת��������
	* @param str
	* @return date
	*/
	public static Date StrToDate(String str,String model) {
	   if(str==null||"".equals(str.trim())) return null;
 	   SimpleDateFormat format = new SimpleDateFormat(model);
	   Date date = null;
	   try {
	    date = format.parse(str);
	   } catch (ParseException e) {
	    e.printStackTrace();
	   }
	   return date;
	}
	/**
	*����ת�����ַ���
	* @param str
	* @return date
	*/
	public static String  dateToStrWithPattern(Date date,String pattern) {
 		 if(date==null) return null;
		   SimpleDateFormat format = new SimpleDateFormat(pattern);
		   String str = format.format(date);
		 return str;
	}
	/**
	 * �Ƚ�����������������
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static long getDiffDays(Date d1,Date d2){
		if(d1==null||d2==null) return -1;
 		long diff =Math.abs( d1.getTime()- d2.getTime());
 	    return diff/(1000 * 60 * 60 * 24);
	}
	/**
	 * �Ƚ�����������������
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static long getDiffYears(Date d1,Date d2){
		if(d1==null||d2==null) return -1;
		return d1.getYear()-d2.getYear();
 	}
	 /**
	 * �Ƚ��������ڵĴ�С
	 * @param date1
	 * @param date2
	 * @return
	 */
	
	public static int compare(Date date1,Date date2){
		 if(date1==null&&date2==null) return 0;
		 else if(date1==null) return -1;
		 else if(date2==null) return 1;
		 int result=0;
		 if(date1.getTime()>date2.getTime())
			 result=1;
		 else if(date1.getTime()<date2.getTime())
			 result=-1;
		 return result;
	 }
	/**
	 * ȥ������֮��Ŀո� ��2014-05-05 13:56:35��ʽ ѹ����2014-05-0513:56:35
	 * ��3g�����¿ո���������
	 * @return
	 */
	public static String delSpaceDate(String stime){
		if(stime!=null)
			return stime.replaceAll(" ", "");
		else return "";
	}
}
