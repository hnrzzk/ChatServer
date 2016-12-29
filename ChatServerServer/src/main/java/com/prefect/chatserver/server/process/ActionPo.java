package com.prefect.chatserver.server.process;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.commoms.util.CommandType;
import com.prefect.chatserver.commoms.util.MessagePacket;
import com.prefect.chatserver.commoms.util.MessageType;
import com.prefect.chatserver.commoms.util.moudel.ActionResponseMessage;
import com.prefect.chatserver.commoms.util.moudel.ChatMessage;
import org.apache.mina.core.session.IoSession;

/**
 * 抽象类：处理客户端请求的
 * Created by zhangkai on 2016/12/28.
 */
public abstract class ActionPo implements MessageProcess {

    /**
     * 服务器向客户端发送响应消息的逻辑
     * @param ioSession 连接
     * @param commandType   命令类型
     * @param result    请求执行结果
     * @param message   详细信息
     */
    void response(IoSession ioSession, int commandType, boolean result, String message) {
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
