package com.axinxuandroid.activity.view;

import java.util.ArrayList;
import java.util.List;

import com.axinxuandroid.activity.R;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.BitmapUtils;
import com.ncpzs.util.DensityUtil;
import com.ncpzs.util.LogUtil;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

public class ImageGalleryView extends ViewGroup {

  	private static final int UP_INDEX_LAYOUT_MODE = 0x1;
	private static final int DOWN_INDEX_LAYOUT_MODE = 0x2;
	private List<Bitmap> images;
	private Context context;
	private LinearLayout gallerylayout;
	private LinearLayout upindexlayout, downindexlayout;
	private ImageGalleryAdapter imageadapter;
	private boolean showindex = true;
	private int currentindex = 1;
	private int indexmode;
    private  ScaleType  userst=null;
    private  int  imgwidth=0;
    private  int  imgheight=0;
    private long LONG_TOUCH_TIME=2000;
    private long startTouchTime=0;
    private OnLongTouchListener lis;
    private ImageGallery gallery ;
 	public ImageGalleryView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray tArray = context.obtainStyledAttributes(attrs,
				R.styleable.ImageGallery);
		indexmode = tArray.getInt(R.styleable.ImageGallery_indexMode,
				UP_INDEX_LAYOUT_MODE);
		tArray.recycle();// 释放
		initview(context);
	}

	public void hiddenIndex() {
		upindexlayout.setVisibility(View.GONE);
		downindexlayout.setVisibility(View.GONE);
		showindex = false;
	}
	
	 

	private void initview(Context context) {
		this.context = context;
		images = new ArrayList<Bitmap>();
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.imagegallery, null);
		this.addView(view);
		gallerylayout = (LinearLayout) view
				.findViewById(R.id.imagegallery_gallerylayout);
		upindexlayout = (LinearLayout) view
				.findViewById(R.id.imagegallery_upindexlayout);
		downindexlayout = (LinearLayout) view
				.findViewById(R.id.imagegallery_downindexlayout);
	    gallery = new ImageGallery(context);
		gallery.setFadingEdgeLength(0);
		gallery.setSpacing(1);
		imageadapter = new ImageGalleryAdapter();
		gallery.setAdapter(imageadapter);
		gallerylayout.addView(gallery);
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if(indexmode==UP_INDEX_LAYOUT_MODE){
					for (int i = 0; i < upindexlayout.getChildCount(); i++) {
						ImageView img = (ImageView) upindexlayout.getChildAt(i);
						// img.setImageResource(R.drawable.index_normal);
						img.setBackgroundColor(Color.rgb(154, 154, 154));
					}
					// 为被选中的gallery的索引添加图片
					((ImageView) upindexlayout.getChildAt(arg2)).setBackgroundColor(Color.rgb(149, 201, 30));
				}else{
					for (int i = 0; i < downindexlayout.getChildCount(); i++) {
						ImageView img = (ImageView) downindexlayout.getChildAt(i);
						// img.setImageResource(R.drawable.index_normal);
						img.setImageResource(R.drawable.imgindex_normal);
					}
					// 为被选中的gallery的索引添加图片
					((ImageView) downindexlayout.getChildAt(arg2)).setImageResource(R.drawable.imgindex_selected);
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});
		
		gallery.setOnItemLongClickListener(new OnItemLongClickListener() {
 			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
 				if(lis!=null)
 					lis.onLongTouch(position);
 				return false;
			}
		});

		if (indexmode == DOWN_INDEX_LAYOUT_MODE) {
			upindexlayout.setVisibility(View.GONE);
			downindexlayout.setVisibility(View.VISIBLE);
		}

	}

	 

	// 遍历子view，测量宽高，或者在onLayout显示调用view的measure方法，否则不显示控件
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		measureChildren(widthMeasureSpec, heightMeasureSpec);
		if(indexmode==DOWN_INDEX_LAYOUT_MODE)
		  setMeasuredDimension(gallerylayout.getMeasuredWidth(), gallerylayout
		 		.getMeasuredHeight()+downindexlayout.getMeasuredHeight());
		else  setMeasuredDimension(gallerylayout.getMeasuredWidth(), gallerylayout
		 		.getMeasuredHeight());
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		for (int i = 0; i < this.getChildCount(); i++) {
			View view = getChildAt(i);
			// int measureHeight = view.getMeasuredHeight();
			// int measuredWidth = view.getMeasuredWidth();
			view.layout(0, 0, this.getWidth(), this.getHeight());

		}
	}
    
	private ImageView createUpIndexImage() {
		ImageView indeximage = new ImageView(context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, DensityUtil.dip2px(4));
		lp.weight = 1;
		if (images != null && images.size() > 1)
			lp.setMargins(1, 0, 0, 0);
		indeximage.setLayoutParams(lp);
		if (currentindex == images.size())
			indeximage.setBackgroundColor(Color.rgb(149, 201, 30));
		else
			indeximage.setBackgroundColor(Color.rgb(154, 154, 154));
		return indeximage;
	}
	private ImageView createDownIndexImage() {
		ImageView indeximage = new ImageView(context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
 		if (images != null && images.size() > 1)
			lp.setMargins(DensityUtil.dip2px(8), 0, 0, 0);
		indeximage.setLayoutParams(lp);
		if (currentindex == images.size())
			indeximage.setBackgroundResource(R.drawable.imgindex_selected);
		else
			indeximage.setBackgroundResource(R.drawable.imgindex_normal);
		return indeximage;
	}
	
	private LinearLayout getIndexLayout(){
		if(indexmode==DOWN_INDEX_LAYOUT_MODE)
			return downindexlayout;
		else return upindexlayout;
	}
	private ImageView createIndexImage(){
		if(indexmode==DOWN_INDEX_LAYOUT_MODE)
			return createDownIndexImage();
		else return createUpIndexImage();
	}
	public void addImage(Bitmap img) {
		if (img != null) {
			images.add(img);
			LinearLayout indexlay=getIndexLayout();
			if (showindex && images.size() > 1) {
 				if ((indexlay.getVisibility() == View.GONE))
 					indexlay.setVisibility(View.VISIBLE);
			} else
				indexlay.setVisibility(View.GONE);
			indexlay.addView(createIndexImage());
			imageadapter.notifyDataSetChanged();
		}
	}

	//替换图片
	public void replaceImage(Bitmap img, int index) {
		if (img != null) {
			images.remove(index);
			images.add(index, img);
			imageadapter.notifyDataSetChanged();
		}
	}
	/**
	 * 删除
	 * @param img
	 * @param index
	 */
	public void removeImage( int index) {
 		images.remove(index);
 		imageadapter.notifyDataSetChanged();
 	}
		
	public int getCurrentIndex(){
		return this.currentindex;
	}

	public int getImgCount(){
		if(this.images!=null)
			return images.size();
		return 0;
	}
	public class ImageGallery extends Gallery {
 		public ImageGallery(Context context) {
			super(context);
		}

		// 一次只能滑动一张图片注：一张图充满全屏
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			int kEvent;
			if (isScrollingLeft(e1, e2)) {
				kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
			} else {
				kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
			}
			onKeyDown(kEvent, null);
			return true;
		}

		private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {
			return e2.getX() > e1.getX();
		}

	}

	public class ImageGalleryAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return images == null ? 0 : images.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ImageView image = new ImageView(context);
			Bitmap bmap = images.get(position);
			int width = bmap.getWidth();
			int height = bmap.getHeight();
			if(userst==null){
				if (width < Gloable.getInstance().getScreenWeight()) {
					image.setScaleType(ScaleType.CENTER);
				} else {
					image.setScaleType(ScaleType.FIT_START);
				}
			}else image.setScaleType(userst);
			
			LayoutParams lp = new Gallery.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			if(imgwidth!=0) lp.width=imgwidth;
			else lp.width = Gloable.getInstance().getScreenWeight();
			if(imgheight!=0) lp.height=imgheight;
			else {
				double sc = width * 1.0 / Gloable.getInstance().getScreenWeight();
				if (sc < 1)
					sc = 1;
				lp.height = (int) (height / sc);
			}
 			image.setLayoutParams(lp);
			// Gallery.LayoutParams params=new
			// Gallery.LayoutParams(Gloable.getInstance().getScreenWeight(),LayoutParams.WRAP_CONTENT);
			// image.setLayoutParams(params);
			image.setImageBitmap(bmap);
	 
