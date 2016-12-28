package com.prefect.chatserver.commoms.util.moudel;

/**
 * Created by zhangkai on 2016/12/26.
 */
public class ChatMessage {
    String sendAccount;
    String receiveAccount;
    String message;

    public String getSendAccount() {
        return sendAccount;
    }

    public void setSendAccount(String sendAccount) {
        this.sendAccount = sendAccount;
    }

    public String getReceiveAccount() {
        return receiveAccount;
    }

    public void setReceiveAccount(String receiveAccount) {
        this.receiveAccount = receiveAccount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
