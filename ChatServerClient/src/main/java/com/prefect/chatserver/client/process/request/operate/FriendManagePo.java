package com.prefect.chatserver.client.process.request.operate;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.client.ChatClient;
import com.prefect.chatserver.client.util.Interactive;
import com.prefect.chatserver.commoms.util.CommandType;
import com.prefect.chatserver.commoms.util.MessagePacket;
import com.prefect.chatserver.commoms.util.MessageType;
import com.prefect.chatserver.commoms.util.moudel.RelationShipMessage;

/**
 * Created by liuxiaonan on 2016/12/31.
 */
public class FriendManagePo extends OperatePo{
    public final String addFriend = "add";
    public final String removeFriend = "remove";

    public FriendManagePo(String[] strings) {
        super(strings);
    }

    /**
     * 管理用户的逻辑
     */
    @Override
    public void process() {
        try {
            String command = super.strings[1];
            String userAccount = super.strings[2];

            switch (command) {
                case addFriend:
                    //-friend add userAccount
                    requestAddFriend(userAccount);
                    break;
                case removeFriend:
                    //-friend remove userAccount
                    requestRemoveFriend(userAccount);
                    break;
                default:
                    printHelpInfo();
            }
        }catch (ArrayIndexOutOfBoundsException e){
            Interactive.getInstance().printlnToConsole("命令错误");
            printHelpInfo();
        }
    }

    private void requestAddFriend(String friendAccount) {
        RelationShipMessage relationShipMessage = new RelationShipMessage();
        relationShipMessage.setUserAccount(ChatClient.account);
        relationShipMessage.setFriendAccount(friendAccount);
        relationShipMessage.setCategoryName("好友");

        String json = JSON.toJSONString(relationShipMessage);

        MessagePacket messagePacket = new MessagePacket(
                CommandType.FRIEND_LIST_ADD,
                MessageType.RELATIONSHIP_MANAGE,
                json.getBytes().length,
                json);

        ChatClient.session.write(messagePacket);
    }

    private void requestRemoveFriend(String account) {
        RelationShipMessage relationShipMessage = new RelationShipMessage();
        relationShipMessage.setUserAccount(ChatClient.account);
        relationShipMessage.setFriendAccount(account);

        String json = JSON.toJSONString(relationShipMessage);

        MessagePacket messagePacket = new MessagePacket(
                CommandType.FRIEND_LIST_REMOVE,
                MessageType.RELATIONSHIP_MANAGE,
                json.getBytes().length,
                json);

        ChatClient.session.write(messagePacket);
    }

    private void printHelpInfo() {
        String string = new StringBuilder()
                .append("-friend add [UserAccount] 增加好友\n")
                .append("-friend remove [UserAccount] 移除好友")
                .toString();
        Interactive.getInstance().printlnToConsole(string);
    }
}
