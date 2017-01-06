package com.prefect.chatserver.commoms.utils;

import java.sql.Time;
import java.sql.Timestamp;

/**
 * 时间操作相关类
 * Created by zhangkai on 2017/1/6.
 */
public class TimeUtil {
    private TimeUtil() {

    }

    private static class TimeUtilHandle {
        private static TimeUtil instance = new TimeUtil();
    }

    public static TimeUtil getInstance() {
        return TimeUtilHandle.instance;
    }

    /**
     * 获取当前的时间戳
     * @return
     */
    public Timestamp getTimeStampNow(){
        return new Timestamp(System.currentTimeMillis());
    }
}
