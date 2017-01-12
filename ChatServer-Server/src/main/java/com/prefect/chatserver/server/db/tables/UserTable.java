package com.prefect.chatserver.server.db.tables;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * 数据库User表信息
 * Created by zhangkai on 2016/12/30.
 */
@Entity
@Table(name = "user")
public class UserTable {

    public static final class Status {
        public static int ADMINISTER = 0; //管理员
        public static int ORDINARY_USER = 1;  //普通用户

        public static int online = 1;
        public static int offline = 0;
    }

    long id;
    String nickName;
    String account;
    String password;
    Timestamp registerTime;
    String sex;
    int onlineStatus;
    int authority;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Timestamp registerTime) {
        this.registerTime = registerTime;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(int onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public int getAuthority() {
        return authority;
    }

    public void setAuthority(int authority) {
        this.authority = authority;
    }
}
