package com.prefect.chatserver.client.process.request.operate;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.client.ChatClient;
import com.prefect.chatserver.client.utils.Interactive;
import com.prefect.chatserver.commoms.utils.CommandType;
import com.prefect.chatserver.commoms.utils.MessagePacket;
import com.prefect.chatserver.commoms.utils.MessageType;
import com.prefect.chatserver.commoms.utils.moudel.RelationShipMessage;
import org.apache.mina.core.session.IoSession;

/**
 * 黑名单管理请求逻辑
 * Created by zhangkai on 2017/1/3.
 */
public class BlackListManagePo extends OperatePo{
    public final String addFriend="add";
    public final String removeFriend="remove";

    public BlackListManagePo(String[] strings) {
        super(strings);
    }

    @Override
    public void process() {
        try {
            String command = super.strings[1];
            String userAccount = super.strings[2];

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
