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
	
	private static  AsyncHttpClient client =new AsyncHttpClient();    //实例话对象
    static{
        client.setTimeout(10000);   //设置链接超时，如果不设置，默认为10s
    }
    public static void get(String urlString,AsyncHttpResponseHandler res)   { //用一个完整url获取一个string对象
         client.get(urlString, res);
    }
    public static void get(String urlString,RequestParams params,AsyncHttpResponseHandler res){   //url里面带参数
     	client.get(urlString, params,res);
    }
    public static void get(String urlString,JsonHttpResponseHandler res) {  //不带参数，获取json对象或者数组
         client.get(urlString, res);
    }
    public static void get(String urlString,RequestParams params,JsonHttpResponseHandler res) {  //带参数，获取json对象或者数组
         client.get(urlString, params,res);
    }
    public static void get(String uString, BinaryHttpResponseHandler bHandler)  { //下载数据使用，会返回byte数据
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
	        	//1K的数据缓冲
	             byte[] bs = new byte[1024];
	             //读取到的数据长度
	             int len;
	             FileUtil.createDirectory(savepath);
	             //输出的文件流
	             OutputStream os = new FileOutputStream(savepath+File.separator+savename);
	             //开始读取
	             while ((len = is.read(bs)) != -1) {
	                 os.write(bs, 0, len);
	             }
	             //完毕，关闭所有链接
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
               //打开读写属性，默认均为false
              connection.setDoOutput(true);                 
              connection.setDoInput(true);
              // 设置请求方式，默认为GET
              connection.setRequestMethod("POST");
              // Post 请求不能使用缓存
              connection.setUseCaches(false);
                // 配置连接的Content-type，配置为application/x- www-form-urlencoded的意思是正文是urlencoded编码过的form参数，下面我们可以看到我们对正文内容使用URLEncoder.encode进行编码
//              connection.setRequestProperty(" Content-Type ",
//                              "application/x-www-form-urlencoded ");
              // 连接，从postUrl.openConnection()至此的配置必须要在 connect之前完成，
              // 要注意的是connection.getOutputStream()会隐含的进行调用 connect()，所以这里可以省略
             // connection.connect();
              DataOutputStream out = new DataOutputStream(connection
                              .getOutputStream());
                // DataOutputStream.writeBytes将字符串中的16位的 unicode字符以8位的字符形式写道流里面
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
