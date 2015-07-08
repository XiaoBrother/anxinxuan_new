package com.xmpp.client.connect.packet.listener;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;

import com.ncpzs.util.LogUtil;
import com.xmpp.client.connect.task.Task;

public class RegisteListener implements PacketListener{

	@Override
	 public void processPacket(Packet packet) {
        if (packet instanceof IQ) {
           IQ response = (IQ) packet;
           if (response.getType() == IQ.Type.ERROR) {
               if (!response.getError().toString().contains(
                       "409")) {
               	LogUtil.logError(Task.class,
                           "Unknown error while registering XMPP account! "
                                   + response.getError()
                                           .getCondition());
               }
           } else if (response.getType() == IQ.Type.RESULT) {
           	LogUtil.logInfo(Task.class, "registe success");
           }
       }
   }

}
