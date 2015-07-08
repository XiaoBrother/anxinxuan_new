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
     
    /** �Ƿ�����־���,��Debug״̬�¿���,  
     * ��Release״̬�¹ر�����ʾ��������  
     * */    
    public static final boolean DEBUG = false;    
    /** ϵͳĬ�ϵ�UncaughtException������ */    
    private Thread.UncaughtExceptionHandler mDefaultHandler;    
    /** CrashHandlerʵ�� */    
    private static GloableExceptionHandler INSTANCE;    
    /** �����Context���� */    
    private Context mContext;
     /** ʹ��Properties�������豸����Ϣ�ʹ����ջ��Ϣ*/    
    private Properties mDeviceCrashInfo = new Properties();    
    private static final String VERSION_NAME = "versionName";
    private static final String VERSION_CODE = "versionCode";  
     /** ��ֻ֤��һ��CrashHandlerʵ�� */    
    private GloableExceptionHandler() {}  
   
    /** ��ȡCrashHandlerʵ�� ,����ģʽ*/    
    public static GloableExceptionHandler getInstance() {    
        if (INSTANCE == null) {    
            INSTANCE = new GloableExceptionHandler();    
        }    
        return INSTANCE;    
    }    
    
    /**  
     * ��ʼ��,ע��Context����,  
     * ��ȡϵͳĬ�ϵ�UncaughtException������,  
     * ���ø�CrashHandlerΪ�����Ĭ�ϴ�����  
     *   
     * @param ctx  
     */    
    public void init(Context ctx) {    
        mContext = ctx;    
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();    
        Thread.setDefaultUncaughtExceptionHandler(this);    
    }    
    
    /**  
     * ��UncaughtException����ʱ��ת��ú���������  
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
     * �Զ��������,�ռ�������Ϣ  
     * ���ʹ��󱨸�Ȳ������ڴ����.  
     * �����߿��Ը����Լ���������Զ����쳣�����߼�  
     * @param ex  
     * @return true:��������˸��쳣��Ϣ;���򷵻�false  
     */    
    private boolean handleException(final Throwable ex) {    
        if (ex == null) {    
            return true;    
        }    
        //�ռ��豸��Ϣ    
        //collectCrashDeviceInfo(mContext);  
        //final String msg = ex.getLocalizedMessage();    
        //ʹ��Toast����ʾ�쳣��Ϣ    
        new Thread() {    
            @Override    
            public void run() {    
                Looper.prepare(); 
                Writer info = new StringWriter();  
                PrintWriter printWriter = new PrintWriter(info);  
                // ���� throwable ����׷�������ָ���� PrintWriter  
                ex.printStackTrace(printWriter);  
                 // getCause() ���ش� throwable �� cause����� cause �����ڻ�δ֪���򷵻� null��  
                Throwable cause = ex.getCause();  
                while (cause != null) {  
                    cause.printStackTrace(printWriter);  
                    cause = cause.getCause();  
                }  
                String result = info.toString();  
                printWriter.close();  
                Toast.makeText(mContext, "Ӧ�ó����쳣,�����˳�:"+result, Toast.LENGTH_LONG)    
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
     * �ռ�����������豸��Ϣ  
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
        //ʹ�÷������ռ��豸��Ϣ.��Build���а��������豸��Ϣ,    
        //����: ϵͳ�汾��,�豸������ �Ȱ������Գ����������Ϣ    
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