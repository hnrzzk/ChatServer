package com.prefect.chatserver.client.process.request.operate.administer;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.client.ChatClient;
import com.prefect.chatserver.client.process.request.operate.OperatePo;
import com.prefect.chatserver.client.utils.Interactive;
import com.prefect.chatserver.commoms.utils.CommandType;
import com.prefect.chatserver.commoms.utils.MessagePacket;
import com.prefect.chatserver.commoms.utils.MessageType;
import com.prefect.chatserver.commoms.utils.moudel.UserAuthorityManageMessage;
import org.apache.mina.core.session.IoSession;

/**
 * 请求设置用户禁止登录
 * Created by zhangkai on 2017/1/6.
 */
public class NoLogin extends OperatePo{


    public NoLogin(String[] strings) {
        super(strings);
    }

    @Override
    public void process() {
        try {
            switch (super.strings[1]) {
                case "add":
                    addNoLogin(strings[2], strings[3]);
                    break;
                case "remove":
                    cancelNoLogin(strings[2]);
                    break;
                default:
                    printHelpInfo();
            }
        } catch (Exception e) {
            printHelpInfo();
        }
    }

    /**
     * 请求设置用户禁封登录
     *
     * @param account
     * @param reason
     */
    private void addNoLogin(String account, String reason) {
        UserAuthorityManageMessage userAuthorityManageMessage = new UserAuthorityManageMessage();
        userAuthorityManageMessage.setAccount(account);
        userAuthorityManageMessage.setReasong(reason);

        String json = JSON.toJSONString(userAuthorityManageMessage);

        MessagePacket messagePacket = new MessagePacket();
        messagePacket.setCommand(CommandType.USER_NO_LOGIN);
        messagePacket.setMessageType(MessageType.USER_AUTHORITY_MANAGE);
        messagePacket.setMessageLength(json.getBytes().length);
        messagePacket.setMessage(json);

        ChatClient.session.write(messagePacket);
    }

    /**
     * 取消用户禁封登录
     *
     * @param account
     */
    private void cancelNoLogin(String account) {
        UserAuthorityManageMessage userAuthorityManageMessage = new UserAuthorityManageMessage();
        userAuthorityManageMessage.setAccount(account);
        String json = JSON.toJSONString(userAuthorityManageMessage);

        MessagePacket messagePacket = new MessagePacket();
        messagePacket.setCommand(CommandType.USER_NO_LOGIN_CANCEL);
        messagePacket.setMessageType(MessageType.USER_AUTHORITY_MANAGE);
        messagePacket.setMessageLength(json.getBytes().length);
        messagePacket.setMessage(json);

        ChatClient.session.write(messagePacket);
    }

    private void printHelpInfo() {
        String string = new StringBuilder()
                .append("-noLogin add [UserAccount] [reason] 增加禁封登录用户\n")
                .append("-noLogin remove [UserAccount] 取消用户禁封登录")
                .toString();
        Interactive.getInstance().printlnToConsole(string);
    }
}
