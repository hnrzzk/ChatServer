package com.prefect.chatserver.server.db;

import com.prefect.chatserver.commoms.utils.MessagePacket;
import com.prefect.chatserver.commoms.utils.TimeUtil;
import com.prefect.chatserver.commoms.utils.moudel.UserInfo;
import com.prefect.chatserver.server.db.TableInfo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

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

            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put(CategoryTable.Field.userAccount, account);
            dataMap.put(CategoryTable.Field.name, category);

            Object key = dbUtil.insert(CategoryTable.name, dataMap);

            if (key == null) {
                return -1;
            }
            result = Long.parseLong(key.toString());

        } else {

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
                if (friendAccount != null) {
                    friendRelationship.put(FriendsTable.Field.categoryId, categoryId);
                }
                Object key = DBUtil.getInstance().insert(FriendsTable.name, friendRelationship);
                return Long.parseLong(key.toString());
            } else {
                return -1;
            }
        } else {
            return 0;
        }
    }

    /**
     * 根据在线状态查找指定用户的朋友列表
     *
     * @param uesrAccount  账号
     * @param onLineStatue (1在线用户 0离线用户 -1所有用户)
     * @return
     */
    public List<String> getFriendListForOnlineStatus(String uesrAccount, int onLineStatue) {

        //select friends.friend_account from friends left join user on friends.friend_account=user.account where friend.user_account=? and user.is_online = ?;
        String sql = new StringBuilder().append("select ").append(FriendsTable.name).append(".").append(FriendsTable.Field.friendAccount)
                .append(" from ").append(FriendsTable.name).append(" left join ").append(UserTable.name)
                .append(" on ").append(FriendsTable.name).append(".").append(FriendsTable.Field.friendAccount).append("=").append(UserTable.name).append(".").append(UserTable.Field.account)
                .append(" where ").append(FriendsTable.name).append(".").append(FriendsTable.Field.userAccount).append("=?")
                .append(" and ").append(UserTable.name).append(".").append(UserTable.Field.isOnline).append("=?").toString();

        ChatServerDbConnectUnit chatServerDbConnectUnit = DBUtil.getInstance().executeQuery(sql, new Object[]{uesrAccount, onLineStatue});
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
        boolean result = false;

        //update user set is_online = 0 where is_online = ?
        String sql = new StringBuilder()
                .append("update ").append(UserTable.name)
                .append(" set ").append(UserTable.Field.isOnline).append("=").append(onlineStatus).append(" where ")
                .append(UserTable.Field.account).append("=?")
                .toString();

        ChatServerDbConnectUnit chatServerDbConnectUnit = DBUtil.getInstance().executeUpdate(sql, new Object[]{account});
        if (chatServerDbConnectUnit.getSuccessRow() >0) {
            result = true;
        }
        //关闭数据库连接
        chatServerDbConnectUnit.close();

        return result;
    }

    /**
     * 增加黑名单
     *
     * @param userAccount
     * @param friendAccount
     * @return
     */
    public long addBlackListInfo(String userAccount, String friendAccount) {
        //如果用户关系不存在
        if (!DBUtil.getInstance().isExit(
                BlackListTable.name,
                new String[]{BlackListTable.Field.userAccount, BlackListTable.Field.blackAccount},
                new Object[]{userAccount, friendAccount})) {

            Timestamp date = new Timestamp(System.currentTimeMillis());

            Map<String, Object> friendRelationship = new HashMap<>();
            friendRelationship.put(BlackListTable.Field.userAccount, userAccount);
            friendRelationship.put(BlackListTable.Field.blackAccount, friendAccount);
            friendRelationship.put(BlackListTable.Field.createTime, date);

            Object key = DBUtil.getInstance().insert(BlackListTable.name, friendRelationship);
            return Long.parseLong(key.toString());

        } else {
            return 0;
        }
    }

    /**
     * 移出黑名单
     *
     * @param userAccount 用户帐号
     * @param account     目标账号
     * @return
     */
    public List<Long> removeFriendRelationShip(String userAccount, String account) {
        Map<String, Object> map = new HashMap<>();
        map.put(FriendsTable.Field.userAccount, userAccount);
        map.put(FriendsTable.Field.friendAccount, account);
        return DBUtil.getInstance().deleteRow(FriendsTable.name, map);
    }

    /**
     * 是否在黑名单中
     *
     * @param userAccount 用户帐号
     * @param account     目标账号
     * @return
     */
    public boolean isInBlackList(String userAccount, String account) {
        return DBUtil.getInstance().isExit(
                BlackListTable.name,
                new String[]{BlackListTable.Field.userAccount, BlackListTable.Field.blackAccount}
                , new Object[]{userAccount, account});
    }

    /**
     * 检查该用户是否是管理员
     *
     * @param userAccount 用户帐号
     * @return
     */
    public boolean authorityCheck(String userAccount) {
        return DBUtil.getInstance().isExit(UserTable.name,
                new String[]{UserTable.Field.account, UserTable.Field.identify},
                new Object[]{userAccount, AuthorityTable.ADMINISTER});
    }

    /**
     * 增加禁言用户
     *
     * @param account 账户
     * @param reason  原因
     * @return 禁言表该条数据对应的id 如果没有则返回-1
     */
    public long addGagAccount(String account, String reason, Timestamp startTime, Timestamp endTime) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put(UserGagTable.Field.account, account);
        conditions.put(UserGagTable.Field.reason, reason);
        conditions.put(UserGagTable.Field.startTime, startTime);
        conditions.put(UserGagTable.Field.endTimne, endTime);

        Object result = DBUtil.getInstance().insert(UserGagTable.name, conditions);
        if (reason != null) {
            return Long.parseLong(result.toString());
        } else {
            return -1;
        }

    }

    /**
     * 取消用户禁言
     *
     * @param account
     * @return 影响的行数，-1 执行失败
     */
    public int cancelGagAccount(String account) {
        //update user_gag set cancel=1 , cancel_time=? where account=? and cancel=?;
        String sql = new StringBuilder()
                .append("update ").append(UserGagTable.name)
                .append(" set ").append(UserGagTable.Field.cancel).append("=").append(UserGagTable.Status.CANCEL)
                .append(",").append(UserGagTable.Field.cancelTime).append("=?")
                .append(" where ").append(UserGagTable.Field.account).append("=?")
                .append(" and ").append(UserGagTable.Field.cancel).append("=?")
                .toString();

        ChatServerDbConnectUnit chatServerDbConnectUnit = DBUtil.getInstance().executeUpdate(
                sql,
                new Object[]{TimeUtil.getInstance().getTimeStampNow(), account, UserNoLoginTable.Status.NO_LOGIN});

        if (chatServerDbConnectUnit != null) {
            return chatServerDbConnectUnit.getSuccessRow();
        } else {
            return -1;
        }
    }

    /**
     * 用户是否处于禁言中
     *
     * @param account
     * @return
     */
    public boolean isGag(String account) {
        return DBUtil.getInstance().isExit(
                UserGagTable.name,
                new String[]{UserGagTable.Field.account, UserGagTable.Field.cancel},
                new Object[]{account, UserGagTable.Status.GAG});
    }

    /**
     * 增加禁封登录用户
     *
     * @param account 账户
     * @param reason  原因
     * @return 禁言表该条数据对应的id 如果没有则返回-1
     */
    public long addNoLoginAccount(String account, String reason, Timestamp startTime, Timestamp endTime) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put(UserNoLoginTable.Field.account, account);
        conditions.put(UserNoLoginTable.Field.reason, reason);
        conditions.put(UserNoLoginTable.Field.startTime, startTime);
        conditions.put(UserNoLoginTable.Field.endTime, endTime);

        Object result = DBUtil.getInstance().insert(UserNoLoginTable.name, conditions);
        if (reason != null) {
            return Long.parseLong(result.toString());
        } else {
            return -1;
        }

    }

    /**
     * 取消禁封登录
     *
     * @param account
     * @return 影响的行数，-1 执行失败
     */
    public int cancelNoLoginAccount(String account) {
        //update user_no_login set cancel=1 , cancel_time=? where account=? and cancel=?;
        String sql = new StringBuilder()
                .append("update ").append(UserNoLoginTable.name)
                .append(" set ").append(UserNoLoginTable.Field.cancel).append("=").append(UserNoLoginTable.Status.CANCEL)
                .append(",").append(UserNoLoginTable.Field.cancelTime).append("=?")
                .append(" where ").append(UserNoLoginTable.Field.account).append("=?")
                .append(" and ").append(UserNoLoginTable.Field.cancel).append("=?")
                .toString();

        ChatServerDbConnectUnit chatServerDbConnectUnit = DBUtil.getInstance().executeUpdate(
                sql,
                new Object[]{TimeUtil.getInstance().getTimeStampNow(), account, UserNoLoginTable.Status.NO_LOGIN});

        if (chatServerDbConnectUnit != null) {
            return chatServerDbConnectUnit.getSuccessRow();
        } else {
            return -1;
        }
    }

    /**
     * 用户是否处于禁封登录中
     *
     * @param account
     * @return
     */
    public boolean isNoLogin(String account) {
        return DBUtil.getInstance().isExit(
                UserNoLoginTable.name,
                new String[]{UserNoLoginTable.Field.account, UserNoLoginTable.Field.cancel},
                new Object[]{account, UserNoLoginTable.Status.NO_LOGIN});
    }

    /**
     * 根据昵称查找符合条件的用户
     *
     * @param nickName 昵称
     * @return
     */
    public List<UserInfo> findUserForNickName(String nickName) {
        return findUser(UserTable.Field.nickName, nickName);
    }

    /**
     * 根据账号查找符合条件的用户
     *
     * @param account
     * @return
     */
    public List<UserInfo> findUserForAccount(String account) {
        return findUser(UserTable.Field.account, account);
    }

    /**
     * 查找好友
     *
     * @param columnName
     * @param nickName
     * @return
     */
    private List<UserInfo> findUser(String columnName, String nickName) {
        //select id,account,nick_name,sex from user where nick_name = ?
        String sql = new StringBuilder()
                .append("select ")
                .append(UserTable.Field.id).append(",")
                .append(UserTable.Field.account).append(",")
                .append(UserTable.Field.nickName).append(",")
                .append(UserTable.Field.sex)
                .append(" from ")
                .append(UserTable.name)
                .append(" where ")
                .append(columnName).append(" like ?")
                .toString();

        List<UserInfo> resultList = new ArrayList<>();

        ChatServerDbConnectUnit chatServerDbConnectUnit = DBUtil.getInstance().executeQuery(sql, new Object[]{"%" + nickName + "%"});
        ResultSet resultSet = chatServerDbConnectUnit.getResultSet();
        try {
            while (resultSet.next()) {
                UserInfo userInfo = new UserInfo();
                userInfo.setId(resultSet.getLong(1));
                userInfo.setAccount(resultSet.getString(2));
                userInfo.setNickname(resultSet.getString(3));
                userInfo.setSex(resultSet.getString(4));
                resultList.add(userInfo);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }

        return resultList;
    }

    /**
     * 根据账户查找密码
     */
    public String getPassWord(String account) {
        //select password from user where account=?
        String sql = new StringBuilder()
                .append("select ").append(UserTable.Field.password)
                .append(" from ").append(UserTable.name)
                .append(" where ").append(UserTable.Field.account).append("=?")
                .toString();

        ChatServerDbConnectUnit chatServerDbConnectUnit = DBUtil.getInstance().executeQuery(sql, new Object[]{account});
        ResultSet resultSet = chatServerDbConnectUnit.getResultSet();

        String password = null;
        try {
            while (resultSet.next()) {
                password = resultSet.getString(1);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        } finally {
            chatServerDbConnectUnit.close();
        }
        return password;
    }

    /**
     * 插入离线数据
     *
     * @param account
     * @param messagePacket
     * @return 是否插入成功
     */
    public boolean saveOfflineMessage(String account, MessagePacket messagePacket) {
        Map<String, Object> sqlMap = new HashMap<>();
        sqlMap.put(OfflineMessageTable.Field.account, account);
        sqlMap.put(OfflineMessageTable.Field.messageType, messagePacket.getMessageType());
        sqlMap.put(OfflineMessageTable.Field.commandType, messagePacket.getCommand());
        sqlMap.put(OfflineMessageTable.Field.message, messagePacket.getMessage());
        sqlMap.put(OfflineMessageTable.Field.createTime, TimeUtil.getInstance().getTimeStampNow());

        Object data = DBUtil.getInstance().insert(OfflineMessageTable.name, sqlMap);

        if (data != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获得离线数据
     *
     * @param account   账户
     * @param loginTime 登录时间
     * @return
     */
    public List<MessagePacket> getOfflineMessage(String account, Timestamp loginTime) {
        //select command_type,message_type,message where account=? and create_time < ? and is_send =?
        String sql = new StringBuilder()
                .append("select ")
                .append(OfflineMessageTable.Field.commandType).append(", ")
                .append(OfflineMessageTable.Field.messageType).append(", ")
                .append(OfflineMessageTable.Field.message)
                .append(" from ").append(OfflineMessageTable.name)
                .append(" where ")
                .append(OfflineMessageTable.Field.account).append("=? and ")
                .append(OfflineMessageTable.Field.createTime).append("<? and ")
                .append(OfflineMessageTable.Field.isSend).append("=?")
                .toString();

        ChatServerDbConnectUnit chatServerDbConnectUnit =
                DBUtil.getInstance().executeQuery(sql, new Object[]{account, loginTime, OfflineMessageTable.Status.NOT_SEND});

        ResultSet resultSet = chatServerDbConnectUnit.getResultSet();

        List<MessagePacket> messagePackets = new LinkedList<>();
        try {
            while (resultSet.next()) {
                int commandType = resultSet.getInt(1);
                int messageType = resultSet.getInt(2);
                String json = resultSet.getString(3);

                MessagePacket messagePacket = new MessagePacket();
                messagePacket.setCommand(commandType);
                messagePacket.setMessageType(messageType);
                messagePacket.setMessage(json);
                messagePacket.setMessageLength(json.getBytes().length);
                messagePackets.add(messagePacket);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            chatServerDbConnectUnit.close();
        }

        return messagePackets;
    }
}

