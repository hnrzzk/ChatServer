package com.prefect.chatserver.commoms.util.db;


import com.prefect.chatserver.commoms.util.moudel.CategoryInfo;
import com.prefect.chatserver.commoms.util.moudel.FriendInfo;
import com.prefect.chatserver.commoms.util.moudel.UserInfo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * 数据库相关业务类
 * Created by zhangkai on 2016/12/26.
 */
public class DBDao {
    private static class DBServiceHandler {
        private static DBDao instance = new DBDao();
    }

    public static DBDao getInstance() {
        return DBServiceHandler.instance;
    }

    /**
     * 根据sql语句获取用户信息
     *
     * @param sql
     * @return
     */
    public UserInfo getUserInfo(String sql) {
        UserInfo userInfo = null;
        ChatServerDbConnectUnit connectUnit = DBUtil.getInstance().executeQuery(sql);
        ResultSet resultSet=connectUnit.getResultSet();
        try {
            while (resultSet.next()) {
                userInfo = new UserInfo();
                userInfo.setId(resultSet.getLong("id"));
                userInfo.setNickname(resultSet.getString("nick_name"));
                userInfo.setPassword(resultSet.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectUnit.close();
        }

        return userInfo;
    }

    /**
     * 根据sql语句获取好友信息
     *
     * @param sql
     * @return
     */
    public List<FriendInfo> getFriendInfo(String sql) {
        List<FriendInfo> friendInfoList = new ArrayList<FriendInfo>();
        ChatServerDbConnectUnit connectUnit = DBUtil.getInstance().executeQuery(sql);

        ResultSet resultSet=connectUnit.getResultSet();
        try {
            while (resultSet.next()) {
                FriendInfo friendInfo = new FriendInfo();
                friendInfo.setId(resultSet.getLong("id"));
                friendInfo.setCategoryName(resultSet.getString("category_id"));
                friendInfo.setFriendId(resultSet.getLong("friend_id"));
                friendInfo.setUserId(resultSet.getLong("user_id"));
                friendInfoList.add(friendInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectUnit.close();
        }

        return friendInfoList;
    }

    /**
     * 根据sql语句获取分组信息
     *
     * @param sql
     * @return
     */
    public List<CategoryInfo> getCategoryInfo(String sql) {
        List<CategoryInfo> categoryInfoList = new ArrayList<CategoryInfo>();

        ChatServerDbConnectUnit connectUnit = DBUtil.getInstance().executeQuery(sql);

        ResultSet resultSet=connectUnit.getResultSet();
        try {
            while (resultSet.next()) {
                CategoryInfo categoryInfo = new CategoryInfo();
                categoryInfo.setId(resultSet.getLong("id"));
                categoryInfo.setUserId(resultSet.getLong("user_id"));
                categoryInfo.setName(resultSet.getString("name"));
                categoryInfoList.add(categoryInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectUnit.close();
        }

        return categoryInfoList;
    }
}
