package com.example.common;

/**
 * @Description:
 * @author: LinQin
 * @date: 2020/01/08
 */
public class RequestInfo {
    private long requestTime;
    private String requestParam;

    public RequestInfo() {
        this(System.currentTimeMillis(), null);
    }


    public RequestInfo(long requestTime, String requestParam) {
        this.requestTime = requestTime;
        this.requestParam = requestParam;
    }

    public void setRequestParam(String requestParam) {
        this.requestParam = requestParam;
    }

    public long getRequestTime() {
        return requestTime;
    }

    public String getRequestParam() {
        return requestParam;
    }


}
