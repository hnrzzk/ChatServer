package com.prefect.chatserver.server.process;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.commoms.utils.*;
import com.prefect.chatserver.commoms.utils.moudel.UserLogin;
import com.prefect.chatserver.server.db.hibernate.DBDao;
import com.prefect.chatserver.server.handle.ChatServerHandler;
import org.apache.mina.core.session.IoSession;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

/**
 * 登录请求验证
 * Created by hnrzz on 2017/1/7.
 */
public class LoginVerifyPo extends ActionPo {
    @Override
    public void process(IoSession ioSession, MessagePacket messageObj) {
        UserLogin userLogin = JSON.parseObject(messageObj.getMessage(), UserLogin.class);

        String account = userLogin.getAccount();
        String verifyStr = userLogin.getVerifyStr();
        //从session的属性中获取发给该session随机字符串
        String randomStr = AttributeOperate.getInstance().getVerifyStr(ioSession);

        //验证客户端发来的验证码与服务器生成的验证码是否相等
        boolean result = verify(account, randomStr, verifyStr);
        if (!result) {
            super.response(ioSession, CommandType.USER_LOGIN_VERIFY_ACK, false, "登录验证失败: Account does not exist or Incorrect password.");
            return;
        } else if (DBDao.getInstance().getOnlineStatue(account) > 0) { //用户已登录
            super.response(ioSession, CommandType.USER_LOGIN_VERIFY_ACK, false, "登录失败: Account is logged in.");
            return;
        } else if (DBDao.getInstance().isNoLogin(account)) { //验证是否禁封登录
            super.response(ioSession, CommandType.USER_LOGIN_VERIFY_ACK, false, "登录失败: Account are prohibited to log on");
            return;
        }

        //更新在线状态
        if (DBDao.getInstance().changeAccountOnlineStatus(account, 1)) { //更新在线状态 成功

            //将已建立的连接保存在内存中
            ChatServerHandler.sessionMap.put(account, ioSession);
            //在session中记录account名称
            AttributeOperate.getInstance().setAccountOfAttribute(ioSession, account);
            super.response(ioSession, CommandType.USER_LOGIN_VERIFY_ACK, true, "登录成功: Welcome!");
            logger.info("user login, user num:" + ChatServerHandler.sessionMap.size());
        } else {
            //更新在线状态失败
            super.response(ioSession, CommandType.USER_LOGIN_VERIFY_ACK, false, "登录失败: Account does not exist or Incorrect password.");
            return;
        }

        //发送上线通知
        sendOnlineNotice(account);
        //发送离线消息
        sendOfflineMessage(ioSession, account);
    }

    /**
     * 验证客户端发送的验证码是否正确
     *
     * @param account   账户
     * @param randomStr 随机字符串
     * @param verifyStr 验证码
     * @return
     */
    private boolean verify(String account, String randomStr, String verifyStr) {
        if (null == randomStr) {
            return false;
        }

        //根据用户名拿到密码
        String password = DBDao.getInstance().getPassWord(account);

        //将密码与随机字符串拼接后生成md5码
        String md5 = MathUtil.getInstance().getMD5(password + randomStr);

        //将md5码与验证码匹配
        if (md5 != null && md5.equals(verifyStr)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 发送上线通知
     *
     * @param account
     */
    private void sendOnlineNotice(String account) {
        //根据得到该账号的在线好友列表
        List<String> accountList = DBDao.getInstance().getFriendListForOnlineStatus(account, 1);

        //生产消息字符串
        String message = new StringBuilder().append("您的好友").append(account).append("已上线").toString();

        //打包
        MessagePacket messagePacket = null;
        try {
            messagePacket = new MessagePacket(CommandType.USER_ON_LINE_NOTICE, MessageType.STRING, message.getBytes("utf-8").length, message);
            //发送通知
            sendNotice(accountList, messagePacket);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        }

    }

    /**
     * 发送离线消息
     *
     * @param ioSession
     * @param account
     */
    private void sendOfflineMessage(IoSession ioSession, String account) {
        Timestamp now = TimeUtil.getInstance().getTimeStampNow();
        List<MessagePacket> messagePacketList = DBDao.getInstance().getOfflineMessage(account, now);
        Iterator<MessagePacket> iterator = messagePacketList.iterator();
        while (iterator.hasNext()) {
            MessagePacket item = iterator.next();
            //根据message从工厂类生产对应的处理类
            MessageProcess messageProcess = MessagePoFactory.getClass(item.getCommand());
            messageProcess.process(ioSession, item);
        }
    }
}
