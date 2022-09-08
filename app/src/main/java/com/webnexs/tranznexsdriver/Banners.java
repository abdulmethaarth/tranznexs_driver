package com.webnexs.tranznexsdriver;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Banners {

    @SerializedName("screenList")
    private ArrayList<Banner> banners;

    public Banners(){ }

    public ArrayList<Banner> getBanners(){
        return banners;
    }
}
