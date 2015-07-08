package com.axinxuandroid.activity.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

import com.axinxuandroid.activity.Appstart;
import com.axinxuandroid.activity.CleanActivity;
import com.axinxuandroid.activity.DraftActivity;
import com.axinxuandroid.activity.ProustActivity;
import com.axinxuandroid.activity.R;
import com.axinxuandroid.activity.SystemUpdateActivity;
import com.axinxuandroid.activity.TemplateSynchroniseActivity;
import com.axinxuandroid.activity.TimelineActivity;
import com.axinxuandroid.activity.SytemSetActivity;
import com.axinxuandroid.activity.UserPageActivity;
import com.axinxuandroid.activity.UserFovoriteActivity;
import com.axinxuandroid.activity.VisitHistoryActivity;
import com.axinxuandroid.activity.handler.ConfirmDialogHandlerMethod;
import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.net.AddUserFavoriteThread;
import com.axinxuandroid.activity.net.LoadBatchByCodeOrIdThread;
import com.axinxuandroid.activity.net.LoadUserFavoriteThread;
import com.axinxuandroid.activity.net.LoadVilleageBatchThread;
import com.axinxuandroid.activity.net.LoadRecordThread;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.activity.net.RemoveUserFavoriteThread;
import com.axinxuandroid.activity.view.BatchListView.ViewHolder;
import com.axinxuandroid.activity.view.DragListView.DragListener;
import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.SystemNotice;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.UserFavorite;
import com.axinxuandroid.data.Version;
import com.axinxuandroid.data.Villeage;
import com.axinxuandroid.data.SystemNotice.SystemNoticeType;
import com.axinxuandroid.data.UserFavorite.FavoriteType;
import com.axinxuandroid.oauth.SinaOAuthListener;
import com.axinxuandroid.service.BatchLabelService;
import com.axinxuandroid.service.BatchService;
import com.axinxuandroid.service.RecordService;
import com.axinxuandroid.service.SharedPreferenceService;
import com.axinxuandroid.service.SystemNoticeService;
import com.axinxuandroid.service.UserFovoriteService;
import com.axinxuandroid.service.UserService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.axinxuandroid.sys.gloable.Session;
import com.axinxuandroid.sys.gloable.SessionAttribute;
import com.axinxuandroid.sys.gloable.SystemUpdateService;
import com.ncpzs.util.BitmapUtils;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.DensityUtil;
import com.ncpzs.util.LogUtil;
import com.ncpzs.util.ValidPattern;
import com.stay.pull.lib.PullToRefreshBase;
import com.stay.pull.lib.PullToRefreshDragListView;
import com.stay.pull.lib.PullToRefreshBase.OnRefreshListener;
 
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
 
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
 import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
 
