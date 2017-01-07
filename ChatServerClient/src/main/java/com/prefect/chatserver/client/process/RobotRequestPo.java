package com.prefect.chatserver.client.process;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.client.utils.Util;
import com.prefect.chatserver.commoms.utils.AttributeOperate;
import com.prefect.chatserver.commoms.utils.CommandType;
import com.prefect.chatserver.commoms.utils.MessagePacket;
import com.prefect.chatserver.commoms.utils.MessageType;
import com.prefect.chatserver.commoms.utils.moudel.UserInfo;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhangkai on 2017/1/5.
 */
public class RobotRequestPo {
    private static Logger logger = LoggerFactory.getLogger(RobotRequestPo.class);

    private RobotRequestPo() {

    }

    private static class RobotPoHandle {
        private static RobotRequestPo instance = new RobotRequestPo();
    }

    public static RobotRequestPo getInstance() {
        return RobotPoHandle.instance;
    }

    /**
     * 机器人注册逻辑
     *
     * @param session
     */
    public void signIn(IoSession session) {

        //判断session中是否有account属性，如果没有等待一下
        String account = Util.getInstance().getAccount(session);

        //编辑报文
        MessagePacket messagePacket = new MessagePacket();
        messagePacket.setCommand(CommandType.USER_SIGN_IN);
        messagePacket.setMessageType(MessageType.USER_INFO);

        UserInfo userInfo = new UserInfo();
        userInfo.setAccount(account);
        userInfo.setPassword(account);
        userInfo.setNickname(account);

        String json = JSON.toJSONString(userInfo);
        messagePacket.setMessageLength(json.getBytes().length);
        messagePacket.setMessage(json);

        //发送报文
        session.write(messagePacket);
    }

    /**
     * 机器人登录逻辑
     *
     * @param session
     */
    public void login(IoSession session) {
        MessagePacket message = new MessagePacket();

        message.setMessageType(MessageType.MESSAGE);
        message.setCommand(CommandType.USER_LOGIN_REQUEST);

        String pubKsy= AttributeOperate.getInstance().getPubKey(session);
        while(null==pubKsy){
            pubKsy=AttributeOperate.getInstance().getPubKey(session);
        }

        message.setMessage(pubKsy);
        message.setMessageLength(pubKsy.getBytes().length);

        session.write(message);
    }




}
