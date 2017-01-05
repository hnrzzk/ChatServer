package com.prefect.chatserver.client.utils;

import com.prefect.chatserver.commoms.util.AttributeOperate;
import org.apache.mina.core.session.IoSession;

/**
 * 公共方法类
 * Created by zhangkai on 2017/1/5.
 */
public class Util {
    private Util(){}

    private static class UtilHandle{
        private static Util instance=new Util();
    }

    public static Util getInstance(){
        return UtilHandle.instance;
    }

    /**
     * 从session中获取账户名
     * @param session
     * @return
     */
    public String getAccount(IoSession session) {
        //判断session中是否有account属性，如果没有等待一下
        String account = "";
        while (account.equals("")) {
            account = AttributeOperate.getInstance().getAccountOfAttribute(session);
        }

        return account;
    }
}
