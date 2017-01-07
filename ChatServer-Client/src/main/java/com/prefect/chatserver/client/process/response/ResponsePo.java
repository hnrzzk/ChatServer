package com.prefect.chatserver.client.process.response;

import com.prefect.chatserver.commoms.utils.MessagePacket;

/**
 * Created by zhangkai on 2016/12/28.
 */
public interface ResponsePo {
    void process(MessagePacket messagePacket);
}
