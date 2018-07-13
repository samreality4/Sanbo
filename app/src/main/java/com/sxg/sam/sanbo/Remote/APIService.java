package com.sxg.sam.sanbo.Remote;

import com.sxg.sam.sanbo.Model.ResultData;
import com.sxg.sam.sanbo.Model.YelpData;

import java.util.Map;
import java.util.Observable;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface APIService {
        String name = "japanese";

        /*@GET("businesses/{id}")
        Call<YelpData> getBusiness(@QueryMap Map<String, String> params);*/

        @GET("businesses/search?term=japanese")
        Call<ResultData> getYelpData(@Path("id") String ID, @Query("api_key") String API_KEY);


    }

