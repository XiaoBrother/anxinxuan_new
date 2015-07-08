package com.axinxuandroid.activity.view;

import java.util.ArrayList;
import java.util.List;

import com.axinxuandroid.activity.AddRecordActivity;
import com.axinxuandroid.activity.DeleteResourceActivity;
import com.axinxuandroid.activity.R;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.BitmapUtils;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.ImageTools;
import com.ncpzs.util.LogUtil;
 
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.provider.MediaStore.Video.Thumbnails;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 资源展示列表
 * @author hubobo
 *
 */
public class ResourcesView extends ViewGroup  {
	
	public static  final int RESROUCETYPE_OF_IMAGE=1;
	public static  final int RESROUCETYPE_OF_VIDEO=2;
	public static  final int RESROUCETYPE_OF_AUDIO=3;
	private static final int LINE_IMAGE_COUNT=4;//每行显示图片个数
 	private static final int MARGIN_LEFT=5;
	private static final int MARGIN_TOP=5;
 	private List<Resource> resources;
	private int screenWidth;
 	private Context context;
 	private int width;
 	private int height;
  	private ResourceClickListener listener;
  	private int opcount=0;//操作次数
  	private LayoutInflater inflater;
	public ResourcesView(Context context) {
		super(context);
		initview(context);
   	}
	public ResourcesView(Context context,AttributeSet sets) {
		super(context,sets);
		initview(context);
 	}	
	
