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
 * 服务器端连接管理
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
	
	public boolean isAuthenticated() {//是否登录过
	        return connection != null && connection.isConnected()
	                && connection.isAuthenticated();
	}
 	/**
 	 * 连接远程服务器,连接失败后会弹出对话框让用户选择是否继续连接
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
 						handler.excuteMethod(new ConfirmDialogHandlerMethod("连接失败","重新连接服务器？","连接","取消"));
 					}
						 
 				}
			 });
		 TaskManager.getInstance().addTask(connect);
		}
	}
	
	/**
 	 * 重新连接远程服务器
 	 * @param handler ，连接完成的事件委托处理
 	 */
	public   void reconnectServer(TaskFinishEventHandler handler){
		closeConnect();
 		connectServer();
	}
	/**
	 * 关闭连接
	 */
	public synchronized void closeConnect(){
		if(connection!= null && connection.isConnected()){
 			connection.disconnect();//关闭连接
 			connection=null;
 		}
		isconnect=false;
	}
}
