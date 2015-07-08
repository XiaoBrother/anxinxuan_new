package com.axinxuandroid.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.anxinxuandroid.constant.AppConstant;
import com.axinxuandroid.activity.CommentActivity.ViewHolder;
import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.net.DeleteCommentThread;
import com.axinxuandroid.activity.net.LoadUserCommentThread;
import com.axinxuandroid.activity.net.LoadVareityBatchThread;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.activity.view.CommentAtClickSpan;
import com.axinxuandroid.activity.view.CommentWindow;
import com.axinxuandroid.activity.view.CommonTopView;
import com.axinxuandroid.activity.view.CommentWindow.SaveCommentFinishListener;
import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.Comment;
import com.axinxuandroid.data.NetUpdateRecord;
import com.axinxuandroid.data.TimeOrderTool;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.NetUpdateRecord.NetUpdateRecordTime;
import com.axinxuandroid.service.CommentService;
import com.axinxuandroid.service.SharedPreferenceService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.BitmapUtils;
import com.ncpzs.util.DateUtil;
import com.stay.pull.lib.PullToRefreshBase;
import com.stay.pull.lib.PullToRefreshDragListView;
import com.stay.pull.lib.PullToRefreshBase.OnRefreshListener;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class UserCommentActivity  extends NcpZsActivity{
	private static final int TAB_INDEX_RECEIVE = 1;
	private static final int TAB_INDEX_SEND = 2;
	private ProgressDialog progress;
	private CommonTopView topview;
 	private PullToRefreshDragListView sendview, reveview;
	private List<Comment> sendcom,recvcom;
 	private User user;
	private Button revbtn,sendbtn;
	private UserCommentAdapter revadapter,sendadapter;
	private ListView sendlview,revlview;
	private int currentindex=TAB_INDEX_RECEIVE;
 	private CommentService commentservice;
	private NcpzsHandler handler;
	private CommentWindow commentwindow;
	private View root;
 	@Override
	protected void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);
 		this.setContentView(R.layout.usercomment);
 		handler=Gloable.getInstance().getCurHandler();
 		user=SharedPreferenceService.getLastLoginUser();
 		topview=(CommonTopView) this.findViewById(R.id.usercomment_topview);
 		revbtn=(Button) this.findViewById(R.id.usercomment_receive);
 		sendbtn=(Button) this.findViewById(R.id.usercomment_send);
 		sendview=(PullToRefreshDragListView) this.findViewById(R.id.usercomment_sendview);
 		reveview=(PullToRefreshDragListView) this.findViewById(R.id.usercomment_receiveview);
 		root=this.findViewById(R.id.usercomment_root);
 		sendlview=sendview.getRefreshableView();
 		revlview=reveview.getRefreshableView();
 		revadapter=new UserCommentAdapter(this);
 		sendadapter=new UserCommentAdapter(this);
 		sendlview.setAdapter(sendadapter);
 		revlview.setAdapter(revadapter);
 		commentwindow=new CommentWindow(this);
 		commentwindow.setOnSaveCommentFinishListener(new SaveCommentFinishListener() {
 			@Override
			public void onfinish(boolean success, Comment com) {
 				if(success){
 					if(currentindex==TAB_INDEX_SEND){
 						sendcom.add(0, com);
 						sendadapter.notifyDataSetChanged();
 					}else{
 						//�л�������
 						setTabIndex(TAB_INDEX_SEND);
 					}
  				}
 					
			}
		});
 		topview.setLeftClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				UserCommentActivity.this.finish();
  			}
		});
 		commentservice=new CommentService();
 		if(user==null){
 			handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
 				@Override
				public void onHandlerFinish(Object result) {
 					UserCommentActivity.this.finish();
				}
			});
 			handler.excuteMethod(new MessageDialogHandlerMethod("","���ȵ�¼��"));
 		}
 		revbtn.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				setTabIndex(TAB_INDEX_RECEIVE);
			}
		});
 		sendbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setTabIndex(TAB_INDEX_SEND);
			}
		});
 		sendview.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(int direct) {
				 queryShowDatas();
  			}
		});
 		reveview.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(int direct) {
				 queryShowDatas();
  			}
		});
 		 //��ʼ������������
		new Thread(){
 			@Override
			public void run() {
 				prepareloadData(true);
			}
 		}.start();
	}

	 
	private void setTabIndex(int index) {
		((LinearLayout)sendbtn.getParent()).setBackgroundResource(R.drawable.tabbg);
		sendbtn.setTextColor(Color.rgb(64, 64, 64));
 		((LinearLayout)revbtn.getParent()).setBackgroundResource(R.drawable.tabbg);
 		revbtn.setTextColor(Color.rgb(64, 64, 64));
 		switch (index) {
		case TAB_INDEX_RECEIVE:
			((LinearLayout)revbtn.getParent()).setBackgroundColor(Color.rgb(255, 255, 255));
			revbtn.setTextColor(Color.rgb(93, 69, 64));
			sendview.setVisibility(View.GONE);
			reveview.setVisibility(View.VISIBLE);
			currentindex=TAB_INDEX_RECEIVE;
  			break;
 		case TAB_INDEX_SEND:
 			((LinearLayout)sendbtn.getParent()).setBackgroundColor(Color.rgb(255, 255, 255));
 			sendbtn.setTextColor(Color.rgb(93, 69, 64));
 			reveview.setVisibility(View.GONE);
			sendview.setVisibility(View.VISIBLE);
			currentindex=TAB_INDEX_SEND;
 			break;
		}
		new Thread() {
			@Override
			public void run() {
				prepareloadData(true);
			}

		}.start();
	}

	class  ViewHolder { 
	       ImageView iv,spimg;
	       TextView username,time,context;
	}
	private class UserCommentAdapter extends BaseAdapter{

		private List<Comment> datas;
		private LayoutInflater inflater;
		public UserCommentAdapter(Context context){
			inflater=LayoutInflater.from(context);
		}
		@Override
		public int getCount() {
 			return datas==null?0:datas.size();
		}

		@Override
		public Object getItem(int position) {
 			return position;
		}

		@Override
		public long getItemId(int position) {
 			return position;
		}

		public void setDatas(List<Comment> datas){
			 this.datas=datas;
			 this.notifyDataSetChanged();
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final Comment comment=datas.get(position);
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
						//�����û�ͷ��
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
	 *׼�� ��������
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
					"���ݼ�����...."));
		} else {
			startLoadData();
		}

	}

	/**
	 * ��ʼ ��������
	 */
	private void startLoadData() {
		String lastoptime=null;
		if(currentindex==TAB_INDEX_RECEIVE)
			lastoptime=commentservice.getUserReceiveLatoptime(user.getUser_id());
		else lastoptime=commentservice.getUserSendLatoptime(user.getUser_id());
		LoadUserCommentThread th = new LoadUserCommentThread(user.getUser_id(),currentindex,lastoptime);
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
				finishload();
			}
		});
		th.start();
	}

	/**
	 * ������ɺ���
	 */
	private void finishload() {
		NcpzsHandler handler = Gloable.getInstance().getCurHandler();
		handler.post(new Runnable() {
			@Override
			public void run() {
 				if (progress != null && progress.isShowing()) {
					progress.dismiss();
				}
 				
 				if(currentindex==TAB_INDEX_RECEIVE&&recvcom!=null)
 					recvcom.clear();
 				else if(currentindex==TAB_INDEX_SEND&&sendcom!=null)
 					sendcom.clear();
 				queryShowDatas();
 			}
		});
	}
	
	private void queryShowDatas(){
		String lastoptime=null;
		if(currentindex==TAB_INDEX_RECEIVE){
			if(recvcom==null) recvcom=new ArrayList<Comment>();
			if(recvcom.size()>0)
				lastoptime=recvcom.get(recvcom.size()-1).getLastoptime();
			List<Comment> coms=commentservice.getUserReceiveComment(user.getUser_id(), lastoptime);
			if(coms!=null){
				 recvcom.removeAll(coms);
				 recvcom.addAll(coms);
			}
			  
			revadapter.setDatas(recvcom);
		}else{
			if(sendcom==null) sendcom=new ArrayList<Comment>();
			if(sendcom.size()>0)
				lastoptime=sendcom.get(sendcom.size()-1).getLastoptime();
			List<Comment> coms=commentservice.getUserSendComment(user.getUser_id(), lastoptime);
			if(coms!=null){
				sendcom.removeAll(coms);
				sendcom.addAll(coms );
			}
 			sendadapter.setDatas(sendcom);
		}
		if(sendview!=null)
				sendview.onRefreshComplete();
		if(reveview!=null)
				reveview.onRefreshComplete();
	}
	/**
	 * ��ʾ���۲�������
	 */
	private void showCommentOpWindow(final Comment com){
		if(com!=null){
			String[] ops=null;
			if(user!=null&&(com.getUser_id()==user.getUser_id()))
					ops=new String[] {"�鿴��¼","ɾ��" };
			else ops=new String[] {"�鿴��¼","�ظ�","�ٱ�" };
			final int count=ops.length;
			new AlertDialog.Builder(UserCommentActivity.this).setItems(ops,new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,int which) {
							dialog.dismiss();
							if (which == 0) {
								Intent inte=new Intent(UserCommentActivity.this,RecordDetailedActivity.class);
				 				inte.putExtra("recordid", com.getRecordid());
				  				startActivity(inte);
 							}else if(which==1){
								if(count==2) parepareDeleleComment(com); 
								else{
									commentwindow.setData(com.getRecordid(), com);
									commentwindow.showAtLocation(root, Gravity.BOTTOM, 0,0);
								}
 							} else if(which==2){
 								showAgainstWindow(com);
							}
						}
			}).show();
		}
	}
	
	
	
	/**
	 * ��ʾ�ٱ�����
	 * @param com
	 */
	private void showAgainstWindow(Comment com){
		if(com!=null){
			final String[] ops=new String[]{"�������","������","������Ϣ","�н���Ϣ","����ɫ��","ɧ����Ϣ"};
  			new AlertDialog.Builder(UserCommentActivity.this).setTitle("��ѡ��").setSingleChoiceItems(ops,0,new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,int which) {
							 String msg=ops[which];
							 dialog.dismiss();
						}
			}).show();
		}
	}
	
	/**
	 * ɾ������
	 * @param com
	 */
	private void parepareDeleleComment(final Comment com){
		NcpzsHandler handler=Gloable.getInstance().getCurHandler();
 		handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
	 			@Override
				public void onHandlerFinish(Object result) {
	 				if(result!=null)
	 					progress = (ProgressDialog) ((Map) result).get("process");
	 				startDeleteComment(com);
				}
			});
		handler.excuteMethod(new ProcessDialogHandlerMethod("","����ɾ��...."));
  	}
	private void startDeleteComment(final Comment com){
		DeleteCommentThread th=new DeleteCommentThread(com.getComment_id(),user);
		th.setLiserner(new NetFinishListener() {
  			@Override
			public void onfinish(NetResult data) {
  				NcpzsHandler handler=Gloable.getInstance().getCurHandler();
 				if(data.result==NetResult.RESULT_OF_SUCCESS){
   					commentservice.deleteById((int)com.getId());
  					handler.excuteMethod(new MessageDialogHandlerMethod("","ɾ���ɹ�"));
  					finishDeleteComment(com);
 				}else {
 					handler.excuteMethod(new MessageDialogHandlerMethod("",data.message));
 					finishDeleteComment(null);
 				}
 				
			}
		});
		th.start();
	}
	private void finishDeleteComment(final Comment com){
 	    NcpzsHandler handler=Gloable.getInstance().getCurHandler();
 		handler.post(new Runnable() {
 			@Override
			public void run() {
				if(progress!=null)
					progress.dismiss();
				if(com!=null){
					if(currentindex==TAB_INDEX_RECEIVE){
						recvcom.remove(com);
						revadapter.setDatas(recvcom);
					}else{
							sendcom.remove(com);
							sendadapter.setDatas(sendcom);
					}
				}
   			}
		});
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int keyCode=event.getKeyCode();
          switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
            	if(commentwindow.isShowing()){
            		commentwindow.dismiss();
            		return true;
            	}
              	return super.dispatchKeyEvent(event);	
            default:
             break;
        }
        return super.dispatchKeyEvent(event);
 	}
}
