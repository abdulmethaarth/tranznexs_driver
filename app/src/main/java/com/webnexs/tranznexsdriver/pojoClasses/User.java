package com.webnexs.tranznexsdriver.pojoClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User extends BaseResponse {

    @SerializedName("userDetails")
    @Expose
    private UserDetails userDetails;

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public class UserDetails{

        @SerializedName("ride_id")
        @Expose
        private String ride_id;

        @SerializedName("rider_id")
        @Expose
        private String rider_id;

        @SerializedName("ride_type")
        @Expose
        private String ride_type;

        @SerializedName("cabtype")
        @Expose
        private String cabtype;

        @SerializedName("payment_method")
        @Expose
        private String payment_method;

        @SerializedName("ride_otp")
        @Expose
        private String ride_otp;

        @SerializedName("pickup_lat")
        @Expose
        private String pickup_lat;

        @SerializedName("pickup_lng")
        @Expose
        private String pickup_lng;

        @SerializedName("pickup_loc")
        @Expose
        private String pickup_loc;

        @SerializedName("drop_lat")
        @Expose
        private String drop_lat;

        @SerializedName("drop_lng")
        @Expose
        private String drop_lng;

        @SerializedName("drop_loc")
        @Expose
        private String drop_loc;

        @SerializedName("fare")
        @Expose
        private String fare;

        @SerializedName("distance")
        @Expose
        private String distance;

        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("phone_no")
        @Expose
        private String phone_no;

        @SerializedName("user_image")
        @Expose
        private String user_image;

        public String getRide_id() {
            return ride_id;
        }

        public void setRide_id(String ride_id) {
            this.ride_id = ride_id;
        }

        public String getRider_id() {
            return rider_id;
        }

        public void setRider_id(String rider_id) {
            this.rider_id = rider_id;
        }

        public String getRide_type() {
            return ride_type;
        }

        public void setRide_type(String ride_type) {
            this.ride_type = ride_type;
        }

        public String getCabtype() {
            return cabtype;
        }

        public void setCabtype(String cabtype) {
            this.cabtype = cabtype;
        }

        public String getPayment_method() {
            return payment_method;
        }

        public void setPayment_method(String payment_method) {
            this.payment_method = payment_method;
        }

        public String getRide_otp() {
            return ride_otp;
        }

        public void setRide_otp(String ride_otp) {
            this.ride_otp = ride_otp;
        }

        public String getPickup_lat() {
            return pickup_lat;
        }

        public void setPickup_lat(String pickup_lat) {
            this.pickup_lat = pickup_lat;
        }

        public String getPickup_lng() {
            return pickup_lng;
        }

        public void setPickup_lng(String pickup_lng) {
            this.pickup_lng = pickup_lng;
        }

        public String getDrop_lat() {
            return drop_lat;
        }

        public void setDrop_lat(String drop_lat) {
            this.drop_lat = drop_lat;
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

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone_no() {
            return phone_no;
        }

        public void setPhone_no(String phone_no) {
            this.phone_no = phone_no;
        }

        public String getUser_image() {
            return user_image;
        }

        public void setUser_image(String user_image) {
            this.user_image = user_image;
        }

        public String getDrop_lng() {
            return drop_lng;
        }

        public void setDrop_lng(String drop_lng) {
            this.drop_lng = drop_lng;
        }
    }

}