public class UserFavoriteView extends ViewGroup {
	private Context context;
	private PullToRefreshDragListView pullview;
 	private DragListView listview;
 	private UserFovoriteService favservice;
 	private List<UserFavorite> favorites;
	private List<Batch> batchs;
	private UserFovoriteAdapter adapter;
	private ProgressDialog progress;
	private LoadBatchQuene loadquene;
	private BatchService btchservice;
	private UserService uservice;
	private User user;
	private EditText serachtext;
	private ImageButton serachbtn;
	public CommonTopView topview;
  	public UserFavoriteView(Context context) {
		super(context);
		initview(context);
 	}
 	public UserFavoriteView(Context context,AttributeSet sets) {
		super(context,sets);
		initview(context);
  	}

 
 	private void initview(final Context context){
 		this.context=context;
 		LayoutInflater inflater = LayoutInflater.from(context);
		View  view=inflater.inflate(R.layout.userfovorite, null);
         addView(view);
		 pullview=(PullToRefreshDragListView) view.findViewById(R.id.usercollect_list);
 		 favservice=new UserFovoriteService();
		 btchservice=new BatchService();
		 uservice=new UserService();
		 user=uservice.getLastLoginUser();
		 topview= (CommonTopView) view.findViewById(R.id.usercollect_topview);
		 adapter=new UserFovoriteAdapter(context);
		 serachtext=(EditText) view.findViewById(R.id.usercollect_seachtext);
		 serachbtn=(ImageButton) view.findViewById(R.id.usercollect_seachbtn);
		 pullview.setOnRefreshListener(new OnRefreshListener() {
				@Override
				public void onRefresh(int direct) {
 					String lastoptime=null;
 					int maxid=Integer.MAX_VALUE;
					if(favorites!=null){
						maxid=favorites.get(favorites.size()-1).getFavid();
						lastoptime=favorites.get(favorites.size()-1).getLastoptime();
					}
 					if(lastoptime==null)
					    lastoptime=DateUtil.DateToStr(new Date());
					List<UserFavorite> sls=favservice.selectbyUserFavoriteType(FavoriteType.FavoriteType_OF_BATCH,user
							.getUser_id(),lastoptime,maxid);
					if(sls!=null&&sls.size()>0){
						int loadindex=0;
						if(favorites==null) 
						   favorites=sls;
						else {
							loadindex=favorites.size();
							favorites.addAll(sls);
 						}
	 					loadquene.setStartIndex(loadindex);
		 				loadquene.queryNext();//加载用户收藏的批次
					}else{
						pullview.onRefreshComplete();
					}
					
	  			}
			});
		 listview = pullview.getRefreshableView();
		 listview.setAdapter(adapter);
		 listview.setLeftHiddenLen(DensityUtil.dip2px(55));
		 listview.setCanMove(false);
		 listview.setDragListener(new DragListener() {
			
			@Override
			public void onTouchUp(int position, View view) {
				if(view!=null){
 				 //view.findViewById(R.id.usercollect_bglayout).setBackgroundResource(R.drawable.batchlist_normalbg);
				}
			}
			
			@Override
			public void onTouchDown(int position, View view) {
				 if(view!=null){
	 					//view.findViewById(R.id.usercollect_bglayout).setBackgroundResource(R.drawable.batchlist_clickbg);
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
 				 UserFavorite uf=favorites.get(position);
				 if(uf!=null){
					 Batch bh=btchservice.getByBatchId(uf.getFavorite_id());
					 if(bh!=null){
						 Intent inten=new Intent(context,TimelineActivity.class);
						 inten.putExtra("id", bh.getCode());
						 context.startActivity(inten);
					 }
					 
				 }
 			}

			@Override
			public void onBackToInitPosition(View view) {
				// TODO Auto-generated method stub
				
			}

			 
		});
		 batchs=new ArrayList<Batch>();
		 loadquene=new LoadBatchQuene();
		 
//		 new Thread(){
//			 @Override
//				public void run() {
//				 prepareUpdate(true);
//				}
//		 }.start();
//		 
		 serachbtn.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				String text=serachtext.getText().toString();
 				batchs.clear();
 				adapter.notifyDataSetChanged();
 				if(!"".equals(text.trim())){
 					if(ValidPattern.isAllNumber(text)){
 	 					favorites=favservice.serchByBatchCode(FavoriteType.FavoriteType_OF_BATCH, text);
 	 				}else{
 	 					favorites=favservice.serchByName(FavoriteType.FavoriteType_OF_BATCH, text);
 	 				}
 				}else{
 					String lastoptime=DateUtil.DateToStr(new Date());
 					favorites=favservice.selectbyUserFavoriteType(FavoriteType.FavoriteType_OF_BATCH,user
 							.getUser_id(),lastoptime,Integer.MAX_VALUE);
 				}
 				
 				loadquene.queryNext();//加载用户收藏的批次
			}
		 });
         
 	}
 	
 	  
 	//遍历子view，测量宽高，或者在onLayout显示调用view的measure方法，否则不显示控件
  	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
  		int widthSize = MeasureSpec.getSize(widthMeasureSpec);  
  	    int heightSize = MeasureSpec.getSize(heightMeasureSpec);  
   	    measureChildren(widthMeasureSpec, heightMeasureSpec);     
   	    setMeasuredDimension(widthSize, heightSize);  
 	 }
 	//在我们继承ViewGroup时会在除了构造函数之外提供这个方法，我们可以看到，
 	//在ViewGroup的源代码中方法是这样定义的，也就是父类没有提供方法的内容，需要我们自己实现。
  	//当View要为所有子对象分配大小和位置时，调用此方法
 	//在这个类的实现中，需要调用每一个控件的布局方法为其布局。
 	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
  			int childCount = getChildCount();//一个view是通过xml实例化来的，另一个是自己添加的BatchView，这里的个数是2
 	 	    for(int i = 0; i < childCount; i ++){
 	 	         View view = getChildAt(i);
   	             //view.measure(r - l, b - t);  
  	 	        int measuredWidth = view.getMeasuredWidth(); 
 	            view.layout(0,0, measuredWidth, this.getHeight()); 
  	            
  	 	    }
  	}
	
 	public void prepareUpdate(boolean show){
  		if(show){
  			NcpzsHandler handler = Gloable.getInstance().getCurHandler();
  			handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
  				@Override
  				public void onHandlerFinish(Object result) {
  					if (result != null) {
  						progress = (ProgressDialog) ((Map) result)
  								.get("process");
  	 				}
  					updateUserFavorite();
  				}
  			});
  			handler.excuteMethod(new ProcessDialogHandlerMethod("", "正在加载数据..."));
  		}else{
				updateUserFavorite();
   		}
  		
   	}
  	
  	private void updateUserFavorite(){
 		String lastoptime = favservice.getLatoptime();
		LoadUserFavoriteThread lth = new LoadUserFavoriteThread(user
				.getUser_id(), lastoptime);
		lth.setLiserner(new NetFinishListener() {
			 

			@Override
			public void onfinish(NetResult data) {
 				if (data.result == NetResult.RESULT_OF_SUCCESS) {
					List<UserFavorite> fls = (List<UserFavorite>) data.returnData;
					if (fls != null && fls.size() > 0)
						for (UserFavorite ul : fls)
							favservice.saveOrUpdate(ul);
				}
				String lastoptime=DateUtil.DateToStr(new Date());
				favorites=favservice.selectbyUserFavoriteType(FavoriteType.FavoriteType_OF_BATCH,user
						.getUser_id(),lastoptime,Integer.MAX_VALUE);
				loadquene.queryNext();//加载用户收藏的批次
			}
		});
		lth.start();
   	}
  	/**
	 *  
	 */
	private class LoadBatchQuene {
		private int loadindex=0;
   		private UserFavorite getNextUserFavorite(){
  			if(favorites!=null&&favorites.size()>0&&loadindex<favorites.size()){
				return favorites.get(loadindex++);
			}
			return null;
		}
  		public void setStartIndex(int index){
  			loadindex=index;
  		}
		public void queryNext(){
			final UserFavorite fav=getNextUserFavorite();
			if(fav!=null){
 					new Thread(){
	 					@Override
						public void run() {
	 						startLoadBatch(fav.getFavorite_id());
	 					}
	 				}.start();
  			}else{
  				loadindex=0;
  				NcpzsHandler hand=Gloable.getInstance().getCurHandler();
  				hand.post(new Runnable() {
 					@Override
					public void run() {
 						if(progress!=null)
 		 					progress.dismiss();
 						pullview.onRefreshComplete();
					}
				});
  			}
			
  		}
	}
  	
  	private void startLoadBatch(final int batchid){
//  		Batch batch=btchservice.getByBatchId(batchid);
//   		if(batch!=null){
//   			loadFinish(batch);
//  		}else{
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
    	 			} else{
    	 				batch=btchservice.getByBatchId(batchid);
    	 			}
  	 				loadFinish(batch);
				}
  			});
  	  		th.start();
  		//}
  		
  	}
  	private void loadFinish(final Batch batch){
  		NcpzsHandler hand=Gloable.getInstance().getCurHandler();
  		hand.post(new Runnable() {
 			@Override
			public void run() {
 				 if(batch!=null){
 					batchs.add(batch);
 					adapter.notifyDataSetChanged();
 				 }
  				loadquene.queryNext();
			}
		});
  	}
  	/**
  	 * 从现有批次中按id查询
  	 * @param batchid
  	 * @return
  	 */
  	private UserFavorite getByBatchId(int batchid){
  		if(this.favorites!=null){
  			for(UserFavorite bh:favorites){
  				if(bh.getFavorite_id()==batchid)
  					return bh;
  			}
  		}
  		return null;
  	}
  	private void prepareCancelCollect(final Batch batch) {
		NcpzsHandler handler = Gloable.getInstance().getCurHandler();
 		handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
			@Override
			public void onHandlerFinish(Object result) {
				if (result != null) {
					progress = (ProgressDialog) ((Map) result).get("process");
				}
				cancelCollect(batch); 
 			}
		});
 		handler.excuteMethod(new ProcessDialogHandlerMethod("","正在取消收藏..."));
	}
	private void cancelCollect(final Batch batch) {
		// 取消收藏
		final UserFavorite uf=getByBatchId((int)batch.getBatch_id());
 		if (uf != null) {
			RemoveUserFavoriteThread rth = new RemoveUserFavoriteThread(uf
					.getFavid());
			rth.setLiserner(new NetFinishListener() {
 				@Override
				public void onfinish(NetResult data) {
 					if (data.result == NetResult.RESULT_OF_SUCCESS) {
						UserFovoriteService ufservice = new UserFovoriteService();
						ufservice.deleteByFavId(uf.getFavid());
						favorites.remove(uf);
						batchs.remove(batch);
						finishCollect(true);
					} else
						finishCollect(false);
				}
			});
			rth.start();
		} else finishCollect(false);
   	}
	private void finishCollect(final boolean success){
		final NcpzsHandler handler = Gloable.getInstance().getCurHandler();
		handler.post(new Runnable() {
 			@Override
			public void run() {
 				if(progress!=null)
 					progress.dismiss();
 				String message="取消收藏失败！";
 				if(success){
 					message="取消收藏成功！";
 					adapter.notifyDataSetChanged();
 				}
    		    handler.excuteMethod(new MessageDialogHandlerMethod("", message));
 			}
		});
	}
	class  ViewHolder { 
		   LinearLayout bglayout;
	       ImageView repeatimg; 
	       ImageButton collect;
	       TextView batchcode,variety,villeagename,recordcount;
	} 
  	public class UserFovoriteAdapter extends BaseAdapter {

		private LayoutInflater inflater;
 		public UserFovoriteAdapter(Context context) {
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
				view = inflater.inflate(R.layout.userfovorite_item, null);
				holder=new ViewHolder();
 				holder.batchcode=(TextView) view.findViewById(R.id.usercollect_batchcode);
				holder.variety=(TextView) view.findViewById(R.id.usercollect_variety);
				holder.villeagename=(TextView) view.findViewById(R.id.usercollect_villeagename);
				holder.recordcount=(TextView) view.findViewById(R.id.usercollect_recordcount);
				holder.collect=(ImageButton) view.findViewById(R.id.usercollect_collect);
				holder.repeatimg= (ImageView) view.findViewById(R.id.usercollect_repeatimg);
				holder.bglayout= (LinearLayout) view.findViewById(R.id.usercollect_bglayout);
 				view.setTag(holder);
			}else{
				holder=(ViewHolder) view.getTag();
			}
  			final Batch batch=cbatch;
  			if(batch!=null){
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
			 
 			}else{
 				holder.villeagename.setText("该批次已经不存在，请取消改收藏！");
			}
  			holder.collect.setOnClickListener(new OnClickListener() {
					@Override
				public void onClick(View v) {
						NcpzsHandler hand=Gloable.getInstance().getCurHandler();
						hand.setOnHandlerFinishListener(new OnHandlerFinishListener() {
							@Override
						public void onHandlerFinish(Object result) {
								int usersel=(Integer)((Map)result).get("result");
								if(usersel==1){
									prepareCancelCollect(batch);
								}
						}
					});
						hand.excuteMethod(new ConfirmDialogHandlerMethod("","确定要取消收藏吗？"));
				}
			});
			return view;
		}
	}
}
