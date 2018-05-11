package com.example.eric.Http;

import android.content.Context;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Eric on 2018/5/2.
 */

public class Dataload {

    private static Dataload instance = new Dataload();
    private static WeakReference wrContent;

    private static ApiServer mApiServer;

    public static  Dataload with(Context c) {
        wrContent = new WeakReference(c);
        return instance;
    }

    public Dataload(){
        initRetrofit();
    }

    public void startTaskWithTypeAndParams(ApiType type, HashMap<String ,String> params) {
        String method = getRequestMethod(type);
    }

    private void initRetrofit() {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        // 定制okhttp

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("https://hsej.app360.cn/app/")
                .build();

        mApiServer = retrofit.create(ApiServer.class);
    }

    private String getRequestMethod(ApiType type) {
        String method = "";
        switch (type) {
            case AppConfig:
                method = "settingsget";
                break;
            default:
                break;
        }
        return method;
    }
}
