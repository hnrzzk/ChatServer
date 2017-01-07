package com.prefect.chatserver.client.process.response;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.client.ChatClient;
import com.prefect.chatserver.client.utils.Interactive;
import com.prefect.chatserver.commoms.utils.*;
import com.prefect.chatserver.commoms.utils.moudel.ACKMessage;
import com.prefect.chatserver.commoms.utils.moudel.UserLogin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;
import java.util.Scanner;

/**
 * Created by hnrzz on 2017/1/7.
 */
public class LoginResponsePo implements ResponsePo {
    private static Logger logger = LoggerFactory.getLogger(LoginResponsePo.class);

    @Override
    public void process(MessagePacket messagePacket) {
        ACKMessage ackMessage = JSON.parseObject(messagePacket.getMessage(),ACKMessage.class);
        String ackStr=ackMessage.getMessage();

        if (!ackMessage.getActionResult()){
            Interactive.getInstance().printlnToConsole(ackStr);
            return;
        }

        String privateKey = AttributeOperate.getInstance().getPrivKey(ChatClient.session);

        try {
            byte[] bytes = Base64.getDecoder().decode(ackStr);

            String randomStr = new String(RSA.decryptByPrivateKey(bytes, privateKey));

            UserLogin userLogin = getUserInfo(randomStr);

            String json = JSON.toJSONString(userLogin);

            ChatClient.session.write(new MessagePacket(
                    CommandType.USER_LOGIN_VERIFY,
                    MessageType.USER_LOGIN,
                    json.getBytes().length,
                    json));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private UserLogin getUserInfo(String randomStr) {
        Interactive interactive = Interactive.getInstance();

        Scanner scanner = new Scanner(System.in);
        UserLogin userInfo = new UserLogin();

        interactive.printlnToConsole("～～欢迎登录～～");

        interactive.printToConsole("请输入账户:");
        String account = scanner.next();

        interactive.printToConsole("请输入密码:");
        String password = scanner.next();

        userInfo.setAccount(account);

        String verify = MathUtil.getInstance().getMD5(password + randomStr);

        userInfo.setVerifyStr(verify);

        ChatClient.account = account;

        return userInfo;
    }
}