	private void initview(Context context){
		this.context=context;
		screenWidth=Gloable.getInstance().getScreenWeight();
		width=(screenWidth-(LINE_IMAGE_COUNT+1)*MARGIN_LEFT)/LINE_IMAGE_COUNT;
		//LogUtil.logInfo(getClass(), width+"");
		height=width;
		inflater=LayoutInflater.from(context);
	}
	/**
	 * 添加资源
	 * @param path
	 * @param type
	 */
	public void addResource(String path,int type,String thumbimg,String info){
		if(path==null) return ;
		Resource rec=new Resource(path,type,thumbimg,info);
 		if(resources==null)
			resources=new ArrayList<Resource>();
 		if(type==RESROUCETYPE_OF_AUDIO||type==RESROUCETYPE_OF_VIDEO){
 			//删除以前的
 			deleteResourceByType(type);
 		}
 		resources.add(rec);
 		showResouce(rec);
 		opcount++;
	}
	 /**
	  * 删除资源
	  * @param path
	  */
	public void deleteReource(String path){
		Resource rec=this.findByPath(path);
 		if(rec!=null){
			this.removeView(rec.rootview);
			this.resources.remove(rec);
		}
 		opcount++;
	}
	/**
	 * 删除指定类型的所有资源
	 * @param type
	 */
	public void deleteResourceByType(int type){
		if(this.resources!=null&&this.resources.size()>0){
			List<Resource> remove=new ArrayList<Resource>();
			for(Resource res:this.resources){
				if(res.type==type){
				  remove.add(res);
				  if(res.rootview!=null)
				    this.removeView(res.rootview);
				}
			}
			this.resources.removeAll(remove);
		}
		opcount++;
	}
	/**
	 * 清空
	 */
	public void clearResource(){
		if(this.resources!=null&&this.resources.size()>0){
 			for(Resource res:this.resources){
 				if(res.rootview!=null)
				    this.removeView(res.rootview);
			 }
 		     this.resources.clear();
	     }
 		opcount=0;
	}
	/**
	 * 返回指定类型的资源
	 * @param type
	 * @return
	 */
	public List<Resource> getByType(int type){
		if(this.resources==null||this.resources.size()<1) return null;
		List<Resource> rdata=new ArrayList<Resource>();
		for(Resource rec:this.resources)
			if(rec.type==type)
				rdata.add(rec);
		return rdata;
	}
	/**
	 * 返回是否含有资源
	 * @param type
	 * @return
	 */
	public boolean hasResources(){
		if(this.resources==null||this.resources.size()<1) return false;
 		return true;
	}
	/**
	 * 根据资源路径查找
	 * @param path
	 * @return
	 */
	private Resource findByPath(String path){
		if(path==null||this.resources==null||this.resources.size()<1) return null;
	    for(Resource rec:this.resources)
	    	if(rec.path.equals(path))
	    		return rec;
	    return null;
	}
	
	 
	/**
	 * 显示资源
	 * @param rec
	 */
	private void showResouce(final Resource rec){
		View resview=inflater.inflate(R.layout.resource_item, null);
		resview.setLayoutParams(new ViewGroup.LayoutParams(width,height));
		ImageView iview=(ImageView) resview.findViewById(R.id.resource_showimg);
		Bitmap btm=null;
		if(rec.type==RESROUCETYPE_OF_IMAGE){
			btm=BitmapUtils.getImageBitmap(rec.path, width,height);
		}else if(rec.type==RESROUCETYPE_OF_VIDEO){
			if(rec.thumbimg==null){
				LogUtil.logInfo(getClass(), "show resource:"+rec.path);
				rec.thumbimg=ImageTools.getLocalVideoImage(rec.path);
 			}
			if(rec.thumbimg!=null)
			 btm=BitmapUtils.getImageBitmap(rec.thumbimg, width,height);
			 //给视频缩略图加水印
// 	         if(btm!=null){
//	        	 Bitmap waterBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.videotag);
//	        	 btm=ImageTools.imageWater(btm, waterBitmap);
// 	         }
		}
 		if(btm!=null){
			//iview.setBackgroundColor(Color.RED);
   			iview.setImageBitmap(btm);
 			iview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                	if(listener!=null){
                		listener.click(rec);
                	}
                  }
            });
 			ImageView delview=(ImageView) resview.findViewById(R.id.resource_delete);
    			delview.setOnClickListener(new OnClickListener() {
 				@Override
				public void onClick(View v) {
 					deleteReource(rec.path);
				}
			});
  			rec.rootview=resview;
			this.addView(resview);
 			//this.requestLayout();//调整布局
		}
 	}
	//遍历子view，测量宽高，或者在onLayout显示调用view的measure方法，否则不显示控件
  	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
  		int widthSize = MeasureSpec.getSize(widthMeasureSpec);  
  	    int heightSize = MeasureSpec.getSize(heightMeasureSpec);  
  	    if(resources!=null){
  	    	heightSize=(resources.size()/LINE_IMAGE_COUNT+1)*(MARGIN_TOP+height);
  	    }
  	   //LogUtil.logInfo(getClass(), "heightSize:"+heightSize+"");
   	    measureChildren(widthMeasureSpec, heightMeasureSpec);     
   	    setMeasuredDimension(widthSize, heightSize);  
 	 }
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		 int mTotalHeight = MARGIN_TOP;  
		 int mTotalWidth=MARGIN_LEFT;
		 int childCount = getChildCount();
		 //LogUtil.logInfo(getClass(), this.screenWidth+"");
	 	 for(int i = 0; i < childCount; i ++){
	 		 //LogUtil.logInfo(getClass(), mTotalWidth+","+mTotalHeight);
	 	        View view = getChildAt(i);
	 	        if((mTotalWidth+width)>this.screenWidth){
	 	        	 mTotalHeight += height+MARGIN_TOP;//换行
	 	        	 mTotalWidth=MARGIN_LEFT;
	 	        }
 	            view.layout(mTotalWidth,mTotalHeight, mTotalWidth+width, mTotalHeight+height); 
 	            mTotalWidth+=width+MARGIN_LEFT;
 	           //LogUtil.logInfo(getClass(), mTotalWidth+","+mTotalHeight);
	     }
  	}
	
	
	public int getOpcount() {
		return opcount;
	}
	/**
	 * 设置点击事件监听
	 * @param listener
	 */
	public void setOnResourceClickListener(ResourceClickListener listener){
		this.listener=listener;
	}
	/**
	 * 点击事件监听
	 * @author Administrator
	 *
	 */
	public interface ResourceClickListener{
		public void click(Resource res);
	}
	/**
	 * 资源定义
	 * @author hubobo
	 *
	 */
	public class Resource {
 		public String path;//资源地址
		public int type;
		public String thumbimg;//缩略图
		public View rootview;
		public String info;
		public Resource(String path,int type,String thumbimg,String info){
			this.path=path;
			this.type=type;
			this.info=info;
			this.thumbimg=thumbimg;
		}
 	}
	
	 
  	
}
