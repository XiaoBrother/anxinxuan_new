package com.xmpp.client.connect;

 
import java.util.Map;

import org.jivesoftware.smack.XMPPConnection;
 

 import com.axinxuandroid.activity.handler.ConfirmDialogHandlerMethod;
 import com.axinxuandroid.activity.handler.NcpzsHandler;
import com.axinxuandroid.activity.handler.OnHandlerFinishListener;
import com.axinxuandroid.sys.gloable.Gloable;
 import com.xmpp.client.connect.task.ConnectTask;
import com.xmpp.client.connect.task.Task;
import com.xmpp.client.connect.task.TaskFinishEventHandler;
import com.xmpp.client.connect.task.TaskManager;
import com.xmpp.client.connect.task.TaskResult;

/**
 * �����������ӹ���
 * @author hubobo
 *
 */
public class ServerConnection   {
	private static  ServerConnection serverconnect;
 	private XMPPConnection connection;
 	private boolean isconnect=false;
 	private boolean userAgreeConnect=true;
  	private ServerConnection(){
 		
 	}
 	public static ServerConnection getInstance(){
 		if(serverconnect==null)
 			serverconnect=new ServerConnection();
 		return serverconnect;
 	}
 	public boolean  isConnect(){
 		if(connection!=null)
 			return connection.isConnected();
 		return false;
 	}
	public XMPPConnection getConnection() {
		return connection;
	}
	public synchronized void setConnection(XMPPConnection connection) {
		if(connection!=null){
		 this.connection = connection;
 		}
	}
	
	public boolean isAuthenticated() {//�Ƿ��¼��
	        return connection != null && connection.isConnected()
	                && connection.isAuthenticated();
	}
 	/**
 	 * ����Զ�̷�����,����ʧ�ܺ�ᵯ���Ի������û�ѡ���Ƿ��������
  	 */
	public synchronized void  connectServer(){
		if(isconnect) return  ;
		if(connection==null||!connection.isConnected()&&userAgreeConnect){
		 isconnect=true;
 		 Task connect=new ConnectTask();
		 connect.setTaskFinishEventHandler(new TaskFinishEventHandler() {
				@Override
				public void onfinish(TaskResult result) {
 					if(result.getResult()!=TaskResult.RESULT_OK){
 						NcpzsHandler handler=Gloable.getInstance().getCurHandler();
 						handler.setOnHandlerFinishListener(new OnHandlerFinishListener() {
 							@Override
							public void onHandlerFinish(Object result) {
 							  isconnect=false;
 							  Map rdata=(Map) result;
							  Integer tresult=(Integer) rdata.get("result");
							  if(tresult==1){
								  ServerConnection.this.connectServer();
							  }else {
 								  userAgreeConnect=false;
							  }
							  
 							}
						});
 						handler.excuteMethod(new ConfirmDialogHandlerMethod("����ʧ��","�������ӷ�������","����","ȡ��"));
 					}
						 
 				}
			 });
		 TaskManager.getInstance().addTask(connect);
		}
	}
	
	/**
 	 * ��������Զ�̷�����
 	 * @param handler ��������ɵ��¼�ί�д���
 	 */
	public   void reconnectServer(TaskFinishEventHandler handler){
		closeConnect();
 		connectServer();
	}
	/**
	 * �ر�����
	 */
	public synchronized void closeConnect(){
		if(connection!= null && connection.isConnected()){
 			connection.disconnect();//�ر�����
 			connection=null;
 		}
		isconnect=false;
	}
}
