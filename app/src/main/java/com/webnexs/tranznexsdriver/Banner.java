package com.webnexs.tranznexsdriver;

import com.google.gson.annotations.SerializedName;

public class Banner {

    @SerializedName("image")
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
