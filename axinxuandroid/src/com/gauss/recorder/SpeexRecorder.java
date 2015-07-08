package com.gauss.recorder;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import com.gauss.speex.encode.SpeexEncoder;
import com.ncpzs.util.LogUtil;

public class SpeexRecorder implements Runnable {

//	private Logger log = LoggerFactory.getLogger(SpeexRecorder.class);
	private volatile boolean isRecording;
	private final Object mutex = new Object();
	private static final int frequency = 8000;
	private static final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
	public static int packagesize = 160;
	private String fileName = null;
	private int volumn=-1;//音量大小
	private VolumnChangeListener listener;
	public SpeexRecorder(String fileName) {
		super();
		this.fileName = fileName;
	}

	public void run() {

		// 启动编码线程
		SpeexEncoder encoder = new SpeexEncoder(this.fileName);
		Thread encodeThread = new Thread(encoder);
		encoder.setRecording(true);
		encodeThread.start();

		synchronized (mutex) {
			while (!this.isRecording) {
				try {
					mutex.wait();
				} catch (InterruptedException e) {
					throw new IllegalStateException("Wait() interrupted!", e);
				}
			}
		}
		android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);

		int bufferRead = 0;
		int bufferSize = AudioRecord.getMinBufferSize(frequency, AudioFormat.CHANNEL_IN_MONO, audioEncoding);

		short[] tempBuffer = new short[packagesize];

		AudioRecord recordInstance = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency, AudioFormat.CHANNEL_IN_MONO, audioEncoding,
				bufferSize);

		recordInstance.startRecording();

		while (this.isRecording) {
//			log.debug("start to recording.........");
			bufferRead = recordInstance.read(tempBuffer, 0, packagesize);
			// bufferRead = recordInstance.read(tempBuffer, 0, 320);
			if (bufferRead == AudioRecord.ERROR_INVALID_OPERATION) {
				throw new IllegalStateException("read() returned AudioRecord.ERROR_INVALID_OPERATION");
			} else if (bufferRead == AudioRecord.ERROR_BAD_VALUE) {
				throw new IllegalStateException("read() returned AudioRecord.ERROR_BAD_VALUE");
			} else if (bufferRead == AudioRecord.ERROR_INVALID_OPERATION) {
				throw new IllegalStateException("read() returned AudioRecord.ERROR_INVALID_OPERATION");
			}
//			log.debug("put data into encoder collector....");
			encoder.putData(tempBuffer, bufferRead);
			//通知音量变化
            int v=0;
			for (int i = 0; i < tempBuffer.length; i++) {
				v += tempBuffer[i] * tempBuffer[i];
			}
 			int value = (int) (Math.abs((int)(v /(float)bufferRead)/10000) >> 1);
 			if(value!=this.volumn){
 				this.volumn=value;
 				if(this.listener!=null)
 					this.listener.onchange(this.volumn);
 				//LogUtil.logInfo(getClass(), "volum:"+ value);
 			}
            
 		}
		recordInstance.stop();
		//tell encoder to stop.
		encoder.setRecording(false);
	}

	public void setRecording(boolean isRecording) {
		synchronized (mutex) {
			this.isRecording = isRecording;
			if (this.isRecording) {
				mutex.notify();
			}
		}
	}

	public boolean isRecording() {
		synchronized (mutex) {
			return isRecording;
		}
	}
	public interface  VolumnChangeListener{
		public void onchange(int volumn);
	}
	
	public void setVolumnChangeListener(VolumnChangeListener listener){
		this.listener=listener;
	}
}
