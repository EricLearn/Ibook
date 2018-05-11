package com.example.eric.Http;

import org.json.JSONArray;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Eric on 2018/4/28.
 */

public interface NetworkApi {

    @POST("/repos/{owner}/{repo}/contributors")
    Call<List<JSONArray>>repoContributors(
            @Path("owner") String owner,
            @Path("repo") String repo);
}
