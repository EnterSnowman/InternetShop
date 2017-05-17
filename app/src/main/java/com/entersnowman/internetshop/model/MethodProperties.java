package com.entersnowman.internetshop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MethodProperties extends AbstractMethod{

    @SerializedName("FindByString")
    @Expose
    private String FindByString;


    public String getFindByString() {
        return FindByString;
    }
    public MethodProperties(){

    }
    public void setFindByString(String cityName) {
        this.FindByString = cityName;
    }

    public MethodProperties(String FindByString) {
        this.FindByString = FindByString;
    }



}