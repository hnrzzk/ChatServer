package com.prefect.chatserver.commoms.util;

import java.io.Serializable;

/**
 * 消息包头
 * Created by zhangkai on 2016/12/26.
 */
public class MessagePacket implements Serializable {

    private static final long serialVersionUID = -6744683316349472480L;

    private final int packageHeadLength = 20; //包头长度 字节

    private int command;//命令

    private int messageType;//消息类型

    private int messageLength;//包体长度

    private String message;//消息

    public int getPackageHeadLength() {
        return packageHeadLength;
    }

    public int getCommand() {
        return command;
    }

    public void setCommand(int command) {
        this.command = command;
    }

    public int getMessageLength() {
        return messageLength;
    }

    public void setMessageLength(int messageLength) {
        this.messageLength = messageLength;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return String.format("Message : command[%s] messageType[%s] context[%s]",
                getCommand(), getMessageType(), getMessage());
    }
}