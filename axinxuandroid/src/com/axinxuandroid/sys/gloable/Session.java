package com.axinxuandroid.sys.gloable;

import java.util.HashMap;
import java.util.Map;

public class Session {

	private static Session session;
	private Map<String,Object> datas;
	private Session(){
		datas =new HashMap<String,Object>();
	}
 	public static Session getInstance(){
		if(session==null)
			session=new Session();
		return session;
	}
 	
 	public void setAttribute(String key,Object val){
 		datas.put(key, val);
 	}
 	public Object getAttribute(String key){
 		return datas.get(key);
 	}
 	
 	public void removeAttribute(String key){
  		datas.remove(key);
 	}
}
