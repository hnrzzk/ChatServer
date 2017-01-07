package com.prefect.chatserver.commoms.utils.moudel;

import java.util.List;

/**
 * 好友列表
 * Created by zhangkai on 2017/1/6.
 */
public class UserListInfo {
    List<UserInfo> friendList;

    public List<UserInfo> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<UserInfo> friendList) {
        this.friendList = friendList;
    }
}
