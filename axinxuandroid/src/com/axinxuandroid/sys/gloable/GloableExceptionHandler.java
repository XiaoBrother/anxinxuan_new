package com.axinxuandroid.sys.gloable;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import com.axinxuandroid.activity.handler.ConfirmDialogHandlerMethod;
import com.axinxuandroid.activity.handler.MessageDialogHandlerMethod;
import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.activity.handler.ProcessDialogHandlerMethod;
import com.axinxuandroid.activity.net.NetFinishListener;
import com.axinxuandroid.activity.net.UploadSystemErrorThread;
import com.axinxuandroid.data.SystemLog;
import com.axinxuandroid.service.SystemLogService;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.ncpzs.util.DateUtil;
import com.ncpzs.util.LogUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class GloableExceptionHandler  implements UncaughtExceptionHandler {    
     
    /** 是否开启日志输出,在Debug状态下开启,  
     * 在Release状态下关闭以提示程序性能  
     * */    
    public static final boolean DEBUG = false;    
    /** 系统默认的UncaughtException处理类 */    
    private Thread.UncaughtExceptionHandler mDefaultHandler;    
    /** CrashHandler实例 */    
    private static GloableExceptionHandler INSTANCE;    
    /** 程序的Context对象 */    
    private Context mContext;
     /** 使用Properties来保存设备的信息和错误堆栈信息*/    
    private Properties mDeviceCrashInfo = new Properties();    
    private static final String VERSION_NAME = "versionName";
    private static final String VERSION_CODE = "versionCode";  
     /** 保证只有一个CrashHandler实例 */    
    private GloableExceptionHandler() {}  
   
    /** 获取CrashHandler实例 ,单例模式*/    
    public static GloableExceptionHandler getInstance() {    
        if (INSTANCE == null) {    
            INSTANCE = new GloableExceptionHandler();    
        }    
        return INSTANCE;    
    }    
    
    /**  
     * 初始化,注册Context对象,  
     * 获取系统默认的UncaughtException处理器,  
     * 设置该CrashHandler为程序的默认处理器  
     *   
     * @param ctx  
     */    
    public void init(Context ctx) {    
        mContext = ctx;    
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();    
        Thread.setDefaultUncaughtExceptionHandler(this);    
    }    
    
    /**  
     * 当UncaughtException发生时会转入该函数来处理  
     */    
    @Override    
    public void uncaughtException(Thread thread, Throwable ex) { 
    	 handleException(ex);
        if (mDefaultHandler != null) {    
         	try {    
                Thread.sleep(1500);    
            } catch (InterruptedException e) {    
                LogUtil.logError(getClass(), "Error : "+e);    
            }    
         //  LogUtil.logError(getClass(), "exit application :"+ex.getMessage());  
           ex.printStackTrace();
           
           android.os.Process.killProcess(android.os.Process.myPid());    
           //System.exit(10); 
           
          }    
    }    
    
    /**  
     * 自定义错误处理,收集错误信息  
     * 发送错误报告等操作均在此完成.  
     * 开发者可以根据自己的情况来自定义异常处理逻辑  
     * @param ex  
     * @return true:如果处理了该异常信息;否则返回false  
     */    
    private boolean handleException(final Throwable ex) {    
        if (ex == null) {    
            return true;    
        }    
        //收集设备信息    
        //collectCrashDeviceInfo(mContext);  
        //final String msg = ex.getLocalizedMessage();    
        //使用Toast来显示异常信息    
        new Thread() {    
            @Override    
            public void run() {    
                Looper.prepare(); 
                Writer info = new StringWriter();  
                PrintWriter printWriter = new PrintWriter(info);  
                // 将此 throwable 及其追踪输出到指定的 PrintWriter  
                ex.printStackTrace(printWriter);  
                 // getCause() 返回此 throwable 的 cause；如果 cause 不存在或未知，则返回 null。  
                Throwable cause = ex.getCause();  
                while (cause != null) {  
                    cause.printStackTrace(printWriter);  
                    cause = cause.getCause();  
                }  
                String result = info.toString();  
                printWriter.close();  
                Toast.makeText(mContext, "应用出现异常,即将退出:"+result, Toast.LENGTH_LONG)    
                        .show();  
                SystemLogService logser=new SystemLogService();
                SystemLog log=new SystemLog();
                log.setMessage(result);
                log.setActiontime(DateUtil.DateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
                logser.save(log);
                Looper.loop();    
                Gloable.getInstance().exitApplication();
            }    
    
        }.start();    
        return true;    
    }    
    
     
      
    
    
    /**  
     * 收集程序崩溃的设备信息  
     *   
     * @param ctx  
     */    
    public void collectCrashDeviceInfo(Context ctx) {    
        try {    
            PackageManager pm = ctx.getPackageManager();    
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),    
                    PackageManager.GET_ACTIVITIES);    
            if (pi != null) {    
                mDeviceCrashInfo.put(VERSION_NAME,    
                        pi.versionName == null ? "not set" : pi.versionName);    
                mDeviceCrashInfo.put(VERSION_CODE, pi.versionCode);    
            }    
        } catch (NameNotFoundException e) {    
        	LogUtil.logError(getClass(), e.getMessage());    
        }    
        //使用反射来收集设备信息.在Build类中包含各种设备信息,    
        //例如: 系统版本号,设备生产商 等帮助调试程序的有用信息    
         Field[] fields = Build.class.getDeclaredFields();    
         for (Field field : fields) {    
            try {    
                field.setAccessible(true);    
                mDeviceCrashInfo.put(field.getName(), field.get(null));    
                if (DEBUG) {    
                     LogUtil.logInfo(getClass(), field.getName() + " : " + field.get(null));    
                }    
            } catch (Exception e) {    
                LogUtil.logError(getClass(), e.getMessage());    
            }    
    
        }    
    
    }    
    
} 