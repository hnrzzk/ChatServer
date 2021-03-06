package com.prefect.chatserver.commoms.utils.moudel;

/**
 * 服务器对客户端请求的响应消息
 * Created by zhangkai on 2016/12/29.
 */
public class ACKMessage {

    //执行结果
    private boolean actionResult;

    //详细信息
    private String message;

    public boolean getActionResult() {
        return actionResult;
    }

    public void setActionResult(boolean actionResult) {
        this.actionResult = actionResult;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
