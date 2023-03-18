package com_we.java_we.weapplication.rest;

import java.util.List;

import com_we.java_we.weapplication.models.Donation;
import com_we.java_we.weapplication.models.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("login.php")
    Call<ResponseBody> loginUser(@Field("email") String email,
                                        @Field("password") String password);

    @FormUrlEncoded
    @POST("register.php")
    Call<ResponseBody> registerUser(@Field("name") String name,
                                           @Field("email") String email,
                                           @Field("password") String password,
                                           @Field("image") String image);

    @FormUrlEncoded
    @POST("userUpdate.php")
    Call<ResponseBody> updateUser(@Field("id") int id,
                                         @Field("name") String name,
                                         @Field("email") String email,
                                         @Field("image") String image);


    @FormUrlEncoded
    @POST("userGetter.php")
    Call<User> getUser(@Field("email") String email);


    @FormUrlEncoded
    @POST("additionMyNewDonation.php")
    Call<ResponseBody> addDonation(@Field("uid") int id,
                                          @Field("amount") int amount,
                                          @Field("date") String date);

    @POST("myDonations.php")
    Call<List<Donation>> getMyDonations(@Query("uid") int id);

    @GET("allDonors.php")
    Call<List<Donation>> getAllDonors();

    @GET("raisedAmount.php")
    Call<List<Donation>> getAllRaisedMoney();


}
