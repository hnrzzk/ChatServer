package com.prefect.chatserver.client.process.interactive;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.client.ChatClient;
import com.prefect.chatserver.client.process.request.FriendManagePo;
import com.prefect.chatserver.client.process.request.SendMessagePo;
import com.prefect.chatserver.client.util.Interactive;
import com.prefect.chatserver.commoms.util.CommandType;
import com.prefect.chatserver.commoms.util.MessagePacket;
import com.prefect.chatserver.commoms.util.MessageType;
import com.prefect.chatserver.commoms.util.moudel.ChatMessage;
import com.prefect.chatserver.commoms.util.moudel.FriendInfo;
import com.prefect.chatserver.commoms.util.moudel.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;


/**
 * 处理用户控制台输入的逻辑
 * Created by zhangkai on 2016/12/29.
 */
public class UserInteractive implements Runnable {
    private final static Logger logger = LoggerFactory.getLogger(UserInteractive.class);

    {
        Interactive.getInstance().printlnToConsole("开始接受用户命令:");
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String inputStr = scanner.nextLine();

            try {
                parseCommand(inputStr);
            } catch (Exception e) {
                logger.error("command is illegal", e);
            }
        }
    }

    /**
     * 处理用户输入的命令
     *
     * @param userInput
     */
    void parseCommand(String userInput) throws Exception {
        String[] strings = userInput.split(" ");

        switch (getCommandType(strings[0])) {
            case InteractiveCommandType.TALK:
                processTalk(strings[1], strings[2]);
                break;
            case InteractiveCommandType.FRIEND_ADD:
                processFriendAdd(strings[1]);
                break;
        }
    }

    /**
     * 根据用户输入获取命令类型
     *
     * @param userInput
     * @return
     */
    private String getCommandType(String userInput) {
        return userInput;
    }

    /**
     * 处理用户输入的聊天命令
     * @param receiveAccount
     * @param message
     */
    private void processTalk(String receiveAccount, String message) {
        MessagePacket messagePacket = new MessagePacket();
        messagePacket.setCommand(CommandType.MESSAGE);
        messagePacket.setMessageType(MessageType.MESSAGE);

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSendAccount(ChatClient.account);
        chatMessage.setReceiveAccount(receiveAccount);
        chatMessage.setMessage(message);

        String json = JSON.toJSONString(chatMessage);
        messagePacket.setMessage(json);
        messagePacket.setMessageLength(json.getBytes().length);

        new SendMessagePo().process(ChatClient.session, messagePacket);
    }

    private  void processFriendAdd(String receiveAccount){

        FriendInfo friendInfo=new FriendInfo();
        friendInfo.setUserAccount(ChatClient.account);
        friendInfo.setFriendAccount(receiveAccount);
        friendInfo.setCategoryName("好友");

        String json= JSON.toJSONString(friendInfo);

        MessagePacket messagePacket=new MessagePacket(
                CommandType.FRIEND_LIST_ADD,
                MessageType.FRIEND_MANAGE,
                json.getBytes().length,
                json);

        new FriendManagePo().process(ChatClient.session, messagePacket);


    }
}

class InteractiveCommandType {
    final static String FRIEND_FIND="-friendFind";    //查找好友
    final static String FRIEND_ADD="-friendAdd";     //增加好友
    final static String FRIEND_REMOVE="-friendRemove";  //
    final static String TALK="-talk";
}
