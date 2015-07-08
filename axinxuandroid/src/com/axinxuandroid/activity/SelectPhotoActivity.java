package com.axinxuandroid.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import com.axinxuandroid.activity.PhotoAlbumActivity.ViewHolder;
import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.view.CommonTopView;
import com.axinxuandroid.activity.view.PhotoBottomView;
import com.axinxuandroid.activity.view.PhotoBottomView.ItemClickListener;
import com.axinxuandroid.data.PhotoAibum;
 import com.axinxuandroid.data.PhotoItem;
 import com.axinxuandroid.sys.gloable.Gloable;
import com.axinxuandroid.sys.gloable.Session;
import com.axinxuandroid.sys.gloable.SessionAttribute;
import com.ncpzs.util.BitmapUtils;
import com.ncpzs.util.DensityUtil;
import com.ncpzs.util.LoadThumbnailThreadPool;
import com.ncpzs.util.LogUtil;
import com.ncpzs.util.LoadThumbnailThreadPool.ExecuteTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SelectPhotoActivity extends NcpZsActivity {
	private GridView gv;
	private LinearLayout bottomlay;
	private PhotoAibum aibum;
 	private PhotoAdappter adapter;
 	private ProgressDialog process;
  	private ArrayList<PhotoItem> photos;
 	private Animation inanimation ;
	private Animation outanimation;
	private static final String[] STORE_IMAGES = {
			MediaStore.Images.Media.DATA, // 路径
			MediaStore.Images.Media._ID // id
	};
    private CommonTopView topview;
    private Handler handler=new Handler();
    private LoadThumbnailThreadPool threadpool;
    private PhotoBottomView bottomview;
    private int allwidth=Gloable.getInstance().getScreenWeight();
  	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_photo);
 		aibum = (PhotoAibum) getIntent().getExtras().get("aibum");
		topview=(CommonTopView) findViewById(R.id.selectphoto_topview);
		gv = (GridView) findViewById(R.id.selectphoto_gridview);
		bottomview = (PhotoBottomView) findViewById(R.id.selectphoto_bottomview);
		adapter = new PhotoAdappter(this);
		gv.setAdapter(adapter);
		gv.setColumnWidth(allwidth/4);
		inanimation=AnimationUtils.loadAnimation(this, R.anim.fade_in);
 		outanimation=AnimationUtils.loadAnimation(this, R.anim.fade_out);
 		
  		bottomview.setItemClickListener(new ItemClickListener() {
 			@Override
			public void onClickItem(PhotoItem item) {
 				int index=photos.indexOf(item);
 				if(index>-1){
 					int findex=gv.getFirstVisiblePosition();
 					int lindex=gv.getLastVisiblePosition();
 					if(index>=findex&&index<=lindex)
 					gv.getChildAt(index-findex).findViewById(R.id.photo_grid_select).setVisibility(View.GONE);
  				}
 				List<PhotoItem> sphotos=(List<PhotoItem>) Session.getInstance().getAttribute(SessionAttribute.SESSION_SELECT_PHOTOS);
	            if(sphotos!=null)
	                	sphotos.remove(item);
 			}
		});
 		bottomview.setOnOkBtnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				Intent aintent = new Intent(SelectPhotoActivity.this,
						PhotoAlbumActivity.class);
 				setResult(RESULT_OK, aintent);
				SelectPhotoActivity.this.finish();
			}
		});
		gv.setOnItemClickListener(new OnItemClickListener() {
 			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
  				ImageView select=(ImageView) view.findViewById(R.id.photo_grid_select);
				List<PhotoItem> sphotos=(List<PhotoItem>) Session.getInstance().getAttribute(SessionAttribute.SESSION_SELECT_PHOTOS);
   				if(select.getVisibility()==View.GONE){
   					if(sphotos!=null&&sphotos.size()>=9){
	   						NcpzsHandler hand=Gloable.getInstance().getCurHandler();
	   					    hand.excuteMethod(new MessageDialogHandlerMethod("","最多支持9张图片!"));
	   				}else{
	   					select.setVisibility(View.VISIBLE);
	 					select.startAnimation(inanimation);
	 				    if(sphotos==null){
	 				    	sphotos=new ArrayList<PhotoItem>();
	  				    }
	 				    sphotos.add(photos.get(position));
	 				    bottomview.addBottomItem(photos.get(position));
	 				    Session.getInstance().setAttribute(SessionAttribute.SESSION_SELECT_PHOTOS, sphotos);
	   				}
  				}else{
 					select.startAnimation(outanimation);
 					select.setVisibility(View.GONE);
 					if(sphotos!=null)
 						sphotos.remove(photos.get(position));
 					bottomview.removeBottomItem(photos.get(position));
 				}
 			}
		});
		topview.setLeftClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				SelectPhotoActivity.this.finish();
			}
		});
		topview.setRightClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				Intent aintent = new Intent(SelectPhotoActivity.this,
						PhotoAlbumActivity.class);
 				setResult(RESULT_BACK, aintent);
				SelectPhotoActivity.this.finish();
			}
		});
		topview.setTitle(aibum.getName());
		initBottom();
		threadpool=LoadThumbnailThreadPool.createPool();
 		// 开始加载图片数据
		new Thread() {
			@Override
			public void run() {
				prepareData();
			}
		}.start();
	}

   private void initBottom(){
		List<PhotoItem> sphotos=(List<PhotoItem>) Session.getInstance().getAttribute(SessionAttribute.SESSION_SELECT_PHOTOS);
        if(sphotos!=null){
        	for(PhotoItem item:sphotos)
        		bottomview.addBottomItem(item);
        }
   }
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK )  {  
//			Intent aintent = new Intent(SelectPhotoActivity.this,PhotoAlbumActivity.class);
// 			setResult(RESULT_CANCELED, aintent);
//			SelectPhotoActivity.this.finish();
//        }  
//  		return super.onKeyDown(keyCode, event);
//	}

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
						getPhotos();
						loadFinish();
					}
				}.start();
			}
		});
		handler.excuteMethod(new ProcessDialogHandlerMethod("", "加载中...."));
	}

	/**
	 * 方法描述：获取图片信息
	 * 
	 * @author: why
	 * @time: 2013-10-18 下午1:35:24
	 */
	private void getPhotos() {
		String selection = MediaStore.Images.Media.BUCKET_ID + "="
				+ aibum.getDir_id();
		Cursor cursor = managedQuery(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STORE_IMAGES,
				selection, null, null);
		if (cursor.getCount() > 0) {
			photos = new ArrayList<PhotoItem>();
			PhotoItem pa = null;
			while (cursor.moveToNext()) {
				String path = cursor.getString(0);
				String id = cursor.getString(1);
				pa = new PhotoItem();
				pa.setPath(path);
 				pa.setPhotoID(id);
				photos.add(pa);
			}
		}
		cursor.close();
	}

	public void loadFinish() {
		NcpzsHandler handler = Gloable.getInstance().getCurHandler();
		handler.post(new Runnable() {
			@Override
			public void run() {
				if (process != null)
					process.dismiss();
				adapter.notifyDataSetChanged();
			}
		});
	}
	
	static class ViewHolder {
		ImageView iv;
 		ImageView fgimg;
	}

	public class PhotoAdappter extends BaseAdapter implements OnScrollListener{
		private Context context;
		private ViewHolder holder;
		public PhotoAdappter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			return photos == null ? 0 : photos.size();
		}

		@Override
		public PhotoItem getItem(int position) {
			return photos == null ? null : photos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final PhotoItem photo=photos.get(position);
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
 			ViewGroup.LayoutParams params=holder.iv.getLayoutParams();
 			params.width=allwidth/4-DensityUtil.dip2px(4);
 			params.height=allwidth/4-DensityUtil.dip2px(4);
 			holder.iv.setLayoutParams(params);
 			holder.fgimg.setLayoutParams(params);
 			if(photo!=null){//特殊处理，position 0多次加载。这是由于gridview不知道它里面到底能放多少item。因此多次加载position 0来适配。
 				holder.iv.setImageDrawable(new ColorDrawable(Color.rgb(0, 0, 0)));
  				if(position!=0){
  					if(position==1){//记载第一个
    	 				threadpool.addNewRunnableTask(photos.get(0).getPhotoID(), new LoadThumbnailRunnable(photos.get(0).getPhotoID(),0));
   					}
	 				threadpool.addNewRunnableTask(photo.getPhotoID(), new LoadThumbnailRunnable(photo.getPhotoID(),position));
  				}
  			}
 			if(sphotos!=null&&sphotos.contains(photo)){
 				holder.fgimg.setVisibility(View.VISIBLE);
 			}
 			return convertView;
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			
		}

	}

	
	public class LoadThumbnailRunnable implements Runnable{
 		private String photoid;
		private int position;
		public LoadThumbnailRunnable(String photoid,int position){
 			this.photoid=photoid;
			this.position=position;
		}
 		@Override
		public void run() {
 			int fposition=gv.getFirstVisiblePosition();
		    int lposition=gv.getLastVisiblePosition();
		    if(position>=fposition&&position<=lposition){
		    	final Bitmap  bitmap = MediaStore.Images.Thumbnails.getThumbnail(
						getContentResolver(),Long.parseLong(photoid),
						Thumbnails.MICRO_KIND, null);
				final int pos=position;
	 			if(bitmap!=null){
	  				handler.post(new Runnable() {
	 					@Override
						public void run() {
	 						int fposition=gv.getFirstVisiblePosition();
	 						int lposition=gv.getLastVisiblePosition();
	  						if(pos>=fposition&&pos<=lposition){
	  							((ImageView)gv.getChildAt(pos-fposition).findViewById(R.id.photo_grid_img_view)).setImageBitmap(bitmap); 
	  						}
							  
	 					}
					});
	 			}
		    }
 		}
 	}
//	private class DefaultGestureListener extends SimpleOnGestureListener{
//
//		@Override
//		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
//				float velocityY) {
//			isScrolling=false;
// 			return super.onFling(e1, e2, velocityX, velocityY);
//		}
// 
//		@Override
//		public void onLongPress(MotionEvent e) {
//			isScrolling=false;
//			super.onLongPress(e);
//		}
//
//		@Override
//		public boolean onScroll(MotionEvent e1, MotionEvent e2,
//				float distanceX, float distanceY) {
//			isScrolling=true;
// 			return super.onScroll(e1, e2, distanceX, distanceY);
//		}
//
//		 
//		
//	}
//	@Override
//	public boolean dispatchTouchEvent(MotionEvent ev) {
//		gestureDetector.onTouchEvent(ev);
//		return super.dispatchTouchEvent(ev);
//	}
//	 


	@Override
	protected void onDestroy() {
		threadpool.stopLoad();
		super.onDestroy();
	}
	 
}
