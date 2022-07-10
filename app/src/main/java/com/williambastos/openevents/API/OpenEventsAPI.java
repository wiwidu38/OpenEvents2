package com.williambastos.openevents.API;

import com.williambastos.openevents.model.Event;
import com.williambastos.openevents.model.Login;
import com.williambastos.openevents.model.User;

import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OpenEventsAPI {
    @POST("users/login/")
    Call<Login> loginUser(
            @Body Login userAToken
    );

    @POST("users/")
    Call<User> signUpUser(
            @Body User user
    );

    @GET("events")
    Call<ArrayList<Event>> getEvents(@Header("Authorization") String accessToken);

    @FormUrlEncoded
    @POST("events")
    Call<Event> createEvent(@Header("Authorization") String accessToken,
                            @Field("name") String name,
                            @Field("image") String image,
                            @Field("location") String location,
                            @Field("description") String description,
                            @Field("eventStart_date") String eventStart_date,
                            @Field("eventEnd_date") String eventEnd_date,
                            @Field("n_participators") int n_participators,
                            @Field("type") String type);
    @GET("events/{id}")
    Call<ArrayList<Event>> getEvent(@Header("Authorization") String accessToken, @Path("id") int id);

    @GET("users/search")
    Call<ArrayList<User>> searchUsers(@Header("Authorization") String accessToken, @Query("s") String s);

    @DELETE("events/{id}")
    Call<Event> deleteEvent(@Header("Authorization") String accessToken, @Path("id") int id);

    @GET("users/{id}")
    Call<ArrayList<User>> getUser(@Header("Authorization") String accessToken, @Path("id") int id);

    @GET("users")
    Call<ArrayList<User>> getUsers(@Header("Authorization") String accessToken);

    @GET("users/{id}/statistics")
    Call<User> getStatisticByUser(@Header("Authorization") String accessToken, @Path("id") int id);

    @PUT("users")
    Call<User> updateUser(@Header("authorization") String accessToken, @Body User user);
}
