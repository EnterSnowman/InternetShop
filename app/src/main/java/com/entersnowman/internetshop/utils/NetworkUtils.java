package com.entersnowman.internetshop.utils;

import android.content.Context;
import android.database.MatrixCursor;
import android.provider.BaseColumns;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.entersnowman.internetshop.MakeOrderActivity;
import com.entersnowman.internetshop.R;
import com.entersnowman.internetshop.model.CityRequestBody;
import com.entersnowman.internetshop.model.MethodProperties;
import com.entersnowman.internetshop.model.MethodPropertiesWarehouse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Valentin on 17.05.2017.
 */

public class NetworkUtils {
    final static String API_KEY = "1d6e9662c202d09918194825e92fc77f";
    NewPostService newPostService;
    public NetworkUtils(){
        Retrofit retrofit = new Retrofit.Builder()
        //.addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.novaposhta.ua/v2.0/")
                .build();
        newPostService = retrofit.create(NewPostService.class);
    }

    public ArrayList<String> getCities(String query, final SimpleCursorAdapter simpleCursorAdapter){
        CityRequestBody cityRequestBody = new CityRequestBody(API_KEY,"Address","getCities",new MethodProperties(query));
        Call<ResponseBody> call  = newPostService.getCities(cityRequestBody);
        final ArrayList<String> r = new ArrayList<>();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String result = response.body().string();
                        System.out.println(result);
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.has("data")){
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            final MatrixCursor c = new MatrixCursor(new String[]{ BaseColumns._ID, "cityName" });
                            for (int i = 0; i < jsonArray.length(); i++) {
                                r.add(((JSONObject) jsonArray.get(i)).getString("Description"));
                                System.out.println(r.get(i));
                                c.addRow(new Object[] {i, r.get(i)});
                            }
                            simpleCursorAdapter.changeCursor(c);

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });


        return  r;
    }

    public ArrayList<String> getWarehouses (String city, final ArrayAdapter spinnerAdapter){
        CityRequestBody cityRequestBody = new CityRequestBody(API_KEY,"AddressGeneral","getWarehouses",new MethodPropertiesWarehouse(city));
        Call<ResponseBody> call  = newPostService.getCities(cityRequestBody);
        final ArrayList<String> r = new ArrayList<>();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    System.out.println(result);
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.has("data")){
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            r.add(((JSONObject) jsonArray.get(i)).getString("Description"));
                            System.out.println(r.get(i));
                        }
                        spinnerAdapter.addAll(r);
                        spinnerAdapter.notifyDataSetChanged();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
        return r;
    }
}
