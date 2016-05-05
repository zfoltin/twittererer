package com.zedeff.twittererer.utils;

import com.zedeff.twittererer.models.TimelineItem;
import com.zedeff.twittererer.models.User;
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

public final class TimelineConverter {

    public static final String DATE_TIME_FORMAT = "EEE MMM dd HH:mm:ss Z yyyy";

    private TimelineConverter() {}

    public static List<TimelineItem> fromTweets(List<Tweet> tweets, DateTime now) {
        List<TimelineItem> timelineItems = new ArrayList<>();
        for (Tweet t : tweets) {
            User u = new User(t.user.name, t.user.screenName, t.user.profileImageUrl);
            timelineItems.add(new TimelineItem(TimelineConverter.dateToAge(t.createdAt, now), t.text, u));
        }
        return timelineItems;
    }

    private static String dateToAge(String createdAt, DateTime now) {
        if (createdAt == null) {
            return "";
        }

        DateTimeFormatter dtf = DateTimeFormat.forPattern(DATE_TIME_FORMAT);
        DateTime created = dtf.parseDateTime(createdAt);

        if (Seconds.secondsBetween(created, now).getSeconds() < 60) {
            return Seconds.secondsBetween(created, now).getSeconds() + "s";
        } else if (Minutes.minutesBetween(created, now).getMinutes() < 60) {
            return Minutes.minutesBetween(created, now).getMinutes() + "m";
        } else if (Hours.hoursBetween(created, now).getHours() < 24) {
            return Hours.hoursBetween(created, now).getHours() + "h";
        } else {
            return Days.daysBetween(created, now).getDays() + "d";
        }
    }
}
