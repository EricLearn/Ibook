package com.example.eric.Http;

import org.json.JSONObject;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Eric on 2018/5/2.
 */

public interface ApiServer {

    @POST("{method}")
    Call<Translation> post(@Path("method") String method, @Body RequestBody  entery);

    @GET("{method}")
    Call<Translation> get(@Path("method") String method);
}
