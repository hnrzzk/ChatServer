package com.prefect.chatserver.client.utils;

import java.util.Scanner;

/**
 * 控制台交互相关工具类
 * Created by zhangkai on 2016/12/28.
 */
public class Interactive {
    Scanner scanner;

    private Interactive() {
        this.scanner=new Scanner(System.in);
    }

    private static class InteractiveHandler {
        static Interactive instance = new Interactive();
    }

    public static Interactive getInstance() {
        return InteractiveHandler.instance;
    }


    synchronized public void printToConsole(String str) {
        System.out.print(str);
    }

    /**
     * System.out.println的进一步封装 输出一行
     *
     * @param str
     */
    synchronized public void printlnToConsole(String str) {
        System.out.println(str);
    }

    /**
     * System.out.println的进一步封装 输出多行
     *
     * @param strs
     */
    synchronized public void printlnToConsole(String[] strs) {
        for (String item : strs) {
            System.out.println(item);
        }
    }

    synchronized public String getUserInto(){
        if (scanner.hasNext()){
            return scanner.nextLine();
        }else {
            return "";
        }
    }
}
