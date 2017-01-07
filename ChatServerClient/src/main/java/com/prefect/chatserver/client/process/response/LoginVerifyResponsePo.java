package com.prefect.chatserver.client.process.response;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.client.process.interactive.UserInteractive;
import com.prefect.chatserver.client.utils.Interactive;
import com.prefect.chatserver.commoms.utils.MessagePacket;
import com.prefect.chatserver.commoms.utils.moudel.ACKMessage;


/**
 * Created by hnrzz on 2017/1/7.
 */
public class LoginVerifyResponsePo implements ResponsePo{
    @Override
    public void process(MessagePacket messagePacket) {

        ACKMessage ackMessage = JSON.parseObject(messagePacket.getMessage(),ACKMessage.class);
        Interactive.getInstance().printlnToConsole(String.format("System:\n    %s", ackMessage.getMessage()));

        if (ackMessage.getActionResult()){
            Thread thread=new Thread(new UserInteractive());
            thread.start();
        }
    }
}
