package com.axinxuandroid.sys.gloable;

import java.util.HashMap;
import java.util.Map;

import com.axinxuandroid.activity.R;
import com.ncpzs.util.BitmapUtils;
 
import android.graphics.Bitmap;

public class GloableBitmap {
    public static final String GB_IMG_LOAD_IMG="gb_img_load_img";
    public static final String GB_IMG_LOAD_FAILD_IMG="gb_img_load_falid_img";
    public static final String GB_BATCH_MANAGE_ITEM_BG_NORMAL_IMG="gb_batch_manage_item_bg_normal_img";
    public static final String GB_BATCH_MANAGE_ITEM_BG_CLICK_IMG="gb_batch_manage_item_bg_click_img";
	private static   GloableBitmap gb;
	private Map<String,Bitmap> gb_imgs;
	private Map<String,Integer> imgs_rel;
	private GloableBitmap(){
		gb_imgs=new HashMap<String, Bitmap>();
		imgs_rel=new HashMap<String, Integer>();
		imgs_rel.put(GB_IMG_LOAD_IMG,R.drawable.default_load);
		imgs_rel.put(GB_IMG_LOAD_FAILD_IMG,R.drawable.loadfaild);
		imgs_rel.put(GB_BATCH_MANAGE_ITEM_BG_NORMAL_IMG,R.drawable.batchlist_normalbg);
		imgs_rel.put(GB_BATCH_MANAGE_ITEM_BG_CLICK_IMG,R.drawable.batchlist_clickbg);
	}
	public static GloableBitmap getInstance(){
		if(gb==null)
			gb =new GloableBitmap();
		return gb;
	}
	
	
	public Bitmap getImgBitmap(String key){
		if(!gb_imgs.containsKey(key)){
			//≥¢ ‘º”‘ÿ
			loadImg(key);
		}
	     return gb_imgs.get(key);
 	}
	
	private void loadImg(String key){
		if(imgs_rel.containsKey(key)){
			Bitmap btm=BitmapUtils.loadResourceImg(imgs_rel.get(key));
			if(btm!=null)
				gb_imgs.put(key, btm);
		}
		
	}
}
