package com.prefect.chatserver.client;

import com.prefect.chatserver.client.handler.ChatClientHandler;
import com.prefect.chatserver.client.util.Config;
import com.prefect.chatserver.client.util.ServerInfo;
import com.prefect.chatserver.commoms.codefactory.ChatServerCodecFactory;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;


import java.net.InetSocketAddress;

/**
 * Created by zhangkai on 2016/12/23.
 */
public class ChatClient {
    private static final String HOSTNAME = "localhost";

    private static final int PORT = 9123;

    private static final long CONNECT_TIMEOUT = 30 * 1000L;

    private static final boolean USE_CUSTOM_CODEC = true;

    public static void main(String[] args) throws Throwable {
        Config config = new Config();
        ServerInfo serverInfo = config.getServerConf();

        NioSocketConnector connector = new NioSocketConnector();
        connector.setConnectTimeoutMillis(CONNECT_TIMEOUT);

        connector.getFilterChain().addLast("logger", new LoggingFilter());
        connector.getFilterChain().addLast("codec",
                new ProtocolCodecFilter(new ChatServerCodecFactory()));

        connector.setHandler(new ChatClientHandler());

        IoSession session;

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

        // wait until the summation is done
        session.getCloseFuture().awaitUninterruptibly();
        connector.dispose();
    }
}
