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
	private SurfaceView surfaceview;// ��ƵԤ���ؼ�
	private LinearLayout lay; // Ԥ���ؼ���
	private Button start; // 
	private Button stop; // 
	private SurfaceHolder surfaceHolder; // ��surfaceView��ص�
	private RecordThread thread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.basevideo_main);
		// ��ʼ���ؼ�
		init();
	}

	/**
	 * ��ʼ���ؼ��Լ��ص�
	 */
	private void init() {
		surfaceview = (SurfaceView) this.findViewById(R.id.surfaceview);
		lay = (LinearLayout) this.findViewById(R.id.lay);
		start = (Button) this.findViewById(R.id.start);
		stop = (Button) this.findViewById(R.id.stop);
//		lay.setVisibility(LinearLayout.INVISIBLE);
		SurfaceHolder holder = this.surfaceview.getHolder();// ȡ��holder
		holder.addCallback(this); // holder����ص��ӿ�
		// ����setType
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (thread==null) {
					thread = new RecordThread(1*60*1000, surfaceview, surfaceHolder);
					thread.start();
				}else {
					Toast.makeText(RecordDemoActivity.this, "����¼���С���", Toast.LENGTH_SHORT).show();
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
					Toast.makeText(RecordDemoActivity.this, "��Ƶ¼�ƻ�û��ʼ", Toast.LENGTH_SHORT).show();
				}
			}
		});

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub
		// ��holder�����holderΪ��ʼ��oncreat����ȡ�õ�holder����������surfaceHolder 
		Log.i("SurfaceHolder", "surfaceChanged()");
        surfaceHolder = holder; 
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Log.i("SurfaceHolder", Thread.currentThread().getName());
		// ��holder�����holderΪ��ʼ��oncreat����ȡ�õ�holder����������surfaceHolder
		surfaceHolder = holder;
		// ¼���̣߳���ȻҲ�����ڱ�ĵط�����������һ��Ҫ��onCreate����ִ������Լ�surfaceHolder����ֵ�Ժ�����
//		thread = new RecordThread(1*60*1000, surfaceview, surfaceHolder);
//		thread.start();

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		Log.i("SurfaceHolder", "surfaceDestroyed()");
		// surfaceDestroyed��ʱ��ͬʱ��������Ϊnull
		surfaceview = null;
		surfaceHolder = null;
		/*�ͷ���Դ mediarecorder mCamera �������������*/
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
