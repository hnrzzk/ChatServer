package com.prefect.chatserver.commoms.utils;

import org.apache.mina.core.session.IoSession;

/**
 * 获取session中属性的工具类
 * Created by zhangkai on 2017/1/3.
 */
public class AttributeOperate {
    //session存储账户的属性名
    private String attributeNameOfAccount = "account";
    private String attributeNameOfChatRoom = "chatRoom";
    private String attributeNameOfPubKey = "RSAPubKey";
    private String attributeNameOfPrivKey = "RSAPrivKey";
    private String attributeNameOfVerifyStr = "verifyStr";

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
     *
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
     *
     * @param session
     * @param value
     */
    public void setChatRoomNameOfAttribute(IoSession session, Object value) {
        session.setAttribute(attributeNameOfChatRoom, value);
    }

    /**
     * 将密钥对存入session中
     *
     * @param session
     * @param pubKey
     */
    public void setPubKey(IoSession session, String pubKey) {
        session.setAttribute(attributeNameOfPubKey, pubKey);
    }

    /**
     * 将私钥存入session中
     *
     * @param session
     * @param privKey
     */
    public void setPrivKey(IoSession session, String privKey) {
        session.setAttribute(attributeNameOfPrivKey, privKey);
    }

    /**
     * 从session中获取公钥
     * @param session
     * @return
     */
    public String getPubKey(IoSession session) {
        Object obj = session.getAttribute(attributeNameOfPubKey);
        if (obj != null) {
            return obj.toString();
        } else {
            return null;
        }
    }

    /**
     * 从session中获取私钥
     * @param session
     * @return
     */
    public String getPrivKey(IoSession session) {
        Object obj = session.getAttribute(attributeNameOfPrivKey);
        if (obj != null) {
            return obj.toString();
        } else {
            return null;
        }
    }

    public void setVerifyStr(IoSession session,String verityStr){
        session.setAttribute(attributeNameOfVerifyStr,verityStr);
    }

    public String getVerifyStr(IoSession session){
        Object obj = session.getAttribute(attributeNameOfVerifyStr);
        if (obj != null) {
            return obj.toString();
        } else {
            return null;
        }
    }

}
