package com.prefect.chatserver.commoms.codefactory;

import com.prefect.chatserver.commoms.util.MessagePacket;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;


import java.nio.charset.Charset;

/**
 * 解码器类
 * Created by zhangkai on 2016/12/26.
 */
public class ChatServerDecode implements ProtocolDecoder {
    private final AttributeKey CONTEXT = new AttributeKey(getClass(), "CONTEXT");
    private final Charset charset;
    private int maxMessageLength = 100;

    public ChatServerDecode(Charset charset) {
        this.charset = charset;
    }

    public ChatServerDecode() {
        this(Charset.defaultCharset());
    }

    public int getMaxMessageLength() {
        return maxMessageLength;
    }

    public void setMaxMessageLength(int maxMessageLength) {
        if (maxMessageLength <= 0) {
            throw new IllegalArgumentException("maxMessageLength must >0， maxMessageLength:" + maxMessageLength);
        }
        this.maxMessageLength = maxMessageLength;
    }

    private ChatContext getContext(IoSession session) {
        ChatContext chatContext;
        chatContext = (ChatContext) session.getAttribute(CONTEXT);
        if (null == chatContext) {
            chatContext = new ChatContext(charset);
            session.setAttribute(CONTEXT, chatContext);
        }
        return chatContext;
    }

    public void decode(IoSession ioSession, IoBuffer ioBuffer, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

        //先获取上次处理的上下文，为了避免其中包含有上次未处理的数据
        ChatContext chatContext = getContext(ioSession);

        //将当前buffer中的数据追加到Message的buffer当中
        chatContext.append(ioBuffer);

        //把position指向0的位置，把limit指向原来的position的位置
        IoBuffer buffer = chatContext.getBuffer();

        buffer.flip();

        if (buffer.remaining() >= 4) {
            buffer.mark();
            int packHeadLength = buffer.getInt();

            if (buffer.remaining() >= packHeadLength) {
                buffer.mark();

                //读取messagePacket包头信息
                MessagePacket messagePacket = new MessagePacket();
                messagePacket.setCommand(buffer.getInt());
                messagePacket.setMessageType(buffer.getInt());
                int bodyLen = buffer.getInt();
                messagePacket.setMessageLength(bodyLen);

                //读取正常的消息包，并写入输出流中，以便IoHandler进行处理
                if (bodyLen > 0 && buffer.remaining() >= bodyLen) {
                    messagePacket.setMessage(buffer.getString(bodyLen, charset.newDecoder()));
                } else {
                    buffer.reset();
                }
                protocolDecoderOutput.write(messagePacket);
            }

            if (buffer.hasRemaining()) {
                //move the data to the front of buffer
                IoBuffer temp = IoBuffer.allocate(maxMessageLength).setAutoExpand(true);
                temp.put(buffer);
                temp.flip();
                buffer.clear();
                buffer.put(temp);
            } else {
                //处理完成，清空标识
                buffer.clear();
            }
        }
    }

    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {
    }

    public void dispose(IoSession ioSession) throws Exception {
        ChatContext chatContext = (ChatContext) ioSession.getAttribute(CONTEXT);
        if (chatContext != null) {
            ioSession.removeAttribute(CONTEXT);
        }
    }
}
