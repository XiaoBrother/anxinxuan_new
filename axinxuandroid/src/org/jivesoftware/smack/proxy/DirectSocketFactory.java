/*    */ package org.jivesoftware.smack.proxy;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.InetAddress;
/*    */ import java.net.InetSocketAddress;
/*    */ import java.net.Proxy;
/*    */ import java.net.Socket;
/*    */ import java.net.UnknownHostException;
import javax.net.SocketFactory;

import com.ncpzs.util.LogUtil;
/*    */ 
/*    */ class DirectSocketFactory extends SocketFactory
/*    */ {
	        private static final int CONNECT_TIMEOUT=10*1000;//默认连接超时时间
/*    */   public Socket createSocket(String host, int port)
/*    */     throws IOException, UnknownHostException
/*    */   {
  /* 27 */     Socket newSocket = new Socket(Proxy.NO_PROXY);
/* 28 */     newSocket.connect(new InetSocketAddress(host, port),CONNECT_TIMEOUT);
/* 29 */     return newSocket;
/*    */   }
/*    */ 
/*    */   public Socket createSocket(String host, int port, InetAddress localHost, int localPort)
/*    */     throws IOException, UnknownHostException
/*    */   {
/* 36 */     return new Socket(host, port, localHost, localPort);
/*    */   }
/*    */ 
/*    */   public Socket createSocket(InetAddress host, int port)
/*    */     throws IOException
/*    */   {
/* 42 */     Socket newSocket = new Socket(Proxy.NO_PROXY);
/* 43 */     newSocket.connect(new InetSocketAddress(host, port),CONNECT_TIMEOUT);
/* 44 */     return newSocket;
/*    */   }
/*    */ 
/*    */   public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort)
/*    */     throws IOException
/*    */   {
/* 51 */     return new Socket(address, port, localAddress, localPort);
/*    */   }
/*    */ }

/* Location:           E:\eclipse\eclipseworder\ncpzs\lib\asmack.jar
 * Qualified Name:     org.jivesoftware.smack.proxy.DirectSocketFactory
 * JD-Core Version:    0.6.0
 */