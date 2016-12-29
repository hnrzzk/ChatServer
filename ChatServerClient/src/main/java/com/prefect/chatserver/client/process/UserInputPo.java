package com.prefect.chatserver.client.process;

import com.prefect.chatserver.client.util.Interactive;

import java.util.Scanner;

/**
 * 处理用户控制台输入的逻辑
 * Created by zhangkai on 2016/12/29.
 */
public class UserInputPo implements Runnable {

    {
        Interactive.getInstance().printlnToConsole("开始接受用户命令:");
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            parseCommand(scanner.next());
        }
    }

    void parseCommand(String userInput) {
        System.out.println("获取输入：" + userInput);
    }
}
