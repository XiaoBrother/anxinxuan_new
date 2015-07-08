package com.xmpp.client.connect.task;

 
 import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.provider.ProviderManager;

import android.util.Log;

import com.ncpzs.util.LogUtil;
import com.xmpp.client.connect.ServerConfig;
import com.xmpp.client.connect.ServerConnection;
import com.xmpp.client.connect.packet.NotifyInfoIQProvider;

public class ConnectTask extends Task {
 	private ServerConnection serverconnect;
 
	public ConnectTask(){
		this.serverconnect=ServerConnection.getInstance();
		 
		this.setTaskName("connectServer");
		this.setTaskFinishEventHandler(new DefaultTaskFinishEventHandler());
	}
	@Override
	public void task() throws Exception {
  		if(!serverconnect.isConnect()){
  			//�����û���ӵ�������
			ConnectionConfiguration connConfig = new ConnectionConfiguration(
					ServerConfig.SERVER_IP, ServerConfig.SERVER_PORT);
            // connConfig.setSecurityMode(SecurityMode.required);
            connConfig.setCompressionEnabled(false);
            connConfig.setSecurityMode(SecurityMode.disabled);
            connConfig.setSASLAuthenticationEnabled(false);
           
            XMPPConnection connection = new XMPPConnection(connConfig);
             serverconnect.setConnection(connection);
             LogUtil.logInfo(TaskExcute.class, "ip:"+ServerConfig.SERVER_IP+",port:"+ServerConfig.SERVER_PORT);
            try {
				connection.connect();
				
				//ע��һ��֪ͨ������
				//��������һ�������ƣ��ڶ����������ռ䣬����������Ӧ��packet������
				ProviderManager.getInstance().addIQProvider("notification",
                        "androidpn:iq:notification",
                        new NotifyInfoIQProvider());
 
			} catch (XMPPException e) {
  				 LogUtil.logInfo(ConnectTask.class, "connect server failed");
				 throw new Exception();
			}
		}
	}

}
