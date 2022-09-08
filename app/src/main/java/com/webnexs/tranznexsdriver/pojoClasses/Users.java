package com.webnexs.tranznexsdriver.pojoClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Users extends BaseResponse {

    @SerializedName("driverDetails")
    @Expose
    private LoginUserDetails driver_Details;

    public LoginUserDetails getDriver_Details() {
        return driver_Details;
    }

    public void setDriver_Details(LoginUserDetails driver_Details) {
        this.driver_Details = driver_Details;
    }

    public class LoginUserDetails{

        @SerializedName("driver_id")
        @Expose
        private String driver_id;

        @SerializedName("firstname")
        @Expose
        private String firstname;

        @SerializedName("lastname")
        @Expose
        private String lastname;

        @SerializedName("address")
        @Expose
        private String address;

        @SerializedName("email_id")
        @Expose
        private String email_id;

        @SerializedName("phone_no")
        @Expose
        private String phone_no;

        @SerializedName("user_image")
        @Expose
        private String user_image;

        @SerializedName("number_plate")
        @Expose
        private String number_plate;

        @SerializedName("model")
        @Expose
        private String model;

        @SerializedName("cabtype")
        @Expose
        private String cabtype;

        @SerializedName("brand")
        @Expose
        private String brand;

        @SerializedName("color")
        @Expose
        private String color;

        public String getDriver_id() {
            return driver_id;
        }

        public void setDriver_id(String driver_id) {
            this.driver_id = driver_id;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getEmail_id() {
            return email_id;
        }

        public void setEmail_id(String email_id) {
            this.email_id = email_id;
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


        public String getNumber_plate() {
            return number_plate;
        }

        public void setNumber_plate(String number_plate) {
            this.number_plate = number_plate;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getCabtype() {
            return cabtype;
        }

        public void setCabtype(String cabtype) {
            this.cabtype = cabtype;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }

}