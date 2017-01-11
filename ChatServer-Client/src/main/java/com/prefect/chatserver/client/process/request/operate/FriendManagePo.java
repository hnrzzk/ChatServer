package com.prefect.chatserver.client.process.request.operate;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.client.ChatClient;
import com.prefect.chatserver.client.utils.Interactive;
import com.prefect.chatserver.commoms.utils.CommandType;
import com.prefect.chatserver.commoms.utils.MessagePacket;
import com.prefect.chatserver.commoms.utils.MessageType;
import com.prefect.chatserver.commoms.utils.moudel.RelationShipMessage;
import com.prefect.chatserver.commoms.utils.moudel.UserInfo;
import org.apache.mina.core.session.IoSession;

import java.io.UnsupportedEncodingException;

/**
 * 好友管理请求
 * Created by zhangkai on 2016/12/31.
 */
public class FriendManagePo extends OperatePo {
    public final String addFriend = "add";
    public final String removeFriend = "remove";
    public final String find = "find";
    public final String findNickName = "nickName";
    public final String findAccount = "account";

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
                case find:
                    String type = strings[2];
                    String info = strings[3];
                    requestFindFriend(type, info);
                    break;
                default:
                    printHelpInfo();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            Interactive.getInstance().printlnToConsole("命令错误");
            printHelpInfo();
        }
    }

    /**
     * 请求增加好友
     *
     * @param friendAccount
     */
    private void requestAddFriend(String friendAccount) {
        RelationShipMessage relationShipMessage = new RelationShipMessage();
        relationShipMessage.setUserAccount(ChatClient.account);
        relationShipMessage.setFriendAccount(friendAccount);
        relationShipMessage.setCategoryName("好友");

        String json = JSON.toJSONString(relationShipMessage);

        MessagePacket messagePacket = null;
        try {
            messagePacket = new MessagePacket(
                    CommandType.FRIEND_LIST_ADD,
                    MessageType.RELATIONSHIP_MANAGE,
                    json.getBytes("utf-8").length,
                    json);
            ChatClient.session.write(messagePacket);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求删除好友
     *
     * @param account
     */
    private void requestRemoveFriend(String account) {
        RelationShipMessage relationShipMessage = new RelationShipMessage();
        relationShipMessage.setUserAccount(ChatClient.account);
        relationShipMessage.setFriendAccount(account);

        String json = JSON.toJSONString(relationShipMessage);

        MessagePacket messagePacket = null;
        try {
            messagePacket = new MessagePacket(
                    CommandType.FRIEND_LIST_REMOVE,
                    MessageType.RELATIONSHIP_MANAGE,
                    json.getBytes("utf-8").length,
                    json);
            ChatClient.session.write(messagePacket);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求查找好友
     *
     * @param type
     * @param info
     */
    private void requestFindFriend(String type, String info) {
        MessagePacket messagePacket = new MessagePacket();
        messagePacket.setCommand(CommandType.USER_FIND);
        messagePacket.setMessageType(MessageType.USER_INFO);

        UserInfo userInfo = new UserInfo();
        switch (type) {
            case findNickName:
                userInfo.setNickname(info);
                break;
            case findAccount:
                userInfo.setAccount(info);
                break;
        }
        String json = JSON.toJSONString(userInfo);
        try {
            messagePacket.setMessageLength(json.getBytes("utf-8").length);
            messagePacket.setMessage(json);

            ChatClient.session.write(messagePacket);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void printHelpInfo() {
        String string = new StringBuilder()
                .append("-friend add [UserAccount] 增加好友\n")
                .append("-friend remove [UserAccount] 移除好友\n")
                .append("-friend find nickName [UserNickName] 按照昵称查找好友\n")
                .append("-friend find account [UserNickName] 按照账户查找好友\n")
                .toString();
        Interactive.getInstance().printlnToConsole(string);
    }
}
