package com.axinxuandroid.activity;




import java.util.Date;
import java.util.Map;

 
import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.activity.net.SaveProustThread;
import com.axinxuandroid.activity.view.CommonTopView;
import com.axinxuandroid.activity.view.MenuWindow;
import com.axinxuandroid.data.Proust;
import com.axinxuandroid.data.ProustItem;
import com.axinxuandroid.data.User;
import com.axinxuandroid.service.ProustService;
import com.axinxuandroid.service.SharedPreferenceService;
import com.axinxuandroid.service.UserService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.axinxuandroid.sys.gloable.Session;
import com.axinxuandroid.sys.gloable.SessionAttribute;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.LogUtil;
 
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.app.ProgressDialog;
import android.content.Intent;
 
public class EditProustActivity extends NcpZsActivity{
	private TextView question;
	private Button save;
	private EditText answer;
	private CommonTopView topview;
	private ProustService pservice;
	private Proust editproust;
	private ProgressDialog progress;
	private UserService uservice;
	private User user;
 	private ProustItem proustitem;
   	@Override
	public void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);	
		setContentView(R.layout.proust_edit);
		uservice=new UserService();
		user=uservice.getLastLoginUser();
		question=(TextView) this.findViewById(R.id.editproust_question);
		save=(Button) this.findViewById(R.id.editproust_save);
		answer=(EditText) this.findViewById(R.id.editproust_answer);
		topview=(CommonTopView) this.findViewById(R.id.editproust_topview);
		topview.setLeftClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				EditProustActivity.this.finish();				
			}
		});
		save.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				if(editproust!=null)
 				   preapareSaveProust();
			}
		});
		int proustid=this.getIntent().getIntExtra("proustid", -1);
		proustitem=ProustItem.getByKey(proustid);
 		pservice=new ProustService();
		editproust=pservice.selectbyUserIdProustId(user.getUser_id(), proustid);
		if(proustitem!=null){
			question.setText("Q: "+proustitem.value);
 		}
		if(editproust!=null){
			answer.setText(editproust.getAnswer());
		}else{
			editproust=new Proust();
			editproust.setProust_id(proustid);
 			editproust.setQuestion(proustitem.value);
			editproust.setUser_id(user.getUser_id());
 		}
		
   }
   	
   public void preapareSaveProust(){
   		NcpzsHandler handler=Gloable.getInstance().getCurHandler();
   		handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
 			@Override
			public void onHandlerFinish(Object result) {
 				if(result!=null)
 					progress=(ProgressDialog) ((Map)result).get("process");
 				startSaveProust();
			}
		});
   		handler.excuteMethod(new ProcessDialogHandlerMethod("","正在提交...."));
   }
	 
	 public void startSaveProust(){
		 editproust.setAnswer(answer.getText().toString());
		 SaveProustThread th=new SaveProustThread(editproust,user);
		 th.setLiserner(new NetFinishListener() {
  			@Override
			public void onfinish(NetResult data) {
  				NcpzsHandler handler=Gloable.getInstance().getCurHandler();
 				if(data.result==NetResult.RESULT_OF_SUCCESS){
 					editproust=(Proust)data.returnData;
 					pservice.saveOrUpdateProust(editproust);
 					handler.excuteMethod(new MessageDialogHandlerMethod("","保存成功！"));
 					
 				}else{
 					handler.excuteMethod(new MessageDialogHandlerMethod("","保存失败:"+data.message));
 				}
 				finishSaveProust();
			}
		 });
		 th.start();
	 }
	
	 public void finishSaveProust(){
		 NcpzsHandler handler=Gloable.getInstance().getCurHandler();
		 handler.post(new Runnable() {
 			@Override
			public void run() {
 				if(progress!=null)
 					progress.dismiss();
 				EditProustActivity.this.finish();
			}
		});
	 }
}