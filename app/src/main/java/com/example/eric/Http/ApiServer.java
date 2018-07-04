package com.example.eric.Http;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Eric
 */

public interface ApiServer {

    @POST("{path}")
    Call<Object> post(@Path("path") String path, @Body RequestBody  entery);

    @GET("{path}")
    Call<Object> get(@Path("path") String path);
    
    @GET("ajax.php?a=fy&f=auto&t=auto&w=hi%20world")
    Observable<ResultModel> getcall();
}
