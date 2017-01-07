package com.prefect.chatserver.commoms.utils.moudel;

/**
 * Created by zhangkai on 2016/12/26.
 */
public class ChatMessage {
    private String sendAccount;
    private String receiveAccount;
    private String message;

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
