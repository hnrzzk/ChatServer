package com.prefect.chatserver.server.db;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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
     * @param sql     sql语句
     * @param columns 参数值
     * @param resultNum 查询结果的最大数目
     * @return 空则返回null
     */
    List executeQuery(String sql, Object[] columns, int resultNum) {
        List result = null;

        Session session = null;
        try {
            session = this.dbManager.getSession();
            Query query = session.createQuery(sql);

            if (columns != null) {
                for (int i = 0; i < columns.length; i++) {
                    query.setParameter(i, columns[i]);
                }
            }

            if (resultNum > 0) {
                query.setMaxResults(resultNum);
            }


            result = query.list();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            System.exit(0);
        } finally {
            if (session != null) {
                session.close();
                session = null;
            }

        }

        return result;
    }

    /**
     * 执行查询操作 不指定MaxResults
     * @param sql
     * @param columns
     * @return
     */
    List executeQuery(String sql, Object[] columns) {
        return this.executeQuery(sql,columns,0);
    }


    /**
     * 执行update操作
     *
     * @param sql     sql语句
     * @param columns 参数名称与值的对应表
     * @return
     */
    int executeUpdate(String sql, Object[] columns) {
        int successNum = -1;
        Session session = null;
        Transaction transaction = null;
        try {
            session = this.dbManager.getSession();
            transaction = session.beginTransaction();
            Query query = session.createQuery(sql);

            for (int i = 0; i < columns.length; i++) {
                query.setParameter(i, columns[i]);
            }
            successNum = query.executeUpdate();
            transaction.commit();//提交事务，将保存在session中的缓存提交到数据库
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error(e.getMessage(), e);
            System.exit(0);
        } finally {
            if (session != null) {
                session.close();
                session = null;
            }
        }

        return successNum;
    }


    /**
     * 插入数据
     *
     * @param object
     */
    void executeInsert(Object object) throws Exception {
        Session session = null;
        Transaction transaction = null;
        try {
            session = this.dbManager.getSession();
            transaction = session.beginTransaction();
            session.save(object);
            transaction.commit();//提交事务，将保存在session中的缓存提交到数据库
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error(e.getMessage(), e);
            System.exit(0);
            throw e;
        } finally {
            if (session != null) {
                session.close();
                session = null;
            }
        }
    }

}
