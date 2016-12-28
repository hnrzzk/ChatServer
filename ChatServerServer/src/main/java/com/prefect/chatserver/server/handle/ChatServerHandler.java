package com.prefect.chatserver.server.handle;

import com.prefect.chatserver.commoms.util.MessagePacket;
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
        logger.info(session.toString() + " 关闭连接");

//        long userId = Long.parseLong(session.getAttribute("userId").toString());
//
//        String sql = "update user set is_online=1 where id=" + userId;
//        DBUtil.getInstance().executeQuery(sql);
//        sessionMap.remove(userId);
//
//        //修改下线通知
//        String userSql = "Select * from user where id=" + userId;
//        UserInfo userInfo = DBDao.getInstance().getUserInfo(userSql);
//
//        String friendListSql = "select * from friends where user_id=" + userInfo.getId();
//        List<FriendInfo> friendInfoList = DBDao.getInstance().getFriendInfo(friendListSql);
//        for (FriendInfo item : friendInfoList) {
//            String fUserSql = "select * from user where id=" + item.getFriendId();
//            UserInfo userInfo1 = DBDao.getInstance().getUserInfo(fUserSql);
//            IoSession ioSession = sessionMap.get(userInfo1.getId());
//            if (ioSession != null) {
//                Message message = new Message();
//                Map<String, Object> objectMap = new HashMap<String, Object>();
//                objectMap.put("userId", userInfo1.getId());
//                objectMap.put("status", 1); //下线
//                String content = JSON.toJSONString(objectMap);
//                message.setPackageHeadLength(10);
//                message.setCommand(CommandType.USER_OFF_LINE_NOTICE);
//                message.setMessageLength(content.getBytes().length);
//                message.setMessage(content);
//                ioSession.write(message);
//            }
//        }
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

        MessageProcess messageProcess = MessagePoFactory.getClass(message.getCommand());

        if (messageProcess != null) {
            messageProcess.process(ioSession, message);
        }

    }
}
