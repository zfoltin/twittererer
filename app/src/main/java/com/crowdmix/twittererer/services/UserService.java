package com.crowdmix.twittererer.services;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.models.User;

import retrofit.http.GET;
import retrofit.http.Query;

public interface UserService {

    @GET("/1.1/users/show.json")
    void show(@Query("user_id") long id, Callback<User> cb);
}
