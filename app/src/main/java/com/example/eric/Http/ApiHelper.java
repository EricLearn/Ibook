package com.example.eric.Http;


import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Eric on 2018/5/3.
 */

public abstract class ApiHelper {

    public static  ApiServer mApiServer;
    public static  Observable mObservable;

    public static ApiServer getApiServer() {
        if (mApiServer == null) {
            mApiServer = new RtHttp.ApiServerBuilder()
                    .setBaseUrl("http://fy.iciba.com/")
                    .setConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mApiServer;
    }

    public static Observable getObservable(Observable observable) {
        mObservable = new ObserableBuilder(observable).build();
        return mObservable;
    }

    /**
     * @param map
     * @return
     */
    public static RequestBody toBody(HashMap map) {
        Gson gson = new Gson();
        Object obj = new Object();
        return RequestBody.create(mediatype(), gson.toJson(obj));
    }

    public static RequestBody toBody(JSONObject jsonObject) {
        return RequestBody.create(mediatype(), (jsonObject).toString());
    }

    public static String getMethod(ApiType type ){
        String method = "";
        switch (type){
            case AppConfig:
                method = "settings/get";
                break;
            default:
                break;
        }
        return method;
    }

    private static MediaType mediatype() {
        return MediaType.parse("application/json; charset=utf-8");
    }

    /**
     * 定制Obserable
     * 区分组装配件不同的类型
     * 如轮子有雪地的、跑车的。
     */
    public static class ObserableBuilder {

        private Observable observable;

        public ObserableBuilder(Observable o) {
            this.observable = o;
        }

        private Observable build() {
            observable = observable.subscribeOn(Schedulers.io());// 切换到IO线程进行网络请求
            observable = observable.observeOn(AndroidSchedulers.mainThread()); // 切换回到主线程 处理请求结果
            return observable;
        }
    }
}
