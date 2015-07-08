package com.axinxuandroid.activity.view;

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

import android.content.Context;
import android.content.Intent;
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

public class RecordAudioView extends ViewGroup {
	private Context context;
 	private ImageButton deletebtn,playbtn,stopbtn;
	private TextView timetextmin,timetextsec;
	private static final int MAX_RECORD_TIME=60;//���¼��ʱ�䣺1����
	private Timer timer;
  	protected String saveFilePath = null;
	protected SpeexPlayer splayer = null;
  	protected SpeexRecorder recorderInstance = null;
  	private int recordtime=0;//¼��ʱ��
   	private NcpzsHandler handler=Gloable.getInstance().getCurHandler();
 	/**
	 * ���ڶ��̣߳�ͬ�������� �߳�Ϊ�����Ч�ʣ�
	 * ��ĳ��Ա����(��A)������һ�ݣ���B�����߳��ж�A�ķ�����ʵ���ʵ���B��
	 * ֻ��ĳЩ����ʱ�Ž���A��B��ͬ������˴���A��B��һ�µ������
	 * volatile��������������������ġ�volatile����jvm�� �������εı���������������
	 * ֱ�ӷ������ڴ��еģ�Ҳ��������˵��A) 
	 * @param context
	 */
	private volatile boolean isRecording;
	private volatile boolean isPlaying=false;
	
	private DeleteAudioListener listener;
	public RecordAudioView(Context context) {
		super(context);
		initview(context);
 	}
 	public RecordAudioView(Context context,AttributeSet sets) {
		super(context,sets);
		initview(context);
 	}
    public void initview(Context context){
    	this.context=context;
  		LayoutInflater inflater = LayoutInflater.from(context);
	    final View  view=inflater.inflate(R.layout.recordaudioview, null);
		addView(view);
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
 				FileUtil.deleteFile(saveFilePath);
 				if(listener!=null)
 					listener.onDelete();
			}
		});
        stopbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
 				stopPlay();
			}
		});
    }
  //������view��������ߣ�������onLayout��ʾ����view��measure������������ʾ�ؼ�
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
	
	public void startPlay(){
		 if(!isPlaying){
  			isPlaying=true;
  			splayer.startPlay();
  			handler.post(new Runnable() {
 				@Override
				public void run() {
					playbtn.setVisibility(View.GONE);
		  			stopbtn.setVisibility(View.VISIBLE);					
				}
			});
   			reckontime();
   		 }
	}
    public void stopPlay(){
    	if(timer!=null){
    		timer.cancel();
        	timer=null;
    	}
     	splayer.stopPlay();
      }
	public void startRecord(){
		if(isPlaying) stopPlay();
		if(saveFilePath==null){
			saveFilePath = AppConstant.MEDIA_DIR+ System.currentTimeMillis() + ".spx";
			initSpeex(saveFilePath);
		}
   		Thread th = new Thread(recorderInstance);
 		th.start();
		recorderInstance.setRecording(true);
  	    
		if(isRecording){
    	 stopRecord();	
        } 
		reckontime();
		isRecording=true;
 	}
	
	public void stopRecord(){
		recorderInstance.setRecording(false);
		timer.cancel();
 		isRecording = false;
	}
	
	public String getSavePath(){
		return this.saveFilePath;
	}

	public int getRecordTime(){
		return recordtime;
	}
	public void setSavePath(String path){
		this.saveFilePath=path;
		initSpeex(path);
	}
	public void setRecordTime(int time){
		recordtime=time;
		setTimeString(time);
	}
	private void initSpeex(String path){
		splayer = new SpeexPlayer(path);
        recorderInstance = new SpeexRecorder(path);
        splayer.setPlayEndListener(new PlayEndListener() {
 			@Override
			public void onEnd() {
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
		});
	}
	
	private int schedulecount=0;
	private void reckontime(){
		schedulecount=0;
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
 			// ���÷���
 				long min = time / 60;
 				String timestr = "";
 				if (min < 10)
 					timestr = "0" + min + ":";
 				else
 					timestr = min + ":";
 				timetextmin.setText(timestr);
 				// ��������
 				long sec = time % 60;
 				if (sec < 10)
 					timestr = "0" + sec;
 				else
 					timestr = sec + "";
 				timetextsec.setText(timestr);
				
			}
		});
 	}
}
