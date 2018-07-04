package com.example.eric.Http;


import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by Eric
 */

public class Task  {

    private Call<ResponseBody> mCall;
    private Object mTag;

    public Task(Object tag){
        super();
        this.mTag = tag;
    }

    public Call<ResponseBody> getCall(){
        return mCall;
    }

    public void setCall(Call<ResponseBody> call) {
        this.mCall = call;
    }

    public Object getTag() {
        return mTag;
    }

    public void setTag(Object mTag) {
        this.mTag = mTag;
    }
}
