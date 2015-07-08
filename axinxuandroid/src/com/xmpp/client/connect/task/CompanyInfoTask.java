package com.xmpp.client.connect.task;

 
 import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.provider.ProviderManager;

import android.util.Log;

import com.xmpp.client.connect.ServerConfig;
import com.xmpp.client.connect.ServerConnection;
  
public class CompanyInfoTask extends Task {
 	private ServerConnection serverconnect;
 	public CompanyInfoTask(){
		this.serverconnect=ServerConnection.getInstance();
 		this.setTaskName("company info");
		this.setTaskFinishEventHandler(new DefaultTaskFinishEventHandler());
	}
	@Override
	public void task() throws Exception {
		// TODO Auto-generated method stub
 		if(serverconnect.isConnect()){
 			//·¢ËÍÏûÏ¢
 			  Message msg = new Message(ServerConfig.SERVER_IP, Message.Type.normal);           
 			  msg.setSubject("company");   
 			  msg.setFrom("hubobo");
 			 // msg.setBody("c"+text); 
 			  //c is used to indicate this message is chatting txt     
 			 serverconnect.getConnection().sendPacket(msg);
            
		}
	}

}
