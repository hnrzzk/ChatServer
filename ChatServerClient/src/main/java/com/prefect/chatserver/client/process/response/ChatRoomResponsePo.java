package com.prefect.chatserver.client.process.response;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.client.util.Interactive;
import com.prefect.chatserver.commoms.util.MessagePacket;
import com.prefect.chatserver.commoms.util.moudel.ChatRoomMessage;

/**
 * Created by zhangkai on 2017/1/3.
 */
public class ChatRoomResponsePo implements ResponsePo {
    @Override
    public void process(MessagePacket messagePacket) {
        ChatRoomMessage chatRoomMessage = JSON.parseObject(messagePacket.getMessage(), ChatRoomMessage.class);
        String account = chatRoomMessage.getAccount();
        String chatRoomName = chatRoomMessage.getChatRoomName();
        String message = chatRoomMessage.getMessage();

        printMessage(chatRoomName,account,message);
    }

    void printMessage(String chatRoomName,String account,String message){
        String string=new StringBuilder()
                .append(chatRoomName).append("--").append(account).append(":\n")
                .append(message)
                .toString();

        Interactive.getInstance().printlnToConsole(string);
    }
}
