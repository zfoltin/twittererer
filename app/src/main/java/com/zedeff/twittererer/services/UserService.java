package com.zedeff.twittererer.services;

import com.twitter.sdk.android.core.models.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface UserService {

    @GET("/1.1/users/show.json")
    Call<User> show(@Query("user_id") long id);
}
