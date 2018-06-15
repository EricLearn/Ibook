package com.example.eric.Model;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.logging.LogRecord;

/**
 * Created by Eric on 2018/6/15.
 */

public class HandlerCenter {
    
    private static HandlerCenter instance = new HandlerCenter();
  
    
    private Callback mCallback;
    
    private Handler mHandler;
    
    public interface Callback {
        public void handlerMessage(Message msg);
    }
    
    public static HandlerCenter center() {
        return instance;
    }
    
    public Handler getHandler() {
        return mHandler;
    }
    
    public void setCallback(Callback callback){
        mCallback = callback;
    }
    
    private HandlerCenter() {
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                handlerMessage_(msg);
            }
        };
    }
    
    private void handlerMessage_(Message msg) {
        if (mCallback != null) {
            mCallback.handlerMessage(msg);
        }
    }
}
