/**
 * 
 */
package com.gauss.recorder;

import java.io.File;

import com.gauss.speex.encode.SpeexDecoder;

/**
 * @author Gauss
 * 
 */
public class SpeexPlayer {
	private String fileName = null;
	private SpeexDecoder speexdec = null;
 	private PlayEndListener listener;
	public SpeexPlayer(String fileName) {

		this.fileName = fileName;
		System.out.println(this.fileName);
		try {
			speexdec = new SpeexDecoder(new File(this.fileName));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startPlay() {
	 
		RecordPlayThread rpt = new RecordPlayThread();

		Thread th = new Thread(rpt);
		th.start();
	}

	public void stopPlay(){
		if (speexdec != null)
			speexdec.setStop(true);
	}
	boolean isPlay = true;

	class RecordPlayThread extends Thread {

		public void run() {
			try {
				if (speexdec != null)
					speexdec.decode();
 			} catch (Exception t) {
				t.printStackTrace();
			}finally{
				if(listener!=null)
					listener.onEnd();
			}
		}
	};
	
	public void setPlayEndListener(PlayEndListener lis){
		this.listener=lis;
	}
	
	public  interface PlayEndListener{
		public void onEnd();
	}
}
