package com.webnexs.tranznexsdriver;

import com.webnexs.tranznexsdriver.pojoClasses.BaseResponse;
import com.webnexs.tranznexsdriver.pojoClasses.EndTripResponse;
import com.webnexs.tranznexsdriver.pojoClasses.OvertripDetailsList;
import com.webnexs.tranznexsdriver.pojoClasses.SingleTripDetails;
import com.webnexs.tranznexsdriver.pojoClasses.User;
import com.webnexs.tranznexsdriver.pojoClasses.Users;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface Api {

    @GET("login_screens")
    Call<Banners> getBanners();

    @GET("cab_types")
    Call<CabTypes_list> getCabDetails();

    @FormUrlEncoded
    @POST("otp_login")
    Call<Users> getDriverdetails(@Field("phone_no") String phone_no);

    @POST("email_login")
    @FormUrlEncoded
    Call<Users> login(@Field("email_id") String email_id,
                      @Field("password") String password);



    @FormUrlEncoded
    @POST("register_step1")
    Call<CreateResponse> createprofile(@Field("firstname") String firstname,
                                   @Field("lastname") String lastname,
                                   @Field("email_id") String email_id,
                                   @Field("password") String password,
                                   @Field("phone_no") String phone_no,
                                   @Field("address") String address,
                                   @Field("dob") String dob,
                                   @Field("gender") String gender


    );

    @FormUrlEncoded
    @POST("register_step3")
    Call<CreateResponse> uploadvehicle(@Field("driver_id") String driver_id,
                                   @Field("phone_no") String phone_no,
                                   @Field("cabtype") String cabtype,
                                   @Field("brand") String brand,
                                   @Field("model") String model,
                                   @Field("number_plate") String number_plate,
                                   @Field("color") String color


    );


    @FormUrlEncoded
    @POST("ride_request")
    Call<User> getUsers(@Field("driver_id") String driver_id,
                        @Field("cabtype") String cabtype,
                        @Field("latitude") String latitude,
                        @Field("longitude") String longitude
    );


    @FormUrlEncoded
    @POST("accepted_ridedetails")
    Call<User> getAcceptedDtls(@Field("driver_id") String driver_id,
                        @Field("ride_id") String ride_id,
                        @Field("rider_id") String rider_id
    );

    @FormUrlEncoded
    @POST("auto_cancel")
    Call<BaseResponse> auto_cancel(@Field("rider_id") String rider_id,
                                   @Field("driver_id") String driver_id,
                                   @Field("ride_id") String ride_id,
                                   @Field("created") String created
    );


    @FormUrlEncoded
    @POST("accept_ride")
    Call<BaseResponse> iHaveArriveToSendDetails( @Field("driver_id") String driver_id,
                                                 @Field("rider_id") String rider_id,
                                                 @Field("ride_id") String ride_id,
                                                 @Field("driver_lat") String driver_lat,
                                                 @Field("driver_lng") String driver_lng,
                                                 @Field("accept_date") String accept_date);

    @FormUrlEncoded
    @POST("cancel_ride")
    Call<BaseResponse> cancelRide(@Field("driver_id") String driver_id,
                                  @Field("ride_id") String ride_id,
                                  @Field("rider_id") String rider_id,
                                  @Field("created") String created);

    @FormUrlEncoded
    @POST("cancel_acceptride")
    Call<BaseResponse> cancelAcceptRide(@Field("driver_id") String driver_id,
                                  @Field("ride_id") String ride_id,
                                  @Field("rider_id") String rider_id,
                                  @Field("reason") String reason,
                                  @Field("current_time") String current_time);

    @FormUrlEncoded
    @POST("ride_cancelled")
    Call<BaseResponse> userCancelledRide(@Field("ride_id") String ride_id);

    @FormUrlEncoded
    @POST("start_trip")
    Call<BaseResponse> startRide(@Field("user_id") String user_id,
                                 @Field("ride_id") String ride_id,
                                 @Field("start_date") String start_date,
                                 @Field("start_lat") String start_lat,
                                 @Field("start_lng") String start_lng,
                                 @Field("ride_otp") String ride_otp);


    @FormUrlEncoded
    @POST("go_offline")
    Call<BaseResponse> goOffline( @Field("driver_id") String driver_id,
                                  @Field("logout_time") String logout_time
                                  );

    @FormUrlEncoded
    @POST("go_online")
    Call<BaseResponse> goOnline( @Field("driver_id") String driver_id,
                                 @Field("cabtype") String cabtype,
                                 @Field("latitude") String latitude,
                                 @Field("longitude") String longitude,
                                 @Field("login_time") String login_time
                                 );

    @FormUrlEncoded
    @POST("driver_arrived")
    Call<BaseResponse> arrived( @Field("driver_id") String driver_id,
                                @Field("arrived_time") String arrived_time,
                                @Field("ride_id") String ride_id
    );

    @FormUrlEncoded
    @POST("driver_tracking")
    Call<BaseResponse> bikeTracking( @Field("driver_id") String driver_id,
                              @Field("latitude") String latitude,
                              @Field("longitude") String longitude,
                              @Field("ride_id") String ride_id
    );


   @FormUrlEncoded
   @POST("end_trip")
   Call<EndTripResponse> endTrip(@Field("ride_type") String ride_type,
                                  @Field("driver_id") String driver_id,
                                  @Field("ride_id") String ride_id,
                                  @Field("end_lat") String end_lat,
                                  @Field("end_lng") String end_lng,
                                  @Field("end_date") String end_date);

   @FormUrlEncoded
   @POST("change_payment")
   Call<BaseResponse> changePayment(@Field("ride_id") String ride_id,
                                  @Field("new_method") String new_method);

   @FormUrlEncoded
   @POST("my_trips")
   Call<OvertripDetailsList> getTripDetails(@Field("driver_id") String driver_id);

    @FormUrlEncoded
    @POST("single_trip")
    Call<SingleTripDetails> getSingleTripDetail(@Field("driver_id") String driver_id,
                                                @Field("ride_id") String ride_id
    );
}
