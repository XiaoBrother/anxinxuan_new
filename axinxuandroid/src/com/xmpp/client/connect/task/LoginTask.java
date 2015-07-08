package com.xmpp.client.connect.task;

 import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;

import com.xmpp.client.connect.ServerConnectListener;
import com.xmpp.client.connect.ServerConnection;
import com.xmpp.client.connect.packet.CompanyInfoMessage;
import com.xmpp.client.connect.packet.NotifyInfoIQ;
import com.xmpp.client.connect.packet.listener.NotifyInfoListener;

public class LoginTask extends Task {
	private static final String XMPP_RESOURCE_NAME = "AndroidpnClient";
	private ServerConnection connection;
    private String username;
    private String pwd;
	public LoginTask(String username,String pwd){
		this.setTaskName("userLogin");
		this.username=username;
		this.pwd=pwd;
		this.setTaskFinishEventHandler(new DefaultTaskFinishEventHandler());
	    connection=ServerConnection.getInstance();
	}
	@Override
	public void  task() throws Exception {
 		 
 		if(connection.isConnect()&&!connection.isAuthenticated()){
 			connection.getConnection().login(username,pwd, XMPP_RESOURCE_NAME);
 		   // packet filter
            PacketFilter packetFilter = new PacketTypeFilter(
                    NotifyInfoIQ.class);
            PacketFilter companypacketFilter = new MessageTypeFilter(
            		Message.Type.normal);
            // packet listener
            ServerConnectListener connectListener=new ServerConnectListener(connection);
            connection.getConnection().addConnectionListener(connectListener);//注册连接情况监听
            PacketListener packetListener = new NotifyInfoListener();
            connection.getConnection().addPacketListener(packetListener, packetFilter);
            //connection.getConnection().addPacketListener(packetListener, companypacketFilter);
  
 		}else{
 			throw new Exception("has login");
 		}
 	}

}
