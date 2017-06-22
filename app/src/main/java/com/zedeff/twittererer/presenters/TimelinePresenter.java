package com.zedeff.twittererer.presenters;

import com.zedeff.twittererer.R;
import com.zedeff.twittererer.models.TimelineItem;
import com.zedeff.twittererer.services.TwitterService;
import com.zedeff.twittererer.views.TimelineView;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Scheduler;

public class TimelinePresenter {

    private final TwitterService service;
    private final Scheduler scheduler;
    private WeakReference<TimelineView> view;
    private List<TimelineItem> timelineItems;

    @Inject
    public TimelinePresenter(TwitterService service, Scheduler scheduler) {
        this.service = service;
        this.scheduler = scheduler;
    }

    public void initialise(TimelineView view) {
        this.view = new WeakReference<>(view);
        service.getMyDetails()
                .observeOn(scheduler)
                .subscribe(user -> {
                    service.setCurrentUser(user);
                    refreshTweets();
                });
    }

    public void refreshTweets() {
        service.getTimelineItems()
                .observeOn(scheduler)
                .subscribe(timelineItems -> {
                    this.timelineItems = timelineItems;
                    TimelineView view = this.view.get();
                    if (view != null) {
                        if (timelineItems.size() > 0) {
                            view.showTimeline(timelineItems);
                        } else {
                            view.showNoTweets();
                        }
                    }
                });
    }

    public void tweet(String tweetText) {
        timelineItems.add(0, new TimelineItem("0s", tweetText, service.getCurrentUser()));
        view.get().showTimeline(timelineItems);
        service.sendTweet(tweetText)
                .observeOn(scheduler)
                .subscribe(
                        x -> {
                            TimelineView view = this.view.get();
                            if (view != null) {
                                view.showMessage(R.string.alert_tweet_successful);
                            }
                        },
                        e -> {
                            TimelineView view = this.view.get();
                            if (view != null) {
                                view.showMessage(R.string.alert_tweet_failed);
                            }
                        });
    }
}
