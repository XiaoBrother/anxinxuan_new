package com.axinxuandroid.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
 
import com.axinxuandroid.activity.view.ProgressView;
 
 
 
import com.yixia.camera.demo.log.Logger;
import com.yixia.camera.demo.utils.ConvertToUtils;
import com.yixia.videoeditor.adapter.UtilityAdapter;
import com.yixia.weibo.sdk.MediaRecorderBase;
import com.yixia.weibo.sdk.MediaRecorderBase.OnEncodeListener;
import com.yixia.weibo.sdk.MediaRecorderBase.OnErrorListener;
import com.yixia.weibo.sdk.MediaRecorderBase.OnPreparedListener;
import com.yixia.weibo.sdk.MediaRecorderNative;
import com.yixia.weibo.sdk.VCamera;
import com.yixia.weibo.sdk.model.MediaObject;
import com.yixia.weibo.sdk.model.MediaObject.MediaPart;
import com.yixia.weibo.sdk.util.DeviceUtils;
import com.yixia.weibo.sdk.util.FileUtils;
import com.yixia.weibo.sdk.util.Log;

import java.io.File;
import java.util.ArrayList;

/**
 * ��Ƶ¼��
 * 
 * @author yixia.com
 *
 */
public class MediaRecorderActivity extends BaseActivity implements OnErrorListener, OnClickListener, OnPreparedListener,  OnEncodeListener {

	/** ¼���ʱ�� */
	public final static int RECORD_TIME_MAX = 10 * 1000;
	/** ¼����Сʱ�� */
	public final static int RECORD_TIME_MIN = 3 * 1000;
	/** ˢ�½����� */
	private static final int HANDLE_INVALIDATE_PROGRESS = 0;
	/** �ӳ�����ֹͣ */
	private static final int HANDLE_STOP_RECORD = 1;
	/** �Խ� */
	private static final int HANDLE_HIDE_RECORD_FOCUS = 2;
 
	/** �Խ�ͼ��-������Ч�� */
	private ImageView mFocusImage;
 
	/** ��ɾ��ť��ȷ����ť */
	private CheckedTextView mRecordDelete,mRecordOk;
 
	/** ���㰴ť */
	private ImageView mRecordController;

	/** �ײ��� */
	private RelativeLayout mBottomLayout;
	/** ����ͷ������ʾ���� */
	private SurfaceView mSurfaceView;
	/** ¼�ƽ��� */
	private ProgressView mProgressView;
	/** �Խ����� */
	private Animation mFocusAnimation;

	/** SDK��Ƶ¼�ƶ��� */
	private MediaRecorderBase mMediaRecorder;
	/** ��Ƶ��Ϣ */
	private MediaObject mMediaObject;

