package com.axinxuandroid.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.net.LoadTemplateThread;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.activity.net.TemplateCheckThread;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.view.CommonTopView;
import com.axinxuandroid.activity.view.TemplateWindow;
import com.axinxuandroid.activity.view.ShareWindow.ShareMessageInterface;
import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.SystemNotice;
import com.axinxuandroid.data.Template;
import com.axinxuandroid.data.SystemNotice.SystemNoticeType;
import com.axinxuandroid.service.SystemNoticeService;
import com.axinxuandroid.service.TemplateService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.DensityUtil;
import com.ncpzs.util.LogUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class TemplateSynchroniseActivity extends NcpZsActivity {
	private ProgressDialog processDialog;
	private TemplateService tempservice;
	private SystemNoticeService noticeservice;
 	private LinearLayout updatelay, addlay;
	private ListView updatelist, addlist;
 	private TemplateListAdapter updateadapter, addadapter;
	private TemplateWindow twindow;
	private CommonTopView topview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.template_synchronise);
		tempservice = new TemplateService();
		noticeservice=new SystemNoticeService();
		updatelay = (LinearLayout) this
				.findViewById(R.id.tempate_synch_updatelayout);
		addlay = (LinearLayout) this.findViewById(R.id.tempate_synch_addlayout);
		updatelist = (ListView) this.findViewById(R.id.tempate_synch_updates);
		addlist = (ListView) this.findViewById(R.id.tempate_synch_adds);
		topview = (CommonTopView) this.findViewById(R.id.tempate_synch_topview);

		updateadapter = new TemplateListAdapter(this);
		addadapter = new TemplateListAdapter(this);
		updatelist.setAdapter(updateadapter);
		addlist.setAdapter(addadapter);
		twindow = new TemplateWindow(this);
		SystemNotice notice=noticeservice.getByType(SystemNoticeType.NOTICE_TYPE_TEMPLATE);
		if(notice!=null){
			new Thread() {
				@Override
				public void run() {
					prepareUpdate();
				}
			}.start();
		}else{
			NcpzsHandler handler=Gloable.getInstance().getCurHandler();
  	    	handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
 				@Override
				public void onHandlerFinish(Object result) {
 					TemplateSynchroniseActivity.this.finish();
				}
			});
  	    	handler.excuteMethod(new MessageDialogHandlerMethod("", "没有可更新的记录模板!"));
		}
 
	}

	public void prepareUpdate() {
		NcpzsHandler handler = Gloable.getInstance().getCurHandler();
 		handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
			@Override
			public void onHandlerFinish(Object result) {
				if (result != null)
					processDialog = (ProgressDialog) ((Map) result)
							.get("process");
				startUpdate();
 			}
		});
		handler.excuteMethod(new ProcessDialogHandlerMethod("", "更新中..."));

	}

	public void startUpdate() {
		String lastoptime=tempservice.getLatoptime();
		LoadTemplateThread th=new LoadTemplateThread(lastoptime);
 		th.setLiserner(new NetFinishListener() {
 			 
 			@Override
			public void onfinish(NetResult data) {
  				String message="";
 				if(data.result==NetResult.RESULT_OF_SUCCESS){
  					List<Template> redatas=(List<Template>) data.returnData;
 					if(redatas!=null){
 						for (Template tm : redatas) {
								tempservice.saveOrUpdateTemplate(tm);
						}
  						message="更新完成！";
 					}else{
 						message="没有可更新的记录模板！";
 					}
 					noticeservice.deleteByType(SystemNoticeType.NOTICE_TYPE_TEMPLATE);
  				}
 				updateFinish(message);
			}
 			
		});
 		th.start();
 	}

	public void updateFinish(final String message) {
 		final NcpzsHandler handler = Gloable.getInstance().getCurHandler();
		handler.post(new Runnable() {
			@Override
			public void run() {
				if (processDialog != null)
					processDialog.dismiss();
				handler
						.setOnHandlerFinishListener(new OnHandlerFinishListener() {
							@Override
							public void onHandlerFinish(Object result) {
								TemplateSynchroniseActivity.this.finish();
							}
						});
				handler
						.excuteMethod(new MessageDialogHandlerMethod("",
								message));
			}
		});
	}

	/**
	 * 模板窗口
	 * 
	 * @param rec
	 */
	public void showTemplateWindow(View view, final Template tmp) {
		if (twindow != null) {
			twindow.setTemplate(tmp);
			// twindow.showAtLocation(view, Gravity.TOP|Gravity.LEFT, 0,0);
			twindow.showAsDropDown(view, 0, -DensityUtil.dip2px(10));
		}
	}

	@Override
	protected void onDestroy() {
		if (twindow != null)
			twindow.dismiss();
		if (processDialog != null)
			processDialog.dismiss();
		super.onDestroy();
	}

	public class TemplateListAdapter extends BaseAdapter {

		private List<Template> datas;
		private Context context;
		private LayoutInflater inflater;

		public TemplateListAdapter(Context context) {
			this.context = context;
			inflater = LayoutInflater.from(context);
		}

		public void setDatas(List<Template> datas) {
			this.datas = datas;
		}

		@Override
		public int getCount() {
			return datas == null ? 0 : datas.size();
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View arg1, ViewGroup arg2) {
			final Template tmp = datas.get(position);
			if (tmp != null) {
				final View view = inflater.inflate(
						R.layout.template_synchronise_item, null);
				TextView text = (TextView) view
						.findViewById(R.id.tempate_synch_item_text);
				text.setText(tmp.getLabel_name());
				view.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						LogUtil.logInfo(getClass(), "click view...");
						showTemplateWindow(view, tmp);
					}
				});
				return view;
			}
			return null;
		}

	}

}
