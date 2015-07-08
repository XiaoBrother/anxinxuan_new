package com.axinxuandroid.activity;




import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

 
import com.anxinxuandroid.constant.AppConstant;
import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.net.LoadBatchByCodeOrIdThread;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.activity.view.CommonTopView;
import com.axinxuandroid.activity.view.DragListView;
import com.axinxuandroid.activity.view.MenuWindow;
import com.axinxuandroid.activity.view.DragListView.DragListener;
import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.UserFavorite;
import com.axinxuandroid.data.UserVisitHistory;
import com.axinxuandroid.data.UserFavorite.FavoriteType;
import com.axinxuandroid.data.UserVisitHistory.UserVisitType;
import com.axinxuandroid.oauth.OAuthConstant;
import com.axinxuandroid.service.BatchService;
import com.axinxuandroid.service.SharedPreferenceService;
import com.axinxuandroid.service.UserFovoriteService;
import com.axinxuandroid.service.UserService;
import com.axinxuandroid.service.UserVisitHistoryService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.axinxuandroid.sys.gloable.Session;
import com.axinxuandroid.sys.gloable.SessionAttribute;
import com.ncpzs.util.BitmapUtils;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.DensityUtil;
import com.ncpzs.util.FileUtil;
import com.ncpzs.util.GPSUtil;
import com.ncpzs.util.LogUtil;
import com.stay.pull.lib.PullToRefreshBase;
import com.stay.pull.lib.PullToRefreshDragListView;
import com.stay.pull.lib.PullToRefreshBase.OnRefreshListener;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
 
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
 
