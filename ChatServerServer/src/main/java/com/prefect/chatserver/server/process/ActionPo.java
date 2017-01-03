package com.prefect.chatserver.server.process;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.commoms.util.CommandType;
import com.prefect.chatserver.commoms.util.MessagePacket;
import com.prefect.chatserver.commoms.util.MessageType;
import com.prefect.chatserver.commoms.util.moudel.ActionResponseMessage;
import com.prefect.chatserver.commoms.util.moudel.ChatMessage;
import com.prefect.chatserver.server.handle.ChatServerHandler;
import org.apache.mina.core.session.IoSession;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象类：处理客户端请求的
 * Created by zhangkai on 2016/12/28.
 */
public abstract class ActionPo implements MessageProcess {

    /**
     * 服务器向客户端发送响应消息的逻辑
     *
     * @param ioSession   连接
     * @param commandType 命令类型
     * @param result      请求执行结果
     * @param message     详细信息
     */
    void response(IoSession ioSession, int commandType, boolean result, String message) {
        if (ioSession != null) {
            MessagePacket messagePacket = new MessagePacket();
            messagePacket.setMessageType(MessageType.RESPONSE);
            messagePacket.setCommand(commandType);

            ActionResponseMessage actionResponseMessage = new ActionResponseMessage();
            actionResponseMessage.setActionResult(result);
            actionResponseMessage.setMessage(message);

            String json = JSON.toJSONString(actionResponseMessage);
            messagePacket.setMessage(json);
            messagePacket.setMessageLength(json.getBytes().length);

            ioSession.write(messagePacket);
        }
    }

    /**
     * 向用户发送通知
     *
     * @param accountList   需要发送通知的用户列表
     * @param messagePacket 发送的消息
     */
    void sendNotice(List<String> accountList, MessagePacket messagePacket) {
        for (IoSession itemSession : getSession(accountList)) {
            if (itemSession != null) {
                itemSession.write(messagePacket);
            }
        }
    }

    /**
     * 根据用户名列表获取当前活动的Iosession
     *
     * @param accountList
     */
    List<IoSession> getSession(List<String> accountList) {
        //当前活动的IoSession列表
        List<IoSession> activeSessionList = new ArrayList<>();

        //遍历用户名列表，根据用户名从sessionMap中获取session，如果不为null，则加入list中
        for (String item : accountList) {
            IoSession tempSession = ChatServerHandler.sessionMap.get(item);
            if (tempSession != null) {
                activeSessionList.add(tempSession);
            }
        }
        return activeSessionList;
    }
}
