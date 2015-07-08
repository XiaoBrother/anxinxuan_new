package com.zxing.activity;

import java.io.IOException;
import java.util.Map;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.anxinxuandroid.constant.HttpUrlConstant;
import com.axinxuandroid.activity.NcpZsActivity;
import com.axinxuandroid.activity.R;
import com.axinxuandroid.activity.TimelineActivity;
import com.axinxuandroid.activity.ValidSecurityCodeResultActivity;
import com.axinxuandroid.activity.handler.ConfirmDialogHandlerMethod;
import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.net.NetResult;
import com.axinxuandroid.activity.net.ValidSecurityCodeResultThread;
import com.axinxuandroid.data.User;
import com.axinxuandroid.service.UserService;
import com.axinxuandroid.sys.gloable.Gloable;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;
import com.zxing.camera.CameraManager;
import com.zxing.decoding.CaptureActivityHandler;
import com.zxing.decoding.InactivityTimer;
import com.zxing.view.ViewfinderView;
/**
 * Initial the camera
 * @author Ryan.Tang
 */
public class CaptureActivity extends NcpZsActivity implements Callback {

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	private Button cancelScanButton;
	private User user;
	private UserService uservice;
	private static final int LOGIN_MAX_WAIT_MINUT=5;//登陆最大等待时间

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera);
		//ViewUtil.addTopView(getApplicationContext(), this, R.string.scan_card);
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		cancelScanButton = (Button) this.findViewById(R.id.btn_cancel_scan);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		uservice=new UserService();
		user=uservice.getLastLoginUser();
	}

	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
		
		//quit the scan view
		cancelScanButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CaptureActivity.this.finish();
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}
	
	/**
	 * Handler scan result
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(final Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		String resultString = result.getText();
		//FIXME
		if (resultString.equals("")) {
			Toast.makeText(CaptureActivity.this, "扫码失败!", Toast.LENGTH_SHORT).show();
 		}else {
			if(resultString.indexOf("login:")>=0){
				 final NcpzsHandler hand=Gloable.getInstance().getCurHandler();
				 hand.setOnHandlerFinishListener(new OnHandlerFinishListener() {
 					@Override
					public void onHandlerFinish(Object res) {
 				     long systime=System.currentTimeMillis();
					 int sel=(Integer)((Map)res).get("result");
  					 if(sel==1){
 						try {
 							JSONObject obj=new JSONObject(result.getText());
 							if(user!=null&&obj!=null){
 								String loginid=obj.getString("login");
 								long time=obj.getLong("stime");
 								if((systime/1000-time)>LOGIN_MAX_WAIT_MINUT*60){//超过
 									 hand.excuteMethod(new MessageDialogHandlerMethod("","超过登陆时间限定，请重新刷新页面在进行登陆！"));
 								}else{
 									phoneLogin(loginid);
 								}
 								//LogUtil.logInfo(getClass(), "json:"+result.getText()+"time stamp:"+systime+","+time+","+(systime/1000-time));
 									
 							}else{
 								 hand.excuteMethod(new MessageDialogHandlerMethod("","抱歉，无法登陆！"));
 							}
 						} catch (JSONException e) {
 							 hand.excuteMethod(new MessageDialogHandlerMethod("","抱歉，登陆失败！"));
 					    }
						
					 }
					 CaptureActivity.this.finish();
 					}
				});
				 hand.excuteMethod(new ConfirmDialogHandlerMethod("","确定要登陆网页端安心选吗?"));
				
			}else if(resultString.indexOf("vscode")>=0){
				String resultStr = resultString.substring(resultString.indexOf("vscode")+"vscode".length()+1);
 				if(resultStr!=null){
					String[] codes=	resultStr.split("/");
					Intent resultIntent = new Intent();
					resultIntent.setClass(CaptureActivity.this,
							ValidSecurityCodeResultActivity.class);
					resultIntent.putExtra("vscode", codes[0]);
					resultIntent.putExtra("safecode", codes[1]);
					startActivity(resultIntent);
				}else{
					toUri(resultString);
				}
				CaptureActivity.this.finish();
			}else{
//				System.out.println("Result:"+resultString);
				Intent resultIntent = new Intent();
//				Bundle bundle = new Bundle();
//				bundle.putString("result", resultString);
//				resultIntent.putExtras(bundle);
//				this.setResult(RESULT_OK, resultIntent);
				String[] resultStr = resultString.split("code/");
				if (resultStr != null && resultStr.length > 1) {
					String result_id = resultStr[1];
					if (result_id != null && result_id != "") {
						result_id = result_id.substring(0, result_id.indexOf("/"));
						resultIntent.setClass(CaptureActivity.this,
								TimelineActivity.class);
						resultIntent.putExtra("id", result_id);
						startActivity(resultIntent);
					} else {
						Uri uri = Uri.parse(resultString);
						Intent intent = new Intent(Intent.ACTION_VIEW, uri);
						startActivity(intent);
					}
				} else {
					toUri(resultString);
				}
				CaptureActivity.this.finish();
 			}
 		}
		
	}
	
	private void toUri(String url){
		Uri uri = Uri.parse(url);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}
	
	private void phoneLogin(String socketid){
		String url=HttpUrlConstant.PHONE_LOGIN_WEB;
		RequestParams params=new RequestParams();
		params.put("socketid",socketid  );
		params.put("userid",  user.getPhone()+"");
  		HttpUtil.get(url,params, new LoginJsonResponseHandler());
	}
	public class LoginJsonResponseHandler  extends JsonHttpResponseHandler {
 		@Override
		public void onSuccess(int arg0, final JSONObject json) {
// 			 NcpzsHandler hand=Gloable.getInstance().getCurHandler();
// 			 try {
//				hand.excuteMethod(new MessageDialogHandlerMethod("", json.getString("result")));
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
 		}

		@Override
		protected void handleFailureMessage(Throwable arg0, String arg1) {
		 
		}

	}
	
	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

}