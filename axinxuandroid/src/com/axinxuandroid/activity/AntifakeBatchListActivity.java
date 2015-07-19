package com.axinxuandroid.activity;

import java.util.List;
import java.util.Map;

import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.net.LoadAntiFakeBatchListThread;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.activity.view.AntifakeView;
import com.axinxuandroid.activity.view.CommonTopView;
import com.axinxuandroid.data.AntiFakePublishBatch;
import com.axinxuandroid.data.User;
import com.axinxuandroid.service.AntiFakePublishBatchService;
import com.axinxuandroid.service.UserService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.DateUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AntifakeBatchListActivity extends NcpZsActivity {
	private CommonTopView topview;
	private ProgressDialog processDialog;
	private AntifakeView bottomview;
	private UserService uservice;
	private AntiFakePublishBatchService publiccbatchservier;
	private User user;
	private String lastoptime;
	private ListView listView;
	private List<AntiFakePublishBatch> showpbatchs;
	private AntiFakeBatchListAdapter adapter;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.antifake_batchlist);
		topview = (CommonTopView) this.findViewById(R.id.antifake_bathclist_top);

		bottomview = (AntifakeView) this.findViewById(R.id.antifake_batchlist_bottom);
		bottomview.setCurrentItem(AntifakeView.BOTTOM_ITEM_INDEX_OF_BATCHLIST);

		uservice = new UserService();
		user = uservice.getLastLoginUser();
		publiccbatchservier = new AntiFakePublishBatchService();
		lastoptime = publiccbatchservier.getLatoptime(user.getUser_id());

		
		adapter = new AntiFakeBatchListAdapter(this);
        // 得到一个ListView用来显示条目  
        listView = (ListView) findViewById(R.id.antifake_batchlist_lv);  
        // 添加到脚页显示  
       // listView.addFooterView(loadingLayout);  
        // 给ListView添加适配器  
        listView.setAdapter(adapter);  
		
		topview.setLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AntifakeBatchListActivity.this.finish();
			}
		});

		new Thread() {
			@Override
			public void run() {
				prepareloadData(true);
			}
		}.start();
	}

	/**
	 * 准备 加载数据
	 */
	private void prepareloadData(boolean showdialog) {
		if (showdialog) {
			NcpzsHandler processhandler = Gloable.getInstance().getCurHandler();
			processhandler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
				@Override
				public void onHandlerFinish(Object result) {
					if (result != null)
						processDialog = (ProgressDialog) ((Map) result).get("process");
					startLoadData();
				}
			});
			processhandler.excuteMethod(new ProcessDialogHandlerMethod("", "数据加载中...."));
		} else {
			startLoadData();
		}

	}

	/**
	 * 开始 加载数据
	 */
	private void startLoadData() {
		//String lastoptime = null;
		LoadAntiFakeBatchListThread th = new LoadAntiFakeBatchListThread(user.getUser_id(), lastoptime);
		th.setLiserner(new NetFinishListener() {
			@Override
			public void onfinish(NetResult data) {
				if (data.result == NetResult.RESULT_OF_SUCCESS) {
					Map rdata = (Map) data.returnData;
					//publiccbatchservier.clearData();
					List<AntiFakePublishBatch> pbatchlist = (List<AntiFakePublishBatch>) rdata.get("datas");
					if (pbatchlist != null && pbatchlist.size() > 0) {
						for (AntiFakePublishBatch pbatch : pbatchlist) {
							publiccbatchservier.saveOrUpdateBatch(pbatch);
						}
					}
					loadFinish();
				}
			}
		});
		th.start();
	}
	/**
	 * 加载完成
	 */
	private void loadFinish() {
		NcpzsHandler handler=Gloable.getInstance().getCurHandler();
    	handler.post(new Runnable() {
			@Override
			public void run() {
				if (processDialog != null ) {
					processDialog.dismiss();
				}
				showpbatchs=publiccbatchservier.getByUserId(user.getUser_id());
				adapter.notifyDataSetChanged();
 			}
		});
	}
	public class AntiFakeBatchListAdapter extends BaseAdapter {

		private LayoutInflater inflater;

		public AntiFakeBatchListAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return showpbatchs == null ? 0 : showpbatchs.size();
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
		    AntiFakePublishBatch cbatch = null;
			if(showpbatchs!=null){
				cbatch=showpbatchs.get(position);
			}
 			final AntiFakePublishBatch batch=cbatch;
			
			View view=convertView; 
			ViewHolder holder;
			if(view==null){
				view = inflater.inflate(R.layout.antifake_batchlist_item, null);
				holder=new ViewHolder();
				holder.id=(TextView) view.findViewById(R.id.antifake_batchlist_id);
				holder.batchtime=(TextView) view.findViewById(R.id.antifake_bathlist_batchtime);
				holder.snum=(TextView) view.findViewById(R.id.antifake_batchlist_snum);
				holder.endnum=(TextView) view.findViewById(R.id.antifake_batchlist_endnum);
				holder.tcount=(TextView) view.findViewById(R.id.antifake_batchlist_tcount);
				holder.bdesc=(TextView) view.findViewById(R.id.antifake_batchlist_bdesc);
				view.setTag(holder);
			}else{
				holder=(ViewHolder) view.getTag();
			}
		//	final View pview=view;
		 
   			if(batch!=null){
  				holder.batchtime.setText(DateUtil.DateToStr(DateUtil.StrToDate(batch.getBatchtime()), "yyyy-MM-dd"));
  				holder.id.setText(batch.getId()+"");
  				holder.snum.setText(batch.getSnum());
  				holder.endnum.setText(batch.getEndnum());
  				holder.tcount.setText(batch.getTcount()+"");
  				holder.bdesc.setText(batch.getBdesc());
 
			}
			return view;
		}
		
	}
	class  ViewHolder { 
	       TextView batchtime,snum,endnum,tcount,bdesc,id;
	} 

}
