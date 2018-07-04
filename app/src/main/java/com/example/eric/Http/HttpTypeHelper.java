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
}
