package com.axinxuandroid.activity.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.activity.EditVilleageDescActivity;
import com.axinxuandroid.activity.ProustActivity;
import com.axinxuandroid.activity.R;
import com.axinxuandroid.activity.SelectTypeActivity;
 import com.axinxuandroid.activity.TimelineActivity;
import com.axinxuandroid.activity.UploadPhotoActivity;
import com.axinxuandroid.activity.VilleageInfoActivity;
import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.menu.InOutImageButton;
import com.axinxuandroid.activity.net.AddUserFavoriteThread;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.activity.net.RemoveUserFavoriteThread;
import com.axinxuandroid.activity.net.SaveCommentThread;
import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.Comment;
import com.axinxuandroid.data.MoreWindowDataInterface;
import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.RecordResource;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.UserFavorite;
import com.axinxuandroid.data.Villeage;
import com.axinxuandroid.data.UserFavorite.FavoriteType;
import com.axinxuandroid.service.CommentService;
import com.axinxuandroid.service.SharedPreferenceService;
import com.axinxuandroid.service.UserFovoriteService;
import com.axinxuandroid.sys.gloable.Gloable;

import com.ncpzs.util.DensityUtil;
import com.ncpzs.util.LogUtil;
import com.ncpzs.util.SendShareInfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.text.ClipboardManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class CommentWindow extends PopupWindow {
	private Context context;
    private EditText ctext;
    private Button subtn; 
    private ProgressDialog process;
    private int recordid=-1;
    private User user;
    private CommentService commentservice;
    private SaveCommentFinishListener finishlis;
    private Comment reply;
	public CommentWindow(Activity context) {
 		super(context);
		this.context=context;
		LayoutInflater inflater = LayoutInflater.from(context);
		View  view=inflater.inflate(R.layout.commentwindow, null);
		this.setContentView(view);
		ctext=(EditText) view.findViewById(R.id.commentwindow_editcomment);
		subtn=(Button) view.findViewById(R.id.commentwindow_sub);
 		// 必须设置宽高
 		this.setWidth(Gloable.getInstance().getScreenWeight());  
        this.setHeight(Gloable.getInstance().getScreenHeight());  
      // 如果设置setBackgroundDrawable，onTouch监听事件不启用，
        // OutsideTouchable不启用，OnKey的KEYCODEY_BACK不启用；
        // 如果没有设置，OutsideTouchable启用，OnTouch启用。
        //setBackgroundDrawable(new BitmapDrawable());
        //ColorDrawable dw = new ColorDrawable(Color.BLACK);
        //this.setBackgroundDrawable(dw);
        this.setBackgroundDrawable(new BitmapDrawable());
		this.setFocusable(true); 
		this.setTouchable(true); //设置PopupWindow可触摸  
		this.setOutsideTouchable(true); //设置非PopupWindow区域可触摸
		this.setAnimationStyle(R.style.BottomInOutAnimation);
		view.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				dismiss();
			}
		});
		subtn.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				parepareSaveComment();
			}
		});
		user=SharedPreferenceService.getLastLoginUser();
		commentservice=new CommentService();
	}
	
	public void setData(int recordid,Comment reply){
		this.recordid=recordid;
		this.reply=reply;
		if(reply!=null&&reply.getUser_name()!=null){
			ctext.setText("@"+reply.getUser_name()+":");
		}
	}
	
	private void parepareSaveComment(){
		NcpzsHandler handler=Gloable.getInstance().getCurHandler();
		if(this.recordid==-1) {
			handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
 				@Override
				public void onHandlerFinish(Object result) {
 					dismiss();
				}
			});
			handler.excuteMethod(new MessageDialogHandlerMethod("", "无法评论！"));
			return ;
		}
		
		String context=ctext.getText().toString();
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
		final String context=ctext.getText().toString();
		int parentid=0;
		if(reply!=null)
			parentid=reply.getComment_id();
		SaveCommentThread th=new SaveCommentThread(recordid,parentid, context,user);
		th.setLiserner(new NetFinishListener() {
 			@Override
			public void onfinish(NetResult data) {
  				NcpzsHandler handler=Gloable.getInstance().getCurHandler();
 				if(data.result==NetResult.RESULT_OF_SUCCESS){
 					Comment com=(Comment) data.returnData;
 					
 					if(com!=null){
 						com.setBatchCode(reply.getBatchCode());
 						com.setVarietyName(reply.getVarietyName());
 						if(reply!=null){
 							com.setReplyUserId(reply.getUser_id()+"");
 							com.setReplyUserName(reply.getUser_name());
 						}
  						commentservice.saveOrUpdate(com);
  					}
 					finishSaveComment(true,com);
 				}else {
 					//保存到本地
  					handler.excuteMethod(new MessageDialogHandlerMethod("", "提交失败！"));
  					finishSaveComment(false,null);
 				}
 			}
		});
		th.start();
	}
	private void finishSaveComment(final boolean success,final Comment com){
 	    NcpzsHandler handler=Gloable.getInstance().getCurHandler();
 		handler.post(new Runnable() {
 			@Override
			public void run() {
				if(process!=null)
					process.dismiss();
                if(finishlis!=null)
                	finishlis.onfinish(success, com);
                dismiss();
 			}
		});
	}
	
	
	public void setOnSaveCommentFinishListener(SaveCommentFinishListener lis){
		this.finishlis=lis;
	}
	
	public  interface SaveCommentFinishListener{
		public void onfinish(boolean success,Comment com);
	}
}