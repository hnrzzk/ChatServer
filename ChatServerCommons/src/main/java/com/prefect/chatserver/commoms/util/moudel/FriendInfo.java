package com.prefect.chatserver.commoms.util.moudel;

/**
 * 好友信息类
 * Created by zhangkai on 2016/12/26.
 */
public class FriendInfo {

    /**
     * 用户账号
     */
    String userAccount;

    /**
     * 好友的账号
     */
    String friendAccount;

    /**
     * 分组名称
     */
    String categoryName;

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getFriendAccount() {
        return friendAccount;
    }

    public void setFriendAccount(String friendAccount) {
        this.friendAccount = friendAccount;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
