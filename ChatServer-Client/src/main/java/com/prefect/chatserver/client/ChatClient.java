package com.prefect.chatserver.client;

import com.prefect.chatserver.client.handler.ChatClientHandler;
import com.prefect.chatserver.client.utils.Config;
import com.prefect.chatserver.client.utils.Interactive;
import com.prefect.chatserver.client.utils.ServerInfo;
import com.prefect.chatserver.commoms.codefactory.ChatServerCodecFactory;

import com.prefect.chatserver.commoms.utils.AttributeOperate;
import com.prefect.chatserver.commoms.utils.MathUtil;
import com.prefect.chatserver.commoms.utils.RSA;
import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;

/**
 * 客户端类
 * Created by zhangkai on 2016/12/23.
 */
public class ChatClient {
    private static Logger logger = LoggerFactory.getLogger(ChatClient.class);

    private final String HOSTNAME = "localhost";
    private final int PORT = 9123;
    private final long CONNECT_TIMEOUT = 30 * 1000L;
    private final boolean USE_CUSTOM_CODEC = true;

    //配置文件文件夹路径
    private String configDirPath;

    /**
     * 与服务器的连接
     */
    public static IoSession session;

    /**
     * 账户名
     */
    public static String account = "";

    /**
     * 聊天室名称
     */
    public static String chatRoomName = "";

    public ChatClient() {

    }

    /**
     * 客户端类构造函数
     *
     * @param configDirPath 配置文件文件夹路径
     */
    public ChatClient(String configDirPath) {
        this.configDirPath = configDirPath;
    }


    synchronized public static String getAccount() {
        return account;
    }

    synchronized public static void setAccount(String account) {
        ChatClient.account = account;
    }

    private void start() throws InterruptedException {
        Interactive.getInstance().printlnToConsole("程序启动……");
        Interactive.getInstance().printlnToConsole("正在读取配置文件……");

        Config config;
        if (configDirPath != null) {
            config = new Config(configDirPath);
        } else {
            config = new Config();
        }

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
                SocketAddress socketAddress = new InetSocketAddress(serverInfo.getHostName(), serverInfo.getPort());
                ConnectFuture future = connector.connect(socketAddress);
                future.awaitUninterruptibly();
                this.session = future.getSession();
                //在session中存储密钥
                setKeyPair(session);

                break;
            } catch (Exception e) {
                logger.error("Connection server failed:" + e.getMessage());
                Thread.sleep(1000);
            }
        }
    }

    /**
     * 在session中设置RSA密钥
     *
     * @param session
     */
    private void setKeyPair(IoSession session) throws Exception {
        Map<String, Object> keyMap = RSA.genKeyPair();
        String publicKey = RSA.getPublicKey(keyMap);
        String privateKey = RSA.getPrivateKey(keyMap);
        AttributeOperate.getInstance().setPubKey(session, publicKey);
        AttributeOperate.getInstance().setPrivKey(session, privateKey);

    }


    public static void main(String[] args) throws Throwable {
        ChatClient clientInstance;
        if (args.length > 0) {
            clientInstance = new ChatClient(args[0]);
        } else {
            clientInstance = new ChatClient();
        }

        clientInstance.start();
    }

}
