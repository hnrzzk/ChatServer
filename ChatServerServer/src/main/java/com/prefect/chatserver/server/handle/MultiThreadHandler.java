package com.prefect.chatserver.server.handle;

import com.prefect.chatserver.commoms.util.MessagePacket;
import com.prefect.chatserver.server.process.MessageProcess;
import org.apache.mina.core.session.IoSession;

/**
 * 消息处理线程
 * Created by zhangkai on 2017/1/3.
 */
public class MultiThreadHandler implements Runnable{
    IoSession ioSession;

    MessagePacket messagePacket;

    MessageProcess messageProcess;

    public IoSession getIoSession() {
        return ioSession;
    }

    public void setIoSession(IoSession ioSession) {
        this.ioSession = ioSession;
    }

    public Object getMessagePacket() {
        return messagePacket;
    }

    public void setMessagePacket(MessagePacket messageObj) {
        this.messagePacket = messageObj;
    }

    public MessageProcess getMessageProcess() {
        return messageProcess;
    }

    public void setMessageProcess(MessageProcess messageProcess) {
        this.messageProcess = messageProcess;
    }

    @Override
    public void run() {

        if (messageProcess != null) {
            messageProcess.process(ioSession, messagePacket);
        }
    }
}
