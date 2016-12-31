package com.prefect.chatserver.client.process.response;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.client.util.Interactive;
import com.prefect.chatserver.commoms.util.MessagePacket;
import com.prefect.chatserver.commoms.util.MessageType;
import com.prefect.chatserver.commoms.util.moudel.ActionResponseMessage;

/**
 * 服务器响应处理逻辑
 * Created by zhangkai on 2016/12/29.
 */
public class ActionResponsePo implements ResponsePo{


    @Override
    public void process(MessagePacket messagePacket) {
        switch (messagePacket.getMessageType()){
            case MessageType.RESPONSE:
                ActionResponseMessage actionResponseMessage = JSON.parseObject(messagePacket.getMessage(),ActionResponseMessage.class);
                Interactive.getInstance().printlnToConsole(String.format("System:\n%s", actionResponseMessage.getMessage()));
                break;
            case MessageType.STRING:
                Interactive.getInstance().printlnToConsole("System:"+messagePacket.getMessage().toString());
                break;
        }

    }
}
