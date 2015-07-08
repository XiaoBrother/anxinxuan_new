package com.axinxuandroid.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ncpzs.util.LogUtil;

 

/**
 * 系统版本
 * @author Administrator
 *
 */
public class Version {
	private float version;
	private String downurl;
	private int important=0;
	private String versiondesc;
	private String history;
	private Map<Integer,String> versiondescMap;
 	public static final int IMPORTANT_TRUE=1;
	public static final int IMPORTANT_FALSE=0;
	public Version(){
		versiondescMap=new HashMap<Integer, String>();
 	}
 	public float getVersion() {
		return version;
	}

	public void setVersion(float version) {
		this.version = version;
	}

	public String getDownurl() {
		return downurl;
	}

	public void setDownurl(String downurl) {
		this.downurl = downurl;
	}

	public int getImportant() {
		return important;
	}

	public String getVersiondesc() {
		return versiondesc;
	}
	public void setVersiondesc(String versiondesc) {
		this.versiondesc = versiondesc;
	}
	public String getHistory() {
		return history;
	}
	public void setHistory(String history) {
		this.history = history;
	}
	public void setImportant(int important) {
		this.important = important;
	}
	
	private void versiondescToMap(int num,String desc){
		
		 if(this.versiondesc!=null){
			 try {
				JSONArray descs=new JSONArray(versiondesc);
				if(descs!=null){
					int size=descs.length(); 
					for(int i=0;i<size;i++){
						JSONObject descobj=descs.getJSONObject(i);
						versiondescMap.put(descobj.getInt("num"), descobj.getString("desc"));
					}
				}
			} catch (JSONException e) {
 				e.printStackTrace();
			}
			 
		 }
	}
	public List<Version> historyToList(){
		List<Version> historyList=null;
		try{
			if(this.history!=null){
				historyList=new ArrayList<Version>();
				JSONArray historys=new JSONArray(history);
				if(historys!=null){
					int size=historys.length(); 
					for(int i=0;i<size;i++){
						JSONObject historyobj=historys.getJSONObject(i);
	 					Version vers=new Version();
						vers.setVersion((float)historyobj.getDouble("version"));
						vers.setImportant(historyobj.getInt("important"));
 						historyList.add(vers);
					}
				}
			}	
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			return historyList;
		}
	}
	/**
	 * 判断版本是不是很旧的版本，超过5个版本号就归于旧版本
	 * @return
	 */
	public boolean isOldVersion(float version){
		List<Version> versions=historyToList();
		if(versions==null) 
			return false;
		else{
			for(Version ver:versions){
				if(version>ver.getVersion())
					return false;
			}
			return true;
		}
	}
	@Override
	public String toString() {
 		String str= "version:"+version+",important:"+important+",downurl:"+downurl+"\n";
 		str+=versiondesc+"\n";
 		str+=history+"\n"; 
 		 
 		return str;
	}

	 

}
