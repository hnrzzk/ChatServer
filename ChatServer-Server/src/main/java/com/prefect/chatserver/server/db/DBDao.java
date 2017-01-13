package com.prefect.chatserver.server.db;

import com.prefect.chatserver.commoms.utils.MessagePacket;
import com.prefect.chatserver.commoms.utils.TimeUtil;
import com.prefect.chatserver.commoms.utils.moudel.UserInfo;
import com.prefect.chatserver.server.db.tables.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.*;

/**
 * 持久层操作逻辑
 * Created by zhangkai on 2016/12/29.
 */
public class DBDao {
    private final static Logger logger = LoggerFactory.getLogger(DBDao.class);

    DBUtil dbUtil;

    private DBDao() {
        dbUtil = DBUtil.getInstance();
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

        String sql = "select 1 from UserTable user where user.account=?";
        List result = dbUtil.executeQuery(sql, new Object[]{account});

        return !(null == result || result.isEmpty());
    }

    /**
     * 创建分组，如存在则不创建,如果存在则返回分组id
     *
     * @param account  账户名
     * @param category 分组名
     * @return 分组id：>0
     * 失败：-1
     */
    private long createCategory(String account, String category) {
        //判断分组是否存在
        boolean categoryExist = false;
        String sqlCategoryExist = "select 1 from "+CategoryTable.class.getName() +" cate  where cate.userAccount=? and cate.name =?";
        List result = dbUtil.executeQuery(sqlCategoryExist, new Object[]{account, category});

        categoryExist = !(null == result || result.isEmpty());

        if (!categoryExist) {
            //如果不存在则创建
            CategoryTable categoryTable = new CategoryTable();

            categoryTable.setName(category);
            categoryTable.setUserAccount(account);

            try {
                dbUtil.executeInsert(categoryTable);
            } catch (Exception e) {
                logger.error("Create category failed! :" + e.getMessage(), e);
                return -1;
            }
            return categoryTable.getId();
        } else {
            String sql = "select cate.id from CategoryTable cate where cate.userAccount=? and cate.name=?";

            return (long) dbUtil.executeQuery(sql, new Object[]{account, category}).get(0);
        }
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
        if (null == userAccount || null == friendAccount) {
            return -1;
        }

        boolean friendRelationshipExist = false;
        String sqlFriendRelationship = "select 1 from FriendsTable friend where friend.userAccount=? and friend.friendAccount=?";
        List resultFRE = dbUtil.executeQuery(sqlFriendRelationship, new Object[]{userAccount, friendAccount});

        friendRelationshipExist = !(null == resultFRE || resultFRE.isEmpty());

        if (!friendRelationshipExist) {
            //如果好友关系不存在，则新增
            long categoryId = DBDao.getInstance().createCategory(userAccount, categoryName);
            if (categoryId > 0) {
                FriendsTable friendsTable = new FriendsTable();
                friendsTable.setUserAccount(userAccount);
                friendsTable.setFriendAccount(friendAccount);
                friendsTable.setCategoryId(categoryId);
                friendsTable.setCreateTime(TimeUtil.getInstance().getTimeStampNow());

                try {
                    dbUtil.executeInsert(friendsTable);
                } catch (Exception e) {
                    logger.error("add friend failed:" + e.getMessage(), e);
                    return -1;
                }
                return friendsTable.getId();
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
     * @param onLineStatus (1在线用户 0离线用户 -1所有用户)
     * @return
     */
    public List<String> getFriendListForOnlineStatus(String uesrAccount, int onLineStatus) {
        if (null == uesrAccount) {
            return null;
        }

        //select friends.friend_account from friends left join user on friends.friend_account=user.account
        // where friend.user_account=? and user.is_online = ?;
        String sql = new StringBuilder()
                .append("select friends.friendAccount from ").append(FriendsTable.class.getName()).append(" friends left join UserTable user on friends.friendAccount = user.account")
                .append(" where friends.userAccount=? and user.onlineStatus=?")
                .toString();

        List<String> result = (List<String>) dbUtil.executeQuery(sql, new Object[]{uesrAccount, onLineStatus});

        return result;
    }

    /**
     * 修改用户的在线状态
     *
     * @param account      用户帐户
     * @param onlineStatus (1:在线，0：离线)
     * @return
     */
    public boolean changeAccountOnlineStatus(String account, int onlineStatus) {
        if (null == account) {
            return false;
        }

        boolean result = false;

        String sql = "update " + UserTable.class.getName() + " user set user.onlineStatus = ? where user.account=?";

        int successNum = dbUtil.executeUpdate(sql, new Object[]{onlineStatus, account});

        return successNum > 0;

    }

    /**
     * 增加黑名单
     *
     * @param userAccount   用户帐户
     * @param friendAccount 要添加黑名单的账户
     * @return
     */
    public long addBlackListInfo(String userAccount, String friendAccount) {
        if (null == userAccount || null == friendAccount) {
            return -1;
        }

        //如果用户关系不存在
        if (!isInBlackList(userAccount, friendAccount)) {
            BlackListTable blackListTable = new BlackListTable();
            blackListTable.setUserAccount(userAccount);
            blackListTable.setBlackAccount(friendAccount);
            blackListTable.setCreateTime(TimeUtil.getInstance().getTimeStampNow());

            try {
                dbUtil.executeInsert(blackListTable);
                return blackListTable.getId();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                return -1;
            }

        } else {
            return 0;
        }
    }

    /**
     * 是否在黑名单中
     *
     * @param userAccount 用户帐号
     * @param account     目标账号
     * @return 是否在黑名单中
     */
    public boolean isInBlackList(String userAccount, String account) {

        String sqlExist = "select 1 from " + BlackListTable.class.getName() + " blacklist where blacklist.userAccount=? and blacklist.blackAccount=?";
        List resultExist = dbUtil.executeQuery(sqlExist, new Object[]{userAccount, account});

        return !(null == resultExist || resultExist.isEmpty());
    }

    /**
     * 移出好友
     *
     * @param userAccount   用户帐号
     * @param friendAccount 目标账号
     * @return 是否删除成功
     */
    public boolean removeFriendRelationShip(String userAccount, String friendAccount) {
        String sqlDelete = "delete from " + FriendsTable.class.getName() + " friends where friends.userAccount=? and friends.friendAccount=?";

        return dbUtil.executeUpdate(sqlDelete, new Object[]{userAccount, friendAccount}) > 0;
    }

    /**
     * 移出黑名单
     *
     * @param userAccount  用户帐号
     * @param blackAccount 目标账号
     * @return 是否删除成功
     */
    public boolean removeBlackRelationShip(String userAccount, String blackAccount) {
        String sqlDelete = "delete from " + BlackListTable.class.getName() + " black where black.userAccount=? and black.blackAccount=?";

        return dbUtil.executeUpdate(sqlDelete, new Object[]{userAccount, blackAccount}) > 0;
    }


    /**
     * 检查该用户是否是管理员
     *
     * @param userAccount 用户帐号
     * @return
     */
    public boolean authorityCheck(String userAccount) {
        String sqlExist = "select 1 from " + UserTable.class.getName() + " user where user.account=? and user.authority=?";

        List resultExist = dbUtil.executeQuery(sqlExist, new Object[]{userAccount, UserTable.Status.ADMINISTER});

        return !(null == resultExist || resultExist.isEmpty());
    }

    /**
     * 增加禁言用户
     *
     * @param account 账户
     * @param reason  原因
     * @return 禁言表该条数据对应的id 如果没有则返回-1
     */
    public long addGagAccount(String account, String reason, Timestamp startTime, Timestamp endTime) {
        UserGagTable userGagTable = new UserGagTable();
        userGagTable.setAccount(account);
        userGagTable.setReason(reason);
        userGagTable.setStartTime(startTime);
        userGagTable.setEndTime(endTime);

        try {
            dbUtil.executeInsert(userGagTable);
            return userGagTable.getId();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return -1;
        }
    }

    /**
     * 取消用户禁言
     *
     * @param account 账户
     * @return 影响的行数，-1 执行失败
     */
    public int cancelGagAccount(String account) {
        String sql = "update " + UserGagTable.class.getName() +
                " gag set gag.cancel=? , gag.cancelTime=? where gag.account=? and gag.cancel=?";

        return dbUtil.executeUpdate(sql,
                new Object[]{
                        UserGagTable.Status.CANCEL,
                        TimeUtil.getInstance().getTimeStampNow(),
                        account,
                        UserGagTable.Status.GAG});
    }

    /**
     * 用户是否处于禁言中
     *
     * @param account 账户
     * @return 是否存在
     */
    public boolean isGag(String account) {
        String sqlExist = "select 1 from " + UserGagTable.class.getName() + " gag where gag.account=? and gag.cancel=?";

        List resultExist = dbUtil.executeQuery(sqlExist, new Object[]{account, UserGagTable.Status.GAG});

        return !(null == resultExist || resultExist.isEmpty());
    }

    /**
     * 增加禁封登录用户
     *
     * @param account 账户
     * @param reason  原因
     * @return 禁言表该条数据对应的id 如果没有则返回-1
     */
    public long addNoLoginAccount(String account, String reason, Timestamp startTime, Timestamp endTime) {
        UserNoLoginTable userNoLoginTable = new UserNoLoginTable();
        userNoLoginTable.setAccount(account);
        userNoLoginTable.setReason(reason);
        userNoLoginTable.setStartTime(startTime);
        userNoLoginTable.setEndTime(endTime);

        try {
            dbUtil.executeInsert(userNoLoginTable);
            return userNoLoginTable.getId();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
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

        String sql = "update " + UserNoLoginTable.class.getName() +
                " nologin set nologin.cancel=? nologin.cancelTime=? where nologin.account=? and nologin.cancel=?";

        return dbUtil.executeUpdate(sql,
                new Object[]{
                        UserNoLoginTable.Status.CANCEL,
                        TimeUtil.getInstance().getTimeStampNow(),
                        account,
                        UserNoLoginTable.Status.NO_CANCEL});
    }

    /**
     * 用户是否处于禁封登录中
     *
     * @param account
     * @return
     */
    public boolean isNoLogin(String account) {
        String sqlExist = "select 1 from " + UserGagTable.class.getName() + " gag where gag.account=? and gag.cancel=?";

        List resultExist = dbUtil.executeQuery(sqlExist, new Object[]{account, UserNoLoginTable.Status.NO_CANCEL});

        return !(null == resultExist || resultExist.isEmpty());
    }

    /**
     * 根据昵称查找符合条件的用户
     *
     * @param nickName 昵称
     * @return
     */
    public List<UserInfo> findUserForNickName(String nickName) {
        return findUser("nickName", nickName);
    }

    /**
     * 根据账号查找符合条件的用户
     *
     * @param account
     * @return
     */
    public List<UserInfo> findUserForAccount(String account) {
        return findUser("account", account);
    }

    /**
     * 查找好友
     *
     * @param columnName
     * @param nickName
     * @return
     */
    private List<UserInfo> findUser(String columnName, String nickName) {
        String sql = "select user.id,user.account,user.nickName,user.sex from " + UserTable.class.getName() + " user where user." + nickName + "=?";

        List queryList = dbUtil.executeQuery(sql, new Object[]{nickName});

        List<UserInfo> resultList = new ArrayList<>();
        for (Iterator iterator = queryList.iterator(); iterator.hasNext(); ) {
            Object[] objects = (Object[]) iterator.next();
            UserInfo userInfo = new UserInfo();
            userInfo.setId(Long.parseLong(objects[0].toString()));
            userInfo.setAccount(objects[1].toString());
            userInfo.setNickname(objects[2].toString());
            userInfo.setSex(objects[3].toString());

            resultList.add(userInfo);
        }

        return resultList;
    }

    /**
     * 根据账户查找密码
     */
    public String getPassWord(String account) {
        String sql = "select user.password from " + UserTable.class.getName() + " user where user.account=?";

        List queryResult = dbUtil.executeQuery(sql, new Object[]{account});

        if (null != queryResult && !queryResult.isEmpty()) {
            return queryResult.get(0).toString();
        } else {
            return null;
        }
    }

    /**
     * 插入离线数据
     *
     * @param account
     * @param messagePacket
     * @return 返回是否插入成功
     */
    public boolean saveOfflineMessage(String account, MessagePacket messagePacket) {

        OfflineMessageTable data = new OfflineMessageTable();
        data.setAccount(account);
        data.setCommandType(messagePacket.getCommand());
        data.setMessageType(messagePacket.getMessageType());
        data.setMessage(messagePacket.getMessage());
        data.setCreateTime(TimeUtil.getInstance().getTimeStampNow());

        long dataId = -1;
        try {
            dbUtil.executeInsert(data);
            dataId = data.getId();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return dataId > 0;
    }

    /**
     * 获得离线数据
     *
     * @param account   账户
     * @param loginTime 登录时间
     * @return
     */
    public List<MessagePacket> getOfflineMessage(String account, Timestamp loginTime) {
        String sql = "from " + OfflineMessageTable.class.getName() + " msg where msg.account=? and msg.createTime<? and msg.isSend=?";

        List<MessagePacket> messagePacketList = new ArrayList<>();
        List msgList = dbUtil.executeQuery(sql, new Object[]{account, loginTime, OfflineMessageTable.Status.NOT_SEND});
        for (Iterator iterator = msgList.iterator(); iterator.hasNext(); ) {
            Object obj = iterator.next();
            if (obj instanceof OfflineMessageTable) {
                try {
                    OfflineMessageTable offlineMessageTable = (OfflineMessageTable) obj;
                    MessagePacket messagePacket = new MessagePacket();
                    messagePacket.setCommand(offlineMessageTable.getCommandType());
                    messagePacket.setMessageType(offlineMessageTable.getMessageType());
                    messagePacket.setMessage(offlineMessageTable.getMessage());
                    messagePacket.setMessageLength(offlineMessageTable.getMessage().getBytes("utf-8").length);

                    messagePacketList.add(messagePacket);
                } catch (UnsupportedEncodingException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }

        //修改离线消息的发送状态
        changeOfflineMessageSendStatus(account, loginTime, OfflineMessageTable.Status.SEND);

        return messagePacketList;
    }

    /**
     * 修改离线消息的发送状态，从未发送改为 status
     *
     * @param account
     * @param loginTime
     * @param status
     * @return
     */
    private long changeOfflineMessageSendStatus(String account, Timestamp loginTime, int status) {
        String sql = "update " + OfflineMessageTable.class.getName() +
                " msg set msg.isSend=? where msg.account=? and msg.createTime<? and msg.isSend=?";

        return dbUtil.executeUpdate(sql, new Object[]{status, account, loginTime, OfflineMessageTable.Status.NOT_SEND});
    }

    /**
     * 注册用户
     *
     * @param info
     * @return >=0 用户id；-1 用户存在；-2 数据库操作失败; -3数据无效
     */
    public long siginAccount(UserInfo info) {
        if (null == info) {
            return -3;
        }

        if (accountIsExist(info.getAccount())) {
            //用户如果存在
            return -1;
        } else {
            //用户如果不存在
            UserTable userTable = new UserTable();
            userTable.setAccount(info.getAccount());
            userTable.setPassword(info.getPassword());
            userTable.setSex(info.getSex());
            userTable.setNickName(info.getNickname());
            userTable.setRegisterTime(TimeUtil.getInstance().getTimeStampNow());

            try {
                dbUtil.executeInsert(userTable);
                return userTable.getId();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                return -2;
            }
        }
    }

    /**
     * 获取用户在线状态
     *
     * @param account 账户
     * @return
     */
    public int getOnlineStatue(String account) {
        String sqlExist = "select user.onlineStatus from " + UserTable.class.getName() + " user where user.account=?";
        List resultExist = dbUtil.executeQuery(sqlExist, new Object[]{account});

        int result=(int) resultExist.get(0);
        return result;
    }

}

