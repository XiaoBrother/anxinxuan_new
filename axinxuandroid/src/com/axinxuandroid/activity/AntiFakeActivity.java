package com.axinxuandroid.activity;




import java.util.Map;

import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.net.LoadAntiFakeQuerySetThread;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.activity.net.SaveAntiFakeQuerySetThread;
import com.axinxuandroid.activity.view.AntifakeView;
import com.axinxuandroid.activity.view.CommonBottomView;
import com.axinxuandroid.activity.view.CommonTopView;
import com.axinxuandroid.data.AntiFakeQuerySet;
import com.axinxuandroid.data.User;
import com.axinxuandroid.service.UserService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.CheckUtil;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
 
public class AntiFakeActivity extends NcpZsActivity{
	private EditText stitle,bottominf,sinaweibo,tel,weidian,weixin;
	private CommonTopView topview;
	private ProgressDialog progress;
	private User user;
	private AntifakeView bottomview;
	private AntiFakeQuerySet qset ;
	private UserService uservice;
	private ProgressDialog processDialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);	
		setContentView(R.layout.antifake_set);
		stitle=(EditText) this.findViewById(R.id.antifake_stitle);
		bottominf=(EditText) this.findViewById(R.id.antifake_bottominf);
		sinaweibo=(EditText) this.findViewById(R.id.antifake_sinaweibo);
		weixin=(EditText) this.findViewById(R.id.antifake_weixin);
		weidian=(EditText) this.findViewById(R.id.antifake_weidian);
		tel=(EditText) this.findViewById(R.id.antifake_tel);
		
 		topview=(CommonTopView) this.findViewById(R.id.antifake_queryset);
 		
 		 bottomview=(AntifakeView) this.findViewById(R.id.antifake_bottom);
         bottomview.setCurrentItem(AntifakeView.BOTTOM_ITEM_INDEX_OF_QUERYSET);
 		uservice=new UserService();
 		user=uservice.getLastLoginUser();
		topview.setRightClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				prepare();
			}
		});
		topview.setLeftClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				AntiFakeActivity.this.finish();
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
	 *准备 加载数据
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
					"数据加载中...."));
		} else {
			startLoadData();
		}

	}

	/**
	 * 开始 加载数据
	 */
	private void startLoadData() {
		String lastoptime=null;
		/*if(currentindex==TAB_INDEX_RECEIVE)
			lastoptime=commentservice.getUserReceiveLatoptime(user.getUser_id());
		else lastoptime=commentservice.getUserSendLatoptime(user.getUser_id());*/
		LoadAntiFakeQuerySetThread th = new LoadAntiFakeQuerySetThread(user.getUser_id());
		th.setLiserner(new NetFinishListener() {
  			@Override
			public void onfinish(NetResult data) {
  				if (data.result == NetResult.RESULT_OF_SUCCESS) {
  					qset=	(AntiFakeQuerySet)data.returnData;
  				
				}
				finishload();
			}
		});
		th.start();
	}

	/**
	 * 加载完成后处理
	 */
	private void finishload() {
		NcpzsHandler handler = Gloable.getInstance().getCurHandler();
		handler.post(new Runnable() {
			@Override
			public void run() {
 				if (progress != null && progress.isShowing()) {
					progress.dismiss();
				}
 				stitle.setText(qset.getStitle());
 				bottominf.setText(qset.getBottominf());
 				sinaweibo.setText(qset.getSinaweibo());
 				weixin.setText(qset.getWeixin());
 				weidian.setText(qset.getWeidian());
 				tel.setText(qset.getTel());
 			}
		});
	}
  	
	
	private void prepare() {
		if (check()) {
			NcpzsHandler processhandler = Gloable.getInstance().getCurHandler();
			processhandler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
						@Override
						public void onHandlerFinish(Object result) {
							if (result != null)
								processDialog = (ProgressDialog) ((Map) result).get("process");
							saveQuerySet();
						}
					});
			processhandler.excuteMethod(new ProcessDialogHandlerMethod("","正在保存...!"));
		
		}
	}
  	
	private void saveQuerySet(){
	
		qset.setTel(tel.getText().toString());	
		qset.setStitle(stitle.getText().toString());	
		qset.setBottominf(bottominf.getText().toString());	
		qset.setSinaweibo(sinaweibo.getText().toString());	
		qset.setWeixin(weixin.getText().toString());	
		qset.setWeidian(weidian.getText().toString());	
		SaveAntiFakeQuerySetThread resetth=new SaveAntiFakeQuerySetThread(qset);
		resetth.setLiserner(new NetFinishListener() {
			@Override
			public void onfinish(NetResult data) {
  				NcpzsHandler handler = Gloable.getInstance().getCurHandler();
				if (data.result == NetResult.RESULT_OF_SUCCESS) {
					qset=(AntiFakeQuerySet)data.returnData;
					handler.excuteMethod(new MessageDialogHandlerMethod("","设置成功！"));
				}else{
					handler.excuteMethod(new MessageDialogHandlerMethod("","设置失败！"));
				}
				saveSetFinish();			}
		});
		resetth.start();
	}
	
	private void saveSetFinish(){
		NcpzsHandler handler = Gloable.getInstance().getCurHandler();
		handler.post(new  Runnable() {
			public void run() {
				if(processDialog!=null)
					processDialog.dismiss();
				
			}
		});
	}
 
	private boolean check(){
		
		NcpzsHandler handler = Gloable.getInstance().getCurHandler();
		
		String  sinaweibostr,weidianstr,telstr;
		sinaweibostr=sinaweibo.getText().toString();
		weidianstr=weidian.getText().toString();
		telstr=tel.getText().toString();
		
		boolean sinaweibob= CheckUtil.isUrl(sinaweibo.getText().toString());
		boolean weidianb= CheckUtil.isUrl( weidian.getText().toString());
		boolean telb= CheckUtil.isMobile( tel.getText().toString());
		
		if(!sinaweibob && !("".equals(sinaweibostr.trim()))){
			handler.excuteMethod(new MessageDialogHandlerMethod("提示", "请填写正确的微博地址!"));
			sinaweibo.requestFocus();
			return false;
		}
 		if (!weidianb && !("".equals(weidianstr.trim()))) {
			handler.excuteMethod(new MessageDialogHandlerMethod("提示", "请填写正确的网店地址!"));
			return false;
		}
		if (!telb && !("".equals(telstr.trim()))) {
			if (!(CheckUtil.isTel( tel.getText().toString())) && !("".equals(telstr.trim()))) {
			handler.excuteMethod(new MessageDialogHandlerMethod("提示",	"请输入正确的联系电话!"));
			return false;
			}
		}
		return true;
	}
 
}