package com.axinxuandroid.activity;




import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.net.LoadCommentThread;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.UploadVilleagePhotoThread;
import com.axinxuandroid.activity.net.UploadVilleagePhotoThread.UploadVilleagePhotoFinishListener;
import com.axinxuandroid.activity.view.CommonTopView;
import com.axinxuandroid.data.UploadImg;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.VilleagePhoto;
import com.axinxuandroid.service.SharedPreferenceService;
import com.axinxuandroid.service.UserService;
import com.axinxuandroid.service.VilleagePhotoService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.BitmapUtils;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
 
public class UploadPhotoActivity extends NcpZsActivity{
	public static final int RETURN_FROM_SELECT_PHOTO = 1;
    private GridView  upgrid;
    private CommonTopView topview;
    private UploadPhotoAdapter adapter;
    private List<UploadImg> imgpaths;
    private int villeageid;
    private UploadPhotoQuene  uploadquene;
    private User user;
    private UserService uservice;
    private VilleagePhotoService photoservice;
    private volatile boolean isuploading=false;
    private EditText label;
    private String labeltext;
  	@Override
	public void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);	
		setContentView(R.layout.uploadphoto);
		villeageid=this.getIntent().getIntExtra("villeageid", -1);
		topview=(CommonTopView) this.findViewById(R.id.uploadphoto_topview);
		upgrid=(GridView) this.findViewById(R.id.uploadphoto_gridview);
		label=(EditText) this.findViewById(R.id.uploadphoto_label);
		uservice=new UserService();
		photoservice=new VilleagePhotoService();
		user=uservice.getLastLoginUser();
		imgpaths=new ArrayList<UploadImg>();
		adapter=new UploadPhotoAdapter(this);
		upgrid.setAdapter(adapter);
 		uploadquene=new UploadPhotoQuene();
		topview.setLeftClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				UploadPhotoActivity.this.finish();
			}
		});
		topview.setTitleClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				Intent inte = new Intent();
				inte.setClass(UploadPhotoActivity.this,PhotoAlbumActivity.class);
				startActivityForResult(inte, RETURN_FROM_SELECT_PHOTO);
			}
		});
		topview.setRightClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!isuploading){
					labeltext=label.getText().toString();
					NcpzsHandler hand=Gloable.getInstance().getCurHandler();
					if("".equals(labeltext)){
 						hand.excuteMethod(new  MessageDialogHandlerMethod("", "请填写标签"));
					}else{
						if(imgpaths==null||imgpaths.size()<1){
							hand.excuteMethod(new  MessageDialogHandlerMethod("", "请选择图片"));
						}else{
							adapter.notifyDataSetChanged();
							uploadquene.startUpload();
							topview.setRightText("暂停");
						}
 					}
 				}else{
					uploadquene.stopUpload();
  					finishUpload();
				}
				
 			}
		});
		 
   }
  	
 	
 	
 
 	 
 	
 	
 	
 	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 		if(requestCode==RETURN_FROM_SELECT_PHOTO){
 			if (data != null) {
 				if(resultCode==RESULT_OK){
 					ArrayList<String> paths= (ArrayList<String>) data.getExtras().get("path");
 					 if(paths!=null&&paths.size()>0)
 					   for(String url:paths){
  							if (url.indexOf("content://") != -1) {
 								url = getMediaUrl(url);
 							}
  							if(!imgpaths.contains(url)){
  								imgpaths.add(new UploadImg(url));
  								adapter.notifyDataSetChanged();
  							}
   					 }
 				}
 	 		}
 		}
	}
 	/**
	 *  
	 */
	private class UploadPhotoQuene {
		private boolean stop=false;
		private boolean next=true;
    	private UploadImg getNextUploadImgPath(){
  			if(imgpaths!=null&&imgpaths.size()>0){
  				//先搜索当前是否有进行中的上传
  				for(UploadImg img:imgpaths){
  					if(img.getStatus()==UploadImg.STATUS_UPLOADING)
  						return img;
  				}
  				for(UploadImg img:imgpaths){
  					if(img.getStatus()==UploadImg.STATUS_PREPARE)
  						return img;
  				}
 			}
			return null;
		}
    	public void stopUpload(){
    		this.stop=true;
    	}
    	public void startUpload(){
    		this.stop=false;
    		isuploading=true;
    		next=true;
    		uploadNext();
    	}
 		public void uploadNext(){
 			final UploadImg img=getNextUploadImgPath();
			if(img!=null&&!stop){
				if(img.getStatus()==UploadImg.STATUS_PREPARE){
					new Thread(){
	 					@Override
						public void run() {
	 						startUploadPhoto(img);
	 					}
	 				}.start();
				}
   			}else{
   				finishUpload();
    		}
			
  		}
 		public void uploadImg(final UploadImg img){
 			next=false;
 			if(img!=null){
				if(img.getStatus()==UploadImg.STATUS_PREPARE){
					new Thread(){
	 					@Override
						public void run() {
	 						startUploadPhoto(img);
	 					}
	 				}.start();
				}
   			}else{
   				finishUpload();
    		}
			
  		}
 		public boolean next(){
 			return next;
 		}
	}
	private void finishUpload(){
		isuploading=false;
		NcpzsHandler hand=Gloable.getInstance().getCurHandler();
		hand.post(new Runnable() {
 			@Override
			public void run() {
 				topview.setRightText("上传");
 				adapter.notifyDataSetChanged();
			}
		});	
	}
	private void startUploadPhoto(final UploadImg img){
		img.setStatus(UploadImg.STATUS_UPLOADING);
		NcpzsHandler hand=Gloable.getInstance().getCurHandler();
		hand.post(new Runnable() {
 			@Override
			public void run() {
 				adapter.notifyDataSetChanged();
			}
		});
		UploadVilleagePhotoThread uth=new UploadVilleagePhotoThread(img.getPath(),labeltext, villeageid,user.getUser_id());
		uth.setUploadVilleagePhotoFinishListener(new UploadVilleagePhotoFinishListener() {
 			@Override
			public void onfinish(int resultcode, Object resultdata) {
 				if (resultcode == UploadVilleagePhotoThread.RESULT_SUCCESS){
//					VilleagePhoto photo=(VilleagePhoto) ((Map) data).get(LoadCommentThread.RETURNDATA);
//					if(photo!=null)
//						photoservice.saveOrUpdateNotice(photo);
					img.setStatus(UploadImg.STATUS_UPLOAD_FINISH);
				}else img.setStatus(UploadImg.STATUS_UPLOAD_FINISH_ERROR);
				if(uploadquene.next())
				  uploadquene.uploadNext();
				else  finishUpload();
			}
		});
		 
		uth.start();
 		
	}

 	/**
	 * 从系统数据库查询文件地址
	 * @param data
	 * @return
	 */
	public String getMediaUrl(String data) {
		Uri uri = Uri.parse(data);
 		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor actualimagecursor = this.managedQuery(uri, proj, null, null,
				null);
		int actual_image_column_index = actualimagecursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		actualimagecursor.moveToFirst();
		String img_path = actualimagecursor
				.getString(actual_image_column_index);
		return img_path;
	}

	
	class  ViewHolder { 
		   View contview;
		   ImageView img,delete,finishimg; 
 	       TextView message;
	} 
	private class UploadPhotoAdapter extends BaseAdapter{
		private LayoutInflater inflater;
        public UploadPhotoAdapter(Context context){
        	inflater=LayoutInflater.from(context);
        }
		@Override
		public int getCount() {
 			return imgpaths==null?0:imgpaths.size();
		}

		@Override
		public Object getItem(int arg0) {
 			return arg0;
		}

		@Override
		public long getItemId(int position) {
 			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final UploadImg uploadimg=imgpaths.get(position);
			View view=convertView; 
			ViewHolder holder;
			if(view==null){
				view = inflater.inflate(R.layout.uploadphoto_item, null);
				holder=new ViewHolder();
 				holder.img=(ImageView) view.findViewById(R.id.uploadphoto_griditem_img);
				holder.delete=(ImageView) view.findViewById(R.id.uploadphoto_griditem_delete);
				holder.message=(TextView)view.findViewById(R.id.uploadphoto_griditem_msg);
				holder.finishimg=(ImageView) view.findViewById(R.id.uploadphoto_griditem_finishimg);
				holder.contview=view.findViewById(R.id.uploadphoto_griditem_convlay);
  				view.setTag(holder);
			}else{
				holder=(ViewHolder) view.getTag();
 			}
			holder.img.setImageBitmap(BitmapUtils.getCompressImage(uploadimg.getPath()));
			holder.delete.setVisibility(View.VISIBLE);
			holder.message.setVisibility(View.GONE);
			holder.finishimg.setVisibility(View.GONE);
			holder.contview.setVisibility(View.GONE);
 		 
 			if(isuploading||uploadimg.getStatus()!=UploadImg.STATUS_PREPARE){
 				holder.contview.setVisibility(View.VISIBLE);
 				holder.contview.getBackground().setAlpha(220);
 				holder.delete.setVisibility(View.GONE);
 				if(uploadimg.getStatus()==UploadImg.STATUS_UPLOAD_FINISH){
 					holder.finishimg.setVisibility(View.VISIBLE);
				}else if(uploadimg.getStatus()==UploadImg.STATUS_PREPARE){
					holder.message.setVisibility(View.VISIBLE); 
					holder.message.setText("等待上传");
				}else if(uploadimg.getStatus()==UploadImg.STATUS_UPLOADING){
					holder.message.setVisibility(View.VISIBLE); 
					holder.message.setText("上传中...");
				}else if(uploadimg.getStatus()==UploadImg.STATUS_UPLOAD_FINISH_ERROR){
					holder.message.setVisibility(View.VISIBLE); 
					holder.message.setText("上传失败\n点击重新上传");
				}
			}
 			view.setOnClickListener(new OnClickListener() {
 				@Override
				public void onClick(View v) {
 					if(uploadimg.getStatus()==UploadImg.STATUS_UPLOAD_FINISH_ERROR){
 						uploadimg.setStatus(UploadImg.STATUS_PREPARE);
 						adapter.notifyDataSetChanged();
 						if(!isuploading)
 						  uploadquene.uploadImg(uploadimg);
 					}
				}
			});
 			holder.delete.setOnClickListener(new OnClickListener() {
 				@Override
				public void onClick(View v) {
 					imgpaths.remove(uploadimg);
 					adapter.notifyDataSetChanged();
				}
			});
			//
 			return view;
		}			

 		
 	}

	@Override
	protected void onDestroy() {
 		super.onDestroy();
	}
  	
 
}