package com.prefect.chatserver.server.db;

import com.prefect.chatserver.server.db.TableInfo.CategoryTable;
import com.prefect.chatserver.server.db.TableInfo.FriendsTable;
import com.prefect.chatserver.server.db.TableInfo.UserTable;
import com.prefect.chatserver.server.handle.ChatServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangkai on 2016/12/29.
 */
public class DBDao {

    private final static Logger logger = LoggerFactory.getLogger(DBDao.class);

    private DBDao() {

    }

    private static class DBDaoHandle {
        private static DBDao instance = new DBDao();
    }

    public static DBDao getInstance() {
        return DBDaoHandle.instance;
    }

    /**
     * 查询该user表中该账户是否存在
     *
     * @param account 账户
     * @return 是否存在
     */
    public boolean accountIsExist(String account) {
        return DBUtil.getInstance().isExit("user", "account", account);
    }

    /**
     * 创建分组，如存在则不创建,如果存在则返回分组id
     *
     * @param account  账户名
     * @param category 分组名
     * @return 分组id：>0
     * 失败：-1
     */
    public long creatCategory(String account, String category) {
        DBUtil dbUtil = DBUtil.getInstance();
        long result = -1;

        //该用户下没有改分组
        if (!dbUtil.isExit(CategoryTable.name, new String[]{CategoryTable.Field.userAccount, CategoryTable.Field.name}, new String[]{account, category})) {
            System.out.println("category does not exist, create");

            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put(CategoryTable.Field.userAccount, account);
            dataMap.put(CategoryTable.Field.name, category);

            Object key = dbUtil.executeInsert(CategoryTable.name, dataMap);

            if (key == null) {
                return -1;
            }
            result = Long.parseLong(key.toString());

        } else {
            System.out.println("category exist, get");

            //该用户下有分组，则根据account和categoryName得到分组id
            String sql = String.format("select %s from %s where %s=? and %s=?",
                    CategoryTable.Field.id, CategoryTable.name, CategoryTable.Field.userAccount, CategoryTable.Field.name);

            ChatServerDbConnectUnit chatServerDbConnectUnit = dbUtil.executeQuery(sql, new Object[]{account, category});
            ResultSet resultSet = chatServerDbConnectUnit.getResultSet();

            try {
                while (resultSet.next()) {
                    result = resultSet.getLong(1);
                }
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            } finally {
                chatServerDbConnectUnit.close();
            }

        }
        return result;
    }

    /**
     * 在数据库中添加用户信息
     *
     * @param userAccount   用户帐号
     * @param friendAccount 好友账号
     * @param categoryName  分组名称
     * @return 大于0:用户id 0：用户已存在 小于0：操作失败
     */
    public long addFriendInfo(String userAccount, String friendAccount, String categoryName) {

        //如果用户关系不存在
        if (!DBUtil.getInstance().isExit(FriendsTable.name,
                new String[]{FriendsTable.Field.userAccount, FriendsTable.Field.friendAccount},
                new Object[]{userAccount, friendAccount})) {

            //新增
            long categoryId = DBDao.getInstance().creatCategory(userAccount, categoryName);
            if (categoryId > 0) {

                Timestamp date = new Timestamp(System.currentTimeMillis());

                Map<String, Object> friendRelationship = new HashMap<>();
                friendRelationship.put(FriendsTable.Field.userAccount, userAccount);
                friendRelationship.put(FriendsTable.Field.friendAccount, friendAccount);
                friendRelationship.put(FriendsTable.Field.createTime, date);
                friendRelationship.put(FriendsTable.Field.categoryId, categoryId);

                Object key = DBUtil.getInstance().executeInsert(FriendsTable.name, friendRelationship);
                return Long.parseLong(key.toString());
            } else {
                return -1;
            }
        } else {
            return 0;
        }
    }

    /**
     * 查找指定用户的朋友类表
     *
     * @param uesrAccount  账号
     * @param onLineStatue (1在线用户 0离线用户 -1所有用户)
     * @return
     */
    public List<String> getFriendLIst(String uesrAccount, int onLineStatue) {

        //select friends.friend_account from friends left join user on friends.friend_account=user.account where friend.user_account=? and user.is_online = ?;
        String sql=new StringBuilder().append("select ").append(FriendsTable.name).append(".").append(FriendsTable.Field.friendAccount)
                .append(" from ").append(FriendsTable.name).append(" left join ").append(UserTable.name)
                .append(" on ").append(FriendsTable.name).append(".").append(FriendsTable.Field.friendAccount).append("=").append(UserTable.name).append(".").append(UserTable.Field.account)
                .append(" where ").append(FriendsTable.name).append(".").append(FriendsTable.Field.userAccount).append("=?")
                .append(" and ").append(UserTable.name).append(".").append(UserTable.Field.isOnline).append("=?").toString();

        ChatServerDbConnectUnit chatServerDbConnectUnit = DBUtil.getInstance().executeQuery(sql, new Object[]{uesrAccount,onLineStatue});
        ResultSet resultSet = chatServerDbConnectUnit.getResultSet();

        List<String> friendInfoList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                friendInfoList.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        } finally {
            chatServerDbConnectUnit.close();
        }
        return friendInfoList;
    }

    /**
     * 修改用户的在线状态
     *
     * @param account
     * @param onlineStatus (1:在线，0：离线)
     * @return
     */
    public boolean changeAccountOnlineStatus(String account, int onlineStatus) {
        //update $tableName set $filed = 0 where $field1 = ?
        String sql = new StringBuilder()
                .append("update ").append(UserTable.name)
                .append(" set ").append(UserTable.Field.isOnline).append("=").append(onlineStatus).append(" where ")
                .append(UserTable.Field.account).append("=?")
                .toString();

        if (DBUtil.getInstance().executeUpdate(sql, new Object[]{account})!=null){
            return true;
        }else{
            return false;
        }
    }

}

