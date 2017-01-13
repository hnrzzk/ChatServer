package com.prefect.chatserver.server.process;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.commoms.utils.MessagePacket;
import com.prefect.chatserver.commoms.utils.CommandType;
import com.prefect.chatserver.server.db.DBDao;
import com.prefect.chatserver.commoms.utils.moudel.UserInfo;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 用户注册逻辑处理类
 * Created by zhangkai on 2016/12/27.
 */
public class SignInPo extends ActionPo {
    private final static Logger logger = LoggerFactory.getLogger(SignInPo.class);

    public void process(IoSession ioSession, MessagePacket message) {

        //将消息内容从json转换成object
        UserInfo userInfo = JSON.parseObject(message.getMessage(), UserInfo.class);

        long result = DBDao.getInstance().siginAccount(userInfo);

        if (result > 0) {
            //返回账户创建成功
            response(ioSession, CommandType.USER_SIGN_IN_ACK, true, "SUCCESS: account build success, UserID is: " + result);
        } else if (-1 == result) {
            response(ioSession, CommandType.USER_SIGN_IN_ACK, false, "ERROR: account is exist");
        } else {
            response(ioSession, CommandType.USER_SIGN_IN_ACK, false, "ERROR: System error");
        }


    }
}
