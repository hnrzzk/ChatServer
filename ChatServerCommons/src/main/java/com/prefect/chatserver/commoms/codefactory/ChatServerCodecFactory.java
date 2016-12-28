package com.prefect.chatserver.commoms.codefactory;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;


import java.nio.charset.Charset;

/**
 * 解码器工厂类
 * Created by zhangkai on 2016/12/26.
 */
public class ChatServerCodecFactory implements ProtocolCodecFactory{
    private final ChatServerEncode encoder;
    private final ChatServerDecode decoder;

    public ChatServerCodecFactory(){
        this(Charset.defaultCharset());
    }

    public ChatServerCodecFactory(Charset charset){
        this.encoder=new ChatServerEncode(charset);
        this.decoder=new ChatServerDecode(charset);
    }

    public ProtocolEncoder getEncoder(IoSession ioSession) throws Exception {
        return this.encoder;
    }

    public ProtocolDecoder getDecoder(IoSession ioSession) throws Exception {
        return this.decoder;
    }
}
