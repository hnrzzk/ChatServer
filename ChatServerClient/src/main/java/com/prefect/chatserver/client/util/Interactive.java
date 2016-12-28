package com.prefect.chatserver.client.util;

/**
 * 控制台交互相关工具类
 * Created by zhangkai on 2016/12/28.
 */
public class Interactive {

    private Interactive(){}

    private static class InteractiveHandler{
        static Interactive instance=new Interactive();
    }

    public static Interactive getInstance(){
        return InteractiveHandler.instance;
    }

    synchronized public void printToConsole(String str){
        System.out.print(str);
    }

    synchronized public void printlnToConsole(String str){
        System.out.println(str);
    }
}
