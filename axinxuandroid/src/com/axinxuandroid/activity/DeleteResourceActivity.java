package com.axinxuandroid.activity;
 

import com.axinxuandroid.activity.view.CommonTopView;
import com.axinxuandroid.activity.view.ResourcesView;
import com.axinxuandroid.activity.view.VideoView;
import com.axinxuandroid.activity.view.VideoView.VideoPlayFinishListener;
import com.axinxuandroid.service.SharedPreferenceService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.BitmapUtils;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.FileUtil;
 
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
 
@SuppressLint("NewApi")
public class DeleteResourceActivity extends NcpZsActivity{
	private String path;
	private String thumbimg;
	private int type;
	private ImageView img_show;
	private CommonTopView topview;
	private VideoView videoview;
	private Button videoback;
	private Button videodel;
	private ImageView videoimgshow;
	private RelativeLayout imglayout;
	private RelativeLayout videolayout;
	private RelativeLayout audiolayout;
 	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.deleteresource);
 		img_show=(ImageView) findViewById(R.id.deleteres_imgshow);
		path= this.getIntent().getStringExtra("path");
		thumbimg= this.getIntent().getStringExtra("thumbimg");
		type= this.getIntent().getIntExtra("type",-1);
		topview=(CommonTopView) this.findViewById(R.id.deleteres_topview);
		videoview=(VideoView) this.findViewById(R.id.deleteres_videoview);
		imglayout=(RelativeLayout) this.findViewById(R.id.deleteres_imglayout);
		videolayout=(RelativeLayout) this.findViewById(R.id.deleteres_videolayout);
		audiolayout=(RelativeLayout) this.findViewById(R.id.deleteres_audiolayout);
		videoimgshow=(ImageView) findViewById(R.id.deleteres_videoshowimg);
		videoback=(Button)this.findViewById(R.id.deleteres_backvideo);
		videodel=(Button)this.findViewById(R.id.deleteres_deletevideo);
		topview.setLeftClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				reback();
			}
		});
		videoback.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				reback();
			}
		});
		topview.setRightClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				delete();
			}
		});
		videodel.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				//删除资源
 			   // FileUtil.deleteFile(path);
  				delete();
			}
		});
		if(type==ResourcesView.RESROUCETYPE_OF_IMAGE){
			showImg();
		}else if(type==ResourcesView.RESROUCETYPE_OF_AUDIO){
			showAudio();
		}else if(type==ResourcesView.RESROUCETYPE_OF_VIDEO){
			showVideo();
		}
   }
	 /**
	  * 展示图片
	 */
 	public void showImg(){
 		videolayout.setVisibility(View.GONE);
 		audiolayout.setVisibility(View.GONE);
// 		Matrix mMatrix = new Matrix();
//		mMatrix.setScale(3.0f, 3.0f);
// 		Bitmap btm=BitmapUtils.getImageBitmap(path, 500*300);
//		Bitmap bm = Bitmap.createBitmap(btm, 0, 0, btm.getWidth(), 
//				btm.getHeight(), mMatrix, true);
 		Bitmap bm=BitmapFactory.decodeFile(path);
		img_show.setImageBitmap(bm);
 	}
 	/**
	  * 展示视频
	 */
	public void showVideo(){
		
		imglayout.setVisibility(View.GONE);
		audiolayout.setVisibility(View.GONE);
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //横屏
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();  
		retriever.setDataSource(path);
		String height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);  
		String width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);  
		if(height!=null&&width!=null){
 			RelativeLayout.LayoutParams videolay=(LayoutParams) videoview.getLayoutParams();
 			int screenw=Gloable.getInstance().getScreenWeight();
 			double scale=screenw*1.0/Integer.parseInt(width);
 			videolay.width=screenw;
 			videolay.height=(int) (Integer.parseInt(height)*scale);
			videoview.setLayoutParams(videolay);
			RelativeLayout.LayoutParams vimglay=(LayoutParams) videoimgshow.getLayoutParams();
			vimglay.width=screenw;
			vimglay.height=(int) (Integer.parseInt(height)*scale);
 			videoimgshow.setLayoutParams(vimglay);
 		}
		
		if(thumbimg!=null)
		  videoimgshow.setImageBitmap(BitmapUtils.getImageBitmap(thumbimg));
		else videoimgshow.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.videobg));
		videoimgshow.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				videoimgshow.setVisibility(View.GONE);
 				videoview.startPlay(path);
 			}
		});
		videoview.setVideoPlayFinishListener(new VideoPlayFinishListener() {
 			@Override
			public void onPlayFinish() {
 				videoimgshow.setVisibility(View.VISIBLE);
			}
		});
	}
	
	
 
	
	/**
	  * 展示音频
	 */
	public void showAudio(){
		imglayout.setVisibility(View.GONE);
		videolayout.setVisibility(View.GONE);
 	}
	public void reback() {  
		if(videoview.isPlaying())
			videoview.stopPlay();
 		finish();
	}
	public void delete() {   
		if(videoview.isPlaying())
			videoview.stopPlay();
 		//删除
		Intent inte=new Intent(DeleteResourceActivity.this,AddRecordActivity.class);
		inte.putExtra("path", path);
		setResult(RESULT_OK,inte);
		finish();
	}
	
	  
	
}