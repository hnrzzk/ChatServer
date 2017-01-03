package com.prefect.chatserver.client.process.request.operate;

import com.prefect.chatserver.client.util.Interactive;

/**
 * Created by zhangkai on 2017/1/3.
 */
public class OperateFactory {
    public static OperatePo getClass(String[] strings){
        switch (strings[0]) {
            case InteractiveCommandType.TALK:
                return new SendMessagePo(strings);
            case InteractiveCommandType.FRIEND_MANAGE:
                return new FriendManagePo(strings);
            case InteractiveCommandType.BLACK_LIST_MANAGE:
                return new BlackListManagePo(strings);
            case InteractiveCommandType.ChAT_ROOM_MANAGE:
                return new ChatRoomManagePo(strings);
            case InteractiveCommandType.BROADCAST:
                return new BroadCastPo(strings);
            default:
                printHelpInfo();
                return null;
        }
    }

    /**
     * 帮助信息
     */
    private static void printHelpInfo() {
        String string = new StringBuilder()
                .append("-friend 好友管理\n")
                .append("-blackList 黑名单管理\n")
                .append("-talk [userAccount] [message] 聊天\n")
                .append("-chatRoom 聊天室\n" )
                .append("-broadcast 广播\n")
                .toString();
        Interactive.getInstance().printlnToConsole(string);
    }

    class InteractiveCommandType {
        final static String FRIEND_MANAGE = "-friend";
        final static String BLACK_LIST_MANAGE = "-blackList";
        final static String TALK = "-talk";
        final static String ChAT_ROOM_MANAGE = "-chatRoom";
        final static String BROADCAST = "-broadcast";
    }
}


