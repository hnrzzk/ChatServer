package com.prefect.chatserver.client.process.interactive;

import com.prefect.chatserver.client.process.request.operate.*;
import com.prefect.chatserver.client.utils.Interactive;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;


/**
 * 处理用户控制台输入的逻辑
 * Created by zhangkai on 2016/12/29.
 */
public class UserInteractive implements Runnable {
    private final static Logger logger = LoggerFactory.getLogger(UserInteractive.class);

    @Override
    public void run() {
        Interactive.getInstance().printlnToConsole("开始接受用户命令:");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String inputStr = scanner.nextLine();

            try {
                parseCommand(inputStr);
            } catch (Exception e) {
                logger.error("command is illegal", e);
            }
        }
    }

    /**
     * 处理用户输入的命令
     *
     * @param userInput 用户在控制台的输入
     */
    private void parseCommand(String userInput) throws Exception {
        String[] strings = userInput.split(" ");

        OperatePo operatePo=OperateFactory.getClass(strings);

        if (operatePo!=null){
            operatePo.process();
        }
    }

}
