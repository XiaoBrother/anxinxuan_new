package com.xmpp.client.connect.packet;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

 
public class NotifyInfoIQProvider  implements IQProvider{

	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		// TODO Auto-generated method stub
		NotifyInfoIQ notification = new NotifyInfoIQ();
        for (boolean done = false; !done;) {
            int eventType = parser.next();
            
            if (eventType == XmlPullParser.START_TAG) {
                if ("id".equals(parser.getName())) {
                    notification.setId(parser.nextText());
                }
                 if ("title".equals(parser.getName())) {
                    notification.setTitle(parser.nextText());
                }
                if ("message".equals(parser.getName())) {
                    notification.setMessage(parser.nextText());
                }
                 
            } else if (eventType == XmlPullParser.END_TAG
                    && "notification".equals(parser.getName())) {
                done = true;
            }
        }

        return notification;
	}

}
