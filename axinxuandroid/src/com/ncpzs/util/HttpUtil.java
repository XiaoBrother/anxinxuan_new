package com.ncpzs.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

 import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
 import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
 

public class HttpUtil {
	
	private static  AsyncHttpClient client =new AsyncHttpClient();    //ʵ��������
    static{
        client.setTimeout(10000);   //�������ӳ�ʱ����������ã�Ĭ��Ϊ10s
    }
    public static void get(String urlString,AsyncHttpResponseHandler res)   { //��һ������url��ȡһ��string����
         client.get(urlString, res);
    }
    public static void get(String urlString,RequestParams params,AsyncHttpResponseHandler res){   //url���������
     	client.get(urlString, params,res);
    }
    public static void get(String urlString,JsonHttpResponseHandler res) {  //������������ȡjson�����������
         client.get(urlString, res);
    }
    public static void get(String urlString,RequestParams params,JsonHttpResponseHandler res) {  //����������ȡjson�����������
         client.get(urlString, params,res);
    }
    public static void get(String uString, BinaryHttpResponseHandler bHandler)  { //��������ʹ�ã��᷵��byte����
         client.get(uString, bHandler);
    }
    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		client.post(url, params, responseHandler);
 	}
    public static void post(String url,AsyncHttpResponseHandler responseHandler) {
		client.post(url, responseHandler);
 	}
     
    public static InputStream getImageStream(String path) throws Exception {
		HttpURLConnection conn = getConnection(path, "GET");
		if (conn != null) {
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				return conn.getInputStream();
			}
		}
 		return null;
	}
	 
    public static HttpURLConnection getConnection(String url, String method) {
		try {
			URL curl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) curl.openConnection();
			//conn.setConnectTimeout(5 * 1000);
			//conn.setReadTimeout( 20 * 1000);
			conn.setRequestMethod(method);
  			return conn;
		} catch (Exception ex) {
			return null;
		}
	}
    public static boolean downLoadFile(String url,String savepath,String savename){
		try{
			HttpURLConnection conn=getConnection(url, "GET");
			InputStream is =null;
			if (conn != null) {
				if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
					is= conn.getInputStream();
				}
			}
  	         if(is!=null){
	        	//1K�����ݻ���
	             byte[] bs = new byte[1024];
	             //��ȡ�������ݳ���
	             int len;
	             FileUtil.createDirectory(savepath);
	             //������ļ���
	             OutputStream os = new FileOutputStream(savepath+File.separator+savename);
	             //��ʼ��ȡ
	             while ((len = is.read(bs)) != -1) {
	                 os.write(bs, 0, len);
	             }
	             //��ϣ��ر���������
	             os.close();
	             is.close();
	             return true;
	         }
	         return false;
		}catch(Exception ex){
			return false;
		}
 	}
    
    /**
	 * Get data from stream
	 * 
	 * @param inStream
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] readStream(String path)  {
		HttpURLConnection conn =getConnection(path, "GET");
   		if(conn!=null){
			try {
				InputStream inStream=conn.getInputStream();
			    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
		  		while ((len = inStream.read(buffer)) != -1) {
					outStream.write(buffer, 0, len);
				}
 				outStream.close();
				inStream.close();
				return outStream.toByteArray();
			} catch (Exception e) {
 				return null;
		    }
		}
		return null;
	}
	/**
	 * Get data from stream
	 * 
	 * @param inStream
	 * type:get post
	 * @return byte[]
	 * @throws Exception
	 */
	public static String readStreamWithPost(String url,String params)  {
          try{
        	  HttpURLConnection connection=getConnection(url, "POST");
               //�򿪶�д���ԣ�Ĭ�Ͼ�Ϊfalse
              connection.setDoOutput(true);                 
              connection.setDoInput(true);
              // ��������ʽ��Ĭ��ΪGET
              connection.setRequestMethod("POST");
              // Post ������ʹ�û���
              connection.setUseCaches(false);
                // �������ӵ�Content-type������Ϊapplication/x- www-form-urlencoded����˼��������urlencoded�������form�������������ǿ��Կ������Ƕ���������ʹ��URLEncoder.encode���б���
//              connection.setRequestProperty(" Content-Type ",
//                              "application/x-www-form-urlencoded ");
              // ���ӣ���postUrl.openConnection()���˵����ñ���Ҫ�� connect֮ǰ��ɣ�
              // Ҫע�����connection.getOutputStream()�������Ľ��е��� connect()�������������ʡ��
             // connection.connect();
              DataOutputStream out = new DataOutputStream(connection
                              .getOutputStream());
                // DataOutputStream.writeBytes���ַ����е�16λ�� unicode�ַ���8λ���ַ���ʽд��������
              out.write(params.getBytes());
              out.flush();
              out.close(); // flush and close
              InputStream is = connection.getInputStream();  
              int read = -1;  
              ByteArrayOutputStream   baos = new ByteArrayOutputStream();  
              while ((read = is.read()) != -1) {  
                       baos.write(read);  
              }  
              byte[] data = baos.toByteArray();  
              baos.close();  
              return new String(data);
           }catch(Exception  ex){
        	  ex.printStackTrace();
          }
          return null;
          //connection.disconnect();
 	}
}
