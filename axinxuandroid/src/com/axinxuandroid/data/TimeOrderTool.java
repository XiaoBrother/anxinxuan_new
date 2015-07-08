package com.axinxuandroid.data;

import java.util.List;

import com.ncpzs.util.DateUtil;

public class TimeOrderTool {

	/**
	 * 
	 * @param times 实现 TimeOrderInterface 接口的类
	 * @return
	 */
	public static String[] getMaxMinTime(List times){
		if(times==null||times.size()<1) return null;
		String[] rtimes=new String[2];
		int size=times.size();
		if(size==1){
			rtimes[0]=((TimeOrderInterface)times.get(0)).getTimeStr();
			rtimes[1]=((TimeOrderInterface)times.get(0)).getTimeStr();
		}
 		else{
			rtimes[0]=((TimeOrderInterface)times.get(0)).getTimeStr();//最大时间
			rtimes[1]=((TimeOrderInterface)times.get(0)).getTimeStr();//最小时间
			for(int i=1;i<size;i++){
				TimeOrderInterface fs=(TimeOrderInterface)times.get(i);
 				if(DateUtil.compare(DateUtil.StrToDate(fs.getTimeStr()), DateUtil.StrToDate(rtimes[0]))>0){
 					rtimes[0]=fs.getTimeStr();
 				}
 				if(DateUtil.compare(DateUtil.StrToDate(fs.getTimeStr()), DateUtil.StrToDate(rtimes[1]))<0){
 					rtimes[1]=fs.getTimeStr();
 				}
			}
		}
		return rtimes;
		
	}
}
