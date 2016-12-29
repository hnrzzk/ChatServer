package com.prefect.chatserver.client;

import com.prefect.chatserver.client.handler.ChatClientHandler;
import com.prefect.chatserver.client.util.Config;
import com.prefect.chatserver.client.util.Interactive;
import com.prefect.chatserver.client.util.ServerInfo;
import com.prefect.chatserver.commoms.codefactory.ChatServerCodecFactory;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;


import java.net.InetSocketAddress;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 客户端类
 * Created by zhangkai on 2016/12/23.
 */
public class ChatClient {

    public static CopyOnWriteArrayList<String> friendList;

    private final String HOSTNAME = "localhost";
    private final int PORT = 9123;
    private final long CONNECT_TIMEOUT = 30 * 1000L;
    private final boolean USE_CUSTOM_CODEC = true;

    /**
     * 与服务器的连接
     */
    public static IoSession session;

    public static String account="";

    synchronized public static String getAccount() {
        return account;
    }

    synchronized public static void setAccount(String account) {
        ChatClient.account = account;
    }

    public void start() throws InterruptedException {
        Interactive.getInstance().printlnToConsole("程序启动……");
        friendList=new CopyOnWriteArrayList<>();

        Interactive.getInstance().printlnToConsole("正在读取配置文件……");
        Config config = new Config();
        ServerInfo serverInfo = config.getServerConf();

        Interactive.getInstance().printlnToConsole("正在初始化连接……");
        NioSocketConnector connector = new NioSocketConnector();
        connector.setConnectTimeoutMillis(CONNECT_TIMEOUT);

        connector.getFilterChain().addLast("logger", new LoggingFilter());
        connector.getFilterChain().addLast("codec",
                new ProtocolCodecFilter(new ChatServerCodecFactory()));

        connector.setHandler(new ChatClientHandler());



        Interactive.getInstance().printlnToConsole("正在连接服务器……");
        while (true) {
            try {
                ConnectFuture future = connector.connect(new InetSocketAddress(serverInfo.getHostName(), serverInfo.getPort()));
                future.awaitUninterruptibly();
                session = future.getSession();
                break;
            } catch (RuntimeIoException e) {
                System.err.println("Failed to connect.");
                e.printStackTrace();
                Thread.sleep(5000);
            }
        }

        // Wait until the connection is closed or the connection attempt fails.
        session.getCloseFuture().awaitUninterruptibly();
        connector.dispose();
    }


    public static void main(String[] args) throws Throwable {
        ChatClient clientInstance=new ChatClient();
        clientInstance.start();
    }

}
