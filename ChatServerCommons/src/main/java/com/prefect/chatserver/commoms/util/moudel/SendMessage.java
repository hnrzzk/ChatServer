package com.prefect.chatserver.commoms.util.moudel;

/**
 * Created by zhangkai on 2016/12/26.
 */
public class SendMessage {
    long receiveId;
    String message;

    public long getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(long receiveId) {
        this.receiveId = receiveId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
