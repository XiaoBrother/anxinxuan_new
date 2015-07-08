package com.axinxuandroid.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.anxinxuandroid.constant.AppConstant;
import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.net.DeleteCommentThread;
import com.axinxuandroid.activity.net.LoadCommentThread;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.activity.net.SaveCommentThread;
import com.axinxuandroid.activity.view.CommentAtClickSpan;
import com.axinxuandroid.activity.view.CommonTopView;
import com.axinxuandroid.data.Comment;
import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.User;
import com.axinxuandroid.service.CommentService;
import com.axinxuandroid.service.RecordService;
import com.axinxuandroid.service.UserService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.BitmapUtils;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.LogUtil;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

/**
 * 评论
 * @author Administrator
 *
 */
public class CommentActivity extends NcpZsActivity {

	private int record_id;
	private CommonTopView topview;
 	private ProgressDialog process;
	private List<Comment> comments;
	private ListView listview;
     private EditText contexttext;
    private User user;
    private UserService uservice;
    private CommentAdapter adapter;
    private CommentService commentservice;
    private Reply reply;
    private Record record;
    private RecordService recordservice;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);
 		this.setContentView(R.layout.comment);
 		commentservice=new CommentService();
 		uservice=new UserService();
 		user=uservice.getLastLoginUser();
 		recordservice=new RecordService();
 		record_id=this.getIntent().getIntExtra("record_id", -1);
 		record=recordservice.getByRecordId(record_id);
 		topview=(CommonTopView) this.findViewById(R.id.comment_topview);
  		contexttext=(EditText) this.findViewById(R.id.comment_editcomment);
  		listview=(ListView) findViewById(R.id.comment_listview);
 		comments=new ArrayList<Comment>();
 		adapter=new CommentAdapter(this);
 		listview.setAdapter(adapter);
 		reply=new Reply();
 		topview.setLeftClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				CommentActivity.this.finish();
			}
		});
 		topview.setRightClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				parepareSaveComment();
			}
		});
 		new Thread(){
 			@Override
			public void run() {
 				parepareLoadComment();
			}
 			
 		}.start();
 	}
	private void parepareLoadComment(){
		NcpzsHandler handler=Gloable.getInstance().getCurHandler();
		handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
 			@Override
			public void onHandlerFinish(Object result) {
 				if(result!=null)
 					process = (ProgressDialog) ((Map) result)
					.get("process");
 				loadComment();
			}
		});
		handler.excuteMethod(new ProcessDialogHandlerMethod("","加载中...."));
 	}
	private void loadComment() {
 			String lastoptime=commentservice.getLatoptime(record_id);
			LoadCommentThread th = new LoadCommentThread(record_id,lastoptime);
			th.setLiserner(new NetFinishListener() {
				 

				@Override
				public void onfinish(NetResult data) {
 					if (data.result == NetResult.RESULT_OF_SUCCESS) {
						List<Comment> tcomments = (List<Comment>)data.returnData;
	   				    if(tcomments!=null){
	   				    	for(Comment com:tcomments)
	   				    		commentservice.saveOrUpdate(com);
	   				    }
					}
					comments=commentservice.getCommentByRecordid(record_id);
					loadFinish();
				}
			});
			th.start();
  	}

	 

	private void loadFinish() {
		NcpzsHandler handler = Gloable.getInstance().getCurHandler();
		handler.post(new Runnable() {
			@Override
			public void run() {
				if (process != null)
					process.dismiss();
				adapter.notifyDataSetChanged();
  			}
		});
	}
	private void parepareSaveComment(){
		NcpzsHandler handler=Gloable.getInstance().getCurHandler();
		String context=contexttext.getText().toString();
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
		String context=contexttext.getText().toString();
		int parentid=0;
		if(reply.replycomid!=0){
 				parentid=reply.replycomid;
		}
		SaveCommentThread th=new SaveCommentThread(record_id, parentid,context,user);
		th.setLiserner(new NetFinishListener() {
 			@Override
			public void onfinish(NetResult data) {
  				if(data.result==NetResult.RESULT_OF_SUCCESS){
 					Comment com=(Comment) data.returnData;
 					if(com!=null){
  						com.setBatchCode(record.getTrace_code());
 						com.setVarietyName(record.getVariety_name());
 						if(reply.replyuserid!=0)
 						   com.setReplyUserId(reply.replyuserid+"");
 						if(reply.replyname!=null)
 							com.setReplyUserName(reply.replyname);
 						commentservice.saveOrUpdate(com);
 						if(comments==null) comments=new ArrayList<Comment>();
 						comments.add(0,com);
 					}
  				}
 				finishSaveComment(data.result,data.message);
			}
		});
		th.start();
	}
	private void finishSaveComment(final int result, final String message){
		final NcpzsHandler handler = Gloable.getInstance().getCurHandler();
		handler.post(new Runnable() {
 			@Override
			public void run() {
 				if(process!=null)
 					process.dismiss();
  				if(result==NetResult.RESULT_OF_SUCCESS){
 		 			//handler.excuteMethod(new MessageDialogHandlerMethod("","评论成功"));
 				}else{
 					handler.excuteMethod(new MessageDialogHandlerMethod("",message));
 				}
  				adapter.notifyDataSetChanged();
			}
		});
		
	}
	class  ViewHolder { 
 	       ImageView iv,spimg;
	       TextView username,time,context;
	} 
	private class CommentAdapter extends BaseAdapter{
        private LayoutInflater inflater;
        public CommentAdapter(Context context){
        	inflater=LayoutInflater.from(context);
        }
		@Override
		public int getCount() {
 			return comments==null?0:comments.size();
		}

		@Override
		public Object getItem(int position) {
			if(comments!=null)
 			   return comments.get(position);
			return null;
		}

		@Override
		public long getItemId(int position) {
 			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final Comment comment=comments.get(position);
			View view=convertView; 
			ViewHolder holder;
			if(view==null){
				view = inflater.inflate(R.layout.record_detail_commentitem, null);
				holder=new ViewHolder();
 				holder.username=(TextView) view.findViewById(R.id.comment_item_username);
				holder.time=(TextView) view.findViewById(R.id.comment_item_time);
				holder.context=(TextView) view.findViewById(R.id.comment_item_context);
				holder.iv=(ImageView) view.findViewById(R.id.comment_item_userimg);
				holder.spimg=(ImageView) view.findViewById(R.id.comment_item_repeatimg);
  				view.setTag(holder);
			}else{
				holder=(ViewHolder) view.getTag();
			}
			if((position+1)%2==0){
				view.setBackgroundColor(Color.rgb(255, 255, 255));
				holder.spimg.setBackgroundResource(R.drawable.batch_bg_click);
			}else{
				view.setBackgroundColor(Color.rgb(249, 249, 249));
				holder.spimg.setBackgroundResource(R.drawable.batch_bg_normal);
			}
 			Bitmap btm = null;
				try {
				if (comment != null) {
					holder.username.setText(comment.getUser_name());
					String context=comment.getContext();
					String replyname=comment.getReplyUserName();
					if(replyname!=null&&!"null".equals(replyname)){
 						SpannableString  spanstr=new SpannableString ("@"+replyname);
 						spanstr.setSpan(new  CommentAtClickSpan(comment), 0, replyname.length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
  						int index=context==null?-1:context.indexOf("@"+replyname);
						if(index>=0){
							holder.context.setText(context.substring(0, index));
							holder.context.append(spanstr);
							holder.context.append(context.substring(index+replyname.length()+1));
  						}else{
  							holder.context.setText(spanstr);
  							holder.context.append(":"+comment.getContext());
 						}
   						holder.context.setMovementMethod(LinkMovementMethod.getInstance());
 						
 					}else holder.context.setText(comment.getContext());
					//holder.context.setText(comment.getContext());
					String date = DateUtil.DateToStr(DateUtil.StrToDate(comment.getComment_date()), "yyyy/MM/dd HH:mm");
					holder.time.setText(date);
					btm = BitmapUtils.getImageBitmap(AppConstant.USERIMG_DIR+comment.getUser_id());
					if(btm!=null){
						//设置用户头像
						holder.iv.setImageBitmap(btm);
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
	 * 显示评论操作窗口
	 */
	private void showCommentOpWindow(final Comment com){
		if(com!=null){
			String[] ops=null;
			if(user!=null&&(com.getUser_id()==user.getUser_id()))
					ops=new String[] {"回复","删除", "举报" };
			else ops=new String[] { "回复","举报" };
			final int count=ops.length;
			new AlertDialog.Builder(CommentActivity.this).setItems(ops,new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,int which) {
							dialog.dismiss();
							if (which == 0) {
								reply.init();
								if(com.getUser_id()!=user.getUser_id()){
									reply.replycomid=com.getComment_id();
									reply.replyuserid=com.getUser_id();
									reply.replyname=com.getUser_name();
									contexttext.setText("@"+reply.replyname+":");
								}
 							}else if(which==1){
								if(count==2)showAgainstWindow(com);
								else parepareDeleleComment(com);
 							} else if(which==2){
 								showAgainstWindow(com);
							}
						}
			}).show();
		}
	}
	/**
	 * 显示举报窗口
	 * @param com
	 */
	private void showAgainstWindow(Comment com){
		if(com!=null){
			final String[] ops=new String[]{"垃圾广告","人身攻击","敏感信息","中奖信息","淫秽色情","骚扰信息"};
  			new AlertDialog.Builder(CommentActivity.this).setTitle("请选择").setSingleChoiceItems(ops,0,new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,int which) {
							 String msg=ops[which];
							 dialog.dismiss();
						}
			}).show();
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
  			}
		});
	}
	
	public class Reply{
		public int replycomid;
		public int replyuserid;
		public String replyname;
		public void init(){
			replycomid=0;
			replyuserid=0;
			replyname=null;
		}
	}
}
