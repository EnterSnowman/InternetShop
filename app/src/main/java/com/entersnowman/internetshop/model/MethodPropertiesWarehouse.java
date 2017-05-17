package com.entersnowman.internetshop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MethodPropertiesWarehouse extends AbstractMethod{

    @SerializedName("CityName")
    @Expose
    private String CityName	;


    public String getCityName	() {
        return CityName	;
    }
    public MethodPropertiesWarehouse(){

    }
    public void setCityName	(String cityName) {
        this.CityName	 = cityName;
    }

    public MethodPropertiesWarehouse(String FindByString) {
        this.CityName	 = FindByString;
    }



}