package com.zedeff.twittererer.test.utils;

import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.zedeff.twittererer.BuildConfig;
import com.zedeff.twittererer.models.TimelineItem;
import com.zedeff.twittererer.utils.TimelineConverter;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 23)
public class TimelineConverterTest {

    private DateTimeFormatter dtf = DateTimeFormat.forPattern(TimelineConverter.DATE_TIME_FORMAT);

    private Tweet createTweet(String createdAt, String text, String name, String screenName, String profileImageUrl) {
        User user = new User(false, null, false, false, null, null, null, 0, false, 0, 0, false, 0, null, false, null, 0, null, name, null, null, null, false, null, profileImageUrl, null, null, null, null, null, false, false, screenName, false, null, 0, null, null, 0, false, null, null);
        return new Tweet(null, createdAt, null, null, null, 0, false, null, 0, null, null, 0, null, 0, null, null, null, false, null, 0, null, null, 0, false, null, null, text, null, false, user, false, null, null, null);
    }

    @Before
    public void setUp() {
        JodaTimeAndroid.init(RuntimeEnvironment.application);
    }

    @Test
    public void fromTweetsMapUserDetails() {
        List<Tweet> tweets = new ArrayList<>();
        tweets.add(createTweet(null, null, "Bob", "bob", "http://some-cloud.com/image_of_bob.png"));

        List<TimelineItem> results = TimelineConverter.fromTweets(tweets, DateTime.now());

        assertThat(results, is(notNullValue()));
        assertThat(results.size(), is(1));

        TimelineItem item = results.get(0);
        assertThat(item.getUser(), is(notNullValue()));
        assertThat(item.getUser().getName(), is(equalTo("Bob")));
        assertThat(item.getUser().getScreenName(), is(equalTo("bob")));
        assertThat(item.getUser().getProfileImageUrl(), is(equalTo("http://some-cloud.com/image_of_bob.png")));
    }

    @Test
    public void fromTweetsMapText() {
        List<Tweet> tweets = new ArrayList<>();
        tweets.add(createTweet(null, "tweet text", null, null, null));

        List<TimelineItem> results = TimelineConverter.fromTweets(tweets, DateTime.now());

        assertThat(results.get(0).getText(), is(equalTo("tweet text")));
    }

    @Test
    public void fromTweetsMapAgeSeconds() {
        DateTime now = DateTime.now();
        DateTime tenSecondsAgo = now.minusSeconds(10);
        List<Tweet> tweets = new ArrayList<>();
        tweets.add(createTweet(dtf.print(tenSecondsAgo), null, null, null, null));

        List<TimelineItem> results = TimelineConverter.fromTweets(tweets, now);

        assertThat(results.get(0).getCreatedAt(), is(equalTo("10s")));
    }

    @Test
    public void fromTweetsMapAgeMinutes() {
        DateTime now = DateTime.now();
        DateTime sixMinutesAgo = now.minusMinutes(6);
        List<Tweet> tweets = new ArrayList<>();
        tweets.add(createTweet(dtf.print(sixMinutesAgo), null, null, null, null));

        List<TimelineItem> results = TimelineConverter.fromTweets(tweets, now);

        assertThat(results.get(0).getCreatedAt(), is(equalTo("6m")));
    }

    @Test
    public void fromTweetsMapAgeHours() {
        DateTime now = DateTime.now();
        DateTime twoHoursAgo = now.minusHours(2);
        List<Tweet> tweets = new ArrayList<>();
        tweets.add(createTweet(dtf.print(twoHoursAgo), null, null, null, null));

        List<TimelineItem> results = TimelineConverter.fromTweets(tweets, now);

        assertThat(results.get(0).getCreatedAt(), is(equalTo("2h")));
    }

    @Test
    public void fromTweetsMapAgeDays() {
        DateTime now = DateTime.now();
        DateTime fourDaysAgo = now.minusDays(4);
        List<Tweet> tweets = new ArrayList<>();
        tweets.add(createTweet(dtf.print(fourDaysAgo), null, null, null, null));

        List<TimelineItem> results = TimelineConverter.fromTweets(tweets, now);

        assertThat(results.get(0).getCreatedAt(), is(equalTo("4d")));
    }

    @Test
    public void fromTweetsMapAgeWithZeroHours() {
        String createdAt = "Tue Dec 15 00:53:04 +0000 2015";
        DateTime fewMinutesAfter = new DateTime(2015, 12, 15, 0, 57, 14);
        List<Tweet> tweets = new ArrayList<>();
        tweets.add(createTweet(createdAt, null, null, null, null));

        List<TimelineItem> results = TimelineConverter.fromTweets(tweets, fewMinutesAfter);

        assertThat(results.get(0).getCreatedAt(), is(equalTo("4m")));
    }

    @Test
    public void invalidDateDoesNotThrow() {
        String createdAt = "XXX Sep 12 11:13:04 +0000 2016";
        DateTime fewMinutesAfter = new DateTime(2016, 9, 12, 11, 15, 14);
        List<Tweet> tweets = new ArrayList<>();
        tweets.add(createTweet(createdAt, null, null, null, null));

        List<TimelineItem> results = TimelineConverter.fromTweets(tweets, fewMinutesAfter);

        assertThat(results.get(0).getCreatedAt(), is(equalTo("")));
    }
}
