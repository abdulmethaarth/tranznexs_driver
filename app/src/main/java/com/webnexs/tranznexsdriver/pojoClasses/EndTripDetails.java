package com.webnexs.tranznexsdriver.pojoClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EndTripDetails extends EndTripResponse {

    @SerializedName("details")
    @Expose
    private EndTrip details;

    public EndTrip getDetails() {
        return details;
    }

    public void setDetails(EndTrip details) {
        this.details = details;
    }

    public class EndTrip{

        @SerializedName("Amount")
        @Expose
        private String Amount;

        @SerializedName("hour")
        @Expose
        private String hour;

        public String getAmount() {
            return Amount;
        }

        public void setAmount(String amount) {
            Amount = amount;
        }

        public String getHour() {
            return hour;
        }

        public void setHour(String hour) {
            this.hour = hour;
        }
    }
}
