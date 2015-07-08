package com.axinxuandroid.activity;

import java.util.List;
 
import org.json.JSONArray;
import org.json.JSONObject;
 
import com.axinxuandroid.activity.view.ResourcesView;
 
import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.RecordResource;
 
import com.ncpzs.util.LogUtil;

 
 import android.content.Intent;
 
import android.os.Bundle;
 
import android.view.View;
 import android.view.View.OnClickListener;
 

public class EditRecordActivity extends AddRecordActivity {
 	@Override
	protected void onCreate(Bundle savedInstanceState) {
	   super.onCreate(savedInstanceState);
	   Long recordid=this.getIntent().getLongExtra("id", 0);
	   record=this.recordservice.getById(recordid);
 
	   if(record!=null){
		   trace_code =record.getTrace_code();
 		   batch_id = record.getBatch_id()+"";
		   variety_id = record.getVariety_id();
		   labelname = record.getLabel_name();
		   variety_name=record.getVariety_name();
		   villeage_id=record.getVilleage_id();
		   topview.setSubTitle("NO."+trace_code);
	   }
	   LogUtil.logInfo(getClass(), isSelectBatchAndLabel()+"'");
	   if(isSelectBatchAndLabel()){
		    topview.setTitle("编辑记录");
			topview.setSubTitle("选择批次");
			topview.setSubTitleClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent  inte=new Intent(EditRecordActivity.this,SelectBatchActivity.class);
					startActivityForResult(inte, RETURN_FROM_SELECT_BATCH);
				}
			});
		}else{
			topview.setTitle("编辑"+labelname+"记录");
			//topview.hiddenSubTitle();
		}
	   init(record);
	   saveBeforeState=getState();
 	}

 	private void init(Record rec){
 		if(rec!=null){
 			if((rec.getSave_type()&Record.BATCHRECORD_TYPE_OF_TEXT)==Record.BATCHRECORD_TYPE_OF_TEXT){
 				//设置内容
 				this.records_text.setText(rec.getContext());
 			}else if((rec.getSave_type()&Record.BATCHRECORD_TYPE_OF_TEMPLATE)==Record.BATCHRECORD_TYPE_OF_TEMPLATE){
 				initTable(rec);
 				records_text.setVisibility(View.GONE);
 				edittable.setVisibility(View.VISIBLE);
 			}else if((rec.getSave_type()&Record.BATCHRECORD_TYPE_OF_AUDIO)==Record.BATCHRECORD_TYPE_OF_AUDIO){
 				setAudio(rec);
 			}
 			if((rec.getSave_type()&Record.BATCHRECORD_TYPE_OF_VIDEO)==Record.BATCHRECORD_TYPE_OF_VIDEO){
 				setVideo(rec);
  			}
 			if((rec.getSave_type()&Record.BATCHRECORD_TYPE_OF_IMAGE)==Record.BATCHRECORD_TYPE_OF_IMAGE){
 				List<RecordResource> res=rec.getResourceByType(Record.BATCHRECORD_TYPE_OF_IMAGE);
  				if(res!=null&&res.size()>0){
 					for(RecordResource re:res){
 	  					LogUtil.logInfo(getClass(), re.getLocalurl());
   						this.rview.addResource(re.getLocalurl(), ResourcesView.RESROUCETYPE_OF_IMAGE, null,re.getInfo());
 					}
 				}
 			}
 		}
 	}
 	
 	private void initTable(Record rec){
 		try{
 			//String json=rec.getContext().replaceAll("\'", "\"");
 			JSONObject   jsonobj   =   new   JSONObject(rec.getContext());
  	    	JSONArray answers_array=jsonobj.getJSONArray("answers"); 
  	    	if(answers_array!=null){
  	    		for(int j = 0; j<answers_array.length(); j++){
  		    		 JSONObject joa = answers_array.getJSONObject(j);  
  		    		 String joa_name=joa.getString("name");
  		    		 String joa_value=joa.getString("value");
  		    		 if(!joa_name.equals("")){
  		    			 edittable.addItem(j, joa_name,joa_value==null?"":joa_value);
  		    		 }
  		    		 
  		    	}
  	 	    	tablebtn.setImageResource(R.drawable.tableicon);
  	    	}
 		}catch(Exception ex){
			ex.printStackTrace();
		}
 	}
	 
 	private void setAudio(Record rec){
 		try{
 			List<RecordResource> res=rec.getResourceByType(Record.BATCHRECORD_TYPE_OF_AUDIO);
 			if(res!=null&&res.size()>0){
 				records_text.setVisibility(View.GONE);
    			edittable.setVisibility(View.GONE);
    			audioview.setVisibility(View.VISIBLE);
     			String filepath=res.get(0).getLocalurl();
     			audioview.setSavePath(filepath);
     			if(res.get(0).getInfo()!=null)
     			  audioview.setRecordTime(Integer.parseInt(res.get(0).getInfo()));
     			rview.addResource(filepath,ResourcesView.RESROUCETYPE_OF_AUDIO,null,res.get(0).getInfo());
 			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
 	}
 	private void setVideo(Record rec){
 		try{
 			List<RecordResource> res=rec.getResourceByType(Record.BATCHRECORD_TYPE_OF_VIDEO);
 			if(res!=null&&res.size()>0){
 				 for(RecordResource re:res){
 					rview.addResource(re.getLocalurl(),ResourcesView.RESROUCETYPE_OF_VIDEO,null,re.getInfo()); 
 				 }
 			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
 	}
 }
