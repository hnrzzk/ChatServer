package com.prefect.chatserver.client.process.request;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.client.ChatClient;
import com.prefect.chatserver.client.util.Interactive;
import com.prefect.chatserver.commoms.util.CommandType;
import com.prefect.chatserver.commoms.util.MessagePacket;
import com.prefect.chatserver.commoms.util.MessageType;
import com.prefect.chatserver.commoms.util.moudel.RelationShipMessage;

/**
 * 黑名单管理请求逻辑
 * Created by zhangkai on 2017/1/3.
 */
public class BlackListManagePo {
    public final String addFriend="add";
    public final String removeFriend="remove";

    public void manageFriend(String[] commands) {
        try {
            String command = commands[1];
            String userAccount = commands[2];

            switch (command) {
                case addFriend:
                    //-blackList add userAccount
                    requestAddBlack(userAccount);
                    break;
                case removeFriend:
                    //-blackList remove userAccount
                    requestRemoveBlack(userAccount);
                    break;
                default:
                    printHelpInfo();
            }
        }catch (ArrayIndexOutOfBoundsException e){
            Interactive.getInstance().printlnToConsole("命令错误");
            printHelpInfo();
        }
    }

    private void requestAddBlack(String friendAccount) {
        RelationShipMessage relationShipMessage = new RelationShipMessage();
        relationShipMessage.setUserAccount(ChatClient.account);
        relationShipMessage.setFriendAccount(friendAccount);

        String json = JSON.toJSONString(relationShipMessage);

        MessagePacket messagePacket = new MessagePacket(
                CommandType.BLACK_LIST_ADD,
                MessageType.RELATIONSHIP_MANAGE,
                json.getBytes().length,
                json);

        ChatClient.session.write(messagePacket);
    }

    private void requestRemoveBlack(String account) {
        RelationShipMessage relationShipMessage = new RelationShipMessage();
        relationShipMessage.setUserAccount(ChatClient.account);
        relationShipMessage.setFriendAccount(account);

        String json = JSON.toJSONString(relationShipMessage);

        MessagePacket messagePacket = new MessagePacket(
                CommandType.BLACK_LIST_REMOVE,
                MessageType.RELATIONSHIP_MANAGE,
                json.getBytes().length,
                json);

        ChatClient.session.write(messagePacket);
    }

    private void printHelpInfo() {
        String string = new StringBuilder()
                .append("-blackList add [UserAccount] 增加黑名单\n")
                .append("-blackList remove [UserAccount] 移除黑名单")
                .toString();
        Interactive.getInstance().printlnToConsole(string);
    }
}
