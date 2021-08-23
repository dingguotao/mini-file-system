package com.iclouding.mfs.common;

/**
 * ResponseStatus
 * 统一请求状态
 * @author: iclouding
 * @date: 2021/7/31 21:05
 * @email: clouding.vip@qq.com
 */
public enum ResponseStatus {
    /**
     * 成功
     */
    SUCCESS(0, ""),

    /**
     *
     */
    FAILURE(-1, "未知错误");


    ResponseStatus(int status, String msg) {
        this.status = status;
        this.messages = msg;
    }

    private int status;
    private String messages;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }
}
