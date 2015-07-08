package com.axinxuandroid.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

 
public class SystemSet {

	private int id;
	private int type;
	private String set;//json×Ö·û´®
 	private Map<String,String> setmap;
	
	public SystemSet(){
		setmap=new HashMap<String, String>();
	}
	
	public void addSet(String  key,String value){
		setmap.put(key, value);
	}
	public String getSetValue(String key){
		if(set!=null&&!"".equals(set)){
			try{
				JSONObject jsonObject = new JSONObject(set);
				if(jsonObject.has(key))
					return jsonObject.getString(key);
			}catch (Exception e) {
				e.printStackTrace();
 			}
 		}
		return null;
			
			
	}
	public static class SystemSetType{
 		public static final int SYSTEMSETTYPE_COMMONTYPE=1;
 		public static final String TRUE_VALUE="1";
		public static final String FALSE_VALUE="0";
		public static class CommonType{
			public static final String DEFAULT_SET="{'videowifi':'1','audiowifi':'0','imagewifi':'0','showshare':'1'}";
 			public static final String IMAGE_WIFI="imagewifi";
			public static final String AUDIO_WIFI="audiowifi";
			public static final String VIDEO_WIFI="videowifi";
			public static final String SHOW_SHARE="showshare";
 		}
		 
	}
	
	 
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getSetJsonString() {
		if(setmap.size()>0){
			Iterator its=setmap.entrySet().iterator();
			JSONObject jobj=new JSONObject();
			try{
				while(its.hasNext()){
					Entry nx=(Entry) its.next();
					jobj.put((String)nx.getKey(),(String) nx.getValue());
				}
				set=jobj.toString();
			}catch (Exception e) {
				e.printStackTrace();
 			}
 		}
		return set;
	}

	public void setSet(String set) {
		this.set = set;
	}
   
}
