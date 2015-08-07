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

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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
                        timelineItems.add(new TimelineItem(convertToAge(t.createdAt), t.text, u));
                    }
                    return timelineItems;
                }

                private String convertToAge(String createdAt) {
                    DateTimeFormatter dtf = DateTimeFormat.forPattern("EEE MMM dd kk:mm:ss Z yyyy");
                    DateTime created = dtf.parseDateTime(createdAt);
                    DateTime now = new DateTime();

                    if (Seconds.secondsBetween(created, now).getSeconds() < 60) {
                        return Seconds.secondsBetween(created, now).getSeconds() + "s";
                    } else if (Minutes.minutesBetween(created, now).getMinutes() < 60) {
                        return Minutes.minutesBetween(created, new DateTime()).getMinutes() + "m";
                    } else if (Hours.hoursBetween(created, now).getHours() < 24) {
                        return Hours.hoursBetween(created, now).getHours() + "h";
                    } else {
                        return Days.daysBetween(created, now).getDays() + "d";
                    }
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
