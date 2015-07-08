package com.axinxuandroid.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.anxinxuandroid.constant.AppConstant;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.net.LoadUserRecordThread;
import com.axinxuandroid.activity.net.LoadVareityBatchThread;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.activity.view.CommonTopView;
import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.NetUpdateRecord;
import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.TimeOrderTool;
import com.axinxuandroid.data.Variety;
import com.axinxuandroid.data.Villeage;
import com.axinxuandroid.data.NetUpdateRecord.NetUpdateRecordTime;
import com.axinxuandroid.service.BatchService;
import com.axinxuandroid.service.NetUpdateRecordService;
import com.axinxuandroid.service.RecordService;
import com.axinxuandroid.service.VarietyService;
import com.axinxuandroid.service.VilleageService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.BitmapUtils;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.ImgUploadUtil;
import com.ncpzs.util.ValidPattern;
import com.stay.pull.lib.PullToRefreshBase;
import com.stay.pull.lib.PullToRefreshDragListView;
import com.stay.pull.lib.PullToRefreshBase.OnRefreshListener;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class VarietyBatchActivity  extends NcpZsActivity{
	private static final int TAB_INDEX_VAREITYRECORD = 1;
    private VilleageService vilservice;
    private VarietyService varservice;
	private int villeageid;
	private int varietyid;
	private CommonTopView topview;
	private EditText serachtext;
	private ImageButton serachbtn;
	private ListView lview;
	private PullToRefreshDragListView dragview;
	private List<Batch> showbatchs;
	 private List<Batch> allbatchs;
	private boolean ispulldown = true;// 是否是下拉刷新
	private ProgressDialog progress;
	private NetUpdateRecord varietyupdaterecord;
	private NetUpdateRecordService netupdateservice;
	private BatchService batchservice;
	private VarietyBatchAdapter batchadapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);
 		this.setContentView(R.layout.varietybatch);
 		villeageid=this.getIntent().getIntExtra("villeageid", 0);
 		varietyid=this.getIntent().getIntExtra("varietyid", 0);
 		vilservice=new VilleageService();
 		varservice=new VarietyService();
 		topview=(CommonTopView) this.findViewById(R.id.varietybatch_topview);
 		serachtext=(EditText) this.findViewById(R.id.varietybatch_seachtext);
 		serachbtn=(ImageButton) this.findViewById(R.id.varietybatch_seachbtn);
 		dragview=(PullToRefreshDragListView) this.findViewById(R.id.varietybatch_list);
 		dragview.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(int direct) {
				if (direct == PullToRefreshBase.MODE_PULL_DOWN_TO_REFRESH) {
					ispulldown = true;
				} else if (direct == PullToRefreshBase.MODE_PULL_UP_TO_REFRESH) {
					ispulldown = false;
				}
				new Thread() {
					@Override
					public void run() {
						prepareloadData(false);
					}
				}.start();
  			}
		});
 		lview = dragview.getRefreshableView();
 		 
 		netupdateservice=new NetUpdateRecordService();
 		varietyupdaterecord = netupdateservice.getByTypeWithObjId(
				NetUpdateRecord.TYPE_VARIETY_RECORD,varietyid);
 		batchservice=new BatchService();
 		batchadapter=new VarietyBatchAdapter(this);
 		lview.setAdapter(batchadapter);
 		topview.setLeftClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				VarietyBatchActivity.this.finish();
			}
		});
 		Variety variety=varservice.getByVarietyId(varietyid);
 		if(variety!=null)
 			topview.setTitle(variety.getVariety_name());
 		Villeage villeage=vilservice.getByVilleageid(villeageid);
 		if( villeage!=null)
 			topview.setSubTitle(villeage.getVilleage_name());
 		
 		serachtext.addTextChangedListener(new TextWatcher() {
 			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
 			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
 			}
 			@Override
			public void afterTextChanged(Editable s) {
 				filterBatch();
  			}
		});
 		serachbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				filterBatch();
			}
		});
 		 //开始加载网络数据
		new Thread(){
 			@Override
			public void run() {
 				prepareloadData(true);
			}
 		}.start();
	}
	
	
	public class VarietyBatchAdapter extends BaseAdapter {

		private LayoutInflater inflater;

		public VarietyBatchAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return showbatchs == null ? 0 : showbatchs.size();
		}

		@Override
		public Object getItem(int position) {
 			return position;
		}

		@Override
		public long getItemId(int position) {
 			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
		    Batch cbatch = null;
			if(showbatchs!=null){
				cbatch=showbatchs.get(position);
			}
 			final Batch batch=cbatch;
			
			View view=convertView; 
			ViewHolder holder;
			if(view==null){
				view = inflater.inflate(R.layout.varietybatch_item, null);
				holder=new ViewHolder();
				holder.bglayout=view.findViewById(R.id.varietybatchitem_bglayout);
 				holder.code=(TextView) view.findViewById(R.id.varietybatchitem_batchcode);
				holder.userimg=(ImageView) view.findViewById(R.id.varietybatchitem_userimg);
				holder.timetext=(TextView) view.findViewById(R.id.varietybatchitem_date);
				holder.recordcount=(TextView) view.findViewById(R.id.varietybatchitem_recordcount);
 				holder.img=(ImageView) view.findViewById(R.id.varietybatchitem_repeatimg);
 				holder.circleimg=(ImageView) view.findViewById(R.id.varietybatchitem_circleimg);
				view.setTag(holder);
			}else{
				holder=(ViewHolder) view.getTag();
			}
			final View pview=view;
		 
   			if(batch!=null){
    			if((position+1)%2==0){
    				holder.bglayout.setBackgroundColor(Color.rgb(255, 255, 255));
   					holder.circleimg.setImageResource(R.drawable.circlebg1);
   					holder.img.setBackgroundResource(R.drawable.batch_bg_click);
   				}else{
   					holder.bglayout.setBackgroundColor(Color.rgb(246,246,246));
   					holder.circleimg.setImageResource(R.drawable.circlebg2);
   					holder.img.setBackgroundResource(R.drawable.batch_bg_normal);
   				}
    			holder.code.setText(batch.getCode());
    			Bitmap userbtm =getRecycleImg(batch.getUserid()+"");
    			if(userbtm==null)	
    				userbtm=BitmapUtils.getImageBitmap(AppConstant.USERIMG_DIR+batch.getUserid());
				if (userbtm != null) {
					holder.userimg.setImageBitmap(userbtm);
 					addRecycleImg(batch.getUserid()+"", userbtm);
				}else {
					  holder.userimg.setBackgroundResource(R.drawable.defaultpersonimg);
				}
  				if(batch.getCreate_time()!=null){
					holder.timetext.setText( DateUtil.DateToStr(DateUtil.StrToDate(batch.getCreate_time()), "yyyy/MM/dd HH:mm"));
				}
				
 				holder.recordcount.setText(batch.getProducecount() + "条生产者记录/"+batch.getConsumercount()+"条消费者记录");
 				   
 
			}
			
			return view;
		}
	}
	class  ViewHolder { 
	       View bglayout; 
 	       TextView code,timetext,recordcount;
	       ImageView img,circleimg,userimg;
	} 
	 
	/**
	 * 过滤批次
	 */
	public void  filterBatch(){
		String text=serachtext.getText().toString();
		if(text!=null&&this.allbatchs!=null&&this.allbatchs.size()>0){
 			if("".equals(text.trim())) this.showbatchs=allbatchs;
 			else{
 				List<Batch> nbatchs=new ArrayList<Batch>();
 	  			for(Batch batch:this.allbatchs){
 					if(ValidPattern.isAllNumber(text)&&batch.getCode().indexOf(text)>=0){
 						nbatchs.add(batch);
 					} 
 				}
 	  			this.showbatchs=nbatchs;
 			}
			
   			this.batchadapter.notifyDataSetChanged();
  		}
  	}
	/**
	 *准备 加载数据
	 */
	private void prepareloadData(boolean showdialog) {
 		if (showdialog) {
			NcpzsHandler processhandler = Gloable.getInstance().getCurHandler();
			processhandler
					.setOnHandlerFinishListener(new OnHandlerFinishListener() {
						@Override
						public void onHandlerFinish(Object result) {
							if (result != null)
								progress = (ProgressDialog) ((Map) result)
										.get("process");
							startLoadData();
						}
					});
			processhandler.excuteMethod(new ProcessDialogHandlerMethod("",
					"数据加载中...."));
		} else {
			startLoadData();
		}

	}

	/**
	 * 开始 加载数据
	 */
	private void startLoadData() {
		String starttime = null, endtime = null;

		if (ispulldown) {
			starttime = varietyupdaterecord == null ? null : varietyupdaterecord
					.getMaxTime(TAB_INDEX_VAREITYRECORD);
		} else {
			NetUpdateRecordTime time = varietyupdaterecord == null ? null
					: varietyupdaterecord.findNeedToLoadTime(TAB_INDEX_VAREITYRECORD);
			if (time != null) {
				starttime = time.stime;
				endtime = time.etime;
			}
		}
		final String loadmaxtime = endtime;
		final String loadmintime = starttime;
		LoadVareityBatchThread th = new LoadVareityBatchThread(varietyid, starttime, endtime);
		th.setLiserner(new NetFinishListener() {
  			@Override
			public void onfinish(NetResult data) {
  				if (data.result == NetResult.RESULT_OF_SUCCESS) {
  					Map rdata=(Map) data.returnData;
					List<Batch>  nbts= (List<Batch>)rdata.get("datas");
 					if (nbts != null && nbts.size() > 0) {
						for (Batch batch : nbts) {
							batchservice.saveOrUpdateBatch(batch);
 						}
						String[] tims=TimeOrderTool.getMaxMinTime(nbts);
						if(varietyupdaterecord==null){
							varietyupdaterecord=new NetUpdateRecord();
							varietyupdaterecord.setObjid(varietyid);
							varietyupdaterecord.setType(NetUpdateRecord.TYPE_VILLEAGE_BATCH);
						}
						int totalcount=(Integer)rdata.get("datacount");
						if(tims!=null){
							//如果是下拉的刷新，则最大日期和最小日期从数据里获取，如果是上拉更新，则最大日期是查询endtime
							if(ispulldown){
								//如果返回的数据大小大于等于约定的数量，则表示服务器端可能还有未查询的数据
								//否则代表这一时间段的数据已经全部获取到
								if(loadmintime==null){
									varietyupdaterecord.saveOrUpdateStatus(TAB_INDEX_VAREITYRECORD, tims[1], tims[0]);
								}else{
									if(totalcount<=AppConstant.NET_RETURN_MAX_COUNT){
										varietyupdaterecord.saveOrUpdateStatus(TAB_INDEX_VAREITYRECORD, loadmintime,  tims[0]);
									}else{
										varietyupdaterecord.saveOrUpdateStatus(TAB_INDEX_VAREITYRECORD, tims[1],  tims[0]);
									}
								}
 							}
 							else {
 								if(loadmaxtime!=null){
 									if(loadmintime!=null){
 										if(totalcount<=AppConstant.NET_RETURN_MAX_COUNT){
 											varietyupdaterecord.saveOrUpdateStatus(TAB_INDEX_VAREITYRECORD, loadmintime, loadmaxtime);
 										}else{
 											varietyupdaterecord.saveOrUpdateStatus(TAB_INDEX_VAREITYRECORD, tims[1], loadmaxtime);
  										}
 									}else{
 										varietyupdaterecord.saveOrUpdateStatus(TAB_INDEX_VAREITYRECORD, tims[1], loadmaxtime);
 									}
 									
 								}
  							}
							netupdateservice.saveOrUpdate(varietyupdaterecord);
						}
     				} 
				}  
  				allbatchs= batchservice.serachByVarietyId(varietyid);
				finishload();
			}
		});
		th.start();
	}

	/**
	 * 加载完成后处理
	 */
	private void finishload() {
		NcpzsHandler handler = Gloable.getInstance().getCurHandler();
		handler.post(new Runnable() {
			@Override
			public void run() {
 				if (progress != null && progress.isShowing()) {
					progress.dismiss();
				}
 				if(dragview!=null)
 					dragview.onRefreshComplete();
 				showbatchs =allbatchs;
 				batchadapter.notifyDataSetChanged();
			}
		});
	}
}
