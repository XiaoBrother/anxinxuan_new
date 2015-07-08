package com.ncpzs.util;

public class EANValidate {
	 
    
	
 	 /**
 	  * 生成校验码
 	  * 从代码位置序号2开始，所有偶数位的数字代码求和为a。 
  	  * 将上步中的a乘以3为a。 
   	  *从代码位置序号1开始，所有奇数位的数字代码求和为b。 
   	  *将a和b相加为c。 
   	  *取c的个位数d。 
   	  *用10减去d即为校验位数值。 
 	  */
 	private static  int   generateValidateCode(String code){
 		if(code==null) return -1;
 		char[] nums=code.toCharArray();
  		int even= evenNumber(nums);
  		even=even*3;
  		int odd=oddNumber(nums);
   		int num=even+odd;
 		//取个位数
 		num=num%10;
 		 
 		if(num==0) num=1;
  	    return 10- num;
 	}
 	/**
 	 * 第一步，偶数求和
 	 */
 	private static  int evenNumber(char[] chs){
 		int size=chs.length;
 		int even=0;
     	for(int i=1;i<size;i=i+2){
   			even+=Integer.parseInt(chs[i]+"");
  		}
   		return even;
 	}
 	/**
 	 * 第三步，奇数求和
 	 */
 	private static int oddNumber(char[] chs){
 		int size=chs.length;
 		int odd=0;
 		for(int i=0;i<size;i=i+2){
 			odd+=Integer.parseInt(chs[i]+"");
 		}
 		return odd;
 	}
 	/**
 	 * 校验
 	 * @param unknown $code
 	 */
 	public static boolean validateCode(String code){
  		if(code==null||!ValidPattern.isAllNumber(code)||code.length()!=18) return false;
 		int validcode=Integer.parseInt(code.substring(code.length()-1));//取最后一个校验位
 		String codedata=code.substring(0,code.length()-1);//取数据位
 		int getcode=generateValidateCode(codedata);
   		if(validcode==getcode)
 			return true;
 		return false;
 	}
 	 
}
