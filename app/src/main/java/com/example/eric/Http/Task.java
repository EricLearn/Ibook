package com.example.eric.Http;


import retrofit2.Call;

/**
 * Created by Eric
 */

public class Task  {

    private Call<Object> mCall;
    private Object mTag;

    public Task(Object tag){
        super();
        this.mTag = tag;
    }

    public Call<Object> getCall(){
        return mCall;
    }

    public void setCall(Call<Object> call) {
        this.mCall = call;
    }

    public Object getTag() {
        return mTag;
    }

    public void setTag(Object mTag) {
        this.mTag = mTag;
    }
}
