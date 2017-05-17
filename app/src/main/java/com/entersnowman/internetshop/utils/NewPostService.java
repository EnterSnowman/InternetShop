package com.entersnowman.internetshop.utils;

import com.entersnowman.internetshop.adapter.BestProductAdapter;
import com.entersnowman.internetshop.model.CityRequestBody;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Valentin on 17.05.2017.
 */

public interface NewPostService {
    @POST("json/")
    Call<ResponseBody> getCities(@Body CityRequestBody cityRequestBody);
    @POST("json/")
    Call<ResponseBody> getWarehouses(@Body CityRequestBody warehousesRequestBody);
}
