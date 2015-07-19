package com.ncpzs.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUtil {

	/** 
     * 手机号验证 
     *  
     * @param  str 
     * @return 验证通过返回true 
     */  
    public static boolean isMobile(String str) {   
        Pattern p = null;  
        Matcher m = null;  
        boolean b = false;   
        p = Pattern.compile("/^13[0-9]{9}|15[012356789][0-9]{8}|18[0-9][0-9]{8}|147[0-9]{8}$/"); // 验证手机号  
        m = p.matcher(str);  
        b = m.matches();   
        return b;  
    }  
    /** 
     * 手机号验证 
     *  
     * @param  str 
     * @return 验证通过返回true 
     */  
    public static boolean isTel(String str) {   
    	Pattern p = null;  
    	Matcher m = null;  
    	boolean b = false;   
    	p = Pattern.compile("/^[+]{0,1}(\\d){1,4}[ ]{0,1}([-]{0,1}((\\d)|[ ]){1,12})+$/");//座机号  
    	m = p.matcher(str);  
    	b = m.matches();   
    	return b;  
    }  
	/** 
     * 网址验证
     *  
     * @param  str 
     * @return 验证通过返回true 
     */  
    public static boolean isUrl(String str) {   
        Pattern p = null;  
        Matcher m = null;  
        boolean b = false;  
        String strRegex = "^((https|http|ftp|rtsp|mms)?://)"
        		+ "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" // ftp的user@
        		+ "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
        		+ "|" // 允许IP和DOMAIN（域名）
        		+ "([0-9a-z_!~*'()-]+[.])*" // 域名- www.
        		+ "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z][.]" // 二级域名
        		+ "[a-z]{2,6})" // first level domain- .com or .museum
        		+ "(:[0-9]{1,4})?" // 端口- :80
        		+ "((/?)|" // a slash isn't required if there is no file name
        		+ "(/[0-9a-zA-Z_!~*'().;?:@&=+$,%#-]+)+/?)$";
        
        p = Pattern.compile(strRegex); // 验证手机号  
        m = p.matcher(str);  
        b = m.matches();   
        return b;  
    }  
}
