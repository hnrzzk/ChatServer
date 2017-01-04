package com.prefect.chatserver.commoms.util;

import org.apache.mina.core.session.IoSession;

/**
 * 获取session中属性的工具类
 * Created by zhangkai on 2017/1/3.
 */
public class AttributeOperate {
    //session存储账户的属性名
    public String attributeNameOfAccount = "account";
    public String attributeNameOfChatRoom = "chatRoom";

    private AttributeOperate() {
    }

    private static class AttributeOperateHandle {
        private static AttributeOperate attributeOperate = new AttributeOperate();
    }

    public static AttributeOperate getInstance() {
        return AttributeOperateHandle.attributeOperate;
    }

    /**
     * 从session的属性中得到用户名
     *
     * @param session
     * @return
     */
    public String getAccountOfAttribute(IoSession session) {
        Object obj = session.getAttribute(attributeNameOfAccount);
        if (null != obj) {
            return obj.toString();
        } else {
            return "";
        }
    }

    /**
     * 在session的属性中设置用户名字
     * @param session
     * @param value
     */
    public void setAccountOfAttribute(IoSession session, Object value) {
        session.setAttribute(attributeNameOfAccount, value);
    }

    /**
     * 从session的属性中得到聊天室名字
     *
     * @param session
     * @return
     */
    public String getChatRoomNameOfAttribute(IoSession session) {
        Object obj = session.getAttribute(attributeNameOfChatRoom);
        if (null != obj) {
            return obj.toString();
        } else {
            return "";
        }
    }

    /**
     * 在session的属性中设置聊天室名字
     * @param session
     * @param value
     */
    public void setChatRoomNameOfAttribute(IoSession session, Object value) {
        session.setAttribute(attributeNameOfChatRoom, value);
    }
}
