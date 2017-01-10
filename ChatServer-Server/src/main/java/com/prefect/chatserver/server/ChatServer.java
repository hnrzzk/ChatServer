package com.prefect.chatserver.server;

import com.prefect.chatserver.commoms.codefactory.ChatServerCodecFactory;
import com.prefect.chatserver.server.handle.ChatServerHandler;
import com.prefect.chatserver.server.utils.Config;
import com.prefect.chatserver.server.utils.ServerInfo;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LogLevel;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;

/**
 * 聊天服务器启动类
 * Created by zhangkai on 2016/12/23.
 */
public class ChatServer {
    private final static Logger logger = LoggerFactory.getLogger(ChatServer.class);

    //聊天室信息 <聊天室名称，用户session列表>
    public static ConcurrentHashMap<String, CopyOnWriteArraySet<IoSession>> chatRoomInfo = new ConcurrentHashMap<>();

    private SocketAcceptor acceptor;

    public SocketAcceptor getAcceptor() {
        if (null == acceptor) {
            acceptor = new NioSocketAcceptor();
        }
        return acceptor;
    }

    public boolean start() {
        ServerInfo serverInfo = new ServerInfo();
        try {
            Config config = new Config();
            serverInfo = config.getServerConf();
        } catch (Exception e) {
            logger.warn("读取配置文件失败，启用默认设置. ", e);
        }

        DefaultIoFilterChainBuilder filterChainBuilder = getAcceptor().getFilterChain();

        LoggingFilter loggingFilter = new LoggingFilter();
        loggingFilter.setMessageReceivedLogLevel(LogLevel.INFO);
        loggingFilter.setMessageSentLogLevel(LogLevel.INFO);
        filterChainBuilder.addLast("logger", loggingFilter);

        filterChainBuilder.addLast("codec", new ProtocolCodecFilter(new ChatServerCodecFactory()));

//        filterChainBuilder.addLast("threadPool", new ExecutorFilter());

        getAcceptor().setHandler(new ChatServerHandler());
        getAcceptor().getSessionConfig().setBothIdleTime(serverInfo.getIdleTime());
        getAcceptor().getSessionConfig().setReadBufferSize(serverInfo.getBufferSize());
        getAcceptor().getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, serverInfo.getTimeOut());
        try {
            getAcceptor().bind(new InetSocketAddress(serverInfo.getPort()));
            return true;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return true;
    }

    public static void main(String[] argv) throws IOException {
        ChatServer chatServer = new ChatServer();
        if (chatServer.start()) {
            logger.info("服务器启动成功。。。");
        } else {
            logger.error("服务器启动失败");
        }
    }
}