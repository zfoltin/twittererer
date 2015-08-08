package com.crowdmix.twittererer.services;

import android.util.Log;

import com.crowdmix.twittererer.models.TimelineItem;
import com.crowdmix.twittererer.utils.TimelineConverter;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import rx.Observable;

public class TwitterService extends TwitterApiClient {

    private static final String TAG = TwitterService.class.getSimpleName();

    @Getter
    @Setter
    private com.crowdmix.twittererer.models.User currentUser;

    public TwitterService(Session session) {
        super(session);
    }

    public Observable<List<TimelineItem>> getTimelineItems() {
        return Observable.create(subscriber -> {
            Callback<List<Tweet>> callback = new Callback<List<Tweet>>() {
                @Override
                public void success(Result<List<Tweet>> result) {
                    Log.i(TAG, "Got the tweets, buddy!");
                    subscriber.onNext(TimelineConverter.fromTweets(result.data));
                }

                @Override
                public void failure(TwitterException e) {
                    Log.e(TAG, e.getMessage(), e);
                    subscriber.onError(e);
                }
            };

            Twitter.getApiClient().getStatusesService().homeTimeline(null, null, null, null, null, null, null, callback);
        });
    }

    public Observable<com.crowdmix.twittererer.models.User> getMyDetails() {
        return Observable.create(subscriber -> {
            Callback<User> callback = new Callback<User>() {
                @Override
                public void success(Result<User> result) {
                    Log.i(TAG, "Got your details, pal!");
                    subscriber.onNext(new com.crowdmix.twittererer.models.User(result.data.name, result.data.screenName, result.data.profileImageUrl));
                }

                @Override
                public void failure(TwitterException e) {
                    Log.e(TAG, e.getMessage(), e);
                    subscriber.onError(e);
                }
            };

            getService(UserService.class).show(Twitter.getSessionManager().getActiveSession().getUserId(), callback);
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

            Twitter.getApiClient().getStatusesService().update(tweetText, null, null, null, null, null, null, null, callback);
        });
    }
}
