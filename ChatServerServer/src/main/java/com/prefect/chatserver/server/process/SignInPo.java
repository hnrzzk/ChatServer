package com.prefect.chatserver.server.process;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.commoms.util.MessagePacket;
import com.prefect.chatserver.commoms.util.CommandType;
import com.prefect.chatserver.commoms.util.MessageType;
import com.prefect.chatserver.commoms.util.db.ChatServerDbConnectUnit;
import com.prefect.chatserver.commoms.util.db.DBUtil;
import com.prefect.chatserver.commoms.util.moudel.ChatMessage;
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
public class SignInPo extends UserManagePo {
    private final static Logger logger = LoggerFactory.getLogger(SignInPo.class);

    public void handle(IoSession ioSession, MessagePacket message) throws Exception {

        //将消息内容从json转换成object
        UserInfo userInfo = JSON.parseObject(message.getMessage(), UserInfo.class);

        String account = userInfo.getAccount();
        String password = userInfo.getPassword();
        String nickName = userInfo.getAccount();

        boolean userExist = DBUtil.getInstance().isExit("user", "account", account);

        //如果账户已存在则返回
        if (userExist) {
            response(ioSession, CommandType.USER_SIGN_IN_ACK, "ERROR: account is exist");
            return;
        }

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("account", account);
        dataMap.put("password", password);
        dataMap.put("nick_name", nickName);


        if (DBUtil.getInstance().executeInsert("user", dataMap) > 0) {
            //返回账户创建成功
            response(ioSession, CommandType.USER_SIGN_IN_ACK, "SUCCESS: account build success");
        } else {
            //返回账户创建失败
            response(ioSession, CommandType.USER_SIGN_IN_ACK, "ERROR: account build failed");
        }

    }
}
