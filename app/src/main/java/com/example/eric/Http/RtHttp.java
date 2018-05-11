package com.example.eric.Http;

import android.content.Context;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Eric on 2018/4/27.
 */

public class RtHttp {
    private static RtHttp instance = new RtHttp();
    private static WeakReference<Context> wrContext;

    private static OkHttpClient.Builder mOkClientBuilder;
    private static Retrofit.Builder mRtBuilder;
    private static ApiServer mApiServer;

    private RtHttp() {
        mOkClientBuilder = new OkHttpClient.Builder();
        mRtBuilder = new Retrofit.Builder();
        mRtBuilder.baseUrl("https://hsej.app360.cn/app/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(mOkClientBuilder.build());
        mApiServer = mRtBuilder.build().create(ApiServer.class);
    }

    public static RtHttp with(Context ct) {
        wrContext = new WeakReference<Context>(ct);
        return instance;
    }

    public RtHttp startTask(ApiType type, Map map) {
        String method = ApiHelper.getMethod(type);
        boolean isPost = true;

        Call call = null;
        // body参数
        if (isPost) {
            call = mApiServer.post(method,null);
        }
        else {
            call = mApiServer.get(method);
        }
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                System.out.print("");
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
        return instance;
    }

    public static class ApiServerBuilder {
        private String baseUrl;

        private Retrofit.Builder rtBuilder;
        private OkHttpClient.Builder okBuilder;
        private Converter.Factory converterFactory;

        public ApiServerBuilder setConverterFactory(Converter.Factory converterFactory) {
            this.converterFactory = converterFactory;
            return this;
        }

        public ApiServerBuilder setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public ApiServerBuilder addDynamicParameterMap(HashMap parameter) {
            return this;
        }

        public ApiServer build() {
            rtBuilder = new Retrofit.Builder();
            okBuilder = new OkHttpClient().newBuilder();

            if (baseUrl!=null) {
                rtBuilder.baseUrl(baseUrl);
            }

            if (converterFactory != null) {
                rtBuilder.addConverterFactory(converterFactory);
            }
            else {
                rtBuilder.addConverterFactory(GsonConverterFactory.create());
            }
            rtBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());

            // 拦截器  header
            //okBuilder.addInterceptor(new ParameterInterceptor());

            rtBuilder.client(okBuilder.build());

            return rtBuilder.build().create(ApiServer.class);
        }
    }
}
