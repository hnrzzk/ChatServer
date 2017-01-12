package com.prefect.chatserver.server.db.tables;

import java.sql.Timestamp;

/**
 * 黑名单表 信息
 * Created by zhangkai on 2017/1/3.
 */
public class BlackListTable {
    long id;
    String userAccount;
    String blackAccount;
    Timestamp createTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getBlackAccount() {
        return blackAccount;
    }

    public void setBlackAccount(String blackAccount) {
        this.blackAccount = blackAccount;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
