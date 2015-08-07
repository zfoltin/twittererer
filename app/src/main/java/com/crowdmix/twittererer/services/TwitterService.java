package com.crowdmix.twittererer.services;

import android.util.Log;

import com.crowdmix.twittererer.models.TimelineItem;
import com.crowdmix.twittererer.models.User;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class TwitterService extends TwitterApiClient {

    private static final String TAG = TwitterService.class.getSimpleName();

    public TwitterService(Session session) {
        super(session);
    }

    public Observable<List<TimelineItem>> getTimelineItems() {
        return Observable.create(subscriber -> {
            Callback<List<Tweet>> callback = new Callback<List<Tweet>>() {
                @Override
                public void success(Result<List<Tweet>> result) {
                    Log.i(TAG, "Got the tweets, buddy!");
                    subscriber.onNext(getTimelineItems(result));
                }

                private List<TimelineItem> getTimelineItems(Result<List<Tweet>> result) {
                    List<TimelineItem> timelineItems = new ArrayList<>();
                    for (Tweet t : result.data) {
                        User u = new User(t.user.name, t.user.screenName, t.user.profileImageUrl);
                        timelineItems.add(new TimelineItem(t.createdAt, t.text, u));
                    }
                    return timelineItems;
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
}
