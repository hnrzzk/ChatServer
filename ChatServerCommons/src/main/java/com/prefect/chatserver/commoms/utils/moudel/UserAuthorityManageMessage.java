package com.prefect.chatserver.commoms.utils.moudel;

import java.sql.Timestamp;

/**
 * Created by zhangkai on 2017/1/6.
 */
public class UserAuthorityManageMessage {
    /**
     * 账户
     */
    String account;

    /**
     * 原因
     */
    String reasong;

    /**
     * 开始时间
     */
    Timestamp startTime;

    /**
     * 结束时间
     */
    Timestamp endTime;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getReasong() {
        return reasong;
    }

    public void setReasong(String reasong) {
        this.reasong = reasong;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }
}
