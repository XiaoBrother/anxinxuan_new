package com.axinxuandroid.activity;

import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.view.CommonBottomView;
import com.axinxuandroid.activity.view.CommonTopView;
import com.axinxuandroid.data.LeadViewInfo;
import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.RecordResource;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.BitmapUtils;
import com.ncpzs.util.DensityUtil;
import com.ncpzs.util.EANValidate;
import com.ncpzs.util.FileUtil;
import com.ncpzs.util.LogUtil;
import com.zxing.activity.CaptureActivity;
/**
 * 入口：登录，oauth登录，注册，开始页，
 * 出口：二维码扫描，查询
 * @author hubobo
 *
 */
public class ScanCodeActivity extends NcpZsActivity{
  	private CommonTopView topview;
 	private CommonBottomView bottomview;
 	private   EditText code ;
   	private View serachlay;
   	private ImageView showimg;
 	@Override
	protected void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);
 		setContentView(R.layout.main_scancode);
 		code = (EditText)this.findViewById(R.id.code);
        code.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        topview  = (CommonTopView) this.findViewById(R.id.scan_code_topview);
        Button seachCodeButton = (Button) this.findViewById(R.id.seach_code);
        serachlay=this.findViewById(R.id.scan_code_searchlay);
        bottomview=(CommonBottomView) this.findViewById(R.id.scan_code_bottom);
        showimg=(ImageView) this.findViewById(R.id.scan_code_showimg);
        bottomview.setCurrentItem(CommonBottomView.BOTTOM_ITEM_INDEX_OF_SCANCODE);
       
        int[] imgwidthheight=BitmapUtils.getImageWidthHeight(R.drawable.scancodeimg);
         if(imgwidthheight!=null){
        	LinearLayout.LayoutParams pars=(android.widget.LinearLayout.LayoutParams) showimg.getLayoutParams();
        	if(Gloable.getInstance().getScreenWeight()<imgwidthheight[0]){
        		int margin=(int) ((Gloable.getInstance().getScreenWeight()-imgwidthheight[0])*1.0/imgwidthheight[0]*imgwidthheight[1]);
             	pars.topMargin=(int)(margin/3.0*1.7);
            	//LogUtil.logInfo(getClass(),imgwidthheight[0]+":"+imgwidthheight[1]+ "margin:"+margin);
             	showimg.setLayoutParams(pars);
             	LinearLayout.LayoutParams spars=(android.widget.LinearLayout.LayoutParams) serachlay.getLayoutParams();
             	spars.topMargin=margin/4;
             	serachlay.setLayoutParams(spars);
          	}else {
          		//pars.topMargin=DensityUtil.dip2px(8);
         	}
        	//serachlay.setLayoutParams(pars);
        }
        
        
        topview.setRightClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
//  				Intent inte = new Intent(ScanCodeActivity.this,SettingActivity.class);
// 		  		startActivity(inte);
 				scancodeButtonClick(v);
			}
		});
        
//        topview.setLeftClickListener(new OnClickListener() {
// 			@Override
//			public void onClick(View v) {
// 				scancodeButtonClick(v);
//			}
//		});
        
        seachCodeButton.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				seachcodeButtonClick();
			}
		});
        
        //设置功能引导页面
        LeadViewInfo linfo=new LeadViewInfo(R.drawable.scancodelead);
        linfo.addClickView(seachCodeButton);
        this.setLeadViewInfo(linfo);
	}
 	
 	
	
	 


	 



	//扫描
	private void scancodeButtonClick(View v){
		Intent openCameraIntent = new Intent(ScanCodeActivity.this,
				CaptureActivity.class);
		startActivity(openCameraIntent);
 	}
	//搜索
    private void seachcodeButtonClick(){
 			String seachcode=code.getText().toString().trim();
 			if(seachcode==null||"".equals(seachcode))
 				seachcode="100000141140830001";
 			if(EANValidate.validateCode(seachcode)){
 				Intent openCameraIntent = new Intent(ScanCodeActivity.this,TimelineActivity.class);
 				openCameraIntent.putExtra("id", seachcode);
 	 			startActivity(openCameraIntent);
  			}else{
 				NcpzsHandler handler=Gloable.getInstance().getCurHandler();
 				handler.excuteMethod(new MessageDialogHandlerMethod("","查询码不合法！"));
 			}
 			
  	}

	@Override
	protected boolean isExitActivity() {
 		return true;
	}


}
