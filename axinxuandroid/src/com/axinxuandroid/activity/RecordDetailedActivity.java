package com.axinxuandroid.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import com.anxinxuandroid.constant.AppConstant;
import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.net.DeleteCommentThread;
import com.axinxuandroid.activity.net.LoadBatchByCodeOrIdThread;
import com.axinxuandroid.activity.net.LoadCommentThread;
import com.axinxuandroid.activity.net.LoadImageQueue;
import com.axinxuandroid.activity.net.LoadNetImgThread;
import com.axinxuandroid.activity.net.LoadRecordByIdThread;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.activity.net.SaveCommentThread;
import com.axinxuandroid.activity.net.LoadImageQueue.ImageItem;
import com.axinxuandroid.activity.view.CommentAtClickSpan;
import com.axinxuandroid.activity.view.CommonTopView;
import com.axinxuandroid.activity.view.EditTabelView;
import com.axinxuandroid.activity.view.ImageGalleryView;
import com.axinxuandroid.activity.view.MoreWindow;
import com.axinxuandroid.activity.view.ShareWindow;
import com.axinxuandroid.activity.view.TimeLineItemView;
import com.axinxuandroid.activity.view.ShareWindow.ShareMessageInterface;
import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.Comment;
import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.RecordResource;
import com.axinxuandroid.data.User;
import com.axinxuandroid.service.BatchService;
import com.axinxuandroid.service.CommentService;
import com.axinxuandroid.service.RecordResourceService;
import com.axinxuandroid.service.RecordService;
import com.axinxuandroid.service.UserService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.axinxuandroid.sys.gloable.Session;
import com.axinxuandroid.sys.gloable.SessionAttribute;
 import com.ncpzs.util.BitmapUtils;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.DensityUtil;
import com.ncpzs.util.FileUtil;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * 信息详细页
 * 
 * @author Administrator
 * 
 */
