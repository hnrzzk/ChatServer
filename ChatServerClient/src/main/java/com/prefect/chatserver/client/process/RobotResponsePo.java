package com.prefect.chatserver.client.process;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.client.utils.Interactive;
import com.prefect.chatserver.client.utils.Util;
import com.prefect.chatserver.commoms.util.CommandType;
import com.prefect.chatserver.commoms.util.MessagePacket;
import com.prefect.chatserver.commoms.util.MessageType;
import com.prefect.chatserver.commoms.util.moudel.ActionResponseMessage;
import com.prefect.chatserver.commoms.util.moudel.ChatMessage;
import org.apache.mina.core.session.IoSession;

import java.sql.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhangkai on 2017/1/5.
 */
public class RobotResponsePo implements Runnable {

    MessagePacket messagePacket;
    IoSession session;

    public RobotResponsePo(IoSession session, MessagePacket messagePacket) {
        this.messagePacket = messagePacket;
        this.session = session;
    }

    @Override
    public void run() {
        int command = messagePacket.getCommand();

        switch (command) {
            case CommandType.USER_LOGIN_ACK:
                System.out.println("登录成功!!");
                processLogin();
                break;
            case CommandType.MESSAGE:
            case CommandType.SEND_BROADCAST_ACK:
                processMessage();
                break;


        }
    }

    /**
     * 处理服务器的登录反馈
     */
    private void processLogin() {
        ActionResponseMessage actionResponseMessage = JSON.parseObject(messagePacket.getMessage(), ActionResponseMessage.class);
        //如果登录成功
        if (actionResponseMessage.getActionResult()) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    MessagePacket messagePacket = new MessagePacket();
                    messagePacket.setCommand(CommandType.MESSAGE);
                    messagePacket.setMessageType(MessageType.MESSAGE);

                    String account = Util.getInstance().getAccount(session);
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setSendAccount(account);
                    chatMessage.setReceiveAccount(account);

                    String message = new StringBuilder()
                            .append(session.toString())
                            .append(" send: ")
                            .append(new Date(System.currentTimeMillis())).toString();
                    chatMessage.setMessage(message);

                    String json = JSON.toJSONString(chatMessage);
                    messagePacket.setMessage(json);
                    messagePacket.setMessageLength(json.getBytes().length);

                    session.write(messagePacket);
                }
            }, 500, 1000);
        }
    }

    private void processMessage() {
        ChatMessage chatMessage = JSON.parseObject(messagePacket.getMessage(), ChatMessage.class);

        Interactive.getInstance().printlnToConsole(chatMessage.getMessage());
    }
}
