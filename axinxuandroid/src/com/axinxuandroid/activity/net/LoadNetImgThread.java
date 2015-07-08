package com.axinxuandroid.activity.net;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.anxinxuandroid.constant.AppConstant;
import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.BitmapUtils;
import com.ncpzs.util.FileUtil;
import com.ncpzs.util.HttpUtil;
import com.ncpzs.util.LogUtil;

public class LoadNetImgThread extends Thread {
	private String neturl;
	private String savepath; // 默认AppConstant.PHOTO_DIR
	private String savename;
	private LoadImgFinishListener listener;
	public static final int RESULT_SUCCESS = 1;
	public static final int RESULT_ERROR = 2;

	public LoadNetImgThread(String url, String savepath, String savename) {
		this.neturl = url;
		this.savepath = savepath;
		this.savename = savename;
	}

	@Override
	public void run() {
		try {
			if (savename == null) {
				String url = neturl.replace("\\", "/");
				savename = url.substring(url.lastIndexOf("/") + 1);
			}
			if (savepath == null) {
				savepath = AppConstant.PHOTO_DIR;
			}
			if (FileUtil.hasFile(savepath + savename)) {// 本地有改图片
				if (this.listener != null)
					this.listener.onfinish(RESULT_SUCCESS, savepath + savename);
			} else {
				if (savename != null) {
					savename = savename.trim();
					HttpUtil.downLoadFile(neturl, savepath, savename);
					BitmapUtils.compressImage(savepath+savename);//压缩图片质量
					if (this.listener != null)
						this.listener.onfinish(RESULT_SUCCESS, savepath
								+ savename);
					// 图片处理
					// mBitmap=BitmapUtils.createImageThumb(mBitmap,
					// Gloable.getInstance().getScreenWeight(), savename,
					// savepath);
				} else {
					if (this.listener != null)
						this.listener.onfinish(RESULT_ERROR, null);
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
			if (this.listener != null)
				this.listener.onfinish(RESULT_ERROR, null);
		}

	}

	public interface LoadImgFinishListener {
		public void onfinish(int result, String path);
	}

	public void setLoadImgFinishListener(LoadImgFinishListener lis) {
		this.listener = lis;
	}

}
