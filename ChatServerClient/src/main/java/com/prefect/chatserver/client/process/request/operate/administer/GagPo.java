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
 * 用户禁言操作
 * Created by zhangkai on 2017/1/6.
 */
public class GagPo extends OperatePo {

    public GagPo(String[] strings) {
        super(strings);
    }

    @Override
    public void process() {
        try {
            switch (super.strings[1]) {
                case "add":
                    addGag(strings[2], strings[3]);
                    break;
                case "remove":
                    cancelGag(strings[2]);
                    break;
                default:
                    printHelpInfo();
            }
        } catch (Exception e) {
            printHelpInfo();
        }
    }

    /**
     * 请求设置用户禁言
     *
     * @param account
     * @param reason
     */
    private void addGag(String account, String reason) {
        UserAuthorityManageMessage userAuthorityManageMessage = new UserAuthorityManageMessage();
        userAuthorityManageMessage.setAccount(account);
        userAuthorityManageMessage.setReasong(reason);

        String json = JSON.toJSONString(userAuthorityManageMessage);

        MessagePacket messagePacket = new MessagePacket();
        messagePacket.setCommand(CommandType.USER_GAG);
        messagePacket.setMessageType(MessageType.USER_AUTHORITY_MANAGE);
        messagePacket.setMessageLength(json.getBytes().length);
        messagePacket.setMessage(json);

        ChatClient.session.write(messagePacket);
    }

    /**
     * 取消用户禁言
     *
     * @param account
     */
    private void cancelGag(String account) {
        UserAuthorityManageMessage userAuthorityManageMessage = new UserAuthorityManageMessage();
        userAuthorityManageMessage.setAccount(account);
        String json = JSON.toJSONString(userAuthorityManageMessage);

        MessagePacket messagePacket = new MessagePacket();
        messagePacket.setCommand(CommandType.USER_GAG_CANCEL);
        messagePacket.setMessageType(MessageType.USER_AUTHORITY_MANAGE);
        messagePacket.setMessageLength(json.getBytes().length);
        messagePacket.setMessage(json);

        ChatClient.session.write(messagePacket);
    }


    private void printHelpInfo() {
        String string = new StringBuilder()
                .append("-gag add [UserAccount] [reason]增加禁言用户\n")
                .append("-gag remove [UserAccount] 取消用户禁言")
                .toString();
        Interactive.getInstance().printlnToConsole(string);
    }
}
