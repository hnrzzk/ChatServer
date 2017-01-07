package com.prefect.chatserver.commoms.utils.moudel;

/**
 * Created by zhangkai on 2016/12/26.
 */
public class CategoryInfo {

    long id;

    /**
     * 用户id
     */
    long userId;

    /**
     * 分组名称
     */
    String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
