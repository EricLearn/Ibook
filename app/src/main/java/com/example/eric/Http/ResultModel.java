package com.example.eric.Http;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Eric on 2018/7/3.
 */

public class ResultModel
{
    private Object mResult;
    private HttpTypeHelper.BodyDataType mDataType;
    private HttpError mError;

    public HttpError getError(){
        return mError;
    }

    public void setError(HttpError eroor){
        mError = eroor;
    }

    public void setmResult(Object mResult) {
        this.mResult = mResult;
    }

    public Object getResult() {
        return mResult;
    }

    public void setmDataType(HttpTypeHelper.BodyDataType mDataType) {
        this.mDataType = mDataType;
    }

    public HttpTypeHelper.BodyDataType getmDataType() {
        return mDataType;
    }

    public class HttpError
    {
        private int mCode;
        private String mMsg;

        public String  getMsg() {
            return mMsg;
        }

        public void setMsg(String msg) {
            mMsg = msg;
        }

        public int getCode() {
            return mCode;
        }

        public void setCode(int code) {
            mCode = code;
        }

        public HttpError(int code, String message){
            super();
            mCode = code;
            this.mMsg = message;
        }
    }
}