public class VisitHistoryActivity extends NcpZsActivity{
	private UserService userservice;
	private DragListView listview;
	private PullToRefreshDragListView refresh_view;
	private CommonTopView topview;
  	private List<Batch> batchs;
  	private UserVisitHistoryService visitservice;
  	private List<UserVisitHistory> historys;
 	private VisitHistoryAdapter adapter;
	private ProgressDialog progress;
 	private BatchService btchservice;
 	private User user;
  	private SynchronizeBatchQuene synchroizequene;
  	@Override
	public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 this.setContentView(R.layout.visithistory);
		 refresh_view=(PullToRefreshDragListView) this.findViewById(R.id.visithistory_list);
		 topview= (CommonTopView) this.findViewById(R.id.visithistory_topview);
 		 btchservice=new BatchService();
 		 visitservice=new UserVisitHistoryService();
 		 userservice=new UserService();
 		 adapter=new VisitHistoryAdapter(this);
 		 synchroizequene=new SynchronizeBatchQuene();
 		 refresh_view.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(int direct) {
				String visitime=null;
				int loadindex=historys==null?0:historys.size();
 				if(historys!=null){
 					visitime=historys.get(historys.size()-1).getVisittime();
				}
				if(visitime==null)
					visitime=DateUtil.DateToStr(new Date());
 				 getUserHistory(visitime);
				 synchroizequene.setStartIndex(loadindex);
				 prepareSynchronizeBatch(false);
  				
  			}
		});
		 listview = refresh_view.getRefreshableView();
		 listview.setAdapter(adapter);
 		 listview.setCanMove(false);
 		 listview.setDragListener(new DragListener() {
			
			@Override
			public void onTouchUp(int position, View view) {
				if(view!=null){
	 				  //view.findViewById(R.id.visithistory_bglayout).setBackgroundResource(R.drawable.batchlist_normalbg);
				}
			}
			
			@Override
			public void onTouchDown(int position, View view) {
				 if(view!=null){
	 					//view.findViewById(R.id.visithistory_bglayout).setBackgroundResource(R.drawable.batchlist_clickbg);
	  			 }
			}
			
			@Override
			public void onStartMove(int position, View view, float x, float y) {
 				
			}
			
			@Override
			public void onFinishMove(int position, View view, float x, float y) {
 				
			}
			
			@Override
			public void onDragFinsh(int position) {
 				
			}
			
			@Override
			public void onClick(int position) {
 				if(position>-1&&batchs!=null&&batchs.size()>position){
					Intent inten=new Intent(VisitHistoryActivity.this,TimelineActivity.class);
	 			     Batch bh=batchs.get(position);
	 			     if(bh!=null)
				       inten.putExtra("id", bh.getCode());
	  	   			 startActivity(inten);
				}
				 
			}

			@Override
			public void onBackToInitPosition(View view) {
				// TODO Auto-generated method stub
				
			}

			 
		});
		 batchs=new ArrayList<Batch>();
 		 topview.setLeftClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				VisitHistoryActivity.this.finish();
			}
		 });
		 new Thread(){
			 @Override
				public void run() {
				 String visitime=DateUtil.DateToStr(new Date());
				 getUserHistory(visitime);
				 prepareSynchronizeBatch(true);
				}
		 }.start();
		 
  	}
  	/**
  	 * 获取用户的浏览记录
  	 */
  	private void getUserHistory(String visitime){
  		 user=userservice.getLastLoginUser();
	  	 if(user!=null){
	  			historys=visitservice.getByTypeWithUser(UserVisitType.VISIT_TYPE_BATCH, user.getUser_id(), visitime);
	  			if(historys!=null){
	  				List<Batch> tmpbth=new ArrayList<Batch>();
	  				for(UserVisitHistory his:historys){
	  					Batch batch=btchservice.getByBatchId(his.getVisitobjid());
	  					if(batch!=null)
	  						tmpbth.add(batch);
	  				}
	  				batchs.addAll(tmpbth);
 	  			}
	  	}
  	}
  	
  	
  	private void prepareSynchronizeBatch(boolean show){
  		if(show){
  			NcpzsHandler handler = Gloable.getInstance().getCurHandler();
			handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
				@Override
				public void onHandlerFinish(Object result) {
					if (result != null) {
						progress = (ProgressDialog) ((Map) result).get("process");
	 				}
					synchroizequene.queryNext();
				}
			});
		   handler.excuteMethod(new ProcessDialogHandlerMethod("", "同步数据中..."));
  		}else{
  			synchroizequene.queryNext();
  		}
   		
   	}
  	
	private void startSynchronizeBatch(final int batchid){
 			LoadBatchByCodeOrIdThread th=new LoadBatchByCodeOrIdThread(null, batchid+"");
	  		th.setLiserner(new NetFinishListener() {
	 			 

				@Override
				public void onfinish(NetResult data) {
 	 				Batch batch=null;
	 				if(data.result==NetResult.RESULT_OF_SUCCESS){
	  				    batch=(Batch)data.returnData;
	  				    if(batch!=null&&batch.getCode()!=null){
  	  				    btchservice.saveOrUpdateBatch(batch);
	  				    }
	 			    } else batch=btchservice.getByBatchId(batchid);
	 				loadFinish(batch);
				}
			});
	  		th.start();		
	}
   	private void loadFinish(final Batch batch){
  		NcpzsHandler hand=Gloable.getInstance().getCurHandler();
  		hand.post(new Runnable() {
 			@Override
			public void run() {
 				 if(batch!=null){
 					replaceBatch(batch);
 					adapter.notifyDataSetChanged();
 				 }
  				synchroizequene.queryNext();
			}
		});
  	}
   	
   	private void replaceBatch(Batch nbatch){
   		if(this.batchs!=null){
   			int index=0;
   			for(Batch batch:batchs){
   				if(batch.getBatch_id()==nbatch.getBatch_id())
   					break;
   				index++;
   			}
   			batchs.set(index, nbatch);
   		}
   	}
  	
  	/**
	 *  同步
	 */
	private class SynchronizeBatchQuene {
		private int loadindex=0;
   		private Batch getNextBatch(){
  			if(batchs!=null&&batchs.size()>0&&loadindex<batchs.size()){
				return batchs.get(loadindex++);
			}
			return null;
		}
  		public void setStartIndex(int index){
  			loadindex=index;
  		}
		public void queryNext(){
			final Batch fav=getNextBatch();
			if(fav!=null){
				startSynchronizeBatch((int)fav.getBatch_id());
  			}else{
  				loadindex=0;
  				NcpzsHandler hand=Gloable.getInstance().getCurHandler();
  				hand.post(new Runnable() {
 					@Override
					public void run() {
 						if(progress!=null)
 		 					progress.dismiss();
 						refresh_view.onRefreshComplete();
					}
				});
  			}
			
  		}
	}
  	
  	
  	class  ViewHolder { 
  		   LinearLayout bglayout; 
  	       ImageView repeatimg; 
	       TextView batchcode,variety,villeagename,recordcount;
	} 
  	public class VisitHistoryAdapter extends BaseAdapter {

		private LayoutInflater inflater;
 		public VisitHistoryAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return batchs == null ? 0 : batchs.size();
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
			if(batchs!=null&&batchs.size()>0){
				cbatch=batchs.get(position);
			}
			View view=convertView; 
			ViewHolder holder;
			if(view==null){
				view = inflater.inflate(R.layout.visithistory_item, null);
				holder=new ViewHolder();
 				holder.batchcode=(TextView) view.findViewById(R.id.visithistory_batchcode);
				holder.variety=(TextView) view.findViewById(R.id.visithistory_variety);
				holder.villeagename=(TextView) view.findViewById(R.id.visithistory_villeagename);
				holder.recordcount=(TextView) view.findViewById(R.id.visithistory_recordcount);
				holder.bglayout=(LinearLayout) view.findViewById(R.id.visithistory_bglayout);
 				holder.repeatimg= (ImageView) view.findViewById(R.id.visithistory_repeatimg);
 				view.setTag(holder);
			}else{
				holder=(ViewHolder) view.getTag();
			}
 			final Batch batch=cbatch;
  			if(batch!=null){
  				LogUtil.logInfo(getClass(), "position:"+position);
  				if((position+1)%2==0){
    				holder.bglayout.setBackgroundColor(Color.rgb(255, 255, 255));
   					holder.variety.setTextColor(Color.rgb(138, 188, 29));
   					holder.repeatimg.setBackgroundResource(R.drawable.batch_bg_click);
   				}else{
   					holder.bglayout.setBackgroundColor(Color.rgb(246,246,246));
   					holder.variety.setTextColor(Color.rgb(102, 102, 102));
   					holder.repeatimg.setBackgroundResource(R.drawable.batch_bg_normal);
   				}
   				holder.batchcode.setText(batch.getCode());
   				holder.variety.setText(batch.getVariety());
 				if(batch.getVilleage_name()!=null){
 					holder.villeagename.setText(batch.getVilleage_name());
				}
  				holder.recordcount.setText(batch.getProducecount() + "条生产者记录/"+batch.getConsumercount()+"条消费者记录");
  			}
			
			return view;
		}
	}
}