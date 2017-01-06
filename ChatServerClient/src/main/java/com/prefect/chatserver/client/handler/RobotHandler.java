package com.prefect.chatserver.client.handler;

import com.prefect.chatserver.client.Robot;
import com.prefect.chatserver.client.process.RobotRequestPo;
import com.prefect.chatserver.client.process.RobotResponsePo;
import com.prefect.chatserver.commoms.utils.MessagePacket;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zhangkai on 2017/1/4.
 */
public class RobotHandler implements IoHandler {
    private static Logger logger = LoggerFactory.getLogger(RobotHandler.class);

    private ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    @Override
    public void sessionCreated(IoSession ioSession) throws Exception {

    }

    @Override
    public void sessionOpened(IoSession ioSession) throws Exception {

        //每500ms检查一次session是否建立成功
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //判断连接是否建立
                if (ioSession.isConnected()) {
                    logger.info(Thread.currentThread().getName() + " 已建立连接，当前连接数:" + Robot.sessionConcurrentHashMap.size());

                    RobotRequestPo.getInstance().login(ioSession);

                    //取消定时任务
                    this.cancel();
                }
            }
        }, 0, 500);
    }

    @Override
    public void sessionClosed(IoSession ioSession) throws Exception {
        Robot.sessionConcurrentHashMap.remove(ioSession.getAttribute("account").toString());
        logger.info(Thread.currentThread().getName() + " 连接已关闭，当前连接数:" + Robot.sessionConcurrentHashMap.size());

    }

    @Override
    public void sessionIdle(IoSession ioSession, IdleStatus idleStatus) throws Exception {

    }

    @Override
    public void exceptionCaught(IoSession ioSession, Throwable throwable) throws Exception {
        logger.info(Thread.currentThread().getName() + " session出现异常，当前连接数:" + Robot.sessionConcurrentHashMap.size());
    }

    @Override
    public void messageReceived(IoSession ioSession, Object o) throws Exception {
        if (o instanceof MessagePacket) {
            MessagePacket messagePacket = (MessagePacket) o;

            RobotResponsePo responsePo=new RobotResponsePo(ioSession,messagePacket);
            cachedThreadPool.execute(responsePo);
        }
    }

    @Override
    public void messageSent(IoSession ioSession, Object o) throws Exception {

    }

    @Override
    public void inputClosed(IoSession ioSession) throws Exception {

    }
}
