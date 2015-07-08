package com.axinxuandroid.activity;





 
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.anxinxuandroid.constant.AppConstant;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.net.LoadBatchByCodeOrIdThread;
import com.axinxuandroid.activity.net.LoadImageQueue;
import com.axinxuandroid.activity.net.LoadUserVilleageThread;
import com.axinxuandroid.activity.net.LoadVillageInfoThread;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.activity.net.LoadImageQueue.ImageItem;
import com.axinxuandroid.activity.view.CommonTopView;
import com.axinxuandroid.activity.view.EditTabelView;
import com.axinxuandroid.activity.view.ImageGalleryView;
import com.axinxuandroid.activity.view.ImageGalleryWindow;
import com.axinxuandroid.activity.view.MoreWindow;
import com.axinxuandroid.activity.view.UserVilleageView;
import com.axinxuandroid.activity.view.ImageGalleryView.ImageGallery;
import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.Intelligence;
import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.RecordResource;
import com.axinxuandroid.data.SystemLabel;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.UserVilleage;
import com.axinxuandroid.data.Variety;
import com.axinxuandroid.data.Villeage;
import com.axinxuandroid.data.VilleageBanner;
import com.axinxuandroid.service.IntelligenceService;
import com.axinxuandroid.service.SystemLabelService;
import com.axinxuandroid.service.UserService;
import com.axinxuandroid.service.UserVilleageService;
import com.axinxuandroid.service.VarietyService;
import com.axinxuandroid.service.VilleageBannerService;
import com.axinxuandroid.service.VilleageService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.BitmapUtils;
import com.ncpzs.util.DensityUtil;
import com.ncpzs.util.FileUtil;
  
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
 
