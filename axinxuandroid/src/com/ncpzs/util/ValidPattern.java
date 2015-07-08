package com.ncpzs.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

 

public class ValidPattern {
	public static boolean isMobileNO(String mobiles){
		if(mobiles==null) return false;
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");     
        Matcher m = p.matcher(mobiles);     
        return m.matches();     
    } 
	
	public static boolean isAllNumber(String str){
		if(str==null) return false;
        Pattern p = Pattern.compile("\\d{1,18}");     
        Matcher m = p.matcher(str);     
        return m.matches();     
    } 
	 
}
