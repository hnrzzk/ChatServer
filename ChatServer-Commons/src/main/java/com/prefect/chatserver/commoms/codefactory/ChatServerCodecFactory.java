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
    private final ChatServerNewDecode decoder;

    public ChatServerCodecFactory(){
        this(Charset.forName("UTF-8"));
    }

    public ChatServerCodecFactory(Charset charset){
        this.encoder=new ChatServerEncode(charset);
        this.decoder=new ChatServerNewDecode(charset);
    }

    public ProtocolEncoder getEncoder(IoSession ioSession) throws Exception {
        return this.encoder;
    }

    public ProtocolDecoder getDecoder(IoSession ioSession) throws Exception {
        return this.decoder;
    }
}
