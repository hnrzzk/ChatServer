package com.prefect.chatserver.server.handle;

import com.prefect.chatserver.commoms.util.MessagePacket;
import com.prefect.chatserver.server.util.db.DBUtil;
import com.prefect.chatserver.server.process.MessageProcess;
import com.prefect.chatserver.server.process.MessagePoFactory;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhangkai on 2016/12/26.
 */
public class ChatServerHandler extends IoHandlerAdapter {
    private final static Logger logger = LoggerFactory.getLogger(ChatServerHandler.class);

    public static Map<String, IoSession> sessionMap = new ConcurrentHashMap<String, IoSession>();

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
        String account = session.getAttribute("account").toString();
        logger.info(account + " 关闭连接");

        String sql = "update user set is_online= 0 where account=?";
        DBUtil.getInstance().executeUpdate(sql, new Object[]{account});
        sessionMap.remove(account);

        //TODO:下线通知
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        logger.info(session.toString() + " 进入空闲");
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        logger.warn(session.toString() + "异常");
        session.closeNow();
    }

    @Override
    public void messageReceived(IoSession ioSession, Object messageObj) throws Exception {
        logger.info(ioSession.toString() + " get message:" + messageObj);

        MessagePacket message = null;
        if (messageObj instanceof MessagePacket) {
            message = (MessagePacket) messageObj;
        } else {
            logger.error("获取message失败 session:" + ioSession.toString());
            return;
        }

        //根据message从工厂类生产对应的处理类
        MessageProcess messageProcess = MessagePoFactory.getClass(message.getCommand());

        if (messageProcess != null) {
            messageProcess.process(ioSession, message);
        }

    }
}
