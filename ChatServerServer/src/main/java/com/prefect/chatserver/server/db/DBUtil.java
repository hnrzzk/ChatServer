package com.prefect.chatserver.server.db;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 数据库操作相关工具类
 * Created by zhangkai on 2016/12/26.
 */
public class DBUtil {
    private final static Logger logger = LoggerFactory.getLogger(DBUtil.class);

    private static class DBUtilHandler {
        private static DBUtil instance = new DBUtil();
    }

    public static DBUtil getInstance() {
        return DBUtilHandler.instance;
    }

    private DBManager dbManager = null;

    private DBUtil() {
        this.dbManager = DBManager.getInstance();
    }

    /**
     * 执行查询操作
     *
     * @param sql
     * @return 查询得到的结果集
     */
    public ChatServerDbConnectUnit executeQuery(String sql, Object[] values) {
        ResultSet resultSet = null;
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = this.dbManager.getConnection();
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            for (int i = 0; i < values.length; i++) {
                statement.setObject(i + 1, values[i]);
            }
            resultSet = statement.executeQuery();

        } catch (SQLException e) {
            DBManager.closeConnection(connection);
            DBManager.closeStatement(statement);
            DBManager.closeResultSet(resultSet);
            logger.error(String.format("sql execute error, sql[%s] values[%s] ", sql, values), e);
        }

        return new ChatServerDbConnectUnit(resultSet, statement, connection);
    }

    /**
     * 执行update操作
     *
     * @param sql
     * @return sql影响的行数
     */
    public ChatServerDbConnectUnit executeUpdate(String sql, Object[] values) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = this.dbManager.getConnection();
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < values.length; i++) {
                statement.setObject(i + 1, values[i]);
            }

            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
        } catch (SQLException e) {
            DBManager.closeResultSet(resultSet);
            DBManager.closeStatement(statement);
            DBManager.closeConnection(connection);
            logger.error(String.format("sql execute error, sql[%s] values[%s] ", sql, values), e);
            return null;
        }

        return new ChatServerDbConnectUnit(resultSet, statement, connection);
    }

    /**
     * 查询数据库中是否存在在某个值
     *
     * @param table  数据库表名
     * @param column 字段名
     * @param value  要查询的值
     * @return 是否存在
     */
    public boolean isExit(String table, String column, String value) {
        String sql = String.format(
                "select * from %s where %s = ?",
                table, column);

        ChatServerDbConnectUnit connectUnit = this.executeQuery(sql, new Object[]{value});

        return resultSetIsEmpty(connectUnit);
    }

    /**
     * 查询数据库中是否存在某些值
     *
     * @param table   表名
     * @param columns 列名
     * @param values  字段数组
     * @return 是否存在
     */
    public boolean isExit(String table, String[] columns, Object[] values) {
        StringBuilder stringBuilder = new StringBuilder("select * from " + table + " where 1=1");
        for (int i = 0; i < columns.length; i++) {
            stringBuilder.append(
                    String.format(" and %s = ?", columns[i]));
        }
        ChatServerDbConnectUnit connectUnit = this.executeQuery(stringBuilder.toString(), values);

        return resultSetIsEmpty(connectUnit);
    }

    /**
     * 判断结果集是否为空
     *
     * @param connectUnit
     * @return 结果集是否是空
     */
    public boolean resultSetIsEmpty(ChatServerDbConnectUnit connectUnit) {
        boolean flag = false;
        try {
            while (connectUnit.getResultSet().next()) {
                flag = true;
                break;
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        } finally {
            connectUnit.close();
        }
        return flag;
    }

    /**
     * 执行插入操作 返回受影响的key 列表
     *
     * @param tableName 数据表
     * @param data      要插入的数据
     * @return 返回一个ResultSet里面保存了受影响的key
     */
    public Object executeInsert(String tableName, Map<String, Object> data) {
        if (data.isEmpty()) {
            return null;
        }

        StringBuilder keyNameList = new StringBuilder();
        StringBuilder valueList = new StringBuilder();

        int i = 0;
        Object[] objects = new Object[data.size()];

        Iterator iterator = data.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            objects[i++] = entry.getValue();
            keyNameList.append(String.format("%s,", entry.getKey()));
            valueList.append("?,");
        }

        //删除最后一个逗号
        keyNameList.deleteCharAt(keyNameList.length() - 1);
        valueList.deleteCharAt(valueList.length() - 1);

        StringBuilder sql = new StringBuilder(
                String.format("insert into %s (%s) values (%s)", tableName, keyNameList, valueList));


        Object key = null;
        ChatServerDbConnectUnit chatServerDbConnectUnit = this.executeUpdate(sql.toString(), objects);
        ResultSet resultSet = chatServerDbConnectUnit.getResultSet();

        //得到主键
        try {
            while (resultSet.next()) {
                key = resultSet.getObject(1);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        } finally {
            chatServerDbConnectUnit.close();
        }

        return key;
    }

    /**
     * 删除数据库的数据
     * @param table 表名
     * @param condition 条件
     * @return 受影响的id列表
     */
    public List<Long>  deleteRow(String table, Map<String, Object> condition) {
        Object[] values = new Object[condition.size()];

        StringBuilder conditionSql = new StringBuilder("");
        Iterator iterator = condition.entrySet().iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            values[i++] = entry.getValue();
            conditionSql.append(" and ").append(entry.getKey()).append("=?");
        }

        String sql = new StringBuilder()
                .append("Delete from ").append(table).append(" where 1=1").append(conditionSql)
                .toString();

        ChatServerDbConnectUnit chatServerDbConnectUnit = this.executeUpdate(sql, values);
        ResultSet resultSet = chatServerDbConnectUnit.getResultSet();
        List<Long> list=new ArrayList<>();

        try {
            while (resultSet.next()) {
                list.add(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        } finally {
            chatServerDbConnectUnit.close();
        }
        return list;
    }
}
