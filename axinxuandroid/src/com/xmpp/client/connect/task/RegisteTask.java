package com.xmpp.client.connect.task;

 import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Registration;

 

import com.ncpzs.util.LogUtil;
import com.xmpp.client.connect.ServerConnection;
import com.xmpp.client.connect.packet.listener.RegisteListener;

public class RegisteTask extends Task {
 	private ServerConnection serverconnect;
    private String username;
    private String pwd;
	public RegisteTask(String username,String pwd){
		serverconnect=ServerConnection.getInstance();
		this.setTaskName("userRegiste");
		this.setTaskFinishEventHandler(new DefaultTaskFinishEventHandler());
		this.username=username;
		this.pwd=pwd;
	}
	@Override
	public void task() throws Exception {
 		if(serverconnect.isConnect()){
			Registration registration = new Registration();
			//过滤服务器返回的包，只接受id和注册发送相同的包
			PacketFilter packetFilter = new AndFilter(new PacketIDFilter(
                    registration.getPacketID()), new PacketTypeFilter(
                    IQ.class));
			//监听服务器返回的包，
			PacketListener packetListener = new RegisteListener();
            serverconnect.getConnection().addPacketListener(packetListener, packetFilter);
            registration.setType(IQ.Type.SET);
            registration.addAttribute("username", username);
            registration.addAttribute("password", pwd);
            serverconnect.getConnection().sendPacket(registration);
		}else{
			LogUtil.logError(Task.class, "cant connect server!");
			throw new Exception("can't connect server");
		}
		 
	}
	 

}
