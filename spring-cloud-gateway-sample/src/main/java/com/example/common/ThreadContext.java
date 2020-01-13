package com.example.common;

/**
 * @Description: 不适合在gateway责任链中使用，线程会换
 * @author: LinQin
 * @date: 2020/01/08
 */
public class ThreadContext {

    private ThreadContext() {
    }

    private static final ThreadLocal<String> CURRENT_URI = new ThreadLocal<>();

    private static final ThreadLocal<UserInfo> USER_INFO = new ThreadLocal<>();

    private static final ThreadLocal<ClientInfo> CLIENT_INFO = new ThreadLocal<>();

    private static final ThreadLocal<RequestInfo> REQUEST_INFO = new ThreadLocal<>();


    public static ThreadLocal<String> getCurrentUri() {
        return CURRENT_URI;
    }

    public static ThreadLocal<UserInfo> getUserInfo() {
        return USER_INFO;
    }

    public static ThreadLocal<ClientInfo> getClientInfo() {
        return CLIENT_INFO;
    }

    public static ThreadLocal<RequestInfo> getRequestInfo() {
        return REQUEST_INFO;
    }

    public static void clear() {
        // todo 用完要清空
        CURRENT_URI.remove();
        USER_INFO.remove();
        CLIENT_INFO.remove();
        REQUEST_INFO.remove();
    }
}
