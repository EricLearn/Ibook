package com.example.eric.base;

import java.io.Serializable;

/**
 * Created by Eric
 */

public class BaseModel implements Serializable {

    private int mCode;
    private String mMsg;
    private Errors mError;

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

    public Errors getError(){
        return mError;
    }

    public void setError(Errors eroor){
        mError = eroor;
    }

    public static <T extends Comparable<T>> T maxmum(T x,T y,T z){
        T max = x;
        if (y.compareTo(max) > 0) {
            max = y;
        }
        if (z.compareTo(max) > 0) {
            max = z;
        }
        return max;
    }
}
