package com.zedeff.twittererer.services;

import android.util.Log;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.zedeff.twittererer.models.TimelineItem;
import com.zedeff.twittererer.utils.TimelineConverter;

import org.joda.time.DateTime;

import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Observable;
import lombok.Getter;
import lombok.Setter;

@Singleton
public class TwitterServiceImpl extends TwitterApiClient implements TwitterService {

    private static final String TAG = TwitterServiceImpl.class.getSimpleName();

    @Getter
    @Setter
    private com.zedeff.twittererer.models.User currentUser;

    public TwitterServiceImpl(TwitterSession session) {
        super(session);
    }

    public Observable<List<TimelineItem>> getTimelineItems() {
        return Observable.create(subscriber -> {
            Callback<List<Tweet>> callback = new Callback<List<Tweet>>() {
                @Override
                public void success(Result<List<Tweet>> result) {
                    Log.i(TAG, "Got the tweets, buddy!");
                    subscriber.onNext(TimelineConverter.fromTweets(result.data, DateTime.now()));
                }

                @Override
                public void failure(TwitterException e) {
                    Log.e(TAG, e.getMessage(), e);
                    subscriber.onError(e);
                }
            };

            getStatusesService().homeTimeline(null, null, null, null, null, null, null).enqueue(callback);
        });
    }

    public Observable<com.zedeff.twittererer.models.User> getMyDetails() {
        return Observable.create(subscriber -> {
            Callback<User> callback = new Callback<User>() {
                @Override
                public void success(Result<User> result) {
                    Log.i(TAG, "Got your details, pal!");
                    subscriber.onNext(new com.zedeff.twittererer.models.User(result.data.name, result.data.screenName, result.data.profileImageUrl));
                }

                @Override
                public void failure(TwitterException e) {
                    Log.e(TAG, e.getMessage(), e);
                    subscriber.onError(e);
                }
            };

            getService(UserService.class).show(TwitterCore.getInstance().getSessionManager().getActiveSession().getUserId()).enqueue(callback);
        });
    }

    public Observable<Boolean> sendTweet(String tweetText) {
        return Observable.create(subscriber -> {
            Callback<Tweet> callback = new Callback<Tweet>() {
                @Override
                public void success(Result<Tweet> result) {
                    Log.i(TAG, "Tweet tweeted");
                    subscriber.onNext(true);
                }

                @Override
                public void failure(TwitterException e) {
                    Log.e(TAG, e.getMessage(), e);
                    subscriber.onError(e);
                }
            };

            getStatusesService().update(tweetText, null, null, null, null, null, null, null, null).enqueue(callback);
        });
    }
}
