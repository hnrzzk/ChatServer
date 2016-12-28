package com.prefect.chatserver.commoms.util.moudel;

/**
 * 好友信息类
 * Created by zhangkai on 2016/12/26.
 */
public class FriendInfo {
    long id;

    /**
     * 用户账号id
     */
    long userId;

    /**
     * 好友的账号id
     */
    long friendId;

    /**
     * 分组名称
     */
    String categoryName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getFriendId() {
        return friendId;
    }

    public void setFriendId(long friendId) {
        this.friendId = friendId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
