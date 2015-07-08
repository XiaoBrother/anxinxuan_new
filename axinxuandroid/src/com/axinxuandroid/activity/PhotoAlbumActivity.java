package com.axinxuandroid.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.anxinxuandroid.constant.AppConstant;
import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.view.CommonTopView;
import com.axinxuandroid.activity.view.PhotoBottomView;
import com.axinxuandroid.activity.view.ResourcesView;
import com.axinxuandroid.activity.view.PhotoBottomView.ItemClickListener;
 import com.axinxuandroid.data.PhotoAibum;
 import com.axinxuandroid.data.PhotoItem;
 import com.axinxuandroid.sys.gloable.Gloable;
import com.axinxuandroid.sys.gloable.Session;
import com.axinxuandroid.sys.gloable.SessionAttribute;
import com.ncpzs.util.DensityUtil;
import com.ncpzs.util.LogUtil;

 import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
 import android.provider.MediaStore.Images.Thumbnails;
import android.view.Gravity;
import android.view.KeyEvent;
 import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
 import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.GridView;

/******************************************
 * �������� �������� �����ƣ�PhotoAlbumActivity
 * 
 * @version: 1.0
 * @author: why
 * @time: 2013-10-18 ����2:10:46
 *****************************************/
public class PhotoAlbumActivity extends NcpZsActivity {
	public static final int RETURN_FROM_TAKE_CAMERA = 1;
	public static final int RETURN_FROM_SELECT_PHOTO = 2;
	private GridView gridview;
	private ListView listview;
 	private List<PhotoAibum> aibumList;
 	private ProgressDialog process;
 	private List<PhotoItem> photolist;
	private PhotoListAdapter padapter;
	private PhotoGridAdapter tadapter;
 	private static final String FITEM="content://fitem";
	private static final String PHOTO_DIR=Environment.getExternalStorageDirectory()+"/"+Environment.DIRECTORY_DCIM;
 	private static final String[] STORE_IMAGES = {
			MediaStore.Images.Media.DISPLAY_NAME, // ��ʾ����
			MediaStore.Images.Media.DATA, MediaStore.Images.Media.LONGITUDE, // ����
			MediaStore.Images.Media._ID, // id
			MediaStore.Images.Media.BUCKET_ID, // dir id Ŀ¼
			MediaStore.Images.Media.BUCKET_DISPLAY_NAME // dir name Ŀ¼����

	};
 	private Animation inanimation ;
	private Animation outanimation;
	private PhotoBottomView bottomview;
  	private CommonTopView topview;
	protected String photo_img_save_path;//����ͼƬ�����ַ
 	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photoalbum);
		gridview = (GridView) findViewById(R.id.photoalbum_gridview);
		listview=(ListView) findViewById(R.id.photoalbum_listview);
		bottomview=(PhotoBottomView) findViewById(R.id.photoalbum_bottomview);
 		topview=(CommonTopView) findViewById(R.id.photoalbum_topview);
 		aibumList = new ArrayList<PhotoAibum>();
		photolist=new ArrayList<PhotoItem>(); 
		PhotoItem fitem=new PhotoItem();
		fitem.setPath(FITEM);
		photolist.add(fitem);
		tadapter=new PhotoGridAdapter(this);
		gridview.setAdapter(tadapter);
		int allwidth=Gloable.getInstance().getScreenWeight();
 		gridview.setColumnWidth(allwidth/4);
  		padapter = new PhotoListAdapter(this);
 		listview.setOnItemClickListener(aibumClickListener);
 		inanimation=AnimationUtils.loadAnimation(this, R.anim.fade_in);
 		outanimation=AnimationUtils.loadAnimation(this, R.anim.fade_out);
     	gridview.setOnItemClickListener(new OnItemClickListener() {
 			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
 				if(position!=0){
 					ImageView select=(ImageView) view.findViewById(R.id.photo_grid_select);
 					List<PhotoItem> photos=(List<PhotoItem>) Session.getInstance().getAttribute(SessionAttribute.SESSION_SELECT_PHOTOS);
 	   				if(select.getVisibility()==View.GONE){
 	   					if(photos!=null&&photos.size()>=9){
 	   						NcpzsHandler hand=Gloable.getInstance().getCurHandler();
 	   					    hand.excuteMethod(new MessageDialogHandlerMethod("","���֧��9��ͼƬ!"));
 	   					}else{
	 	   					select.setVisibility(View.VISIBLE);
	 	 					select.startAnimation(inanimation);
	 	 				    if(photos==null){
	 	 				    	photos=new ArrayList<PhotoItem>();
	  	 				    }
	 	 				    bottomview.addBottomItem(photolist.get(position));
	 	  				    photos.add(photolist.get(position));
	 	 				    Session.getInstance().setAttribute(SessionAttribute.SESSION_SELECT_PHOTOS, photos);
 	   					}
  	 				}else{
 	 					select.startAnimation(outanimation);
 	 					select.setVisibility(View.GONE);
 	 					if(photos!=null)
 	 						photos.remove(photolist.get(position));
 	 					bottomview.removeBottomItem(photolist.get(position));
 	 				}
  				}else{
 					takecamera();
 				}
 			}
		});
    	topview.setRightClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				Intent aintent = new Intent(PhotoAlbumActivity.this,AddRecordActivity.class);
 				setResult(RESULT_CANCELED, aintent);
 				Session.getInstance().removeAttribute(SessionAttribute.SESSION_SELECT_PHOTOS);
 				PhotoAlbumActivity.this.finish();
			}
		});
    	bottomview.setItemClickListener(new ItemClickListener() {
 			@Override
			public void onClickItem(PhotoItem item) {
 				int index=photolist.indexOf(item);
 				if(index>-1){
 					gridview.getChildAt(index).findViewById(R.id.photo_grid_select).setVisibility(View.GONE);
  				}
 				List<PhotoItem> sphotos=(List<PhotoItem>) Session.getInstance().getAttribute(SessionAttribute.SESSION_SELECT_PHOTOS);
	            if(sphotos!=null)
	                	sphotos.remove(item);
 			}
		});
    	bottomview.setOnOkBtnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				Intent aintent = new Intent(PhotoAlbumActivity.this,AddRecordActivity.class);
  		  		aintent.putExtra("path", getPaths());
 				setResult(RESULT_OK, aintent);
 				Session.getInstance().removeAttribute(SessionAttribute.SESSION_SELECT_PHOTOS);
 				PhotoAlbumActivity.this.finish();
			}
		});
    	initBottom();
   		// ��ʼ����ͼƬ����
		new Thread() {
			@Override
			public void run() {
				prepareData();
			}
		}.start();
	}

 	 private void initBottom(){
 		bottomview.clearAll();
 		List<PhotoItem> sphotos=(List<PhotoItem>) Session.getInstance().getAttribute(SessionAttribute.SESSION_SELECT_PHOTOS);
         if(sphotos!=null){
         	for(PhotoItem item:sphotos)
         		bottomview.addBottomItem(item);
         }
    }
	@Override
	protected void onResume() {
 		super.onResume();
 		initBottom();
   		tadapter.notifyDataSetChanged();
	}


	/**
	 * ������¼�
	 */
	OnItemClickListener aibumClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent inte = new Intent();
			inte.setClass(PhotoAlbumActivity.this, SelectPhotoActivity.class);
			inte.putExtra("aibum", aibumList.get(position));
 			startActivityForResult(inte, RETURN_FROM_SELECT_PHOTO);
		}
	};

	/**
	 * ����������������ȡͼƬ��Ϣ
	 * 
	 * @author: why
	 * @time: 2013-10-18 ����1:35:24
	 */
	private void getPhotoAlbum() {
		/**
		 * ���SQL�Ľṹ�ǣ�select projection from table where ( selection ) order by
		 * order; ����projection������Ҫ��ѯ���У� ����table�Ǹ���Uriȷ�������ݿ��
		 * ����selectionʹ�����Լ��Ĳ�ѯ������ ����order��������Ҫ������ʽ�� ���group by���� select
		 * projection from table where ( selection ) group by ( group ) order by
		 * order;
		 */
		String selection = " 1=1 ) group by ("
				+ MediaStore.Images.Media.BUCKET_ID;
		Cursor cursor = managedQuery(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STORE_IMAGES,
				selection, null, null);
		// MediaStore.Images.Media.query(getContentResolver(),
		// MediaStore.Images.Media.EXTER NAL_CONTENT_URI, STORE_IMAGES);
 		PhotoAibum pa = null;
		while (cursor!=null&&cursor.moveToNext()) {
			String path = cursor.getString(1);
			String id = cursor.getString(3);
			String dir_id = cursor.getString(4);
			String dir = cursor.getString(5);
  			String countselection = MediaStore.Images.Media.BUCKET_ID + "="
					+ dir_id;
			Cursor countcursor = managedQuery(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					new String[] { "count(*)" }, countselection, null, null);
			countcursor.moveToFirst();
			int num = countcursor.getInt(0);
 			//4.0���ϵİ汾���Զ��ر� (4.0--14;; 4.0.3--15)  
	        if(Integer.parseInt(Build.VERSION.SDK) < 14)  {  
	        	countcursor.close();
	        }  
			pa = new PhotoAibum();
			pa.setName(dir);
			pa.setBitmap(Integer.parseInt(id));
			pa.setCount(num + "");
			pa.setDir_id(dir_id);
			pa.setPath(path);
 			aibumList.add(pa);
		}
		 //4.0���ϵİ汾���Զ��ر� (4.0--14;; 4.0.3--15)  
        if(Integer.parseInt(Build.VERSION.SDK) < 14)  {  
            cursor.close();  
        }  
  	}
	/**
	 * ����������������ȡͼƬ��Ϣ
	 * 
	 * @author: why
	 * @time: 2013-10-18 ����1:35:24
	 */
	private void getCameraPhotos() {
 		String selection =MediaStore.Images.Media.DATA+ " like '"+PHOTO_DIR+"%'";
 		Cursor cursor = managedQuery(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STORE_IMAGES,
				selection, null, MediaStore.Images.Media.DATE_MODIFIED+" desc");
	 
 		PhotoItem pa = null;
		while (cursor!=null&&cursor.moveToNext()) {
			String path = cursor.getString(1);
			String id = cursor.getString(3);
  			pa = new PhotoItem();
			pa.setPath(path);
			pa.setPhotoID(id);
  			photolist.add(pa);
  			if(photolist.size()>=8)
  				break;
		}
		 //4.0���ϵİ汾���Զ��ر� (4.0--14;; 4.0.3--15)  
        if(Integer.parseInt(Build.VERSION.SDK) < 14)  {  
            cursor.close();  
        }  
  	}
	/**
	 * �����ؽ��
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Intent aintent = new Intent(PhotoAlbumActivity.this,AddRecordActivity.class);
		if(requestCode==RETURN_FROM_SELECT_PHOTO){
			if (resultCode ==RESULT_OK) {
				aintent.putExtra("path", getPaths());
				setResult(RESULT_OK, aintent);
				Session.getInstance().removeAttribute(SessionAttribute.SESSION_SELECT_PHOTOS);
				PhotoAlbumActivity.this.finish();
	 		}else if(resultCode ==RESULT_BACK){
				setResult(RESULT_CANCELED, aintent);
				Session.getInstance().removeAttribute(SessionAttribute.SESSION_SELECT_PHOTOS);
				PhotoAlbumActivity.this.finish();
			}
		}else if(requestCode==RETURN_FROM_TAKE_CAMERA){
			 if(resultCode==RESULT_OK){
				 ArrayList<String> paths = new ArrayList<String>();
				 paths.add(photo_img_save_path);
				 aintent.putExtra("path", paths);
				 setResult(RESULT_OK, aintent);
				 Session.getInstance().removeAttribute(SessionAttribute.SESSION_SELECT_PHOTOS);
				 PhotoAlbumActivity.this.finish();
 			 }
		}
  		
	}
	/**
	 * ����
	 */
	public void takecamera() {
 		photo_img_save_path=AppConstant.CAMERA_DIR+System.currentTimeMillis()+".jpg";
 		LogUtil.logInfo(getClass(), "photo_img_save_path:"+photo_img_save_path);
 		Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
        i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(photo_img_save_path)));//����ͼƬ����·�� 
        startActivityForResult(i, RETURN_FROM_TAKE_CAMERA); 
	}
	public void prepareData() {
		NcpzsHandler handler = Gloable.getInstance().getCurHandler();
		handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
			@Override
			public void onHandlerFinish(Object result) {
				if (result != null)
					process = (ProgressDialog) ((Map) result).get("process");
				new Thread() {
					@Override
					public void run() {
 							getPhotoAlbum();
 							getCameraPhotos();
 							loadFinish();
 					}
				}.start();
 			}
		});
		handler.excuteMethod(new ProcessDialogHandlerMethod("", "������...."));
	}

	public void loadFinish() {
		NcpzsHandler handler = Gloable.getInstance().getCurHandler();
		handler.post(new Runnable() {
			@Override
			public void run() {
				if (process != null)
					process.dismiss();
				tadapter.notifyDataSetChanged();
				listview.setAdapter(padapter);
				padapter.notifyDataSetChanged();
				
			}
		});
	}

	private ArrayList<String> getPaths(){
		ArrayList<String> paths = new ArrayList<String>();
		List<PhotoItem> photos=(List<PhotoItem>) Session.getInstance().getAttribute(SessionAttribute.SESSION_SELECT_PHOTOS);
		if(photos!=null&&photos.size()>0)
			for(PhotoItem p:photos)
				paths.add(p.getPath());
		return paths;
	}
	 
	 
	static class ViewHolder {
		ImageView iv;
		TextView tv;
		ImageView fgimg;
	}

	public class PhotoListAdapter extends BaseAdapter {
		private Context context;
		private ViewHolder holder;

		public PhotoListAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			return aibumList == null ? 0 : aibumList.size();
		}

		@Override
		public Object getItem(int position) {
			if (aibumList != null)
				return aibumList.get(position);
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView =   LayoutInflater.from(context)
						.inflate(R.layout.photoalbum_item, null);
				holder = new ViewHolder();
				holder.iv = (ImageView) convertView
						.findViewById(R.id.photoalbum_item_image);
				holder.tv = (TextView) convertView
						.findViewById(R.id.photoalbum_item_name);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
  			Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(
					getContentResolver(), aibumList.get(position).getBitmap(),
					Thumbnails.MICRO_KIND, null);
			holder.iv.setImageBitmap(bitmap);
			holder.tv.setText(aibumList.get(position).getName() + " ( "
					+ aibumList.get(position).getCount() + " )");
			return convertView;
		}

	}
	
	
	public class PhotoGridAdapter extends BaseAdapter {
		private Context context;
		private ViewHolder holder;
  		public PhotoGridAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			return photolist == null ? 0 : photolist.size();
		}

		@Override
		public Object getItem(int position) {
 			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			PhotoItem photo=photolist.get(position);
			int allwidth=Gloable.getInstance().getScreenWeight();
			List<PhotoItem> sphotos=(List<PhotoItem>) Session.getInstance().getAttribute(SessionAttribute.SESSION_SELECT_PHOTOS);
 			if (convertView == null) {
				convertView =   LayoutInflater.from(context)
						.inflate(R.layout.photoalbum_gridview_item, null);
				holder = new ViewHolder();
				holder.iv = (ImageView) convertView
						.findViewById(R.id.photo_grid_img_view);
				holder.fgimg = (ImageView) convertView
						.findViewById(R.id.photo_grid_select);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
				holder.fgimg.setVisibility(View.GONE);
			}
// 			LayoutParams params=holder.iv.getLayoutParams();
// 			params.width=allwidth/4-DensityUtil.dip2px(4);
// 			params.height=allwidth/4-DensityUtil.dip2px(4);
// 			holder.iv.setLayoutParams(params);
// 			holder.fgimg.setLayoutParams(params);
 			if(photo!=null){
				Bitmap bitmap = null;
				if(FITEM.equals(photo.getPath())){
   				  // holder.iv.setImageBitmap(null);
   				   holder.iv.setScaleType(ScaleType.CENTER_INSIDE);
  				   holder.iv.setImageResource(R.drawable.opencamera);
				}else{ 
				   bitmap = MediaStore.Images.Thumbnails.getThumbnail(
							getContentResolver(),Long.parseLong(photo.getPhotoID()),
							Thumbnails.MICRO_KIND, null);
				   holder.iv.setScaleType(ScaleType.FIT_XY);
  				   holder.iv.setImageBitmap(bitmap);
				}
				
 			}
 			if(sphotos!=null&&sphotos.contains(photo)){
 				holder.fgimg.setVisibility(View.VISIBLE);
 			}
 			return convertView;
		}

	}
	
	
}
