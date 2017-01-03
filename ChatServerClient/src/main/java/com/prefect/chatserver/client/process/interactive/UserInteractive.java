package com.prefect.chatserver.client.process.interactive;

import com.prefect.chatserver.client.process.request.BlackListManagePo;
import com.prefect.chatserver.client.process.request.ChatRoomManagePo;
import com.prefect.chatserver.client.process.request.FriendManagePo;
import com.prefect.chatserver.client.process.request.SendMessagePo;
import com.prefect.chatserver.client.util.Interactive;
import com.prefect.chatserver.commoms.util.moudel.ChatRoomMessage;
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
     * @param userInput 用户在控制台的输入
     */
    void parseCommand(String userInput) throws Exception {
        String[] strings = userInput.split(" ");

        switch (strings[0]) {
            case InteractiveCommandType.TALK:
                new SendMessagePo().requestSendMessage(strings);
                break;
            case InteractiveCommandType.FRIEND_MANAGE:
                new FriendManagePo().manageFriend(strings);
                break;
            case InteractiveCommandType.BLACK_LIST_MANAGE:
                new BlackListManagePo().manageFriend(strings);
                break;
            case InteractiveCommandType.Chat_ROOM_MANAGE:
                new ChatRoomManagePo().process(strings);
                break;
            default:
                printHelpInfo();
        }
    }

    /**
     * 帮助信息
     */
    private void printHelpInfo() {
        String string = new StringBuilder()
                .append("-friend 好友管理\n")
                .append("-blackList 黑名单管理\n")
                .append("-talk userAccount message 聊天\n")
                .append("-chatRoom 聊天室" )
                .toString();
        Interactive.getInstance().printlnToConsole(string);
    }
}

class InteractiveCommandType {
    final static String FRIEND_MANAGE = "-friend";
    final static String BLACK_LIST_MANAGE = "-blackList";
    final static String TALK = "-talk";
    final static String Chat_ROOM_MANAGE = "-chatRoom";
}
