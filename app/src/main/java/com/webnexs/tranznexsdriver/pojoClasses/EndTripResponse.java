package com.webnexs.tranznexsdriver.pojoClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EndTripResponse extends BaseResponse {

    @SerializedName("closeData")
    @Expose
    private EndTrip closeData;

    public EndTrip getCloseData() {
        return closeData;
    }

    public void setCloseData(EndTrip closeData) {
        this.closeData = closeData;
    }

    public class EndTrip{

        @SerializedName("trip_amount")
        @Expose
        private String trip_amount;

        @SerializedName("trip_hour")
        @Expose
        private String trip_hour;

        @SerializedName("TotalEarning")
        @Expose
        private String TotalEarning;

        public String getTrip_amount() {
            return trip_amount;
        }

        public void setTrip_amount(String trip_amount) {
            this.trip_amount = trip_amount;
        }

        public String getTrip_hour() {
            return trip_hour;
        }

        public void setTrip_hour(String trip_hour) {
            this.trip_hour = trip_hour;
        }

        public String getTotalEarning() {
            return TotalEarning;
        }

        public void setTotalEarning(String totalEarning) {
            TotalEarning = totalEarning;
        }
    }
}
