package com.axinxuandroid.activity;

import java.util.List;
import java.util.Map;

import com.axinxuandroid.activity.handler.ConfirmDialogHandlerMethod;
import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.net.DeleteProustThread;
import com.axinxuandroid.activity.net.LoadCommentThread;
import com.axinxuandroid.activity.net.LoadProustThread;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.activity.net.SaveCommentThread;
import com.axinxuandroid.activity.view.CommonTopView;
import com.axinxuandroid.activity.view.DragListView;
import com.axinxuandroid.activity.view.DragListView.DragListener;
import com.axinxuandroid.data.Comment;
import com.axinxuandroid.data.Proust;
import com.axinxuandroid.data.ProustItem;
import com.axinxuandroid.data.User;
import com.axinxuandroid.service.ProustService;
import com.axinxuandroid.service.UserService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.axinxuandroid.sys.gloable.Session;
import com.axinxuandroid.sys.gloable.SessionAttribute;
import com.ncpzs.util.LogUtil;
import com.stay.pull.lib.PullToRefreshDragListView;
import com.stay.pull.lib.PullToRefreshBase.OnRefreshListener;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 评论
 * @author Administrator
 *
 */
public class ProustActivity extends NcpZsActivity {
    private static final String NUM_IMG_PREX="num_";
	private int user_id;
	private CommonTopView topview;
	private PullToRefreshDragListView pullview;
	private DragListView listview;
	private ProgressDialog process;
	private List<ProustItem> proustitems;
    private ProustAdapter adapter;
    private ProustService pservice;
    private UserService uservice;
    private User user;
  	@Override
	protected void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);
 		this.setContentView(R.layout.proust);
 		pservice=new ProustService();
 		uservice=new UserService();
 		user=uservice.getLastLoginUser();
 		user_id= this.getIntent().getIntExtra("user_id", -1);
 		topview=(CommonTopView) this.findViewById(R.id.proust_topview);
 		pullview=(PullToRefreshDragListView) this.findViewById(R.id.proust_listview);
 		proustitems=ProustItem.getAllProustItems();
  		topview.setLeftClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				ProustActivity.this.finish();
			}
		});
  		 pullview.setOnRefreshListener(new OnRefreshListener() {
				@Override
				public void onRefresh(int direct) {
 	   				new Thread(){
	 					@Override
						public void run() {
	 						prepareLoad(false);
						}
	   				}.start();
	  				
	  			}
		});
 		adapter=new ProustAdapter(this);
 		listview=pullview.getRefreshableView();
 		listview.setAdapter(adapter);
 		listview.setCanMove(false);
  		new Thread(){
				@Override
			public void run() {
					prepareLoad(true);
			}
		}.start();
	}

	private void prepareLoad(boolean show){
		if(show){
			NcpzsHandler handler=Gloable.getInstance().getCurHandler();
			handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
	 			@Override
				public void onHandlerFinish(Object result) {
	 				if(result!=null)
	 					process = (ProgressDialog) ((Map) result)
						.get("process");
	 				loadProust();
				}
			});
			handler.excuteMethod(new ProcessDialogHandlerMethod("","数据加载中...."));
		}else{
			loadProust();
		}
		
	}
	
	private void loadProust() {
		String lastoptime=pservice.getLatoptime(user_id);
		LoadProustThread th = new LoadProustThread(user_id,lastoptime);
		th.setLiserner(new NetFinishListener() {
			 
			@Override
			public void onfinish(NetResult data) {
 				if (data.result == NetResult.RESULT_OF_SUCCESS) {
					List<Proust> tprousts = (List<Proust>) data.returnData;
					if (tprousts != null && tprousts.size() > 0) {
						for (Proust p : tprousts)
							pservice.saveOrUpdateProust(p);
					}
				}  
				//prousts = pservice.getByUserid(user_id);
 				loadFinish();
			}
		});
		th.start();

	}
	private void loadFinish(){
 		NcpzsHandler handler=Gloable.getInstance().getCurHandler();
 		handler.post(new Runnable() {
 	 			@Override
				public void run() {
	 				if(process!=null)
	 					process.dismiss();
	 				adapter.notifyDataSetChanged();
	 				pullview.onRefreshComplete();
				}
		});
	}
	
