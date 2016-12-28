package com.prefect.chatserver.commoms.util.db;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;

/**
 * 数据库操作相关工具类
 * Created by zhangkai on 2016/12/26.
 */
public class DBUtil {
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
    public ResultSet executeQuery(String sql) {
        ResultSet resultSet = null;
        Connection connection = null;
        Statement statement = null;
        try {
            connection = this.dbManager.getConnection();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeConnection(connection);
            DBManager.closeStatement(statement);
        }

        return resultSet;
    }

    /**
     * 执行update操作
     *
     * @param sql
     * @return sql影响的行数
     */
    public int executeUpdate(String sql) {
        int executeNum = 0;
        Connection connection = null;
        Statement statement = null;
        try {
            connection = this.dbManager.getConnection();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            executeNum = statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeConnection(connection);
            DBManager.closeStatement(statement);
        }

        return executeNum;
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
                "select * from %s where %s = '%s'",
                table, column, value);

        ResultSet resultSet = this.executeQuery(sql);
        boolean flag = false;
        try {
            while (resultSet.next()) {
                flag = true;
                break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeResultSet(resultSet);
        }

        return flag;
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
        StringBuilder stringBuilder = new StringBuilder("select * from " + table + "where 1=1");
        for (int i = 0; i < columns.length; i++) {
            stringBuilder.append(
                    String.format(" and %s = '%s'",
                            columns[i], values[i]));
        }
        ResultSet resultSet = this.executeQuery(stringBuilder.toString());
        boolean flag = false;
        try {
            while (resultSet.next()) {
                flag = true;
                break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public int executeInsert(String tableName, Map<String, Object> data) {
        if (data.isEmpty()) {
            return 0;
        }

        StringBuilder keyNameList = new StringBuilder();
        StringBuilder valueList = new StringBuilder();

        Iterator iterator = data.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            keyNameList.append(String.format("%s,", entry.getKey()));
            valueList.append(String.format("'%s',", entry.getValue()));
        }

        //删除最后一个逗号
        keyNameList.deleteCharAt(keyNameList.length() - 1);
        valueList.deleteCharAt(valueList.length() - 1);


        StringBuilder sql = new StringBuilder(
                String.format("insert into %s (%s) values (%s)", tableName, keyNameList, valueList));

        System.out.println(sql);
        return this.executeUpdate(sql.toString());
    }
}
