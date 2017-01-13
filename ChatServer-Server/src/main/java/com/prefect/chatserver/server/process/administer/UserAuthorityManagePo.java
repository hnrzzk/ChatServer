package com.prefect.chatserver.server.process.administer;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.commoms.utils.AttributeOperate;
import com.prefect.chatserver.commoms.utils.CommandType;
import com.prefect.chatserver.commoms.utils.MessagePacket;
import com.prefect.chatserver.commoms.utils.moudel.UserAuthorityManageMessage;
import com.prefect.chatserver.server.db.DBDao;
import org.apache.mina.core.session.IoSession;

import java.sql.Timestamp;

/**
 * 用户禁言逻辑
 * Created by zhangkai on 2017/1/6.
 */
public class UserAuthorityManagePo extends AdministerPo {

    @Override
    public void process(IoSession ioSession, MessagePacket messageObj) {
        UserAuthorityManageMessage userAuthorityManageMessage = JSON.parseObject(messageObj.getMessage(), UserAuthorityManageMessage.class);

        String account = userAuthorityManageMessage.getAccount();
        String reason = userAuthorityManageMessage.getReasong();

        //如果要设置禁言的用户不存在
        if (!DBDao.getInstance().accountIsExist(account)){
            response(ioSession, CommandType.USER_GAG_ACK, false, "account does not exist!");
            return;
        }

        if (authorizationCheck(ioSession, AttributeOperate.getInstance().getAccountOfAttribute(ioSession))) {
            switch (messageObj.getCommand()) {
                case CommandType.USER_GAG:
                    addGag(ioSession, account, reason, null, null);
                    break;
                case CommandType.USER_GAG_CANCEL:
                    cancelGag(ioSession, account);
                    break;
                case CommandType.USER_NO_LOGIN:
                    addNoLogin(ioSession, account, reason, null, null);
                    break;
                case CommandType.USER_NO_LOGIN_CANCEL:
                    cancelNoLogin(ioSession, account);
                    break;
            }
        }
    }

    /**
     * 设置禁言
     *
     * @param ioSession 发起请求的session
     * @param account   要禁言的账户
     * @param reason    禁言原因
     * @param endTime   禁言结束时间
     * @param startTime 禁言开始时间
     */
    private void addGag(IoSession ioSession, String account, String reason, Timestamp startTime, Timestamp endTime) {
        boolean processResult;
        String message;

        if (DBDao.getInstance().addGagAccount(account, reason, startTime, endTime) > 0) {

            processResult = true;

            message = "Gag user Success, account:" + account;
        } else {
            processResult = false;
            message = "Gag user Failed, account:" + account;
        }

        response(ioSession, CommandType.USER_GAG_ACK, processResult, message);
    }

    /**
     * 取消禁言
     *
     * @param account
     */
    private void cancelGag(IoSession ioSession, String account) {
        boolean processResult;
        String message;

        int successRows = DBDao.getInstance().cancelGagAccount(account);
        if (successRows > 0) {
            processResult = true;
            message = "Cancel Gag Success, account:" + account;
        } else {
            processResult = false;
            message = "Cancel Gag user Failed, account:" + account;
        }

        response(ioSession, CommandType.USER_GAG_ACK, processResult, message);
    }

    /**
     * 设置禁封登录
     *
     * @param account
     * @param reason
     * @param startTime
     * @param endTime
     */
    private void addNoLogin(IoSession ioSession, String account, String reason, Timestamp startTime, Timestamp endTime) {
        boolean processResult;
        String message;

        if (DBDao.getInstance().addNoLoginAccount(account, reason, startTime, endTime) > 0) {

            processResult = true;

            message = "Set user No Login Success, account:" + account;
        } else {
            processResult = false;
            message = "Set user No Login Failed, account:" + account;
        }

        response(ioSession, CommandType.USER_GAG_ACK, processResult, message);
    }

    /**
     * 取消禁封登录
     *
     * @param account
     */
    private void cancelNoLogin(IoSession ioSession, String account) {
        boolean processResult;
        String message;

        int successRows = DBDao.getInstance().cancelNoLoginAccount(account);
        if (successRows > 0) {
            processResult = true;
            message = "Cancel No Login Success, account:" + account;
        } else {
            processResult = false;
            message = "Cancel No Login user Failed, account:" + account;
        }

        response(ioSession, CommandType.USER_GAG_ACK, processResult, message);
    }
}
