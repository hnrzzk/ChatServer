package com.prefect.chatserver.server.process;

import com.prefect.chatserver.commoms.util.CommandType;
import com.prefect.chatserver.commoms.util.MessagePacket;
import com.prefect.chatserver.commoms.util.MessageType;
import com.prefect.chatserver.server.db.DBDao;
import com.prefect.chatserver.server.handle.ChatServerHandler;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by zhangkai on 2016/12/30.
 */
public class LogOutPo extends ActionPo {
    private final static Logger logger = LoggerFactory.getLogger(LogOutPo.class);

    @Override
    public void process(IoSession ioSession, MessagePacket messageObj) throws Exception {
        //从session中得到账号
        String account = ioSession.getAttribute(ChatServerHandler.attributeNameOfAccount).toString();

        //发送离线通知
        sendOfflineNotice(account);

        //修改用户离线状态
        if (!DBDao.getInstance().changeAccountOnlineStatus(account, 0)){
            logger.error("修改离线操作失败：修改数据库状态失败");
        }

        //修改sessionMap
        ChatServerHandler.sessionMap.remove(account);
    }

    /**
     * 发送用户下线通知
     *
     * @param
     */
    private void sendOfflineNotice(String account) {

        //根据得到该账号的在线好友列表
        List<String> accountList = DBDao.getInstance().getFriendLIst(account, 1);

        //生产消息字符串
        String message = new StringBuilder().append("您的好友").append(account).append("已下线").toString();

        //打包
        MessagePacket messagePacket = new MessagePacket(CommandType.USER_OFF_LINE_NOTICE, MessageType.STRING, message.getBytes().length, message);

        //发送通知
        sendNotice(accountList, messagePacket);
    }
}
