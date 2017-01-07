package com.prefect.chatserver.client.process.response;

import com.alibaba.fastjson.JSON;
import com.prefect.chatserver.client.utils.Interactive;
import com.prefect.chatserver.commoms.utils.MessagePacket;
import com.prefect.chatserver.commoms.utils.moudel.UserInfo;
import com.prefect.chatserver.commoms.utils.moudel.UserListInfo;

import java.util.List;

/**
 * 收到服务器 关于 查找好友请求的反馈
 * Created by zhangkai on 2017/1/6.
 */
public class FindFriendListResponsePo implements ResponsePo {
    @Override
    public void process(MessagePacket messagePacket) {
        UserListInfo userListInfo = JSON.parseObject(messagePacket.getMessage(), UserListInfo.class);
        List<UserInfo> friendList = userListInfo.getFriendList();
        String[] strings = new String[friendList.size() + 1];

        strings[0] = "搜到的好友为：";
        for (int i = 0; i < friendList.size(); i++) {
            strings[i + 1] = userInfo2String(friendList.get(i));
        }

        Interactive.getInstance().printlnToConsole(strings);

    }

    String userInfo2String(UserInfo userInfo) {
        String result = new StringBuilder()
                .append("id:").append(userInfo.getId())
                .append(" account:").append(userInfo.getAccount())
                .append(" nick_name:").append(userInfo.getNickname())
                .append(" sex:").append(userInfo.getSex())
                .toString();
        return result;
    }
}
