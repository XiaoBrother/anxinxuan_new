package com.xmpp.client.connect;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionListener;

import com.ncpzs.util.LogUtil;

import android.util.Log;

/**
 * ����������Ӽ�������ʱ�̼�������������������
 * @author hubobo
 *
 */
public class ServerConnectListener implements ConnectionListener{

	private ServerConnection connection;
	public ServerConnectListener(ServerConnection connection){
		this.connection=connection;
	}
	//�����ر�����
	@Override
	public void connectionClosed() {
		// TODO Auto-generated method stub
		LogUtil.logInfo(ServerConnectListener.class, "connectionClosed()...");
	}

	//���������ӱ�ǿ�ƹرգ����½�������
	@Override
	public void connectionClosedOnError(Exception arg0) {
		// TODO Auto-generated method stub
		LogUtil.logInfo(ServerConnectListener.class, "connection closed by error");
		connection.reconnectServer(null);
        //xmppManager.startReconnectionThread();
	}

	@Override
	public void reconnectingIn(int arg0) {
		// TODO Auto-generated method stub
		LogUtil.logInfo(ServerConnectListener.class, "reconnectingIn()...");
	}

	@Override
	public void reconnectionFailed(Exception arg0) {
		// TODO Auto-generated method stub
		LogUtil.logInfo(ServerConnectListener.class, "reconnectionFailed()...");
	}

	@Override
	public void reconnectionSuccessful() {
		// TODO Auto-generated method stub
		LogUtil.logInfo(ServerConnectListener.class, "reconnectionSuccessful()...");
	}

}
