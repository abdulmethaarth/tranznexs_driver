package com.webnexs.tranznexsdriver.pojoClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SingleTripDetails extends BaseResponse {

    @SerializedName("details")
    @Expose
    SingleTrip details;

    public SingleTrip getDetails() {
        return details;
    }

    public void setDetails(SingleTrip details) {
        this.details = details;
    }

    public class SingleTrip {

        @SerializedName("accept_date")
        @Expose
        private String accept_date;

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

        @SerializedName("distance")
        @Expose
        private String distance;

        @SerializedName("rating")
        @Expose
        private String rating;


        @SerializedName("rider_image")
        @Expose
        private String rider_image;

        @SerializedName("status_stamp")
        @Expose
        private String status_stamp;

        @SerializedName("ridername")
        @Expose
        private String ridername;

        @SerializedName("Payment")
        @Expose
        private String Payment;

        @SerializedName("fare")
        @Expose
        private String fare;

        public String getAccept_date() {
            return accept_date;
        }

        public void setAccept_date(String accept_date) {
            this.accept_date = accept_date;
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

        public String getPickup_loc() {
            return pickup_loc;
        }

        public void setPickup_loc(String pickup_loc) {
            this.pickup_loc = pickup_loc;
        }

        public String getDrop_lat() {
            return drop_lat;
        }

        public void setDrop_lat(String drop_lat) {
            this.drop_lat = drop_lat;
        }

        public String getDrop_lng() {
            return drop_lng;
        }

        public void setDrop_lng(String drop_lng) {
            this.drop_lng = drop_lng;
        }

        public String getDrop_loc() {
            return drop_loc;
        }

        public void setDrop_loc(String drop_loc) {
            this.drop_loc = drop_loc;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String getRider_image() {
            return rider_image;
        }

        public void setRider_image(String rider_image) {
            this.rider_image = rider_image;
        }

        public String getStatus_stamp() {
            return status_stamp;
        }

        public void setStatus_stamp(String status_stamp) {
            this.status_stamp = status_stamp;
        }

        public String getRidername() {
            return ridername;
        }

        public void setRidername(String ridername) {
            this.ridername = ridername;
        }

        public String getPayment() {
            return Payment;
        }

        public void setPayment(String payment) {
            Payment = payment;
        }

        public String getFare() {
            return fare;
        }

        public void setFare(String fare) {
            this.fare = fare;
        }
    }
}
