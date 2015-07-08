package com.axinxuandroid.data;

 
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ncpzs.util.DateUtil;
import com.ncpzs.util.LogUtil;
  
 
public class NetUpdateRecord {
	public static final int TYPE_VILLEAGE_BATCH=1;
	public static final int TYPE_RECORD=2;
	public static final int TYPE_LABEL=3;
	public static final int TYPE_TEMPLATE=4;
	public static final int TYPE_COMMENT=5;
	public static final int TYPE_VILLEAGE=6;
	public static final int TYPE_VILLEAGE_PHOTO=7;
	public static final int TYPE_VILLEAGE_BANNER=8;
	public static final int TYPE_USER_RECORD=9;
	public static final int TYPE_VARIETY_RECORD=10;
	private int id;
	private int type;
	private int objid;
	private String statusjson;//更新状态json信息
     private List<NetUpdateRecordStatus> stas;//json转换后的对象
    /**
     * 
     * @author Administrator
     *
     */
    public class NetUpdateRecordTime implements Comparable  {
    	public String stime;//开始时间
    	public String etime;//结束时间
    	public NetUpdateRecordTime(String stime,String etime){
    		this.stime=stime;
    		this.etime=etime;
    	}
    	public NetUpdateRecordTime(){}
    	//比较time是否在当前时间段内
    	public boolean inTime(String time){
    		if(time!=null){
     			if(DateUtil.compare(DateUtil.StrToDate(time), DateUtil.StrToDate(stime))>=0&&DateUtil.compare(DateUtil.StrToDate(time), DateUtil.StrToDate(etime))<=0)
    				return true;
    		}
    		return false;
    		
    	}
    	@Override
    	public int compareTo(Object another) {
     		if( another==null||!(another instanceof NetUpdateRecordTime) ) return -1;
     		NetUpdateRecordTime com=(NetUpdateRecordTime) another;
     		LogUtil.logInfo(getClass(), com.etime+":"+etime);
     		int result= DateUtil.compare(DateUtil.StrToDate(com.etime), DateUtil.StrToDate(etime));
    		if(result==0){
    			return DateUtil.compare(DateUtil.StrToDate(com.stime), DateUtil.StrToDate(stime));
    		}else  return result;
      	}
    }
    /**
     * 
     * @author Administrator
     *
     */
	public class NetUpdateRecordStatus{
		public int id;//标识
		private List<NetUpdateRecordTime> times;//存放开始时间和结束时间
		 
	    public void addTime(String stime,String etime){
 	    	if(times==null){
	    		times=new ArrayList<NetUpdateRecordTime>();
 	    	}
 	    	times.add(new NetUpdateRecordTime(stime,etime));
 	    	Collections.sort(times);//排序
 	    	combineTimes();//合并时间
 	    }
	    private void  combineTimes(){
	    	if(times!=null&&times.size()>1){
	    		int size=times.size();
	    		NetUpdateRecordTime ctime,ntime;
	    		NetUpdateRecordTime  removetime=null;
 	    		for(int i=0;i<size-1;i++){
 	    			ctime=times.get(i);
 	    			ntime=times.get(i+1);
 	    			//如果下一个时间段的结束时间在当前时间段内，则合并
 	    			if(ctime.inTime(ntime.etime)){//合并,
 	    				ctime.stime=ntime.stime;
 	    				removetime=ntime;
 	    				break;
   	    			}
  	    		}
 	    		if(removetime!=null){
 	    			times.remove(removetime);
 	    			combineTimes();
 	    		}
 	    	  
	    	}
	    }
 	    public List getJsonData(){
	    	List data=new ArrayList();
	    	if(times!=null){
 	    		for(NetUpdateRecordTime  tim:times){
	    			Map datamap=new HashMap();
	    			datamap.put("stime",  (tim.stime==null?"":tim.stime) );
	    			datamap.put("etime", (tim.etime==null?"":tim.etime) );
	    			data.add(datamap);
	    		}
 	    	}
	    	return data;
	    }
		@Override
		public String toString() {
			 String str="{id:"+id+",";
			 if(this.times!=null)
				 for(NetUpdateRecordTime time:times)
					 str+="start:"+time.stime+",end:"+time.etime+"";
			 str+="}";
			return str;
		}
	    
	}
	
