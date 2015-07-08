package com.axinxuandroid.activity.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.axinxuandroid.activity.Appstart;
import com.axinxuandroid.activity.CleanActivity;
import com.axinxuandroid.activity.DraftActivity;
import com.axinxuandroid.activity.ProustActivity;
import com.axinxuandroid.activity.R;
import com.axinxuandroid.activity.SystemUpdateActivity;
import com.axinxuandroid.activity.TemplateSynchroniseActivity;
import com.axinxuandroid.activity.TimelineActivity;
import com.axinxuandroid.activity.UserPageActivity;
import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.net.LoadVilleageBatchThread;
import com.axinxuandroid.activity.net.LoadRecordThread;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.data.Batch;
import com.axinxuandroid.data.PhotoItem;
import com.axinxuandroid.data.Record;
import com.axinxuandroid.data.User;
import com.axinxuandroid.data.Villeage;
import com.axinxuandroid.oauth.SinaOAuthListener;
import com.axinxuandroid.service.BatchLabelService;
import com.axinxuandroid.service.BatchService;
import com.axinxuandroid.service.SharedPreferenceService;
import com.axinxuandroid.service.UserService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.axinxuandroid.sys.gloable.Session;
import com.axinxuandroid.sys.gloable.SessionAttribute;
import com.ncpzs.util.BitmapUtils;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.DensityUtil;
import com.ncpzs.util.LogUtil;
 
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
 
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
 import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
 
public class PhotoBottomView extends ViewGroup {
	private Context context;
	private Button okbtn; 
 	private LinearLayout mainlay;
    private List<BottomItem> items;
    private ItemClickListener listener;
    private LayoutInflater inflat;
 	public PhotoBottomView(Context context) {
		super(context);
		initview(context);
 	}
 	public PhotoBottomView(Context context,AttributeSet sets) {
		super(context,sets);
		initview(context);
 	}

 
 	private void initview(final Context context){
 		this.context=context;
 		inflat=LayoutInflater.from(context);
  		LayoutInflater inflater = LayoutInflater.from(context);
		View  view=inflater.inflate(R.layout.photobottomview, null);
        addView(view);
        okbtn=(Button) view.findViewById(R.id.photobottom_btn_sure);
        mainlay=(LinearLayout) view.findViewById(R.id.photobottom_mainlay);
        items=new ArrayList<BottomItem>();
 	}
 	
 	public void addBottomItem(PhotoItem item){
 		if(item!=null){
 			BottomItem bitm=new BottomItem();
  			bitm.item=item;
  			if(!items.contains(bitm)){
  				items.add(bitm);
  	 			bitm.img=createImg(bitm);
  	 			LinearLayout.LayoutParams param=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
  	 			param.leftMargin=DensityUtil.dip2px(5);
   	 			mainlay.addView(bitm.img,param);
  	 			okbtn.setText("确定("+items.size()+"/9)");
  			}
   		}
 	}
 	public void removeBottomItem(PhotoItem item){
 		if(item!=null){
 			BottomItem bitm=new BottomItem();
 			bitm.item=item;
 			int index=items.indexOf(bitm);
 			if(index>=0){
 				BottomItem it=items.get(index);
 				mainlay.removeView(it.img);
 				items.remove(index);
 				okbtn.setText("确定("+items.size()+"/9)");
 			}
 		}
 	}
 	public void clearAll(){
 		if(items!=null){
 			for(BottomItem it:items)
 				mainlay.removeView(it.img);
 			items.clear();
 		}
 		okbtn.setText("确定(0/9)");
 	}
 	public void setOnOkBtnClickListener(OnClickListener lis){
 		if(lis!=null)
 		  okbtn.setOnClickListener(lis);
 	}
   	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
  		int widthSize = MeasureSpec.getSize(widthMeasureSpec);  
  	    int heightSize = MeasureSpec.getSize(heightMeasureSpec);  
   	    measureChildren(widthMeasureSpec, heightMeasureSpec);     
   	    setMeasuredDimension(widthSize, heightSize);  
 	 }
  	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
  			int childCount = getChildCount();//一个view是通过xml实例化来的，另一个是自己添加的BatchView，这里的个数是2
 	 	    for(int i = 0; i < childCount; i ++){
 	 	         View view = getChildAt(i);
  	            view.layout(0,0, this.getWidth(), this.getHeight()); 
   	 	    }
  	}
  	
  	private View createImg(final BottomItem item){
  		if(item!=null&&item.item!=null){
  			Bitmap  bitmap = MediaStore.Images.Thumbnails.getThumbnail(
  	  				context.getContentResolver(),Long.parseLong(item.item.getPhotoID()),
  					Thumbnails.MICRO_KIND, null);
  			if(bitmap!=null){
  				final View view=inflat.inflate(R.layout.photobottom_yuanjiao, null);
  			    ImageView img = (ImageView) view.findViewById(R.id.yuanjiao_showimg);
  			    img.setImageBitmap(bitmap);
  				view.setOnClickListener(new OnClickListener() {
 					@Override
					public void onClick(View v) {
 						mainlay.removeView(view);
 						items.remove(item);
 						if(listener!=null)
 							listener.onClickItem(item.item);
					}
				});
   				return view;
  			}
  		}
  		return null;
  	}
  	
  	public void setItemClickListener(ItemClickListener lis){
  		this.listener=lis;
  	}
  	
	public interface ItemClickListener{
		public void onClickItem(PhotoItem item);
	}
 	private class BottomItem{
 		public PhotoItem item;
 		public View img;
		@Override
		public boolean equals(Object o) {
			if(o==null) return false;
			BottomItem tmp=(BottomItem) o;
			if(tmp.item!=null&&tmp.item.getPhotoID()!=null&&tmp.item.getPhotoID().equals(item.getPhotoID()))
				return true;
 		    return false;
		}
		@Override
		public int hashCode() {
 			return 1;
		}
   	}
  
}
