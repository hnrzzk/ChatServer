package com.prefect.chatserver.server.process;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.commoms.util.CommandType;
import com.prefect.chatserver.commoms.util.MessagePacket;
import com.prefect.chatserver.commoms.util.MessageType;
import com.prefect.chatserver.commoms.util.moudel.ActionResponseMessage;
import com.prefect.chatserver.commoms.util.moudel.ChatMessage;
import com.prefect.chatserver.server.db.DBDao;
import com.prefect.chatserver.server.handle.ChatServerHandler;
import org.apache.mina.core.session.IoSession;

import java.util.Map;

/**
 * 发送广播消息的处理逻辑
 * Created by zhangkai on 2017/1/3.
 */
public class BroadcastPo extends ActionPo {
    @Override
    public void process(IoSession ioSession, MessagePacket messagePacket) {
        ChatMessage chatMessage = JSON.parseObject(messagePacket.getMessage(), ChatMessage.class);

        if (authorizationCheck(chatMessage.getSendAccount())) {
            broadcast(messagePacket);
        } else {    //如果没有权限，则返回没有权限
            ActionResponseMessage actionResponseMessage=new ActionResponseMessage();
            actionResponseMessage.setActionResult(false);
            actionResponseMessage.setMessage("对不起，您没有权限!");

            String json = JSON.toJSONString(actionResponseMessage);
            ioSession.write(new MessagePacket(CommandType.SEND_BROADCAST_ACK, MessageType.RESPONSE, json.getBytes().length, json));
        }

        }

    /**
     * 权限检查，是否是管理员
     * @param account
     * @return
     */
    boolean authorizationCheck(String account) {
        return DBDao.getInstance().authorityCheck(account);
    }

    /**
     * 向所有在线用户发送广播
     * @param messagePacket
     */
    void broadcast(MessagePacket messagePacket) {
         for (Map.Entry<String, IoSession> entry : ChatServerHandler.sessionMap.entrySet()) {
             messagePacket.setCommand(CommandType.SEND_BROADCAST_ACK);
             IoSession ioSession=entry.getValue();
            if (ioSession.isConnected()){
                ioSession.write(messagePacket);
            }
        }
    }
}