@SuppressLint("NewApi")
public class RecordDetailedActivity extends NcpZsActivity {
	private static final int RETURN_FROM_COMMENT=0;
	private Record record;
	private Batch  batch;
 	private ImageView user_img;
	private TextView user_username,commentcount;
	private TextView send_time;
 	private ListView listview;
	private ProgressDialog process;
	private BatchService batchservice;
	private EditTabelView jsonTable;
	private ImageGalleryView imggallery;
	private LinearLayout list_header;
  	private CommentAdapter adapter;
 	private Handler handler = new Handler();
	private List<Comment> comments;
	private CommonTopView topview;
	private TextView timeitem_context;
	private List<Bitmap> bmps;
    private MoreWindow morewindow;
    private EditText commenttext;
    private Button sendbtn;
    private boolean isshow=false;
    private User user;
    private UserService uservice;
    private LoadImageQueue loadqueue;
    private RecordService rservice;
    private CommentService commentservice;
    private View userinfodesc;
    private int recordid;
 	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		this.setContentView(R.layout.record_detailed);
		topview = (CommonTopView) this.findViewById(R.id.record_detaile_topview);
		listview = (ListView) this.findViewById(R.id.record_detaile_comment_listview);
		recordid=this.getIntent().getIntExtra("recordid", -1);
		batchservice = new BatchService();
		uservice=new UserService();
		rservice=new RecordService();
		user=uservice.getLastLoginUser();
		commentservice=new CommentService();
 		list_header = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.record_detailed_head, null);
 		userinfodesc= list_header.findViewById(R.id.record_detaile_userinfodesc);
		timeitem_context = (TextView) list_header
				.findViewById(R.id.record_detaile_timeitem_context);
		commentcount=(TextView) list_header.findViewById(R.id.record_detaile_timeitem_commentcount);
		commenttext=(EditText) this.findViewById(R.id.record_detaile_comment_comenttext);
		sendbtn=(Button) this.findViewById(R.id.record_detaile_comment_add);
		jsonTable = (EditTabelView) list_header
				.findViewById(R.id.record_detaile_timeitem_tablelayout);
		user_img = (ImageView) list_header.findViewById(R.id.record_detaile_user_img);
		user_username = (TextView) list_header.findViewById(R.id.record_detaile_user_username);
		send_time = (TextView) list_header.findViewById(R.id.record_detaile_send_time);
		imggallery = (ImageGalleryView) list_header
				.findViewById(R.id.record_detaile_showres_imggallery);
 		bmps = new ArrayList<Bitmap>();
 		adapter = new CommentAdapter(this);
		listview.addHeaderView(list_header);
		listview.setAdapter(adapter);
		loadqueue=new LoadImageQueue();
		loadqueue.setLiserner(new NetFinishListener() {
  			@Override
			public void onfinish(NetResult data) {
				final ImageItem item=(ImageItem) data.returnData;
 				handler.post(new Runnable() {
 					@Override
					public void run() {
  						updateViewResourceData((RecordResource)item.res,item.index);
					}
				});
			}
		});
 		topview.setLeftClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                RecordDetailedActivity.this.finish();
			}

		});
 		topview.setRightClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				if(morewindow!=null){
 					if(isshow){
 	  					morewindow.dismiss();
 	 					isshow=false;
 	 				}
 	 				else {
 	 					morewindow.showAsDropDown(topview.getRightimg(),-DensityUtil.dip2px(150),-DensityUtil.dip2px(20));
 	 					isshow=true;
 	 				}
 				}
  				
			}
		});
 		sendbtn.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				parepareSaveComment();
			}
		});
 		record = rservice.getByRecordId(recordid);
 	 
 		new Thread(){
	 			@Override
				public void run() {
					prepareData();
				}
	 	}.start();
  	}

	

	private void initData() {
 	    if(record!=null){
	    	String title_time = DateUtil.DateToStr(DateUtil.StrToDate(record
					.getSend_date()), "yyyy年MM月dd日");
		    send_time.setText(DateUtil.DateToStr(DateUtil.StrToDate(record
					.getSend_date()), "yyyy/MM/dd HH:mm"));
			user_username.setText(record.getNick_name());
			if (record.getUser_img() != null) {
				Bitmap userbtm = BitmapUtils.getImageBitmap(record.getUser_img());
				if (userbtm != null) {
					user_img.setImageBitmap(userbtm);
					bmps.add(userbtm);
				}
			} else {
				user_img.setBackgroundResource(R.drawable.defaultpersonimg);
			}
			topview.setTitle(record.getLabel_name());
			topview.setSubTitle(title_time);
	  	    
	 		if ((record.getSave_type() & Record.BATCHRECORD_TYPE_OF_TEMPLATE) == Record.BATCHRECORD_TYPE_OF_TEMPLATE)
				setTable(record.getContext());
			else if ((record.getSave_type() & Record.BATCHRECORD_TYPE_OF_TEXT) == Record.BATCHRECORD_TYPE_OF_TEXT) {
				if(record.getContext()!=null&&!"".equals(record.getContext())){
					timeitem_context.setVisibility(View.VISIBLE);
					timeitem_context.setText(record.getContext());
				}
	 		}
			List<RecordResource> res=record.getResourceByType(Record.BATCHRECORD_TYPE_OF_IMAGE);
			if(res!=null&&res.size()>0){
				int  index=0;
				for(RecordResource re:res){
	 					Bitmap btm = null;
						if(re.getLocalurl()!=null&&FileUtil.hasFile(re.getLocalurl())){
							btm=BitmapUtils.getCompressImage(re.getLocalurl());
								//BitmapUtils.getImageBitmap(re.getLocalurl());
						}else if(re.getNeturl()!=null){
							btm=BitmapFactory.decodeResource(getResources(), R.drawable.default_load);
							ImageItem item=loadqueue.getFinishItem(re,imggallery);
							if (item== null)// 如果没加载过，则进行加载
								loadqueue.addItem(re, imggallery,index);
							else btm=BitmapUtils.getCompressImage(((RecordResource)item.res).getLocalurl());
						}
						if(btm!=null){
							final Bitmap addbtm=btm;
							handler.post(new Runnable() {
									@Override
								public void run() {
									imggallery.addImage(addbtm);										
								}
							});
						}
						index++;
	  			}
			}
			userinfodesc.setOnClickListener(new OnClickListener() {
	 			@Override
				public void onClick(View v) {
	 				if(record.getUser_id()!=null){
	 					Intent inte=new Intent(RecordDetailedActivity.this,UserPageActivity.class);
	 	 				inte.putExtra("userid", Integer.parseInt(record.getUser_id()));
	 	 				startActivity(inte);
	 				}
	 			}
			});
	    }
 	    
 	}
	//加载批次和评论内容
	public void prepareData() {
		NcpzsHandler handler = Gloable.getInstance().getCurHandler();
		handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
			@Override
			public void onHandlerFinish(Object result) {
				if (result != null)
					process = (ProgressDialog) ((Map) result).get("process");
 				// 先加载记录信息,批次信息，在加载评论
				loadRecord();
			}
		});
		ProcessDialogHandlerMethod pdh=new ProcessDialogHandlerMethod("", "加载中....");
		pdh.isAutoClose(false);
		handler.excuteMethod(pdh);
	}
	private void setTable(String rec) {
 		jsonTable.setVisibility(View.VISIBLE);
		jsonTable.setTextColor(Color.rgb(111, 110, 113));
		jsonTable.setReadOnly();
		jsonTable.setShowEmpty(false);
		jsonTable.clear();
 		try {
 			JSONObject jsonobj = new JSONObject(rec);
			JSONArray answers_array = jsonobj.getJSONArray("answers");
			for (int j = 0; j < answers_array.length(); j++) {
				JSONObject joa = answers_array.getJSONObject(j);
				String joa_name = joa.getString("name");
				String joa_value = joa.getString("value");
				jsonTable.addItem(j, joa_name, joa_value);

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
 	}
	
	/**
	 * 加载记录信息
	 */
	private void loadRecord() {
  		if(record==null){
  			LoadRecordByIdThread lth=new LoadRecordByIdThread(recordid);
  		     lth.setLiserner(new NetFinishListener() {
 					@Override
					public void onfinish(NetResult data) {
  						if (data.result == NetResult.RESULT_OF_SUCCESS) {
  							record=(Record)data.returnData;
		   					if(record!=null){
 		   						rservice.saveOrUpdate(record);
		  					}
 						}
  						loadRecordFinish();
					}
				});
  			  lth.start();
  			}else loadRecordFinish();
  }
 private void loadRecordFinish(){
	final NcpzsHandler handler=Gloable.getInstance().getCurHandler();
	handler.post(new Runnable() {
			@Override
		public void run() {
				if(record!=null)
				  initData();
				else{
					handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
 						@Override
						public void onHandlerFinish(Object result) {
 							RecordDetailedActivity.this.finish();
						}
					});
					handler.excuteMethod(new MessageDialogHandlerMethod("","未查询到相关记录信息！"));
				}
		}
	});
	loadBatch();//加载批次信息
 }
	/**
	 * 加载批次信息
	 */
	private void loadBatch() {
  		if(record!=null){
  			batch=batchservice.getByBatchId(record.getBatch_id());
  			if(batch==null){
  				LoadBatchByCodeOrIdThread lth=new LoadBatchByCodeOrIdThread(null,record.getBatch_id()+"");
  				lth.setLiserner(new NetFinishListener() {
 					@Override
					public void onfinish(NetResult data) {
  						if (data.result == NetResult.RESULT_OF_SUCCESS) {
 							batch=(Batch)data.returnData;
		   					if(batch!=null){
		   						batchservice.saveOrUpdateBatch(batch);
		  					}
 						}
 						loadBatchFinish();
					}
				});
  				lth.start();
  			}else loadBatchFinish();
 		}else{
			loadFinish();
		}
 	}
	private void loadBatchFinish(){
		NcpzsHandler handler=Gloable.getInstance().getCurHandler();
		handler.post(new Runnable() {
 			@Override
			public void run() {
 				morewindow=new MoreWindow(RecordDetailedActivity.this, new int[]{MoreWindow.SHARE_COMMENTS_FUN,MoreWindow.SHARE_WEIBO_FUN});
 		    	morewindow.addData(MoreWindow.DATA_BATCH, batch);
 		    	morewindow.addData(MoreWindow.DATA_RECORD, record);
 		    	morewindow.addData(MoreWindow.DATA_USER, user);
			}
		});
		loadComment();
	}
	/**
	 * 加载评论
	 */
	private void loadComment() {
 		if(record!=null){
			String lastoptime=commentservice.getLatoptime(record.getRecord_id());
			LoadCommentThread th = new LoadCommentThread(record.getRecord_id(),lastoptime);
			th.setLiserner(new NetFinishListener() {
				 

				@Override
				public void onfinish(NetResult data) {
					List<Comment> tcomments =null;
 					if (data.result == NetResult.RESULT_OF_SUCCESS) {
						tcomments = (List<Comment>)data.returnData;
	   				    if(tcomments!=null){
	   				    	for(Comment com:tcomments)
	   				    		commentservice.saveOrUpdate(com);
	   				    }
					}
 					loadFinish();
				}
			});
			th.start();
		}else{
			loadFinish();
		}
 	}

	 

	private void loadFinish() {
 		NcpzsHandler handler = Gloable.getInstance().getCurHandler();
		handler.post(new Runnable() {
			@Override
			public void run() {
				if (process != null)
					process.dismiss();
 				adapter.notifyDataSetChanged();
 				if(record!=null)
 				 comments=commentservice.getCommentByRecordid(record.getRecord_id());
				if(comments!=null&&comments.size()>0)
					 commentcount.setText(comments.size()+"条");
				else ((View)commentcount.getParent()).setVisibility(View.GONE);
 			}
		});
	}

	
	private void parepareSaveComment(){
		NcpzsHandler handler=Gloable.getInstance().getCurHandler();
		String context=commenttext.getText().toString();
		if("".equals(context)){
			handler.excuteMethod(new MessageDialogHandlerMethod("", "请输入评论内容！"));
		}else{
			handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
	 			@Override
				public void onHandlerFinish(Object result) {
	 				if(result!=null)
	 					process = (ProgressDialog) ((Map) result)
						.get("process");
	 				saveComment();
				}
			});
			handler.excuteMethod(new ProcessDialogHandlerMethod("","保存中...."));
		}
 	}
	private void saveComment(){
		final String context=commenttext.getText().toString();
		SaveCommentThread th=new SaveCommentThread(record.getRecord_id(),0, context,user);
		th.setLiserner(new NetFinishListener() {
 			@Override
			public void onfinish(NetResult data) {
  				NcpzsHandler handler=Gloable.getInstance().getCurHandler();
 				if(data.result==NetResult.RESULT_OF_SUCCESS){
 					Comment com=(Comment) data.returnData;
 					if(com!=null){
 						commentservice.saveOrUpdate(com);
 						if(comments==null) comments=new ArrayList<Comment>();
 						comments.add(0, com);
 					}
  					//handler.excuteMethod(new MessageDialogHandlerMethod("","评论成功"));
 				}else {
 					//保存到本地
// 					Comment comt=new Comment();
// 					comt.setRecordid(record.getRecord_id());
// 					comt.setContext(context);
// 					comt.setUser_id(user.getUser_id());
// 					comt.setComment_date(DateUtil.DateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
// 					comt.setParent_id(0);
// 					comt.setUser_img(user.getLocal_imgurl());
// 					comt.setUser_name(user.getUser_name());
// 					commentservice.save(comt);
  					handler.excuteMethod(new MessageDialogHandlerMethod("", "评论失败！"));
 				}
 				finishSaveComment();
			}
		});
		th.start();
	}
	private void finishSaveComment(){
 	    NcpzsHandler handler=Gloable.getInstance().getCurHandler();
 		handler.post(new Runnable() {
 			@Override
			public void run() {
				if(process!=null)
					process.dismiss();
				adapter.notifyDataSetChanged();
				commenttext.setText("");
				if(comments!=null&&comments.size()>0)
					 commentcount.setText(comments.size()+"条");
 			}
		});
	}
	 

 

	/**
	 * 更新图片资源信息
	 * 
	 * @param url
	 * @param viewindex
	 */
    private void updateViewResourceData(RecordResource resource,int index) {
  		if (resource != null) {
			try {
				rservice.updateResourceLocalUrl(resource.getId(),(String) resource.getLocalurl());
				Bitmap bmp=BitmapUtils.getCompressImage(resource.getLocalurl());
					//BitmapUtils.getImageBitmap(resource.getLocalurl());
				if(bmp!=null)
				 imggallery.replaceImage(bmp, index);
  			  
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	protected void onDestroy() {
		LogUtil.logInfo(getClass(), "destory record detail");
		if (this.bmps != null && bmps.size() > 0) {
			for (Bitmap bm : bmps) {
				if (bm != null && !bm.isRecycled()) {
					bm.recycle();
				}
			}
		}
		imggallery.releaseSource();
		loadqueue.stopLoad();
		if(morewindow!=null)
			morewindow.dismiss();
		super.onDestroy();
	}
 
	private class CommentAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		public CommentAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return comments == null ? 0 : comments.size();
		}

		@Override
		public Object getItem(int position) {
			if (comments != null)
				return comments.get(position);
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final Comment comment = comments.get(position);
			View view = inflater.inflate(R.layout.record_detail_commentitem, null);
 			TextView username = (TextView) view
					.findViewById(R.id.comment_item_username);
			TextView time = (TextView) view
					.findViewById(R.id.comment_item_time);
			TextView context = (TextView) view
					.findViewById(R.id.comment_item_context);
			ImageView iv = (ImageView) view
					.findViewById(R.id.comment_item_userimg);
			ImageView spimg = (ImageView) view
			.findViewById(R.id.comment_item_repeatimg);
			if((position+1)%2==0){
				view.setBackgroundColor(Color.rgb(255, 255, 255));
				spimg.setBackgroundResource(R.drawable.batch_bg_click);
			}else{
				view.setBackgroundColor(Color.rgb(249, 249, 249));
				spimg.setBackgroundResource(R.drawable.batch_bg_normal);
			}
			Bitmap btm = null;
  			try {
				if (comment != null) {
					username.setText(comment.getUser_name());
 					String contextstr=comment.getContext();
					String replyname=comment.getReplyUserName();
					if(replyname!=null&&!"null".equals(replyname)){
 						SpannableString  spanstr=new SpannableString ("@"+replyname);
 						spanstr.setSpan(new  CommentAtClickSpan(comment), 0, replyname.length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
  						int index=context==null?-1:contextstr.indexOf("@"+replyname);
						if(index>=0){
							 context.setText(contextstr.substring(0, index));
							 context.append(spanstr);
							 context.append(contextstr.substring(index+replyname.length()+1));
  						}else{
  							 context.setText(spanstr);
  							 context.append(":"+comment.getContext());
 						}
   						context.setMovementMethod(LinkMovementMethod.getInstance());
 						
 					}else  context.setText(comment.getContext());
					String date = DateUtil.DateToStr(DateUtil.StrToDate(comment
							.getComment_date()), "yyyy/MM/dd HH:mm");
					time.setText(date);
					btm = BitmapUtils.getImageBitmap(AppConstant.USERIMG_DIR+comment.getUser_id());
					if(btm!=null){
						//设置用户头像
 						iv.setImageBitmap(btm);
					} 
 					view.setOnClickListener(new View.OnClickListener() {
 						@Override
						public void onClick(View v) {
 							showCommentOpWindow(comment);
 						}
					});
				}
			} catch (Exception e) {
 				e.printStackTrace();
			}

			return view;
		}

	}
	
	/**
	 * 删除评论
	 * @param com
	 */
	private void parepareDeleleComment(final Comment com){
		NcpzsHandler handler=Gloable.getInstance().getCurHandler();
 		handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
	 			@Override
				public void onHandlerFinish(Object result) {
	 				if(result!=null)
	 					process = (ProgressDialog) ((Map) result).get("process");
	 				startDeleteComment(com);
				}
			});
		handler.excuteMethod(new ProcessDialogHandlerMethod("","正在删除...."));
  	}
	private void startDeleteComment(final Comment com){
		DeleteCommentThread th=new DeleteCommentThread(com.getComment_id(),user);
		th.setLiserner(new NetFinishListener() {
 			@Override
			public void onfinish(NetResult data) {
  				NcpzsHandler handler=Gloable.getInstance().getCurHandler();
 				if(data.result==NetResult.RESULT_OF_SUCCESS){
 					comments.remove(com);
 					commentservice.deleteById((int)com.getId());
  					handler.excuteMethod(new MessageDialogHandlerMethod("","删除成功"));
 				}else handler.excuteMethod(new MessageDialogHandlerMethod("",data.message));
 				finishDeleteComment();
			}
		});
		th.start();
	}
	private void finishDeleteComment(){
 	    NcpzsHandler handler=Gloable.getInstance().getCurHandler();
 		handler.post(new Runnable() {
 			@Override
			public void run() {
				if(process!=null)
					process.dismiss();
				adapter.notifyDataSetChanged();
 				if(comments!=null)
					 commentcount.setText(comments.size()+"条");
				else ((View)commentcount.getParent()).setVisibility(View.GONE);
 			}
		});
	}
	/**
	 * 跳转评论界面
	 */
	private void toComment(Comment com){
		Intent inte = new Intent(RecordDetailedActivity.this,CommentActivity.class);
		inte.putExtra("record_id",record.getRecord_id());
		inte.putExtra("parent_id",com.getId()+ "");
		startActivityForResult(inte, RETURN_FROM_COMMENT);
	}
	
	 
	/**
	 * 显示举报窗口
	 * @param com
	 */
	private void showAgainstWindow(Comment com){
		if(com!=null){
			final String[] ops=new String[]{"垃圾广告","人身攻击","敏感信息","中奖信息","淫秽色情","骚扰信息"};
  			new AlertDialog.Builder(RecordDetailedActivity.this).setTitle("请选择").setSingleChoiceItems(ops,0,new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,int which) {
							 String msg=ops[which];
							 dialog.dismiss();
						}
			}).show();
		}
	}
	/**
	 * 显示评论操作窗口
	 */
	private void showCommentOpWindow(final Comment com){
		if(com!=null){
			String[] ops=null;
			if(user!=null&&(com.getUser_id()==user.getUser_id()))
					ops=new String[] { "评论", "删除", "举报" };
			else ops=new String[] { "评论", "举报" };
			final int count=ops.length;
			new AlertDialog.Builder(RecordDetailedActivity.this).setItems(ops,new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,int which) {
							dialog.dismiss();
							if (which == 0) {
								toComment(com);
							}else if(which==1){
 								if(count==2)showAgainstWindow(com);
								else parepareDeleleComment(com);
							}else if(which==2){
 								showAgainstWindow(com);
 							}
						}
			}).show();
		}
	}

 

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==RETURN_FROM_COMMENT){
			 comments=commentservice.getCommentByRecordid(record.getRecord_id());
			 adapter.notifyDataSetChanged();		
			 if(comments!=null)
				 commentcount.setText(comments.size()+"条");
			 else ((View)commentcount.getParent()).setVisibility(View.GONE);
		}
 	}
	 
}
