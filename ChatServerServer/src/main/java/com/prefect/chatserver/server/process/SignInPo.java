package com.prefect.chatserver.server.process;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.commoms.util.ChatMessage;
import com.prefect.chatserver.commoms.util.db.DBUtil;
import com.prefect.chatserver.commoms.util.moudel.SendMessage;
import com.prefect.chatserver.commoms.util.moudel.UserInfo;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * 用户登录处理类
 * Created by zhangkai on 2016/12/27.
 */
public class SignInPo implements MessageProcess {
    private final static Logger logger = LoggerFactory.getLogger(SignInPo.class);

    public void handle(IoSession ioSession, ChatMessage message) throws Exception {

        UserInfo userInfo = JSON.parseObject(message.getMessage(), UserInfo.class);

        String account = userInfo.getAccount();
        String password = userInfo.getPassword();
        String nickName = userInfo.getAccount();

        boolean userExist = DBUtil.getInstance().isExit("user", "account", account);

        SendMessage sendMessage = new SendMessage();
        if (userExist) {
            //TODO：返回用户已存在的消息
            return;
        }

        logger.info("收到");

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("account",account);
        dataMap.put("password",password);
        dataMap.put("nick_name",nickName);

        DBUtil.getInstance().executeInsert("user",dataMap);

    }
}
