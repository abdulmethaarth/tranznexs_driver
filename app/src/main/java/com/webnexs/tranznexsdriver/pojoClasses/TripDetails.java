package com.webnexs.tranznexsdriver.pojoClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TripDetails {

    @SerializedName("rider_id")
    @Expose
    private String rider_id;

   @SerializedName("ride_id")
    @Expose
    private String ride_id;

    @SerializedName("pickup_loc")
    @Expose
    private String pickup_loc;

    @SerializedName("drop_loc")
    @Expose
    private String drop_loc;

    @SerializedName("fare")
    @Expose
    private String fare;

    @SerializedName("request_time")
    @Expose
    private String request_time;

    @SerializedName("ridername")
    @Expose
    private String ridername;

    @SerializedName("status_stamp")
    @Expose
    private String status_stamp;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("rider_image")
    @Expose
    private String rider_image;

    public String getRider_id() {
        return rider_id;
    }

    public void setRider_id(String rider_id) {
        this.rider_id = rider_id;
    }

    public String getPickup_loc() {
        return pickup_loc;
    }

    public void setPickup_loc(String pickup_loc) {
        this.pickup_loc = pickup_loc;
    }

    public String getDrop_loc() {
        return drop_loc;
    }

    public void setDrop_loc(String drop_loc) {
        this.drop_loc = drop_loc;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    public String getRequest_time() {
        return request_time;
    }

    public void setRequest_time(String request_time) {
        this.request_time = request_time;
    }

    public String getRidername() {
        return ridername;
    }

    public void setRidername(String ridername) {
        this.ridername = ridername;
    }

    public String getStatus_stamp() {
        return status_stamp;
    }

    public void setStatus_stamp(String status_stamp) {
        this.status_stamp = status_stamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRider_image() {
        return rider_image;
    }

    public void setRider_image(String rider_image) {
        this.rider_image = rider_image;
    }

    public String getRide_id() {
        return ride_id;
    }

    public void setRide_id(String ride_id) {
        this.ride_id = ride_id;
    }
}
