package com.prefect.chatserver.client.process.request;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.client.ChatClient;
import com.prefect.chatserver.commoms.util.CommandType;
import com.prefect.chatserver.commoms.util.MessagePacket;
import com.prefect.chatserver.commoms.util.MessageType;
import com.prefect.chatserver.commoms.util.moudel.FriendManageMessage;
import org.apache.mina.core.session.IoSession;

/**
 * Created by liuxiaonan on 2016/12/31.
 */
public class FriendManagePo {
    public final String addFriend="add";
    public final String removeFriend="remove";

    /**
     * 管理用户的逻辑
     * @param command   用户输入的命令行
     */
    public void manageFriend(String[] command){
        switch (command[1]){
            case addFriend:
                //-friend add userAccount
                requestAddFriend(command[2]);
                break;
            case removeFriend:
                //-friend remove userAccount
                requestRemoveFriend(command[2]);
                break;
        }
    }

    private void requestAddFriend(String friendAccount){
        FriendManageMessage friendManageMessage = new FriendManageMessage();
        friendManageMessage.setUserAccount(ChatClient.account);
        friendManageMessage.setFriendAccount(friendAccount);
        friendManageMessage.setCategoryName("好友");

        String json = JSON.toJSONString(friendManageMessage);

        MessagePacket messagePacket = new MessagePacket(
                CommandType.FRIEND_LIST_ADD,
                MessageType.FRIEND_MANAGE,
                json.getBytes().length,
                json);

        ChatClient.session.write(messagePacket);
    }

    private void requestRemoveFriend(String account){
        FriendManageMessage friendManageMessage = new FriendManageMessage();
        friendManageMessage.setUserAccount(ChatClient.account);
        friendManageMessage.setFriendAccount(account);

        String json = JSON.toJSONString(friendManageMessage);

        MessagePacket messagePacket = new MessagePacket(
                CommandType.FRIEND_LIST_REMOVE,
                MessageType.FRIEND_MANAGE,
                json.getBytes().length,
                json);

        ChatClient.session.write(messagePacket);
    }
}
