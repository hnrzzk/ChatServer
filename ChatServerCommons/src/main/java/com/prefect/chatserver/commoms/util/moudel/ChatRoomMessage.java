package com.prefect.chatserver.commoms.util.moudel;

/**
 * 聊天室消息
 * Created by zhangkai on 2017/1/3.
 */
public class ChatRoomMessage {

    private String account;
    private String chatRoomName;
    private String message;

    /**
     * 服务器返回的请求结果
     */
    private boolean result;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getChatRoomName() {
        return chatRoomName;
    }

    public void setChatRoomName(String chatRoomName) {
        this.chatRoomName = chatRoomName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
