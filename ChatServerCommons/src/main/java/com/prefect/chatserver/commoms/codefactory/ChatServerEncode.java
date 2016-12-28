package com.prefect.chatserver.commoms.codefactory;

import com.prefect.chatserver.commoms.util.ChatMessage;
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
        if (o instanceof ChatMessage) {
            ChatMessage chatMessage = (ChatMessage) o;
            IoBuffer buffer = IoBuffer.allocate(chatMessage.getPackageHeadLength() + chatMessage.getMessageLength());//设置缓冲的容量

            //buffer.setAutoExpand(true);
            buffer.putInt(chatMessage.getPackageHeadLength());
            buffer.putInt(chatMessage.getCommand());
            buffer.putInt(chatMessage.getMessageType());
            buffer.putInt(chatMessage.getMessageLength());
            if (chatMessage.getMessageLength() > 0) {
                buffer.putString(chatMessage.getMessage(), charset.newEncoder());
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
