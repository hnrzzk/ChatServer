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
import com.prefect.chatserver.commoms.util.moudel.FriendManageMessage;
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

        switch (strings[0]) {
            case InteractiveCommandType.TALK:
                new SendMessagePo().requestSendMessage(strings);
                break;
            case InteractiveCommandType.FRIEND_Manage:
                new FriendManagePo().manageFriend(strings);
                break;
        }
    }
}

class InteractiveCommandType {
    final static String FRIEND_Manage = "-friend";
    final static String TALK = "-talk";
}
