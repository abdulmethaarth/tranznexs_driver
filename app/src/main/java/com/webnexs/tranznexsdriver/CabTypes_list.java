package com.webnexs.tranznexsdriver;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CabTypes_list {

    @SerializedName("CabList")
    @Expose
    private ArrayList<CabType> cabTypes;

    public ArrayList<CabType> getCabTypes() {
        return cabTypes;
    }

    public void setCabTypes(ArrayList<CabType> cabTypes) {
        this.cabTypes = cabTypes;
    }
}
