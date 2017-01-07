package com.prefect.chatserver.commoms.codefactory;

import org.apache.mina.core.buffer.IoBuffer;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * Created by zhangkai on 2016/12/26.
 */
public class ChatContext {
    private final CharsetDecoder decoder;
    private IoBuffer buffer;
    private int matchCount = 0;
    private int overflowPosition = 0;

    public ChatContext(Charset charset) {
        this.decoder = charset.newDecoder();
        this.buffer = IoBuffer.allocate(80).setAutoExpand(true);//设置缓存容量为80，并自动扩充
    }

    public CharsetDecoder getDecoder() {
        return decoder;
    }

    public IoBuffer getBuffer() {
        return buffer;
    }

    public int getMatchCount() {
        return matchCount;
    }

    public int getOverflowPosition() {
        return overflowPosition;
    }

    public void setMatchCount(int matchCount) {
        this.matchCount = matchCount;
    }

    public void reset(){
        overflowPosition=0;
        matchCount=0;
        decoder.reset();
    }

    public void append(IoBuffer in){
        getBuffer().put(in);
    }
}
