package com.prefect.chatserver.client;

import com.prefect.chatserver.client.handler.RobotHandler;
import com.prefect.chatserver.commoms.codefactory.ChatServerCodecFactory;
import com.prefect.chatserver.commoms.util.AttributeOperate;
import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhangkai on 2017/1/4.
 */
public class Robot {
    public static ConcurrentHashMap<String, IoSession> sessionConcurrentHashMap;

    private final String HOSTNAME = "localhost";
    private final int PORT = 9123;
    private final long CONNECT_TIMEOUT = 30 * 1000L;
    private final boolean USE_CUSTOM_CODEC = true;

    NioSocketConnector connector;
    int connectNum;

    public Robot() {
        this(100);
    }

    public Robot(int connectNum) {
        sessionConcurrentHashMap = new ConcurrentHashMap<>();

        this.connector = new NioSocketConnector();
        this.connector.setConnectTimeoutMillis(CONNECT_TIMEOUT);
        this.connector.getFilterChain().addLast("logger", new LoggingFilter());
        this.connector.getFilterChain().addLast("codec",
                new ProtocolCodecFilter(new ChatServerCodecFactory()));
        this.connector.setHandler(new RobotHandler());

        this.connectNum = connectNum;
    }

    public void start() {
        getConnect();
    }

    private void getConnect() {
        for (int i = 0; i < connectNum; i++) {

            while (true) {
                try {
                    ConnectFuture future = connector.connect(new InetSocketAddress(HOSTNAME, PORT));
                    future.awaitUninterruptibly();
                    String account = String.format("%04d", i);
                    IoSession session = future.getSession();
                    System.out.println("get session: "+session);
                    AttributeOperate.getInstance().setAccountOfAttribute(session, account);
                    this.sessionConcurrentHashMap.put(account, session);
                    break;
                } catch (RuntimeIoException e) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Robot robot = new Robot(5000);
        robot.start();
    }
}
