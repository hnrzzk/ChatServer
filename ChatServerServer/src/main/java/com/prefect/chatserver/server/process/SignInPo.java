package com.prefect.chatserver.server.process;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.commoms.utils.MessagePacket;
import com.prefect.chatserver.commoms.utils.CommandType;
import com.prefect.chatserver.server.db.DBUtil;
import com.prefect.chatserver.commoms.utils.moudel.UserInfo;
import com.prefect.chatserver.server.db.TableInfo.UserTable;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;


/**
 * 用户注册逻辑处理类
 * Created by zhangkai on 2016/12/27.
 */
public class SignInPo extends ActionPo {
    private final static Logger logger = LoggerFactory.getLogger(SignInPo.class);

    public void process(IoSession ioSession, MessagePacket message) {

        //将消息内容从json转换成object
        UserInfo userInfo = JSON.parseObject(message.getMessage(), UserInfo.class);

        String account = userInfo.getAccount();
        String password = userInfo.getPassword();
        String nickName = userInfo.getAccount();

        //如果账户已存在则返回
        if (DBUtil.getInstance().isExit(UserTable.name, UserTable.Field.account, account)) {
            response(ioSession, CommandType.USER_SIGN_IN_ACK, false, "ERROR: account is exist");
            return;
        }

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(UserTable.Field.account, account);
        dataMap.put(UserTable.Field.password, password);
        dataMap.put(UserTable.Field.nickName, nickName);
        dataMap.put(UserTable.Field.sex, userInfo.getSex());

        Timestamp date = new Timestamp(System.currentTimeMillis());
        dataMap.put(UserTable.Field.registerTime, date);

        Object key;
        if ((key = DBUtil.getInstance().insert(UserTable.name, dataMap)) != null) {
            long id = Long.parseLong(key.toString());

            if (id >= 0) {
                //返回账户创建成功
                response(ioSession, CommandType.USER_SIGN_IN_ACK, true, "SUCCESS: account build success, UserID is: " + id);
            }

        } else {
            //返回账户创建失败
            response(ioSession, CommandType.USER_SIGN_IN_ACK, true, "ERROR: account build failed");
        }

    }
}