	/** ��Ҫ���±��루�����µĻ��߻�ɾ�� */
	private boolean mRebuild;
	/** on */
	private boolean mCreated;
	/** �Ƿ��ǵ��״̬ */
	private volatile boolean mPressedStatus;
	/** �Ƿ��Ѿ��ͷ� */
	private volatile boolean mReleased;
	/** �Խ�ͼƬ��� */
	private int mFocusWidth;
	/** �ײ�����ɫ */
	private int mBackgroundColorNormal, mBackgroundColorPress;
	/** ��Ļ��� */
	private int mWindowWidth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mCreated = false;
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // ��ֹ����
		loadIntent();
		loadViews();
		mCreated = true;
		
	}

	/** ���ش���Ĳ��� */
	private void loadIntent() {
		mWindowWidth = DeviceUtils.getScreenWidth(this);

		mFocusWidth = ConvertToUtils.dipToPX(this, 64);
		mBackgroundColorNormal = getResources().getColor(R.color.black);//camera_bottom_bg
		mBackgroundColorPress = getResources().getColor(R.color.camera_bottom_press_bg);
	}

	/** ������ͼ */
	private void loadViews() {
		setContentView(R.layout.activity_media_recorder);
		// ~~~ �󶨿ؼ�
		mSurfaceView = (SurfaceView) findViewById(R.id.record_preview);
 		mFocusImage = (ImageView) findViewById(R.id.record_focusing);
		mProgressView = (ProgressView) findViewById(R.id.record_progress);
		mRecordDelete = (CheckedTextView) findViewById(R.id.record_delete);
		mRecordOk=(CheckedTextView) findViewById(R.id.record_ok);
		mRecordController = (ImageView) findViewById(R.id.record_controller);
		mBottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
 
		// ~~~ ���¼�
		if (DeviceUtils.hasICS()) {
			mSurfaceView.setOnTouchListener(mOnSurfaveViewTouchListener);
		}
  		mRecordDelete.setOnClickListener(this);
  		mRecordOk.setOnClickListener(this);
		mBottomLayout.setOnTouchListener(mOnVideoControllerTouchListener);

		// ~~~ ��������
 
 		try {
			mFocusImage.setImageResource(R.drawable.video_focus);
//			mFocusImage.setVisibility(View.VISIBLE);
		} catch (OutOfMemoryError e) {
			Logger.e(e);
		}

		mProgressView.setMaxDuration(RECORD_TIME_MAX);
		initSurfaceView();
	}

	/** ��ʼ������ */
	private void initSurfaceView() {
		final int w = DeviceUtils.getScreenWidth(this);
		((RelativeLayout.LayoutParams) mBottomLayout.getLayoutParams()).topMargin = w;
		int width = w;
		int height = w * 4 / 3;
		//
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mSurfaceView.getLayoutParams();
		lp.width = width;
		lp.height = height;
		mSurfaceView.setLayoutParams(lp);
	}

	/** ��ʼ������SDK */
	private void initMediaRecorder() {
		mMediaRecorder = new MediaRecorderNative();
		mRebuild = true;

		mMediaRecorder.setOnErrorListener(this);
		mMediaRecorder.setOnEncodeListener(this);
		File f = new File(VCamera.getVideoCachePath());
		if (!FileUtils.checkFile(f)) {
			f.mkdirs();
		}
		String key = String.valueOf(System.currentTimeMillis());
		mMediaObject = mMediaRecorder.setOutputDirectory(key, VCamera.getVideoCachePath() + key);
		mMediaRecorder.setOnSurfaveViewTouchListener(mSurfaceView);
		mMediaRecorder.setSurfaceHolder(mSurfaceView.getHolder());
		mMediaRecorder.prepare();
	}

	/** �����Ļ¼�� */
	private View.OnTouchListener mOnSurfaveViewTouchListener = new View.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (mMediaRecorder == null || !mCreated) {
				return false;
			}

			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					//����Ƿ��ֶ��Խ�
					showFocusImage(event);
					if (!mMediaRecorder.onTouch(event, new AutoFocusCallback() {
						@Override
						public void onAutoFocus(boolean success, Camera camera) {
							mFocusImage.setVisibility(View.GONE);
							
						}
					})) {
						return true;
					} else {
						mFocusImage.setVisibility(View.GONE);
					}
					mMediaRecorder.setAutoFocus();
					break;
			}
			return true;
		}

	};

	/** �����Ļ¼�� */
	private View.OnTouchListener mOnVideoControllerTouchListener = new View.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (mMediaRecorder == null) {
				return false;
			}

			//			Logger.e("[MediaRecorderActivity]event.getAction() " + event.getAction());

			switch (event.getAction()) {
			//				case MotionEvent.ACTION_MOVE:
			//
			//					if (mMediaObject.getDuration() >= RECORD_TIME_MAX) {
			//						stopRecord();
			//						mTitleNext.performClick();
			//					}
			//					break;

				case MotionEvent.ACTION_DOWN:
					//����Ƿ��ֶ��Խ�
					//�ж��Ƿ��Ѿ���ʱ
					//					Logger.e("[MediaRecorderActivity]mMediaObject.getDuration() " + mMediaObject.getDuration());
					if (mMediaObject.getDuration() >= RECORD_TIME_MAX) {
						return true;
					}

					//ȡ����ɾ
					if (cancelDelete())
						return true;

					startRecord();

					break;

				case MotionEvent.ACTION_UP:
					// ��ͣ
					if (mPressedStatus) {
						stopRecord();

						//����Ƿ��Ѿ����
						if (mMediaObject.getDuration() >= RECORD_TIME_MAX) {
						 
						}
					}
					break;
			}
			return true;
		}

	};

	@Override
	public void onResume() {
		super.onResume();
		UtilityAdapter.freeFilterParser();
		UtilityAdapter.initFilterParser();

		if (mMediaRecorder == null) {
			initMediaRecorder();
		} else {
 			mMediaRecorder.prepare();
			mProgressView.setData(mMediaObject);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		stopRecord();
		UtilityAdapter.freeFilterParser();
		if (!mReleased) {
			if (mMediaRecorder != null)
				mMediaRecorder.release();
		}
		mReleased = false;
	}

	 
	private void showFocusImage(MotionEvent e) {

		int x = Math.round(e.getX());
		int y = Math.round(e.getY());
		int focusWidth = 100;
		int focusHeight = 100;
		int previewWidth = mSurfaceView.getWidth();
		Rect touchRect = new Rect();

		mMediaRecorder.calculateTapArea(focusWidth, focusHeight, 1f, x, y, previewWidth, previewWidth, touchRect);

		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mFocusImage.getLayoutParams();
		int left = touchRect.left - (mFocusWidth / 2);//(int) x - (focusingImage.getWidth() / 2);
		int top = touchRect.top - (mFocusWidth / 2);//(int) y - (focusingImage.getHeight() / 2);
		if (left < 0)
			left = 0;
		else if (left + mFocusWidth > mWindowWidth)
			left = mWindowWidth - mFocusWidth;
		if (top + mFocusWidth > mWindowWidth)
			top = mWindowWidth - mFocusWidth;

		lp.leftMargin = left;
		lp.topMargin = top;

		 

		mFocusImage.setLayoutParams(lp);
		mFocusImage.setVisibility(View.VISIBLE);

		if (mFocusAnimation == null)
			mFocusAnimation = AnimationUtils.loadAnimation(this, R.anim.record_focus);

		mFocusImage.startAnimation(mFocusAnimation);
		
		
		

		mHandler.sendEmptyMessageDelayed(HANDLE_HIDE_RECORD_FOCUS, 3500);//���3.5��ҲҪ��ʧ
	}

	/** ��ʼ¼�� */
	private void startRecord() {
		if (mMediaRecorder != null) {
			MediaPart part = mMediaRecorder.startRecord();
			if (part == null) {
				return;
			}

			//���ʹ��MediaRecorderSystem����������;�л�ǰ������ͷ������������
 			mProgressView.setData(mMediaObject);
		}

		mRebuild = true;
		mPressedStatus = true;
		mRecordController.setImageResource(R.drawable.record_controller_press);
		mBottomLayout.setBackgroundColor(mBackgroundColorPress);

		if (mHandler != null) {
			mHandler.removeMessages(HANDLE_INVALIDATE_PROGRESS);
			mHandler.sendEmptyMessage(HANDLE_INVALIDATE_PROGRESS);

			mHandler.removeMessages(HANDLE_STOP_RECORD);
			mHandler.sendEmptyMessageDelayed(HANDLE_STOP_RECORD, RECORD_TIME_MAX - mMediaObject.getDuration());
		}
		mRecordDelete.setVisibility(View.GONE);
		mRecordOk.setVisibility(View.GONE);
 	}

	@Override
	public void onBackPressed() {
		if (mRecordDelete != null && mRecordDelete.isChecked()) {
			cancelDelete();
			return;
		}

		if (mMediaObject != null && mMediaObject.getDuration() > 1) {
			//δת��
			new AlertDialog.Builder(this).setTitle(R.string.hint).setMessage(R.string.record_camera_exit_dialog_message).setNegativeButton(R.string.record_camera_cancel_dialog_yes, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					mMediaObject.delete();
					finish();
				}

			}).setPositiveButton(R.string.record_camera_cancel_dialog_no, null).setCancelable(false).show();
			return;
		}

		if (mMediaObject != null)
			mMediaObject.delete();
		finish();
	}

	/** ֹͣ¼�� */
	private void stopRecord() {
		mPressedStatus = false;
		mRecordController.setImageResource(R.drawable.record_controller_normal);
		mBottomLayout.setBackgroundColor(mBackgroundColorNormal);

		if (mMediaRecorder != null) {
			mMediaRecorder.stopRecord();
		}

		mRecordDelete.setVisibility(View.VISIBLE);
 
		mHandler.removeMessages(HANDLE_STOP_RECORD);
		checkStatus();
	}

	@Override
	public void onClick(View v) {
		final int id = v.getId();
		if (mHandler.hasMessages(HANDLE_STOP_RECORD)) {
			mHandler.removeMessages(HANDLE_STOP_RECORD);
		}

		//��������ɾ�������������
		if (id != R.id.record_delete) {
			if (mMediaObject != null) {
				MediaObject.MediaPart part = mMediaObject.getCurrentPart();
				if (part != null) {
					if (part.remove) {
						part.remove = false;
						mRecordDelete.setChecked(false);
						if (mProgressView != null)
							mProgressView.invalidate();
					}
				}
			}
		}

		switch (id) {
 			case R.id.record_ok:// ֹͣ¼��
				mMediaRecorder.startEncoding();
				break;
			case R.id.record_delete:
 				//ȡ����ɾ
				if (mMediaObject != null) {
					MediaObject.MediaPart part = mMediaObject.getCurrentPart();
					if (part != null) {
//						if (part.remove) {
							mRebuild = true;
							part.remove = false;
							backRemove();
							mRecordDelete.setChecked(false);
							mRecordOk.setVisibility(View.GONE);
							Log.e("delete ...");
//						} else {
//							part.remove = true;
//							mRecordDelete.setChecked(true);
//							Log.e("delete ...true");
//						}
					}
					if (mProgressView != null)
						mProgressView.invalidate();

					//��ⰴť״̬
					checkStatus();
				}
				break;
		}
	}
	
	/** ��ɾ */
	public boolean backRemove() {
		if (mMediaObject != null && mMediaObject.mediaList != null) {
			int size = mMediaObject.mediaList.size();
			if (size > 0) {
				MediaPart part = mMediaObject.mediaList.get(size - 1);
				mMediaObject.removePart(part, true);

				if (mMediaObject.mediaList.size() > 0)
					mMediaObject.mCurrentPart = mMediaObject.mediaList.get(mMediaObject.mediaList.size() - 1);
				else
					mMediaObject.mCurrentPart = null;
				return true;
			}
		}
		return false;
	}

	/** ȡ����ɾ */
	private boolean cancelDelete() {
		if (mMediaObject != null) {
			MediaObject.MediaPart part = mMediaObject.getCurrentPart();
			if (part != null && part.remove) {
				part.remove = false;
				mRecordDelete.setChecked(false);

				if (mProgressView != null)
					mProgressView.invalidate();

				return true;
			}
		}
		return false;
	}

	/** ���¼��ʱ�䣬��ʾ/������һ����ť */
	private int checkStatus() {
		int duration = 0;
		if (!isFinishing() && mMediaObject != null) {
			duration = mMediaObject.getDuration();
			Log.e("time:"+duration);
			if (duration < RECORD_TIME_MIN) {
				if (duration == 0) {
 					mRecordDelete.setVisibility(View.GONE);
				}
				//��Ƶ�������3��
				
			} else {
 				//ȷ������
				if (mRecordOk.getVisibility() == View.GONE)
 					mRecordOk.setVisibility(View.VISIBLE);
			}
		}
		return duration;
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case HANDLE_STOP_RECORD:
					stopRecord();
 					break;
				case HANDLE_INVALIDATE_PROGRESS:
					if (mMediaRecorder != null && !isFinishing()) {
						if (mProgressView != null)
							mProgressView.invalidate();
						//					if (mPressedStatus)
						//						titleText.setText(String.format("%.1f", mMediaRecorder.getDuration() / 1000F));
						if (mPressedStatus)
							sendEmptyMessageDelayed(0, 30);
					}
					break;
			}
		}
	};

	@Override
	public void onEncodeStart() {
		showProgress("", getString(R.string.record_camera_progress_message));
	}

	@Override
	public void onEncodeProgress(int progress) {
		//		Logger.e("[MediaRecorderActivity]onEncodeProgress..." + progress);
	}

	/** ת����� */
	@Override
	public void onEncodeComplete() {
		hideProgress();
//		Intent intent = new Intent(this, PlayVideoActivity.class);
//		Bundle bundle = getIntent().getExtras();
//		if (bundle == null)
//			bundle = new Bundle();
//		bundle.putSerializable(CommonIntentExtra.EXTRA_MEDIA_OBJECT, mMediaObject);
//		bundle.putString("output", mMediaObject.getOutputTempVideoPath());
// 		bundle.putBoolean("Rebuild", mRebuild);
//		intent.putExtras(bundle);
//		intent.putExtra("output",  mMediaObject.getOutputTempVideoPath());
//		startActivity(intent);
		mRebuild = false;
		reback();
	}
	/**
	 * ���ص���һ��activity
	 */
	public void reback(){
 		 Intent aintent = new Intent(MediaRecorderActivity.this, AddRecordActivity.class);
 		 aintent.putExtra("videourl", mMediaObject.getOutputTempVideoPath());
  		// aintent.putExtra("thumburl", videoview.getVideoImgPath());
  		 aintent.putExtra("time", mMediaObject.getDuration()/1000);
	     setResult(RESULT_OK,aintent);
	     MediaRecorderActivity.this.finish();
	}
	/**
	 * ת��ʧ��
	 * 	���sdcard�Ƿ���ã����ֿ��Ƿ����
	 */
	@Override
	public void onEncodeError() {
		hideProgress();
		Toast.makeText(this, R.string.record_video_transcoding_faild, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onVideoError(int what, int extra) {
		Toast.makeText(this, R.string.record_video_connect_camera_faild, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onAudioError(int what, String message) {
		Toast.makeText(this, R.string.record_video_connect_audio_faild, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onPrepared() {

	}
}
