package com.example.eric.Http;

import android.support.annotation.NonNull;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Eric
 */

public class HttpClient
{
    private static String BASE_URL = "https://api.douban.com/";

    private static volatile HttpClient INSTANCE;

    private static OkHttpClient mOkHttpClient;
    private static Retrofit mRetrofitClient;
    private static ApiServer mApiServer;

    private HashMap<String,Call<ResponseBody>> mTaskHold = new HashMap<>();

    private Builder mBuilder;

    public Builder getBuilder(){
        return mBuilder;
    }

    private void setBuilder(Builder builder){
        this.mBuilder = builder;
    }

    private HttpClient(){
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1000L, TimeUnit.MILLISECONDS)
                //.addInterceptor(new HeaderInterceptor())
                .build();
    }

    public static HttpClient getInstance() {
        if (INSTANCE == null) {
            synchronized (HttpClient.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpClient();
                }
            }
        }
        return INSTANCE;
    }

    private RequestBody body(HashMap params){
        RequestBody body = new RequestBody() {
            @Override
            public MediaType contentType() {
                return null;
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {

            }
        };
        return body;
    }

    public void getRetrofit(){
        String baseUrl = mBuilder.getBaseUrl();
        if (baseUrl.length() == 0) {
            baseUrl = BASE_URL;
        }

        if (mRetrofitClient == null) {
            mRetrofitClient = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(mOkHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            mApiServer = mRetrofitClient.create(ApiServer.class);
        }
    }

    /**
     * 任务的复用  只有成功的任务才会从队列移除，所以如果报错的可以直接复用。
     * 同时同一个task多次调用之后，直接使用clone，被clone之后前一次调用会cancal，调用只会访问一次。
     * retrofit中call的clone
     * @param urlPath
     * @return
     */
    private Call<ResponseBody> checkTaskIsExist(String urlPath){
        synchronized (mTaskHold){
            for (String key: mTaskHold.keySet()){
                if (key.contains(urlPath)){
                    Call<ResponseBody> call = mTaskHold.get(key);
                    return call.clone();
                }
            }
        }
        return null;
    }

    /**
     * 保存Task方便维护
     * @param task
     * @param key  已View名+urlPath 为key
     */
    private void putTask(Call<ResponseBody> task,String key){
        if (key == null) return;
        synchronized (mTaskHold) {
            mTaskHold.put(key,task);
        }
    }

    public void cancelTask(String urlPath){
        synchronized (mTaskHold){
            for (String key: mTaskHold.keySet()){
                if (key.contains(urlPath)){
                    Call<ResponseBody> call = mTaskHold.get(key);
                    call.cancel();
                    urlPath = key;
                    break;
                }
            }
            mTaskHold.remove(urlPath);
        }
    }

    public void cancelAllTask(Object tag){
        List<String> list = new ArrayList<>();
        synchronized (mTaskHold) {
            for (String key: mTaskHold.keySet()){
                if (key.contains(tag.toString())){
                    list.add(key);
                }
            }
            for (String sub: list) {
                cancelTask(sub);
            }
        }
    }

    // 断网 恢复 后    retrofit 不会主动重新完成刚刚的Task

    public void start(final TaskCallback callback,final String urlPath){
        Call<ResponseBody> task = checkTaskIsExist(urlPath);
        if (task == null) {
            if (mBuilder.mHttpMethod == HttpTypeHelper.HttpMethod.HttpMethod_Get) {
                task = mApiServer.get(urlPath);
            }
            else {
                task = mApiServer.post(urlPath,body(null));
            }
            putTask(task,callback.toString() + urlPath);
        }

        load(callback,task,urlPath);
    }

    /**
     * 真正开始执行数据访问
     *
     * @param callback
     * @param task
     * @param urlPath
     */
    private void load(final TaskCallback callback, Call<ResponseBody> task, final String urlPath){

        if (callback != null) callback.taskStart(urlPath);

        if (task != null) {
            // 同时调用1000次，      callback为不同的对象，不知道会不会出错
            task.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    // 正常连接到服务端 响应了

                    ResultModel model = new ResultModel();

                    if (response.code() == 200) {
                        //访问正确
                        try {
                            String result = response.body().string();
                            Object object = parseData(result,mBuilder.getParseClass(),mBuilder.mBodyType);
                            model.setmResult(object);
                        }
                        catch (IOException | IllegalStateException e) {
                            e.printStackTrace();
                        }
                    }
                    else  {
                        //访问报错   包括自定义的 errorcode
                        ResultModel.HttpError error = model.new HttpError(response.code(),response.message());
                        model.setError(error);
                    }

                    if (response.isSuccessful()){
                        Log.d("httpclient","访问成功");
                    }
                    else {
                        Log.d("httpclient","访问失败");
                    }
                    cancelTask(urlPath);
                    if (callback != null)  callback.taskFinish(urlPath, model);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // 未正常连接到服务端
                    if (call.isCanceled()) {
                        // 主动取消
                    }
                    else {
                        // 真的报错
                    }
                }
            });
        }
    }

    /**
     * 解析返回的数据：解析类独立 方便随时替换解析库
     *  @param result
     */
    private Object parseData(String result, Class parseClass, HttpTypeHelper.BodyDataType bodyType) {
        Object object = null;
        switch (bodyType) {
            case STRING:
                object = result;
                break;
            case JSON_OBJECT:
                object = DataParseUtil.parseObject(result, parseClass);
                break;
            case JSON_ARRAY:
                object = DataParseUtil.parseToArrayList(result, parseClass);
                break;
            case XML:
                //onResultListener.onSuccess(DataParseUtil.parseXml(data, clazz));
                break;
            default:
                break;
        }
        return object;
    }

    public static final class Builder
    {
        private int mHttpMethod = HttpTypeHelper.HttpMethod.HttpMethod_Get;
        private String mBaseUrl = "";
        private String mUrlPath;
        private Class  mParseClass;
        private HttpTypeHelper.BodyDataType mBodyType = HttpTypeHelper.BodyDataType.JSON_OBJECT;

        private HashMap<String,String> mParams = new HashMap<>();

        public Builder(){
        }

        public String getUrlPath(){
            return mUrlPath;
        }

        public Builder urlPath(String path) {
            this.mUrlPath = path;
            return this;
        }

        public Builder setParams(HashMap<String,String>params) {
            this.mParams = params;
            return this;
        }

        public Builder setParseClass(Class parseClass){
            this.mParseClass = parseClass;
            return this;
        }

        public Builder setBodyDataType(HttpTypeHelper.BodyDataType type) {
            this.mBodyType = type;
            return this;
        }

        public Builder httpMethod(int method) {
            this.mHttpMethod = method;
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            this.mBaseUrl = baseUrl;
            return this;
        }

        public Class getParseClass() {
            return mParseClass;
        }

        public String getBaseUrl(){
            return mBaseUrl;
        }

        public HttpClient build(){
            HttpClient client = HttpClient.getInstance();
            client.setBuilder(this);
            client.getRetrofit();
            return  client;
        }
    }

    public class HeaderInterceptor implements Interceptor
    {
        @Override
        public Response intercept(Chain chain) throws IOException {
            return null;
        }
    }
}
