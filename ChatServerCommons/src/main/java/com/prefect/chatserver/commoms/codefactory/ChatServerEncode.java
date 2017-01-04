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
            buffer.putInt(messagePacket.getPackageHeadLength());
            buffer.putInt(messagePacket.getCommand());
            buffer.putInt(messagePacket.getMessageType());
            buffer.putInt(messagePacket.getMessageLength());
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
