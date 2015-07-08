package com.youku.uploader;

import java.util.HashMap;

import com.loopj.android.http.AsyncHttpResponseHandler;

public interface IRequest {

	/**
	 * ��ȡaccess_token
	 */
	void login(String username, String password, AsyncHttpResponseHandler responseHandler);

	/**
	 * ˢ��access_token
	 */
	void refresh_token(String refresh_token, AsyncHttpResponseHandler responseHandler);

	/**
	 * ��ȡupload_token��upload_server_url
	 */
	void create(String access_token, HashMap<String, String> uploadInfo, AsyncHttpResponseHandler responseHandler);

	/**
	 * �����ϴ��ļ����ύ�ϴ���Ϣ
	 */
	void create_file(String upload_token, String file_size, String ext, String upload_server_uri, AsyncHttpResponseHandler responseHandler);

	/**
	 * ���󴴽�slice_task_id, ��ȡ��Ƭoffset�����ȵ�
	 */
	void new_slice(String upload_token, String upload_server_uri, AsyncHttpResponseHandler responseHandler);

	/**
	 * �ϴ���Ƭ
	 */
	void upload_slice(String upload_token, String upload_server_uri, HashMap<String, String> sliceInfo, byte[] data, AsyncHttpResponseHandler responseHandler);

	/**
	 * ����ϴ������Ƿ����
	 */
	void check(String upload_token, String upload_server_uri, AsyncHttpResponseHandler responseHandler);

	/**
	 * ȷ���ϴ�����
	 */
	void commit(String access_token, String upload_token, String upload_server_ip, AsyncHttpResponseHandler responseHandler);
	
	void cancel(String access_token, String upload_token, String upload_server_ip, AsyncHttpResponseHandler responseHandler);
}
