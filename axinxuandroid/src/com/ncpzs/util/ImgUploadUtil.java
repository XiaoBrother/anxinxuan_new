package com.ncpzs.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import android.util.Log;

public class ImgUploadUtil {
	private static final String TAG = "uploadFile";
	private static final int TIME_OUT = 10 * 1000; // ��ʱʱ��
	private static final String CHARSET = "utf-8"; // ���ñ���

	/**
	 * �ϴ��ļ���������
	 * 
	 * @param file
	 *            ��Ҫ�ϴ����ļ�
	 * @param RequestURL
	 *            �����rul
	 * @return ������Ӧ������
	 */
	public static String uploadFile(String file, String RequestURL, String newName,Map<String,String> params) {
		int res = 0; 
 		String BOUNDARY = UUID.randomUUID().toString(); // �߽��ʶ �������
		String PREFIX = "--", LINE_END = "\r\n";
		String CONTENT_TYPE = "multipart/form-data"; // ��������
 		try {
			URL url = new URL(RequestURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(TIME_OUT);
			conn.setConnectTimeout(TIME_OUT);
			conn.setDoInput(true); // ����������
			conn.setDoOutput(true); // ���������
			conn.setUseCaches(false); // ������ʹ�û���
			conn.setRequestMethod("POST"); // ����ʽ
			conn.setRequestProperty("Charset", CHARSET); // ���ñ���
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
					+ BOUNDARY);
		 
			StringBuffer sb = new StringBuffer();
			if(params!=null&&params.size()>0){
				Iterator its=params.entrySet().iterator();
				while(its.hasNext()){
					Entry ent=(Entry) its.next();
					sb.append(PREFIX); 
					sb.append(BOUNDARY); 
					sb.append(LINE_END); 
					sb.append("Content-Disposition: form-data; name=\""+(String)ent.getKey()+"\""+LINE_END+LINE_END); 
					sb.append((String)ent.getValue()); 
					sb.append(LINE_END); 
				}
			}
			
  
			if (file != null) {
				/**
				 * ���ļ���Ϊ��ʱִ���ϴ�
				 */
				DataOutputStream dos = new DataOutputStream(conn
						.getOutputStream());
			
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINE_END);
				/**
				 * �����ص�ע�⣺ name�����ֵΪ����������Ҫkey ֻ�����key �ſ��Եõ���Ӧ���ļ�
				 * filename���ļ������֣�������׺��
				 */
 				sb.append("Content-Disposition: form-data; name=\"file\"; filename=\""
								+ newName + "\"" + LINE_END);
				sb.append("Content-Type: application/octet-stream; charset="
						+ CHARSET + LINE_END);
				sb.append(LINE_END);
				dos.write(sb.toString().getBytes());
				InputStream is = new FileInputStream(file);
				byte[] bytes = new byte[1024];
				int len = 0;
				while ((len = is.read(bytes)) != -1) {
					dos.write(bytes, 0, len);
				}
				is.close();
 				dos.write(LINE_END.getBytes());
				byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
						.getBytes();
				dos.write(end_data);
				dos.flush();
				/**
				 * ��ȡ��Ӧ�� 200=�ɹ� ����Ӧ�ɹ�����ȡ��Ӧ����
				 */
				res = conn.getResponseCode();
 				if (res == 200) {
 					InputStream input = conn.getInputStream();
					StringBuffer sb1 = new StringBuffer();
					int ss;
					while ((ss = input.read()) != -1) {
						sb1.append((char) ss);
					}
					return sb1.toString();
 				}  
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}