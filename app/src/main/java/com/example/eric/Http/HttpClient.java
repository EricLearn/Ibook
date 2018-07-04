package com.example.eric.Http;

import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

    private static OkHttpClient mOkHttpClient;
    private static Retrofit mRetrofitClient;
    private static ApiServer mApiServer;

    private ArrayList<Task> mTaskQueue = new ArrayList<>();

    private Builder mBuilder;

    public Builder getBuilder(){
        return mBuilder;
    }

    private static HttpClient getIns(){
        return HttpClientHolder.sInstance;
    }

    private static class HttpClientHolder{
        private  static final HttpClient sInstance = new HttpClient();
    }

    private void setBuilder(Builder builder){
        this.mBuilder = builder;
    }

    private HttpClient(){
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1000L, TimeUnit.MILLISECONDS)
                .addInterceptor(new HeaderInterceptor())
                .build();
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

    public void cancel(Object tag){
        for (int i = 0;i < mTaskQueue.size(); i++) {
            Task sub = mTaskQueue.get(i);
            if (sub.getTag().equals(tag)) {
                if (sub.getCall().isCanceled() == false)  {
                    sub.getCall().cancel();
                }
            }
        }
    }

    public void start(final TaskCallback callback,String taskType){

        Task task = checkTaskIsExist(taskType);

        if (task == null) {
            task = new Task(taskType);
            if (mBuilder.mHttpMethod == HttpTypeHelper.HttpMethod.HttpMethod_Get) {
                task.setCall(mApiServer.get(taskType));
            }
            else {
                task.setCall(mApiServer.post(taskType,body(null)));
            }
            mTaskQueue.add(task);
        }

        load(callback,task,taskType);
    }

    /**
     * 真正开始执行数据访问
     *
     * @param callback
     * @param task
     * @param taskType
     */
    private void load(final TaskCallback callback,Task task,String taskType){

        //断网 恢复 后    retrofit 不会主动重新完成刚刚的Task
        if (callback != null) callback.taskStart(taskType);

        if (task.getCall() != null) {
            // 同时调用1000次，      callback为不同的对象，不知道会不会出错
            task.getCall().enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                    // 正常连接到服务端 响应了

                    ResultModel model = new ResultModel();

                    if (response.isSuccessful()){
                        Log.d("httpclient","访问成功");
                        if (response.code() == 200) {
                            //访问正确
                            String result = response.body().toString();
                            Object object = parseData(result,mBuilder.getParseClass(),mBuilder.mBodyType);
                            model.setmResult(object);
                        }
                        else  {
                            //访问报错   包括自定义的 errorcode
                        }
                    }
                    else {
                        Log.d("httpclient","访问失败");

                        ResponseBody errorBody = response.errorBody();
                        int code = response.code();
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
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
     * 解析返回的数据
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
                //Logger.e("http parse tip:", "if you want return object, please use bodyType() set data type");
                break;
        }
        return object;
    }

    /**
     * 任务的复用  只有成功的任务才会从队列移除，所以如果报错的可以直接复用。
     * retrofit中call的clone
     * @param tag
     * @return
     */
    private Task checkTaskIsExist(Object tag){
        for (int i = 0;i < mTaskQueue.size(); i++) {
            Task  sub = mTaskQueue.get(i);
            if (sub.getTag().equals(tag)) {
                Call<Object> temp = sub.getCall().clone();
                sub.setCall(temp);
                return sub;
            }
        }
        return null;
    }

    public static final class Builder
    {
        private int mHttpMethod = HttpTypeHelper.HttpMethod.HttpMethod_Get;
        private String mBaseUrl = "";
        private String mUrlPath;
        private String mTaskTag;
        private Class  mParseClass;
        private HttpTypeHelper.BodyDataType mBodyType = HttpTypeHelper.BodyDataType.UNKNOWN;

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

        public Builder setTaskTag(String taskTag) {
            this.mTaskTag = taskTag;
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

        public String getTaskTag(){
            return mTaskTag;
        }

        public HttpClient build(){
            HttpClient client = HttpClient.getIns();
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
