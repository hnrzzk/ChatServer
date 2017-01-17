package com.prefect.chatserver.server;

import com.prefect.chatserver.commoms.codefactory.ChatServerCodecFactory;
import com.prefect.chatserver.server.db.DBDao;
import com.prefect.chatserver.server.handle.ChatServerHandler;
import com.prefect.chatserver.server.utils.Config;
import com.prefect.chatserver.server.utils.LRUCache;
import com.prefect.chatserver.server.utils.ServerInfo;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LogLevel;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 聊天服务器启动类
 * Created by zhangkai on 2016/12/23.
 */
public class ChatServer {
    private final static Logger logger = LoggerFactory.getLogger(ChatServer.class);

    //聊天室信息 <聊天室名称，用户session列表>
    public static ConcurrentHashMap<String, CopyOnWriteArraySet<IoSession>> chatRoomInfo = new ConcurrentHashMap<>();
    //禁言表缓存<String account,Boolean isGag>
    public static LRUCache gagListCache;

    //黑名单缓存<String account,Vector blackList>
    public static LRUCache blackListCache;

    private SocketAcceptor acceptor;

    public SocketAcceptor getAcceptor() {
        if (null == acceptor) {
            acceptor = new NioSocketAcceptor();
        }
        return acceptor;
    }

    public boolean start() {
        logger.info("Start loading configuration file.");
        ServerInfo serverInfo = new ServerInfo();
        try {
            Config config = new Config();
            serverInfo = config.getServerConf();
            logger.info("Load configuration finished.");
        } catch (Exception e) {
            logger.warn("Load configuration failed. Use the default configuration:", e);
        }

        initCache(serverInfo);

        return initServer(serverInfo);
    }

    /**
     * 初始化缓存
     * @param serverInfo    配置信息
     */
    private void initCache(ServerInfo serverInfo) {
        int cacheSize = serverInfo.getCacheSize();
        gagListCache = new LRUCache(cacheSize);
        blackListCache = new LRUCache(cacheSize);
    }

    /**
     * 初始化mina框架
     * @param serverInfo    配置信息
     * @return
     */
    private boolean initServer(ServerInfo serverInfo) {
        DefaultIoFilterChainBuilder filterChainBuilder = getAcceptor().getFilterChain();

        LoggingFilter loggingFilter = new LoggingFilter();
        loggingFilter.setMessageReceivedLogLevel(LogLevel.INFO);
        loggingFilter.setMessageSentLogLevel(LogLevel.INFO);
        filterChainBuilder.addLast("logger", loggingFilter);

        filterChainBuilder.addLast("codec", new ProtocolCodecFilter(new ChatServerCodecFactory()));

        filterChainBuilder.addLast("threadPool", new ExecutorFilter());

        getAcceptor().setHandler(new ChatServerHandler());
        getAcceptor().getSessionConfig().setBothIdleTime(serverInfo.getIdleTime());
        getAcceptor().getSessionConfig().setReadBufferSize(serverInfo.getBufferSize());
        getAcceptor().getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, serverInfo.getTimeOut());
        try {
            SocketAddress socketAddress = new InetSocketAddress(InetAddress.getByName(serverInfo.getHostname()), serverInfo.getPort());
            logger.info("Bind address to：" + socketAddress.toString());
            getAcceptor().bind(socketAddress);
            return true;
        } catch (IOException e) {
            logger.error("Bind address error:", e);
            return false;
        }
    }

    public static void main(String[] argv) throws IOException {
        ChatServer chatServer = new ChatServer();
        if (chatServer.start()) {
            logger.info("Service start success!");
        } else {
            logger.error("Service start failed!");
        }
    }
}