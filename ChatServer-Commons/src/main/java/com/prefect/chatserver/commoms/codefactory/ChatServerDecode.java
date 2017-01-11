package com.prefect.chatserver.commoms.codefactory;

import com.prefect.chatserver.commoms.utils.MessagePacket;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import java.nio.charset.Charset;

/**
 * 聊天服务器报文解码器
 * Created by zhangkai on 2017/1/4.
 */
public class ChatServerDecode extends CumulativeProtocolDecoder {
    private Charset charset;

    public ChatServerDecode(Charset charSet) {
        this.charset = charSet;
    }

    @Override
    protected boolean doDecode(IoSession ioSession, IoBuffer ioBuffer, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {
        if (ioBuffer.remaining() > 4) {
            //记录包头位置
            ioBuffer.mark();

            //得到包头
            int packHeadLength = ioBuffer.getInt();

            if (ioBuffer.remaining() > packHeadLength) {
                MessagePacket messagePacket = new MessagePacket();

                int commandType = ioBuffer.getInt();
                messagePacket.setCommand(commandType);

                int messageType = ioBuffer.getInt();
                messagePacket.setMessageType(messageType);

                int messageLength = ioBuffer.getInt();
                messagePacket.setMessageLength(messageLength);

                if (messageLength > 0 && ioBuffer.remaining() >= messageLength) {
                    messagePacket.setMessage(ioBuffer.getString(messageLength, charset.newDecoder()));
                    protocolDecoderOutput.write(messagePacket);
                    return true;
                } else {
                    ioBuffer.reset();
                    return false;
                }
            } else {
                ioBuffer.reset();
                return false;
            }

        }

        return false;
    }
}
