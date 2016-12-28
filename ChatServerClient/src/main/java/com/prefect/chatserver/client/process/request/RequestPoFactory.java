package com.prefect.chatserver.client.process.request;

import com.prefect.chatserver.client.util.ClientRequestType;

/**
 * Created by zhangkai on 2016/12/28.
 */
public class RequestPoFactory {
    public static RequestPo getClass(ClientRequestType commandType) {
        switch (commandType) {
            case Login:
                return new LoginPo();
            case Sigin:
                return new SiginInPo();
            default:
                return null;
        }
    }
}
