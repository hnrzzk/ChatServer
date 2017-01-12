package com.prefect.chatserver.server.db.tables;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 数据库category表信息
 * Created by zhangkai on 2016/12/30.
 */
@Entity
@Table(name="category")
public class CategoryTable {

    long id;
    String userAccount;
    String name;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
