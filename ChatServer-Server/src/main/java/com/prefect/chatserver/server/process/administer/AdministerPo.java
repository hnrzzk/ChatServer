package com.prefect.chatserver.server.process.administer;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.commoms.utils.CommandType;
import com.prefect.chatserver.commoms.utils.MessagePacket;
import com.prefect.chatserver.commoms.utils.MessageType;
import com.prefect.chatserver.commoms.utils.moudel.ACKMessage;
import com.prefect.chatserver.server.db.hibernate.DBDao;
import com.prefect.chatserver.server.process.ActionPo;
import org.apache.mina.core.session.IoSession;

import java.io.UnsupportedEncodingException;

/**
 * Created by zhangkai on 2017/1/6.
 */
public abstract class AdministerPo extends ActionPo {
    /**
     * 权限检查，是否是管理员
     *
     * @param account
     * @return
     */
    boolean authorizationCheck(IoSession ioSession, String account) {
        boolean isAllow = DBDao.getInstance().authorityCheck(account);

        if (!isAllow) {
            ACKMessage ACKMessage = new ACKMessage();
            ACKMessage.setActionResult(false);
            ACKMessage.setMessage("对不起，您没有权限!");

            String json = JSON.toJSONString(ACKMessage);
            try {
                ioSession.write(new MessagePacket(CommandType.SEND_BROADCAST_ACK, MessageType.RESPONSE, json.getBytes("utf-8").length, json));
            } catch (UnsupportedEncodingException e) {
                logger.error(e.getMessage(), e);
            }
        }


        return isAllow;
    }
}
