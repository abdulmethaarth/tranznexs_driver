package com.webnexs.tranznexsdriver.pojoClasses;

public enum MethodFactory {

    REGISTER("driver_registration");

   private String method;
    MethodFactory(String method) {
        this.method = method;
    }

    public String getMethod(){
        return this.method;
    }
}
