package com.example.eric.Http;

/**
 * Created by Eric
 *
 * Http类型区分
 */

public class HttpTypeHelper {

    public class  HttpMethod {
        public static final int HttpMethod_Get = 1;  // Get
        public static final int HttpMethod_Post = 2; // Post
        public static final int HttpMethod_Delete = 3; // Delete
    }

    /**
     * 服务器返回数据类型， 可由访问处显示指定，也可以判断返回数据结构。
     */
    public enum BodyDataType {
        UNKNOWN,
        STRING,         //String
        JSON_OBJECT,    //json对象
        JSON_ARRAY,     //json数组
        XML,            //XML
        SHOP,           //shop
    }

    public class ErrorCode {
        public final static int UNKNOWN_ERROR = 10001;
        public final static int SERVER_ERROR = 10002;
        public final static int NETWORK_UNAVAILABLE = 10011;
        public final static int RESPONSE_PARSE_ERROR = 10012;
        public final static int NULL_EXCEPTION = 10013;
    }

    public class ErrorMessage {
        public final static String UNKNOWN_ERROR = "未知错误";
        public final static String SERVER_ERROR = "服务器错误";
        public final static String NETWORK_UNAVAILABLE = "连接失败，请检查你的网络设置";
        public final static String RESPONSE_PARSE_ERROR = "数据解析错误";
    }
}
