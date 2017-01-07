package com.prefect.chatserver.client.process.request.operate;


/**
 * Created by zhangkai on 2017/1/3.
 */
public abstract class OperatePo {
    protected String[] strings;

    public OperatePo(String[] strings) {
        this.strings = strings;
    }

    public abstract void process();
}
