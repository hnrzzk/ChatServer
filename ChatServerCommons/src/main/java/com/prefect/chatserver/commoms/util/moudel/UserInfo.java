package com.prefect.chatserver.commoms.util.moudel;

import java.sql.Timestamp;

/**
 * 用户信息类
 * Created by zhangkai on 2016/12/26.
 */
public class UserInfo {
    /**
     * 账号id
     */
    long id;

    String account;
    /**
     * 昵称
     */
    String nickname;
    /**
     * 密码
     */
    String password;

    /**
     * 是否在线
     */
    int isOnline;

    byte sex;

    Timestamp registerTime;

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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }

    public byte getSex() {
        return sex;
    }

    public void setSex(byte sex) {
        this.sex = sex;
    }

    public Timestamp getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Timestamp registerTime) {
        this.registerTime = registerTime;
    }

    @Override
    public String toString() {
        return String.format("id:[%s] account:[%s] nickName[%s] password[%s] isOnline[%s]",
                getId(), getAccount(), getNickname(), getPassword(), getIsOnline());
    }
}
