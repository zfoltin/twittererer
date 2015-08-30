package com.crowdmix.twittererer.services;

import com.crowdmix.twittererer.models.TimelineItem;
import com.crowdmix.twittererer.models.User;

import java.util.List;

import rx.Observable;

public interface TwitterService {

    Observable<List<TimelineItem>> getTimelineItems();

    Observable<User> getMyDetails();

    Observable<Boolean> sendTweet(String tweetText);

    User getCurrentUser();

    void setCurrentUser(User user);
}
