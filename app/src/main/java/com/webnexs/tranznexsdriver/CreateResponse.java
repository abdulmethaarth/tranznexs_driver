package com.webnexs.tranznexsdriver;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webnexs.tranznexsdriver.pojoClasses.BaseResponse;

public class CreateResponse extends BaseResponse {

    @SerializedName("driverDetails")
    @Expose CreatingResponse creatingResponse;

    public CreatingResponse getCreatingResponse() {
        return creatingResponse;
    }

    public void setCreatingResponse(CreatingResponse creatingResponse) {
        this.creatingResponse = creatingResponse;
    }

    public  class CreatingResponse{

        @SerializedName("phone_no")
        @Expose
        private String phone_no;

        @SerializedName("driver_id")
        @Expose
        private String driver_id;


        public String getPhone_no() {
            return phone_no;
        }

        public void setPhone_no(String phone_no) {
            this.phone_no = phone_no;
        }

        public String getDriver_id() {
            return driver_id;
        }

        public void setDriver_id(String driver_id) {
            this.driver_id = driver_id;
        }
    }
}
