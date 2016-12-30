package com.prefect.chatserver.client.process.response;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.client.ChatClient;
import com.prefect.chatserver.client.util.Interactive;
import com.prefect.chatserver.commoms.util.CommandType;
import com.prefect.chatserver.commoms.util.MessagePacket;
import com.prefect.chatserver.commoms.util.MessageType;
import com.prefect.chatserver.commoms.util.moudel.FriendManageMessage;

import java.util.Scanner;

/**
 * Created by zhangkai on 2016/12/30.
 */
public class FriendAddResponsePo implements ResponsePo {
    @Override
    public void process(MessagePacket messagePacket) {
        FriendManageMessage friendManageMessage = JSON.parseObject(messagePacket.getMessage(), FriendManageMessage.class);

        Interactive.getInstance().printlnToConsole("收到好友请求，对方账户：" + friendManageMessage.getUserAccount());
        Interactive.getInstance().printlnToConsole("接受:1 不接受:0");
        Scanner scanner = new Scanner(System.in);
        String command=scanner.nextLine();
        Interactive.getInstance().printlnToConsole(command);
        switch (command) {
            case "1":
                friendManageMessage.setAccept(true);
                Interactive.getInstance().printlnToConsole("已接受");
                break;
            case "0":
                friendManageMessage.setAccept(false);
                Interactive.getInstance().printlnToConsole("已拒绝");
                break;
        }
        String json = JSON.toJSONString(friendManageMessage);
        MessagePacket responseMessagePacket = new MessagePacket(CommandType.FRIEND_LIST_ADD_ACK,
                MessageType.FRIEND_MANAGE, json.getBytes().length, json);

        ChatClient.session.write(responseMessagePacket);
    }
}
