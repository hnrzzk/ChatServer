package com.prefect.chatserver.client.process.response;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.client.ChatClient;
import com.prefect.chatserver.client.utils.Interactive;
import com.prefect.chatserver.commoms.util.CommandType;
import com.prefect.chatserver.commoms.util.MessagePacket;
import com.prefect.chatserver.commoms.util.MessageType;
import com.prefect.chatserver.commoms.util.moudel.RelationShipMessage;

import java.util.Scanner;

/**
 * Created by zhangkai on 2016/12/30.
 */
public class FriendAddResponsePo implements ResponsePo {
    @Override
    public void process(MessagePacket messagePacket) {
        RelationShipMessage relationShipMessage = JSON.parseObject(messagePacket.getMessage(), RelationShipMessage.class);

        Interactive.getInstance().printlnToConsole("收到好友请求，对方账户：" + relationShipMessage.getUserAccount());
        Interactive.getInstance().printlnToConsole("接受:1 不接受:0");
        Scanner scanner = new Scanner(System.in);
        String command=scanner.nextLine();
        Interactive.getInstance().printlnToConsole(command);
        switch (command) {
            case "1":
                relationShipMessage.setAccept(true);
                Interactive.getInstance().printlnToConsole("已接受");
                break;
            case "0":
                relationShipMessage.setAccept(false);
                Interactive.getInstance().printlnToConsole("已拒绝");
                break;
        }
        String json = JSON.toJSONString(relationShipMessage);
        MessagePacket responseMessagePacket = new MessagePacket(CommandType.FRIEND_LIST_ADD_ACK,
                MessageType.RELATIONSHIP_MANAGE, json.getBytes().length, json);

        ChatClient.session.write(responseMessagePacket);
    }
}
