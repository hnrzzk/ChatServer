package com.prefect.chatserver.server.process;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.commoms.utils.*;
import com.prefect.chatserver.commoms.utils.moudel.ACKMessage;
import org.apache.mina.core.session.IoSession;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * 登录请求
 * Created by hnrzz on 2017/1/7.
 */
public class LoginRequestPo extends ActionPo {
    @Override
    public void process(IoSession ioSession, MessagePacket messageObj) {
        String pubKey = messageObj.getMessage();

        ACKMessage ackMessage = new ACKMessage();
        try {
            //得到一个10位的随机字符串
            String random = MathUtil.getInstance().getRandomStr(10);

            //对字符串用公钥加密
            byte[] encodeData = RSA.encryptByPublicKey(random.getBytes("utf-8"), pubKey);
            //转换为string
            String veirfyStr = new String(Base64.getEncoder().encode(encodeData));

            ackMessage.setActionResult(true);
            ackMessage.setMessage(veirfyStr);

            //将验证字符串保存在session中
            AttributeOperate.getInstance().setVerifyStr(ioSession, random);

        } catch (Exception e) {
            logger.error("RSA公钥加密失败:" + e.getMessage(), e);
            ackMessage.setActionResult(false);
            ackMessage.setMessage("login failed, RSA公钥加密失败.");
        }

        String json = JSON.toJSONString(ackMessage);

        MessagePacket messagePacket = new MessagePacket();
        messagePacket.setCommand(CommandType.USER_LOGIN_REQUEST_ACK);
        messagePacket.setMessageType(MessageType.RESPONSE);
        try {
            messagePacket.setMessageLength(json.getBytes("utf-8").length);
            messagePacket.setMessage(json);

            ioSession.write(messagePacket);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        }

    }
}
