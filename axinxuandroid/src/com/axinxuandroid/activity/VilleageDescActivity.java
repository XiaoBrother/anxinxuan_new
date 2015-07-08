package com.axinxuandroid.activity;





 
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.anxinxuandroid.constant.AppConstant;
import com.axinxuandroid.activity.handler.ConfirmDialogHandlerMethod;
import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.net.DeleteVilleagePhotoThread;
import com.axinxuandroid.activity.net.LoadBatchByCodeOrIdThread;
import com.axinxuandroid.activity.net.LoadImageQueue;
import com.axinxuandroid.activity.net.LoadVillageInfoThread;
import com.axinxuandroid.activity.net.LoadVillagePhotoThread;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.activity.net.LoadImageQueue.ImageItem;
import com.axinxuandroid.activity.view.CommonTopView;
import com.axinxuandroid.activity.view.EditTabelView;
import com.axinxuandroid.activity.view.ImageGalleryView;
import com.axinxuandroid.activity.view.ImageGalleryView.OnLongTouchListener;
import com.axinxuandroid.activity.view.MoreWindow;
import com.axinxuandroid.activity.view.UserVilleageView;
import com.axinxuandroid.activity.view.ImageGalleryView.ImageGallery;
import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.Intelligence;
import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.RecordResource;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.Variety;
import com.axinxuandroid.data.Villeage;
import com.axinxuandroid.data.VilleageBanner;
import com.axinxuandroid.data.VilleagePhoto;
import com.axinxuandroid.data.UserVilleage.UserVilleageRole;
import com.axinxuandroid.db.SystemDB;
import com.axinxuandroid.service.IntelligenceService;
import com.axinxuandroid.service.UserService;
import com.axinxuandroid.service.UserVilleageService;
import com.axinxuandroid.service.VarietyService;
import com.axinxuandroid.service.VilleageBannerService;
import com.axinxuandroid.service.VilleagePhotoService;
import com.axinxuandroid.service.VilleageService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.BitmapUtils;
import com.ncpzs.util.DensityUtil;
import com.ncpzs.util.FileUtil;
  
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
 
