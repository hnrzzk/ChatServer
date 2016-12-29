package com.prefect.chatserver.server.util.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
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
     * 创建分组，如存在则不创建
     *
     * @param account  账户名
     * @param category 分组名
     * @return 分组id：>0
     * 分组已存在：0
     * 失败：-1
     */
    public long creatCategory(String account, String category) {
        DBUtil dbUtil = DBUtil.getInstance();
        long result = -1;
        //该用户下没有改分组
        if (!dbUtil.isExit("user", new String[]{"user_account", "name"}, new String[]{account, category})) {
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("user_account", account);
            dataMap.put("name", category);

            ChatServerDbConnectUnit chatServerDbConnectUnit=dbUtil.executeInsert("category", dataMap);

            if (chatServerDbConnectUnit==null){
                return -1;
            }
            ResultSet resultSet = chatServerDbConnectUnit.getResultSet();

            long categoryId;
            try {
                //得到categoryId
                while (resultSet.next()) {
                    categoryId = resultSet.getLong(1);
                    break;
                }
                Map<String, Object> friendDataMap = new HashMap<>();


                dbUtil.executeInsert("friends",friendDataMap);

            } catch (SQLException e) {
                result = -1;
                logger.error(e.getMessage(), e);
            }
        } else {
            result = 0;
        }
        return result;
    }


}
