package com.prefect.chatserver.server.db.tables;

import java.sql.Timestamp;

/**
 * 禁言表user_gag信息
 * Created by zhangkai on 2017/1/6.
 */
public class UserNoLoginTable {

    public static class Field {
        public static String id = "id";
        public static String account = "account";
        public static String reason = "reason";
        public static String startTime = "start_time";
        public static String endTime = "end_time";
        public static String cancel = "CANCEL";
        public static String cancelTime = "cancel_time";
    }

    public static class Status {
        public static int NO_CANCEL = 0;
        public static int CANCEL = 1;
    }

    long id;
    String account;
    String reason;
    Timestamp startTime;
    Timestamp endTime;
    int cancel;
    Timestamp cancelTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public int getCancel() {
        return cancel;
    }

    public void setCancel(int cancel) {
        this.cancel = cancel;
    }

    public Timestamp getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Timestamp cancelTime) {
        this.cancelTime = cancelTime;
    }
}