//			image.setOnTouchListener(new OnTouchListener() {
// 				@Override
//				public boolean onTouch(View view, MotionEvent me) {
// 					if(me.getAction()==MotionEvent.ACTION_DOWN){
// 						startTouchTime=System.nanoTime();
// 						LogUtil.logError(getClass(), "startTouchTime:"+startTouchTime);
// 					}else if(me.getAction()==MotionEvent.ACTION_UP){
//  						if(((System.nanoTime()-startTouchTime)/1000000>LONG_TOUCH_TIME&&lis!=null)){
//   							lis.onLongTouch(position);
//  						}else{
//  							
//  						}
// 					}
// 					
// 					 
// 					return true;
//				}
//			});
			return image;
		}

	}

	public void cleare() {
		upindexlayout.removeAllViews();
		downindexlayout.removeAllViews();
		releaseSource();
		if (this.images != null) {
			images.clear();
			imageadapter.notifyDataSetChanged();
		}
		currentindex = 1;

	}

	public void releaseSource() {
		if (images != null && images.size() > 0) {
			for (Bitmap bm : images) {
				if (bm != null && !bm.isRecycled()) {
					bm.recycle();
				}
			}
		}
	}
	
	public void setImgScaleType(ScaleType st){
		this.userst=st;
	}
	
	public void setImgWidthWidthHeight(int width,int height){
		this.imgwidth=width;
		this.imgheight=height;
	}
	
	
	public void setOnLongTouchListener(OnLongTouchListener lis){
		this.lis=lis;
	}
	
	public interface OnLongTouchListener{
		public void onLongTouch(int position);
	}
}
