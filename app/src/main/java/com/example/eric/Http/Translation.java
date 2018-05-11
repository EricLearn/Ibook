package com.example.eric.Http;

/**
 * Created by Eric on 2018/5/3.
 */

public class Translation {
    private int status;

    private content content;
    private static class content {
        private String from;
        private String to;
        private String vendor;
        private String out;
        private int errNo;
    }

    public String verdor() {
        return content.vendor;
    }
    //定义 输出返回数据 的方法
    public void show() {

    }
}
