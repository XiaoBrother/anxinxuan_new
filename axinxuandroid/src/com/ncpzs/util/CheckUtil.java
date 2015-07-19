package com.ncpzs.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUtil {

	/** 
     * �ֻ�����֤ 
     *  
     * @param  str 
     * @return ��֤ͨ������true 
     */  
    public static boolean isMobile(String str) {   
        Pattern p = null;  
        Matcher m = null;  
        boolean b = false;   
        p = Pattern.compile("/^13[0-9]{9}|15[012356789][0-9]{8}|18[0-9][0-9]{8}|147[0-9]{8}$/"); // ��֤�ֻ���  
        m = p.matcher(str);  
        b = m.matches();   
        return b;  
    }  
    /** 
     * �ֻ�����֤ 
     *  
     * @param  str 
     * @return ��֤ͨ������true 
     */  
    public static boolean isTel(String str) {   
    	Pattern p = null;  
    	Matcher m = null;  
    	boolean b = false;   
    	p = Pattern.compile("/^[+]{0,1}(\\d){1,4}[ ]{0,1}([-]{0,1}((\\d)|[ ]){1,12})+$/");//������  
    	m = p.matcher(str);  
    	b = m.matches();   
    	return b;  
    }  
	/** 
     * ��ַ��֤
     *  
     * @param  str 
     * @return ��֤ͨ������true 
     */  
    public static boolean isUrl(String str) {   
        Pattern p = null;  
        Matcher m = null;  
        boolean b = false;  
        String strRegex = "^((https|http|ftp|rtsp|mms)?://)"
        		+ "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" // ftp��user@
        		+ "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP��ʽ��URL- 199.194.52.184
        		+ "|" // ����IP��DOMAIN��������
        		+ "([0-9a-z_!~*'()-]+[.])*" // ����- www.
        		+ "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z][.]" // ��������
        		+ "[a-z]{2,6})" // first level domain- .com or .museum
        		+ "(:[0-9]{1,4})?" // �˿�- :80
        		+ "((/?)|" // a slash isn't required if there is no file name
        		+ "(/[0-9a-zA-Z_!~*'().;?:@&=+$,%#-]+)+/?)$";
        
        p = Pattern.compile(strRegex); // ��֤�ֻ���  
        m = p.matcher(str);  
        b = m.matches();   
        return b;  
    }  
}