public class VilleageInfoActivity extends NcpZsActivity{
	private VilleageService villeageservice;
	private VilleageBannerService bannerservice;
	private IntelligenceService intellservice;
	private UserService userservice;
	private UserVilleageService uservilleageservice;
	private VarietyService varietyservice;
 	protected CommonTopView topview;
  	private Villeage selvilleage;
  	private ProgressDialog progress;
  	private LinearLayout intellgenlay,personlay,varietylay;
  	private ImageGalleryView gallery;
  	private TextView scale,buildtime,address,tele;
  	private RelativeLayout desclay;
  	private MoreWindow bottomwindow;
  	private boolean isshow=false;
  	private int villeageid;
  	private List<Villeage> uservils;
  	private SystemLabelService labelservice;
  	private ImageGalleryWindow imggllertwindow;
  	private LoadImageQueue loadqueue;
  	private ImageView mapimg;
  	private View root;
   	@Override
	public void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);	
		setContentView(R.layout.villeageinfo);
		Intent intent = getIntent();
		villeageid = intent.getIntExtra("villeage_id",-1);
 		topview= (CommonTopView) this.findViewById(R.id.uservilleage_topview);
  		intellgenlay=(LinearLayout) this.findViewById(R.id.uservilleage_intelligenlay);
  		personlay=(LinearLayout) this.findViewById(R.id.uservilleage_personlay);
  		desclay=(RelativeLayout) this.findViewById(R.id.uservilleage_desclay);
  		varietylay=(LinearLayout) this.findViewById(R.id.uservilleage_varitylay);
 		gallery=(ImageGalleryView) this.findViewById(R.id.uservilleage_imggallery);
 		scale=(TextView) this.findViewById(R.id.uservilleage_scale);
 		buildtime=(TextView) this.findViewById(R.id.uservilleage_buildtime);
 		address=(TextView) this.findViewById(R.id.uservilleage_address);
 		tele=(TextView) this.findViewById(R.id.uservilleage_tele);
 		root=this.findViewById(R.id.uservilleage_root);
 		mapimg=(ImageView) this.findViewById(R.id.uservilleage_map);
 		//gallery.hiddenIndex();
 		villeageservice=new VilleageService();
 		labelservice=new SystemLabelService();
 		bannerservice=new VilleageBannerService();
 		userservice=new UserService();
 		varietyservice=new VarietyService();
 		uservilleageservice=new UserVilleageService();
 		intellservice=new IntelligenceService();
 		uservils=new ArrayList<Villeage>();
 		selvilleage=villeageservice.getByVilleageid(villeageid);
 		
 		bottomwindow=new MoreWindow(VilleageInfoActivity.this, new int[]{MoreWindow.EDIT_VILLEAGEDESC_FUN,
					MoreWindow.UPLOAD_VILLEAGEIMG_FUN,MoreWindow.SHARE_WEIBO_FUN,MoreWindow.SHARE_COMMENTS_FUN});
 		bottomwindow.addData(MoreWindow.DATA_VILLEAGE, selvilleage);
 		
 		imggllertwindow=new ImageGalleryWindow(VilleageInfoActivity.this);
 		loadqueue=new LoadImageQueue();
 		loadqueue.setLiserner(new NetFinishListener() {
  			@Override
			public void onfinish(NetResult data) {
				final ImageItem item=(ImageItem) data.returnData;
				NcpzsHandler handler=Gloable.getInstance().getCurHandler(); 
				handler.post(new Runnable() {
 					@Override
					public void run() {
 						updateIntelligenceImg((Intelligence)item.res,item.index);
					}
				});
 			}
		});
 		topview.setLeftClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				VilleageInfoActivity.this.finish();
			}
		});
		topview.setRightClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				if(bottomwindow!=null){
 					if(isshow){
 	 					bottomwindow.dismiss();
 	 					isshow=false;
 	 				}
 	 				else {
 	 					bottomwindow.showAsDropDown(topview.getRightimg(),-DensityUtil.dip2px(150),-DensityUtil.dip2px(20));
 	 					isshow=true;
 	 				}
 				}
 			}
		});
		topview.setTitleClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
				//showSelectVilleage();
			}
		});
		desclay.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				if(selvilleage!=null){
 					if(isshow){
 	 					bottomwindow.dismiss();
 	 					isshow=false;
 	 				}
 					Intent inte=new Intent(VilleageInfoActivity.this,VilleageDescActivity.class);
 	                inte.putExtra("villeage_id", selvilleage.getVilleage_id());
 	                startActivity(inte);
 				}
                
			}
		});
		mapimg.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				if(selvilleage!=null){
 					 Uri uri = Uri.parse("geo:"+selvilleage.getLat()+","+selvilleage.getLng()); // 这种方法开打导航，总是提示“请正确传入参数”
  		             Intent intent = new Intent(Intent.ACTION_VIEW, uri);
 		             startActivity(intent);
 				}
			}
		});
		if(selvilleage!=null)
 			initBaseVilleageInfo(selvilleage);
		new Thread(){
 			@Override
			public void run() {
 				prepareloadVilleageInfo();
			}
			
		}.start();
   }
	
//   	private void prepareloadUserVilleages(){
//   		NcpzsHandler processhandler=Gloable.getInstance().getCurHandler();
//	    processhandler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
//						@Override
//						public void onHandlerFinish(Object result) {
//							if(result!=null)
//								progress=(ProgressDialog) ((Map)result).get("process");
//							loadUserVilleages();
//						}
//					});
//		 processhandler.excuteMethod(new ProcessDialogHandlerMethod("", "数据加载中...."));
//   	}
   	/**
   	 * 加载所有农场
   	 */
