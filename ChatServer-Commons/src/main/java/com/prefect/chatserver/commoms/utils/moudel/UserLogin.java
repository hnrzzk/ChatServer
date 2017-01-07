package com.prefect.chatserver.commoms.utils.moudel;

/**
 * Created by hnrzz on 2017/1/7.
 */
public class UserLogin {

    //帐户名
    String account;

    //验证字符串
    String verifyStr;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getVerifyStr() {
        return verifyStr;
    }

    public void setVerifyStr(String verifyStr) {
        this.verifyStr = verifyStr;
    }
}