	public void saveOrUpdateStatus(int id,String starttime,String endtime){
		NetUpdateRecordStatus status=getStatusById(id);
		if(status==null){
			status=new NetUpdateRecordStatus();
			status.id=id;
			if(stas==null) stas=new ArrayList<NetUpdateRecordStatus>();
			stas.add(status);
		}
		status.addTime(starttime, endtime);
		

	}
	
	/**
	 * 
	 * @param statid  标识状态的id
	 * @return
	 */
	private NetUpdateRecordStatus getStatusById(int statid){
		if(stas!=null&&stas.size()>0)
			for(NetUpdateRecordStatus sta:stas)
				if(sta.id==statid)
					return sta;
		return null;
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

 
	public String getStatusjson() {
		List datas=new ArrayList();
		 if(this.stas!=null){
			StringBuffer sb=new StringBuffer();
 			for(NetUpdateRecordStatus sta:stas){
				Map data=new HashMap();
				data.put("id", sta.id);
				data.put("times", sta.getJsonData());
				datas.add(data);
			}
		 }
		 JSONArray jsonarray= new JSONArray(datas);
  		 return jsonarray.toString();
	}

	public void setStatusjson(String statusjson) {
		this.statusjson = statusjson;
 		changeJsonStrToData();
 	}
    
	private void changeJsonStrToData(){
		if(this.statusjson!=null){
			 try{
				 JSONArray datas=new JSONArray(statusjson);
				 if(datas!=null){
					 if(stas==null) stas=new ArrayList<NetUpdateRecordStatus>();
					 stas.clear();
					 int size=datas.length();
					 for(int i=0;i<size;i++){
						 NetUpdateRecordStatus stat=new NetUpdateRecordStatus();
						 JSONObject obj=new JSONObject(datas.getString(i));
						 if(obj!=null){
							 stat.id=obj.getInt("id");
							 if(obj.has("times")){
								 JSONArray times=obj.getJSONArray("times");
								 if(times!=null&&times.length()>0){
									 for(int j=0;j<times.length();j++){
										 JSONObject time=new JSONObject(times.getString(j));
  										 if(time!=null){
  											 stat.addTime(time.getString("stime"), time.getString("etime"));
										 }
									 }
								 }
							 }
						 }
						 stas.add(stat);
					 }
					
				 }
			 }catch(Exception ex){
				 ex.printStackTrace();
			 }
		}
	}
	
	public int getObjid() {
		return objid;
	}

	public void setObjid(int objid) {
		this.objid = objid;
	}
    
	/**
	 * 第一个时间间隔
	 * @param id
	 * @return
	 */
	public NetUpdateRecordTime findNeedToLoadTime(int id){
		 NetUpdateRecordStatus stat=getStatusById(id);
		 if(stat==null) return null;

 		 if(stat.times!=null&&stat.times.size()>0){
			 NetUpdateRecordTime rtime=new NetUpdateRecordTime();
			 NetUpdateRecordTime ftime=stat.times.get(0);
			 if(stat.times.size()>1){
				 NetUpdateRecordTime stime=stat.times.get(1);
				 rtime.stime=stime.etime;
				 rtime.etime=ftime.stime;
			 }else{
				 rtime.etime=ftime.stime;
 			 }
 			 return rtime;
		 }
		 return null;
	}
	/**
	 * 最大时间
	 * @param id
	 * @return
	 */
	public String getMaxTime(int id){
		NetUpdateRecordStatus stat=getStatusById(id);
		 if(stat==null) return null;
		 if(stat.times!=null&&stat.times.size()>0)
	    	  return stat.times.get(0).etime;	//获取最大时间
		 return null;
	}
	/**
	 * 
	 * @param id
	 * @return
	 */
	public String getFirstStartTime(int id){
		 NetUpdateRecordStatus stat=getStatusById(id);
		 if(stat==null) return null;

		 if(stat.times!=null&&stat.times.size()>0){
 			 NetUpdateRecordTime ftime=stat.times.get(0);
			 if(ftime!=null)
			   return ftime.stime;
			 return null;
		 }
		 return null;
	}
	@Override
	public String toString() {
		String str="";
		if(this.stas!=null){
			for(NetUpdateRecordStatus sta:stas)
				str+=sta.toString()+"\n";
		}
 		return str;
	}

	 

	 
	 
}
