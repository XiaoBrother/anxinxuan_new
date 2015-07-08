package com.ncpzs.util;

public class EANValidate {
	 
    
	
 	 /**
 	  * ����У����
 	  * �Ӵ���λ�����2��ʼ������ż��λ�����ִ������Ϊa�� 
  	  * ���ϲ��е�a����3Ϊa�� 
   	  *�Ӵ���λ�����1��ʼ����������λ�����ִ������Ϊb�� 
   	  *��a��b���Ϊc�� 
   	  *ȡc�ĸ�λ��d�� 
   	  *��10��ȥd��ΪУ��λ��ֵ�� 
 	  */
 	private static  int   generateValidateCode(String code){
 		if(code==null) return -1;
 		char[] nums=code.toCharArray();
  		int even= evenNumber(nums);
  		even=even*3;
  		int odd=oddNumber(nums);
   		int num=even+odd;
 		//ȡ��λ��
 		num=num%10;
 		 
 		if(num==0) num=1;
  	    return 10- num;
 	}
 	/**
 	 * ��һ����ż�����
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
 	 * ���������������
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
 	 * У��
 	 * @param unknown $code
 	 */
 	public static boolean validateCode(String code){
  		if(code==null||!ValidPattern.isAllNumber(code)||code.length()!=18) return false;
 		int validcode=Integer.parseInt(code.substring(code.length()-1));//ȡ���һ��У��λ
 		String codedata=code.substring(0,code.length()-1);//ȡ����λ
 		int getcode=generateValidateCode(codedata);
   		if(validcode==getcode)
 			return true;
 		return false;
 	}
 	 
}
