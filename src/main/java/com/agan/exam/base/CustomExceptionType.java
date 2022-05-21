package com.agan.exam.base;

public enum CustomExceptionType {

    USER_INPUT_ERROR(400,"您输入的数据错误或您没有权限访问资源！"),
    SYSTEM_ERROR (500,"系统出现异常，请您稍后再试或联系管理员！"),
    OTHER_ERROR(999,"系统出现未知异常，请联系管理员！");

    CustomExceptionType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final String desc;//异常类型中文描述

    private final int code; //code

    public String getDesc() {
        return desc;
    }

    public int getCode() {
        return code;
    }
}