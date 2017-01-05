package com.prefect.chatserver.commoms.codefactory;

import com.prefect.chatserver.commoms.util.MessagePacket;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import java.nio.charset.Charset;

/**
 * 编码器类
 * Created by zhangkai on 2016/12/26.
 */
public class ChatServerEncode extends ProtocolEncoderAdapter {
    private final Charset charset;

    public ChatServerEncode(Charset charset) {
        this.charset = charset;
    }


    public void encode(IoSession ioSession, Object o, ProtocolEncoderOutput protocolEncoderOutput) throws Exception {
        if (o instanceof MessagePacket) {
            MessagePacket messagePacket = (MessagePacket) o;
            IoBuffer buffer = IoBuffer.allocate(messagePacket.getPackageHeadLength() + messagePacket.getMessageLength());//设置缓冲的容量

            buffer.setAutoExpand(true);

            //写入包头长度 4字节
            buffer.putInt(messagePacket.getPackageHeadLength());
            //写入命令类容 4字节
            buffer.putInt(messagePacket.getCommand());
            //写入消息类型 4字节
            buffer.putInt(messagePacket.getMessageType());
            //写入消息长度 4字节
            buffer.putInt(messagePacket.getMessageLength());
            //写入消息体
            if (messagePacket.getMessageLength() > 0) {
                buffer.putString(messagePacket.getMessage(), charset.newEncoder());
            }
            buffer.flip();
            protocolEncoderOutput.write(buffer);
            protocolEncoderOutput.flush();
            buffer.free();
        }
    }

    @Override
    public void dispose(IoSession ioSession) throws Exception {

    }
}
