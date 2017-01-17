package com.prefect.chatserver.server.utils;

import org.apache.commons.collections.map.LRUMap;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 基于commons.collections.map.LRUMap实现的线程安全的LRUCache
 * Created by zhangkai on 2017/1/16.
 */
public class LRUCache extends LRUMap {
    Lock lock;

    public LRUCache(int bufferSize) {
        super(bufferSize);
        lock=new ReentrantLock();
    }

    @Override
    public Object get(Object key) {
        Object obj;

        lock.lock();
        try {
            obj = super.get(key);
        } finally {
            lock.unlock();
        }
        return obj;
    }

    @Override
    public Object put(Object key, Object value) {
        Object obj;

        lock.lock();
        try {
            obj = super.put(key, value);
        } finally {
            lock.unlock();
        }
        return obj;
    }

    @Override
    public Object remove(Object key) {
        Object obj;

        lock.lock();
        try {
            obj = super.remove(key);
        } finally {
            lock.unlock();
        }
        return obj;
    }

}