public class VilleageDescActivity extends NcpZsActivity{
	private static final int MAX_SHOW_IMG_COUNT=10;
	private VilleageService villeageservice;
	private UserVilleageService uvilleageservice;
   	protected CommonTopView topview;
  	private Villeage villeage;
   	private ImageGalleryView gallery;
  	private TextView desc;
  	private VilleagePhotoService photoservice;
  	private UserService userService;
  	private ProgressDialog progress;
  	private List<VilleagePhoto> photos;
  	private MoreWindow bottomwindow;
  	private boolean isshow=false;
  	private LoadImageQueue loadqueue;
  	private User user;
   	@Override
	public void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);	
		setContentView(R.layout.villeagedesc);
 		int villeage_id = getIntent().getIntExtra("villeage_id",-1);
 		topview= (CommonTopView) this.findViewById(R.id.villeagedesc_topview);
  		gallery=(ImageGalleryView) this.findViewById(R.id.villeagedesc_imggallery);
   		desc=(TextView) this.findViewById(R.id.villeagedesc_desc);
  		villeageservice=new VilleageService();
  		uvilleageservice=new UserVilleageService();
 		photoservice=new VilleagePhotoService();
 		userService=new UserService();
  		photos=new ArrayList<VilleagePhoto>();
   		villeage=villeageservice.getByVilleageid(villeage_id);
   		topview.setTitle(villeage.getVilleage_name());
   		user=userService.getLastLoginUser();
 		if(villeage.getVilleage_desc()!=null){
 			desc.append(Html.fromHtml(villeage.getVilleage_desc(),null, null));
 		}
   		topview.setLeftClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
  				VilleageDescActivity.this.finish();
			}
		});
   		bottomwindow=new MoreWindow(VilleageDescActivity.this, new int[]{MoreWindow.EDIT_VILLEAGEDESC_FUN,
				MoreWindow.UPLOAD_VILLEAGEIMG_FUN,MoreWindow.SHARE_WEIBO_FUN,MoreWindow.SHARE_COMMENTS_FUN});
   		bottomwindow.addData(MoreWindow.DATA_VILLEAGE, villeage);
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
   		loadqueue=new LoadImageQueue();
   		loadqueue.setLiserner(new NetFinishListener() {
 			 
			@Override
			public void onfinish(NetResult data) {
				final ImageItem item=(ImageItem) data.returnData;
 				NcpzsHandler handler=Gloable.getInstance().getCurHandler();
 				handler.post(new Runnable() {
 					@Override
					public void run() {
  						updateViewPhotoData((VilleagePhoto)item.res,item.index);
					}
				});
			}
		});
   		
   		gallery.setOnLongTouchListener(new OnLongTouchListener() {
 			@Override
			public void onLongTouch(final int position) {
 				NcpzsHandler handler=Gloable.getInstance().getCurHandler();
  				int role=uvilleageservice.getUserRoleInVilleage(user.getUser_id(), villeage.getVilleage_id());
 				if(user!=null&&role==UserVilleageRole.VILLEAGE_ROLE_MASTER){
 					handler.setOnHandlerFinishListener(new OnHandlerFinishListener(){
 						@Override
						public void onHandlerFinish(Object result) {
 							int res=(Integer) ((Map)result).get("result");
 							if(res==1){
 								prepareDeletePhoto(position);
 							}
						}
  					});
 					handler.excuteMethod(new ConfirmDialogHandlerMethod("", "您要删除该农场图片吗？"));
 				}
			}
		});
   		new Thread(){
 			@Override
			public void run() {
 				prepareLoad();
			}
   		}.start();
   		
   }
	
	private void prepareLoad(){
		NcpzsHandler processhandler=Gloable.getInstance().getCurHandler();
	    processhandler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
						@Override
						public void onHandlerFinish(Object result) {
							if(result!=null)
								progress=(ProgressDialog) ((Map)result).get("process");
							loadVilleagePhoto();
						}
					});
		 processhandler.excuteMethod(new ProcessDialogHandlerMethod("", "数据加载中...."));
	}

	private void loadVilleagePhoto(){
		String lastoptime=photoservice.getLastoptime(villeage.getVilleage_id());
		LoadVillagePhotoThread lth=new LoadVillagePhotoThread(villeage.getVilleage_id(), lastoptime);
		lth.setLiserner(new NetFinishListener() {
  			@Override
			public void onfinish(NetResult data) {
  				if(data.result==NetResult.RESULT_OF_SUCCESS){
 					List<VilleagePhoto> ps=(List<VilleagePhoto>)data.returnData;//网络
 					if(ps!=null){
  						for(VilleagePhoto p:ps)
 							photoservice.saveOrUpdateNotice(p);
 					}
 						
 				}
 				photos=photoservice.getByVilleageid(villeage.getVilleage_id(),MAX_SHOW_IMG_COUNT);//本地
  				finishLoad();
			}
		});
		lth.start();
	}
	private void finishLoad(){
		NcpzsHandler handler=Gloable.getInstance().getCurHandler();
		handler.post(new Runnable() {
			@Override
			public void run() {
 				if(progress!=null)
 					progress.dismiss();
 				if(photos!=null&&photos.size()>0){
 					int  index=0;
 					for(VilleagePhoto photo:photos){{
  						if(photo.getLocalurl()!=null&&FileUtil.hasFile(photo.getLocalurl())){
 							gallery.addImage(BitmapUtils.getImageBitmap(photo.getLocalurl()));
 						}else if(photo.getImage_url()!=null){
 							gallery.addImage(BitmapFactory.decodeResource(getResources(), R.drawable.default_load));
  							loadqueue.addItem(photo, gallery,index);
  						}
  						index++;
 					}
 				   }
  	 			}
			}
		});
	}
	
	
	
	private void prepareDeletePhoto(final int position ){
		NcpzsHandler processhandler=Gloable.getInstance().getCurHandler();
	    processhandler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
						@Override
						public void onHandlerFinish(Object result) {
							if(result!=null)
								progress=(ProgressDialog) ((Map)result).get("process");
							deletePhoto(position);
							 
						}
					});
		 processhandler.excuteMethod(new ProcessDialogHandlerMethod("", "正在删除...."));
	}

	
	private void deletePhoto(final int position){
		VilleagePhoto photo=photos.get(position);
		DeleteVilleagePhotoThread delth=new DeleteVilleagePhotoThread(photo.getPhotoid(),user);
		delth.setLiserner(new NetFinishListener() {
 			@Override
			public void onfinish(NetResult data) {
  				deletePhotoFinish(position,data.result);
			}
		});
		delth.start();
	}
	
	private void deletePhotoFinish(final int position,final int result){
		final NcpzsHandler handler=Gloable.getInstance().getCurHandler();
		handler.post(new Runnable() {
 			@Override
			public void run() {
 				if(progress!=null&&progress.isShowing())
 					progress.dismiss();
 				if(result==NetResult.RESULT_OF_SUCCESS){
 					VilleagePhoto photo=photos.get(position);
 					photo.setIsdel(SystemDB.DATA_HAS_DELETE);
 					photoservice.saveOrUpdateNotice(photo);
 					photos.remove(photo);
 					gallery.removeImage(position);
 					handler.excuteMethod(new MessageDialogHandlerMethod("","删除成功！"));
  				}else{
 					handler.excuteMethod(new MessageDialogHandlerMethod("","删除失败！"));
 				}
			}
		});
	}
	@Override
	protected void onDestroy() {
		gallery.releaseSource();
		loadqueue.stopLoad();
		if(bottomwindow!=null)
				bottomwindow.dismiss();
 		super.onDestroy();
	}
	
	/**
	 * 更新图片资源信息
	 * 
	 * @param url
	 * @param viewindex
	 */
    private void updateViewPhotoData(VilleagePhoto photo,int index) {
  		if (photo != null) {
			try {
				photoservice.updateImageLocalUrl(photo.getId(), photo.getLocalurl());
 				Bitmap bmp=BitmapUtils.getCompressImage(photo.getLocalurl());
					//BitmapUtils.getImageBitmap(resource.getLocalurl());
				if(bmp!=null)
				 gallery.replaceImage(bmp, index);
  			  
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	
}