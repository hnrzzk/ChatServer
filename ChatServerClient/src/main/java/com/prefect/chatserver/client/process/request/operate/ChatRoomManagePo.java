package com.prefect.chatserver.client.process.request.operate;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.client.ChatClient;
import com.prefect.chatserver.client.utils.Interactive;
import com.prefect.chatserver.commoms.util.CommandType;
import com.prefect.chatserver.commoms.util.MessagePacket;
import com.prefect.chatserver.commoms.util.MessageType;
import com.prefect.chatserver.commoms.util.moudel.ChatRoomMessage;

/**
 * 聊天室
 * Created by zhangkai on 2017/1/3.
 */
public class ChatRoomManagePo extends OperatePo{
    final static String enter = "enter";
    final static String quit = "quit";
    final static String chat = "send";
    final static String getName = "name";

    public ChatRoomManagePo(String[] strings) {
        super(strings);
    }

    @Override
    public void process() {

        try {
            String commandStr = super.strings[1];

            switch (commandStr) {
                case enter:
                    enterChatRoom(super.strings[2]);
                    break;
                case quit:
                    quitChatRoom();
                    break;
                case chat:
                    sendChatMessage(super.strings[2]);
                    break;
                case getName:
                    printChatRoomName();
                    break;
                default:
                    printHelpInfo();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            printHelpInfo();
        }

    }

    /**
     * 打印帮助信息
     */
    void printHelpInfo() {
        String helpInfo = new StringBuilder()
                .append("-chatRoom enter [name] 进入指定聊天室")
                .append("-chatRoom quit 离开聊天室")
                .append("-chatRoom send [message] 发送消息")
                .append("-chatRoom name 获取当前聊天室名称")
                .toString();
        Interactive.getInstance().printlnToConsole(helpInfo);
    }

    /**
     * 离开聊天室
     *
     * @param chatRoomName
     */
    void enterChatRoom(String chatRoomName) {
        ChatRoomMessage chatRoomMessage = new ChatRoomMessage();
        chatRoomMessage.setAccount(ChatClient.account);
        chatRoomMessage.setChatRoomName(chatRoomName);

        String json = JSON.toJSONString(chatRoomMessage);

        MessagePacket messagePacket = new MessagePacket(
                CommandType.CHAT_ROOM_ENTER,
                MessageType.CHATROOM_MANAGE,
                json.getBytes().length,
                json);

        ChatClient.session.write(messagePacket);
    }

    /**
     * 进入聊天室
     */
    void quitChatRoom() {

        String chatRoomName = ChatClient.chatRoomName;
        if (null == chatRoomName && chatRoomName.equals("")) {
            Interactive.getInstance().printlnToConsole("未进入任何聊天室，不需要退出");
        } else {
            ChatRoomMessage chatRoomMessage = new ChatRoomMessage();
            chatRoomMessage.setAccount(ChatClient.account);
            chatRoomMessage.setChatRoomName(ChatClient.chatRoomName);

            String json = JSON.toJSONString(chatRoomMessage);

            MessagePacket messagePacket = new MessagePacket(
                    CommandType.CHAT_ROOM_QUIT,
                    MessageType.CHATROOM_MANAGE,
                    json.getBytes().length,
                    json);

            ChatClient.session.write(messagePacket);
        }
    }

    /**
     * 发送消息
     *
     * @param message
     */
    void sendChatMessage(String message) {
        ChatRoomMessage chatRoomMessage = new ChatRoomMessage();
        chatRoomMessage.setAccount(ChatClient.account);
        chatRoomMessage.setChatRoomName(ChatClient.chatRoomName);
        chatRoomMessage.setMessage(message);

        String json = JSON.toJSONString(chatRoomMessage);

        MessagePacket messagePacket = new MessagePacket(
                CommandType.CHAT_ROOM_SEND,
                MessageType.CHATROOM_MANAGE,
                json.getBytes().length,
                json);

        ChatClient.session.write(messagePacket);
    }

    void printChatRoomName(){
        Interactive.getInstance().printlnToConsole(ChatClient.chatRoomName);
    }

}
