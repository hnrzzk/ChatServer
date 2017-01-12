package com.prefect.chatserver.server.db;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
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
     * @param sql     sql语句
     * @param columns 参数值
     * @return 空则返回null
     */
    List executeQuery(String sql, Object[] columns) {
        List result = null;

        Session session = this.dbManager.getSession();

        if (session.isOpen()){
            try {
                Query query = session.createQuery(sql);

                for (int i = 0; i < columns.length; i++) {
                    query.setParameter(i, columns[i]);
                }

                result = query.list();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            } finally {
                session.close();
            }
        }

        return result;
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
        Session session = this.dbManager.getSession();
        Transaction transaction=null;
        try {
            transaction=session.beginTransaction();
            Query query = session.createQuery(sql);

            for (int i = 0; i < columns.length; i++) {
                query.setParameter(i, columns[i]);
            }
            successNum = query.executeUpdate();
            transaction.commit();//提交事务，将保存在session中的缓存提交到数据库
        } catch (Exception e) {
            if (transaction!=null){
                transaction.rollback();
            }
            logger.error(e.getMessage(), e);
        } finally {
            session.close();
        }

        return successNum;
    }


    /**
     * 插入数据
     *
     * @param object
     */
    void executeInsert(Object object) throws Exception{
        Session session = this.dbManager.getSession();
        Transaction transaction=null;
        try {
            transaction=session.beginTransaction();
            session.save(object);
            transaction.commit();//提交事务，将保存在session中的缓存提交到数据库
        } catch (HibernateException e) {
            if (transaction!=null){
                transaction.rollback();
            }
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * 从数据库中删除数据
     * @param object 要删除的数据
     * @throws Exception
     */
    void executeDelete(Object object) throws Exception{
        Session session = this.dbManager.getSession();
        Transaction transaction=null;
        try {
            transaction=session.beginTransaction();
            session.delete(object);
            transaction.commit();//提交事务，将保存在session中的缓存提交到数据库
        } catch (HibernateException e) {
            if (transaction!=null){
                transaction.rollback();
            }
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

}
