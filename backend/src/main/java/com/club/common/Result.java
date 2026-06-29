package com.club.common;

public class Result {
    private Integer code;
    private String message;
    private Object data;

    public static Result success(Object data) {
        Result r = new Result();
        r.setCode(200);
        r.setMessage("success");
        r.setData(data);
        return r;
    }

    public static Result success() {
        return success(null);
    }

    public static Result error(String message) {
        Result r = new Result();
        r.setCode(500);
        r.setMessage(message);
        return r;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
