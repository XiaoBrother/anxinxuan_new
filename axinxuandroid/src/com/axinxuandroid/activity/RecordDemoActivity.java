package com.axinxuandroid.activity;

import com.ncpzs.util.RecordThread;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class RecordDemoActivity extends Activity implements SurfaceHolder.Callback {
	private SurfaceView surfaceview;// 视频预览控件
	private LinearLayout lay; // 预揽控件的
	private Button start; // 
	private Button stop; // 
	private SurfaceHolder surfaceHolder; // 和surfaceView相关的
	private RecordThread thread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.basevideo_main);
		// 初始化控件
		init();
	}

	/**
	 * 初始化控件以及回调
	 */
	private void init() {
		surfaceview = (SurfaceView) this.findViewById(R.id.surfaceview);
		lay = (LinearLayout) this.findViewById(R.id.lay);
		start = (Button) this.findViewById(R.id.start);
		stop = (Button) this.findViewById(R.id.stop);
//		lay.setVisibility(LinearLayout.INVISIBLE);
		SurfaceHolder holder = this.surfaceview.getHolder();// 取得holder
		holder.addCallback(this); // holder加入回调接口
		// 设置setType
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (thread==null) {
					thread = new RecordThread(1*60*1000, surfaceview, surfaceHolder);
					thread.start();
				}else {
					Toast.makeText(RecordDemoActivity.this, "正在录制中……", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		stop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (thread!=null) {
					thread.stopRecord();
					thread=null;
				}else {
					Toast.makeText(RecordDemoActivity.this, "视频录制还没开始", Toast.LENGTH_SHORT).show();
				}
			}
		});

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub
		// 将holder，这个holder为开始在oncreat里面取得的holder，将它赋给surfaceHolder 
		Log.i("SurfaceHolder", "surfaceChanged()");
        surfaceHolder = holder; 
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Log.i("SurfaceHolder", Thread.currentThread().getName());
		// 将holder，这个holder为开始在oncreat里面取得的holder，将它赋给surfaceHolder
		surfaceHolder = holder;
		// 录像线程，当然也可以在别的地方启动，但是一定要在onCreate方法执行完成以及surfaceHolder被赋值以后启动
//		thread = new RecordThread(1*60*1000, surfaceview, surfaceHolder);
//		thread.start();

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		Log.i("SurfaceHolder", "surfaceDestroyed()");
		// surfaceDestroyed的时候同时对象设置为null
		surfaceview = null;
		surfaceHolder = null;
		/*释放资源 mediarecorder mCamera 否则会后果很严重*/
		if (thread!=null) {
			thread.stopRecord();
			thread=null;
		}
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i("RecordDemoActivity", "onResume()");
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i("RecordDemoActivity", "onPause()");
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("RecordDemoActivity", "onDestroy()");
	}
	

}
