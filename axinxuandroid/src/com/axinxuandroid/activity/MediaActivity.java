package com.axinxuandroid.activity;


import java.io.File;

import com.anxinxuandroid.constant.AppConstant;
import com.axinxuandroid.sys.gloable.Session;
import com.axinxuandroid.sys.gloable.SessionAttribute;
import com.gauss.recorder.SpeexPlayer;
import com.gauss.recorder.SpeexRecorder;
import com.ncpzs.util.FileUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MediaActivity extends NcpZsActivity implements OnClickListener{
	public static final int STOPPED = 0;
	public static final int RECORDING = 1;
  	SpeexRecorder recorderInstance = null;
	LinearLayout layout;
	TextView take_media_text;
	Button startButton = null;
	Button stopButton = null;
	Button playButton = null;
	Button exitButon = null;
	TextView textView = null;
	int status = STOPPED;

	String fileName = null;
	SpeexPlayer splayer = null;

	public void onClick(View v) {
 		File dirFile = new File(AppConstant.MEDIA_DIR);
		if (!dirFile.exists()) {
  			FileUtil.createDirectory(AppConstant.MEDIA_DIR);
		}
		if (v == startButton) {
			take_media_text.setText("开始录音了！");
 			fileName = AppConstant.MEDIA_DIR+System.currentTimeMillis()+".spx";
			if (recorderInstance == null) {
 				recorderInstance = new SpeexRecorder(fileName);
				Thread th = new Thread(recorderInstance);
				th.start();
			}
			recorderInstance.setRecording(true);
		} else if (v == stopButton) {
			take_media_text.setText("停止了");
			recorderInstance.setRecording(false);
		} else if (v == playButton) {
 			if(fileName!=null&&!fileName.equals("")){
				take_media_text.setText("开始播放");
				splayer = new SpeexPlayer(fileName);
				splayer.startPlay();
			}
  		} else if (v == exitButon) {
			recorderInstance.setRecording(false);
			 Session.getInstance().setAttribute(SessionAttribute.SESSION_TAKE_MEDIA, fileName);
			 Intent aintent = new Intent(MediaActivity.this, AddRecordActivity.class);
  			 setResult(RESULT_OK,aintent);
  			MediaActivity.this.finish();
		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.takemedia);
		startButton = new Button(this);
		stopButton = new Button(this);
		exitButon = new Button(this);
		playButton = new Button(this);
		textView = new TextView(this);
		take_media_text=(TextView) findViewById(R.id.take_media_text);
		startButton.setText("开始录音");
		stopButton.setText("停止");
		playButton.setText("播放");
		exitButon.setText("退出");

		startButton.setOnClickListener(this);
		playButton.setOnClickListener(this);
		stopButton.setOnClickListener(this);
		exitButon.setOnClickListener(this);
		layout = (LinearLayout)findViewById(R.id.take_media_anniu);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.addView(textView);
		layout.addView(startButton);
		layout.addView(stopButton);
		layout.addView(playButton);
		layout.addView(exitButon);
	}
}