//	private void prepareDelete(final Proust p){
//		NcpzsHandler handler=Gloable.getInstance().getCurHandler();
//		handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
// 			@Override
//			public void onHandlerFinish(Object result) {
// 				if(result!=null)
// 					process = (ProgressDialog) ((Map) result)
//					.get("process");
// 				deleteProust(p);
//			}
//		});
//		handler.excuteMethod(new ProcessDialogHandlerMethod("","删除中...."));
//	}
	   
//	private void deleteProust(final Proust p){
//		if(p!=null){
//			DeleteProustThread th=new DeleteProustThread(p.getProust_id(),user);
//			th.setLiserner(new NetFinishListener() {
// 				@Override
//				public void onfinish(Object data) {
// 					int result=(Integer) ((Map)data).get(DeleteProustThread.RESULT);
// 					NcpzsHandler handler=Gloable.getInstance().getCurHandler();
// 					if(result==NetResult.RESULT_OF_SUCCESS){
// 						pservice.deleteByUserIdProustId(p.getUser_id(), p.getProust_id());
// 						prousts.remove(p);
// 						handler.excuteMethod(new MessageDialogHandlerMethod("","删除成功!"));
// 					}else handler.excuteMethod(new MessageDialogHandlerMethod("",(String)((Map)data).get(DeleteProustThread.MESSAGE)));
// 					deleteFinish(result);
//				}
//			});
//			th.start();
//		}else{
//			deleteFinish(NetResult.RESULT_OF_ERROR);
//		}
// 	}
//	
//	private void deleteFinish(int result){
//		if(process!=null)
//			process.dismiss();
//		if(result==NetResult.RESULT_OF_SUCCESS){
//			NcpzsHandler handler=Gloable.getInstance().getCurHandler();
//	 		handler.post(new Runnable() {
//		 			@Override
//					public void run() {
//		 				adapter.notifyDataSetChanged();
//					}
//	 		});
//		}
//  	}
	private class ProustAdapter extends BaseAdapter{
        private LayoutInflater inflater;
        public ProustAdapter(Context context){
        	inflater=LayoutInflater.from(context);
        }
		@Override
		public int getCount() {
 			return proustitems==null?0:proustitems.size();
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
			final ProustItem proust=proustitems.get(position);
			View view=inflater.inflate(R.layout.proust_item, null);
			ImageView fnumimg=(ImageView) view.findViewById(R.id.proust_item_numf);
			ImageView snumimg=(ImageView) view.findViewById(R.id.proust_item_nums);
			int snum=(position+1)/10;
			int fnum=(position+1)%10;
			int resid=0;
			resid=getResources().getIdentifier(NUM_IMG_PREX+fnum,  "drawable", getPackageName());
		    fnumimg.setImageDrawable(getResources().getDrawable(resid));
 			if(snum>0){
				resid=getResources().getIdentifier(NUM_IMG_PREX+snum,  "drawable", getPackageName());
 			}else{
				resid=getResources().getIdentifier(NUM_IMG_PREX+0,  "drawable", getPackageName());
			}
			snumimg.setImageDrawable(getResources().getDrawable(resid));
  			TextView question=(TextView) view.findViewById(R.id.proust_item_question);
			TextView answer=(TextView) view.findViewById(R.id.proust_item_answer);
   			if(proust!=null){
   				if((position+1)%2==0){
   					view.setBackgroundColor(Color.rgb(255, 255, 255));
   				}else{
   					view.setBackgroundColor(Color.rgb(249, 249, 249));
   				}
 				question.setText("Q : "+proust.value);
 				//获取用户的答案
 				final Proust ans=pservice.selectbyUserIdProustId(user_id, proust.key);
 				if(ans!=null)
  			    answer.setText("A : "+ans.getAnswer());
 				if(user_id==user.getUser_id()){
 					  view.setOnClickListener(new OnClickListener() {
 		 				@Override
 						public void onClick(View v) {
 							 Intent inte=new Intent(ProustActivity.this,EditProustActivity.class);
 		 					 inte.putExtra("proustid",proust.key);
  		 					 startActivity(inte);
 						}
 					   });
 				}
   			   
  			}
  
  			return view;
		}
		
	}

	@Override
	protected void onResume() {
		adapter.notifyDataSetChanged();
 		super.onResume();
	}
 
	
	
}
