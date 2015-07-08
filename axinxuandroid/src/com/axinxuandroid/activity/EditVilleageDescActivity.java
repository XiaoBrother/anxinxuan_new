package com.axinxuandroid.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.anxinxuandroid.constant.AppConstant;
import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.net.AddVarietyThread;
import com.axinxuandroid.activity.net.LoadProustThread;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.activity.net.SaveVilleageDescThread;
import com.axinxuandroid.activity.view.CommonTopView;
import com.axinxuandroid.activity.view.ImageGalleryView;
import com.axinxuandroid.activity.view.ResourcesView;
import com.axinxuandroid.activity.view.VideoView;
import com.axinxuandroid.activity.view.VideoView.VideoPlayFinishListener;
import com.axinxuandroid.data.Proust;
import com.axinxuandroid.data.Villeage;
import com.axinxuandroid.oauth.YukuOAuth;
import com.axinxuandroid.service.VilleageService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.BitmapUtils;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;
import com.sina.weibo.sdk.utils.MD5;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.PluginState;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 *  
 * 
 * @author Administrator
 * 
 */
public class EditVilleageDescActivity extends NcpZsActivity {
	private int villeageid;
 	private CommonTopView topview;
 	private Villeage villeage;
	private VilleageService villeageservice;
	private ProgressDialog processDialog;
    private EditText edittext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.editvilleagedesc);
		villeageid = this.getIntent().getIntExtra("villeageid", -1);
		edittext = (EditText) this.findViewById(R.id.editvilleagedesc_editetext);
		topview = (CommonTopView) this
				.findViewById(R.id.editvilleagedesc_topview);
 		villeageservice=new VilleageService();
		villeage=villeageservice.getByVilleageid(villeageid);
		topview.setRightClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				prepareSave();
			}
		});
		topview.setLeftClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				EditVilleageDescActivity.this.finish();
			}
		});
		edittext.append(Html.fromHtml(
				villeage.getVilleage_desc(), null, null));
	}

	private void prepareSave() {
		NcpzsHandler handler = Gloable.getInstance().getCurHandler();
		handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
			@Override
			public void onHandlerFinish(Object result) {
				if (result != null)
					processDialog = (ProgressDialog) ((Map) result)
							.get("process");
				startSave();
			}
		});
		handler.excuteMethod(new ProcessDialogHandlerMethod("", "加载中..."));
	}
	
	private void startSave(){
		final String desc=edittext.getText().toString();
 		SaveVilleageDescThread th=new SaveVilleageDescThread(villeageid, desc);
		th.setLiserner(new NetFinishListener() {
 		 

			@Override
			public void onfinish(NetResult data) {
				NcpzsHandler handler=Gloable.getInstance().getCurHandler();
  				handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
 					@Override
					public void onHandlerFinish(Object result) {
 						EditVilleageDescActivity.this.finish();
					}
				});
 				if(data.result==NetResult.RESULT_OF_SUCCESS){
 					villeage.setVilleage_desc(desc);
 					villeageservice.saveOrUpdate(villeage);
 					handler.excuteMethod(new MessageDialogHandlerMethod("","保存成功！"));
 				}else{
 					handler.excuteMethod(new MessageDialogHandlerMethod("","保存失败！"));
 				}
 				
 				finishSave();
			}
		});
		th.start();
	}
	/**
	 * 结束 
	 */
	private void finishSave() {
		NcpzsHandler handler = Gloable.getInstance().getCurHandler();
		handler.post(new Runnable() {
			@Override
			public void run() {
				if (processDialog != null && processDialog.isShowing())
					processDialog.dismiss();
			}
		});

	}
}
