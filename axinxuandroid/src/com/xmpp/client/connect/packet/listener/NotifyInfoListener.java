package com.xmpp.client.connect.packet.listener;
 
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import com.axinxuandroid.sys.gloable.Gloable;
import com.ncpzs.util.LogUtil;
import com.xmpp.client.connect.packet.CompanyInfoMessage;
import com.xmpp.client.connect.packet.NotifyInfoIQ;

import android.content.Intent;

public class NotifyInfoListener implements PacketListener{
 	@Override
	public void processPacket(Packet packet) {
		  if (packet instanceof NotifyInfoIQ) {
			 NotifyInfoIQ notification = (NotifyInfoIQ) packet;
 	            if (notification.getChildElementXML().contains(
	                    "androidpn:iq:notification")) {
	                String notificationId = notification.getId();
 	                String notificationTitle = notification.getTitle();
	                String notificationMessage = notification.getMessage();
                    LogUtil.logInfo(NotifyInfoListener.class, "title:"+notificationTitle+",info:"+notificationMessage);
                    Intent in=new Intent("com.ncpzs.notify");
                    in.putExtra("id", notificationId);
                    in.putExtra("title", notificationTitle);
                    in.putExtra("info", notificationMessage);
                    Gloable.getInstance().getCurContext().sendBroadcast(in);
 	            } 
	        }else if(packet instanceof Message){
	        	Message company=(Message)packet;
 	        	LogUtil.logInfo(NotifyInfoListener.class, "name:"+company.getBody());
	        }
		
	}

}
