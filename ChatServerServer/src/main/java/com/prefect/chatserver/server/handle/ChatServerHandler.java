package com.prefect.chatserver.server.handle;

import com.prefect.chatserver.commoms.util.MessagePacket;
import com.prefect.chatserver.server.process.LogOutPo;
import com.prefect.chatserver.server.process.MessageProcess;
import com.prefect.chatserver.server.process.MessagePoFactory;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zhangkai on 2016/12/26.
 */
public class ChatServerHandler extends IoHandlerAdapter {
    private final static Logger logger = LoggerFactory.getLogger(ChatServerHandler.class);


    public static Map<String, IoSession> sessionMap = new ConcurrentHashMap<String, IoSession>();

    private ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        logger.info(session.toString() + "创建连接");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        logger.info(session.toString() + "打开链接");
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        logger.info(session.toString() + "关闭连接");

        MultiThreadHandler multiThreadHandler = new MultiThreadHandler();
        multiThreadHandler.setIoSession(session);
        multiThreadHandler.setMessagePacket(null);
        multiThreadHandler.setMessageProcess(new LogOutPo());
        cachedThreadPool.execute(multiThreadHandler);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
//        logger.info(session.toString() + " 进入空闲");
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        logger.warn(session.toString() + "异常");
//        session.closeNow();
    }

    @Override
    public void messageReceived(IoSession ioSession, Object messageObj) throws Exception {

        logger.info(ioSession.toString() + " 接收到数据:" + messageObj);

        MessagePacket message = null;
        if (messageObj instanceof MessagePacket) {
            message = (MessagePacket) messageObj;
        } else {
            logger.error("获取message失败 session:" + ioSession.toString());
            return;
        }

        //根据message从工厂类生产对应的处理类
        MessageProcess messageProcess = MessagePoFactory.getClass(message.getCommand());

        MultiThreadHandler multiThreadHandler = new MultiThreadHandler();
        multiThreadHandler.setIoSession(ioSession);
        multiThreadHandler.setMessagePacket(message);
        multiThreadHandler.setMessageProcess(messageProcess);
        cachedThreadPool.execute(multiThreadHandler);
    }
}