//	private void loadUserVilleages() {
//		// 加载网络数据
//		LoadUserVilleageThread vth = new LoadUserVilleageThread(userid);
//		vth.setLiserner(new NetFinishListener() {
//			@Override
//			public void onfinish(Object data) {
//				int result = (Integer) ((Map) data).get(LoadUserVilleageThread.RESULT);
//				if (result == NetResult.RESULT_OF_SUCCESS) {
//					List<UserVilleage> tuservils = (List<UserVilleage>) ((Map) data).get(LoadUserVilleageThread.RETURNDATA);
//					if (tuservils != null) {
//						for (UserVilleage vil : tuservils) {
// 							uservilleageservice.saveOrUpdate(vil);
// 							if(vil.getVilleage()!=null)
//							 villeageservice.saveOrUpdate(vil.getVilleage());
//						}
// 					}  
// 				} 
//				uservils=uservilleageservice.getVilleageByUser(userid);
//				if(uservils!=null){
//					selvilleage = uservils.get(0);
//					initBaseVilleageInfo(selvilleage);
//					loadVilleageInfo();
//				}else{
//					loadFinish();
//				}
//			}
//		});
//		vth.start();
//	}
   	
   	private void prepareloadVilleageInfo(){
   		NcpzsHandler processhandler=Gloable.getInstance().getCurHandler();
	    processhandler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
						@Override
						public void onHandlerFinish(Object result) {
							if(result!=null)
								progress=(ProgressDialog) ((Map)result).get("process");
							loadVilleageInfo();
						}
					});
		 processhandler.excuteMethod(new ProcessDialogHandlerMethod("", "数据加载中...."));
   	}
    /**
     * 加载农场具体信息   
     */
 	private void loadVilleageInfo(){
 		if(selvilleage!=null){
 			String banneroptime =bannerservice.getLatoptime(selvilleage.getVilleage_id());
 			String intelligenceoptime =intellservice.getLatoptime(selvilleage.getVilleage_id());
 			String varietyoptime =varietyservice.getLatoptime(selvilleage.getVilleage_id());
 	 	    LoadVillageInfoThread th=new LoadVillageInfoThread(selvilleage,banneroptime,intelligenceoptime,varietyoptime);
 	  		th.setLiserner(new NetFinishListener() {
 				@Override
				public void onfinish(NetResult data) {
  	 				if(data.result==NetResult.RESULT_OF_SUCCESS){
 	 					if(selvilleage.getBanners()!=null){
 	 	 					for(VilleageBanner banner:selvilleage.getBanners()){
 	 	 						bannerservice.saveOrUpdate(banner);
 	 	  					}
 	 	 				}
 	 	 				if(selvilleage.getIntelligences()!=null){
 	 	 					for(Intelligence intell:selvilleage.getIntelligences()){
 	 	 						intellservice.saveOrUpdate(intell);
 	 	  					}
 	 	 				}
 	 	 				if(selvilleage.getVarieties()!=null){
 	 	 					for(Variety variety:selvilleage.getVarieties()){
 	 	 						varietyservice.saveOrUpdate(variety);
 	 	  					}
 	 	 				}
 	 	 				if(selvilleage.getUsers()!=null){
 	 	 					for(User user:selvilleage.getUsers()){
 	 	 						User tmp=userservice.selectbyUserid(user.getUser_id());
 	 	 						if(tmp==null){
 	 	 							userservice.addUser(user);
 	 	 							UserVilleage uv=new UserVilleage();
 	 	 							uv.setUser_id(user.getUser_id());
 	 	 							uv.setVilleage_id(selvilleage.getVilleage_id());
 	 	 							uservilleageservice.saveOrUpdate(uv);
 	 	 						}
 	 	 						 
 	 	  					}
 	 	 				}
 	 				} 
 	 				selvilleage.setBanners(bannerservice.getByVilleage(selvilleage.getVilleage_id()));
 	 				selvilleage.setIntelligences(intellservice.getByVilleage(selvilleage.getVilleage_id()));
 	 				selvilleage.setUsers(uservilleageservice.getUserByVilleage(selvilleage.getVilleage_id()));
 	 				selvilleage.setVarieties(varietyservice.selectbyVilleage(selvilleage.getVilleage_id()));
 	    		    loadFinish();
				}
 			});
 	 		th.start();
 		}else loadFinish();
  	}
 	
 	private void loadFinish(){
 		NcpzsHandler handler=Gloable.getInstance().getCurHandler();
 		handler.post(new Runnable() {
 			@Override
			public void run() {
 				if(progress!=null) progress.dismiss();
 				gallery.cleare();
 				intellgenlay.removeAllViews();
 				varietylay.removeAllViews();
 				personlay.removeAllViews();
  				if(selvilleage.getBanners()!=null){
 					for(VilleageBanner banner:selvilleage.getBanners()){
 		 				gallery.addImage(BitmapUtils.getImageBitmapWithWidth(banner.getLocalurl(), Gloable.getInstance().getScreenWeight()));
  					}
 				}
  				Set<String> intels=getIntelligence();
 				if(intels!=null){
 					for(String intel:intels){
  						intellgenlay.addView(createIntelligenceImgView(intel));
  					}
  				}
 				if(selvilleage.getVarieties()!=null){
 					for(Variety var:selvilleage.getVarieties()){
 						varietylay.addView(createVarietyTextView(var));
  					}
  				}
 				if(selvilleage.getUsers()!=null){
 					for(User user:selvilleage.getUsers()){
 						personlay.addView(createPersonImage(user));
  					}
  				}
			}
		});
 	}
 	
 	private void initBaseVilleageInfo(final Villeage vil){
 		NcpzsHandler hand=Gloable.getInstance().getCurHandler();
 		hand.post(new Runnable() {
 			@Override
			public void run() {
				if(vil!=null){
		 			if(vil.getScale()!=null)
		 	  		   scale.setText("规模"+vil.getScale()+"亩");
		 	  		if(vil.getBulid_time()!=null)
		 	  		   buildtime.setText("创建于"+vil.getBulid_time()+"年");
		 	  		address.setText(vil.getAddress());
		 	  		tele.setText(vil.getTele());
		 	  		topview.setTitle(vil.getVilleage_name());
		 		}
			}
		});
   	}
 	
 	
 	/**
 	 * 农场资质图标
 	 * @param name
 	 * @return
 	 */
 	private ImageView createIntelligenceImgView(final String name){
 		final ImageView img=new ImageView(VilleageInfoActivity.this);
 		final SystemLabel label=labelservice.getByName(name);
 		if(label!=null){
 			String imgurl=label.getLabel_imageurl();
 	 		if(imgurl!=null){
 	 			Bitmap btm=getRecycleImg(imgurl);
 	 			if(btm==null){
 	 				btm=BitmapUtils.getAssetImg(imgurl);
 	 				if(btm!=null){
 	 					this.addRecycleImg(imgurl, btm);
 	 					img.setImageBitmap(btm);
 	 				}
 	  			}
 	 		}
 	 		img.setOnClickListener(new OnClickListener() {
 	 			@Override
 				public void onClick(View v) {
 	 				if(label.getType()==2){//系统认证的几个资质
 	 					String sysurl=label.getLabel_imageurl();
 	 					sysurl=sysurl.replaceAll(".png$", "_sys.png");
 	 					Bitmap btm=BitmapUtils.getAssetImg(sysurl);
 	 					imggllertwindow.addImg(btm);		
 	 					imggllertwindow.showAtLocation(root, Gravity.CENTER_VERTICAL|Gravity.LEFT, -10,0);
 	 				}else{
 	 					List<Intelligence> intls=intellservice.getByNameWithVilleage(name, villeageid);
 	 	 				NcpzsHandler handler=Gloable.getInstance().getCurHandler();
 	 	 				if(intls!=null){
 	 	 					int index=0;
 	 	 					for(Intelligence intl:intls){
 	 	 						Bitmap btm = null;
 	 							if(intl.getLocalurl()!=null&&FileUtil.hasFile(intl.getLocalurl())){
 	 								btm=BitmapUtils.getCompressImage(intl.getLocalurl());
 	 									//BitmapUtils.getImageBitmap(re.getLocalurl());
 	 							}else if(intl.getIntelligence_imgurl()!=null){
 	 								btm=BitmapFactory.decodeResource(getResources(), R.drawable.default_load);
 	 								ImageItem item=loadqueue.getFinishItem(intl,null);
 	 								if (item== null)// 如果没加载过，则进行加载
 	 									loadqueue.addItem(intl, null,index);
 	 								else btm=BitmapUtils.getCompressImage(((Intelligence)item.res).getLocalurl());
 	 							}
 	 							if(btm!=null){
 	 								final Bitmap addbtm=btm;
 	 								handler.post(new Runnable() {
 	 										@Override
 	 									public void run() {
 	 										imggllertwindow.addImg(addbtm);				
 	 									}
 	 								});
 	 							}
 	 							index++;
 	 	 					}
 	 	 					imggllertwindow.showAtLocation(root, Gravity.CENTER_VERTICAL|Gravity.LEFT, -10,0);
 	 	  				}
 	 				}
 	  			}
 			});
 		}
 		
  		return img;
  	}
 	/**
	 * 更新图片资源信息
	 * 
	 * @param url
	 * @param viewindex
	 */
    private void updateIntelligenceImg(Intelligence intel,int index) {
  		if (intel != null) {
			try {
				intellservice.updateImgLocalUrl((int)intel.getId(), intel.getLocalurl());
				Bitmap bmp=BitmapUtils.getCompressImage(intel.getLocalurl());
					//BitmapUtils.getImageBitmap(resource.getLocalurl());
				if(bmp!=null)
					imggllertwindow.replaceImg(index, bmp);
  			  
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
  
 	/**
 	 * 品种
 	 * @param var
 	 * @return
 	 */
 	private TextView createVarietyTextView(final Variety var){
 		TextView text=new TextView(VilleageInfoActivity.this);
 		text.setTextColor(Color.rgb(96, 94, 96));
 		text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
 		LinearLayout.LayoutParams lay=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.FILL_PARENT);
 		lay.leftMargin=DensityUtil.dip2px(10);
 		text.setLayoutParams(lay);
 		text.setText(var.getVariety_name());
 		text.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				Intent inte=new Intent(VilleageInfoActivity.this,VarietyBatchActivity.class);
	 			inte.putExtra("villeageid",  villeageid);
	 			inte.putExtra("varietyid",  var.getVariety_id());
	 			startActivity(inte);
			}
		});
 		return text;
  	}
   	
   	/**
   	 * 农人头像
   	 * @param user
   	 * @return
   	 */
 	private View createPersonImage(final User user ){
 		FrameLayout fram=new FrameLayout(VilleageInfoActivity.this);
 		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(DensityUtil.dip2px(40),DensityUtil.dip2px(40));
 		params.leftMargin=DensityUtil.dip2px(10);
 		fram.setLayoutParams(params);
 		ImageView person=new ImageView(VilleageInfoActivity.this);
 		person.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
 		person.setScaleType(ScaleType.CENTER_CROP);
 		person.setImageBitmap(BitmapUtils.getImageBitmap(user.getLocal_imgurl()));
 		ImageView yuanjiao=new ImageView(VilleageInfoActivity.this);
 		yuanjiao.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
 		yuanjiao.setImageResource(R.drawable.yuanjiao);
 		fram.addView(person);
 		fram.addView(yuanjiao);
 		fram.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				Intent inte=new Intent(VilleageInfoActivity.this,UserPageActivity.class);
	 			inte.putExtra("userid",  user.getUser_id());
	 			startActivity(inte);
			}
		});
  		return fram;
  	}
 	
 	private Set<String> getIntelligence(){
 		Set<String> rets=null;
 		if(selvilleage!=null&&selvilleage.getIntelligences()!=null){
 			rets=new HashSet<String>();
 			for(Intelligence intel:selvilleage.getIntelligences()){
 				rets.add(intel.getLabel_name());
 			}
 		}
 		return rets;
 	}
 	
 	 /**
	 * 显示选择农场界面
	 */
	private void showSelectVilleage(){
		if(this.uservils!=null){
			 String[] villeagenames=new String[this.uservils.size()];
			 int i=0;
			 for( Villeage vil:uservils)
				   villeagenames[i++]=vil.getVilleage_name();
			 
		     AlertDialog dialog= new AlertDialog.Builder(this).setTitle("").setItems(villeagenames,new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,int which) {
		 						 dialog.dismiss();
 		 						 Villeage sel=uservils.get(which);
  		 						  if(sel!=selvilleage){
 		 							 selvilleage=sel;
 		 							 initBaseVilleageInfo(selvilleage);
  			 						 new Thread(){
 										@Override
										public void run() {
 											 prepareloadVilleageInfo();
										}
 			 							 
 			 						 }.start();
  		 						  }
		 						
							}
			}).show();
		}
 	   
 		
  		
 	}
	 

	@Override
	protected void onDestroy() {
		gallery.releaseSource();
		if(bottomwindow!=null)
			bottomwindow.dismiss();
		if(imggllertwindow!=null)
			imggllertwindow.dismiss();
 		super.onDestroy();
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int keyCode=event.getKeyCode();
          switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
            	if(imggllertwindow.isShowing()){
            		imggllertwindow.dismiss();
            		return true;
            	}
            	if(bottomwindow.isShowing()){
            		bottomwindow.dismiss();
            		return true;
            	}
            	
            	return super.dispatchKeyEvent(event);	
            default:
             break;
        }
        return super.dispatchKeyEvent(event);
 	}
}