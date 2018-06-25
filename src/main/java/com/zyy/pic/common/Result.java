package com.zyy.pic.common;

import java.util.List;

/**
 * Created by Administrator on 2018/5/16.
 */
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(){
        this(200, "交易成功");
    }

    public static Result ok(){
        return new Result(200,"交易成功");
    }

    public static Result error(int code, String msg){
        return new Result(code, msg);
    }
}
