package com.axinxuandroid.activity.net;

public class NetResult {
	public static final int RESULT_OF_SUCCESS=1;
	public static final int RESULT_OF_ERROR=0;
	public static final int RESULT_OF_OTHER=-1;
	public int result;
	public String message;
	public Object returnData;
	public NetResult(int result,String message){
		this(result,message,null);
	}
	public NetResult(int result,String message,Object returndata){
		this.result=result;
		this.message=message;
		this.returnData=returndata;
	}
	public void setData(Object data){
		this.returnData=data;
	}
}
