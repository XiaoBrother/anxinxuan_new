package com.axinxuandroid.activity.view;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.anxinxuandroid.constant.AppConstant;
import com.axinxuandroid.activity.AddRecordActivity;
 import com.axinxuandroid.activity.R;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.service.UserService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.gauss.recorder.SpeexPlayer;
import com.gauss.recorder.SpeexRecorder;
import com.gauss.recorder.SpeexPlayer.PlayEndListener;
import com.ncpzs.util.DensityUtil;
import com.ncpzs.util.FileUtil;
import com.ncpzs.util.LogUtil;
import com.pocketdigi.utils.FLameUtils;

import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RecordAudioViewV2 extends ViewGroup {
	private Context context;
	private static final String TEMP_FILE_SUBFIX=".raw";
	private static final String SAVE_FILE_SUBFIX=".mp3";
 	private ImageButton deletebtn,playbtn,stopbtn;
	private TextView timetextmin,timetextsec;
	private static final int MAX_RECORD_TIME=60;//最大录制时间：1分钟
	private Timer timer;
  	protected String saveFilePath = null;
  	protected String tmpFilePath=null;
   	private int recordtime=0;//录制时长
   	private NcpzsHandler handler=Gloable.getInstance().getCurHandler();
	private AudioRecord mRecorder;
 	private MediaPlayer   player;
 	/**
	 * 用在多线程，同步变量。 线程为了提高效率，
	 * 将某成员变量(如A)拷贝了一份（如B），线程中对A的访问其实访问的是B。
	 * 只在某些动作时才进行A和B的同步。因此存在A和B不一致的情况。
	 * volatile就是用来避免这种情况的。volatile告诉jvm， 它所修饰的变量不保留拷贝，
	 * 直接访问主内存中的（也就是上面说的A) 
	 * @param context
	 */
	private volatile boolean isRecording=false;
	private volatile boolean isPlaying=false;
	private short[] mBuffer;
	private DeleteAudioListener listener;
	private OnCompletionListener playcompletelistener;
	public RecordAudioViewV2(Context context) {
		super(context);
		initview(context);
 	}
 	public RecordAudioViewV2(Context context,AttributeSet sets) {
		super(context,sets);
		initview(context);
 	}
    public void initview(Context context){
    	this.context=context;
  		LayoutInflater inflater = LayoutInflater.from(context);
	    final View  view=inflater.inflate(R.layout.recordaudioview, null);
		addView(view);
		init();
        playbtn=(ImageButton) view.findViewById(R.id.recordaudio_play);
        deletebtn=(ImageButton) view.findViewById(R.id.recordaudio_delete);
        stopbtn=(ImageButton) view.findViewById(R.id.recordaudio_stop);
        timetextmin=(TextView) view.findViewById(R.id.recordaudio_timetextmin);
        timetextsec=(TextView) view.findViewById(R.id.recordaudio_timetextsec);
         playbtn.setOnClickListener(new OnClickListener() {
         	 @Override
 			public void onClick(View v) {
         		 startPlay();
       		}
		});
        deletebtn.setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View v) {
 				removeAudio();
			}
		});
        stopbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
 				stopPlay();
			}
		});
    }
    /**
	 * 初始化Recorder
	 */
	public void init()
	{
		int bufferSize = AudioRecord.getMinBufferSize(16000, AudioFormat.CHANNEL_IN_MONO,
				AudioFormat.ENCODING_PCM_16BIT);
		mBuffer = new short[bufferSize];
		mRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC, 16000, AudioFormat.CHANNEL_IN_MONO,
				AudioFormat.ENCODING_PCM_16BIT, bufferSize);
		playcompletelistener=new OnCompletionListener() {
 			@Override
			public void onCompletion(MediaPlayer mp) {
   				if(isPlaying){
  					if(timer!=null){
 	 					timer.cancel();
  	 				}
 					handler.post(new Runnable() {
 						@Override
	 					public void run() {
	 						playbtn.setVisibility(View.VISIBLE);
	 			  			stopbtn.setVisibility(View.GONE);					
	 					}
	 				});
 	  				isPlaying = false;
 				}
			}
		};
	}
  //遍历子view，测量宽高，或者在onLayout显示调用view的measure方法，否则不显示控件
  	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
  		int widthSize = MeasureSpec.getSize(widthMeasureSpec);  
  	    int heightSize = MeasureSpec.getSize(heightMeasureSpec);  
   	    measureChildren(widthMeasureSpec, heightMeasureSpec);     
    	setMeasuredDimension(widthSize, DensityUtil.dip2px(135));  
  	 }
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
  		for (int i = 0; i < this.getChildCount(); i++) {
			View view = getChildAt(i);
   			view.layout(0, 0, this.getWidth(),this.getHeight());
 		}
  		 
	}
	public void removeAudio(){
		//FileUtil.deleteFile(saveFilePath);
		//FileUtil.deleteFile(tmpFilePath);
		if(listener!=null)
				listener.onDelete();
	}
	public void startPlay(){
		try{
 			if(!isPlaying){
	  			isPlaying=true;
	  			player=new MediaPlayer();
	  			player.setOnCompletionListener(playcompletelistener);
 	  			FileInputStream fis = new FileInputStream(new File(saveFilePath)); 
	  			player.setDataSource(fis.getFD());
	  			player.prepare();
	  			player.start();
 	  			handler.post(new Runnable() {
	 				@Override
					public void run() {
						playbtn.setVisibility(View.GONE);
			  			stopbtn.setVisibility(View.VISIBLE);					
					}
				});
	   			reckontime();
	   		 }
		}catch(Exception ex){
			ex.printStackTrace();
		}
		 
	}
    public void stopPlay(){
     	if (player != null&&isPlaying) {
     		player.release();
     		isPlaying = false;
    		if(timer!=null){
        		timer.cancel();
            	timer=null;
         	}
    		handler.post(new Runnable() {
					@Override
					public void run() {
						playbtn.setVisibility(View.VISIBLE);
			  			stopbtn.setVisibility(View.GONE);					
					}
			});
    		
         }
    }
	public void startRecord(){
		if(isPlaying) stopPlay();
		if(tmpFilePath==null){
			String time=AppConstant.MEDIA_DIR+ System.currentTimeMillis();
			tmpFilePath=time+TEMP_FILE_SUBFIX;
			saveFilePath=time+SAVE_FILE_SUBFIX;
 		}
 		if(isRecording){
    	 stopRecord();	
        } 
  		isRecording=true;
   		startBufferedWrite(new File(tmpFilePath));
 		reckontime();
 	}
	
	public void stopRecord(){
 		mRecorder.stop();
		isRecording=false;
		if(tmpFilePath!=null&&FileUtil.hasFile(tmpFilePath)){
			toMp3();
		}
		timer.cancel();
 	}
	
	public String getSavePath(){
		return this.saveFilePath;
	}

	public int getRecordTime(){
		return recordtime;
	}
	public void setSavePath(String path){
		this.saveFilePath=path;
 	}
	public void setRecordTime(int time){
		recordtime=time;
		setTimeString(time);
	}
	 
	
	private int schedulecount=0;
	private void reckontime(){
		schedulecount=-1;
		timer = new Timer();
   		timer.schedule(new TimerTask() {
 			@Override
			public void run() {
 				schedulecount++;
 				setTimeString(schedulecount);
  				if(isPlaying&&schedulecount>=recordtime){
  					timer.cancel();
  					timer=null;
 				}else if(isRecording){
  					recordtime=schedulecount;
 				}
			}
		}, 0, 1000);
 	}
	
	public void setDeleteAudioListener(DeleteAudioListener lis){
		this.listener=lis;
	}
	public interface DeleteAudioListener{
		public void onDelete();
	}
	private void setTimeString(final int time){
		
		handler.post(new Runnable() {
 			@Override
			public void run() {
 			// 设置分钟
 				long min = time / 60;
 				String timestr = "";
 				if (min < 10)
 					timestr = "0" + min + ":";
 				else
 					timestr = min + ":";
 				timetextmin.setText(timestr);
 				// 设置秒数
 				long sec = time % 60;
 				if (sec < 10)
 					timestr = "0" + sec;
 				else
 					timestr = sec + "";
 				timetextsec.setText(timestr);
				
			}
		});
 	}
	
	
	/**
	 * 写入到文件
	 * @param file
	 */
	private void startBufferedWrite(final File file) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				DataOutputStream output = null;
				try {
					output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
					mRecorder.startRecording();
					while (isRecording) {
						int readSize = mRecorder.read(mBuffer, 0, mBuffer.length);
						for (int i = 0; i < readSize; i++) {
							output.writeShort(mBuffer[i]);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					//Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
				} finally {
					if (output != null) {
						try {
							output.flush();
 						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							try {
								output.close();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}).start();
	}
	
	public void toMp3(){
		new Thread()
		{
			public void run()
			{
				if(tmpFilePath!=null){
 					FLameUtils lameUtils=new FLameUtils(1, 16000, 96);
					lameUtils.raw2mp3(tmpFilePath, saveFilePath);
				}
				
			}
		}.start();
	}
}
