package com.prefect.chatserver.client.process.response;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.client.ChatClient;
import com.prefect.chatserver.client.process.request.account.AccountManagePo;
import com.prefect.chatserver.client.utils.Interactive;
import com.prefect.chatserver.commoms.utils.MessagePacket;
import com.prefect.chatserver.commoms.utils.moudel.ACKMessage;

import java.sql.ResultSet;

/**
 * Created by zhangkai on 2017/1/12.
 */
public class SignInResponsePo implements ResponsePo {
    @Override
    public void process(MessagePacket messagePacket) {
        ACKMessage ackMessage = JSON.parseObject(messagePacket.getMessage(), ACKMessage.class);
        Interactive.getInstance().printlnToConsole(String.format("System:\n    %s", ackMessage.getMessage()));

        new AccountManagePo().manangeUser(ChatClient.session);

    }
}
